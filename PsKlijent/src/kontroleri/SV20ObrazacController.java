package kontroleri;

import cordinator.Cordinator;
import domen.SV20Obrazac;
import domen.Status;
import domen.StavkeObrasca;
import domen.Student;
import domen.TipPolja;
import domen.ZaposleniFakulteta;
import forme.SV20ObrazacForma;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import komunikacija.Komunikacija;

public class SV20ObrazacController {

    private final SV20ObrazacForma forma;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    // Puni objekti sa servera (za razliku od tabele koja drži samo formatirane stringove/int-ove za prikaz) -
    // koriste se u popuniFormu() da selektovani obrazac zadrži SVA polja (npr. datumUnosa), umesto da se
    // ručno rekonstruiše iz ćelija tabele i time izgubi ono što tabela ne prikazuje.
    private List<SV20Obrazac> ucitaniObrasci = new ArrayList<>();
    private List<ZaposleniFakulteta> sviZaposleni = new ArrayList<>();

    // Stanje kartice "OCR pregled"
    private int stranaPregled = 1;
    private int maxStranaPregled = 1;
    private SV20Obrazac obrazacUPregledu;
    private List<StavkeObrasca> stavkeUPregledu = new ArrayList<>();

    public SV20ObrazacController(SV20ObrazacForma forma) {
        this.forma = forma;
        addActionListeners();
        ucitajStudente();
        ucitajZaposlene();
        ucitajStatuse();
        ucitajPodatke();
    }

    private void addActionListeners() {

        forma.addDodajListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { dodaj(); }
        });

        forma.addSacuvajListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { sacuvaj(); }
        });

        forma.addObrisiListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { obrisi(); }
        });

        forma.addOcistiListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { ocisti(); }
        });

        forma.addPretraziListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { pretrazi(); }
        });

        forma.addDodajSlikuListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { dodajSliku(); }
        });

        forma.addPokreniOcrListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { pokreniOcr(obrazacUPregledu); }
        });

        forma.addNazadListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                forma.prikaziKarticu(SV20ObrazacForma.KARTICA_LISTA);
                ucitajPodatke();
            }
        });

        forma.addSacuvajStavkeListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { sacuvajStavke(); }
        });

        forma.addPrethodnaStranaPregledListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { okreniStranuPregleda(stranaPregled - 1); }
        });
        forma.addSledecaStranaPregledListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { okreniStranuPregleda(stranaPregled + 1); }
        });

        forma.addTabelaObrasciSelectionListener(new ListSelectionListener() {
            @Override public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) { popuniFormu(); }
            }
        });

        forma.getTblObrasci().addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { otvoriOcrPregled(forma.getSelektovaniObrazac()); }
            }
        });
    }

    private void ucitajStudente() {
        try {
            List<Student> lista = Komunikacija.getInstanca().vratiSveStudente();
            forma.getCmbStudent().removeAllItems();
            for (Student s : lista) { forma.getCmbStudent().addItem(s); }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(forma, "Greska pri ucitavanju studenata: " + ex.getMessage(),
                    "Greska", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Zaposleni se više NE bira ručno na ovom ekranu (uvek je ulogovani korisnik) —
     *  lista se učitava samo da bi se u tabeli prikazalo ime po ID-ju. */
    private void ucitajZaposlene() {
        try {
            sviZaposleni = Komunikacija.getInstanca().vratiSveZaposlene();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(forma, "Greska pri ucitavanju zaposlenih: " + ex.getMessage(),
                    "Greska", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ucitajStatuse() {
        forma.getCmbStatus().removeAllItems();
        for (Status s : Status.values()) { forma.getCmbStatus().addItem(s); }
        forma.getCmbStatus().setSelectedItem(Status.PODNET);
    }

    private void ucitajPodatke() {
        try {
            List<SV20Obrazac> lista = Komunikacija.getInstanca().vratiListuSV20Obrazaca("");
            sortirajNajnovijePrvo(lista);
            ucitaniObrasci = lista;
            DefaultTableModel model = forma.getTableModelObrasci();
            model.setRowCount(0);
            for (SV20Obrazac o : lista) {
                model.addRow(buildRow(o));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(forma, "Greska pri ucitavanju: " + ex.getMessage(),
                    "Greska", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Najnoviji obrazac (najveći ID) na vrhu liste. */
    private void sortirajNajnovijePrvo(List<SV20Obrazac> lista) {
        lista.sort(Comparator.comparingInt(SV20Obrazac::getIdObrazac).reversed());
    }

    private Object[] buildRow(SV20Obrazac o) {
        return new Object[]{
            o.getIdObrazac(),
            o.getDatumUnosa() != null ? sdf.format(o.getDatumUnosa()) : "",
            o.getSkolskaGodina(),
            o.getSemestar(),
            o.getStatus(),
            o.getIndeks() != null ? o.getIndeks().getIndeks() : "",
            imeZaposlenog(o.getIdZaposlenog()),
            o.isOcrIzvrseno() ? "Da" : "Ne",
            o.getPutanjaDoFajla() != null ? o.getPutanjaDoFajla() : ""
        };
    }

    /**
     * Generički broker učita SV20Obrazac preko "SELECT * FROM sv20obrazac" (bez JOIN-a),
     * pa vratiObjekatIzRS zna samo ID zaposlenog, ne i ime/prezime — otud su ta polja null
     * i tabela je pre ove popravke prikazivala "null null". Ime tražimo u listi svih
     * zaposlenih učitanoj pri pokretanju ekrana, umesto što se oslanjamo na nepotpun
     * objekat iz baze.
     */
    private String imeZaposlenog(ZaposleniFakulteta stub) {
        if (stub == null) return "";
        for (ZaposleniFakulteta z : sviZaposleni) {
            if (z.getIdZaposlenog() == stub.getIdZaposlenog()) {
                return z.getIme() + " " + z.getPrezime();
            }
        }
        return "";
    }

    private void dodaj() {
        try {
            Student student = (Student) forma.getCmbStudent().getSelectedItem();
            ZaposleniFakulteta ulogovani = Cordinator.getInstanca().getUlogovaniKorisnik();
            int skolskaGodina = (int) forma.getSpnSkolskaGodina().getValue();
            int semestar = (int) forma.getSpnSemestar().getValue();
            Status status = (Status) forma.getCmbStatus().getSelectedItem();

            if (!validirajPodatke(student, ulogovani)) return;

            SV20Obrazac o = new SV20Obrazac();
            o.setDatumUnosa(new Date());
            o.setSkolskaGodina(skolskaGodina);
            o.setSemestar(semestar);
            o.setStatus(status);
            o.setPutanjaDoFajla(null);
            o.setOcrIzvrseno(false);
            o.setBrojUspesnihStavki(0);
            o.setBrojNeuspesnihStavki(0);
            o.setIndeks(student);
            o.setIdZaposlenog(ulogovani);

            Komunikacija.getInstanca().kreirajSV20Obrazac(o);

            // Osveži listu (najnoviji je na vrhu) i odmah ga selektuj — korisnik može
            // dvoklikom odmah da otvori obrazac i priloži sken, bez dodatnog traženja u tabeli.
            ucitajPodatke();
            if (forma.getTableModelObrasci().getRowCount() > 0) {
                forma.getTblObrasci().setRowSelectionInterval(0, 0);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(forma, "Sistem ne moze da zapamti SV-20 obrazac: " + ex.getMessage(),
                    "Greska", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void sacuvaj() {
        try {
            if (forma.getSelektovaniObrazac() == null) {
                JOptionPane.showMessageDialog(forma, "Izaberite obrazac iz tabele!", "Upozorenje", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Student student = (Student) forma.getCmbStudent().getSelectedItem();
            int skolskaGodina = (int) forma.getSpnSkolskaGodina().getValue();
            int semestar = (int) forma.getSpnSemestar().getValue();
            Status status = (Status) forma.getCmbStatus().getSelectedItem();

            if (student == null) {
                JOptionPane.showMessageDialog(forma, "Student je obavezan!", "Upozorenje", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Zaposleni se NE menja pri izmeni osnovnih podataka — ostaje onaj ko je
            // obrazac originalno kreirao, izmena ovde ne "preuzima vlasništvo".
            SV20Obrazac o = forma.getSelektovaniObrazac();
            o.setSkolskaGodina(skolskaGodina);
            o.setSemestar(semestar);
            o.setStatus(status);
            o.setIndeks(student);

            Komunikacija.getInstanca().promeniSV20Obrazac(o);

            JOptionPane.showMessageDialog(forma, "Sistem je zapamtio SV-20 obrazac.", "Uspeh", JOptionPane.INFORMATION_MESSAGE);
            ucitajPodatke();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(forma, "Sistem ne moze da zapamti SV-20 obrazac: " + ex.getMessage(),
                    "Greska", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void obrisi() {
        try {
            if (forma.getSelektovaniObrazac() == null) {
                JOptionPane.showMessageDialog(forma, "Izaberite obrazac iz tabele!", "Upozorenje", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int opcija = JOptionPane.showConfirmDialog(forma,
                    "Da li ste sigurni da zelite da obrisete ovaj obrazac?",
                    "Potvrda brisanja", JOptionPane.YES_NO_OPTION);
            if (opcija == JOptionPane.YES_OPTION) {
                Komunikacija.getInstanca().obrisiSV20Obrazac(forma.getSelektovaniObrazac());
                JOptionPane.showMessageDialog(forma, "Sistem je obrisao SV-20 obrazac.", "Uspeh", JOptionPane.INFORMATION_MESSAGE);
                ocisti();
                ucitajPodatke();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(forma, "Sistem ne moze da obrise SV-20 obrazac: " + ex.getMessage(),
                    "Greska", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void pretrazi() {
        try {
            String tekst = forma.getTxtPretraga().getText().trim();
            int kriterijumIndex = forma.getCmbKriterijum().getSelectedIndex();

            String kriterijum = "";
            if (!tekst.isEmpty()) {
                if (kriterijumIndex == 0) {
                    kriterijum = " WHERE indeks LIKE '%" + tekst + "%'";
                } else if (kriterijumIndex == 1) {
                    kriterijum = " JOIN zaposlenifakulteta z ON sv20obrazac.idZaposlenog = z.idZaposlenog WHERE z.ime LIKE '%" + tekst + "%' OR z.prezime LIKE '%" + tekst + "%'";
                } else {
                    kriterijum = " WHERE status LIKE '%" + tekst + "%'";
                }
            }

            List<SV20Obrazac> lista = Komunikacija.getInstanca().vratiListuSV20Obrazaca(kriterijum);
            sortirajNajnovijePrvo(lista);
            ucitaniObrasci = lista;
            DefaultTableModel model = forma.getTableModelObrasci();
            model.setRowCount(0);
            for (SV20Obrazac o : lista) { model.addRow(buildRow(o)); }

            if (lista.isEmpty()) {
                JOptionPane.showMessageDialog(forma, "Sistem ne moze da nadje SV-20 obrasce po zadatim kriterijumima.",
                        "Informacija", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(forma, "Greska pri pretrazi: " + ex.getMessage(), "Greska", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Baza gradi upite spajanjem stringova bez escape-ovanja, a MySQL tretira
     * '\' kao escape karakter u string literalima — Windows putanje sa '\'
     * se zato osakate pri upisu (npr. "C:\Users\..." postane "C:Users...").
     * Kosa crta '/' radi identično kao '\' i na Windows-u i bezbedna je za SQL.
     */
    private static String normalizujPutanju(String putanja) {
        return putanja == null ? null : putanja.replace('\\', '/');
    }

    // ── OCR pregled — otvaranje ekrana za jedan obrazac ──────────────────────

    /**
     * Otvara ekran za dati obrazac. RADI I KAD OBRAZAC NEMA SKEN — u tom slučaju levi
     * panel pokazuje "dodaj sliku" stanje, desni panel je prazan dok se OCR ne pokrene.
     * Ako obrazac već ima sken, slika i sačuvane stavke (ako postoje) se učitaju.
     */
    private void otvoriOcrPregled(SV20Obrazac obrazac) {
        if (obrazac == null) {
            JOptionPane.showMessageDialog(forma, "Izaberite obrazac iz tabele!", "Upozorenje", JOptionPane.WARNING_MESSAGE);
            return;
        }
        obrazacUPregledu = obrazac;
        String putanja = normalizujPutanju(obrazac.getPutanjaDoFajla());
        String zaglavlje = zaglavljeObrasca(obrazac);

        if (putanja == null || putanja.isEmpty()) {
            stavkeUPregledu = new ArrayList<>();
            stranaPregled = 1;
            maxStranaPregled = 1;
            forma.prikaziStanjeSlike(false);
            forma.prikaziStavke(stavkeUPregledu, 1);
            forma.getLblSazetakPregled().setText(zaglavlje + " — nema priložene slike.");
            forma.prikaziKarticu(SV20ObrazacForma.KARTICA_PREGLED);
            return;
        }

        forma.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));

        SwingWorker<Object[], Void> worker = new SwingWorker<Object[], Void>() {
            @Override protected Object[] doInBackground() throws Exception {
                ocr.OcrPrikaz prikaz = ocr.OcrKlijent.preuzmiPrikaz(putanja);

                StavkeObrasca kriterijum = new StavkeObrasca();
                SV20Obrazac o = new SV20Obrazac();
                o.setIdObrazac(obrazac.getIdObrazac());
                kriterijum.setIdObrazac(o);
                List<StavkeObrasca> stavke = Komunikacija.getInstanca().pretraziStavkeObrasca(kriterijum);

                // Generički broker (bez JOIN-a) vraća idPolja kao "patrljak" (samo ID, bez
                // naziva/strane/redosleda) — bez ovoga polja u pregledu nemaju labelu
                // i sva ispadnu pod "Strana 0".
                obogatiTipPoljaPodacima(stavke, Komunikacija.getInstanca().vratiSveTipovePolja());

                stavke.sort(Comparator.comparingInt(
                        (StavkeObrasca s) -> s.getIdPolja() != null ? s.getIdPolja().getStranica() : 1)
                        .thenComparingInt(s -> s.getIdPolja() != null ? s.getIdPolja().getRedosledObrade() : 0));

                return new Object[]{prikaz, stavke};
            }

            @Override protected void done() {
                forma.setCursor(java.awt.Cursor.getDefaultCursor());
                try {
                    Object[] rezultat = get();
                    ocr.OcrPrikaz prikaz = (ocr.OcrPrikaz) rezultat[0];
                    @SuppressWarnings("unchecked")
                    List<StavkeObrasca> stavke = (List<StavkeObrasca>) rezultat[1];

                    stavkeUPregledu = stavke;
                    stranaPregled = 1;
                    maxStranaPregled = prikaz.getBrojStrana();

                    forma.prikaziStanjeSlike(true);
                    forma.postaviTekstDugmetaOcr(obrazac.isOcrIzvrseno());
                    forma.prikaziSliku(prikaz.getSlika(), forma.getLblSlikaPregled());
                    forma.getLblStranaPregled().setText("Strana 1 od " + maxStranaPregled);
                    forma.getBtnPrethodnaStranaPregled().setEnabled(false);
                    forma.getBtnSledecaStranaPregled().setEnabled(maxStranaPregled > 1);

                    forma.prikaziStavke(stavkeZaStranu(1), 1);
                    forma.getLblSazetakPregled().setText(zaglavlje + (stavke.isEmpty() ? "" : " — " + sazetakStavki(stavke)));
                    forma.prikaziKarticu(SV20ObrazacForma.KARTICA_PREGLED);

                } catch (Exception ex) {
                    Throwable uzrok = ex.getCause() != null ? ex.getCause() : ex;
                    JOptionPane.showMessageDialog(forma,
                            "Ne mogu da otvorim OCR pregled: " + uzrok.getMessage()
                            + "\n\nProverite da li je OCR servis pokrenut na http://localhost:9001",
                            "Greška", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private String zaglavljeObrasca(SV20Obrazac o) {
        String indeks = o.getIndeks() != null ? o.getIndeks().getIndeks() : "";
        return "Obrazac #" + o.getIdObrazac() + (indeks.isEmpty() ? "" : " — " + indeks);
    }

    /** Bira fajl i ODMAH ga trajno vezuje za obrazac (bez posebnog "Sačuvaj" koraka za sken). */
    private void dodajSliku() {
        if (obrazacUPregledu == null) return;

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Odaberite SV-20 obrazac");
        fileChooser.setFileFilter(new FileNameExtensionFilter(
                "Slike i PDF (*.jpg, *.png, *.pdf, *.tif)", "jpg", "jpeg", "png", "pdf", "tif", "tiff"));
        if (fileChooser.showOpenDialog(forma) != JFileChooser.APPROVE_OPTION) return;

        String putanja = normalizujPutanju(fileChooser.getSelectedFile().getAbsolutePath());
        obrazacUPregledu.setPutanjaDoFajla(putanja);
        try {
            Komunikacija.getInstanca().promeniSV20Obrazac(obrazacUPregledu);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(forma, "Ne mogu da sačuvam putanju fajla: " + ex.getMessage(),
                    "Greška", JOptionPane.ERROR_MESSAGE);
            return;
        }

        final String putanjaKonacna = putanja;
        SwingWorker<ocr.OcrPrikaz, Void> worker = new SwingWorker<ocr.OcrPrikaz, Void>() {
            @Override protected ocr.OcrPrikaz doInBackground() throws Exception {
                return ocr.OcrKlijent.preuzmiPrikaz(putanjaKonacna);
            }
            @Override protected void done() {
                try {
                    ocr.OcrPrikaz prikaz = get();
                    stranaPregled = 1;
                    maxStranaPregled = prikaz.getBrojStrana();
                    forma.prikaziStanjeSlike(true);
                    forma.postaviTekstDugmetaOcr(false);
                    forma.prikaziSliku(prikaz.getSlika(), forma.getLblSlikaPregled());
                    forma.getLblStranaPregled().setText("Strana 1 od " + maxStranaPregled);
                    forma.getBtnPrethodnaStranaPregled().setEnabled(false);
                    forma.getBtnSledecaStranaPregled().setEnabled(maxStranaPregled > 1);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(forma,
                            "Fajl je sačuvan, ali prikaz nije uspeo: " + ex.getMessage()
                            + "\n\nProverite da li je OCR servis pokrenut na http://localhost:9001",
                            "Greška", JOptionPane.ERROR_MESSAGE);
                    forma.prikaziStanjeSlike(true);
                    forma.postaviTekstDugmetaOcr(false);
                }
            }
        };
        worker.execute();
    }

    private void okreniStranuPregleda(int nova) {
        if (nova < 1 || nova > maxStranaPregled) return;
        sinhronizujTrenutnuStranicu();
        SwingWorker<java.awt.image.BufferedImage, Void> worker = new SwingWorker<java.awt.image.BufferedImage, Void>() {
            @Override protected java.awt.image.BufferedImage doInBackground() throws Exception {
                return ocr.OcrKlijent.preuzmiStranu(nova);
            }
            @Override protected void done() {
                try {
                    forma.prikaziSliku(get(), forma.getLblSlikaPregled());
                    stranaPregled = nova;
                    forma.getLblStranaPregled().setText("Strana " + stranaPregled + " od " + maxStranaPregled);
                    forma.getBtnPrethodnaStranaPregled().setEnabled(stranaPregled > 1);
                    forma.getBtnSledecaStranaPregled().setEnabled(stranaPregled < maxStranaPregled);
                    forma.prikaziStavke(stavkeZaStranu(stranaPregled), stranaPregled);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(forma, "Ne mogu da učitam stranu " + nova + ": " + ex.getMessage(),
                            "Greška", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private static void obogatiTipPoljaPodacima(List<StavkeObrasca> stavke, List<TipPolja> tipoviPolja) {
        Map<Integer, TipPolja> poId = new HashMap<>();
        for (TipPolja tp : tipoviPolja) { poId.put(tp.getIdPolja(), tp); }
        for (StavkeObrasca s : stavke) {
            if (s.getIdPolja() != null) {
                TipPolja pun = poId.get(s.getIdPolja().getIdPolja());
                if (pun != null) { s.setIdPolja(pun); }
            }
        }
    }

    private List<StavkeObrasca> stavkeZaStranu(int strana) {
        List<StavkeObrasca> rezultat = new ArrayList<>();
        for (StavkeObrasca s : stavkeUPregledu) {
            int st = s.getIdPolja() != null ? s.getIdPolja().getStranica() : 1;
            if (st == strana) { rezultat.add(s); }
        }
        return rezultat;
    }

    /** Upisuje trenutno vidljive (nesačuvane) korekcije iz forme nazad u stavkeUPregledu,
     *  pre nego što se strana promeni ili stavke sačuvaju u bazu — inače bi se izmene na
     *  jednoj strani izgubile čim korisnik ode na drugu. */
    private void sinhronizujTrenutnuStranicu() {
        Map<Integer, JTextField> polja = forma.getPoljaZaKorekciju();
        for (StavkeObrasca s : stavkeUPregledu) {
            JTextField txt = polja.get(s.getIdStavke());
            if (txt != null) {
                String nova = txt.getText().trim();
                s.setKorigovanaVrednost(nova.isEmpty() ? null : nova);
            }
        }
    }

    private String sazetakStavki(List<StavkeObrasca> stavke) {
        int pouzdano = 0, zaProveru = 0, nijePrepoznato = 0;
        for (StavkeObrasca s : stavke) {
            double c = s.getNivoPodudarnosti();
            if (c >= 85) pouzdano++;
            else if (c >= 60) zaProveru++;
            else nijePrepoznato++;
        }
        return stavke.size() + " polja  ·  " + pouzdano + " pouzdano prepoznato  ·  "
                + zaProveru + " za proveru  ·  " + nijePrepoznato + " nije prepoznato";
    }

    private void sacuvajStavke() {
        sinhronizujTrenutnuStranicu();
        int sacuvano = 0, greske = 0;
        for (StavkeObrasca s : stavkeUPregledu) {
            try {
                Komunikacija.getInstanca().promeniStavkuObrasca(s);
                sacuvano++;
            } catch (Exception ex) {
                greske++;
            }
        }
        if (greske == 0) {
            JOptionPane.showMessageDialog(forma, "Sačuvano " + sacuvano + " stavki.",
                    "Uspeh", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(forma,
                    "Sačuvano " + sacuvano + " stavki, " + greske + " nije uspelo.",
                    "Delimičan uspeh", JOptionPane.WARNING_MESSAGE);
        }
    }

    // ── Pokretanje OCR analize ───────────────────────────────────────────────

    private void pokreniOcr(SV20Obrazac obrazacZaObradu) {
        if (obrazacZaObradu == null) {
            JOptionPane.showMessageDialog(forma, "Izaberite obrazac iz tabele!", "Upozorenje", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String putanja = normalizujPutanju(obrazacZaObradu.getPutanjaDoFajla());
        if (putanja == null || putanja.isEmpty()) {
            JOptionPane.showMessageDialog(forma,
                    "Dodajte sliku pre pokretanja OCR analize!",
                    "Upozorenje", JOptionPane.WARNING_MESSAGE);
            return;
        }

        java.io.File fajl = new java.io.File(putanja);
        if (!fajl.exists()) {
            JOptionPane.showMessageDialog(forma,
                    "Fajl nije pronađen:\n" + putanja
                    + "\n\nDodajte sliku ponovo dugmetom \"Dodaj sliku\".",
                    "Greška", JOptionPane.ERROR_MESSAGE);
            return;
        }

        final SV20Obrazac obrazac = obrazacZaObradu;
        final String putanjaKonacna = putanja;
        obrazac.setPutanjaDoFajla(putanjaKonacna);

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            private int uspesnih = 0;
            private int neuspesnih = 0;
            private int greskaPriCuvanju = 0;

            @Override
            protected Void doInBackground() throws Exception {
                ocr.OcrKlijent.proveriServis();

                List<ocr.OcrPolje> rezultati = ocr.OcrKlijent.obradiObrazac(
                        putanjaKonacna, String.valueOf(obrazac.getIdObrazac()));

                if (rezultati.isEmpty()) {
                    throw new Exception("OCR nije pronašao nijedan rezultat u dokumentu. "
                            + "Proveri kvalitet slike i da li je fajl validan SV-20 obrazac.");
                }

                List<TipPolja> tipoviPolja = Komunikacija.getInstanca().vratiSveTipovePolja();

                // Postojeće stavke ovog obrasca — da ponovno pokretanje OCR-a AŽURIRA
                // postojeći red umesto da napravi duplikat (jedan red po idPolja).
                StavkeObrasca kriterijum = new StavkeObrasca();
                SV20Obrazac ref = new SV20Obrazac();
                ref.setIdObrazac(obrazac.getIdObrazac());
                kriterijum.setIdObrazac(ref);
                List<StavkeObrasca> postojece = Komunikacija.getInstanca().pretraziStavkeObrasca(kriterijum);
                Map<Integer, StavkeObrasca> postojecePoPolju = new HashMap<>();
                for (StavkeObrasca p : postojece) {
                    if (p.getIdPolja() != null) postojecePoPolju.put(p.getIdPolja().getIdPolja(), p);
                }

                for (ocr.OcrPolje op : rezultati) {
                    if (op.getNazivPolja() == null) continue;

                    TipPolja tip = null;
                    for (TipPolja tp : tipoviPolja) {
                        if (tp.getNazivPolja() == null) continue;
                        String tpNaziv = tp.getNazivPolja().trim().toLowerCase();
                        String opNaziv = op.getNazivPolja().trim().toLowerCase();
                        if (tpNaziv.equals(opNaziv) || tpNaziv.contains(opNaziv) || opNaziv.contains(tpNaziv)) {
                            tip = tp;
                            break;
                        }
                    }
                    if (tip == null) continue;

                    try {
                        StavkeObrasca postojeca = postojecePoPolju.get(tip.getIdPolja());
                        if (postojeca != null) {
                            // Ažuriraj OCR vrednost/pouzdanost — korigovanaVrednost (ako je korisnik
                            // već ispravio) se NE dira, da ponovni OCR ne pregazi ručnu korekciju.
                            postojeca.setOcrVrednost(op.getOcrVrednost());
                            postojeca.setNivoPodudarnosti(op.getKonfidens());
                            postojeca.setOcrUspesno(op.isUspesno());
                            Komunikacija.getInstanca().promeniStavkuObrasca(postojeca);
                        } else {
                            StavkeObrasca stavka = new StavkeObrasca();
                            stavka.setIdObrazac(obrazac);
                            stavka.setIdPolja(tip);
                            stavka.setOcrVrednost(op.getOcrVrednost());
                            stavka.setNivoPodudarnosti(op.getKonfidens());
                            stavka.setOcrUspesno(op.isUspesno());
                            Komunikacija.getInstanca().kreirajStavkuObrasca(stavka);
                        }
                        if (op.isUspesno()) uspesnih++; else neuspesnih++;
                    } catch (Exception stavkaEx) {
                        greskaPriCuvanju++;
                    }
                }

                obrazac.setOcrIzvrseno(true);
                obrazac.setBrojUspesnihStavki(uspesnih);
                obrazac.setBrojNeuspesnihStavki(neuspesnih);
                Komunikacija.getInstanca().promeniSV20Obrazac(obrazac);
                return null;
            }

            @Override
            protected void done() {
                forma.setCursor(java.awt.Cursor.getDefaultCursor());
                try {
                    get();
                    ucitajPodatke();
                    otvoriOcrPregled(obrazac);
                    if (greskaPriCuvanju > 0) {
                        JOptionPane.showMessageDialog(forma,
                                "OCR gotov, ali " + greskaPriCuvanju + " stavki nije sačuvano u bazi.",
                                "Delimičan uspeh", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (Exception ex) {
                    Throwable uzrok = ex.getCause() != null ? ex.getCause() : ex;
                    JOptionPane.showMessageDialog(forma,
                            "Greška pri OCR analizi:\n" + uzrok.getMessage()
                            + "\n\nProverite da li je OCR servis pokrenut na http://localhost:9001",
                            "OCR greška", JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        forma.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));
        worker.execute();
    }

    private void ocisti() {
        if (forma.getCmbStudent().getItemCount() > 0) forma.getCmbStudent().setSelectedIndex(0);
        forma.getSpnSkolskaGodina().setValue(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR));
        forma.getSpnSemestar().setValue(1);
        forma.getCmbStatus().setSelectedIndex(0);
        forma.setSelektovaniObrazac(null);
        forma.getTblObrasci().clearSelection();
        forma.prikaziRezimUnosa(true);
    }

    private void popuniFormu() {
        int red = forma.getTblObrasci().getSelectedRow();
        if (red >= 0) {
            int id = (int) forma.getTableModelObrasci().getValueAt(red, 0);

            // Uzmi PUN objekat sa servera (ucitaniObrasci), ne rekonstruisi ga iz celija tabele -
            // tabela ne prikazuje sva polja (npr. datumUnosa), pa je rucna rekonstrukcija ranije
            // pravila obrazac sa datumUnosa=null, sto je rusilo izmenu/OCR upis (NullPointerException
            // u SV20Obrazac.vratiVrednostiZaIzmenu() -> SimpleDateFormat.format(null)).
            SV20Obrazac o = pronadjiObrazacPoId(id);
            if (o == null) return;

            forma.getSpnSkolskaGodina().setValue(o.getSkolskaGodina());
            forma.getSpnSemestar().setValue(o.getSemestar());
            forma.getCmbStatus().setSelectedItem(o.getStatus());

            String indeksStudenta = o.getIndeks() != null ? o.getIndeks().getIndeks() : null;
            for (int i = 0; i < forma.getCmbStudent().getItemCount(); i++) {
                Student s = forma.getCmbStudent().getItemAt(i);
                if (s.getIndeks() != null && s.getIndeks().equals(indeksStudenta)) {
                    forma.getCmbStudent().setSelectedIndex(i);
                    o.setIndeks(s); // puni objekat iz combo-a (server vraca samo indeks bez imena)
                    break;
                }
            }

            for (ZaposleniFakulteta z : sviZaposleni) {
                if (o.getIdZaposlenog() != null && z.getIdZaposlenog() == o.getIdZaposlenog().getIdZaposlenog()) {
                    o.setIdZaposlenog(z); // puni objekat (server vraca samo ID)
                    break;
                }
            }

            forma.setSelektovaniObrazac(o);
            forma.prikaziRezimUnosa(false);
        }
    }

    private SV20Obrazac pronadjiObrazacPoId(int id) {
        for (SV20Obrazac o : ucitaniObrasci) {
            if (o.getIdObrazac() == id) return o;
        }
        return null;
    }

    private boolean validirajPodatke(Student student, ZaposleniFakulteta zaposleni) {
        if (student == null) {
            JOptionPane.showMessageDialog(forma, "Student je obavezan!", "Upozorenje", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (zaposleni == null) {
            JOptionPane.showMessageDialog(forma, "Greška: nema ulogovanog zaposlenog!", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
