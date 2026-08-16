/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kontroleri;

import domen.StavkeObrasca;
import domen.TipPolja;
import forme.StavkeObrascaForma;
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
public class StavkeObrascaController {

    private final StavkeObrascaForma forma;

    public StavkeObrascaController(StavkeObrascaForma forma) {
        this.forma = forma;
        addActionListeners();
        ucitajTipovePolja();
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

        forma.addOcistiListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ocisti();
            }
        });

        forma.addZatvoriListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                forma.dispose();
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

    private void ucitajTipovePolja() {
        try {
            List<TipPolja> lista = Komunikacija.getInstanca().vratiSveTipovePolja();
            forma.getCmbTipPolja().removeAllItems();
            for (TipPolja tp : lista) {
                forma.getCmbTipPolja().addItem(tp);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(forma, "Greska pri ucitavanju tipova polja: " + ex.getMessage(),
                    "Greska", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ucitajPodatke() {
        try {
            StavkeObrasca kriterijum = new StavkeObrasca();
            kriterijum.setIdObrazac(forma.getObrazac());

            List<StavkeObrasca> lista = Komunikacija.getInstanca().pretraziStavkeObrasca(kriterijum);
            DefaultTableModel model = forma.getTableModel();
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
            forma.getTableModel().setRowCount(0);
        }
    }

    private void dodaj() {
        try {
            TipPolja tipPolja = (TipPolja) forma.getCmbTipPolja().getSelectedItem();
            String ocrVrednost = forma.getTxtOcrVrednost().getText().trim();
            String korigovanaVrednost = forma.getTxtKorigovanaVrednost().getText().trim();
            double nivoPodudarnosti = (double) forma.getSpnNivoPodudarnosti().getValue();
            boolean ocrUspesno = forma.getChkOcrUspesno().isSelected();

            if (!validirajPodatke(tipPolja, ocrVrednost, nivoPodudarnosti, ocrUspesno)) {
                return;
            }

            StavkeObrasca s = new StavkeObrasca();
            s.setIdObrazac(forma.getObrazac());
            s.setOcrVrednost(ocrVrednost.isEmpty() ? null : ocrVrednost);
            s.setKorigovanaVrednost(korigovanaVrednost.isEmpty() ? null : korigovanaVrednost);
            s.setNivoPodudarnosti(nivoPodudarnosti);
            s.setOcrUspesno(ocrUspesno);
            s.setIdPolja(tipPolja);

            Komunikacija.getInstanca().kreirajStavkuObrasca(s);

            JOptionPane.showMessageDialog(forma, "Sistem je zapamtio stavku obrasca.",
                    "Uspeh", JOptionPane.INFORMATION_MESSAGE);

            ocisti();
            ucitajPodatke();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(forma, "Sistem ne moze da zapamti stavku obrasca: " + ex.getMessage(),
                    "Greska", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void sacuvaj() {
        try {
            if (forma.getSelektovana() == null) {
                JOptionPane.showMessageDialog(forma, "Izaberite stavku iz tabele!",
                        "Upozorenje", JOptionPane.WARNING_MESSAGE);
                return;
            }

            TipPolja tipPolja = (TipPolja) forma.getCmbTipPolja().getSelectedItem();
            String ocrVrednost = forma.getTxtOcrVrednost().getText().trim();
            String korigovanaVrednost = forma.getTxtKorigovanaVrednost().getText().trim();
            double nivoPodudarnosti = (double) forma.getSpnNivoPodudarnosti().getValue();
            boolean ocrUspesno = forma.getChkOcrUspesno().isSelected();

            if (!validirajPodatke(tipPolja, ocrVrednost, nivoPodudarnosti, ocrUspesno)) {
                return;
            }

            StavkeObrasca s = forma.getSelektovana();
            s.setOcrVrednost(ocrVrednost.isEmpty() ? null : ocrVrednost);
            s.setKorigovanaVrednost(korigovanaVrednost.isEmpty() ? null : korigovanaVrednost);
            s.setNivoPodudarnosti(nivoPodudarnosti);
            s.setOcrUspesno(ocrUspesno);
            s.setIdPolja(tipPolja);

            Komunikacija.getInstanca().promeniStavkuObrasca(s);

            JOptionPane.showMessageDialog(forma, "Sistem je zapamtio stavku obrasca.",
                    "Uspeh", JOptionPane.INFORMATION_MESSAGE);

            ocisti();
            ucitajPodatke();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(forma, "Sistem ne moze da zapamti stavku obrasca: " + ex.getMessage(),
                    "Greska", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ocisti() {
        if (forma.getCmbTipPolja().getItemCount() > 0) {
            forma.getCmbTipPolja().setSelectedIndex(0);
        }
        forma.getTxtOcrVrednost().setText("");
        forma.getTxtKorigovanaVrednost().setText("");
        forma.getSpnNivoPodudarnosti().setValue(0.0);
        forma.getChkOcrUspesno().setSelected(false);
        forma.setSelektovana(null);
        forma.getTblStavke().clearSelection();
    }

    private void popuniFormu() {
        int red = forma.getTblStavke().getSelectedRow();
        if (red >= 0) {
            int idStavke = (int) forma.getTableModel().getValueAt(red, 0);
            String tipPoljaNaziv = (String) forma.getTableModel().getValueAt(red, 1);
            String ocrVrednost = (String) forma.getTableModel().getValueAt(red, 2);
            String korigovanaVrednost = (String) forma.getTableModel().getValueAt(red, 3);
            String podudarnostStr = (String) forma.getTableModel().getValueAt(red, 4);
            String ocrUspesnoStr = (String) forma.getTableModel().getValueAt(red, 5);

            double podudarnost = Double.parseDouble(podudarnostStr.replace(",", "."));
            boolean ocrUspesno = "Da".equals(ocrUspesnoStr);

            forma.getTxtOcrVrednost().setText(ocrVrednost != null ? ocrVrednost : "");
            forma.getTxtKorigovanaVrednost().setText(korigovanaVrednost != null ? korigovanaVrednost : "");
            forma.getSpnNivoPodudarnosti().setValue(podudarnost);
            forma.getChkOcrUspesno().setSelected(ocrUspesno);

            for (int i = 0; i < forma.getCmbTipPolja().getItemCount(); i++) {
                TipPolja tp = forma.getCmbTipPolja().getItemAt(i);
                if (tp.getNazivPolja() != null && tp.getNazivPolja().equals(tipPoljaNaziv)) {
                    forma.getCmbTipPolja().setSelectedIndex(i);
                    break;
                }
            }

            StavkeObrasca s = new StavkeObrasca();
            s.setIdStavke(idStavke);
            s.setIdObrazac(forma.getObrazac());
            s.setOcrVrednost(ocrVrednost);
            s.setKorigovanaVrednost(korigovanaVrednost);
            s.setNivoPodudarnosti(podudarnost);
            s.setOcrUspesno(ocrUspesno);
            s.setIdPolja((TipPolja) forma.getCmbTipPolja().getSelectedItem());
            forma.setSelektovana(s);
        }
    }

    private boolean validirajPodatke(TipPolja tipPolja, String ocrVrednost,
            double nivoPodudarnosti, boolean ocrUspesno) {

        if (tipPolja == null) {
            JOptionPane.showMessageDialog(forma, "Tip polja je obavezan!",
                    "Upozorenje", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (nivoPodudarnosti < 0 || nivoPodudarnosti > 100) {
            JOptionPane.showMessageDialog(forma, "Nivo podudarnosti mora biti izmedju 0 i 100!",
                    "Upozorenje", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (ocrUspesno && nivoPodudarnosti <= 0) {
            JOptionPane.showMessageDialog(forma, "Ako je OCR uspesan, nivo podudarnosti mora biti veci od 0!",
                    "Upozorenje", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (tipPolja.getTipPodatka() != null
                && tipPolja.getTipPodatka().name().equals("BOOLEAN")
                && !ocrVrednost.isEmpty()) {
            if (!ocrVrednost.matches("(?i)True|False|Da|Ne|0|1")) {
                JOptionPane.showMessageDialog(forma,
                        "Za BOOLEAN tip polja, OCR vrednost mora biti: True, False, Da, Ne, 0 ili 1!",
                        "Upozorenje", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }

        return true;
    }
}
