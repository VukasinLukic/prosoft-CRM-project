/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package operacije.studijskiprogram;

import domen.StudijskiProgram;
import java.sql.SQLException;
import operacije.ApstraktnaGenerickaOperacija;

/**
 *
 * @author Vukasin Lukic
 */
public class ObrisiStudijskiProgramOperacija extends ApstraktnaGenerickaOperacija {

    @Override
    protected void preduslovi(Object objekat) throws Exception {
        if (objekat == null || !(objekat instanceof StudijskiProgram)) {
            throw new Exception("Objekat mora biti tipa StudijskiProgram!");
        }
    }

    @Override
    protected void izvrsiOperaciju(Object objekat, String kljuc) throws Exception {
        try {
            broker.delete((StudijskiProgram) objekat);
        } catch (SQLException ex) {
            throw new Exception("Studijski program se ne moze obrisati jer postoje povezani studenti.");
        }
    }
}
