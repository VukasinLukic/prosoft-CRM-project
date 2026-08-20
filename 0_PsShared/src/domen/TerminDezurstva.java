/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package domen;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Vukasin Lukic
 */
public class TerminDezurstva implements ApstraktniDomenskiObjekat {

    private int idTerminDezurstva;
    private tipTermina tipTermina;
    private String kancelarija;

    public TerminDezurstva() {
    }

    public TerminDezurstva(int idTerminDezurstva, tipTermina tipTermina, String kancelarija) {
        this.idTerminDezurstva = idTerminDezurstva;
        this.tipTermina = tipTermina;
        this.kancelarija = kancelarija;
    }

    public int getIdTerminDezurstva() {
        return idTerminDezurstva;
    }

    public void setIdTerminDezurstva(int idTerminDezurstva) {
        this.idTerminDezurstva = idTerminDezurstva;
    }

    public tipTermina getTipTermina() {
        return tipTermina;
    }

    public void setTipTermina(tipTermina tipTermina) {
        this.tipTermina = tipTermina;
    }

    public String getKancelarija() {
        return kancelarija;
    }

    public void setKancelarija(String kancelarija) {
        this.kancelarija = kancelarija;
    }

    @Override
    public String toString() {
        return tipTermina + " - " + kancelarija;
    }

    @Override
    public String vratiNazivTabele() {
        return "termindezurstva";
    }

    @Override
    public List<ApstraktniDomenskiObjekat> vratiListu(ResultSet rs) throws Exception {
        List<ApstraktniDomenskiObjekat> lista = new ArrayList<>();
        while (rs.next()) {
            lista.add(vratiObjekatIzRS(rs));
        }
        return lista;
    }

    @Override
    public String vratiKoloneZaUbacivanje() {
        return "tipTermina, kancelarija";
    }

    @Override
    public String vratiVrednostiZaUbacivanje() {
        String tipStr = tipTermina.name().replace("_", " ");
        return "'" + tipStr + "', '" + kancelarija + "'";
        // replace jer tipTermana kao enum ima _ crtu umesto spacea
    }

    @Override
    public String vratiPrimarniKljuc() {
        return "termindezurstva.idTerminDezurstva = " + idTerminDezurstva;
    }

    @Override
    public ApstraktniDomenskiObjekat vratiObjekatIzRS(ResultSet rs) throws Exception {
        TerminDezurstva td = new TerminDezurstva();
        td.setIdTerminDezurstva(rs.getInt("idTerminDezurstva"));
        String tipStr = rs.getString("tipTermina").replace(" ", "_").toUpperCase();
        td.setTipTermina(tipTermina.valueOf(tipStr));
        //tipStr prebacen u format kao u enumu tip termina pa uradjen value of
        td.setKancelarija(rs.getString("kancelarija"));
        return td;
    }

    @Override
    public String vratiVrednostiZaIzmenu() {
        String tipStr = tipTermina.name().replace("_", " ");
        return "tipTermina='" + tipStr + "', kancelarija= '" + kancelarija + "'";
    }

}
