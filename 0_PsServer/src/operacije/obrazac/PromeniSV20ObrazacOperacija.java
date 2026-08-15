/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package operacije.obrazac;

import domen.SV20Obrazac;
import domen.StavkeObrasca;
import java.util.List;
import operacije.ApstraktnaGenerickaOperacija;

/**
 *
 * @author Vukasin Lukic
 */
public class PromeniSV20ObrazacOperacija extends ApstraktnaGenerickaOperacija {

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
        
        // Promeni obrazac
        broker.edit(o);
        
        // Promeni stavke ako postoje
        List<StavkeObrasca> stavke = o.getStavke();
        if (stavke != null && !stavke.isEmpty()) {
            for (StavkeObrasca stavka : stavke) {
                if (stavka.getIdStavke() > 0) {
                    broker.edit(stavka);
                } else {
                    stavka.setIdObrazac(o);
                    broker.add(stavka);
                }
            }
        }
    }
}
