/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package operacije.zaposlenitermin;

import domen.ZaposleniTermin;
import operacije.ApstraktnaGenerickaOperacija;

/**
 *
 * @author Vukasin Lukic
 */
public class ObrisiZaposleniTerminOperacija extends ApstraktnaGenerickaOperacija {

    @Override
    protected void preduslovi(Object objekat) throws Exception {
        if (objekat == null || !(objekat instanceof ZaposleniTermin)) {
            throw new Exception("Objekat mora biti tipa ZaposleniTermin!");
        }
        ZaposleniTermin zt = (ZaposleniTermin) objekat;
        if (zt.getIdZaposlenog() == null || zt.getIdTerminDezurstva() == null || zt.getDatum() == null) {
            throw new Exception("Primarni kljuc nije kompletan!");
        }
    }

    @Override
    protected void izvrsiOperaciju(Object objekat, String kljuc) throws Exception {
        broker.delete((ZaposleniTermin) objekat);
    }
}
