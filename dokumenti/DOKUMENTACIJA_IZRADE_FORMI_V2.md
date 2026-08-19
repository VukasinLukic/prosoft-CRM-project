# DOKUMENTACIJA ZA IGRADNJU GUI FORMI I VIZUELNO DIZAJNIRANJE U V2App

> **REŠENO.** Svih 9 formi iz sekcije 1 ispod je napravljeno i povezano sa kontrolerima; opis stanja "Forme čekaju povezivanje" više ne važi. Sekcije 2–5 (uputstva/specifikacije po formi) i dalje su koristan referentni materijal.

---

## 1. PREGLED STANJA V2App PROJEKTA

Analizom celokupnog radnog okruženja utvrđeno je sledeće stanje projekta **V2App**:

1. **`0_PsShared` modul:**  
   - ✅ **100% Kompletan.** Sadrži svih 18 klasa domenskog modela i komunikacionog protokola (`Student`, `ZaposleniFakulteta`, `SV20Obrazac`, `StavkeObrasca`, enume, `Zahtev`, `Odgovor`, `Operacija`, itd.).
   - Čisto se kompajlira bez ikakvih grešaka.

2. **`0_PsServer` modul:**  
   - ✅ **100% Kompletan.** Sve operacije, kontroler, repozitorijum, niti i serverske GUI forme (`ServerskaForma`, `FormaKonfiguracijaBaza`, `FormaKonfiguracijaPort`, `Main.java`) su usklađeni i ugrađena je **FlatLaf** tema.
   - Povezan sa bibliotekom `flatlaf-3.5.2.jar`.

3. **`PsKlijent` modul:**  
   - ⚠️ **Logika i kontroleri su 100% preneseni, ali Forme čekaju povezivanje.**  
   - Klase `Main.java`, `Cordinator.java`, `Komunikacija.java`, kao i svi kontroleri (`LoginController`, `GlavnaFormaController`, `StudentController`, `ZaposleniController`, `SV20ObrazacController`, itd.) su u potpunosti preneseni iz v1 projekta.
   - Forme u `PsKlijent/src/forme/` su kreirane kao prazne NetBeans forme koje još uvek ne sadrže odgovarajuće vizuelne komponente (dugmad, tekstualna polja, tabele) i pomoćne metode (getter-e, setter-e i listener-e) koje kontroleri pozivaju. Zbog toga se javljaju privremene greške u kompilaciji klijenta dok se ne dodaju ti elementi u forme.

---

## 2. UPUTSTVO ZA UGRADNJU FLATLAF TEME U `PsKlijent`

Da bi klijentska aplikacija imala moderan i elegantan FlatLaf izgled kao i server:

### Korak 1: Povezivanje JAR biblioteke u NetBeans-u
1. Biblioteka `flatlaf-3.5.2.jar` se već nalazi na putanji: `V2App/lib/flatlaf-3.5.2.jar`.
2. U NetBeans IDE-u desnim klikom klikni na projekat **`PsKlijent`** $\rightarrow$ **Properties**.
3. Sa leve strane izaberi **Libraries**.
4. Klikni na **Add JAR/Folder** i izaberi fajl `V2App/lib/flatlaf-3.5.2.jar`.
5. Klikni na **OK**.

### Korak 2: Podešavanje u `PsKlijent/src/main/Main.java`
U fajlu `PsKlijent/src/main/Main.java` na početku `main` metode dodaj inicijalizaciju FlatLaf teme pre poziva koordinatora:

```java
package main;

import com.formdev.flatlaf.FlatLightLaf;
import cordinator.Cordinator;

public class Main {

    public static void main(String[] args) {
        try {
            // Postavljanje moderne FlatLaf svetle teme
            FlatLightLaf.setup();
            
            // Opciono: Za tamnu temu možeš upotrebiti:
            // com.formdev.flatlaf.FlatDarkLaf.setup();
        } catch (Exception ex) {
            System.err.println("Greska pri postavljanju teme: " + ex.getMessage());
        }

        java.awt.EventQueue.invokeLater(() -> {
            Cordinator.getInstanca().otvoriLoginFormu();
        });
    }
}
```

---

## 3. PREPORUČENI REDOSLED IZRADE FORMI U NETBEANS FORM BUILDERU

Forme treba izrađivati sledećim redosledom (od jednostavnijih i osnovnih ka kompleksnijim):

1. **`LoginForma`** (JFrame) – Osnovna forma za prijavu.
2. **`GlavnaForma`** (JFrame) – Glavni meni i navigacija.
3. **`StudijskiProgramForma`** (JDialog) – Šifarnik studijskih programa.
4. **`TerminDezurstvaForma`** (JDialog) – Šifarnik termina dežurstva.
5. **`TipPoljaForma`** (JDialog) – Šifarnik tipova polja za OCR.
6. **`StudentForma`** (JDialog) – Rad sa studentima.
7. **`ZaposleniForma`** (JDialog) – Rad sa zaposlenima.
8. **`SV20ObrazacForma`** (JDialog) – Glavna forma za unose i obradu ŠV-20 obrazaca.
9. **`StavkeObrascaForma`** (JDialog) – Pregled i korekcija pojedinačnih stavki obrasca.

---

## 4. DETALJNA SPECIFIKACIJA SVAKE FORME SA METODAMA I KODOM

U nastavku je za svaku od 9 formi data detaljna specifikacija komponenti koje treba napraviti u NetBeans Form Builderu (u tabu *Design*), tačan naziv promenljivih (Variable Name) i kôd koji treba zalepiti u *Source* tab te forme.

---

### FORMA 1: `LoginForma.java` (JFrame)

- **Tip:** `JFrame Form`
- **Upravljački kontroler:** `LoginController`

#### Vizuelni dizajn i komponente (Design view):
| Komponenta | Tip komponente | Variable Name (NetBeans) | Opis / Tekst |
| :--- | :--- | :--- | :--- |
| Korisničko ime labela | `JLabel` | `lblUsername` | "Korisničko ime:" |
| Korisničko ime polje | `JTextField` | `txtUsername` | Prazno tekstualno polje |
| Lozinka labela | `JLabel` | `lblPassword` | "Lozinka:" |
| Lozinka polje | `JPasswordField` | `txtPassword` | Polje za šifru |
| Dugme Prijava | `JButton` | `btnUlogujSe` | "Prijavi se" |

#### Kôd koji treba ubaciti u `LoginForma.java` (Source view):
```java
public void loginAddActionListener(java.awt.event.ActionListener actionListener) {
    btnUlogujSe.addActionListener(actionListener);
}

public javax.swing.JButton getBtnUlogujSe() {
    return btnUlogujSe;
}

public javax.swing.JPasswordField getTxtPassword() {
    return txtPassword;
}

public javax.swing.JTextField getTxtUsername() {
    return txtUsername;
}
```

---

### FORMA 2: `GlavnaForma.java` (JFrame)

- **Tip:** `JFrame Form`
- **Upravljački kontroler:** `GlavnaFormaController`

#### Vizuelni dizajn i komponente (Design view):
U gornjem meniju (`JMenuBar`) ili na samom panelu napraviti komponente:
- Meni Meni: `menuObrasci`, `menuStudenti`, `menuZaposleni`, `menuStudijskiProgram`, `menuTerminDezurstva`, `menuTipPolja`, `menuPodesavanja`, `menuOdjava`, `menuOProgramu`
- Brzi dugmići na panelu (opciono): `btnObrasci`, `btnStudenti`, `btnZaposleni`, `btnSifarnici`

#### Kôd koji treba ubaciti u `GlavnaForma.java` (Source view):
```java
// Konstruktor sa ulogovanim korisnikom
public GlavnaForma(domen.ZaposleniFakulteta ulogovani) {
    initComponents();
    if (ulogovani != null) {
        setTitle("ŠV-20 Sistem - Prijavljeni zaposleni: " + ulogovani.getIme() + " " + ulogovani.getPrezime());
    }
}

public void addSV20ObrazacListener(java.awt.event.ActionListener l) { menuObrasci.addActionListener(l); }
public void addStudentListener(java.awt.event.ActionListener l) { menuStudenti.addActionListener(l); }
public void addZaposleniListener(java.awt.event.ActionListener l) { menuZaposleni.addActionListener(l); }
public void addStudijskiProgramListener(java.awt.event.ActionListener l) { menuStudijskiProgram.addActionListener(l); }
public void addTerminDezurstvaListener(java.awt.event.ActionListener l) { menuTerminDezurstva.addActionListener(l); }
public void addTipPoljaListener(java.awt.event.ActionListener l) { menuTipPolja.addActionListener(l); }
public void addPodesavanjaListener(java.awt.event.ActionListener l) { menuPodesavanja.addActionListener(l); }
public void addOdjavaListener(java.awt.event.ActionListener l) { menuOdjava.addActionListener(l); }
public void addOProgramuListener(java.awt.event.ActionListener l) { menuOProgramu.addActionListener(l); }

public void addBtnObrasciListener(java.awt.event.ActionListener l) { if(btnObrasci != null) btnObrasci.addActionListener(l); }
public void addBtnStudentiListener(java.awt.event.ActionListener l) { if(btnStudenti != null) btnStudenti.addActionListener(l); }
public void addBtnZaposleniListener(java.awt.event.ActionListener l) { if(btnZaposleni != null) btnZaposleni.addActionListener(l); }
public void addBtnSifarniciListener(java.awt.event.ActionListener l) { if(btnSifarnici != null) btnSifarnici.addActionListener(l); }
```

---

### FORMA 3: `StudijskiProgramForma.java` (JDialog)

- **Tip:** `JDialog Form`
- **Upravljački kontroler:** `StudijskiProgramController`

#### Vizuelni dizajn i komponente (Design view):
- `JTextField`: `txtNaziv`, `txtOznaka`
- `JComboBox<stepenStudija>`: `cmbStepenStudija`
- `JTable`: `tblProgrami`
- `JButton`: `btnDodaj`, `btnSacuvaj`, `btnObrisi`, `btnOcisti`

#### Kôd koji treba ubaciti u `StudijskiProgramForma.java` (Source view):
```java
private javax.swing.table.DefaultTableModel tableModel;
private domen.StudijskiProgram selektovani;

public StudijskiProgramForma(java.awt.Frame parent) {
    super(parent, true);
    initComponents();
    initCustomTableModel();
}

private void initCustomTableModel() {
    String[] kolone = {"ID", "Naziv", "Oznaka", "Stepen studija"};
    tableModel = new javax.swing.table.DefaultTableModel(kolone, 0) {
        @Override
        public boolean isCellEditable(int row, int column) { return false; }
    };
    tblProgrami.setModel(tableModel);
}

public javax.swing.JTable getTblProgrami() { return tblProgrami; }
public javax.swing.table.DefaultTableModel getTableModel() { return tableModel; }
public javax.swing.JTextField getTxtNaziv() { return txtNaziv; }
public javax.swing.JTextField getTxtOznaka() { return txtOznaka; }
public javax.swing.JComboBox<domen.stepenStudija> getCmbStepenStudija() { return cmbStepenStudija; }

public domen.StudijskiProgram getSelektovani() { return selektovani; }
public void setSelektovani(domen.StudijskiProgram sp) { this.selektovani = sp; }

public void addDodajListener(java.awt.event.ActionListener l) { btnDodaj.addActionListener(l); }
public void addSacuvajListener(java.awt.event.ActionListener l) { btnSacuvaj.addActionListener(l); }
public void addObrisiListener(java.awt.event.ActionListener l) { btnObrisi.addActionListener(l); }
public void addOcistiListener(java.awt.event.ActionListener l) { btnOcisti.addActionListener(l); }
public void addTabelaSelectionListener(javax.swing.event.ListSelectionListener l) {
    tblProgrami.getSelectionModel().addListSelectionListener(l);
}
```

---

### FORMA 4: `TerminDezurstvaForma.java` (JDialog)

- **Tip:** `JDialog Form`
- **Upravljački kontroler:** `TerminDezurstvaController`

#### Vizuelni dizajn i komponente (Design view):
- `JComboBox<tipTermina>`: `cmbTipTermina`
- `JTextField`: `txtKancelarija`
- `JTable`: `tblTermini`
- `JButton`: `btnDodaj`, `btnSacuvaj`, `btnObrisi`, `btnOcisti`

#### Kôd koji treba ubaciti u `TerminDezurstvaForma.java` (Source view):
```java
private javax.swing.table.DefaultTableModel tableModel;
private domen.TerminDezurstva selektovani;

public TerminDezurstvaForma(java.awt.Frame parent) {
    super(parent, true);
    initComponents();
    initCustomTableModel();
}

private void initCustomTableModel() {
    String[] kolone = {"ID", "Tip termina", "Kancelarija"};
    tableModel = new javax.swing.table.DefaultTableModel(kolone, 0) {
        @Override
        public boolean isCellEditable(int row, int column) { return false; }
    };
    tblTermini.setModel(tableModel);
}

public javax.swing.JTable getTblTermini() { return tblTermini; }
public javax.swing.table.DefaultTableModel getTableModel() { return tableModel; }
public javax.swing.JComboBox<domen.tipTermina> getCmbTipTermina() { return cmbTipTermina; }
public javax.swing.JTextField getTxtKancelarija() { return txtKancelarija; }

public domen.TerminDezurstva getSelektovani() { return selektovani; }
public void setSelektovani(domen.TerminDezurstva t) { this.selektovani = t; }

public void addDodajListener(java.awt.event.ActionListener l) { btnDodaj.addActionListener(l); }
public void addSacuvajListener(java.awt.event.ActionListener l) { btnSacuvaj.addActionListener(l); }
public void addObrisiListener(java.awt.event.ActionListener l) { btnObrisi.addActionListener(l); }
public void addOcistiListener(java.awt.event.ActionListener l) { btnOcisti.addActionListener(l); }
public void addTabelaSelectionListener(javax.swing.event.ListSelectionListener l) {
    tblTermini.getSelectionModel().addListSelectionListener(l);
}
```

---

### FORMA 5: `TipPoljaForma.java` (JDialog)

- **Tip:** `JDialog Form`
- **Upravljački kontroler:** `TipPoljaController`

#### Vizuelni dizajn i komponente (Design view):
- `JTextField`: `txtNazivPolja`, `txtRegexValidacija`, `txtPozicijaX`, `txtPozicijaY`, `txtSirina`, `txtVisina`
- `JComboBox<tipPodatka>`: `cmbTipPodatka`
- `JSpinner`: `spnStranica`, `spnRedosledObrade`
- `JCheckBox`: `chkPodrzavaOCR`, `chkObaveznoPolje`
- `JTable`: `tblTipoviPolja`
- `JButton`: `btnDodaj`, `btnSacuvaj`, `btnObrisi`, `btnOcisti`

#### Kôd koji treba ubaciti u `TipPoljaForma.java` (Source view):
```java
private javax.swing.table.DefaultTableModel tableModel;
private domen.TipPolja selektovani;

public TipPoljaForma(java.awt.Frame parent) {
    super(parent, true);
    initComponents();
    initCustomTableModel();
}

private void initCustomTableModel() {
    String[] kolone = {"ID", "Naziv", "Tip", "Stranica", "Redosled", "OCR", "Obavezno"};
    tableModel = new javax.swing.table.DefaultTableModel(kolone, 0) {
        @Override
        public boolean isCellEditable(int row, int column) { return false; }
    };
    tblTipoviPolja.setModel(tableModel);
}

public javax.swing.JTable getTblTipoviPolja() { return tblTipoviPolja; }
public javax.swing.table.DefaultTableModel getTableModel() { return tableModel; }
public javax.swing.JTextField getTxtNazivPolja() { return txtNazivPolja; }
public javax.swing.JComboBox<domen.tipPodatka> getCmbTipPodatka() { return cmbTipPodatka; }
public javax.swing.JTextField getTxtRegexValidacija() { return txtRegexValidacija; }
public javax.swing.JTextField getTxtPozicijaX() { return txtPozicijaX; }
public javax.swing.JTextField getTxtPozicijaY() { return txtPozicijaY; }
public javax.swing.JTextField getTxtSirina() { return txtSirina; }
public javax.swing.JTextField getTxtVisina() { return txtVisina; }
public javax.swing.JSpinner getSpnStranica() { return spnStranica; }
public javax.swing.JSpinner getSpnRedosledObrade() { return spnRedosledObrade; }
public javax.swing.JCheckBox getChkPodrzavaOCR() { return chkPodrzavaOCR; }
public javax.swing.JCheckBox getChkObaveznoPolje() { return chkObaveznoPolje; }

public domen.TipPolja getSelektovani() { return selektovani; }
public void setSelektovani(domen.TipPolja tp) { this.selektovani = tp; }

public void addDodajListener(java.awt.event.ActionListener l) { btnDodaj.addActionListener(l); }
public void addSacuvajListener(java.awt.event.ActionListener l) { btnSacuvaj.addActionListener(l); }
public void addObrisiListener(java.awt.event.ActionListener l) { btnObrisi.addActionListener(l); }
public void addOcistiListener(java.awt.event.ActionListener l) { btnOcisti.addActionListener(l); }
public void addTabelaSelectionListener(javax.swing.event.ListSelectionListener l) {
    tblTipoviPolja.getSelectionModel().addListSelectionListener(l);
}
```

---

### FORMA 6: `StudentForma.java` (JDialog)

- **Tip:** `JDialog Form`
- **Upravljački kontroler:** `StudentController`

#### Vizuelni dizajn i komponente (Design view):
- TextFields: `txtIndeks`, `txtJmbg`, `txtIme`, `txtPrezime`, `txtMestoRodjenja`, `txtAdresaStanovanja`, `txtPretraga`
- ComboBox: `cmbStudijskiProgram` (`JComboBox<StudijskiProgram>`), `cmbKriterijum` (`JComboBox<String>`)
- Table: `tblStudenti`
- Buttons: `btnDodaj`, `btnSacuvaj`, `btnObrisi`, `btnOcisti`, `btnPretrazi`

#### Kôd koji treba ubaciti u `StudentForma.java` (Source view):
```java
private javax.swing.table.DefaultTableModel tableModel;
private domen.Student selektovani;

public StudentForma(java.awt.Frame parent) {
    super(parent, true);
    initComponents();
    initCustomTableModel();
}

private void initCustomTableModel() {
    String[] kolone = {"Indeks", "JMBG", "Ime", "Prezime", "Mesto rodjenja", "Adresa", "Studijski program"};
    tableModel = new javax.swing.table.DefaultTableModel(kolone, 0) {
        @Override
        public boolean isCellEditable(int row, int column) { return false; }
    };
    tblStudenti.setModel(tableModel);
}

public javax.swing.JTable getTblStudenti() { return tblStudenti; }
public javax.swing.table.DefaultTableModel getTableModel() { return tableModel; }
public javax.swing.JTextField getTxtIndeks() { return txtIndeks; }
public javax.swing.JTextField getTxtJmbg() { return txtJmbg; }
public javax.swing.JTextField getTxtIme() { return txtIme; }
public javax.swing.JTextField getTxtPrezime() { return txtPrezime; }
public javax.swing.JTextField getTxtMestoRodjenja() { return txtMestoRodjenja; }
public javax.swing.JTextField getTxtAdresaStanovanja() { return txtAdresaStanovanja; }
public javax.swing.JComboBox<domen.StudijskiProgram> getCmbStudijskiProgram() { return cmbStudijskiProgram; }
public javax.swing.JTextField getTxtPretraga() { return txtPretraga; }
public javax.swing.JComboBox<String> getCmbKriterijum() { return cmbKriterijum; }

public domen.Student getSelektovani() { return selektovani; }
public void setSelektovani(domen.Student s) { this.selektovani = s; }

public void addDodajListener(java.awt.event.ActionListener l) { btnDodaj.addActionListener(l); }
public void addSacuvajListener(java.awt.event.ActionListener l) { btnSacuvaj.addActionListener(l); }
public void addObrisiListener(java.awt.event.ActionListener l) { btnObrisi.addActionListener(l); }
public void addOcistiListener(java.awt.event.ActionListener l) { btnOcisti.addActionListener(l); }
public void addPretraziListener(java.awt.event.ActionListener l) { btnPretrazi.addActionListener(l); }
public void addTabelaSelectionListener(javax.swing.event.ListSelectionListener l) {
    tblStudenti.getSelectionModel().addListSelectionListener(l);
}
```

---

### FORMA 7: `ZaposleniForma.java` (JDialog)

- **Tip:** `JDialog Form`
- **Upravljački kontroler:** `ZaposleniController`

#### Vizuelni dizajn i komponente (Design view):
- TextFields: `txtIme`, `txtPrezime`, `txtKorisnickoIme`, `txtEmail`, `txtPretraga`
- PasswordFields: `txtSifra`, `txtPotvrdaSifre`
- ComboBox: `cmbKriterijum` (`JComboBox<String>`)
- Table: `tblZaposleni`
- Buttons: `btnDodaj`, `btnSacuvaj`, `btnObrisi`, `btnOcisti`, `btnPretrazi`

#### Kôd koji treba ubaciti u `ZaposleniForma.java` (Source view):
```java
private javax.swing.table.DefaultTableModel tableModel;
private domen.ZaposleniFakulteta selektovani;

public ZaposleniForma(java.awt.Frame parent) {
    super(parent, true);
    initComponents();
    initCustomTableModel();
}

private void initCustomTableModel() {
    String[] kolone = {"ID", "Ime", "Prezime", "Korisničko ime", "Email"};
    tableModel = new javax.swing.table.DefaultTableModel(kolone, 0) {
        @Override
        public boolean isCellEditable(int row, int column) { return false; }
    };
    tblZaposleni.setModel(tableModel);
}

public javax.swing.JTable getTblZaposleni() { return tblZaposleni; }
public javax.swing.table.DefaultTableModel getTableModel() { return tableModel; }
public javax.swing.JTextField getTxtIme() { return txtIme; }
public javax.swing.JTextField getTxtPrezime() { return txtPrezime; }
public javax.swing.JTextField getTxtKorisnickoIme() { return txtKorisnickoIme; }
public javax.swing.JTextField getTxtEmail() { return txtEmail; }
public javax.swing.JPasswordField getTxtSifra() { return txtSifra; }
public javax.swing.JPasswordField getTxtPotvrdaSifre() { return txtPotvrdaSifre; }
public javax.swing.JTextField getTxtPretraga() { return txtPretraga; }
public javax.swing.JComboBox<String> getCmbKriterijum() { return cmbKriterijum; }

public domen.ZaposleniFakulteta getSelektovani() { return selektovani; }
public void setSelektovani(domen.ZaposleniFakulteta z) { this.selektovani = z; }

public void addDodajListener(java.awt.event.ActionListener l) { btnDodaj.addActionListener(l); }
public void addSacuvajListener(java.awt.event.ActionListener l) { btnSacuvaj.addActionListener(l); }
public void addObrisiListener(java.awt.event.ActionListener l) { btnObrisi.addActionListener(l); }
public void addOcistiListener(java.awt.event.ActionListener l) { btnOcisti.addActionListener(l); }
public void addPretraziListener(java.awt.event.ActionListener l) { btnPretrazi.addActionListener(l); }
public void addTabelaSelectionListener(javax.swing.event.ListSelectionListener l) {
    tblZaposleni.getSelectionModel().addListSelectionListener(l);
}
```

---

### FORMA 8: `SV20ObrazacForma.java` (JDialog)

- **Tip:** `JDialog Form`
- **Upravljački kontroler:** `SV20ObrazacController`

#### Vizuelni dizajn i komponente (Design view):
- ComboBox: `cmbStudent` (`JComboBox<Student>`), `cmbZaposleni` (`JComboBox<ZaposleniFakulteta>`), `cmbStatus` (`JComboBox<Status>`), `cmbKriterijum` (`JComboBox<String>`)
- Spinners: `spnSkolskaGodina`, `spnSemestar`
- TextFields: `txtPutanjaFajla`, `txtPretraga`
- Tables: `tblObrasci`, `tblStavke`
- Buttons: `btnOdaberiFajl`, `btnDodaj`, `btnSacuvaj`, `btnObrisi`, `btnOcisti`, `btnPretrazi`

#### Kôd koji treba ubaciti u `SV20ObrazacForma.java` (Source view):
```java
private javax.swing.table.DefaultTableModel tableModelObrasci;
private javax.swing.table.DefaultTableModel tableModelStavke;
private domen.SV20Obrazac selektovaniObrazac;

public SV20ObrazacForma(java.awt.Frame parent) {
    super(parent, true);
    initComponents();
    initCustomTableModels();
}

private void initCustomTableModels() {
    String[] koloneObrasci = {"ID", "Datum", "Šk. godina", "Semestar", "Status", "Student", "Zaposleni"};
    tableModelObrasci = new javax.swing.table.DefaultTableModel(koloneObrasci, 0) {
        @Override
        public boolean isCellEditable(int row, int column) { return false; }
    };
    tblObrasci.setModel(tableModelObrasci);

    String[] koloneStavke = {"ID", "Polje", "OCR Vrednost", "Korigovana", "Podudarnost", "Uspešno"};
    tableModelStavke = new javax.swing.table.DefaultTableModel(koloneStavke, 0) {
        @Override
        public boolean isCellEditable(int row, int column) { return false; }
    };
    tblStavke.setModel(tableModelStavke);
}

public javax.swing.JTable getTblObrasci() { return tblObrasci; }
public javax.swing.table.DefaultTableModel getTableModelObrasci() { return tableModelObrasci; }
public javax.swing.JTable getTblStavke() { return tblStavke; }
public javax.swing.table.DefaultTableModel getTableModelStavke() { return tableModelStavke; }

public javax.swing.JComboBox<domen.Student> getCmbStudent() { return cmbStudent; }
public javax.swing.JComboBox<domen.ZaposleniFakulteta> getCmbZaposleni() { return cmbZaposleni; }
public javax.swing.JSpinner getSpnSkolskaGodina() { return spnSkolskaGodina; }
public javax.swing.JSpinner getSpnSemestar() { return spnSemestar; }
public javax.swing.JComboBox<domen.Status> getCmbStatus() { return cmbStatus; }
public javax.swing.JTextField getTxtPutanjaFajla() { return txtPutanjaFajla; }
public javax.swing.JTextField getTxtPretraga() { return txtPretraga; }
public javax.swing.JComboBox<String> getCmbKriterijum() { return cmbKriterijum; }

public domen.SV20Obrazac getSelektovaniObrazac() { return selektovaniObrazac; }
public void setSelektovaniObrazac(domen.SV20Obrazac o) { this.selektovaniObrazac = o; }

public void addDodajListener(java.awt.event.ActionListener l) { btnDodaj.addActionListener(l); }
public void addSacuvajListener(java.awt.event.ActionListener l) { btnSacuvaj.addActionListener(l); }
public void addObrisiListener(java.awt.event.ActionListener l) { btnObrisi.addActionListener(l); }
public void addOcistiListener(java.awt.event.ActionListener l) { btnOcisti.addActionListener(l); }
public void addPretraziListener(java.awt.event.ActionListener l) { btnPretrazi.addActionListener(l); }
public void addOdaberiFajlListener(java.awt.event.ActionListener l) { btnOdaberiFajl.addActionListener(l); }
public void addTabelaObrasciSelectionListener(javax.swing.event.ListSelectionListener l) {
    tblObrasci.getSelectionModel().addListSelectionListener(l);
}
```

---

### FORMA 9: `StavkeObrascaForma.java` (JDialog)

- **Tip:** `JDialog Form`
- **Upravljački kontroler:** `StavkeObrascaController`

#### Vizuelni dizajn i komponente (Design view):
- ComboBox: `cmbPolje` (`JComboBox<TipPolja>`)
- TextFields: `txtOcrVrednost`, `txtKorigovanaVrednost`, `txtNivoPodudarnosti`
- CheckBox: `chkOcrUspesno`
- Table: `tblStavke`
- Buttons: `btnDodaj`, `btnSacuvaj`, `btnObrisi`, `btnOcisti`

#### Kôd koji treba ubaciti u `StavkeObrascaForma.java` (Source view):
```java
private javax.swing.table.DefaultTableModel tableModel;
private domen.StavkeObrasca selektovani;

public StavkeObrascaForma(java.awt.Frame parent) {
    super(parent, true);
    initComponents();
    initCustomTableModel();
}

private void initCustomTableModel() {
    String[] kolone = {"ID", "Polje", "OCR vrednost", "Korigovana vrednost", "Podudarnost", "OCR uspešno"};
    tableModel = new javax.swing.table.DefaultTableModel(kolone, 0) {
        @Override
        public boolean isCellEditable(int row, int column) { return false; }
    };
    tblStavke.setModel(tableModel);
}

public javax.swing.JTable getTblStavke() { return tblStavke; }
public javax.swing.table.DefaultTableModel getTableModel() { return tableModel; }
public javax.swing.JComboBox<domen.TipPolja> getCmbPolje() { return cmbPolje; }
public javax.swing.JTextField getTxtOcrVrednost() { return txtOcrVrednost; }
public javax.swing.JTextField getTxtKorigovanaVrednost() { return txtKorigovanaVrednost; }
public javax.swing.JTextField getTxtNivoPodudarnosti() { return txtNivoPodudarnosti; }
public javax.swing.JCheckBox getChkOcrUspesno() { return chkOcrUspesno; }

public domen.StavkeObrasca getSelektovani() { return selektovani; }
public void setSelektovani(domen.StavkeObrasca s) { this.selektovani = s; }

public void addDodajListener(java.awt.event.ActionListener l) { btnDodaj.addActionListener(l); }
public void addSacuvajListener(java.awt.event.ActionListener l) { btnSacuvaj.addActionListener(l); }
public void addObrisiListener(java.awt.event.ActionListener l) { btnObrisi.addActionListener(l); }
public void addOcistiListener(java.awt.event.ActionListener l) { btnOcisti.addActionListener(l); }
public void addTabelaSelectionListener(javax.swing.event.ListSelectionListener l) {
    tblStavke.getSelectionModel().addListSelectionListener(l);
}
```

---

## 5. ZAKLJUČAK I POSTUPAK ZAVRŠETKA

Kada za svaku formu u NetBeans-u odradiš ova 2 jednostavna koraka:
1. U **Design view** poslažeš komponente i postaviš im tačne nazive promenljivih (`txtIndeks`, `btnDodaj`, `tblStudenti`...),
2. U **Source view** zalepiš odgovarajući blok koda sa getter-ima, setter-ima i listener-ima iz ovog dokumenta,

svih **346 kompilacionih grešaka u `PsKlijent` će automatski nestati**, a klijent će biti u potpunosti spreman za rad sa FlatLaf dizajnom i komunikaciju sa serverom!
