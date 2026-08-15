/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package operacije.stavke;

import domen.ApstraktniDomenskiObjekat;
import domen.StavkeObrasca;
import java.util.List;
import operacije.ApstraktnaGenerickaOperacija;

/**
 *
 * @author Vukasin Lukic
 */
public class PretraziStavkeObrascaOperacija extends ApstraktnaGenerickaOperacija {
    
    private List<ApstraktniDomenskiObjekat> lista;

    @Override
    protected void preduslovi(Object objekat) throws Exception {
        if (objekat == null || !(objekat instanceof StavkeObrasca)) {
            throw new Exception("Objekat mora biti tipa StavkeObrasca!");
        }
    }

    @Override
    protected void izvrsiOperaciju(Object objekat, String kljuc) throws Exception {
        StavkeObrasca s = (StavkeObrasca) objekat;
        String uslov = " WHERE idObrazac = " + s.getIdObrazac().getIdObrazac();
        
        lista = broker.getAll(new StavkeObrasca(), uslov);
    }
    
    public List<ApstraktniDomenskiObjekat> getLista() {
        return lista;
    }
}