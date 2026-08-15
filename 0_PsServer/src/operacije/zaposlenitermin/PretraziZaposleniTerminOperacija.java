/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package operacije.zaposlenitermin;

import domen.ApstraktniDomenskiObjekat;
import domen.ZaposleniTermin;
import java.text.SimpleDateFormat;
import java.util.List;
import operacije.ApstraktnaGenerickaOperacija;

/**
 *
 * @author Vukasin Lukic
 */
public class PretraziZaposleniTerminOperacija extends ApstraktnaGenerickaOperacija {
    
    private ZaposleniTermin rezultat;

    @Override
    protected void preduslovi(Object objekat) throws Exception {
        if (objekat == null || !(objekat instanceof ZaposleniTermin)) {
            throw new Exception("Objekat mora biti tipa ZaposleniTermin!");
        }
    }

    @Override
    protected void izvrsiOperaciju(Object objekat, String kljuc) throws Exception {
        ZaposleniTermin zt = (ZaposleniTermin) objekat;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        String uslov = " WHERE idZaposlenog = " + zt.getIdZaposlenog().getIdZaposlenog()
                     + " AND idTerminDezurstva = " + zt.getIdTerminDezurstva().getIdTerminDezurstva()
                     + " AND datum = '" + sdf.format(zt.getDatum()) + "'";
        
        List<ApstraktniDomenskiObjekat> lista = broker.getAll(new ZaposleniTermin(), uslov);
        
        if (lista.isEmpty()) {
            throw new Exception("Zaposleni termin nije pronadjen!");
        }
        rezultat = (ZaposleniTermin) lista.get(0);
    }
    
    public ZaposleniTermin getRezultat() {
        return rezultat;
    }
}
