/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kontroleri;

import domen.StudijskiProgram;
import domen.stepenStudija;
import forme.StudijskiProgramForma;
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
public class StudijskiProgramController {

    private final StudijskiProgramForma forma;

    public StudijskiProgramController(StudijskiProgramForma forma) {
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
            List<StudijskiProgram> lista = Komunikacija.getInstanca().vratiSveStudijskePrograme();
            DefaultTableModel model = forma.getTableModel();
            model.setRowCount(0);

            for (StudijskiProgram sp : lista) {
                model.addRow(new Object[]{
                    sp.getIdStudProgram(),
                    sp.getNaziv(),
                    sp.getOznaka(),
                    sp.getStepenStudija()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(forma, "Greška pri učitavanju: " + ex.getMessage(),
                    "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void dodaj() {
        try {
            String naziv = forma.getTxtNaziv().getText().trim();
            String oznaka = forma.getTxtOznaka().getText().trim();
            stepenStudija stepen = (stepenStudija) forma.getCmbStepen().getSelectedItem();

            if (naziv.isEmpty() || oznaka.isEmpty()) {
                JOptionPane.showMessageDialog(forma, "Naziv i oznaka su obavezni!",
                        "Upozorenje", JOptionPane.WARNING_MESSAGE);
                return;
            }

            StudijskiProgram sp = new StudijskiProgram();
            sp.setNaziv(naziv);
            sp.setOznaka(oznaka.toUpperCase());
            sp.setStepenStudija(stepen);

            Komunikacija.getInstanca().kreirajStudijskiProgram(sp);

            JOptionPane.showMessageDialog(forma, "Studijski program uspešno dodat!",
                    "Uspeh", JOptionPane.INFORMATION_MESSAGE);

            ocisti();
            ucitajPodatke();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(forma, "Greška: " + ex.getMessage(),
                    "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void sacuvaj() {
        try {
            if (forma.getSelektovani() == null) {
                JOptionPane.showMessageDialog(forma, "Izaberite studijski program iz tabele!",
                        "Upozorenje", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String naziv = forma.getTxtNaziv().getText().trim();
            String oznaka = forma.getTxtOznaka().getText().trim();
            stepenStudija stepen = (stepenStudija) forma.getCmbStepen().getSelectedItem();

            if (naziv.isEmpty() || oznaka.isEmpty()) {
                JOptionPane.showMessageDialog(forma, "Naziv i oznaka su obavezni!",
                        "Upozorenje", JOptionPane.WARNING_MESSAGE);
                return;
            }

            StudijskiProgram sp = forma.getSelektovani();
            sp.setNaziv(naziv);
            sp.setOznaka(oznaka.toUpperCase());
            sp.setStepenStudija(stepen);

            Komunikacija.getInstanca().promeniStudijskiProgram(sp);

            JOptionPane.showMessageDialog(forma, "Studijski program uspešno izmenjen!",
                    "Uspeh", JOptionPane.INFORMATION_MESSAGE);

            ocisti();
            ucitajPodatke();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(forma, "Greška: " + ex.getMessage(),
                    "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void obrisi() {
        try {
            if (forma.getSelektovani() == null) {
                JOptionPane.showMessageDialog(forma, "Izaberite studijski program iz tabele!",
                        "Upozorenje", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int opcija = JOptionPane.showConfirmDialog(forma,
                    "Da li ste sigurni da želite da obrišete ovaj studijski program?",
                    "Potvrda brisanja", JOptionPane.YES_NO_OPTION);

            if (opcija == JOptionPane.YES_OPTION) {
                Komunikacija.getInstanca().obrisiStudijskiProgram(forma.getSelektovani());

                JOptionPane.showMessageDialog(forma, "Studijski program uspešno obrisan!",
                        "Uspeh", JOptionPane.INFORMATION_MESSAGE);

                ocisti();
                ucitajPodatke();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(forma, "Greška: " + ex.getMessage(),
                    "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ocisti() {
        forma.getTxtNaziv().setText("");
        forma.getTxtOznaka().setText("");
        forma.getCmbStepen().setSelectedIndex(0);
        forma.setSelektovani(null);
        forma.getTblStudijskiProgrami().clearSelection();
    }

    private void popuniFormu() {
        int red = forma.getTblStudijskiProgrami().getSelectedRow();
        if (red >= 0) {
            int id = (int) forma.getTableModel().getValueAt(red, 0);
            String naziv = (String) forma.getTableModel().getValueAt(red, 1);
            String oznaka = (String) forma.getTableModel().getValueAt(red, 2);
            stepenStudija stepen = (stepenStudija) forma.getTableModel().getValueAt(red, 3);

            forma.getTxtNaziv().setText(naziv);
            forma.getTxtOznaka().setText(oznaka);
            forma.getCmbStepen().setSelectedItem(stepen);

            StudijskiProgram sp = new StudijskiProgram();
            sp.setIdStudProgram(id);
            sp.setNaziv(naziv);
            sp.setOznaka(oznaka);
            sp.setStepenStudija(stepen);
            forma.setSelektovani(sp);
        }
    }
}
