/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package domen;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Vukasin Lukic
 */
public class StudijskiProgram implements ApstraktniDomenskiObjekat {

    private int idStudProgram;
    private String naziv;
    private String oznaka;
    private stepenStudija stepenStudija;

    public StudijskiProgram() {
    }

    public StudijskiProgram(int idStudProgram, String naziv, String oznaka, stepenStudija stepenStudija) {
        this.idStudProgram = idStudProgram;
        this.naziv = naziv;
        this.oznaka = oznaka;
        this.stepenStudija = stepenStudija;
    }

    public stepenStudija getStepenStudija() {
        return stepenStudija;
    }

    public void setStepenStudija(stepenStudija stepenStudija) {
        this.stepenStudija = stepenStudija;
    }

    public int getIdStudProgram() {
        return idStudProgram;
    }

    public void setIdStudProgram(int idStudProgram) {
        this.idStudProgram = idStudProgram;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getOznaka() {
        return oznaka;
    }

    public void setOznaka(String oznaka) {
        this.oznaka = oznaka;
    }

    @Override
    public String toString() {
        return "StudijskiProgram{" + "naziv=" + naziv + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.naziv);
        hash = 31 * hash + Objects.hashCode(this.oznaka);
        hash = 31 * hash + Objects.hashCode(this.stepenStudija);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final StudijskiProgram other = (StudijskiProgram) obj;
        if (!Objects.equals(this.naziv, other.naziv)) {
            return false;
        }
        if (!Objects.equals(this.oznaka, other.oznaka)) {
            return false;
        }
        return this.stepenStudija == other.stepenStudija;
    }

    @Override
    public String vratiNazivTabele() {
        return "studijskiprogram";
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
        return "naziv, oznaka, stepenStudija";
    }

    @Override
    public String vratiVrednostiZaUbacivanje() {
        return "'" + naziv + "', '" + oznaka + "', '" + stepenStudija.name() + "'";
    }
    //koristimo name fju da bi mozda uradili to string opis za veliko ime stepena studija videcemo .. 

    @Override
    public String vratiPrimarniKljuc() {
        return "studijskiprogram.idStudProgram=" + idStudProgram;
    }

    @Override
    public ApstraktniDomenskiObjekat vratiObjekatIzRS(ResultSet rs) throws Exception {
        StudijskiProgram sp = new StudijskiProgram();
        sp.setIdStudProgram(rs.getInt("idStudProgram"));
        sp.setNaziv(rs.getString("naziv"));
        sp.setOznaka(rs.getString("oznaka"));
        sp.setStepenStudija(stepenStudija.valueOf(rs.getString("stepenStudija")));
        return sp;
    }

    @Override
    public String vratiVrednostiZaIzmenu() {
        return "naziv = '" + naziv + "', oznaka='" + oznaka + "', stepenStudija='" + stepenStudija.name() + "'";
    }
}
