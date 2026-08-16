/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kontroleri;

import domen.ZaposleniFakulteta;
import forme.LoginForma;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import komunikacija.Komunikacija;

/**
 *
 * @author Vukasin Lukic
 */
public class LoginController {

    private final LoginForma lf;

    public LoginController(LoginForma lf) {
        this.lf = lf;
        addActionListeners();
    }

    private void addActionListeners() {
        lf.loginAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prijava();
            }
        });
    }

    private void prijava() {
        String username = lf.getTxtUsername().getText().trim();
        String password = String.valueOf(lf.getTxtPassword().getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(lf,
                    "Unesite korisničko ime i lozinku!",
                    "Greška",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // zabrana da korisnik klikne dva puta dugme slucajno...
        lf.getBtnUlogujSe().setEnabled(false);
        try {
            // povezivanje na serv
            Komunikacija.getInstanca().konekcija();

            ZaposleniFakulteta ulogovaniZaposleni = Komunikacija.getInstanca().login(username, password);

            if (ulogovaniZaposleni != null) {
                JOptionPane.showMessageDialog(lf,
                        "Prijava na sistem uspešna!\nDobrodošli, " + ulogovaniZaposleni.getIme() + " " + ulogovaniZaposleni.getPrezime(),
                        "Uspeh",
                        JOptionPane.INFORMATION_MESSAGE);
                lf.dispose();

                // Otvori glavnu formu
                cordinator.Cordinator.getInstanca().otvoriGlavnuFormu(ulogovaniZaposleni);
            } else {
                JOptionPane.showMessageDialog(lf,
                        "Pogrešno korisničko ime ili lozinka!",
                        "Greška",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(lf,
                    "Greška: " + ex.getMessage(),
                    "Greška",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            lf.getBtnUlogujSe().setEnabled(true);
        }
    }

    public void otvoriFormu() {
        lf.setVisible(true);
    }

}
