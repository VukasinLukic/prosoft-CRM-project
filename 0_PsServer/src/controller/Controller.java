/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import domen.ApstraktniDomenskiObjekat;
import domen.ZaposleniFakulteta;
import java.util.List;
import operacije.LoginOperacija;
import operacije.OdjaviZaposlenogOperacija;
import operacije.zaposleni.KreirajZaposlenogOperacija;
import operacije.zaposleni.ObrisiZaposlenogOperacija;
import operacije.zaposleni.PretraziZaposlenogOperacija;
import operacije.zaposleni.PromeniZaposlenogOperacija;
import operacije.zaposleni.VratiListuZaposlenihOperacija;
import operacije.zaposleni.VratiSveZaposleneOperacija;

/**
 *
 * @author Vukasin Lukic
 */
public class Controller {

    private static Controller instanca;

    private Controller() {
    }

    public static Controller getInstanca() {
        if (instanca == null) {
            instanca = new Controller();
        }
        return instanca;
    }

    //LOGIN 
    public ZaposleniFakulteta login(ZaposleniFakulteta z) throws Exception {
        LoginOperacija lo = new LoginOperacija();
        lo.izvrsi(z, null);
        return lo.getZaposleni();
    }

    public void odjava(String korisnickoIme) throws Exception {
        OdjaviZaposlenogOperacija op = new OdjaviZaposlenogOperacija();
        op.izvrsi(korisnickoIme, null);
    }

    //Zaposleni fakulteta fje
    public void kreirajZaposlenog(ZaposleniFakulteta z) throws Exception {
        KreirajZaposlenogOperacija op = new KreirajZaposlenogOperacija();
        op.izvrsi(z, null);
    }

    public void promeniZaposlenog(ZaposleniFakulteta z) throws Exception {
        PromeniZaposlenogOperacija op = new PromeniZaposlenogOperacija();
        op.izvrsi(z, null);
    }

    public void obrisiZaposlenog(ZaposleniFakulteta z) throws Exception {
        ObrisiZaposlenogOperacija op = new ObrisiZaposlenogOperacija();
        op.izvrsi(z, null);
    }

    public ZaposleniFakulteta pretraziZaposlenog(ZaposleniFakulteta z) throws Exception {
        PretraziZaposlenogOperacija op = new PretraziZaposlenogOperacija();
        op.izvrsi(z, null);
        return op.getRezultat();
    }

    public List<ApstraktniDomenskiObjekat> vratiSveZaposlene() throws Exception {
        VratiSveZaposleneOperacija op = new VratiSveZaposleneOperacija();
        op.izvrsi(null, null);
        return op.getLista();
    }

    public List<ApstraktniDomenskiObjekat> vratiListuZaposlenih(String kriterijum) throws Exception {
        VratiListuZaposlenihOperacija op = new VratiListuZaposlenihOperacija();
        op.izvrsi(null, kriterijum);
        return op.getLista();
    }

}
