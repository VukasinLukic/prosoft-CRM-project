/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package operacije.studijskiprogram;

import domen.StudijskiProgram;
import operacije.ApstraktnaGenerickaOperacija;

/**
 *
 * @author Vukasin Lukic
 */
public class KreirajStudijskiProgramOperacija extends ApstraktnaGenerickaOperacija {

    @Override
    protected void preduslovi(Object objekat) throws Exception {
        if (objekat == null || !(objekat instanceof StudijskiProgram)) {
            throw new Exception("Objekat mora biti tipa StudijskiProgram!");
        }
        StudijskiProgram sp = (StudijskiProgram) objekat;
        if (sp.getNaziv() == null || sp.getNaziv().isEmpty()) {
            throw new Exception("Naziv je obavezan!");
        }
        if (sp.getOznaka() == null || sp.getOznaka().isEmpty()) {
            throw new Exception("Oznaka je obavezna!");
        }
        if (sp.getOznaka().length() > 10) {
            throw new Exception("Oznaka ne sme biti duza od 10 karaktera!");
        }
        if (sp.getOznaka().length() > sp.getNaziv().length() / 2) {
            throw new Exception("Oznaka ne sme biti duza od polovine duzine naziva!");
        }
        if (sp.getStepenStudija() == null) {
            throw new Exception("Stepen studija je obavezan!");
        }
    }

    @Override
    protected void izvrsiOperaciju(Object objekat, String kljuc) throws Exception {
        broker.add((StudijskiProgram) objekat);
    }
}
