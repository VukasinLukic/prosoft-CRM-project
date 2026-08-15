/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package operacije.stavke;

import domen.StavkeObrasca;
import operacije.ApstraktnaGenerickaOperacija;

/**
 *
 * @author Vukasin Lukic
 */
public class KreirajStavkuObrascaOperacija extends ApstraktnaGenerickaOperacija {

    @Override
    protected void preduslovi(Object objekat) throws Exception {
        if (objekat == null || !(objekat instanceof StavkeObrasca)) {
            throw new Exception("Objekat mora biti tipa StavkeObrasca!");
        }
        StavkeObrasca s = (StavkeObrasca) objekat;
        if (s.getIdObrazac() == null) {
            throw new Exception("Obrazac je obavezan!");
        }
        if (s.getIdPolja() == null) {
            throw new Exception("Tip polja je obavezan!");
        }
    }

    @Override
    protected void izvrsiOperaciju(Object objekat, String kljuc) throws Exception {
        broker.add((StavkeObrasca) objekat);
    }
}
