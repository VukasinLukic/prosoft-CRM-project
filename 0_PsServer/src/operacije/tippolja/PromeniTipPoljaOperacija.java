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
public class PromeniTipPoljaOperacija extends ApstraktnaGenerickaOperacija {

    @Override
    protected void preduslovi(Object objekat) throws Exception {
        if (objekat == null || !(objekat instanceof TipPolja)) {
            throw new Exception("Objekat mora biti tipa TipPolja!");
        }
        TipPolja tp = (TipPolja) objekat;
        if (tp.getIdPolja() <= 0) {
            throw new Exception("ID tipa polja nije validan!");
        }
    }

    @Override
    protected void izvrsiOperaciju(Object objekat, String kljuc) throws Exception {
        broker.edit((TipPolja) objekat);
    }
}
