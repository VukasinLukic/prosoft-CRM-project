# prosoft-CRM-project (V2) — kompletan izveštaj i akcioni plan

Spojen izveštaj (prva analiza pre pull-a + dopuna posle pull-a) u jedan dokument. Ovde je sve na jednom mestu: trenutno stanje, tačno šta treba da uradiš, poređenje sa zahtevima predmeta "Projektovanje softvera", i plan za OCR mikroservis. Ništa u kodu nije menjano — ovo je čisto izveštaj, sve popravke ispod su predlog za tebe.

---

## 0. Trenutno stanje (najvažnije da znaš)

Projekat sam u međuvremenu ponovo pročitao posle tvog `git pull`-a i, da bih bio siguran a ne samo "na oko" poredio nazive metoda, **stvarno sam kompajlirao sva tri modula pravim `javac`-om** (na privremenoj kopiji, ništa tvoje nije dirano):

```
0_PsShared  → 0 grešaka
0_PsServer  → 0 grešaka
PsKlijent   → 0 grešaka
```

**Ceo projekat se sada čisto kompajlira.** Svih 9 formi u `PsKlijent/src/forme/` postoji i tačno se poklapa sa onim što kontroleri očekuju — ranije (pre pull-a) 7 od 9 formi uopšte nije postojalo na disku, to je bio glavni blokator, i sad je potpuno rešen. Takođe su rešene sve neusklađenosti iz `ISPRAVKE_KONTROLERA.md` (V1→V2 nazivi metoda za `StavkeObrascaController`, `StudijskiProgramController`, `SV20ObrazacController`, `TerminDezurstvaController`, `TipPoljaController`) i `Konfiguracija.java` sada čita `dbconfig.properties` preko classloader-a umesto relativne putanje.

Ono što ostaje su isključivo stvari u domenskom/serverskom sloju (koje pull nije dirao) plus jedna sitna UI rupa — sve pobrojano ispod, sa tačnim lokacijama.

---

## 1. Akcioni spisak — šta treba da uradiš (po prioritetu)

### 1.1 KRITIČNO — tri buga u `0_PsShared/src/domen/` (kod se kompajlira, ali radi pogrešno u runtime-u)

**a) `Student.java` — `vratiVrednostiZaUbacivanje()` ima suvišan zarez, INSERT studenta puca**

```java
return "'" + indeks + "', '" + jmbg + "', '" + ime + "', '" + prezime + "', '" + mestoRodjenja + "', '" + "', '" + adresaStanovanja + "', " + studijskiProgram.getIdStudProgram();
```

Posle `mestoRodjenja` je ubačen suvišan `"', '"` koji stvara jednu praznu vrednost previše u odnosu na 7 kolona iz `vratiKoloneZaUbacivanje()`. **Ispravka:** ukloni suvišni `"', '"` tako da string ide direktno sa `mestoRodjenja` na `adresaStanovanja`.

**b) `ZaposleniTermin.java` — `vratiNazivTabele()` vraća pogrešan naziv tabele**

```java
return "ZaposleniTermin";
```

Stvarna tabela je `zaposleni_termin`. Svaki SELECT/INSERT/UPDATE/DELETE nad ovim konceptom (SK29/SK30) puca sa "Table doesn't exist". **Ispravka:** `return "zaposleni_termin";`

**c) `StavkeObrasca.java` — složeni primarni ključ nije ispravno implementiran (najozbiljniji od ova tri)**

Tabela `stavkeobrasca` ima složeni ključ `(idObrazac, idStavke)`, a `idStavke` **nije auto_increment** — namerno se numeriše po obrascu (obrazac #5 i obrazac #7 mogu obe imati stavku sa `idStavke=1`). Trenutno:

```java
// vratiPrimarniKljuc() — pogrešno, koristi samo idStavke
return "stavkeobrasca.idStavke = " + idStavke;

// vratiKoloneZaUbacivanje() — ne uključuje idStavke uopšte
return "idObrazac, ocrVrednost, korigovanaVrednost, nivoPodudarnosti, ocrUspesno, idPolja";
```

Pošto generički broker gradi `UPDATE`/`DELETE ... WHERE <primarniKljuc>` isključivo iz ovog stringa, svaka izmena/brisanje jedne stavke može pogoditi pogrešan red iz **drugog** obrasca (tiha korupcija podataka, bez greške pri izvršavanju). Dodatno, `idStavke` je `NOT NULL` bez default vrednosti pa INSERT bez njega puca odmah.

**Ispravka:**
- `vratiPrimarniKljuc()` → `"stavkeobrasca.idObrazac = " + idObrazac + " AND stavkeobrasca.idStavke = " + idStavke`
- `vratiKoloneZaUbacivanje()`/`vratiVrednostiZaUbacivanje()` → dodati `idStavke` u obe liste
- U operaciji koja kreira stavku (`KreirajStavkuObrascaOperacija`), izračunati sledeći `idStavke` za dati `idObrazac` pre upisa (npr. `SELECT MAX(idStavke)+1 FROM stavkeobrasca WHERE idObrazac = ...`, ili `COUNT(*)+1`).

### 1.2 Nedostajuće poslovne validacije (`preduslovi()`) — DB CHECK sam nije dovoljan

- `KreirajStudijskiProgramOperacija`/`PromeniStudijskiProgramOperacija` — ne proverava `length(oznaka) <= length(naziv)/2`. Dodaj proveru u `preduslovi(Object objekat)`.
- `KreirajTipPoljaOperacija`/`PromeniTipPoljaOperacija` — ne proverava u Java kodu (1) da `podrzavaOCR=false` ⇒ pozicija/dimenzije moraju biti `null`, i `podrzavaOCR=true` ⇒ moraju biti popunjene i pozitivne; (2) da za `tipPodatka=NUMERIC` regex dozvoljava samo cifre. Trenutno se oslanjaš samo na CHECK constraint u bazi, što znači da korisnik na grešku nailazi kao golu SQL poruku umesto smislenog teksta (vidi i 1.4 ispod).

### 1.3 `ObrisiSV20ObrazacOperacija` — ručno gazi dokumentovani `ON DELETE RESTRICT`

`IMPLEMENTACIONI_PLAN.md` eksplicitno kaže (komentar u samom SQL-u): `ON DELETE RESTRICT — DELETE RESTRICTED, ne CASCADE!`. Ipak, `ObrisiSV20ObrazacOperacija.izvrsiOperaciju()` prvo ručno briše sve `StavkeObrasca` za taj obrazac, pa tek onda obrazac — što je u efektu CASCADE implementiran u aplikaciji, suprotno nameri RESTRICT-a (svrha RESTRICT-a je da spreči brisanje/da jasan signal ako obrazac ima stavke, a ne da se to tiho zaobiđe). **Ispravka:** ukloni ručno brisanje stavki; pusti da baza baci FK grešku ako postoje stavke, i uhvati tu grešku u operaciji da prevedeš u smislenu poruku (vidi 1.4).

### 1.4 Gole SQL greške korisniku pri brisanju

`ObrisiStudentaOperacija` i slične operacije brisanja zovu `broker.delete(...)` bez `try/catch` koji bi FK violation preveo u razumljivu poruku. `IMPLEMENTACIONI_PLAN.md` (FAZA6 testiranje) eksplicitno traži "smislenu poruku, ne golu SQL grešku". **Ispravka:** uhvati `SQLException` (ili opštiji `Exception`) oko `broker.delete(...)` i baci novi `Exception("Ne može se obrisati — postoje povezani zapisi.")` ili slično, po konceptu.

### 1.5 "Uredi stavke" dugme trenutno nigde ne postoji / ne radi (novo, nakon pull-a)

U `SV20ObrazacController` postoji metoda `private void urediStavke()` koja ispravno otvara `StavkeObrascaForma` za selektovani obrazac — ali je **nijedan listener ne poziva** (proverio sam `addActionListeners()` u potpunosti: nema poziva `urediStavke()` ni iz jednog postojećeg listenera). Kod se kompajlira, ali korisnik trenutno nema način u GUI-ju da otvori formu za stavke obrasca iz `SV20ObrazacForma`.

**Ispravka (u `forme`/`kontroleri` — samo ti, ja ne diram `forme`):** najlakše je dodati `MouseListener` na `tblObrasci` u `SV20ObrazacForma` (duplo-klik na red otvara stavke), ili dodati novo dugme `btnUrediStavke` u formu i pozvati `urediStavke()` iz njegovog listenera.

### 1.6 Manje/opcione stvari (vredi znati, nisu blokirajuće)

- **`Konfiguracija.sacuvajIzmene()`** i dalje piše preko relativne putanje (`"src/konfiguracija/dbconfig.properties"`) — čitanje je već popravljeno (classloader), upisivanje nije. Sporedna metoda, verovatno se koristi samo iz serverske forme za podešavanja tokom razvoja u NetBeans-u.
- **`hashCode()`** u `Student`, `StudijskiProgram`, `TipPolja` vraća hardkodiranu konstantu (7, 7, 3) umesto da se bazira na poslovnom ključu (npr. `jmbg` kod Studenta) — `CODEBASE.md` eksplicitno traži drugačije.
- **Šifra zaposlenog** (`ZaposleniFakulteta.sifra`) čuva se u čistom tekstu, nema heš nigde u kodu. `CODEBASE.md` ovo eksplicitno označava kao **opciono poboljšanje, ne obavezan zahtev** za ovaj predmet — ako ostane vremena, dodaj bar SHA-256.
- **`ZaposleniController.pretrazi()`** (i slični pretraga-metodi) grade `WHERE` uslov direktnom konkatenacijom teksta iz korisničkog polja — pošto projekat koristi `Statement` a ne `PreparedStatement` (prihvaćeno pojednostavljenje po projektnim napomenama), ovo tehnički otvara SQL injection ako neko u polje za pretragu unese npr. `' OR '1'='1`. Verovatno se neće ispitivati na predmetu, ali vredi znati da postoji.
- **`ApstraktnaGenerickaOperacija.broker`** je deklarisan kao sirovi tip `Repository` umesto `Repository<ApstraktniDomenskiObjekat>` — kozmetika.

### 1.7 Dokumentacija koju vredi počistiti pre odbrane

- `dokumenti/plan.md` je bajt-identičan sa `DOKUMENTACIJA_IZRADE_FORMI_V2.md` (potvrđeno sa `diff`) — dupliran fajl.
- `dokumenti/planImplementiranZaV1aplikacije.md` je zastareo (V1 stanje) — može da zbuni ako se otvori umesto V2 plana.
- `ISPRAVKE_KONTROLERA.md` i sekcija 1 `DOKUMENTACIJA_IZRADE_FORMI_V2.md` sada opisuju stanje koje više ne postoji (prazne forme, V1↔V2 nepodudaranja) — sve je već rešeno. Nije greška u kodu, ali ako ih otvoriš pred ispitivačem kao "trenutno stanje" zbuniće i tebe i njega. Dopiši na vrh "REŠENO" ili ih izbaci iz `dokumenti` foldera pred odbranu.

---

## 2. Poređenje sa zahtevima predmeta "Projektovanje softvera"

Iz `ProjektovanjeSoftveraNotes.txt`, "Odgovori na pitanja za dokumentaciju.pdf" i `VukasinLukicDokumentacija.pdf`. Obavezno ("MUST HAVE") i status u kodu:

- **MVC** — ✅ prisutno: `forme` = View, `kontroleri` = Controller (klijent), domenske klase + server = Model.
- **Generički broker sa opštim domenskim objektom** — ✅ prisutno: `DbRepositoryGeneric implements DbRepository<ApstraktniDomenskiObjekat>`, radi preko `ApstraktniDomenskiObjekat` interfejsa koji svaka domenska klasa implementira.
- **Apstraktna sistemska operacija + template method** — ✅ prisutno: `ApstraktnaGenerickaOperacija.izvrsi()` je `final`, poziva `preduslovi()→zapocniTransakciju()→izvrsiOperaciju()→potvrdiTransakciju()/ponistiTransakciju()→ugasiKonekciju()`. Ovo je tačno ono što ispitivači traže da se objasni ("zašto baš tako", uklanjanje `extends` da se proveri da li implementacija i dalje radi isto).
- **Klijent-server sa soketima i nitima** — ✅ prisutno: `Server`/`ServerSocket`, `ObradaKlijentskihZahteva extends Thread`, po jedna nit po klijentu.
- **Sprečavanje duple prijave + sinhronizacija** — ✅ prisutno: `RegistarPrijavljenih` sa `synchronized` statičkim metodama nad deljenom listom — spreman odgovor na klasično pitanje "kako se rešava problem deljenih resursa kod niti" (check-then-act atomičnost).
- **Dvofazni tok Kreiraj→Promeni** (SV20Obrazac, TipPolja, StavkeObrasca, Student, ZaposleniFakulteta) — operacije za oba koraka postoje u `0_PsServer/src/operacije/`; sada kad forme postoje, vredi na probu proći ceo tok kroz GUI da potvrdiš da forma zaista prvo zove Kreiraj pa tek posle Promeni.
- **Kompletan tok forma→slanje serveru→sistemska operacija→rad sa nitima** — ✅ sada demonstrabilno kroz GUI (pre pull-a ovo nije moglo da se pokaže jer forme nisu postojale) — proveri da probni klik kroz svaku formu zaista radi end-to-end pre odbrane.
- **Šta i dalje treba doraditi** naspram materijala za odbranu: tačke 1.1–1.5 iznad (domenski bagovi, preduslovi validacije, uredi-stavke dugme) — sve to može delovati nedovršeno pred ispitivačem ako ne popraviš pre odbrane.

---

## 3. Plan povezivanja OCR mikroservisa (u skladu sa `VukasinLukicDokumentacija.pdf`)

Trenutno stanje: **potvrđeno grep-om kroz kompletan Java kod — ne postoji nijedna linija koja poziva OCR servis** (nema `HttpURLConnection`, `HttpClient`, ni bilo kakve mreže ka Python servisu). Polja `SV20Obrazac.ocrIzvrseno` i `StavkeObrasca.ocrVrednost/korigovanaVrednost/nivoPodudarnosti/ocrUspesno` postoje u domenskom modelu i bazi, ali se popunjavaju samo ručno kroz postojeće forme/operacije.

`VukasinLukicDokumentacija.pdf` tretira OCR isključivo kao **poreklo vrednosti u `StavkeObrasca`** — nema pomena posebne sistemske operacije ili signala za "pozovi OCR". Ovo se potpuno poklapa sa preporukom iz `IMPLEMENTACIONI_PLAN.md` sekcija 6:

1. OCR servis (`ocr-microservice/sv20-ocr-service`, izgleda kao Python/FastAPI) tretiraš kao crnu kutiju, pozvanu preko `java.net.http.HttpClient` (sinhrono je dovoljno, nema potrebe za novom bibliotekom).
2. Poziv radiš **posle** što je `SV20Obrazac` već kreiran i fajl (`putanjaFajla`) upisan (posle prve faze `KreirajSV20Obrazac`) — npr. iz `SV20ObrazacController` kada korisnik klikne dugme tipa "Obradi OCR" (može biti novo dugme u `SV20ObrazacForma`, ali ni to ne zahteva novi signal/sistemsku operaciju).
3. Rezultat (JSON sa ekstrahovanim vrednostima po polju) mapiraš na postojeće pozive `KreirajStavkuObrasca` (jedna stavka po polju: `ocrVrednost`, `idPolja`, `idObrazac`) i kasnije `PromeniStavkuObrasca` kad korisnik ručno koriguje (`korigovanaVrednost`). Nakon što su sve stavke upisane, `PromeniSV20Obrazac` postavlja `ocrIzvrseno=true`, `brojUspesnihStavki`/`brojNeuspesnihStavki` prema `ocrUspesno` poljima stavki.
4. **Ne pravi novu sistemsku operaciju za sam OCR poziv** — nije deo od 48 dokumentovanih signala; dodavanje nove operacije samo za "pozovi OCR" bi se kosilo sa "1:1 sa dokumentacijom" zahtevom. OCR je implementacioni detalj *unutar* postojećih Kreiraj/Promeni operacija za `StavkeObrasca`, ne novi use case.
5. Ako nema vremena do odbrane: mockuj — generiši nasumične/hardkodirane `ocrVrednost` bez pravog HTTP poziva. `IMPLEMENTACIONI_PLAN.md` eksplicitno kaže da profesor ovo neće ispitivati (predmet je Projektovanje softvera, ne OCR) — bitno je da se vidi da polja `StavkeObrasca` imaju smisla, ne da OCR stvarno radi.
6. Ako ipak radiš pravi poziv: pokreni `ocr-microservice/sv20-ocr-service` lokalno (`http://localhost:<port>`) pre pokretanja Java servera, i pozivaj ga samo sa servera (ne sa klijenta) — server već ima sloj koji radi sa bazom, pa je prirodno mesto za taj poziv.

---

## 4. Kratak spisak akcija — redosled

1. Popraviti tri buga u `0_PsShared/src/domen/`: `Student.java` (dupli zarez), `ZaposleniTermin.java` (naziv tabele), `StavkeObrasca.java` (složeni ključ + `idStavke` u INSERT-u). *(najviši prioritet, direktno lomi funkcionalnost)*
2. Dodati nedostajuće `preduslovi()` provere (dužina oznake/naziva, OCR pozicija/dimenzije, NUMERIC regex).
3. Ispraviti `ObrisiSV20ObrazacOperacija` da poštuje RESTRICT umesto ručnog cascade-a, i uhvatiti FK greške na svim brisanjima da prikažeš smislenu poruku.
4. Ožičiti "uredi stavke" (dugme ili duplo-klik na tabelu obrazaca) u `SV20ObrazacForma`/`SV20ObrazacController` — trenutno mrtav kod.
5. (Opciono) `Konfiguracija.sacuvajIzmene()` na classloader/apsolutnu logiku; heš za šifru; `PreparedStatement` ili bar escape na pretragama.
6. Počistiti/označiti kao rešene stare dokumente (`plan.md` duplikat, `planImplementiranZaV1aplikacije.md`, `ISPRAVKE_KONTROLERA.md`) pre odbrane.
7. Odlučiti i upisati u zaključak dokumentacije da li OCR ide kao pravi HTTP poziv ili mock — po planu iz sekcije 3 ovog izveštaja.
8. Proći ceo tok kroz GUI (login → glavni meni → svaka forma → dvofazni Kreiraj/Promeni) da potvrdiš da sve radi end-to-end, ne samo da se kompajlira.

---

*Izveštaj nastao čitanjem kompletnog sadržaja `C:\Users\Tea\prosoft-CRM-project` (sve `.java` fajlove) i svih dokumenata iz `dokumenti` foldera, uključujući ponovno čitanje posle `git pull`-a i realno kompajliranje sva tri modula sa `javac` radi verifikacije. Nijedan fajl u projektu nije izmenjen.*
