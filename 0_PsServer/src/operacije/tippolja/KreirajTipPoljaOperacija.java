/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package operacije.tippolja;

import domen.TipPolja;
import operacije.ApstraktnaGenerickaOperacija;

/**
 *
 * @author Vukasin Lukic
 */
public class KreirajTipPoljaOperacija extends ApstraktnaGenerickaOperacija {

    @Override
    protected void preduslovi(Object objekat) throws Exception {
        if (objekat == null || !(objekat instanceof TipPolja)) {
            throw new Exception("Objekat mora biti tipa TipPolja!");
        }
        TipPolja tp = (TipPolja) objekat;
        if (tp.getNazivPolja() == null || tp.getNazivPolja().isEmpty()) {
            throw new Exception("Naziv polja je obavezan!");
        }
        if (tp.getTipPodatka() == null) {
            throw new Exception("Tip podatka je obavezan!");
        }
        if (tp.getStranica() < 1) {
            throw new Exception("Stranica mora biti >= 1!");
        }
        if (tp.getTipPodatka() == domen.tipPodatka.NUMERIC) {
            if (tp.getRegexValidacija() == null || !tp.getRegexValidacija().matches("^[0-9]+$")) {
                throw new Exception("Za NUMERIC tip podatka, regex validacija mora sadrzati samo cifre!");
            }
        }
    }

    @Override
    protected void izvrsiOperaciju(Object objekat, String kljuc) throws Exception {
        broker.add((TipPolja) objekat);
    }
}
