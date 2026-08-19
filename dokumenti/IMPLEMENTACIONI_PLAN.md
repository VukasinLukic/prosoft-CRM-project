# V2 – Implementacioni plan: ŠV-20 Sistem (Projektovanje softvera)

Autor: Vukašin Lukić 2023/0342 · Mentor: prof. dr Siniša Vlajić
Cilj: **ručno** (bez AI-generisanog koda) otkucati novu verziju aplikacije iz `v1AplikacijeGotov`,
1:1 usklađenu sa `VukasinLukicDokumentacija.pdf`, uz poštovanje svih pravila iz
`ProjektovanjeSoftveraNotes.txt`, i završiti dokumentaciju do kraja. Rok: **15 dana**.

---

## 0. Stanje stvari — pre nego što počneš da kucaš

### 0.1 Dokumentacija (PDF) — šta postoji, šta ne postoji
Pročitao sam ceo `VukasinLukicDokumentacija.pdf` (40 strana). Stanje:

| Poglavlje | Status |
|---|---|
| 1. Uvod | **Prazno** (samo naslov, str. 3) |
| 2. Prikupljanje korisničkih zahteva (2.1 Vebalni opis, 2.2 Slučajevi korišćenja) | Gotovo |
| 3. Analiza (3.1 SO, 3.2 DS, 3.3 Ugovori, 3.4 Konceptualni model, 3.5 Relacioni model, 3.6 Ograničenja) | Gotovo |
| 4. Projektovanje (4.1 UI, 4.2 Aplikaciona logika, 4.3 Skladište podataka) | **Ne postoji u PDF-u** |
| 5. Implementacija | **Ne postoji** |
| 6. Testiranje | **Ne postoji** |
| 7. Zaključak | **Ne postoji** |
| Literatura | **Ne postoji** |

Znači: dokumentacija ti je **gotova do kraja poglavlja 3 (analiza)** — to je specifikacija koju kod mora
1:1 da prati. Poglavlja 4–7 + Uvod moraš napisati u ova 2 nedelje, paralelno sa kodom (raspored ispod).

Kao stilski/strukturni referentni okvir za poglavlja 4–7 pogledao sam i `primerOdNekeOsobeRADA.pdf`
(tuđi završni rad, isti mentor) — struktura 4.x/5/6/7 tamo je uporediva, ali je to završni rad (drugačiji
format od tvog seminarskog), pa ga koristi samo kao orijentir za "kako izgleda poglavlje", ne kao šablon
koji kopiraš.

`Odgovori na pitanja za dokumentaciju.pdf` je tvoja **teorijska špil-karta za odbranu** — sadrži tačne
definicije (APUSO/APSO/ANSO/SO/IA, kompozicija/asocijacija/agregacija/generalizacija, referencijalni i
entitetski integritet...). Nauči ovo napamet doslovno — ispitivači pitaju baš ovako formulisano.

### 0.2 Kod (v1AplikacijeGotov) — šta je dobro, šta MORA da se popravi
v1 je **kompletno funkcionalna** aplikacija (sve GUI forme i kontroleri su popunjeni, stari
`planImplementiranZaV1aplikacije.md` koji kaže da su forme prazne je zastareo/netačan). To je tvoja
referenca za "kako izgleda kod koji radi" — čitaš ga, razumeš, pa **rukom** kucaš svoju verziju u
`V2App`. Ne kopiraj/nalepi.

Arhitektura u v1 **zadovoljava sva 4 MUST-HAVE pravila** iz beležaka:

| Pravilo (beleške) | Gde je u v1 | Napomena |
|---|---|---|
| MVC | Forma=View, Kontroler(klijent)=Controller, domenske klase+server=Model | ok |
| Generički broker sa opštim domenskim objektom | `Repository<T>` → `DbRepository<T>` → `DbRepositoryGeneric implements DbRepository<ApstraktniDomenskiObjekat>` | ok |
| Apstraktna sistemska operacija | `ApstraktnaGenerickaOperacija` | ok |
| Template method | `izvrsi()` je `final`, poziva `preduslovi()`→`zapocniTransakciju()`→`izvrsiOperaciju()`→`potvrdiTransakciju()/ponistiTransakciju()`→`ugasiKonekciju()` | ok, ovo je tačno ono što ispitivač testira kad traži da "obrišeš `extends`" |

Ali sam našao **3 konkretna problema** koje V2 mora da ispravi (v1 ih nema rešene ili ih rešava pogrešno):

1. **Nema sprečavanja duple prijave.** `LoginOperacija` samo proverava korisničko ime/šifru — ništa ne
   sprečava da se isti `korisnickoIme` prijavi sa dva različita klijenta istovremeno. Beleške (red 10)
   ovo eksplicitno traže: *"sprečiti duplu prijavu istog korisnika, validacije i na klijentu i na serveru"*.
   Ovo mora postojati u V2 (vidi 2.1 ispod).

2. **DB konekcija nije thread-safe.** `DbConnectionFactory` je singleton koji drži **jednu jedinu**
   `Connection` za ceo server. Svaka sistemska operacija (u bilo kojoj od N niti — po jedna nit po
   klijentu) radi `connect()` → radi upit → `commit()/rollback()` → `disconnect()` (što **zatvara** tu
   deljenu konekciju!) na **istom** objektu. Ako dva klijenta rade nešto u isto vreme, jedna nit će
   zatvoriti konekciju dok je druga koristi → race condition / `SQLException: connection closed`.
   Ovo je tačno scenario koji ispitivač pита kroz pitanje *"Kako se rešava problem deljenih resursa kod
   niti — sinhronizacija?"* — u v1 trenutno **nije rešeno ispravno**. Vidi popravku u 2.2.

3. **Hardkodovana apsolutna putanja.** `Konfiguracija.java` čita
   `D:\Downloads\Prosoft\SERVER\src\konfiguracija\dbconfig.properties` — fajl sa tuđeg računara, radiće
   samo slučajno. U V2 mora da čita properties fajl relativno (classpath), da radi na bilo kom računaru
   (uključujući onaj na odbrani).

Sve ostalo (paket struktura, imenovanje, tok kroz `Controller`/`ObradaKlijentskihZahteva`) — kopiraj
**obrazac**, ne fajlove. Otvori v1 fajl pored praznog V2 fajla, pročitaj ga, zatvori ga, pa otkucaj svoju
verziju iz glave. To je jedini način da zaista "sve razumeš i sve znaš kako radi" za odbranu.

---

## 1. Struktura V2App projekta

Isti trosmerni raspored kao v1 (klijent-server-baza sa soketima i nitima, generički DB broker):

```
V2App\
├── ZAJEDNICKI\src\
│   ├── domen\            (ApstraktniDomenskiObjekat + 8 domenskih klasa + 4 enuma)
│   └── komunikacija\      (Operacija enum, Zahtev, Odgovor, Posiljac, Primalac)
├── SERVER\src\
│   ├── konfiguracija\     (Konfiguracija — ISPRAVLJENO čitanje properties-a)
│   ├── repository\        (Repository<T>) i repository\db\ (DbRepository<T>, DbRepositoryGeneric, DbConnectionFactory — ISPRAVLJENO)
│   ├── operacije\         (ApstraktnaGenerickaOperacija + po paket za svaki koncept)
│   ├── controller\        (Controller singleton + registar ulogovanih — NOVO)
│   ├── niti\              (ObradaKlijentskihZahteva extends Thread)
│   ├── server\            (Server — ServerSocket)
│   └── forme\             (ServerskaForma, FormaKonfiguracijaBaza/Port)
└── KLIJENT\src\
    ├── forme\, kontroleri\, komunikacija\, cordinator\, main\
```

Baza: MySQL, ista šema `bazaocr` (vidi SQL u sekciji 3).

---

## 2. Dve stvari koje v1 nema, a moraš ih dizajnirati sam (novo, ne kopiraš od v1)

### 2.1 Sprečavanje duple prijave (beleške, red 10)

Ideja: `Controller` drži **statički, sinhronizovan** skup korisničkih imena koja su trenutno prijavljena.
`LoginOperacija` prvo proveri kredencijale (kao sada), pa **pre** nego što vrati uspeh proveri da
korisnik već nije u skupu — ako jeste, baci grešku. Kada se klijent diskonektuje (ili eksplicitno odjavi),
`ObradaKlijentskihZahteva` mora da ukloni to korisničko ime iz skupa (inače se korisnik nikad više ne bi
mogao ulogovati posle gašenja klijenta bez čistog logout-a — obavezno hendluj i granu kad se socket
prekine bez `ODJAVI_ZAPOSLENOG` poziva, u `catch`/`finally` bloku niti).

Ovo je ujedno i tvoj **konkretan odgovor na pitanje o sinhronizaciji niti** — ovde imaš pravi deljeni
resurs (skup ulogovanih korisnika) kome pristupa više niti istovremeno.

Skica (kucaš sam, ovo je samo ideja — ne kopiraj doslovno):

```java
// u Controller-u (ili posebnoj klasi RegistarPrijavljenih)
private static final Set<String> ulogovaniKorisnici =
        Collections.synchronizedSet(new HashSet<>());

// u LoginOperaciji, POSLE provere kredencijala:
synchronized (ulogovaniKorisnici) {
    if (ulogovaniKorisnici.contains(zf.getKorisnickoIme())) {
        throw new Exception("Korisnik je već prijavljen na sistem!");
    }
    ulogovaniKorisnici.add(zf.getKorisnickoIme());
}
```

Na serveru dodaj i `OdjaviZaposlenogOperacija` (ili samo metodu u `Controller`-u pozvanu iz
`ObradaKlijentskihZahteva.prekini()`/`catch` grane) koja radi `ulogovaniKorisnici.remove(korisnickoIme)`.
Na klijentu: `LoginController` pre slanja zahteva može lokalno da onemogući drugi klik na "Prijavi se"
dok traje poziv (osnovna klijentska validacija — dovoljno je da dugme bude disable-ovano tokom poziva
i da se prazna polja provere pre slanja, kao što v1 već radi).

Ovo direktno omogućava i pitanje sa liste ("prikaz svih trenutno ulogovanih korisnika, da vidi kako se
barata nitima") — dodaj laku sistemsku operaciju `VratiListuUlogovanih()` koja vraća sadržaj tog seta.

### 2.2 Thread-safe DB konekcija

Najjednostavnija ispravka koja se uklapa u postojeći template method (`connect()`/`disconnect()` se već
zovu po operaciji): **ne drži jednu deljenu `Connection`** u singletonu. Umesto toga, `DbConnectionFactory`
otvara **novu konekciju za svaki poziv** `connect()`/`getKonekcija()` (i vraća je zatvorenu na kraju
operacije preko `disconnect()`). Pošto je `ApstraktnaGenerickaOperacija.izvrsi()` već napravljen da radi
`connect()` → rad → `commit/rollback` → `disconnect()` u `finally`, dovoljno je da `DbConnectionFactory`
prestane da bude "jedna konekcija za sve" i postane fabrika koja pravi novu konekciju na zahtev (nema više
`if (konekcija == null) ...` deljenog polja). Time je svaka nit potpuno izolovana — nema deljenog
mutable stanja, pa nema race condition-a. Ovo je i lakše da objasniš na odbrani od pravljenja pool-a.

Kad objašnjavaš na odbrani: reci da resurs "baza podataka" nije deljen mutable objekat u JVM-u (svaka nit
dobija svoju `Connection`), dok resurs "skup ulogovanih korisnika" **jeste** deljen i zato je eksplicitno
sinhronizovan (`Collections.synchronizedSet` + `synchronized` blok pri proveri-pa-dodavanju, jer to mora
biti atomska operacija — check-then-act).

### 2.3 Ispravka Konfiguracija.java

Umesto `new FileInputStream("D:\\...")`, čitaj fajl preko class-loader-a (properties fajl stavi u isti
paket kao `Konfiguracija.class`, npr. `SERVER/src/konfiguracija/dbconfig.properties`):

```java
Konfiguracija.class.getResourceAsStream("dbconfig.properties")
```

Radiće nezavisno od toga gde je projekat na disku (bitno i za odbranu — pokreće se sa drugog računara).

---

## 3. Baza podataka — kompletna SQL skripta (izvedena 1:1 iz 3.5 i 3.6 dokumentacije)

Ovo je do sada nedostajalo i u v1 (nema `.sql` fajla u projektu). Otkucaj ovo u `baza.sql`, pokreni ga PRE
bilo kakvog Java koda (dan 1). Napomena: neka "složena međuzavisna" ograničenja iz tabele 3.6 su
**cross-table** (npr. "školska godina >= godina iz indeksa studenta", "zbir uspešnih+neuspešnih stavki
<= ukupan broj stavki") — MySQL `CHECK` **ne može** da referencira drugu tabelu/red, pa se ta pravila NE
pišu u DDL, nego se proveravaju u `preduslovi()` metodi odgovarajuće sistemske operacije (to je uostalom
tačno svrha `preduslovi()` u template method-u — savršeno se poklapa sa pojmom "preduslovi ugovora" iz
teorije, tačka 19–20 iz `Odgovori na pitanja...pdf`). U skripti su ta mesta označena komentarom.

```sql
CREATE DATABASE IF NOT EXISTS bazaocr CHARACTER SET utf8mb4;
USE bazaocr;

-- 1. ZaposleniFakulteta (nezavisan koncept)
CREATE TABLE zaposlenifakulteta (
    idZaposlenog   INT AUTO_INCREMENT PRIMARY KEY,
    ime            VARCHAR(50)  NOT NULL,
    prezime        VARCHAR(50)  NOT NULL,
    korisnickoIme  VARCHAR(50)  NOT NULL UNIQUE,
    email          VARCHAR(100) NOT NULL UNIQUE,
    sifra          VARCHAR(255) NOT NULL,
    CONSTRAINT chk_zf_korisnickoime
        CHECK (korisnickoIme <> ime AND korisnickoIme <> prezime AND LENGTH(korisnickoIme) >= 5),
    CONSTRAINT chk_zf_email CHECK (email LIKE '%@%'),
    CONSTRAINT chk_zf_sifra CHECK (LENGTH(sifra) >= 8)
);

-- 2. StudijskiProgram (nezavisan koncept, šifarnik)
CREATE TABLE studijskiprogram (
    idStudProgram INT AUTO_INCREMENT PRIMARY KEY,
    naziv         VARCHAR(100) NOT NULL,
    oznaka        VARCHAR(10)  NOT NULL UNIQUE,
    stepenStudija ENUM('OAS','MAS','DAS') NOT NULL,
    CONSTRAINT chk_sp_oznaka CHECK (oznaka REGEXP '^[A-Z]+$')
    -- "dužina oznake <= pola dužine naziva" -> proveri u preduslovi() (poredi 2 atributa iste tabele,
    -- može teorijski i kao CHECK jer su u istom redu, ali je čitljivije u Validatoru)
);

-- 3. TerminDezurstva (nezavisan koncept, šifarnik)
CREATE TABLE termindezurstva (
    idTerminDezurstva INT AUTO_INCREMENT PRIMARY KEY,
    tipTermina ENUM('PRVA_SMENA','DRUGA_SMENA','TRECA_SMENA') NOT NULL,
    kancelarija VARCHAR(50) NOT NULL
);

-- 4. TipPolja (nezavisan koncept, šifarnik)
CREATE TABLE tippolja (
    idPolja         INT AUTO_INCREMENT PRIMARY KEY,
    nazivPolja      VARCHAR(100) NOT NULL,
    tipPodatka      ENUM('TEXT','NUMERIC','ALPHANUMERIC','DATE','BOOLEAN') NOT NULL,
    regexValidacija VARCHAR(255) NULL,
    pozicijaX       INT NULL,
    pozicijaY       INT NULL,
    sirina          INT NULL,
    visina          INT NULL,
    stranica        INT NOT NULL DEFAULT 1,
    redosledObrade  INT NULL,
    podrzavaOCR     BOOLEAN NOT NULL DEFAULT TRUE,
    obaveznoPolje   BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT chk_tp_stranica CHECK (stranica >= 1),
    CONSTRAINT chk_tp_pozicije CHECK (
        (podrzavaOCR = TRUE  AND pozicijaX IS NOT NULL AND pozicijaY IS NOT NULL AND sirina > 0 AND visina > 0)
        OR
        (podrzavaOCR = FALSE AND pozicijaX IS NULL AND pozicijaY IS NULL AND sirina IS NULL AND visina IS NULL)
    ),
    CONSTRAINT chk_tp_regex CHECK (tipPodatka <> 'NUMERIC' OR regexValidacija REGEXP '^[0-9]+$')
    -- "ako je obaveznoPolje=TRUE i obrazac.status='Odobren' onda stavka mora imati vrednost"
    -- -> cross-table, proveri u preduslovi() operacije PromeniSV20Obrazac / PromeniStavkuObrasca
);

-- 5. Student (zavisan od StudijskiProgram - agregacija)
CREATE TABLE student (
    indeks            VARCHAR(9)  PRIMARY KEY,      -- format gggg/bbbb
    jmbg              VARCHAR(13) NOT NULL UNIQUE,
    ime               VARCHAR(50) NOT NULL,
    prezime           VARCHAR(50) NOT NULL,
    mestoRodjenja     VARCHAR(100) NOT NULL,
    adresaStanovanja  VARCHAR(200) NOT NULL,
    idStudProgram     INT NOT NULL,
    CONSTRAINT chk_student_indeks CHECK (indeks REGEXP '^[0-9]{4}/[0-9]{4}$'),
    CONSTRAINT chk_student_jmbg CHECK (jmbg REGEXP '^[0-9]{13}$'),
    CONSTRAINT fk_student_sp FOREIGN KEY (idStudProgram) REFERENCES studijskiprogram(idStudProgram)
        ON UPDATE CASCADE ON DELETE RESTRICT
    -- kontrolni broj JMBG-a (modul 11) i "godina upisa - godina rodjenja iz JMBG-a > 16"
    -- -> proveri u preduslovi() operacije KreirajStudenta / PromeniStudenta
);

-- 6. ZaposleniFakulteta <-> TerminDezurstva (asocijativna klasa: Zaposleni-Termin)
CREATE TABLE zaposleni_termin (
    datum              DATE NOT NULL,
    idZaposlenog       INT  NOT NULL,
    idTerminDezurstva  INT  NOT NULL,
    brojSati           INT  NOT NULL DEFAULT 8,
    vanredan           BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (datum, idZaposlenog, idTerminDezurstva),
    CONSTRAINT chk_zt_brojsati CHECK (brojSati > 0 AND (vanredan = FALSE OR brojSati <= 4)),
    CONSTRAINT fk_zt_zaposleni FOREIGN KEY (idZaposlenog) REFERENCES zaposlenifakulteta(idZaposlenog)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_zt_termin FOREIGN KEY (idTerminDezurstva) REFERENCES termindezurstva(idTerminDezurstva)
        ON UPDATE CASCADE ON DELETE RESTRICT
);

-- 7. SV20Obrazac (zavisan od Student i ZaposleniFakulteta - agregacija)
CREATE TABLE sv20obrazac (
    idObrazac            INT AUTO_INCREMENT PRIMARY KEY,
    datumUnosa           DATE NOT NULL,
    skolskaGodina        INT  NOT NULL,
    semestar             INT  NOT NULL,
    status               ENUM('PODNET','U_OBRADI','VRACEN_NA_KOREKCIJU','ODOBREN','ODBIJEN')
                         NOT NULL DEFAULT 'PODNET',
    putanjaFajla         VARCHAR(500) NOT NULL,
    ocrIzvrseno          BOOLEAN NOT NULL DEFAULT FALSE,
    brojUspesnihStavki   INT NOT NULL DEFAULT 0,
    brojNeuspesnihStavki INT NOT NULL DEFAULT 0,
    idZaposlenog         INT NOT NULL,
    indeks               VARCHAR(9) NOT NULL,
    CONSTRAINT chk_obr_semestar CHECK (semestar > 0),
    CONSTRAINT chk_obr_brojevi CHECK (brojUspesnihStavki >= 0 AND brojNeuspesnihStavki >= 0
        AND (ocrIzvrseno = FALSE OR (brojUspesnihStavki + brojNeuspesnihStavki) > 0)),
    CONSTRAINT uq_obrazac UNIQUE (indeks, skolskaGodina, semestar),
    CONSTRAINT fk_obr_zaposleni FOREIGN KEY (idZaposlenog) REFERENCES zaposlenifakulteta(idZaposlenog)
        ON UPDATE RESTRICT ON DELETE RESTRICT,
    CONSTRAINT fk_obr_student FOREIGN KEY (indeks) REFERENCES student(indeks)
        ON UPDATE RESTRICT ON DELETE RESTRICT
    -- "datumUnosa <= danas", "YEAR(datumUnosa) >= skolskaGodina",
    -- "skolskaGodina >= godina upisa iz indeksa" -> proveri u preduslovi()
);

-- 8. StavkeObrasca (kompozicija - zavisi isključivo od SV20Obrazac; slab objekat)
CREATE TABLE stavkeobrasca (
    idObrazac          INT NOT NULL,
    idStavke           INT NOT NULL,
    ocrVrednost        VARCHAR(500) NULL,
    korigovanaVrednost VARCHAR(500) NULL,
    nivoPodudarnosti   DOUBLE NOT NULL DEFAULT 0,
    ocrUspesno         BOOLEAN NOT NULL DEFAULT FALSE,
    idPolja            INT NOT NULL,
    PRIMARY KEY (idObrazac, idStavke),
    CONSTRAINT chk_st_podudarnost CHECK (nivoPodudarnosti >= 0 AND nivoPodudarnosti <= 100
        AND (ocrUspesno = FALSE OR nivoPodudarnosti > 0)),
    CONSTRAINT fk_st_obrazac FOREIGN KEY (idObrazac) REFERENCES sv20obrazac(idObrazac)
        ON UPDATE CASCADE ON DELETE RESTRICT,      -- dokumentacija (3.6, tabela 6/7): DELETE RESTRICTED, ne CASCADE!
    CONSTRAINT fk_st_polje FOREIGN KEY (idPolja) REFERENCES tippolja(idPolja)
        ON UPDATE CASCADE ON DELETE RESTRICT
);
```

> **Pažnja, razlika u odnosu na stari plan:** stari `planImplementiranZaV1aplikacije.md` (FAZA 1) je
> stavio `ON DELETE CASCADE` između `sv20obrazac` i `stavkeobrasca`. Dokumentacija (tabela 3.6, red 6 i
> 7) eksplicitno kaže **DELETE RESTRICTED** za taj par. Prati dokumentaciju, ne stari plan — ovo je tačno
> razlog zašto je user tražio novi, precizniji plan.

Ubaci i par test redova (`INSERT`) za svaku šifarničku tabelu (StudijskiProgram, TerminDezurstva,
TipPolja) da imaš odmah nešto za `vratiSve*` pozive kad podigneš klijenta.

---

## 4. "Kreiraj" vs "Ubaci" — prati dokumentaciju doslovno, ali implementiraj pametno (beleške, red 20–26)

### 4.0 Problem, jednostavno objašnjeno

Sinišin generator za SK tipa "Kreiraj X" generiše tok: (1) sistem odmah napravi red u bazi, (2) tek onda
se otvori forma, (3) korisnik unosi podatke, (4) "Promeni" zapravo radi UPDATE tog istog reda. Problem:
red **ne može biti prazan** ako imaš NOT NULL / PRIMARY KEY / UNIQUE ograničenja — npr. kod `Student`
primarni ključ (`indeks`) kuca korisnik, ne može da postoji "prazan" Student bez indeksa; kod
`ZaposleniFakulteta`, `korisnickoIme`/`email` su UNIQUE pa dva "prazna" reda odmah pucaju na constraint.

**Odluka (potvrđeno):** dokumentaciju ne diramo ni u jednom redu. U kodu se "Kreiraj" i "Promeni" i dalje
zovu kao dve odvojene operacije — tačno kako doc traži — ali se **ne lančaju nužno u istom kliku** za
svaki koncept. Pošto sam prošao kroz `3.6` (ograničenja) tabelu po tabelu, ispada da entiteti prirodno
padaju u dve grupe:

**Grupa 1 — nema odloživih polja (Student, ZaposleniFakulteta, StudijskiProgram, ZaposleniTermin).**
Kod ovih, u SQL šemi iz sekcije 3, **sve kolone su NOT NULL** (nema ijedne koja bi mogla ostati prazna
posle "kreiraj" pa se popuniti kasnije). Zato tu dvofazni tok iz teksta SK-a ("kreira" pa "unosi pa
zapamti") implementiraš ovako: `KreirajX` se poziva **tek kada korisnik popuni CEO formular** za NOVI
zapis i klikne "Dodaj/Sačuvaj" — to je pravi INSERT sa kompletnim, validiranim podacima, nikad prazan
red. `PromeniX` se i dalje poziva — ali kao **posebna, kasnija akcija**: kad korisnik iz tabele selektuje
POSTOJEĆI zapis, izmeni nešto i klikne "Sačuvaj izmene". Obe operacije postoje, obe se zovu tačno kako
piše u Tabeli 4 i ugovorima — samo što "Kreiraj → odmah pa Promeni" nije bukvalni redosled dva mrežna
poziva u istom toku, nego dva različita korisnička scenarija (novi zapis / izmena postojećeg), što je
uostalom i standardni CRUD obrazac.

**Grupa 2 — ima realno odloživih polja (SV20Obrazac, TipPolja, StavkeObrasca).** Kod ovih baza ima
nullable kolone koje zavise od **kasnijeg** događaja:
- `SV20Obrazac`: `ocrIzvrseno`, `brojUspesnihStavki`, `brojNeuspesnihStavki`, `status` prirodno se
  popunjavaju/menjaju **posle** što OCR obradi fajl (a to se ne dešava istog trenutka kad korisnik
  pošalje formu).
- `TipPolja`: `regexValidacija`, `pozicijaX/Y`, `sirina`, `visina`, `redosledObrade` su opcioni i imaju
  smisla samo ako `podrzavaOCR=true` — mogu se dodati/doraditi naknadno.
- `StavkeObrasca`: `ocrVrednost`, `korigovanaVrednost`, `nivoPodudarnosti` po prirodi stvari dolaze tek
  kad OCR (ili ručna korekcija) obradi tu stavku — u trenutku kreiranja stavke to još ne postoji.

Za ovu grupu, dvofazni tok ima **stvaran, opravdan smisao** i implementiraš ga baš onako kako SK opisuje:
`KreirajX` upiše ono što je poznato u trenutku kreiranja (obavezna polja + podrazumevane vrednosti za
ostalo), `PromeniX` kasnije dopunjava OCR-zavisna/opciona polja.

**TerminDezurstva** (SK25) ostaje kako jeste — već je **jednofazno Ubaci**, ništa se ne menja (sve kolone
su NOT NULL sa podrazumevanim vrednostima, isti slučaj kao Grupa 1, samo što je ovde SK već preimenovan).

### 4.1 Pregled po konceptu

| Koncept | SK | Kako implementiraš `Kreiraj`/`Promeni` |
|---|---|---|
| Student | SK5 Kreiraj | `KreirajStudenta` = pravi INSERT, poziva se tek kad je cela forma popunjena za NOVOG studenta. `PromeniStudenta` = posebna akcija nad POSTOJEĆIM studentom. |
| ZaposleniFakulteta | SK10 Kreiraj | isto — `KreirajZaposlenogOperacija` sa kompletnim podacima za novog zaposlenog; `PromeniZaposlenogOperacija` za izmenu postojećeg. |
| StudijskiProgram | SK18 Kreiraj | isto. |
| ZaposleniTermin | SK29 Kreiraj | isto. |
| SV20Obrazac | SK1 Kreiraj | **pravi dvofazni tok**: `KreirajSV20Obrazac` upiše obrazac (student, zaposleni, godina, semestar, putanja fajla — sve što korisnik zna odmah); `PromeniSV20Obrazac` kasnije upisuje/menja OCR-zavisna polja (status, ocrIzvrseno, brojUspesnih/Neuspesnih). |
| TipPolja | SK14 Kreiraj | dvofazno ima smisla ako `podrzavaOCR=true` pa se pozicione koordinate dodaju/koriguju naknadno preko `PromeniTipPolja`; inače (bez OCR-a) ponašaj se kao Grupa 1. |
| StavkeObrasca | SK26 Kreiraj | pravi dvofazni tok: `KreirajStavkuObrasca` poveže stavku sa obrascem i poljem (bez OCR vrednosti); `PromeniStavkuObrasca` upisuje `ocrVrednost`/`korigovanaVrednost`/`nivoPodudarnosti` kad se pojave. |
| **TerminDezurstva** | SK25 **Ubaci** | jednofazno, bez izmena — `UbaciTerminDezurstva` upisuje kompletan red odjednom. |

Kad te na odbrani pitaju "zašto Kreiraj pa Promeni" (tačno pitanje koje je Bobi postavljao, beleške red
25) — objasni baš ovu podelu: kod nekih koncepata Promeni je kasnija, nezavisna izmena postojećeg zapisa;
kod SV20Obrazac/TipPolja/StavkeObrasca je Promeni stvarni drugi korak istog radnog toka jer zavisi od
OCR obrade koja se dešava posle. Ovo je jače i tačnije objašnjenje od "generator to tako traži" — pokazuje
da razumeš **zašto**, ne samo **da** kod tako radi.

---

## 5. Redosled kucanja (zavisnosti prvo, ponavljajući obrazac posle)

1. `ApstraktniDomenskiObjekat` (interfejs) + 8 domenskih klasa + 4 enuma (ZAJEDNICKI)
2. `Operacija` enum (uskladi tačno sa 48 signala iz Tabele 5, str. 20 dokumentacije — prekopiraj/otkucaj
   listu signala, oduzmi one koje ne koristiš, ne izmišljaj nove) + `Zahtev`/`Odgovor`/`Posiljac`/`Primalac`
3. `Konfiguracija` (ispravljena) + `DbConnectionFactory` (ispravljena) + `Repository<T>`/`DbRepository<T>`/`DbRepositoryGeneric`
4. `ApstraktnaGenerickaOperacija` (template method) — najvažnija klasa u projektu, ne žuri
5. `LoginOperacija` + registar ulogovanih (2.1) + `Controller` (login deo) + `Server` + `ObradaKlijentskihZahteva`
6. Prvi kompletan vertikalni presek: **StudijskiProgram** (najprostiji šifarnik) server+klijent+forma,
   da uvežbaš ceo tok pre složenijih entiteta
7. TerminDezurstva (Ubaci obrazac) + TipPolja
8. Student + ZaposleniFakulteta (dvofazni Kreiraj/Promeni)
9. SV20Obrazac (najsloženiji — FK na oba, dvofazno, JFileChooser za putanjaFajla)
10. StavkeObrasca (ugnježdeno u SV20ObrazacForma) + ZaposleniTermin
11. GlavnaForma + meni (tačno struktura sa str. 5 dokumentacije: Dokumenti / Pružalac usluge / Primalac
    usluge / Šifarnici / Podešavanja / O programu) + `Cordinator`

---

## 6. OCR mikroservis (Python) — minimalan pristup, niska prioritet

Rekao si da OCR deo (Python, `ocr-microservice/`) potpuno ignorišemo za potrebe predmeta — Java strana ga
tretira kao crnu kutiju. Za V2:

- Server (ili klijent, svejedno) posle uploada fajla (`putanjaFajla`) pozove Python servis preko običnog
  HTTP poziva (`HttpURLConnection` ili `java.net.http.HttpClient`, sinhrono je dovoljno) i dobije nazad
  JSON sa ekstrahovanim vrednostima po polju.
- Rezultat se mapira na `StavkeObrasca` (ocrVrednost, nivoPodudarnosti, ocrUspesno) i čuva kroz već
  postojeće `KreirajStavkuObrasca`/`PromeniStavkuObrasca` operacije — **ne treba nova sistemska operacija**
  za sam OCR poziv, jer OCR nije deo dokumentovanog SK/SO spiska (48 signala). To znači da ovaj deo ne
  utiče na "1:1 sa dokumentacijom" zahtev — samo mora da radi dovoljno dobro da se vidi da polja
  StavkeObrasca imaju smisla.
- Ako zafali vremena — mockuj: hardkoduj/generiši nasumične `ocrVrednost` bez pravog poziva servisa.
  Profesor ovo ne ispituje (predmet je Projektovanje softvera, ne OCR), bitno je da ostatak sistema radi.
- Ako radiš pravi poziv, pokreni `ocr-microservice/sv20-ocr-service` (Python/FastAPI sudeći po
  strukturi) lokalno i pozovi ga sa `http://localhost:<port>`.

---

## 7. Dokumentacija — šta tačno napisati (poglavlja koja ne postoje)

Piši ovo paralelno sa kodom (raspored ispod), ne na kraju — lakše je opisati nešto što upravo kucaš.

- **1. Uvod** (kratko, 1 strana): kontekst (ŠV-20 obrazac na fakultetu, ručna obrada), cilj rada (Java
  klijent-server aplikacija sa OCR ekstrakcijom podataka), pregled poglavlja.
- **4.1 Projektovanje korisničkog interfejsa**: screenshot svake forme (LoginForma, GlavnaForma + meni,
  StudentForma, ZaposleniForma, TipPoljaForma, StudijskiProgramForma, TerminDezurstvaForma,
  SV20ObrazacForma sa ugnježdenom StavkeObrascaForma) sa kratkim opisom elemenata i koji SK forma
  pokriva.
- **4.2 Projektovanje aplikacione logike**: dijagram klasa (ili opis) za `ApstraktnaGenerickaOperacija` +
  `Repository`/`DbRepository`/`DbRepositoryGeneric` + `Controller` + niti (`ObradaKlijentskihZahteva`) +
  `Komunikacija`/`Posiljac`/`Primalac` — ovde objašnjavaš **kako** radi ono što je u 3. poglavlju rečeno
  **šta** radi (razlika analiza/projektovanje, tačka 13 u Q&A fajlu). Ovde ide i objašnjenje template
  method-a i sinhronizacije registra ulogovanih.
- **4.3 Projektovanje skladišta podataka**: ER dijagram (može export iz MySQL Workbench-a) + kratko
  objašnjenje kako se konceptualne veze (agregacija/kompozicija/asocijacija) preslikavaju u FK-ove — ovo
  se skoro doslovno naslanja na pitanja 27–36 iz `Odgovori na pitanja...pdf`, iskoristi te formulacije.
- **5. Implementacija**: struktura paketa (slika iz sekcije 1 ovog plana), korišćene tehnologije (Java,
  Swing, JDBC, MySQL, Socket API), par redova o tome kako izgleda kompletan tok jedne operacije kroz sve
  slojeve (forma → `Komunikacija` → socket → `ObradaKlijentskihZahteva` → `Controller` → sistemska
  operacija → `DbRepositoryGeneric` → baza) — ovo je isto ono što ispitivač traži da "ispričaš tečno" na
  odbrani, pa napiši baš taj tok.
- **6. Testiranje**: tabela test slučajeva za svih 9 detaljno opisanih SK (SK1–4, SK5–8, SK9, SK25) —
  ulaz, očekivan izlaz, stvaran izlaz — plus par graničnih slučajeva (dupla prijava, nevalidan JMBG,
  nevalidan email, brisanje studenta koji ima obrazac → RESTRICT).
- **7. Zaključak**: šta je urađeno, ograničenja (OCR je pojednostavljen/mockovan ako jeste), šta bi se
  dalje radilo.
- **Literatura**: predavanja/skripta predmeta, MySQL/Java dokumentacija, eventualno OCR biblioteka koju
  Python servis koristi.

---

## 8. 15-dnevni raspored

Realno računajući da 8+ dana ide na hand-typing ~95 klasa, a preostalo na dokumentaciju + odbranu.
Prilagodi datume svom kalendaru — ovo je redosled i obim po danu, ne fiksni kalendar.

| Dan | Fokus |
|---|---|
| 1 | Setup V2App (3 modula), MySQL instaliran, `baza.sql` iz sekcije 3 otkucan i pokrenut, `dbconfig.properties`. Napisan Uvod (nacrt). |
| 2 | `ApstraktniDomenskiObjekat` + svih 8 domenskih klasa + 4 enuma (ZAJEDNICKI/domen). |
| 3 | `Operacija` enum usklađen sa Tabelom 5 + `Zahtev`/`Odgovor`/`Posiljac`/`Primalac`. |
| 4 | `Konfiguracija` (ispravljena), `DbConnectionFactory` (ispravljena, bez deljene konekcije), `Repository`/`DbRepository`/`DbRepositoryGeneric`, `ApstraktnaGenerickaOperacija` (template method) — polako, ovo je srž ocene. |
| 5 | `LoginOperacija` + registar ulogovanih (sinhronizacija, 2.1) + `Controller` (login) + `Server` + `ObradaKlijentskihZahteva` (uklj. logout na disconnect). Testiraj login sa 2 klijenta ručno. |
| 6 | Vertikalni presek StudijskiProgram: server operacije + `Komunikacija` metode + `StudijskiProgramController` + `StudijskiProgramForma`. Radi kraj-do-kraja. |
| 7 | TerminDezurstva (Ubaci) + TipPolja (kompleksne OCR-pozicione validacije). |
| 8 | Student (JMBG kontrolni broj, format indeksa) + ZaposleniFakulteta (dvofazni Kreiraj/Promeni). |
| 9 | SV20Obrazac (dvofazno, 2 FK, JFileChooser). |
| 10 | StavkeObrasca (ugnježdeno) + ZaposleniTermin. |
| 11 | GlavnaForma + meni (1:1 struktura sa str. 5) + `Cordinator` + ručno testiranje svih 30 SK. |
| 12 | OCR mikroservis integracija (minimalna, sekcija 6) — ili mock ako nema vremena. |
| 13 | Dokumentacija: 4.1, 4.2, 4.3 (screenshotovi, ER dijagram, opis arhitekture). |
| 14 | Dokumentacija: 5. Implementacija, 6. Testiranje (tabele), 7. Zaključak, Literatura. Provera cele dokumentacije 1:1 sa kodom (brojevi SK, nazivi operacija, imena klasa). |
| 15 | Odbrana priprema (sekcija 9) + rezervni dan za ono što je kasnilo. |

---

## 9. Priprema za odbranu — konkretne vežbe

Iz beležaka, ovo su stvarna pitanja/zadaci sa prethodnih odbrana. Uvežbaj svaku stavku dok kucaš, ne
ostavljaj za kraj:

- **Ukloni `extends ApstraktnaGenerickaOperacija` iz jedne operacije i vrati implementaciju da radi
  isto** (bez template method-a) — pa **objasni zašto je bolje sa njim** (nasleđivanje, jedno mesto za
  transakcije, sistemske operacije ne mogu da "zaborave" `commit`/`rollback`). Ovo je doslovno traženo
  na jednoj od odbrana.
- **Uživo dopiši regex validaciju** na neko polje u formi (npr. email, JMBG) — vežbaj da to uradiš za
  < 2 min.
- **Prikaz svih trenutno ulogovanih korisnika** — imaš već registar iz 2.1, samo dodaj formu/dugme koje
  ga prikaže. Ovo direktno pokazuje "kako se barata nitima".
- Znaj napamet objasniti, bez zastajkivanja, **jedan ceo tok**: forma → `Komunikacija.posalji(Zahtev)` →
  socket → `ObradaKlijentskihZahteva` (koja nit, zašto nit-po-klijentu) → `Controller` → konkretna
  `XOperacija.izvrsi()` (template method koraci) → `DbRepositoryGeneric` (generički SQL preko
  `ApstraktniDomenskiObjekat` metoda) → baza → `Odgovor` nazad. Ovo pitaju **skoro svi** ispitivači.
- **MVC, Singleton, Template method, Nasleđivanje** — imenuj tačno koja klasa je šta i zašto (Controller
  = Singleton, `DbConnectionFactory` = Singleton, `Komunikacija` = Singleton na klijentu, `Cordinator` =
  Singleton).
- **Gde je properties fajl na serveru** — znaj tačnu putanju u V2 (i zašto više nije hardkodovana
  apsolutna putanja — to je i sam po sebi dobar odgovor koji pokazuje razumevanje).
- **SELECT uživo** — odradi upit ručno u MySQL klijentu nad `bazaocr` tokom vežbe.
- **Šta su niti / šta je soket / sinhronizacija deljenih resursa** — koristi formulacije iz
  `Odgovori na pitanja...pdf` (već tačno formulisane za ispit) + tvoj konkretan primer (registar
  ulogovanih korisnika).
- **action event / event listener** — znaj objasniti kako `btnX.addActionListener(...)` radi i zašto to
  IDE generiše kad dupliraš dugme, čak i ako to nikad ručno nisi pisao (NetBeans GUI builder generiše
  automatski, ali moraš znati šta se dešava "ispod haube").
- Ponovi teoriju iz `Odgovori na pitanja za dokumentaciju.pdf` (36 pitanja) — ovo su skoro doslovno
  pitanja koja profesor/asistenti postavljaju, formulacije su već tačne za ispit.

---

## 10. Otvorena pitanja za mentora (proveri pre nego što potrošiš vreme na to)

- Da li sekvencni dijagrami (3.2) i ugovori (3.3) treba da pokriju svih 9 detaljnih SK, ili je dovoljno
  ono što već postoji (DS1–DS3 + UG1–UG8, fokusirano na SV20Obrazac)? Dokumentacija trenutno ima
  sekvencne dijagrame samo za Kreiraj/Pretraži/Promeni SV20Obrazac.

**Rešeno (nije više otvoreno pitanje):** Kreiraj→Promeni obrazac ostaje kako je dokumentovano, bez ijedne
izmene u PDF-u — odluka i detaljno obrazloženje po konceptu su u sekciji 4 (4.0/4.1). Ukratko: kod
Student/ZaposleniFakulteta/StudijskiProgram/ZaposleniTermin "Kreiraj" i "Promeni" su dva odvojena
korisnička scenarija (nov zapis / izmena postojećeg), a kod SV20Obrazac/TipPolja/StavkeObrasca je to
pravi dvofazni tok jer ta polja stvarno zavise od kasnije OCR obrade.
