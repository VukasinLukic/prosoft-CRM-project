/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cordinator;

import domen.ZaposleniFakulteta;
import forme.GlavnaForma;
import forme.LoginForma;
import kontroleri.GlavnaFormaController;
import kontroleri.LoginController;

/**
 *
 * @author Vukasin Lukic
 */
public class Cordinator {

    private static Cordinator instanca;
    private LoginController loginController;
    private GlavnaFormaController glavnaFormaController;
    private ZaposleniFakulteta ulogovaniKorisnik;

    public Cordinator() {
    }

    public static Cordinator getInstanca() {
        if (instanca == null) {
            instanca = new Cordinator();
        }
        return instanca;
    }

    public void otvoriLoginFormu() {
        loginController = new LoginController(new LoginForma());
        loginController.otvoriFormu();
    }

    public void otvoriGlavnuFormu(ZaposleniFakulteta ulogovani) {
        this.ulogovaniKorisnik = ulogovani;
        GlavnaForma gf = new GlavnaForma(ulogovani);
        glavnaFormaController = new GlavnaFormaController(gf);
        glavnaFormaController.otvoriFormu();
    }

    //ove metode se nalaze u kordinatoru da bi iz bilo kog kontrolera mogle da se pozovu.
    //npr koji korisnik je uradio nesto ili bilo kad da se odjavi . 
    
    public void odjava() {
        if (ulogovaniKorisnik != null) {
            try {
                komunikacija.Komunikacija.getInstanca().odjaviZaposlenog(ulogovaniKorisnik.getKorisnickoIme());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        ulogovaniKorisnik = null;
        komunikacija.Komunikacija.getInstanca().zatvoriKonekciju();
        otvoriLoginFormu();
    }

    public ZaposleniFakulteta getUlogovaniKorisnik() {
        return ulogovaniKorisnik;
    }
}
