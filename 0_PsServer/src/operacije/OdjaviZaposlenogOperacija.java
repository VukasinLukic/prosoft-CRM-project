/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package operacije;

import controller.RegistarPrijavljenih;

/**
 *
 * @author Vukasin Lukic
 */
public class OdjaviZaposlenogOperacija extends ApstraktnaGenerickaOperacija {

    @Override
    protected void preduslovi(Object objekat) throws Exception {
        if (objekat == null || !(objekat instanceof String)) {
            throw new Exception("Korisničko ime je obavezno!");
        }
    }

    @Override
    protected void izvrsiOperaciju(Object objekat, String kljuc) throws Exception {
        RegistarPrijavljenih.odjavi((String) objekat);
    }

}
