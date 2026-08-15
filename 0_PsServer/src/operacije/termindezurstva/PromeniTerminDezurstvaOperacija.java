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
public class PromeniTerminDezurstvaOperacija extends ApstraktnaGenerickaOperacija {

    @Override
    protected void preduslovi(Object objekat) throws Exception {
        if (objekat == null || !(objekat instanceof TerminDezurstva)) {
            throw new Exception("Objekat mora biti tipa TerminDezurstva!");
        }
        TerminDezurstva td = (TerminDezurstva) objekat;
        if (td.getIdTerminDezurstva() <= 0) {
            throw new Exception("ID termina dezurstva nije validan!");
        }
    }

    @Override
    protected void izvrsiOperaciju(Object objekat, String kljuc) throws Exception {
        broker.edit((TerminDezurstva) objekat);
    }
}
