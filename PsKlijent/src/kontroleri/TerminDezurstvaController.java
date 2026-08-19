/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kontroleri;

import domen.TerminDezurstva;
import domen.tipTermina;
import forme.TerminDezurstvaForma;
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
public class TerminDezurstvaController {

    private final TerminDezurstvaForma forma;

    public TerminDezurstvaController(TerminDezurstvaForma forma) {
        this.forma = forma;
        addActionListeners();
        ucitajPodatke();
    }

    private void addActionListeners() {

        forma.addDodajListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ubaci();
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
            List<TerminDezurstva> lista = Komunikacija.getInstanca().vratiSveTermineDezurstva();
            DefaultTableModel model = forma.getTableModel();
            model.setRowCount(0);

            for (TerminDezurstva td : lista) {
                model.addRow(new Object[]{
                    td.getIdTerminDezurstva(),
                    td.getTipTermina(),
                    td.getKancelarija()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(forma, "Greška pri učitavanju: " + ex.getMessage(),
                    "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ubaci() {
        try {
            tipTermina tip = (tipTermina) forma.getCmbTipTermina().getSelectedItem();
            String kancelarija = forma.getTxtKancelarija().getText().trim();

            if (kancelarija.isEmpty()) {
                JOptionPane.showMessageDialog(forma, "Kancelarija je obavezna!",
                        "Upozorenje", JOptionPane.WARNING_MESSAGE);
                return;
            }

            TerminDezurstva td = new TerminDezurstva();
            td.setTipTermina(tip);
            td.setKancelarija(kancelarija);

            Komunikacija.getInstanca().ubaciTerminDezurstva(td);

            JOptionPane.showMessageDialog(forma, "Termin dežurstva uspešno ubačen!",
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
                JOptionPane.showMessageDialog(forma, "Izaberite termin iz tabele!",
                        "Upozorenje", JOptionPane.WARNING_MESSAGE);
                return;
            }

            tipTermina tip = (tipTermina) forma.getCmbTipTermina().getSelectedItem();
            String kancelarija = forma.getTxtKancelarija().getText().trim();

            if (kancelarija.isEmpty()) {
                JOptionPane.showMessageDialog(forma, "Kancelarija je obavezna!",
                        "Upozorenje", JOptionPane.WARNING_MESSAGE);
                return;
            }

            TerminDezurstva td = forma.getSelektovani();
            td.setTipTermina(tip);
            td.setKancelarija(kancelarija);

            Komunikacija.getInstanca().promeniTerminDezurstva(td);

            JOptionPane.showMessageDialog(forma, "Termin dežurstva uspešno izmenjen!",
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
                JOptionPane.showMessageDialog(forma, "Izaberite termin iz tabele!",
                        "Upozorenje", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int opcija = JOptionPane.showConfirmDialog(forma,
                    "Da li ste sigurni da želite da obrišete ovaj termin?",
                    "Potvrda brisanja", JOptionPane.YES_NO_OPTION);

            if (opcija == JOptionPane.YES_OPTION) {
                Komunikacija.getInstanca().obrisiTerminDezurstva(forma.getSelektovani());

                JOptionPane.showMessageDialog(forma, "Termin dežurstva uspešno obrisan!",
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
        forma.getCmbTipTermina().setSelectedIndex(0);
        forma.getTxtKancelarija().setText("");
        forma.setSelektovani(null);
        forma.getTblTermini().clearSelection();
    }

    private void popuniFormu() {
        int red = forma.getTblTermini().getSelectedRow();
        if (red >= 0) {
            int id = (int) forma.getTableModel().getValueAt(red, 0);
            tipTermina tip = (tipTermina) forma.getTableModel().getValueAt(red, 1);
            String kancelarija = (String) forma.getTableModel().getValueAt(red, 2);

            forma.getCmbTipTermina().setSelectedItem(tip);
            forma.getTxtKancelarija().setText(kancelarija);

            TerminDezurstva td = new TerminDezurstva();
            td.setIdTerminDezurstva(id);
            td.setTipTermina(tip);
            td.setKancelarija(kancelarija);
            forma.setSelektovani(td);
        }
    }
}
