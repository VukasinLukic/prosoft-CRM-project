package kontroleri;

import domen.SV20Obrazac;
import domen.Status;
import domen.StavkeObrasca;
import domen.Student;
import domen.TipPolja;
import domen.ZaposleniFakulteta;
import forme.SV20ObrazacForma;
import forme.StavkeObrascaForma;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import komunikacija.Komunikacija;

public class SV20ObrazacController {

    private final SV20ObrazacForma forma;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    public SV20ObrazacController(SV20ObrazacForma forma) {
        this.forma = forma;
        addActionListeners();
        ucitajStudente();
        ucitajZaposlene();
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

        forma.addOdaberiFajlListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { odaberiFajl(); }
        });

        forma.addPokreniOcrListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { pokreniOcr(); }
        });

        forma.addTabelaObrasciSelectionListener(new ListSelectionListener() {
            @Override public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) { popuniFormu(); }
            }
        });

        forma.getTblObrasci().addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { urediStavke(); }
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

    private void ucitajZaposlene() {
        try {
            List<ZaposleniFakulteta> lista = Komunikacija.getInstanca().vratiSveZaposlene();
            forma.getCmbZaposleni().removeAllItems();
            for (ZaposleniFakulteta z : lista) { forma.getCmbZaposleni().addItem(z); }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(forma, "Greska pri ucitavanju zaposlenih: " + ex.getMessage(),
                    "Greska", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ucitajPodatke() {
        try {
            List<SV20Obrazac> lista = Komunikacija.getInstanca().vratiListuSV20Obrazaca("");
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

    private Object[] buildRow(SV20Obrazac o) {
        return new Object[]{
            o.getIdObrazac(),
            o.getDatumUnosa() != null ? sdf.format(o.getDatumUnosa()) : "",
            o.getSkolskaGodina(),
            o.getSemestar(),
            o.getStatus(),
            o.getIndeks() != null ? o.getIndeks().getIndeks() : "",
            o.getIdZaposlenog() != null ? o.getIdZaposlenog().getIme() + " " + o.getIdZaposlenog().getPrezime() : "",
            o.isOcrIzvrseno() ? "Da" : "Ne",
            o.getPutanjaDoFajla() != null ? o.getPutanjaDoFajla() : ""
        };
    }

    private void ucitajStavke(int idObrazac) {
        try {
            StavkeObrasca kriterijum = new StavkeObrasca();
            SV20Obrazac o = new SV20Obrazac();
            o.setIdObrazac(idObrazac);
            kriterijum.setIdObrazac(o);

            List<StavkeObrasca> lista = Komunikacija.getInstanca().pretraziStavkeObrasca(kriterijum);
            DefaultTableModel model = forma.getTableModelStavke();
            model.setRowCount(0);

            for (StavkeObrasca s : lista) {
                model.addRow(new Object[]{
                    s.getIdStavke(),
                    s.getIdPolja() != null ? s.getIdPolja().getNazivPolja() : "",
                    s.getOcrVrednost(),
                    s.getKorigovanaVrednost(),
                    String.format("%.2f", s.getNivoPodudarnosti()),
                    s.isOcrUspesno() ? "Da" : "Ne"
                });
            }
        } catch (Exception ex) {
            forma.getTableModelStavke().setRowCount(0);
        }
    }

    private void dodaj() {
        try {
            Student student = (Student) forma.getCmbStudent().getSelectedItem();
            ZaposleniFakulteta zaposleni = (ZaposleniFakulteta) forma.getCmbZaposleni().getSelectedItem();
            int skolskaGodina = (int) forma.getSpnSkolskaGodina().getValue();
            int semestar = (int) forma.getSpnSemestar().getValue();
            Status status = (Status) forma.getCmbStatus().getSelectedItem();
            String putanjaFajla = forma.getTxtPutanjaFajla().getText().trim();

            if (!validirajPodatke(student, zaposleni, putanjaFajla)) return;

            SV20Obrazac o = new SV20Obrazac();
            o.setDatumUnosa(new Date());
            o.setSkolskaGodina(skolskaGodina);
            o.setSemestar(semestar);
            o.setStatus(status);
            o.setPutanjaDoFajla(putanjaFajla);
            o.setOcrIzvrseno(false);
            o.setBrojUspesnihStavki(0);
            o.setBrojNeuspesnihStavki(0);
            o.setIndeks(student);
            o.setIdZaposlenog(zaposleni);

            Komunikacija.getInstanca().kreirajSV20Obrazac(o);

            JOptionPane.showMessageDialog(forma, "Sistem je zapamtio SV-20 obrazac.",
                    "Uspeh", JOptionPane.INFORMATION_MESSAGE);
            ocisti();
            ucitajPodatke();

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
            ZaposleniFakulteta zaposleni = (ZaposleniFakulteta) forma.getCmbZaposleni().getSelectedItem();
            int skolskaGodina = (int) forma.getSpnSkolskaGodina().getValue();
            int semestar = (int) forma.getSpnSemestar().getValue();
            Status status = (Status) forma.getCmbStatus().getSelectedItem();
            String putanjaFajla = forma.getTxtPutanjaFajla().getText().trim();

            if (!validirajPodatke(student, zaposleni, putanjaFajla)) return;

            SV20Obrazac o = forma.getSelektovaniObrazac();
            o.setSkolskaGodina(skolskaGodina);
            o.setSemestar(semestar);
            o.setStatus(status);
            o.setPutanjaDoFajla(putanjaFajla);
            o.setIndeks(student);
            o.setIdZaposlenog(zaposleni);

            Komunikacija.getInstanca().promeniSV20Obrazac(o);

            JOptionPane.showMessageDialog(forma, "Sistem je zapamtio SV-20 obrazac.", "Uspeh", JOptionPane.INFORMATION_MESSAGE);
            ocisti();
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
            DefaultTableModel model = forma.getTableModelObrasci();
            model.setRowCount(0);
            for (SV20Obrazac o : lista) { model.addRow(buildRow(o)); }

            if (lista.isEmpty()) {
                JOptionPane.showMessageDialog(forma, "Sistem ne moze da nadje SV-20 obrasce po zadatim kriterijumima.",
                        "Informacija", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(forma, "Sistem je nasao " + lista.size() + " obrazac(a).",
                        "Uspeh", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(forma, "Greska pri pretrazi: " + ex.getMessage(), "Greska", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void odaberiFajl() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Odaberite SV-20 obrazac");
        fileChooser.setFileFilter(new FileNameExtensionFilter(
                "Slike i PDF (*.jpg, *.png, *.pdf, *.tif)", "jpg", "jpeg", "png", "pdf", "tif", "tiff"));
        if (fileChooser.showOpenDialog(forma) == JFileChooser.APPROVE_OPTION) {
            forma.getTxtPutanjaFajla().setText(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void urediStavke() {
        if (forma.getSelektovaniObrazac() == null) {
            JOptionPane.showMessageDialog(forma, "Izaberite obrazac iz tabele!", "Upozorenje", JOptionPane.WARNING_MESSAGE);
            return;
        }
        StavkeObrascaForma stavkeForma = new StavkeObrascaForma((java.awt.Frame) forma.getParent());
        new StavkeObrascaController(stavkeForma, forma.getSelektovaniObrazac());
        stavkeForma.setVisible(true);
        ucitajStavke(forma.getSelektovaniObrazac().getIdObrazac());
    }

    private void pokreniOcr() {
        if (forma.getSelektovaniObrazac() == null) {
            JOptionPane.showMessageDialog(forma, "Izaberite obrazac iz tabele!", "Upozorenje", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String putanja = forma.getTxtPutanjaFajla().getText().trim();
        if (putanja.isEmpty()) {
            JOptionPane.showMessageDialog(forma,
                    "Unesite ili odaberite putanju do fajla pre pokretanja OCR analize!",
                    "Upozorenje", JOptionPane.WARNING_MESSAGE);
            return;
        }

        java.io.File fajl = new java.io.File(putanja);
        if (!fajl.exists()) {
            JOptionPane.showMessageDialog(forma,
                    "Fajl nije pronađen:\n" + putanja,
                    "Greška", JOptionPane.ERROR_MESSAGE);
            return;
        }

        final SV20Obrazac obrazac = forma.getSelektovaniObrazac();
        obrazac.setPutanjaDoFajla(putanja);

        // Run OCR in background so UI stays responsive
        SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
            private int uspesnih = 0;
            private int neuspesnih = 0;

            @Override
            protected String doInBackground() throws Exception {
                // Health check first
                ocr.OcrKlijent.proveriServis();

                List<ocr.OcrPolje> rezultati = ocr.OcrKlijent.obradiObrazac(
                        putanja, String.valueOf(obrazac.getIdObrazac()));

                if (rezultati.isEmpty()) {
                    return "OCR nije pronašao nijedan rezultat u dokumentu.\n" +
                           "Provjeri kvalitet slike i da li je fajl validan SV-20 obrazac.";
                }

                List<TipPolja> tipoviPolja = Komunikacija.getInstanca().vratiSveTipovePolja();
                int preskocenih = 0;

                for (ocr.OcrPolje op : rezultati) {
                    if (op.getNazivPolja() == null) continue;

                    // Pokušaj poklapanje po nazivu polja (case-insensitive, sa i bez prefiksa)
                    TipPolja tip = null;
                    for (TipPolja tp : tipoviPolja) {
                        if (tp.getNazivPolja() == null) continue;
                        String tpNaziv = tp.getNazivPolja().trim().toLowerCase();
                        String opNaziv = op.getNazivPolja().trim().toLowerCase();
                        if (tpNaziv.equals(opNaziv)
                                || tpNaziv.contains(opNaziv)
                                || opNaziv.contains(tpNaziv)) {
                            tip = tp;
                            break;
                        }
                    }

                    if (tip == null) {
                        preskocenih++;
                        continue;
                    }

                    StavkeObrasca stavka = new StavkeObrasca();
                    stavka.setIdObrazac(obrazac);
                    stavka.setIdPolja(tip);
                    stavka.setOcrVrednost(op.getOcrVrednost());
                    stavka.setNivoPodudarnosti(op.getKonfidens());
                    stavka.setOcrUspesno(op.isUspesno());

                    Komunikacija.getInstanca().kreirajStavkuObrasca(stavka);

                    if (op.isUspesno()) uspesnih++; else neuspesnih++;
                }

                obrazac.setOcrIzvrseno(true);
                obrazac.setBrojUspesnihStavki(uspesnih);
                obrazac.setBrojNeuspesnihStavki(neuspesnih);
                Komunikacija.getInstanca().promeniSV20Obrazac(obrazac);

                if (preskocenih > 0 && uspesnih == 0) {
                    return "OCR je završen ali nijedno polje nije upareno sa TipPolja u bazi.\n\n" +
                           "OCR je pronašao " + rezultati.size() + " polja, ali nazivi ne odgovaraju\n" +
                           "zapisima u TipPolja tabeli.\n\n" +
                           "Dodaj TipPolja zapise čiji nazivi odgovaraju poljima SV-20 obrasca\n" +
                           "(npr: ime_prezime_studenta, jmbg, broj_indeksa, ...)";
                }
                return null;
            }

            @Override
            protected void done() {
                forma.setCursor(java.awt.Cursor.getDefaultCursor());
                try {
                    String poruka = get();
                    if (poruka != null) {
                        JOptionPane.showMessageDialog(forma, poruka, "OCR info", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(forma,
                                "OCR analiza je završena!\n\nUspešno obrađenih polja: " + uspesnih
                                + "\nNeuspešnih polja: " + neuspesnih,
                                "OCR uspeh", JOptionPane.INFORMATION_MESSAGE);
                    }
                    ucitajPodatke();
                    ucitajStavke(obrazac.getIdObrazac());
                } catch (Exception ex) {
                    Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
                    JOptionPane.showMessageDialog(forma,
                            "Greška pri OCR analizi:\n" + cause.getMessage()
                            + "\n\nProvjerite da li je OCR servis pokrenut na http://localhost:9001",
                            "OCR greška", JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        forma.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));
        worker.execute();
    }

    private void ocisti() {
        if (forma.getCmbStudent().getItemCount() > 0) forma.getCmbStudent().setSelectedIndex(0);
        if (forma.getCmbZaposleni().getItemCount() > 0) forma.getCmbZaposleni().setSelectedIndex(0);
        forma.getSpnSkolskaGodina().setValue(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR));
        forma.getSpnSemestar().setValue(1);
        forma.getCmbStatus().setSelectedIndex(0);
        forma.getTxtPutanjaFajla().setText("");
        forma.setSelektovaniObrazac(null);
        forma.getTblObrasci().clearSelection();
        forma.getTableModelStavke().setRowCount(0);
    }

    private void popuniFormu() {
        int red = forma.getTblObrasci().getSelectedRow();
        if (red >= 0) {
            int id = (int) forma.getTableModelObrasci().getValueAt(red, 0);
            int skolskaGodina = (int) forma.getTableModelObrasci().getValueAt(red, 2);
            int semestar = (int) forma.getTableModelObrasci().getValueAt(red, 3);
            Status status = (Status) forma.getTableModelObrasci().getValueAt(red, 4);
            String indeksStudenta = (String) forma.getTableModelObrasci().getValueAt(red, 5);
            String putanja = (String) forma.getTableModelObrasci().getValueAt(red, 8);

            forma.getSpnSkolskaGodina().setValue(skolskaGodina);
            forma.getSpnSemestar().setValue(semestar);
            forma.getCmbStatus().setSelectedItem(status);
            if (putanja != null) { forma.getTxtPutanjaFajla().setText(putanja); }

            for (int i = 0; i < forma.getCmbStudent().getItemCount(); i++) {
                Student s = forma.getCmbStudent().getItemAt(i);
                if (s.getIndeks() != null && s.getIndeks().equals(indeksStudenta)) {
                    forma.getCmbStudent().setSelectedIndex(i);
                    break;
                }
            }

            SV20Obrazac o = new SV20Obrazac();
            o.setIdObrazac(id);
            o.setSkolskaGodina(skolskaGodina);
            o.setSemestar(semestar);
            o.setStatus(status);
            o.setPutanjaDoFajla(putanja);
            Student s = (Student) forma.getCmbStudent().getSelectedItem();
            o.setIndeks(s);
            ZaposleniFakulteta z = (ZaposleniFakulteta) forma.getCmbZaposleni().getSelectedItem();
            o.setIdZaposlenog(z);
            forma.setSelektovaniObrazac(o);

            ucitajStavke(id);
        }
    }

    private boolean validirajPodatke(Student student, ZaposleniFakulteta zaposleni, String putanjaFajla) {
        if (student == null) {
            JOptionPane.showMessageDialog(forma, "Student je obavezan!", "Upozorenje", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (zaposleni == null) {
            JOptionPane.showMessageDialog(forma, "Zaposleni je obavezan!", "Upozorenje", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (putanjaFajla.isEmpty()) {
            JOptionPane.showMessageDialog(forma, "Putanja do fajla je obavezna!", "Upozorenje", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
}
