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

    public GlavnaFormaController(GlavnaForma gf) {
        this.gf = gf;
        addActionListeners();
    }

    private void addActionListeners() {

        gf.addSV20ObrazacListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                otvoriSV20Obrazac();
            }
        });

        gf.addStudentListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                otvoriStudent();
            }
        });

        gf.addZaposleniListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                otvoriZaposleni();
            }
        });

        gf.addStudijskiProgramListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                otvoriStudijskiProgram();
            }
        });

        gf.addTerminDezurstvaListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                otvoriTerminDezurstva();
            }
        });

        gf.addTipPoljaListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                otvoriTipPolja();
            }
        });

        gf.addPodesavanjaListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                podesavanja();
            }
        });

        gf.addOdjavaListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                odjava();
            }
        });

        gf.addOProgramuListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                oProgramu();
            }
        });

        gf.addBtnObrasciListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                otvoriSV20Obrazac();
            }
        });

        gf.addBtnStudentiListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                otvoriStudent();
            }
        });

        gf.addBtnZaposleniListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                otvoriZaposleni();
            }
        });

        gf.addBtnSifarniciListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                otvoriStudijskiProgram();
            }
        });
    }

    private void otvoriSV20Obrazac() {
        SV20ObrazacForma forma = new SV20ObrazacForma(gf);
        new SV20ObrazacController(forma);
        forma.setVisible(true);
    }

    private void otvoriStudent() {
        StudentForma forma = new StudentForma(gf);
        new StudentController(forma);
        forma.setVisible(true);
    }

    private void otvoriZaposleni() {
        ZaposleniForma forma = new ZaposleniForma(gf);
        new ZaposleniController(forma);
        forma.setVisible(true);
    }

    private void otvoriStudijskiProgram() {
        StudijskiProgramForma forma = new StudijskiProgramForma(gf);
        new StudijskiProgramController(forma);
        forma.setVisible(true);
    }

    private void otvoriTerminDezurstva() {
        TerminDezurstvaForma forma = new TerminDezurstvaForma(gf);
        new TerminDezurstvaController(forma);
        forma.setVisible(true);
    }

    private void otvoriTipPolja() {
        TipPoljaForma forma = new TipPoljaForma(gf);
        new TipPoljaController(forma);
        forma.setVisible(true);
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
