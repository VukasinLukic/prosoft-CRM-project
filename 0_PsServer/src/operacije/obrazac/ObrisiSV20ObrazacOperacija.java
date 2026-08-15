/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package operacije.obrazac;

import domen.ApstraktniDomenskiObjekat;
import domen.SV20Obrazac;
import domen.StavkeObrasca;
import java.util.List;
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
        
        // Prvo obrisi sve stavke obrasca
        String uslovStavke = " WHERE idObrazac = " + o.getIdObrazac();
        List<ApstraktniDomenskiObjekat> stavke = broker.getAll(new StavkeObrasca(), uslovStavke);
        
        for (ApstraktniDomenskiObjekat ado : stavke) {
            broker.delete((StavkeObrasca) ado);
        }
        
        // Zatim obrisi obrazac
        broker.delete(o);
    }
}
