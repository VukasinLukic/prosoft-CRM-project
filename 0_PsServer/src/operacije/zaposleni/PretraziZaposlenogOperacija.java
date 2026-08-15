/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package operacije.zaposleni;

import domen.ApstraktniDomenskiObjekat;
import domen.ZaposleniFakulteta;
import java.util.List;
import operacije.ApstraktnaGenerickaOperacija;

/**
 *
 * @author Vukasin Lukic
 */
public class PretraziZaposlenogOperacija extends ApstraktnaGenerickaOperacija {

    private ZaposleniFakulteta rezultat;

    @Override
    protected void preduslovi(Object objekat) throws Exception {
        if (objekat == null || !(objekat instanceof ZaposleniFakulteta)) {
            throw new Exception("Objekat mora biti tipa ZaposleniFakulteta!");
        }
    }

    @Override
    protected void izvrsiOperaciju(Object objekat, String kljuc) throws Exception {
        ZaposleniFakulteta z = (ZaposleniFakulteta) objekat;
        String uslov = " WHERE idZaposlenog = " + z.getIdZaposlenog();

        List<ApstraktniDomenskiObjekat> lista = broker.getAll(new ZaposleniFakulteta(), uslov);

        if (lista.isEmpty()) {
            throw new Exception("Zaposleni nije pronadjen!");
        }
        rezultat = (ZaposleniFakulteta) lista.get(0);
    }

    public ZaposleniFakulteta getRezultat() {
        return rezultat;
    }
}
