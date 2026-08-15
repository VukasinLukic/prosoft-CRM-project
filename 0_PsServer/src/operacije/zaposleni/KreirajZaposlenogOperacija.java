/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package operacije.zaposleni;

import domen.ZaposleniFakulteta;
import operacije.ApstraktnaGenerickaOperacija;

/**
 *
 * @author Vukasin Lukic
 */
public class KreirajZaposlenogOperacija extends ApstraktnaGenerickaOperacija {

    @Override
    protected void preduslovi(Object objekat) throws Exception {
        if (objekat == null || !(objekat instanceof ZaposleniFakulteta)) {
            throw new Exception("Objekat mora biti tipa ZaposleniFakulteta!");
        }
        ZaposleniFakulteta z = (ZaposleniFakulteta) objekat;
        if (z.getIme() == null || z.getIme().isEmpty()) {
            throw new Exception("Ime je obavezno!");
        }
        if (z.getPrezime() == null || z.getPrezime().isEmpty()) {
            throw new Exception("Prezime je obavezno!");
        }
        if (z.getKorisnickoIme() == null || z.getKorisnickoIme().length() < 5) {
            throw new Exception("Korisnicko ime mora imati minimum 5 karaktera!");
        }
        if (z.getSifra() == null || z.getSifra().length() < 8) {
            throw new Exception("Sifra mora imati minimum 8 karaktera!");
        }
        if (z.getEmail() == null || !z.getEmail().contains("@")) {
            throw new Exception("Email nije validan!");
        }
    }

    @Override
    protected void izvrsiOperaciju(Object objekat, String kljuc) throws Exception {
        broker.add((ZaposleniFakulteta) objekat);
    }

}
