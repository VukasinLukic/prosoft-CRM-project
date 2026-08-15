/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package operacije.studijskiprogram;

import domen.ApstraktniDomenskiObjekat;
import domen.StudijskiProgram;
import java.util.List;
import operacije.ApstraktnaGenerickaOperacija;

/**
 *
 * @author Vukasin Lukic
 */
public class VratiSveStudijskeProgrameOperacija extends ApstraktnaGenerickaOperacija {
    
    private List<ApstraktniDomenskiObjekat> lista;

    @Override
    protected void preduslovi(Object objekat) throws Exception {
    }

    @Override
    protected void izvrsiOperaciju(Object objekat, String kljuc) throws Exception {
        lista = broker.getAll(new StudijskiProgram(), null);
    }
    
    public List<ApstraktniDomenskiObjekat> getLista() {
        return lista;
    }
}