# IMPLEMENTACIONI PLAN - ŠV-20 Sistem

## Seminarski rad iz Projektovanja Softvera
**Autor:** Vukašin Lukić 0342
**Fakultet organizacionih nauka, Beograd**

---

## 1. PREGLED PROJEKTA

### 1.1 Opis sistema
Sistem za obradu ŠV-20 obrazaca sa OCR mogućnostima. Troslojni klijent-server sistem implementiran u Java tehnologiji.

### 1.2 Arhitektura
```
┌─────────────┐     Socket:9000     ┌─────────────┐     JDBC     ┌─────────────┐
│   KLIJENT   │ ◄─────────────────► │   SERVER    │ ◄──────────► │   MySQL     │
│  (Swing)    │                     │  (Java)     │              │   bazaocr   │
└─────────────┘                     └─────────────┘              └─────────────┘
```

### 1.3 Struktura projekta
```
D:\Downloads\Prosoft\
├── ZAJEDNICKI\src\          # Deljeni modul (18 klasa)
│   ├── domen\               # Domenski objekti (8 klasa + 4 enuma)
│   └── komunikacija\        # Komunikacijski protokol (5 klasa)
├── SERVER\src\              # Serverski modul (61 klasa)
│   ├── controller\          # Controller singleton
│   ├── operacije\           # Sistemske operacije (45 klasa)
│   ├── repository\          # Generički repository pattern
│   ├── niti\                # Thread za klijente
│   └── forme\               # Server GUI forme
└── KLIJENT\src\             # Klijentski modul (16 klasa)
    ├── forme\               # GUI forme (8 klasa)
    ├── kontroleri\          # MVC kontroleri (5 klasa)
    ├── komunikacija\        # Komunikacija sa serverom
    └── cordinator\          # Koordinator navigacije
```

---

## 2. ANALIZA TRENUTNOG STANJA

### 2.1 IMPLEMENTIRANO (100%)

#### Domenski model (ZAJEDNICKI\src\domen\)
| Klasa | Status | Napomena |
|-------|--------|----------|
| `ApstraktniDomenskiObjekat.java` | ✅ Kompletno | Interfejs za ORM |
| `Student.java` | ✅ Kompletno | 7 atributa |
| `ZaposleniFakulteta.java` | ✅ Kompletno | 6 atributa |
| `StudijskiProgram.java` | ✅ Kompletno | 4 atributa |
| `TerminDezurstva.java` | ✅ Kompletno | 3 atributa |
| `TipPolja.java` | ✅ Kompletno | 12 atributa |
| `SV20Obrazac.java` | ✅ Kompletno | 12 atributa |
| `StavkeObrasca.java` | ✅ Kompletno | 7 atributa |
| `ZaposleniTermin.java` | ✅ Kompletno | 5 atributa |
| `Status.java` (enum) | ✅ Kompletno | PODNET, U_OBRADI, VRACEN_NA_KOREKCIJU, ODOBREN, ODBIJEN |
| `stepenStudija.java` (enum) | ✅ Kompletno | OAS, MAS, DAS |
| `tipPodatka.java` (enum) | ✅ Kompletno | TEXT, NUMERIC, ALPHANUMERIC, DATE, BOOLEAN |
| `tipTermina.java` (enum) | ✅ Kompletno | PRVA_SMENA, DRUGA_SMENA, TRECA_SMENA |

#### Komunikacijski protokol (ZAJEDNICKI\src\komunikacija\)
| Klasa | Status | Napomena |
|-------|--------|----------|
| `Operacija.java` | ✅ Kompletno | 34 operacije definisane |
| `Zahtev.java` | ✅ Kompletno | Serializable |
| `Odgovor.java` | ✅ Kompletno | Serializable |
| `Posiljac.java` | ✅ Kompletno | ObjectOutputStream |
| `Primalac.java` | ✅ Kompletno | ObjectInputStream |

#### Server modul (SERVER\src\)
| Komponenta | Status | Napomena |
|------------|--------|----------|
| `Controller.java` | ✅ Kompletno | Singleton, 30+ metoda |
| `Server.java` | ✅ Kompletno | ServerSocket port 9000 |
| `ObradaKlijentskihZahteva.java` | ✅ Kompletno | Thread per client |
| `DbConnectionFactory.java` | ✅ Kompletno | Singleton za MySQL konekciju |
| `DbRepositoryGeneric.java` | ✅ Kompletno | CRUD operacije |
| `Konfiguracija.java` | ✅ Kompletno | dbconfig.properties |
| Sve 45 operacija | ✅ Kompletno | CRUD za sve entitete |

#### Klijent modul - GUI forme (KLIJENT\src\forme\)
| Forma | Status | Napomena |
|-------|--------|----------|
| `LoginForma.java` | ✅ Kompletno | Login funkcionalnost |
| `GlavnaForma.java` | ✅ Kompletno | Menu bar, navigacija |
| `StudijskiProgramForma.java` | ✅ Kompletno | CRUD operacije |
| `TerminDezurstvaForma.java` | ✅ Kompletno | CRUD operacije |
| `TipPoljaForma.java` | ✅ Kompletno | CRUD operacije |

#### Klijent modul - Kontroleri (KLIJENT\src\kontroleri\)
| Kontroler | Status | Napomena |
|-----------|--------|----------|
| `LoginController.java` | ✅ Kompletno | Validacija, konekcija |
| `GlavnaFormaController.java` | ✅ Kompletno | Navigacija |
| `StudijskiProgramController.java` | ✅ Kompletno | CRUD |
| `TerminDezurstvaController.java` | ✅ Kompletno | CRUD |
| `TipPoljaController.java` | ✅ Kompletno | CRUD |

#### Komunikacija klijent (KLIJENT\src\komunikacija\)
| Klasa | Status | Napomena |
|-------|--------|----------|
| `Komunikacija.java` | ✅ Kompletno | Singleton, sve metode |

---

### 2.2 NIJE IMPLEMENTIRANO (Prazne klase)

| Komponenta | Lokacija | Status |
|------------|----------|--------|
| `StudentForma.java` | KLIJENT\src\forme\ | ❌ Prazna klasa |
| `ZaposleniForma.java` | KLIJENT\src\forme\ | ❌ Prazna klasa |
| `SV20ObrazacForma.java` | KLIJENT\src\forme\ | ❌ Prazna klasa |
| `StudentController.java` | KLIJENT\src\kontroleri\ | ❌ Ne postoji |
| `ZaposleniController.java` | KLIJENT\src\kontroleri\ | ❌ Ne postoji |
| `SV20ObrazacController.java` | KLIJENT\src\kontroleri\ | ❌ Ne postoji |

---

### 2.3 NEDOSTAJE (Prema dokumentaciji)

| Stavka | Prioritet | Napomena |
|--------|-----------|----------|
| SQL skripta za kreiranje baze | VISOK | Potrebno za setup |
| Validacija atributa | SREDNJI | Prema specifikaciji |
| Pretraga po kriterijumima | SREDNJI | vratiListu metode |
| OCR integracija | NIZAK | Napredna funkcionalnost |
| Unit testovi | NIZAK | Opciono |

---

## 3. MAPIRANJE SISTEMSKIH OPERACIJA

### Tabela 4 iz dokumentacije - Status implementacije

| SK | Naziv | Operacije | GUI Status |
|----|-------|-----------|------------|
| SK1 | Kreiraj ŠV-20 Obrazac | ✅ Server OK | ❌ Forma nedostaje |
| SK2 | Pretraži ŠV-20 Obrazac | ✅ Server OK | ❌ Forma nedostaje |
| SK3 | Promeni ŠV-20 Obrazac | ✅ Server OK | ❌ Forma nedostaje |
| SK4 | Obriši ŠV-20 Obrazac | ✅ Server OK | ❌ Forma nedostaje |
| SK5 | Kreiraj Studenta | ✅ Server OK | ❌ Forma nedostaje |
| SK6 | Pretraži Studenta | ✅ Server OK | ❌ Forma nedostaje |
| SK7 | Promeni Studenta | ✅ Server OK | ❌ Forma nedostaje |
| SK8 | Obriši Studenta | ✅ Server OK | ❌ Forma nedostaje |
| SK9 | Prijavi Zaposlenog | ✅ Server OK | ✅ LoginForma OK |
| SK10 | Kreiraj Zaposlenog | ✅ Server OK | ❌ Forma nedostaje |
| SK11 | Pretraži Zaposlenog | ✅ Server OK | ❌ Forma nedostaje |
| SK12 | Promeni Zaposlenog | ✅ Server OK | ❌ Forma nedostaje |
| SK13 | Obriši Zaposlenog | ✅ Server OK | ❌ Forma nedostaje |
| SK14 | Kreiraj Tip Polja | ✅ Server OK | ✅ TipPoljaForma OK |
| SK15 | Pretraži Tip Polja | ✅ Server OK | ✅ TipPoljaForma OK |
| SK16 | Promeni Tip Polja | ✅ Server OK | ✅ TipPoljaForma OK |
| SK17 | Obriši Tip Polja | ✅ Server OK | ✅ TipPoljaForma OK |
| SK18 | Kreiraj Studijski Program | ✅ Server OK | ✅ StudijskiProgramForma OK |
| SK19 | Pretraži Studijski Program | ✅ Server OK | ✅ StudijskiProgramForma OK |
| SK20 | Promeni Studijski Program | ✅ Server OK | ✅ StudijskiProgramForma OK |
| SK21 | Obriši Studijski Program | ✅ Server OK | ✅ StudijskiProgramForma OK |
| SK22 | Pretraži Termin Dežurstva | ✅ Server OK | ✅ TerminDezurstvaForma OK |
| SK23 | Promeni Termin Dežurstva | ✅ Server OK | ✅ TerminDezurstvaForma OK |
| SK24 | Obriši Termin Dežurstva | ✅ Server OK | ✅ TerminDezurstvaForma OK |
| SK25 | Ubaci Termin Dežurstva | ✅ Server OK | ✅ TerminDezurstvaForma OK |
| SK26 | Kreiraj Stavku Obrasca | ✅ Server OK | ❌ Deo SV20 forme |
| SK27 | Pretraži Stavku Obrasca | ✅ Server OK | ❌ Deo SV20 forme |
| SK28 | Promeni Stavku Obrasca | ✅ Server OK | ❌ Deo SV20 forme |
| SK29 | Kreiraj Zaposleni-Termin | ✅ Server OK | ❌ Forma nedostaje |
| SK30 | Pretraži Zaposleni-Termin | ✅ Server OK | ❌ Forma nedostaje |

---

## 4. PLAN IMPLEMENTACIJE

### FAZA 1: SQL Skripta za bazu podataka
**Prioritet:** KRITIČAN
**Procenjeni obim:** 1 fajl

#### Zadaci:
1. Kreirati SQL skriptu `baza.sql` sa:
   - CREATE DATABASE bazaocr
   - CREATE TABLE za sve 8 tabela
   - Ograničenja (CONSTRAINTS) prema specifikaciji
   - INSERT za test podatke

#### Specifikacija tabela prema dokumentaciji:

```sql
-- 1. ZaposleniFakulteta
CREATE TABLE zaposlenifakulteta (
    idZaposlenog INT PRIMARY KEY AUTO_INCREMENT,
    ime VARCHAR(50) NOT NULL,
    prezime VARCHAR(50) NOT NULL,
    korisnickoIme VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    sifra VARCHAR(255) NOT NULL,
    CONSTRAINT chk_korisnicko_ime CHECK (korisnickoIme != prezime AND LENGTH(korisnickoIme) >= 5),
    CONSTRAINT chk_sifra CHECK (LENGTH(sifra) >= 8),
    CONSTRAINT chk_email CHECK (email LIKE '%@%')
);

-- 2. StudijskiProgram
CREATE TABLE studijskiprogram (
    idStudProgram INT PRIMARY KEY AUTO_INCREMENT,
    naziv VARCHAR(100) NOT NULL,
    oznaka VARCHAR(10) NOT NULL UNIQUE,
    stepenStudija ENUM('OAS', 'MAS', 'DAS') NOT NULL
);

-- 3. TipPolja
CREATE TABLE tippolja (
    idPolja INT PRIMARY KEY AUTO_INCREMENT,
    nazivPolja VARCHAR(100) NOT NULL,
    tipPodatka ENUM('TEXT', 'NUMERIC', 'ALPHANUMERIC', 'DATE', 'BOOLEAN') NOT NULL,
    regexValidacija VARCHAR(255),
    pozicijaX INT,
    pozicijaY INT,
    sirina INT,
    visina INT,
    stranica INT NOT NULL DEFAULT 1,
    redosledObrade INT,
    podrzavaOCR BOOLEAN NOT NULL DEFAULT TRUE,
    obaveznoPolje BOOLEAN NOT NULL DEFAULT FALSE
);

-- 4. TerminDezurstva
CREATE TABLE termindezurstva (
    idTerminDezurstva INT PRIMARY KEY AUTO_INCREMENT,
    tipTermina ENUM('PRVA SMENA', 'DRUGA SMENA', 'TRECA SMENA') NOT NULL,
    kancelarija VARCHAR(50) NOT NULL
);

-- 5. Student
CREATE TABLE student (
    indeks VARCHAR(20) PRIMARY KEY,
    jmbg VARCHAR(13) NOT NULL UNIQUE,
    ime VARCHAR(50) NOT NULL,
    prezime VARCHAR(50) NOT NULL,
    mestoRodjenja VARCHAR(100),
    adresaStanovanja VARCHAR(200),
    idStudProgram INT NOT NULL,
    FOREIGN KEY (idStudProgram) REFERENCES studijskiprogram(idStudProgram)
);

-- 6. SV20Obrazac
CREATE TABLE sv20obrazac (
    idObrazac INT PRIMARY KEY AUTO_INCREMENT,
    datumUnosa DATE NOT NULL,
    skolskaGodina INT NOT NULL,
    semestar INT NOT NULL,
    status ENUM('PODNET', 'U_OBRADI', 'VRACEN_NA_KOREKCIJU', 'ODOBREN', 'ODBIJEN') NOT NULL DEFAULT 'PODNET',
    putanjaFajla VARCHAR(500) NOT NULL,
    ocrIzvrseno BOOLEAN NOT NULL DEFAULT FALSE,
    brojUspesnihStavki INT NOT NULL DEFAULT 0,
    brojNeuspesnihStavki INT NOT NULL DEFAULT 0,
    idZaposlenog INT NOT NULL,
    indeks VARCHAR(20) NOT NULL,
    FOREIGN KEY (idZaposlenog) REFERENCES zaposlenifakulteta(idZaposlenog),
    FOREIGN KEY (indeks) REFERENCES student(indeks),
    UNIQUE KEY unique_obrazac (indeks, skolskaGodina, semestar)
);

-- 7. StavkeObrasca
CREATE TABLE stavkeobrasca (
    idStavke INT PRIMARY KEY AUTO_INCREMENT,
    idObrazac INT NOT NULL,
    ocrVrednost VARCHAR(500),
    korigovanaVrednost VARCHAR(500),
    nivoPodudarnosti DOUBLE DEFAULT 0,
    ocrUspesno BOOLEAN DEFAULT FALSE,
    idPolja INT NOT NULL,
    FOREIGN KEY (idObrazac) REFERENCES sv20obrazac(idObrazac) ON DELETE CASCADE,
    FOREIGN KEY (idPolja) REFERENCES tippolja(idPolja)
);

-- 8. ZaposleniTermin
CREATE TABLE zaposlenitermin (
    idZaposlenog INT NOT NULL,
    idTerminDezurstva INT NOT NULL,
    datum DATE NOT NULL,
    brojSati INT NOT NULL DEFAULT 8,
    vanredan BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (idZaposlenog, idTerminDezurstva, datum),
    FOREIGN KEY (idZaposlenog) REFERENCES zaposlenifakulteta(idZaposlenog),
    FOREIGN KEY (idTerminDezurstva) REFERENCES termindezurstva(idTerminDezurstva)
);
```

---

### FAZA 2: Implementacija StudentForma
**Prioritet:** VISOK
**Procenjeni obim:** 2 fajla

#### 2.1 StudentForma.java
Lokacija: `KLIJENT\src\forme\StudentForma.java`

**Elementi forme:**
- JTable za prikaz studenata (kolone: Indeks, JMBG, Ime, Prezime, Mesto, Adresa, Program)
- JTextField: txtIndeks, txtJmbg, txtIme, txtPrezime, txtMesto, txtAdresa
- JComboBox<StudijskiProgram>: cmbStudijskiProgram
- JButton: btnDodaj, btnSacuvaj, btnObrisi, btnOcisti, btnPretrazi
- JTextField za pretragu: txtPretraga
- JComboBox za kriterijum pretrage: cmbKriterijum (Po indeksu, Po programu)

**Validacije (prema dokumentaciji Tabela 5):**
- indeks: NOT NULL, format gggg/bbbb (npr. 2020/0342)
- jmbg: NOT NULL, UNIQUE, tačno 13 cifara
- ime: NOT NULL, length > 0
- prezime: NOT NULL, length > 0

#### 2.2 StudentController.java
Lokacija: `KLIJENT\src\kontroleri\StudentController.java`

**Metode:**
- `ucitajPodatke()` - poziva `Komunikacija.vratiSveStudente()`
- `dodaj()` - validacija + `Komunikacija.kreirajStudenta()`
- `sacuvaj()` - validacija + `Komunikacija.promeniStudenta()`
- `obrisi()` - potvrda + `Komunikacija.obrisiStudenta()`
- `pretrazi()` - `Komunikacija.vratiListuStudenata(kriterijum)`
- `ocisti()` - reset forme
- `popuniFormu()` - selekcija iz tabele
- `ucitajStudijskePrograme()` - popunjava ComboBox

---

### FAZA 3: Implementacija ZaposleniForma
**Prioritet:** VISOK
**Procenjeni obim:** 2 fajla

#### 3.1 ZaposleniForma.java
Lokacija: `KLIJENT\src\forme\ZaposleniForma.java`

**Elementi forme:**
- JTable za prikaz zaposlenih (kolone: ID, Ime, Prezime, Korisničko ime, Email)
- JTextField: txtIme, txtPrezime, txtKorisnickoIme, txtEmail
- JPasswordField: txtSifra, txtPotvrdaSifre
- JButton: btnDodaj, btnSacuvaj, btnObrisi, btnOcisti, btnPretrazi

**Validacije (prema dokumentaciji Tabela 1):**
- korisnickoIme: NOT NULL, UNIQUE, length >= 5, korisnickoIme != prezime
- email: NOT NULL, UNIQUE, mora sadržati "@"
- sifra: NOT NULL, length >= 8

#### 3.2 ZaposleniController.java
Lokacija: `KLIJENT\src\kontroleri\ZaposleniController.java`

**Metode:**
- `ucitajPodatke()` - poziva `Komunikacija.vratiSveZaposlene()`
- `dodaj()` - validacija + `Komunikacija.kreirajZaposlenog()`
- `sacuvaj()` - validacija + `Komunikacija.promeniZaposlenog()`
- `obrisi()` - potvrda + `Komunikacija.obrisiZaposlenog()`
- `pretrazi()` - `Komunikacija.vratiListuZaposlenih(kriterijum)`

---

### FAZA 4: Implementacija SV20ObrazacForma
**Prioritet:** VISOK
**Procenjeni obim:** 2 fajla

#### 4.1 SV20ObrazacForma.java
Lokacija: `KLIJENT\src\forme\SV20ObrazacForma.java`

**Elementi forme:**
- JTable za prikaz obrazaca (kolone: ID, Datum, Šk. godina, Semestar, Status, Student)
- JTable za stavke obrasca (kolone: ID, Polje, OCR vrednost, Korigovana, Podudarnost, Uspešno)
- JComboBox<Student>: cmbStudent
- JComboBox<ZaposleniFakulteta>: cmbZaposleni
- JSpinner: spnSkolskaGodina, spnSemestar
- JComboBox<Status>: cmbStatus
- JTextField: txtPutanjaFajla
- JButton: btnOdaberiFajl (JFileChooser za PDF/sliku)
- JButton: btnDodaj, btnSacuvaj, btnObrisi, btnPretrazi
- Panel za filtere: po studentu, po zaposlenom, po statusu

**Validacije (prema dokumentaciji Tabela 6):**
- datumUnosa <= danas
- UNIQUE(indeks, skolskaGodina, semestar)
- skolskaGodina >= godina iz indeksa studenta

#### 4.2 SV20ObrazacController.java
Lokacija: `KLIJENT\src\kontroleri\SV20ObrazacController.java`

**Metode:**
- `ucitajPodatke()` - poziva `Komunikacija.vratiListuSV20Obrazaca()`
- `ucitajStudente()` - popunjava ComboBox
- `ucitajZaposlene()` - popunjava ComboBox
- `ucitajStavke(idObrazac)` - poziva `Komunikacija.pretraziStavkeObrasca()`
- `dodaj()` - validacija + `Komunikacija.kreirajSV20Obrazac()`
- `sacuvaj()` - validacija + `Komunikacija.promeniSV20Obrazac()`
- `obrisi()` - potvrda + `Komunikacija.obrisiSV20Obrazac()`
- `pretrazi()` - filtriranje po kriterijumima
- `odaberiFajl()` - JFileChooser

---

### FAZA 5: Ažuriranje GlavnaFormaController
**Prioritet:** SREDNJI
**Procenjeni obim:** Modifikacija postojećeg fajla

#### Zadaci:
1. Ukloniti TODO poruke
2. Implementirati metode:
   - `otvoriStudent()` - kreira StudentForma i StudentController
   - `otvoriZaposleni()` - kreira ZaposleniForma i ZaposleniController
   - `otvoriSV20Obrazac()` - kreira SV20ObrazacForma i SV20ObrazacController

**Kod za izmenu u GlavnaFormaController.java:**
```java
private void otvoriStudent() {
    StudentForma forma = new StudentForma(gf);
    new StudentController(forma);
    forma.setVisible(true);
}

private void otvoriZaposleni() {
    ZaposleniForma forma = new ZaposleniForma(gf);
    new ZaposleniController(forma);
    forma.setVisible(true);
}

private void otvoriSV20Obrazac() {
    SV20ObrazacForma forma = new SV20ObrazacForma(gf);
    new SV20ObrazacController(forma);
    forma.setVisible(true);
}
```

---

### FAZA 6: Validacija i ograničenja
**Prioritet:** SREDNJI
**Procenjeni obim:** Modifikacija više fajlova

#### 6.1 Kreirati klasu Validator.java
Lokacija: `ZAJEDNICKI\src\domen\Validator.java`

```java
public class Validator {

    public static void validirajZaposlenog(ZaposleniFakulteta z) throws Exception {
        if (z.getKorisnickoIme() == null || z.getKorisnickoIme().length() < 5) {
            throw new Exception("Korisničko ime mora imati najmanje 5 karaktera!");
        }
        if (z.getKorisnickoIme().equals(z.getPrezime())) {
            throw new Exception("Korisničko ime ne može biti isto kao prezime!");
        }
        if (z.getEmail() == null || !z.getEmail().contains("@")) {
            throw new Exception("Email mora sadržati @!");
        }
        if (z.getSifra() == null || z.getSifra().length() < 8) {
            throw new Exception("Šifra mora imati najmanje 8 karaktera!");
        }
    }

    public static void validirajStudenta(Student s) throws Exception {
        if (s.getIndeks() == null || !s.getIndeks().matches("\\d{4}/\\d{4}")) {
            throw new Exception("Indeks mora biti u formatu gggg/bbbb!");
        }
        if (s.getJmbg() == null || !s.getJmbg().matches("\\d{13}")) {
            throw new Exception("JMBG mora imati tačno 13 cifara!");
        }
    }

    public static void validirajTipPolja(TipPolja tp) throws Exception {
        if (tp.isPodrzavaOCR()) {
            if (tp.getPozicijaX() < 0 || tp.getPozicijaY() < 0 ||
                tp.getSirina() <= 0 || tp.getVisina() <= 0) {
                throw new Exception("Za OCR polja moraju biti definisane pozicije i dimenzije!");
            }
        }
    }

    public static void validirajObrazac(SV20Obrazac o) throws Exception {
        if (o.getDatumUnosa().after(new Date())) {
            throw new Exception("Datum unosa ne može biti u budućnosti!");
        }
        // Provera godine iz indeksa
        int godinaIndeksa = Integer.parseInt(o.getIndeks().getIndeks().substring(0, 4));
        if (o.getSkolskaGodina() < godinaIndeksa) {
            throw new Exception("Školska godina ne može biti manja od godine upisa studenta!");
        }
    }
}
```

---

### FAZA 7: Pretraga po kriterijumima (Opciono)
**Prioritet:** NIZAK
**Procenjeni obim:** Modifikacija više fajlova

#### Zadaci:
1. Dodati podršku za višestruke kriterijume pretrage
2. Implementirati u Komunikacija.java dodatne metode:
   - `vratiListuSV20Obrazaca(kriterijumStudent)`
   - `vratiListuSV20Obrazaca(kriterijumZaposleni)`
   - `vratiListuSV20Obrazaca(kriterijumTipPolja)`

---

### FAZA 8: OCR Integracija (Opciono - Napredno)
**Prioritet:** NIZAK
**Procenjeni obim:** Nova biblioteka + servisi

#### Zadaci:
1. Dodati Tesseract OCR biblioteku
2. Kreirati OCRService.java
3. Implementirati prepoznavanje teksta sa slike
4. Povezati sa TipPolja (pozicije, dimenzije)

---

## 5. REDOSLED IMPLEMENTACIJE

```
┌─────────────────────────────────────────────────────────────────┐
│  FAZA 1: SQL Skripta (KRITIČNO - prvo uraditi)                  │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│  FAZA 2: StudentForma + StudentController                       │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│  FAZA 3: ZaposleniForma + ZaposleniController                   │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│  FAZA 4: SV20ObrazacForma + SV20ObrazacController               │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│  FAZA 5: Ažuriranje GlavnaFormaController                       │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│  FAZA 6: Validacija (Validator.java)                            │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│  FAZA 7-8: Opcione faze (pretraga, OCR)                         │
└─────────────────────────────────────────────────────────────────┘
```

---

## 6. CHECKLIST ZA KOMPLETNOST

### Pre predaje seminarske:
- [ ] SQL skripta funkcioniše
- [ ] Svi šifarnici rade (StudijskiProgram, TerminDezurstva, TipPolja) ✅
- [ ] StudentForma kompletna sa CRUD operacijama
- [ ] ZaposleniForma kompletna sa CRUD operacijama
- [ ] SV20ObrazacForma kompletna sa CRUD operacijama
- [ ] GlavnaForma navigacija funkcioniše
- [ ] Login funkcioniše ✅
- [ ] Validacija implementirana
- [ ] Test podaci uneti u bazu

### Opciono:
- [ ] Pretraga po više kriterijuma
- [ ] OCR integracija
- [ ] Unit testovi
- [ ] Javadoc dokumentacija

---

## 7. STATISTIKA PROJEKTA

| Metrika | Vrednost |
|---------|----------|
| Ukupno klasa | 95+ |
| Domenskih objekata | 8 |
| Enum-a | 4 |
| Sistemskih operacija (server) | 45 |
| GUI formi | 8 (5 kompletnih, 3 prazne) |
| Kontrolera | 5 (3 kompletna, 2 nedostaju) |
| Procenjene linije koda | 15,000+ |

---

## 8. TEHNOLOGIJE

- **Programski jezik:** Java 8+
- **GUI:** Swing
- **Baza podataka:** MySQL 8.0
- **Komunikacija:** Java Socket API
- **IDE:** NetBeans (preporučeno)
- **Build:** Ant (NetBeans default)

---

## 9. NAPOMENE

1. **Server mora biti pokrenut pre klijenta** - klijent se povezuje na localhost:9000
2. **Baza mora biti kreirana pre pokretanja** - koristiti SQL skriptu iz Faze 1
3. **Konfiguracija baze** - SERVER\src\konfiguracija\dbconfig.properties
4. **Redosled pokretanja:** Baza → Server → Klijent

---

*Dokument kreiran: Januar 2026*
*Poslednje ažuriranje: Implementacioni plan v1.0*
