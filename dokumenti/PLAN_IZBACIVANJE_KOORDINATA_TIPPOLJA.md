# Plan: izbacivanje pozicionih kolona iz `TipPolja`

**Status: NIJE URAĐENO — ovo je samo plan/dokumentacija, kod nije menjan.**

## Zašto

`TipPolja.pozicijaX/pozicijaY/sirina/visina` (i, granično, `stranica`/`redosledObrade`) se
**nikad ne šalju OCR mikroservisu i OCR ih ne čita**. Stvarne koordinate koje OCR koristi žive
isključivo u `ocr-microservice/sv20-ocr-service/templates/sv20_template.json` (polje
`fields[].coordinates`, u 1024px referentnom sistemu, sa anchor-based poravnanjem —
vidi `ImageProcessor.compute_anchor_offset` / `detect_anchor`). `OcrKlijent.buildMultipartBody()`
šalje samo fajl (bajtove) i `obrazac_id` — nikad `TipPolja` podatke.

Ovo je već i sam autor primetio u `dokumenti/seed_tippolja.sql` (linija 6-7):
> "Koordinate su preuzete iz OCR template-a (1024px sirina). OCR ih koristi iz template.json,
> TipPolja kolone su informativne."

Jedino što se od `TipPolja` stvarno koristi posle OCR obrade je `nazivPolja` — string po kom se
OCR rezultat naknadno uparuje nazad na `TipPolja` red (`SV20ObrazacController.pokreniOcr()`,
poređenje imena case-insensitive).

Zaključak: `pozicijaX`, `pozicijaY`, `sirina`, `visina` su mrtav kod u Java delu sistema. Umesto
da ih ostavimo kao zbunjujući artefakt, izbacujemo ih i jasno dokumentujemo da je
`sv20_template.json` jedini izvor istine za OCR pozicioniranje.

**Python OCR servis (`ocr-microservice/`) se ovom izmenom NE DIRA — on već radi ispravno i
nezavisno od ovih kolona.**

## Šta ostaje, šta se izbacuje iz `TipPolja`

| Kolona | Odluka | Obrazloženje |
|---|---|---|
| `idPolja` | ostaje | PK |
| `nazivPolja` | ostaje | jedino što OCR match stvarno koristi |
| `tipPodatka` | ostaje | koristi se za validaciju vrednosti (regex pravilo zavisi od tipa) |
| `regexValidacija` | ostaje | validacija korigovane vrednosti u `StavkeObrasca` |
| `obaveznoPolje` | ostaje | poslovno pravilo (obrazac ne može biti odobren bez vrednosti) |
| `podrzavaOCR` | ostaje | flag da li se polje uopšte automatski čita ili ručno unosi |
| `pozicijaX` | **izbaci** | nikad se ne čita, OCR koristi template.json |
| `pozicijaY` | **izbaci** | isto |
| `sirina` | **izbaci** | isto |
| `visina` | **izbaci** | isto |
| `stranica` | na tebi — vidi napomenu ispod | |
| `redosledObrade` | na tebi — vidi napomenu ispod | |

**Napomena o `stranica`/`redosledObrade`:** ni njih trenutno OCR ne čita (template.json ima svoj
`page` po polju, a redosled obrade je prosto redosled u JSON nizu) — istog su tipa "mrtav kod" kao
pozicije. Ako ti `stranica` ipak služi kao korisna metapodatak pri pregledu šifarnika ("ovo polje
je sa strane 2 obrasca"), zadrži je — nije vezana za sam OCR mehanizam, samo za organizaciju
podataka. `redosledObrade` nema ni tu upotrebnu vrednost — predlažem da ide zajedno sa pozicijama,
ali odluka je tvoja.

## Fajl-po-fajl šta treba izmeniti (kad odlučiš da uradiš)

### 1. DB šema

- `dokumenti/IMPLEMENTACIONI_PLAN.md`, sekcija 3 (`CREATE TABLE tippolja`, oko linije 219-241):
  - ukloni kolone `pozicijaX`, `pozicijaY`, `sirina`, `visina` (i eventualno `stranica`,
    `redosledObrade` — vidi napomenu gore)
  - ukloni `CONSTRAINT chk_tp_pozicije` u potpunosti (bez pozicija nema šta da se proverava)
  - `CONSTRAINT chk_tp_stranica` briši samo ako ideš i na uklanjanje `stranica`
  - `CONSTRAINT chk_tp_regex` ostaje netaknut
- Na živoj bazi (`bazaocr`): `ALTER TABLE tippolja DROP COLUMN pozicijaX, DROP COLUMN pozicijaY,
  DROP COLUMN sirina, DROP COLUMN visina, DROP CONSTRAINT chk_tp_pozicije;` (tačan sintaks
  zavisi od MySQL verzije — DROP CONSTRAINT za CHECK možda mora kao `DROP CHECK
  chk_tp_pozicije` u MySQL 8). **Napravi backup baze pre ovoga.**

### 2. `0_PsShared/src/domen/TipPolja.java`

- ukloni polja `pozicijaX`, `pozicijaY`, `sirina`, `visina` i njihove getter/setter parove
- ukloni ih iz oba konstruktora (bezargumentni ostaje, veliki konstruktor gubi 4 parametra)
- `vratiKoloneZaUbacivanje()` — ukloni `pozicijaX, pozicijaY, sirina, visina` iz liste kolona
- `vratiVrednostiZaUbacivanje()` — ukloni odgovarajuće vrednosti iz spojenog stringa (pazi na
  zarezе, trenutno je logika grananja "NULL, NULL, NULL, NULL" vs stvarne vrednosti vezana za
  `podrzavaOCR` — vidi trenutni kod oko `pozicije` stringa)
- `vratiObjekatIzRS()` — ukloni `tp.setPozicijaX(...)` itd.
- `vratiVrednostiZaIzmenu()` — isto, ukloni iz SET klauzule (i grananje za NULL kad OCR isključen)
- `hashCode()`/`equals()` — TRENUTNO koriste `pozicijaX`, `pozicijaY`, `sirina`, `visina` kao deo
  poslovnog ključa (vidi komentar u kodu: "Mora da bude konzistentan sa poljima koja equals()
  koristi (pozicija/dimenzije/OCR/regex/tip)"). Kad ih izbaciš, moraćeš da smisliš novi poslovni
  ključ za `equals()`/`hashCode()` — verovatno `nazivPolja` (uz proveru da li je već UNIQUE na
  bazi; trenutno nije, samo je `NOT NULL`).

### 3. `PsKlijent/src/forme/TipPoljaForma.java`

- ukloni polja `txtPozicijaX`, `txtPozicijaY`, `txtSirina`, `txtVisina` i njihove pripadajuće
  gettere (`getTxtPozicijaX()` itd.)
- redizajniraj `formPanel` layout (trenutno `addRow2` raspoređuje "Regex + Pozicija X" i
  "Pozicija Y + Širina" u redovima 2 i 4, "Visina + Stranica" u redu 6) — bez 4 uklonjena polja,
  ostaje: naziv, tip podatka, regex, stranica (ako ostaje), redosled (ako ostaje), OCR checkbox,
  obavezno checkbox — može stati u 2-3 reda umesto 5

### 4. `PsKlijent/src/kontroleri/TipPoljaController.java`

- `prikupiPodatke()` — ukloni `try { pozX = Integer.parseInt(...) ... }` blok i
  `tp.setPozicijaX/Y/Sirina/Visina` pozive
- `ocisti()` — ukloni `setText("0")` pozive za ta 4 polja
- `popuniFormu()` — ova metoda VEĆ ne popunjava pozicije/regex kad se red selektuje iz tabele
  (postojeći, odvojen bag — "Za sada popunjavamo iz tabele (pojednostavljena verzija)"). Kad
  polja nestanu, taj deo bug-a prosto nestaje sam od sebe, ništa dodatno za fix ovde.
- `ucitajPodatke()` — kolone u tabeli (`{"ID", "Naziv", "Tip", "Stranica", "Redosled", "OCR",
  "Obavezno"}`) već ne prikazuju pozicije, ne treba menjati osim ako ideš i na `stranica`/
  `redosledObrade`

### 5. Seed/fix SQL skripte

- `dokumenti/seed_tippolja.sql` — svi `INSERT INTO tippolja (...)` redovi imaju
  `pozicijaX, pozicijaY, sirina, visina` u koloni-listi i po 4 numerička literala u `VALUES`;
  ukloniti iz oba mesta u svih ~35 INSERT-a
- `dokumenti/fix_tippolja_missing.sql` — isto, manji broj redova

### 6. Ostala dokumentacija (ručno, van koda)

- `dokumenti/VukasinLukicDokumentacija.pdf` — konceptualni model/ER dijagram sadrži ove kolone,
  treba ažurirati ručno (nije markdown, ne mogu automatski)
- `dokumenti/CODEBASE.md` — ako pominje `TipPolja` šemu, uskladiti opis

## Preporučen redosled izvođenja (kad krene implementacija)

1. Backup baze
2. Java izmene (domen → forma → kontroler), rebuild, testiraj CRUD na Tipovi polja formi
3. Tek onda ALTER TABLE na bazi (posle Java izmena, da broker ne šalje SQL koji referencira
   kolone koje više ne postoje pre nego što je Java kod spreman)
4. Ažuriraj seed/fix SQL skripte i ponovo ih pusti na čistoj bazi ako se radi reset
5. Ažuriraj `IMPLEMENTACIONI_PLAN.md` šemu i PDF dokumentaciju
6. Regresioni test: kreiraj SV-20 obrazac, pokreni OCR, proveri da se stavke i dalje ispravno
   uparuju po `nazivPolja` (ovaj deo se ne menja, ali vredi potvrditi da ništa nije slučajno
   pukotinu napravilo)
