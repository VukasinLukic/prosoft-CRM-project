/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kontroleri;

import domen.TipPolja;
import domen.tipPodatka;
import forme.TipPoljaForma;
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
public class TipPoljaController {
    private final TipPoljaForma forma;

    public TipPoljaController(TipPoljaForma forma) {
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
            List<TipPolja> lista = Komunikacija.getInstanca().vratiSveTipovePolja();
            DefaultTableModel model = forma.getTableModel();
            model.setRowCount(0);
            
            for (TipPolja tp : lista) {
                model.addRow(new Object[]{
                    tp.getIdPolja(),
                    tp.getNazivPolja(),
                    tp.getTipPodatka(),
                    tp.getStranica(),
                    tp.isPodrzavaOCR() ? "Da" : "Ne",
                    tp.isObaveznoPolje() ? "Da" : "Ne"
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(forma, "Greška pri učitavanju: " + ex.getMessage(), 
                "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void dodaj() {
        try {
            TipPolja tp = prikupiPodatke();
            if (tp == null) return;
            
            Komunikacija.getInstanca().kreirajTipPolja(tp);
            
            JOptionPane.showMessageDialog(forma, "Tip polja uspešno dodat!", 
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
                JOptionPane.showMessageDialog(forma, "Izaberite tip polja iz tabele!", 
                    "Upozorenje", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            TipPolja tp = prikupiPodatke();
            if (tp == null) return;
            
            tp.setIdPolja(forma.getSelektovani().getIdPolja());
            
            Komunikacija.getInstanca().promeniTipPolja(tp);
            
            JOptionPane.showMessageDialog(forma, "Tip polja uspešno izmenjen!", 
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
                JOptionPane.showMessageDialog(forma, "Izaberite tip polja iz tabele!", 
                    "Upozorenje", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int opcija = JOptionPane.showConfirmDialog(forma, 
                "Da li ste sigurni da želite da obrišete ovaj tip polja?", 
                "Potvrda brisanja", JOptionPane.YES_NO_OPTION);
            
            if (opcija == JOptionPane.YES_OPTION) {
                Komunikacija.getInstanca().obrisiTipPolja(forma.getSelektovani());
                
                JOptionPane.showMessageDialog(forma, "Tip polja uspešno obrisan!", 
                    "Uspeh", JOptionPane.INFORMATION_MESSAGE);
                
                ocisti();
                ucitajPodatke();
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(forma, "Greška: " + ex.getMessage(), 
                "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private TipPolja prikupiPodatke() {
        String naziv = forma.getTxtNazivPolja().getText().trim();
        tipPodatka tip = (tipPodatka) forma.getCmbTipPodatka().getSelectedItem();
        String regex = forma.getTxtRegexValidacija().getText().trim();
        int pozX = 0;
        int pozY = 0;
        int sirina = 0;
        int visina = 0;
        try {
            pozX = Integer.parseInt(forma.getTxtPozicijaX().getText().trim());
            pozY = Integer.parseInt(forma.getTxtPozicijaY().getText().trim());
            sirina = Integer.parseInt(forma.getTxtSirina().getText().trim());
            visina = Integer.parseInt(forma.getTxtVisina().getText().trim());
        } catch (NumberFormatException ex) {}
        int stranica = (int) forma.getSpnStranica().getValue();
        int redosled = (int) forma.getSpnRedosledObrade().getValue();
        boolean podrzavaOCR = forma.getChkPodrzavaOCR().isSelected();
        boolean obavezno = forma.getChkObaveznoPolje().isSelected();
        
        if (naziv.isEmpty()) {
            JOptionPane.showMessageDialog(forma, "Naziv polja je obavezan!", 
                "Upozorenje", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        
        TipPolja tp = new TipPolja();
        tp.setNazivPolja(naziv);
        tp.setTipPodatka(tip);
        tp.setRegexValidacija(regex.isEmpty() ? null : regex);
        tp.setPozicijaX(pozX);
        tp.setPozicijaY(pozY);
        tp.setSirina(sirina);
        tp.setVisina(visina);
        tp.setStranica(stranica);
        tp.setRedosledObrade(redosled);
        tp.setPodrzavaOCR(podrzavaOCR);
        tp.setObaveznoPolje(obavezno);
        
        return tp;
    }
    
    private void ocisti() {
        forma.getTxtNazivPolja().setText("");
        forma.getCmbTipPodatka().setSelectedIndex(0);
        forma.getTxtRegexValidacija().setText("");
        forma.getTxtPozicijaX().setText("0");
        forma.getTxtPozicijaY().setText("0");
        forma.getTxtSirina().setText("0");
        forma.getTxtVisina().setText("0");
        forma.getSpnStranica().setValue(1);
        forma.getSpnRedosledObrade().setValue(0);
        forma.getChkPodrzavaOCR().setSelected(true);
        forma.getChkObaveznoPolje().setSelected(false);
        forma.setSelektovani(null);
        forma.getTblTipoviPolja().clearSelection();
    }
    
    private void popuniFormu() {
        int red = forma.getTblTipoviPolja().getSelectedRow();
        if (red >= 0) {
            try {
                int id = (int) forma.getTableModel().getValueAt(red, 0);
                
                // Dohvati kompletan objekat sa servera
                TipPolja tpZaPretragu = new TipPolja();
                tpZaPretragu.setIdPolja(id);
                
                // Za sada popunjavamo iz tabele (pojednostavljena verzija)
                String naziv = (String) forma.getTableModel().getValueAt(red, 1);
                tipPodatka tip = (tipPodatka) forma.getTableModel().getValueAt(red, 2);
                int stranica = (int) forma.getTableModel().getValueAt(red, 3);
                boolean ocr = forma.getTableModel().getValueAt(red, 4).equals("Da");
                boolean obavezno = forma.getTableModel().getValueAt(red, 5).equals("Da");
                
                forma.getTxtNazivPolja().setText(naziv);
                forma.getCmbTipPodatka().setSelectedItem(tip);
                forma.getSpnStranica().setValue(stranica);
                forma.getChkPodrzavaOCR().setSelected(ocr);
                forma.getChkObaveznoPolje().setSelected(obavezno);
                
                TipPolja tp = new TipPolja();
                tp.setIdPolja(id);
                tp.setNazivPolja(naziv);
                tp.setTipPodatka(tip);
                tp.setStranica(stranica);
                tp.setPodrzavaOCR(ocr);
                tp.setObaveznoPolje(obavezno);
                forma.setSelektovani(tp);
                
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
