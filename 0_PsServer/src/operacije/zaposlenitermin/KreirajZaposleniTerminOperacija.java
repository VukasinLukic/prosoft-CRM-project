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
public class KreirajZaposleniTerminOperacija extends ApstraktnaGenerickaOperacija {

    @Override
    protected void preduslovi(Object objekat) throws Exception {
        if (objekat == null || !(objekat instanceof ZaposleniTermin)) {
            throw new Exception("Objekat mora biti tipa ZaposleniTermin!");
        }
        ZaposleniTermin zt = (ZaposleniTermin) objekat;
        if (zt.getIdZaposlenog() == null) {
            throw new Exception("Zaposleni je obavezan!");
        }
        if (zt.getIdTerminDezurstva() == null) {
            throw new Exception("Termin dezurstva je obavezan!");
        }
        if (zt.getDatum() == null) {
            throw new Exception("Datum je obavezan!");
        }
        if (zt.getBrojSati() <= 0) {
            throw new Exception("Broj sati mora biti > 0!");
        }
        if (zt.isVanredan() && zt.getBrojSati() > 4) {
            throw new Exception("Vanredno dezurstvo ne moze trajati vise od 4 sata!");
        }
    }

    @Override
    protected void izvrsiOperaciju(Object objekat, String kljuc) throws Exception {
        broker.add((ZaposleniTermin) objekat);
    }
}