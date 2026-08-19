/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package operacije.zaposleni;

import domen.ZaposleniFakulteta;
import java.sql.SQLException;
import operacije.ApstraktnaGenerickaOperacija;

/**
 *
 * @author Vukasin Lukic
 */
public class ObrisiZaposlenogOperacija extends ApstraktnaGenerickaOperacija{

     @Override
    protected void preduslovi(Object objekat) throws Exception {
        if (objekat == null || !(objekat instanceof ZaposleniFakulteta)) {
            throw new Exception("Objekat mora biti tipa ZaposleniFakulteta!");
        }
        ZaposleniFakulteta z = (ZaposleniFakulteta) objekat;
        if (z.getIdZaposlenog() <= 0) {
            throw new Exception("ID zaposlenog nije validan!");
        }
    }

   @Override
    protected void izvrsiOperaciju(Object objekat, String kljuc) throws Exception {
        try {
            broker.delete((ZaposleniFakulteta) objekat);
        } catch (SQLException ex) {
            throw new Exception("Zaposleni se ne moze obrisati jer ima povezane obrasce ili termine dezurstva.");
        }
    }
    
}
