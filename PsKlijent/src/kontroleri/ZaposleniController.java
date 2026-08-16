/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kontroleri;

import domen.ZaposleniFakulteta;
import forme.ZaposleniForma;
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
public class ZaposleniController {
    private final ZaposleniForma forma;

    public ZaposleniController(ZaposleniForma forma) {
        this.forma = forma;
        addActionListeners();
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

    private void ucitajPodatke() {
        try {
            List<ZaposleniFakulteta> lista = Komunikacija.getInstanca().vratiSveZaposlene();
            DefaultTableModel model = forma.getTableModel();
            model.setRowCount(0);

            for (ZaposleniFakulteta z : lista) {
                model.addRow(new Object[]{
                    z.getIdZaposlenog(),
                    z.getIme(),
                    z.getPrezime(),
                    z.getKorisnickoIme(),
                    z.getEmail()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(forma, "Greska pri ucitavanju: " + ex.getMessage(),
                "Greska", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void dodaj() {
        try {
            String ime = forma.getTxtIme().getText().trim();
            String prezime = forma.getTxtPrezime().getText().trim();
            String korisnickoIme = forma.getTxtKorisnickoIme().getText().trim();
            String email = forma.getTxtEmail().getText().trim();
            String sifra = new String(forma.getTxtSifra().getPassword());
            String potvrdaSifre = new String(forma.getTxtPotvrdaSifre().getPassword());

            if (!validirajPodatke(ime, prezime, korisnickoIme, email, sifra, potvrdaSifre, true)) {
                return;
            }

            ZaposleniFakulteta z = new ZaposleniFakulteta();
            z.setIme(ime);
            z.setPrezime(prezime);
            z.setKorisnickoIme(korisnickoIme);
            z.setEmail(email);
            z.setSifra(sifra);

            Komunikacija.getInstanca().kreirajZaposlenog(z);

            JOptionPane.showMessageDialog(forma, "Sistem je zapamtio zaposlenog fakulteta.",
                "Uspeh", JOptionPane.INFORMATION_MESSAGE);

            ocisti();
            ucitajPodatke();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(forma, "Sistem ne moze da zapamti zaposlenog: " + ex.getMessage(),
                "Greska", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void sacuvaj() {
        try {
            if (forma.getSelektovani() == null) {
                JOptionPane.showMessageDialog(forma, "Izaberite zaposlenog iz tabele!",
                    "Upozorenje", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String ime = forma.getTxtIme().getText().trim();
            String prezime = forma.getTxtPrezime().getText().trim();
            String korisnickoIme = forma.getTxtKorisnickoIme().getText().trim();
            String email = forma.getTxtEmail().getText().trim();
            String sifra = new String(forma.getTxtSifra().getPassword());
            String potvrdaSifre = new String(forma.getTxtPotvrdaSifre().getPassword());

            boolean sifraPromenjena = !sifra.isEmpty();
            if (!validirajPodatke(ime, prezime, korisnickoIme, email, sifra, potvrdaSifre, sifraPromenjena)) {
                return;
            }

            ZaposleniFakulteta z = forma.getSelektovani();
            z.setIme(ime);
            z.setPrezime(prezime);
            z.setKorisnickoIme(korisnickoIme);
            z.setEmail(email);
            if (sifraPromenjena) {
                z.setSifra(sifra);
            }

            Komunikacija.getInstanca().promeniZaposlenog(z);

            JOptionPane.showMessageDialog(forma, "Sistem je zapamtio zaposlenog fakulteta.",
                "Uspeh", JOptionPane.INFORMATION_MESSAGE);

            ocisti();
            ucitajPodatke();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(forma, "Sistem ne moze da zapamti zaposlenog: " + ex.getMessage(),
                "Greska", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void obrisi() {
        try {
            if (forma.getSelektovani() == null) {
                JOptionPane.showMessageDialog(forma, "Izaberite zaposlenog iz tabele!",
                    "Upozorenje", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int opcija = JOptionPane.showConfirmDialog(forma,
                "Da li ste sigurni da zelite da obrisete ovog zaposlenog?",
                "Potvrda brisanja", JOptionPane.YES_NO_OPTION);

            if (opcija == JOptionPane.YES_OPTION) {
                Komunikacija.getInstanca().obrisiZaposlenog(forma.getSelektovani());

                JOptionPane.showMessageDialog(forma, "Sistem je obrisao zaposlenog fakulteta.",
                    "Uspeh", JOptionPane.INFORMATION_MESSAGE);

                ocisti();
                ucitajPodatke();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(forma, "Sistem ne moze da obrise zaposlenog: " + ex.getMessage(),
                "Greska", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void pretrazi() {
        try {
            String tekst = forma.getTxtPretraga().getText().trim();

            String kriterijum = "";
            if (!tekst.isEmpty()) {
                kriterijum = " WHERE ime LIKE '%" + tekst + "%' OR prezime LIKE '%" + tekst + "%' OR korisnickoIme LIKE '%" + tekst + "%'";
            }

            List<ZaposleniFakulteta> lista = Komunikacija.getInstanca().vratiListuZaposlenih(kriterijum);
            DefaultTableModel model = forma.getTableModel();
            model.setRowCount(0);

            for (ZaposleniFakulteta z : lista) {
                model.addRow(new Object[]{
                    z.getIdZaposlenog(),
                    z.getIme(),
                    z.getPrezime(),
                    z.getKorisnickoIme(),
                    z.getEmail()
                });
            }

            if (lista.isEmpty()) {
                JOptionPane.showMessageDialog(forma, "Sistem ne moze da nadje zaposlene po zadatim kriterijumima.",
                    "Informacija", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(forma, "Sistem je nasao zaposlene po zadatim kriterijumima.",
                    "Uspeh", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(forma, "Greska pri pretrazi: " + ex.getMessage(),
                "Greska", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ocisti() {
        forma.getTxtIme().setText("");
        forma.getTxtPrezime().setText("");
        forma.getTxtKorisnickoIme().setText("");
        forma.getTxtEmail().setText("");
        forma.getTxtSifra().setText("");
        forma.getTxtPotvrdaSifre().setText("");
        forma.setSelektovani(null);
        forma.getTblZaposleni().clearSelection();
    }

    private void popuniFormu() {
        int red = forma.getTblZaposleni().getSelectedRow();
        if (red >= 0) {
            int id = (int) forma.getTableModel().getValueAt(red, 0);
            String ime = (String) forma.getTableModel().getValueAt(red, 1);
            String prezime = (String) forma.getTableModel().getValueAt(red, 2);
            String korisnickoIme = (String) forma.getTableModel().getValueAt(red, 3);
            String email = (String) forma.getTableModel().getValueAt(red, 4);

            forma.getTxtIme().setText(ime);
            forma.getTxtPrezime().setText(prezime);
            forma.getTxtKorisnickoIme().setText(korisnickoIme);
            forma.getTxtEmail().setText(email);
            forma.getTxtSifra().setText("");
            forma.getTxtPotvrdaSifre().setText("");

            ZaposleniFakulteta z = new ZaposleniFakulteta();
            z.setIdZaposlenog(id);
            z.setIme(ime);
            z.setPrezime(prezime);
            z.setKorisnickoIme(korisnickoIme);
            z.setEmail(email);
            forma.setSelektovani(z);
        }
    }

    private boolean validirajPodatke(String ime, String prezime, String korisnickoIme,
            String email, String sifra, String potvrdaSifre, boolean validacijaSifre) {

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

        if (korisnickoIme.isEmpty()) {
            JOptionPane.showMessageDialog(forma, "Korisnicko ime je obavezno!",
                "Upozorenje", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (korisnickoIme.length() < 5) {
            JOptionPane.showMessageDialog(forma, "Korisnicko ime mora imati najmanje 5 karaktera!",
                "Upozorenje", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (korisnickoIme.equals(prezime) || korisnickoIme.equals(ime)) {
            JOptionPane.showMessageDialog(forma, "Korisnicko ime ne moze biti isto kao ime ili prezime!",
                "Upozorenje", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(forma, "Email je obavezan!",
                "Upozorenje", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (!email.contains("@")) {
            JOptionPane.showMessageDialog(forma, "Email mora sadrzati @!",
                "Upozorenje", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (validacijaSifre) {
            if (sifra.isEmpty()) {
                JOptionPane.showMessageDialog(forma, "Sifra je obavezna!",
                    "Upozorenje", JOptionPane.WARNING_MESSAGE);
                return false;
            }

            if (sifra.length() < 8) {
                JOptionPane.showMessageDialog(forma, "Sifra mora imati najmanje 8 karaktera!",
                    "Upozorenje", JOptionPane.WARNING_MESSAGE);
                return false;
            }

            if (!sifra.equals(potvrdaSifre)) {
                JOptionPane.showMessageDialog(forma, "Sifre se ne poklapaju!",
                    "Upozorenje", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }

        return true;
    }
}
