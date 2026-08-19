/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package operacije.obrazac;

import domen.SV20Obrazac;
import java.sql.SQLException;
import operacije.ApstraktnaGenerickaOperacija;

/**
 *
 * @author Vukasin Lukic
 */
public class ObrisiSV20ObrazacOperacija extends ApstraktnaGenerickaOperacija {

    @Override
    protected void preduslovi(Object objekat) throws Exception {
        if (objekat == null || !(objekat instanceof SV20Obrazac)) {
            throw new Exception("Objekat mora biti tipa SV20Obrazac!");
        }
        SV20Obrazac o = (SV20Obrazac) objekat;
        if (o.getIdObrazac() <= 0) {
            throw new Exception("ID obrasca nije validan!");
        }
    }

    @Override
    protected void izvrsiOperaciju(Object objekat, String kljuc) throws Exception {
        SV20Obrazac o = (SV20Obrazac) objekat;

        // Namerno se NE brisu stavke obrasca rucno pre ovoga: fk_st_obrazac je definisan kao
        // ON DELETE RESTRICT (dokumentovano u IMPLEMENTACIONI_PLAN.md), pa baza sama sprecava
        // brisanje obrasca koji jos uvek ima stavke - to ne treba tiho zaobilaziti u aplikaciji.
        try {
            broker.delete(o);
        } catch (SQLException ex) {
            throw new Exception("Obrazac se ne moze obrisati jer ima povezane stavke obrasca. Prvo obrisite/prebacite stavke.");
        }
    }
}
