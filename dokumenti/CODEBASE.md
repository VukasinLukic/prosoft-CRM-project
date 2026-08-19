# CODEBASE.md — tačan redosled kucanja, fajl po fajl

Ovo je operativni pratilac uz `IMPLEMENTACIONI_PLAN.md` (tamo su dani/faze i arhitektonske odluke — ovde
je **svaki fajl, tačno šta ide u njega, tačnim redosledom**). Štikliraj `[ ]` → `[x]` kako kucaš. Za svaki
fajl piše koji v1 fajl da otvoriš kao referencu (pročitaj, zatvori, otkucaj iz glave) i šta MORA drugačije
u odnosu na v1 (popravke iz plana: sinhronizacija login-a, thread-safe konekcija, relativna putanja do
config fajla).

Konvencija paketa je identična v1 (menjaš samo `v1AplikacijeGotov` → `V2App` u putanji). Sve klase su u
`domen`/`komunikacija`/`konfiguracija`/`repository`/`operacije`/`controller`/`niti`/`server`/`forme`/
`kontroleri`/`cordinator`/`main` paketima, tačno kao u planu (sekcija 1).

---

## FAZA 0 — Setup (pre ijedne linije Jave)

- [ x] Instaliraj/pokreni MySQL, otkucaj `baza.sql` iz `IMPLEMENTACIONI_PLAN.md` (sekcija 3) i pokreni ga. napravljena bazaocr
- [ ] Napravi 3 modula/projekta u IDE-u: `ZAJEDNICKI`, `SERVER`, `KLIJENT` (SERVER i KLIJENT zavise od
      ZAJEDNICKI kao library/module dependency, tačno kao u v1).
- [ x] `git init` u `V2App`, prvi commit sa praznom strukturom.

---

## FAZA 1 — ZAJEDNICKI / domen (dan 2 iz plana)

### 1.0 `domen/ApstraktniDomenskiObjekat.java` — PRVI fajl koji kucaš, ceo sistem zavisi od njega

v1 referenca: `ZAJEDNICKI/src/domen/ApstraktniDomenskiObjekat.java`

Interfejs sa 7 metoda koje **svaka** domenska klasa mora implementirati (ovo je tvoj "generički
domenski objekat" iz beleški, pravilo 2):

```
String vratiNazivTabele();
List<ApstraktniDomenskiObjekat> vratiListu(ResultSet rs) throws Exception;
String vratiKoloneZaUbacivanje();
String vratiVrednostiZaUbacivanje();
String vratiPrimarniKljuc();
ApstraktniDomenskiObjekat vratiObjekatIzRS(ResultSet rs) throws Exception;
String vratiVrednostiZaIzmenu();
```
xxxxx
`extends Serializable` (mora ići preko mreže kroz `Zahtev`/`Odgovor`).
----------------

**Zašto svaka metoda postoji** (znaj ovo napamet za odbranu — pitanje "kako radi generički broker"):
- `vratiNazivTabele()` → `DbRepositoryGeneric` gradi `SELECT * FROM <ovo>`.
- `vratiListu(rs)` → uvek ista petlja (`while(rs.next()) lista.add(vratiObjekatIzRS(rs))`) — broker je
  generički jer ne zna tip, ali svaka klasa zna da napravi samu sebe iz reda.


- `vratiKoloneZaUbacivanje()` / `vratiVrednostiZaUbacivanje()` → broker gradi `INSERT INTO tabela (...) VALUES (...)`.
- `vratiPrimarniKljuc()` → broker gradi `WHERE ...` za `UPDATE`/`DELETE`.
- `vratiObjekatIzRS(rs)` → mapira JEDAN red rezultata u objekat (mini-ORM po ruci).
- `vratiVrednostiZaIzmenu()` → broker gradi `SET ...` za `UPDATE`.

### 1.1 Enumi (kucaj pre domenskih klasa jer ih koriste)

- [ x] `domen/stepenStudija.java` → `enum stepenStudija { OAS, MAS, DAS }`
- [x ] `domen/tipPodatka.java` → `enum tipPodatka { TEXT, NUMERIC, ALPHANUMERIC, DATE, BOOLEAN }`
- [ x] `domen/tipTermina.java` → `enum tipTermina { PRVA_SMENA, DRUGA_SMENA, TRECA_SMENA }`
- [ x] `domen/Status.java` → `enum Status { PODNET, U_OBRADI, VRACEN_NA_KOREKCIJU, ODOBREN, ODBIJEN }`

(imena i vrednosti moraju se poklapati slovo-za-slovo sa `ENUM(...)` u `baza.sql`, inače JDBC baca
grešku pri mapiranju)

### 1.2 Domenske klase — obrazac (template), pa 8 klasa

**Obrazac (nauči ga na Student-u, pa ponavljaj):** privatna polja tačno kao u konceptualnom modelu
(3.4/3.5 dokumentacije) → konstruktor bez argumenata (obavezan, `DbRepositoryGeneric` ga zove preko
`vratiObjekatIzRS`) → konstruktor sa svim argumentima → getteri/setteri za sve → `toString()` (kratak,
čitljiv) → `hashCode()`/`equals()` (baziran na poslovnom ključu, npr. `jmbg` kod Studenta, ne na
generisanom ID-ju) → 7 metoda iz `ApstraktniDomenskiObjekat`. **Veze ka drugim konceptima drži kao
ugnježden objekat** (npr. `Student` ima polje `StudijskiProgram studijskiProgram`, ne `int idStudProgram`)
— to je isti obrazac po celom v1 kodu, prati ga.

- [ x] **`domen/Student.java`** — v1 referenca pročitana i data kao primer ispod. Polja: `indeks:String,
      jmbg:String, ime:String, prezime:String, mestoRodjenja:String, adresaStanovanja:String,
      studijskiProgram:StudijskiProgram`.
  - `vratiNazivTabele()` → `"student"`
  - `vratiKoloneZaUbacivanje()` → `"indeks, jmbg, ime, prezime, mestoRodjenja, adresaStanovanja, idStudProgram"`
  - `vratiVrednostiZaUbacivanje()` → string-spoji sve vrednosti pod navodnicima, `idStudProgram` je
    `studijskiProgram.getIdStudProgram()` (broj, bez navodnika)
  - `vratiPrimarniKljuc()` → `"student.indeks = '" + indeks + "'"`
  - `vratiObjekatIzRS(rs)` → pročitaj sve kolone, i za `StudijskiProgram` napravi **prazan** `new StudijskiProgram()`
    i postavi mu samo `idStudProgram` iz `rs.getInt("idStudProgram")` (pun objekat se ne učitava ovde —
    to bi zahtevalo JOIN, van je opsega generičkog brokera)
  - `vratiVrednostiZaIzmenu()` → `"jmbg = '...', ime = '...', ... , idStudProgram = " + studijskiProgram.getIdStudProgram()`
    (bez `indeks` — on je PK, ne update-uje se)

- [ x] **`domen/StudijskiProgram.java`** — polja: `idStudProgram:int, naziv:String, oznaka:String,
      stepenStudija:stepenStudija`. `vratiPrimarniKljuc()` je `"studijskiprogram.idStudProgram = " + idStudProgram`
      (broj, ne string). Enum se čita/piše preko `.name()` pri upisu i `stepenStudija.valueOf(rs.getString(...))`
      pri čitanju.

- [x ] **`domen/ZaposleniFakulteta.java`** — polja: `idZaposlenog:int, ime:String, prezime:String,
      korisnickoIme:String, email:String, sifra:String`. Isti obrazac kao StudijskiProgram (int PK,
      auto_increment → `vratiKoloneZaUbacivanje()` NE uključuje `idZaposlenog`).

- [x ] **`domen/TerminDezurstva.java`** — polja: `idTerminDezurstva:int, tipTermina:tipTermina,
      kancelarija:String`.

- [x ] **`domen/TipPolja.java`** — polja: `idPolja:int, nazivPolja:String, tipPodatka:tipPodatka,
      regexValidacija:String, pozicijaX:Integer, pozicijaY:Integer, sirina:Integer, visina:Integer,
      stranica:int, redosledObrade:Integer, podrzavaOCR:boolean, obaveznoPolje:boolean`. Koristi
      `Integer` (ne `int`) za nullable pozicione atribute — inače ne možeš predstaviti "nema vrednosti"
      kad `podrzavaOCR=false`. Pazi u `vratiVrednostiZaUbacivanje()`/`vratiObjekatIzRS()` da hendluješ
      `null` (ne stavljaj `'null'` string u SQL, piši doslovno `NULL` bez navodnika kad je vrednost null).

- [ x] **`domen/SV20Obrazac.java`** — polja: `idObrazac:int, datumUnosa:Date, skolskaGodina:int,
      semestar:int, status:Status, putanjaFajla:String, ocrIzvrseno:boolean, brojUspesnihStavki:int,
      brojNeuspesnihStavki:int, zaposleniFakulteta:ZaposleniFakulteta, student:Student` (poslednja dva su
      ugnježdeni objekti, u bazi su `idZaposlenog`/`indeks`). `java.sql.Date` za `datumUnosa` (ne
      `java.util.Date` direktno u SQL stringu — moraš formatirati kao `'yyyy-MM-dd'`).

- [x ] **`domen/StavkeObrasca.java`** — polja: `idObrazac:int, idStavke:int, ocrVrednost:String,
      korigovanaVrednost:String, nivoPodudarnosti:double, ocrUspesno:boolean, tipPolja:TipPolja`.
      **Kompozitni ključ** — `vratiPrimarniKljuc()` mora vratiti
      `"idObrazac = " + idObrazac + " AND idStavke = " + idStavke` (dve kolone, ne jedna kao kod ostalih).

- [x ] **`domen/ZaposleniTermin.java`** — polja: `datum:Date, zaposleniFakulteta:ZaposleniFakulteta,
      terminDezurstva:TerminDezurstva, brojSati:int, vanredan:boolean`. Takođe kompozitni ključ
      (`datum, idZaposlenog, idTerminDezurstva`).

---

## FAZA 2 — ZAJEDNICKI / komunikacija (dan 3)

- [x ] **`komunikacija/Operacija.java`** — enum sa svim signalima. Otvori dokumentaciju str. 20 (Tabela 5,
      48 signala) i v1 `ZAJEDNICKI/src/komunikacija/Operacija.java` uporedo — otkucaj enum vrednost za
      svaki signal koji stvarno koristiš (grupisano po konceptu, komentar pored svake sa brojem signala iz
      tabele, kao u v1). Ne dodaji `PRETRAZI_TIP_POLJA`/`PRETRAZI_STUDIJSKI_PROGRAM`/
      `PRETRAZI_TERMIN_DEZURSTVA` — dokumentacija za te šifarnike pretragu radi kroz `vratiListu*`, ne
      kroz poseban "pretraži jedan", pa su te tri mrtav kod u v1 (ne prepisuj grešku).


- [ x] **`komunikacija/Zahtev.java`** — `Serializable`, polja `Operacija operacija`, `Object parametar`,
      konstruktor + getteri.
- [ x] **`komunikacija/Odgovor.java`** — `Serializable`, polje `Object odgovor` + getter/setter.
- [x ] **`komunikacija/Posiljac.java`** — omotava `ObjectOutputStream` nad socket-om, metoda
      `posalji(Object o)` → `oos.writeObject(o); oos.flush();`.
- [ x] **`komunikacija/Primalac.java`** — omotava `ObjectInputStream`, metoda `primi()` → `ois.readObject()`.

---

## FAZA 3 — SERVER infrastruktura (dan 4 — najvažniji dan, ne žuri)

- [ x] **`konfiguracija/Konfiguracija.java`** — **razlikuje se od v1!** Singleton, `Properties`, ali
      čitanje ide preko class-loadera, ne preko apsolutne putanje:
      `Konfiguracija.class.getResourceAsStream("dbconfig.properties")`. Stavi
      `dbconfig.properties` u isti paket/folder kao `.java` fajl (`SERVER/src/konfiguracija/`).
      Sadržaj fajla identičan v1: `db.url=jdbc:mysql://localhost:3306/bazaocr`, `db.username=root`,
      `db.password=`.
- [ x] **`repository/Repository.java`** — generički interfejs `Repository<T>`:
      `getAll(T param, String uslov)`, `add(T)`, `edit(T)`, `delete(T)`, `getAll()`.
- [ x] **`repository/db/DbConnectionFactory.java`** — **razlikuje se od v1!** Ne drži jednu deljenu
      `Connection` kao polje. `getKonekcija()` svaki put pravi **novu** `DriverManager.getConnection(...)`
      i vraća je (`setAutoCommit(false)`). Nema `if (konekcija == null)` provere — nema šta da se keš-uje,
      svaki poziv = sveža konekcija. Ovo je popravka thread-safety problema iz plana (sekcija 2.2).
- [ x] **`repository/db/DbRepository.java`** — interfejs `DbRepository<T> extends Repository<T>` sa
      `default` metodama `connect()/disconnect()/commit()/rollback()` koje deleguju na
      `DbConnectionFactory` (identično v1 — ovde nema promene, promena je unutar `DbConnectionFactory`).
- [ x] **`repository/db/DbRepositoryGeneric.java`** — `implements DbRepository<ApstraktniDomenskiObjekat>`.
      4 metode (`getAll(param,uslov)`, `add`, `edit`, `delete`) grade SQL string preko
      `ApstraktniDomenskiObjekat` metoda i izvršavaju ga (`Statement`, ne `PreparedStatement` — isto kao
      v1; ako ti ostane vremena, `PreparedStatement` je bezbednija verzija, ali nije deo obaveznih
      pravila iz beležaka pa nije prioritet).
- [x ] **`operacije/ApstraktnaGenerickaOperacija.java`** — **srž ocene, kucaj polako i razumi svaki red.**
      `protected final Repository<ApstraktniDomenskiObjekat> broker = new DbRepositoryGeneric();` (tip
      generičkog brokera preko interfejsa, ne konkretne klase — to je "programiraj prema interfejsu").
      `public final void izvrsi(Object objekat, String kljuc)` — **template method**, redosled:
      `preduslovi(objekat)` → `connect()` → `try { izvrsiOperaciju(objekat, kljuc); commit(); }
      catch(Exception e) { rollback(); throw e; } finally { disconnect(); }`. Dve `protected abstract`
      metode koje podklase pišu: `preduslovi(Object)` i `izvrsiOperaciju(Object, String)`.

---

## FAZA 4 — Login, registar prijavljenih, server, niti (dan 5)

- [x ] **`operacije/LoginOperacija.java`** — `extends ApstraktnaGenerickaOperacija`. `preduslovi()`
      proverava da parametar nije null i da je `ZaposleniFakulteta`. `izvrsiOperaciju()`:
      1. `broker.getAll(new ZaposleniFakulteta(), null)`, nađi zaposlenog sa istim `korisnickoIme`+`sifra`
         (isti kod kao v1 `LoginOperacija`).
      2. **Novo u odnosu na v1:** ako je pronađen, proveri registar ulogovanih (vidi sledeću stavku) — ako
         je `korisnickoIme` već u registru, baci `Exception("Korisnik je već prijavljen na sistem!")`;
         ako nije, dodaj ga i vrati zaposlenog. Ova provera+dodavanje mora biti u **jednom**
         `synchronized` bloku (check-then-act mora biti atomsko, inače dve niti mogu proći proveru
         istovremeno).
- [ x] **`controller/RegistarPrijavljenih.java`** (nova klasa, ne postoji u v1) — nosi statički
      `private static final Set<String> ulogovani = Collections.synchronizedSet(new HashSet<>());` i
      metode `boolean prijavi(String korisnickoIme)` (vraća `false` ako je već prijavljen, inače doda i
      vrati `true` — sve unutar `synchronized(ulogovani)`), `void odjavi(String korisnickoIme)`,
      `Set<String> vratiSve()`. `LoginOperacija` zove `prijavi()`.
- [ x] **`operacije/OdjaviZaposlenogOperacija.java`** (novo) — `izvrsiOperaciju()` zove
      `RegistarPrijavljenih.odjavi(korisnickoIme)`. Nema `broker` poziva (ne dira bazu), ali i dalje
      nasleđuje `ApstraktnaGenerickaOperacija` radi doslednosti (transakcija se jednostavno ne koristi).
- [x ] Dodaj `ODJAVI_ZAPOSLENOG` u `Operacija` enum (vrati se korak unazad u FAZI 2 i dopuni ga — u redu
      je, enum se ovde prvi put stvarno koristi za novu operaciju).
- [ x] **`controller/Controller.java`** — Singleton (`getInstanca()`), za sada samo `login()`/`odjava()`
      metode koje prave `LoginOperacija`/`OdjaviZaposlenogOperacija` i zovu `izvrsi()`. Ostale metode
      (po jedna za svaki koncept) dodaješ postepeno kako pišeš operacije u Fazi 5 — ne piši ih sve odjednom
      sada.
- [ x] **`server/Server.java`** — `ServerSocket(9000)`, `while(true) { Socket s = server.accept();
      new ObradaKlijentskihZahteva(s).start(); }` — jedna nit po klijentu (pravilo 5 iz beležaka).
- [ x] **`niti/ObradaKlijentskihZahteva.java`** — `extends Thread`. `run()`: petlja `primi()` →
      `switch(zahtev.getOperacija())` → pozovi odgovarajuću `Controller` metodu → upakuj u `Odgovor` →
      `posalji()`. Za sada samo `case PRIJAVI_ZAPOSLENOG` i `case ODJAVI_ZAPOSLENOG` (ostale grane
      dodaješ kako pišeš operacije). **Bitno, razlika od v1:** u `catch` grani (kad se socket
      neočekivano prekine/klijent ugasi prozor bez eksplicitne odjave) pozovi
      `RegistarPrijavljenih.odjavi(...)` za tog klijenta ako je bio ulogovan — čuvaj
      `korisnickoIme` trenutno ulogovanog kao polje niti da znaš koga da odjaviš. Ovo sprečava da
      korisnik ostane "zaglavljen" kao prijavljen posle pada konekcije.

**Test pre nastavka:** pokreni `Server`, napiši mali `main` koji otvara socket, šalje
`PRIJAVI_ZAPOSLENOG` dvaput sa istim korisničkim imenom (koristeći test red koji si ubacio u bazu preko
`baza.sql` ili ručno) — drugi put mora vratiti grešku. Ovo ti je prva potvrda da sinhronizacija radi.

---

## FAZA 5 — Sistemske operacije po konceptu (dani 6–10)

Obrazac je identičan za sve (video si ga celog na Studentu gore): `KreirajX`/`UbaciX` (validacija u
`preduslovi()`, `broker.add()` u `izvrsiOperaciju()`), `PromeniX` (`broker.edit()`), `ObrisiX`
(`broker.delete()`), `PretraziX` (jedan rezultat, `rezultat` polje + getter), `VratiSveX`/`VratiListuX`
(lista, `lista` polje + getter, `preduslovi()` prazan). Posle svake operacije, dodaj odgovarajuću metodu u
`Controller` (poziva `op.izvrsi(...)` pa vraća `op.getRezultat()`/`getLista()`) i `case` granu u
`ObradaKlijentskihZahteva`.

### 5.1 StudijskiProgram (dan 6 — prvi kompletan vertikalni presek, najprostiji šifarnik)

- [ ] `operacije/studijskiprogram/KreirajStudijskiProgramOperacija.java` — preduslovi: `naziv`/`oznaka`
      not null, `oznaka` velika slova, `length(oznaka) <= length(naziv)/2` (3.6 tabela 3).
- [ ] `operacije/studijskiprogram/PromeniStudijskiProgramOperacija.java`
- [ ] `operacije/studijskiprogram/ObrisiStudijskiProgramOperacija.java`
- [ ] `operacije/studijskiprogram/VratiSveStudijskeProgrameOperacija.java`
- [ ] `operacije/studijskiprogram/VratiListuStudijskihProgramaOperacija.java`
- [ ] Dopuni `Controller` (5 metoda) + `ObradaKlijentskihZahteva` (5 `case` grana).
- [ ] **`komunikacija/Komunikacija.java`** (KLIJENT modul) — ovo je prvi put da pišeš ovaj fajl, pa uradi
      ga sada. Singleton, `konekcija()`/`zatvoriKonekciju()` (socket ka `localhost:9000`), i za sada 5
      metoda za StudijskiProgram (svaka pravi `Zahtev`, šalje, čita `Odgovor`, baca izuzetak ako
      `odgovor.getOdgovor() instanceof Exception`). Ostale metode (za ostalih 7 koncepata) dodaješ kako
      napreduješ kroz ovu fazu — obrazac je uvek isti (video si ga celog u v1 fajlu).
- [ ] **`kontroleri/StudijskiProgramController.java`** — metode `ucitajPodatke()`, `dodaj()`
      (validacija + `Komunikacija...kreiraj...`), `sacuvaj()` (`promeni...`), `obrisi()` (potvrda pa
      `obrisi...`), `pretrazi()`, `ocisti()` (reset forme), `popuniFormu()` (klik na red tabele → napuni
      polja).
- [ ] **`forme/StudijskiProgramForma.java`** — `JTable` (kolone: Naziv, Oznaka, Stepen studija),
      `JTextField txtNaziv, txtOznaka`, `JComboBox<stepenStudija> cmbStepen`, dugmad
      `btnDodaj/btnSacuvaj/btnObrisi/btnOcisti/btnPretrazi` + `txtPretraga`.
- [ ] **`forme/GlavnaForma.java`** + **`kontroleri/GlavnaFormaController.java`** — otkucaj sada (ne čekaj
      kraj), makar samo sa stavkom za Šifarnici→Studijski program, dopunjavaš meni kako dodaješ ostale
      forme. **Razlika od v1: dokumentacija (str. 5) traži top-level meni "5. Podešavanja softverskog
      sistema"** koji v1 GlavnaForma nema (v1 ima samo meni "Sistem" sa Odjava/O programu, bez posebne
      stavke za podešavanja). Dodaj `JMenu "Podešavanja"` sa makar jednom stavkom (npr. prikaz trenutne
      adrese/porta servera u dijalogu — ne mora biti funkcionalno bogato, mora postojati da prati
      dokumentovanu strukturu menija 1:1).
- [ ] **`cordinator/Cordinator.java`** — singleton, `otvoriLoginFormu()`, `otvoriGlavnuFormu(ulogovani)`,
      `odjava()` (**dodaj poziv `Komunikacija...odjaviZaposlenog()` PRE zatvaranja konekcije** — v1 ovo
      ne radi, samo zatvori socket bez slanja `ODJAVI_ZAPOSLENOG`, što znači da v1 nikad ne oslobađa
      korisničko ime na serveru kad se korisnik uredno odjavi kroz meni; kod tebe mora, inače ti
      sinhronizacija iz Faze 4 nema smisla u normalnom radu).
- [ ] **`forme/LoginForma.java`** + **`kontroleri/LoginController.java`** — kucaj identično v1 obrascu
      (već si video ceo kod), samo dodaj: dugme za prijavu se `setEnabled(false)` čim se klikne, i vrati
      se na `true` u `finally` bloku posle odgovora servera (osnovna klijentska zaštita od duplog klika —
      to je "validacija i na klijentu" iz beležaka, red 10).
- [ ] **`main/Main.java`** (KLIJENT) i server-side pokretačka klasa (`main/Main.java` u SERVER, samo
      `new Server().start()` ili sl.) — po v1 obrascu.

**Test pre nastavka:** pokreni Server + Klijent, uloguj se, otvori Studijski program formu, uradi
kompletan CRUD ručno. Ako ovo radi, ostatak faze 5 je čisto ponavljanje obrasca.

### 5.2 TerminDezurstva (dan 7, deo 1) — pazi, JEDNOFAZNO (Ubaci, ne Kreiraj)

- [ ] `operacije/termindezurstva/UbaciTerminDezurstvaOperacija.java` — preduslovi: `brojSati>0`,
      `!vanredan || brojSati<=4` — čekaj, ovo polje je na ZaposleniTermin ne ovde; za TerminDezurstva
      preduslovi su samo `tipTermina`/`kancelarija` not null. `izvrsiOperaciju()` → `broker.add(...)`
      (jedan poziv, nema drugog koraka — vidi plan sekcija 4).
- [ ] `operacije/termindezurstva/PromeniTerminDezurstvaOperacija.java`
- [ ] `operacije/termindezurstva/ObrisiTerminDezurstvaOperacija.java`
- [ ] `operacije/termindezurstva/VratiSveTermineDezurstvaOperacija.java`
- [ ] `operacije/termindezurstva/VratiListuTerminaDezurstvaOperacija.java`
- [ ] Controller + ObradaKlijentskihZahteva + Komunikacija metode + `TerminDezurstvaController.java` +
      `TerminDezurstvaForma.java` (`JTable`: Tip termina, Kancelarija; `JComboBox<tipTermina>`,
      `JTextField txtKancelarija`; dugme se zove "Ubaci", ne "Kreiraj"/"Dodaj" — prati nazivlje iz SK25).
- [ ] Dopuni `GlavnaForma`/`GlavnaFormaController` meni: Šifarnici → Termin dežurstva.

### 5.3 TipPolja (dan 7, deo 2)

- [ ] `operacije/tippolja/KreirajTipPoljaOperacija.java` — preduslovi (3.6 tabela 2): `nazivPolja` not
      null; ako `podrzavaOCR=true` → `pozicijaX/Y not null, sirina>0, visina>0`; ako `false` → sve to
      mora biti `null`; ako `tipPodatka==NUMERIC` → `regexValidacija` sadrži samo cifre; `stranica>=1`.
- [ ] `operacije/tippolja/PromeniTipPoljaOperacija.java`
- [ ] `operacije/tippolja/ObrisiTipPoljaOperacija.java`
- [ ] `operacije/tippolja/VratiSveTipovePoljaOperacija.java`
- [ ] `operacije/tippolja/VratiListuTipovaPoljaOperacija.java`
- [ ] Controller + niti + Komunikacija + `TipPoljaController.java` + `TipPoljaForma.java` (`JTable`:
      Naziv, Tip podatka, Stranica, Podržava OCR; `JCheckBox chkPodrzavaOCR` koji enable/disable-uje
      polja `txtPozicijaX/Y, txtSirina, txtVisina` — ovo je dobra prilika da uvežbaš `ActionListener` na
      checkbox-u za pitanje "šta su event listeneri").
- [ ] Dopuni meni: Šifarnici → Tip polja.

### 5.4 Student (dan 8, deo 1) — dvofazno (vidi plan 4.1: Kreiraj = pun insert za NOVOG, Promeni = izmena POSTOJEĆEG)

Ovo si već video celo u primeru gore (domenska klasa + 6 operacija). Samo prekucaj:

- [ ] `operacije/student/KreirajStudentaOperacija.java` — preduslovi: `indeks` not null i format
      `gggg/bbbb` (regex `\d{4}/\d{4}`), `jmbg` tačno 13 cifara + **kontrolni broj po modelu 11** (ovo v1
      NE proverava — dodaj: izračunaj kontrolnu cifru po JMBG algoritmu i uporedi sa 13. cifrom), `ime`,
      `prezime`, `studijskiProgram` not null. Dodatno (3.6, tabela 5): godina iz `indeks` (prve 4 cifre)
      mora biti takva da je student stariji od 16 god. u trenutku upisa — ova provera zavisi od JMBG
      datuma rođenja, pa ide ovde takođe.
- [ ] `operacije/student/PromeniStudentaOperacija.java`
- [ ] `operacije/student/ObrisiStudentaOperacija.java`
- [ ] `operacije/student/PretraziStudentaOperacija.java`
- [ ] `operacije/student/VratiSveStudenteOperacija.java`
- [ ] `operacije/student/VratiListuStudenataOperacija.java`
- [ ] Controller + niti + Komunikacija + `StudentController.java` + `StudentForma.java` (`JTable`:
      Indeks, JMBG, Ime, Prezime, Mesto, Adresa, Program; `JComboBox<StudijskiProgram>` — puni se pozivom
      `vratiSveStudijskePrograme()` pri otvaranju forme).
- [ ] Dopuni meni: Primalac usluge → Student.

### 5.5 ZaposleniFakulteta (dan 8, deo 2) — dvofazno (Kreiraj Zaposlenog, SK10 — login je posebna SK9, već gotova)

- [ ] `operacije/zaposleni/KreirajZaposlenogOperacija.java` — preduslovi (3.6 tabela 1):
      `korisnickoIme` not null, `<> ime`, `<> prezime`, `length>=5`; `email` sadrži `@`; `sifra`
      `length>=8`. **Šifru pre upisa hešuj** (npr. `sifra.hashCode()` je nedovoljno — ako ostane
      vremena koristi pravi hash npr. SHA-256; ako ne, bar napomeni u dokumentaciji da je uprošćeno —
      v1 čuva plain-text šifru, to nije dobra praksa ali nije eksplicitno pravilo iz beležaka pa je
      opciono poboljšanje, ne obavezno).
- [ ] `operacije/zaposleni/PromeniZaposlenogOperacija.java`
- [ ] `operacije/zaposleni/ObrisiZaposlenogOperacija.java`
- [ ] `operacije/zaposleni/PretraziZaposlenogOperacija.java`
- [ ] `operacije/zaposleni/VratiSveZaposleneOperacija.java`
- [ ] `operacije/zaposleni/VratiListuZaposlenihOperacija.java`
- [ ] Controller + niti + Komunikacija + `ZaposleniController.java` + `ZaposleniForma.java` (`JTable`:
      ID, Ime, Prezime, Korisničko ime, Email; `JPasswordField txtSifra, txtPotvrdaSifre` — proveri na
      klijentu da se poklapaju pre slanja).
- [ ] Dopuni meni: Pružalac usluge → Zaposleni fakulteta.

### 5.6 SV20Obrazac (dan 9) — najsloženiji, pravi dvofazni tok (plan 4.1)

- [ ] `operacije/obrazac/KreirajSV20ObrazacOperacija.java` — preduslovi (3.6 tabela 6): `datumUnosa <=
      danas`, `student`/`zaposleniFakulteta` not null (moraju postojati — biraju se iz combobox-a već
      napunjenog listama), `skolskaGodina>=godina iz indeksa studenta`, `semestar>0`,
      `UNIQUE(indeks,skolskaGodina,semestar)` (proveri ručno pre insert-a — pozovi `broker.getAll` sa
      `WHERE` uslovom pa baci grešku ako lista nije prazna, pošto MySQL CHECK ne pokriva ovo preko
      composite unique koja već postoji kao DB constraint — ali bolje je i na nivou koda dati jasnu
      poruku pre nego DB baci SQL grešku), `putanjaFajla` not null.
- [ ] `operacije/obrazac/PromeniSV20ObrazacOperacija.java` — ovde se kasnije upisuju `status`,
      `ocrIzvrseno`, `brojUspesnihStavki`, `brojNeuspesnihStavki`.
- [ ] `operacije/obrazac/ObrisiSV20ObrazacOperacija.java`
- [ ] `operacije/obrazac/PretraziSV20ObrazacOperacija.java`
- [ ] `operacije/obrazac/VratiSveSV20ObrasceOperacija.java`
- [ ] `operacije/obrazac/VratiListuSV20ObrazacaOperacija.java`
- [ ] Controller + niti + Komunikacija + `SV20ObrazacController.java` + `SV20ObrazacForma.java` (`JTable`
      gornja: ID, Datum, Šk.godina, Semestar, Status, Student; `JComboBox<Student>`,
      `JComboBox<ZaposleniFakulteta>`, `JSpinner` za godinu/semestar, `JComboBox<Status>`,
      `JTextField txtPutanjaFajla` + `JButton btnOdaberiFajl` (`JFileChooser`), plus ugnježdena tabela za
      stavke — vidi 5.7).
- [ ] Dopuni meni: Dokumenti → ŠV-20 Obrazac.

### 5.7 StavkeObrasca (dan 10, deo 1) — ugnježdeno u SV20ObrazacForma, nema Obriši (Tabela 1)

- [ ] `operacije/stavke/KreirajStavkuObrascaOperacija.java`
- [ ] `operacije/stavke/PromeniStavkuObrascaOperacija.java`
- [ ] `operacije/stavke/PretraziStavkeObrascaOperacija.java` (vraća listu stavki za dati obrazac)
- [ ] `operacije/stavke/VratiStavkeObrascaOperacija.java`
- [ ] Controller + niti + Komunikacija + `StavkeObrascaController.java` + `StavkeObrascaForma.java`
      (donja tabela unutar SV20ObrazacForma: ID, Polje, OCR vrednost, Korigovana, Podudarnost, Uspešno).

### 5.8 ZaposleniTermin (dan 10, deo 2)

- [ ] `operacije/zaposlenitermin/KreirajZaposleniTerminOperacija.java` — preduslovi: `brojSati>0`,
      `!vanredan || brojSati<=4`.
- [ ] `operacije/zaposlenitermin/PromeniZaposleniTerminOperacija.java`
- [ ] `operacije/zaposlenitermin/ObrisiZaposleniTerminOperacija.java`
- [ ] `operacije/zaposlenitermin/PretraziZaposleniTerminOperacija.java`
- [ ] `operacije/zaposlenitermin/VratiSveZaposleneTermineOperacija.java`
- [ ] `operacije/zaposlenitermin/VratiListuZaposlenihTerminaOperacija.java`
- [ ] Controller + niti + Komunikacija (nema posebne forme u dokumentaciji — ako ti ostane vremena,
      dodaj prostu formu; ako ne, ova operacija može biti demonstrirana samo kroz automatski test / SQL
      uvid na odbrani, pošto SK29/SK30 nemaju dodeljenu formu u meniju na str. 5 dokumentacije).

---

## FAZA 6 — Integracija (dan 11)

- [ ] Ponovo pročitaj `GlavnaForma`/`GlavnaFormaController` — proveri da meni ima TAČNO strukturu sa str.
      5 dokumentacije: 1. Dokumenti (ŠV-20 Obrazac), 2. Pružalac usluge (Zaposleni Fakulteta), 3. Primalac
      usluge (Student), 4. Šifarnici (Studijski program, TerminDežurstva, TipPolja), 5. Podešavanja
      softverskog sistema, 6. O programu.
- [ ] Ručno testiraj svih 30 SK (checklist iz Tabele 2 dokumentacije, str. 6–7) — jedan po jedan, uključno
      sa alternativnim scenarijima (npr. probaj da obrišeš Studenta koji ima SV20Obrazac → mora RESTRICT
      da baci smislenu poruku, ne golu SQL grešku).
- [ ] Testiraj duplu prijavu ponovo (2 instance klijenta, isti nalog).

---

## FAZA 7 — OCR mikroservis (dan 12, niska prioritet — plan sekcija 6)

- [ ] Minimalni HTTP poziv iz `SV20ObrazacController` (klijent) ili sa servera posle upload-a fajla, ka
      `ocr-microservice`. Ako nema vremena — mock (nasumične vrednosti u `StavkeObrasca`).

---

## FAZA 8 — Dokumentacija (dani 13–14, detaljan spisak u IMPLEMENTACIONI_PLAN.md sekcija 7)

---

## FAZA 9 — Odbrana (dan 15, IMPLEMENTACIONI_PLAN.md sekcija 9)

---

## Brzi indeks — sve što treba da otkucaš, redom (bez opisa, za praćenje napretka)

**ZAJEDNICKI:** ApstraktniDomenskiObjekat → 4 enuma → 8 domenskih klasa → Operacija enum → Zahtev →
Odgovor → Posiljac → Primalac

**SERVER (redom):** Konfiguracija → DbConnectionFactory → Repository → DbRepository →
DbRepositoryGeneric → ApstraktnaGenerickaOperacija → LoginOperacija → RegistarPrijavljenih →
OdjaviZaposlenogOperacija → Controller → Server → ObradaKlijentskihZahteva → *(za svaki od 8 koncepata:
Kreiraj/Ubaci → Promeni → Obrisi → Pretrazi → VratiSve → VratiListu, redom StudijskiProgram →
TerminDezurstva → TipPolja → Student → ZaposleniFakulteta → SV20Obrazac → StavkeObrasca →
ZaposleniTermin)*

**KLIJENT (redom):** Komunikacija (dopunjavaš postepeno) → Cordinator → LoginForma → LoginController →
GlavnaForma → GlavnaFormaController → *(za svaki koncept istim redosledom kao gore: Forma → Controller)*
→ Main

Ukupno ≈ 95 fajlova. Kad završiš, ovaj fajl treba da ima sve kućice štiklirane.


Korak 3: Izgradnja Klijentskog modula (PsKlijent)


Infrastruktura: Kopiraj iz v1AplikacijeGotov/KLIJENT/src/:
komunikacija/Komunikacija.java
cordinator/Cordinator.java
main/Main.java



Osnovne forme i kontroleri: Kopiraj:



forme/LoginForma.java + kontroleri/LoginController.java
forme/GlavnaForma.java + kontroleri/GlavnaFormaController.java
Šifarničke forme i kontroleri: Kopiraj:
forme/StudijskiProgramForma.java + kontroleri/StudijskiProgramController.java
forme/TerminDezurstvaForma.java + kontroleri/TerminDezurstvaController.java
forme/TipPoljaForma.java + kontroleri/TipPoljaController.java



Glavne domenske forme i kontroleri: Kopiraj i dopuni:



forme/StudentForma.java + kontroleri/StudentController.java
forme/ZaposleniForma.java + kontroleri/ZaposleniController.java
forme/SV20ObrazacForma.java + kontroleri/SV20ObrazacController.java
forme/StavkeObrascaForma.java + kontroleri/StavkeObrascaController.java