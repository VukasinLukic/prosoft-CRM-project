/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kontroleri;

import domen.Student;
import domen.StudijskiProgram;
import forme.StudentForma;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import komunikacija.Komunikacija;

/**
 *
 * @author Vukasin Lukic
 */
public class StudentController {

    private final StudentForma forma;

    public StudentController(StudentForma forma) {
        this.forma = forma;
        addActionListeners();
        ucitajStudijskePrograme();
        ucitajPodatke();
    }

    private void addActionListeners() {

        forma.addDodajListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dodaj();
            }
        });

        forma.addSacuvajListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sacuvaj();
            }
        });

        forma.addObrisiListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                obrisi();
            }
        });

        forma.addOcistiListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ocisti();
            }
        });

        forma.addPretraziListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pretrazi();
            }
        });

        forma.addTabelaSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    popuniFormu();
                }
            }
        });
    }

    private void ucitajStudijskePrograme() {
        try {
            List<StudijskiProgram> lista = Komunikacija.getInstanca().vratiSveStudijskePrograme();
            forma.getCmbStudijskiProgram().removeAllItems();
            for (StudijskiProgram sp : lista) {
                forma.getCmbStudijskiProgram().addItem(sp);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(forma, "Greska pri ucitavanju studijskih programa: " + ex.getMessage(),
                    "Greska", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ucitajPodatke() {
        try {
            List<Student> lista = Komunikacija.getInstanca().vratiSveStudente();
            DefaultTableModel model = forma.getTableModel();
            model.setRowCount(0);

            for (Student s : lista) {
                model.addRow(new Object[]{
                    s.getIndeks(),
                    s.getJmbg(),
                    s.getIme(),
                    s.getPrezime(),
                    s.getMestoRodjenja(),
                    s.getAdresaStanovanja(),
                    s.getStudijskiProgram() != null ? s.getStudijskiProgram().getNaziv() : ""
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(forma, "Greska pri ucitavanju: " + ex.getMessage(),
                    "Greska", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void dodaj() {
        try {
            String indeks = forma.getTxtIndeks().getText().trim();
            String jmbg = forma.getTxtJmbg().getText().trim();
            String ime = forma.getTxtIme().getText().trim();
            String prezime = forma.getTxtPrezime().getText().trim();
            String mestoRodjenja = forma.getTxtMestoRodjenja().getText().trim();
            String adresaStanovanja = forma.getTxtAdresaStanovanja().getText().trim();
            StudijskiProgram sp = (StudijskiProgram) forma.getCmbStudijskiProgram().getSelectedItem();

            if (!validirajPodatke(indeks, jmbg, ime, prezime, sp)) {
                return;
            }

            Student s = new Student();
            s.setIndeks(indeks);
            s.setJmbg(jmbg);
            s.setIme(ime);
            s.setPrezime(prezime);
            s.setMestoRodjenja(mestoRodjenja);
            s.setAdresaStanovanja(adresaStanovanja);
            s.setStudijskiProgram(sp);

            Komunikacija.getInstanca().kreirajStudenta(s);

            JOptionPane.showMessageDialog(forma, "Sistem je zapamtio studenta.",
                    "Uspeh", JOptionPane.INFORMATION_MESSAGE);

            ocisti();
            ucitajPodatke();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(forma, "Sistem ne moze da zapamti studenta: " + ex.getMessage(),
                    "Greska", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void sacuvaj() {
        try {
            if (forma.getSelektovani() == null) {
                JOptionPane.showMessageDialog(forma, "Izaberite studenta iz tabele!",
                        "Upozorenje", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String jmbg = forma.getTxtJmbg().getText().trim();
            String ime = forma.getTxtIme().getText().trim();
            String prezime = forma.getTxtPrezime().getText().trim();
            String mestoRodjenja = forma.getTxtMestoRodjenja().getText().trim();
            String adresaStanovanja = forma.getTxtAdresaStanovanja().getText().trim();
            StudijskiProgram sp = (StudijskiProgram) forma.getCmbStudijskiProgram().getSelectedItem();

            if (!validirajPodatke(forma.getSelektovani().getIndeks(), jmbg, ime, prezime, sp)) {
                return;
            }

            Student s = forma.getSelektovani();
            s.setJmbg(jmbg);
            s.setIme(ime);
            s.setPrezime(prezime);
            s.setMestoRodjenja(mestoRodjenja);
            s.setAdresaStanovanja(adresaStanovanja);
            s.setStudijskiProgram(sp);

            Komunikacija.getInstanca().promeniStudenta(s);

            JOptionPane.showMessageDialog(forma, "Sistem je zapamtio studenta.",
                    "Uspeh", JOptionPane.INFORMATION_MESSAGE);

            ocisti();
            ucitajPodatke();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(forma, "Sistem ne moze da zapamti studenta: " + ex.getMessage(),
                    "Greska", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void obrisi() {
        try {
            if (forma.getSelektovani() == null) {
                JOptionPane.showMessageDialog(forma, "Izaberite studenta iz tabele!",
                        "Upozorenje", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int opcija = JOptionPane.showConfirmDialog(forma,
                    "Da li ste sigurni da zelite da obrisete ovog studenta?",
                    "Potvrda brisanja", JOptionPane.YES_NO_OPTION);

            if (opcija == JOptionPane.YES_OPTION) {
                Komunikacija.getInstanca().obrisiStudenta(forma.getSelektovani());

                JOptionPane.showMessageDialog(forma, "Sistem je obrisao studenta.",
                        "Uspeh", JOptionPane.INFORMATION_MESSAGE);

                ocisti();
                ucitajPodatke();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(forma, "Sistem ne moze da obrise studenta: " + ex.getMessage(),
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
                } else {
                    kriterijum = " JOIN studijskiprogram sp ON student.idStudProgram = sp.idStudProgram WHERE sp.naziv LIKE '%" + tekst + "%'";
                }
            }

            List<Student> lista = Komunikacija.getInstanca().vratiListuStudenata(kriterijum);
            DefaultTableModel model = forma.getTableModel();
            model.setRowCount(0);

            for (Student s : lista) {
                model.addRow(new Object[]{
                    s.getIndeks(),
                    s.getJmbg(),
                    s.getIme(),
                    s.getPrezime(),
                    s.getMestoRodjenja(),
                    s.getAdresaStanovanja(),
                    s.getStudijskiProgram() != null ? s.getStudijskiProgram().getNaziv() : ""
                });
            }

            if (lista.isEmpty()) {
                JOptionPane.showMessageDialog(forma, "Sistem ne moze da nadje studente po zadatim kriterijumima.",
                        "Informacija", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(forma, "Sistem je nasao studente po zadatim kriterijumima.",
                        "Uspeh", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(forma, "Greska pri pretrazi: " + ex.getMessage(),
                    "Greska", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ocisti() {
        forma.getTxtIndeks().setText("");
        forma.getTxtIndeks().setEditable(true);
        forma.getTxtJmbg().setText("");
        forma.getTxtIme().setText("");
        forma.getTxtPrezime().setText("");
        forma.getTxtMestoRodjenja().setText("");
        forma.getTxtAdresaStanovanja().setText("");
        if (forma.getCmbStudijskiProgram().getItemCount() > 0) {
            forma.getCmbStudijskiProgram().setSelectedIndex(0);
        }
        forma.setSelektovani(null);
        forma.getTblStudenti().clearSelection();
    }

    private void popuniFormu() {
        int red = forma.getTblStudenti().getSelectedRow();
        if (red >= 0) {
            String indeks = (String) forma.getTableModel().getValueAt(red, 0);
            String jmbg = (String) forma.getTableModel().getValueAt(red, 1);
            String ime = (String) forma.getTableModel().getValueAt(red, 2);
            String prezime = (String) forma.getTableModel().getValueAt(red, 3);
            String mestoRodjenja = (String) forma.getTableModel().getValueAt(red, 4);
            String adresaStanovanja = (String) forma.getTableModel().getValueAt(red, 5);
            String programNaziv = (String) forma.getTableModel().getValueAt(red, 6);

            forma.getTxtIndeks().setText(indeks);
            forma.getTxtIndeks().setEditable(false);
            forma.getTxtJmbg().setText(jmbg);
            forma.getTxtIme().setText(ime);
            forma.getTxtPrezime().setText(prezime);
            forma.getTxtMestoRodjenja().setText(mestoRodjenja);
            forma.getTxtAdresaStanovanja().setText(adresaStanovanja);

            for (int i = 0; i < forma.getCmbStudijskiProgram().getItemCount(); i++) {
                StudijskiProgram sp = forma.getCmbStudijskiProgram().getItemAt(i);
                if (sp.getNaziv().equals(programNaziv)) {
                    forma.getCmbStudijskiProgram().setSelectedIndex(i);
                    break;
                }
            }

            Student s = new Student();
            s.setIndeks(indeks);
            s.setJmbg(jmbg);
            s.setIme(ime);
            s.setPrezime(prezime);
            s.setMestoRodjenja(mestoRodjenja);
            s.setAdresaStanovanja(adresaStanovanja);
            s.setStudijskiProgram((StudijskiProgram) forma.getCmbStudijskiProgram().getSelectedItem());
            forma.setSelektovani(s);
        }
    }

    private boolean validirajPodatke(String indeks, String jmbg, String ime, String prezime, StudijskiProgram sp) {
        if (indeks.isEmpty()) {
            JOptionPane.showMessageDialog(forma, "Indeks je obavezan!",
                    "Upozorenje", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        // Provera indeksa BEZ REGEXA (format gggg/bbbb, npr. 2023/0342)
        boolean indeksValidan = true;
        if (indeks.length() != 9 || indeks.charAt(4) != '/') {
            indeksValidan = false;
        } else {
            for (int i = 0; i < indeks.length(); i++) {
                if (i == 4) {
                    continue; // preskačemo kosu crtu '/'
                }
                if (!Character.isDigit(indeks.charAt(i))) {
                    indeksValidan = false;
                    break;
                }
            }
        }
        if (!indeksValidan) {
            JOptionPane.showMessageDialog(forma, "Indeks mora biti u formatu gggg/bbbb (npr. 2023/0342)!",
                    "Upozorenje", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (jmbg.isEmpty()) {
            JOptionPane.showMessageDialog(forma, "JMBG je obavezan!",
                    "Upozorenje", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        // Provera JMBG BEZ REGEXA (tačno 13 cifara)
        boolean jmbgValidan = true;
        if (jmbg.length() != 13) {
            jmbgValidan = false;
        } else {
            for (int i = 0; i < jmbg.length(); i++) {
                if (!Character.isDigit(jmbg.charAt(i))) {
                    jmbgValidan = false;
                    break;
                }
            }
        }
        if (!jmbgValidan) {
            JOptionPane.showMessageDialog(forma, "JMBG mora imati tacno 13 cifara!",
                    "Upozorenje", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (ime.isEmpty()) {
            JOptionPane.showMessageDialog(forma, "Ime je obavezno!",
                    "Upozorenje", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (prezime.isEmpty()) {
            JOptionPane.showMessageDialog(forma, "Prezime je obavezno!",
                    "Upozorenje", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (sp == null) {
            JOptionPane.showMessageDialog(forma, "Studijski program je obavezan!",
                    "Upozorenje", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
}
