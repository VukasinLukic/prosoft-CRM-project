/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package operacije.obrazac;

import domen.ApstraktniDomenskiObjekat;
import domen.SV20Obrazac;
import java.util.List;
import operacije.ApstraktnaGenerickaOperacija;

/**
 *
 * @author Vukasin Lukic
 */
public class VratiListuSV20ObrazacaOperacija extends ApstraktnaGenerickaOperacija {
    
    private List<ApstraktniDomenskiObjekat> lista;

    @Override
    protected void preduslovi(Object objekat) throws Exception {
    }

    @Override
    protected void izvrsiOperaciju(Object objekat, String kljuc) throws Exception {
        lista = broker.getAll(new SV20Obrazac(), kljuc);
    }
    
    public List<ApstraktniDomenskiObjekat> getLista() {
        return lista;
    }
}
