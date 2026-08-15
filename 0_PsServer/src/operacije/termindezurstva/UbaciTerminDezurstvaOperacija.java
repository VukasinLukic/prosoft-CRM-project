/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package operacije.termindezurstva;

import domen.TerminDezurstva;
import operacije.ApstraktnaGenerickaOperacija;

/**
 *
 * @author Vukasin Lukic
 */
public class UbaciTerminDezurstvaOperacija extends ApstraktnaGenerickaOperacija {

    @Override
    protected void preduslovi(Object objekat) throws Exception {
        if (objekat == null || !(objekat instanceof TerminDezurstva)) {
            throw new Exception("Objekat mora biti tipa TerminDezurstva!");
        }
        TerminDezurstva td = (TerminDezurstva) objekat;
        if (td.getTipTermina() == null) {
            throw new Exception("Tip termina je obavezan!");
        }
        if (td.getKancelarija() == null || td.getKancelarija().isEmpty()) {
            throw new Exception("Kancelarija je obavezna!");
        }
    }

    @Override
    protected void izvrsiOperaciju(Object objekat, String kljuc) throws Exception {
        broker.add((TerminDezurstva) objekat);
    }
}
