/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package domen;

import java.io.Serializable;
import java.util.List;
import java.sql.*;

/**
 *
 * @author Vukasin Lukic
 */
public interface ApstraktniDomenskiObjekat extends Serializable {

    //Interfejs koje domenske klase implementiraju!
    public String vratiNazivTabele();

    public List<ApstraktniDomenskiObjekat> vratiListu(ResultSet rs) throws Exception;

    public String vratiKoloneZaUbacivanje();

    public String vratiVrednostiZaUbacivanje();

    public String vratiPrimarniKljuc();

    public ApstraktniDomenskiObjekat vratiObjekatIzRS(ResultSet rs) throws Exception;

    public String vratiVrednostiZaIzmenu();
}
