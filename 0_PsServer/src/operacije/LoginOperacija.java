/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package operacije;

import controller.RegistarPrijavljenih;
import domen.ZaposleniFakulteta;
import java.util.List;

/**
 *
 * @author Vukasin Lukic
 */
public class LoginOperacija extends ApstraktnaGenerickaOperacija {

    private ZaposleniFakulteta zaposleni;

    public ZaposleniFakulteta getZaposleni() {
        return zaposleni;
    }

    @Override
    protected void preduslovi(Object objekat) throws Exception {
        if (objekat == null) {
            throw new Exception("Objekat ne sme biti null!");
        }
        if (!(objekat instanceof ZaposleniFakulteta)) {
            throw new Exception("Objekat mora biti tipa ZaposleniFakulteta!");
        }
    }

    @Override
    protected void izvrsiOperaciju(Object objekat, String kljuc) throws Exception {
        List<ZaposleniFakulteta> sviZaposleni = broker.getAll((ZaposleniFakulteta) objekat, null);
        System.out.println("KLASA login operacija SO " + sviZaposleni);

        ZaposleniFakulteta zf = (ZaposleniFakulteta) objekat;

        for (ZaposleniFakulteta z : sviZaposleni) {
            if (z.getKorisnickoIme().equals(zf.getKorisnickoIme()) && z.getSifra().equals(zf.getSifra())) {
                if (!RegistarPrijavljenih.prijavi(z.getKorisnickoIme())) {
                    throw new Exception("Korisnik je već prijavljen na sistem!");
                }

                zaposleni = z;
                return;
            }
        }
        //Ako nije pronadjen taj korisnik
        throw new Exception("Pogresno korisicko ime ili lozinka");
    }
}
