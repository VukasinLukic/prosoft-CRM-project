/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kontroleri;

import forme.GlavnaForma;
import forme.SV20ObrazacForma;
import forme.StudentForma;
import forme.StudijskiProgramForma;
import forme.TerminDezurstvaForma;
import forme.TipPoljaForma;
import forme.ZaposleniForma;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/**
 *
 * @author Vukasin Lukic
 */
public class GlavnaFormaController {

    private final GlavnaForma gf;

    // Svaki ekran se pravi jednom (lenjo, pri prvoj poseti) i posle se samo prikazuje —
    // navigacija nikad ne otvara nov prozor niti ponovo učitava podatke sa servera.
    private boolean obrasciOtvoreni;
    private boolean studentiOtvoreni;
    private boolean zaposleniOtvoreni;
    private boolean studijskiProgramOtvoren;
    private boolean terminiOtvoreni;
    private boolean tipoviPoljaOtvoreni;

    public GlavnaFormaController(GlavnaForma gf) {
        this.gf = gf;
        addActionListeners();
        // Prva stranica po ulasku u aplikaciju je direktno upravljanje ŠV-20 obrascima —
        // nema posebne "meni" stranice pre toga.
        otvoriSV20Obrazac();
    }

    private void addActionListeners() {

        gf.addSV20ObrazacListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { otvoriSV20Obrazac(); }
        });

        gf.addStudentListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { otvoriStudent(); }
        });

        gf.addZaposleniListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { otvoriZaposleni(); }
        });

        gf.addStudijskiProgramListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { otvoriStudijskiProgram(); }
        });

        gf.addTerminDezurstvaListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { otvoriTerminDezurstva(); }
        });

        gf.addTipPoljaListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { otvoriTipPolja(); }
        });

        gf.addPodesavanjaListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { podesavanja(); }
        });

        gf.addOdjavaListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { odjava(); }
        });

        gf.addOProgramuListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { oProgramu(); }
        });
    }

    private void otvoriSV20Obrazac() {
        if (!obrasciOtvoreni) {
            SV20ObrazacForma forma = new SV20ObrazacForma();
            new SV20ObrazacController(forma);
            gf.registrujIPrikaziEkran(GlavnaForma.EKRAN_OBRASCI, forma);
            obrasciOtvoreni = true;
        } else {
            gf.prikaziEkran(GlavnaForma.EKRAN_OBRASCI);
        }
    }

    private void otvoriStudent() {
        if (!studentiOtvoreni) {
            StudentForma forma = new StudentForma();
            new StudentController(forma);
            gf.registrujIPrikaziEkran(GlavnaForma.EKRAN_STUDENTI, forma);
            studentiOtvoreni = true;
        } else {
            gf.prikaziEkran(GlavnaForma.EKRAN_STUDENTI);
        }
    }

    private void otvoriZaposleni() {
        if (!zaposleniOtvoreni) {
            ZaposleniForma forma = new ZaposleniForma();
            new ZaposleniController(forma);
            gf.registrujIPrikaziEkran(GlavnaForma.EKRAN_ZAPOSLENI, forma);
            zaposleniOtvoreni = true;
        } else {
            gf.prikaziEkran(GlavnaForma.EKRAN_ZAPOSLENI);
        }
    }

    private void otvoriStudijskiProgram() {
        if (!studijskiProgramOtvoren) {
            StudijskiProgramForma forma = new StudijskiProgramForma();
            new StudijskiProgramController(forma);
            gf.registrujIPrikaziEkran(GlavnaForma.EKRAN_STUDIJSKI_PROGRAM, forma);
            studijskiProgramOtvoren = true;
        } else {
            gf.prikaziEkran(GlavnaForma.EKRAN_STUDIJSKI_PROGRAM);
        }
    }

    private void otvoriTerminDezurstva() {
        if (!terminiOtvoreni) {
            TerminDezurstvaForma forma = new TerminDezurstvaForma();
            new TerminDezurstvaController(forma);
            gf.registrujIPrikaziEkran(GlavnaForma.EKRAN_TERMINI, forma);
            terminiOtvoreni = true;
        } else {
            gf.prikaziEkran(GlavnaForma.EKRAN_TERMINI);
        }
    }

    private void otvoriTipPolja() {
        if (!tipoviPoljaOtvoreni) {
            TipPoljaForma forma = new TipPoljaForma();
            new TipPoljaController(forma);
            gf.registrujIPrikaziEkran(GlavnaForma.EKRAN_TIPOVI_POLJA, forma);
            tipoviPoljaOtvoreni = true;
        } else {
            gf.prikaziEkran(GlavnaForma.EKRAN_TIPOVI_POLJA);
        }
    }

    private void podesavanja() {
        JOptionPane.showMessageDialog(gf,
                "Trenutna adresa i port servera:\n\nlocalhost:9000",
                "Podešavanja softverskog sistema",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void odjava() {
        int opcija = JOptionPane.showConfirmDialog(gf,
                "Da li ste sigurni da želite da se odjavite?",
                "Potvrda odjave",
                JOptionPane.YES_NO_OPTION);

        if (opcija == JOptionPane.YES_OPTION) {
            gf.dispose();
            cordinator.Cordinator.getInstanca().odjava();
        }
    }

    private void oProgramu() {
        JOptionPane.showMessageDialog(gf,
                "ŠV-20 Sistem\n\n"
                + "Verzija: 1.1\n"
                + "Fakultet organizacionih nauka\n"
                + "Beograd, 2026.\n\n"
                + "Razvoj Java aplikacije za prepoznavanje i\n"
                + "ekstrakciju podataka sa ŠV-20 obrazaca",
                "O programu",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void otvoriFormu() {
        gf.setVisible(true);
    }

}
