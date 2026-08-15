/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package operacije.obrazac;

import domen.ApstraktniDomenskiObjekat;
import domen.SV20Obrazac;
import domen.StavkeObrasca;
import java.util.ArrayList;
import java.util.List;
import operacije.ApstraktnaGenerickaOperacija;

/**
 *
 * @author Vukasin Lukic
 */
public class PretraziSV20ObrazacOperacija extends ApstraktnaGenerickaOperacija {
    
    private SV20Obrazac rezultat;

    @Override
    protected void preduslovi(Object objekat) throws Exception {
        if (objekat == null || !(objekat instanceof SV20Obrazac)) {
            throw new Exception("Objekat mora biti tipa SV20Obrazac!");
        }
    }

    @Override
    protected void izvrsiOperaciju(Object objekat, String kljuc) throws Exception {
        SV20Obrazac o = (SV20Obrazac) objekat;
        String uslov = " WHERE idObrazac = " + o.getIdObrazac();
        
        // Dohvati obrazac
        List<ApstraktniDomenskiObjekat> lista = broker.getAll(new SV20Obrazac(), uslov);
        
        if (lista.isEmpty()) {
            throw new Exception("SV20 obrazac nije pronadjen!");
        }
        rezultat = (SV20Obrazac) lista.get(0);
        
        // Dohvati stavke obrasca
        String uslovStavke = " WHERE idObrazac = " + rezultat.getIdObrazac();
        List<ApstraktniDomenskiObjekat> stavkeLista = broker.getAll(new StavkeObrasca(), uslovStavke);
        
        List<StavkeObrasca> stavke = new ArrayList<>();
        for (ApstraktniDomenskiObjekat ado : stavkeLista) {
            stavke.add((StavkeObrasca) ado);
        }
        rezultat.setStavke(stavke);
    }
    
    public SV20Obrazac getRezultat() {
        return rezultat;
    }
}
