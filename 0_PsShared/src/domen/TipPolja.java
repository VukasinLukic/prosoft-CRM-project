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
public class TipPolja implements ApstraktniDomenskiObjekat {

    private int idPolja;
    private String nazivPolja;
    private tipPodatka tipPodatka;
    private String regexValidacija;
    private int stranica;
    private int redosledObrade;
    private boolean podrzavaOCR;
    private boolean obaveznoPolje;

    public TipPolja() {
    }

    public TipPolja(int idPolja, String nazivPolja, tipPodatka tipPodatka, String regexValidacija, int stranica, int redosledObrade, boolean podrzavaOCR, boolean obaveznoPolje) {
        this.idPolja = idPolja;
        this.nazivPolja = nazivPolja;
        this.tipPodatka = tipPodatka;
        this.regexValidacija = regexValidacija;
        this.stranica = stranica;
        this.redosledObrade = redosledObrade;
        this.podrzavaOCR = podrzavaOCR;
        this.obaveznoPolje = obaveznoPolje;
    }

    public boolean isObaveznoPolje() {
        return obaveznoPolje;
    }

    public void setObaveznoPolje(boolean obaveznoPolje) {
        this.obaveznoPolje = obaveznoPolje;
    }

    public int getIdPolja() {
        return idPolja;
    }

    public void setIdPolja(int idPolja) {
        this.idPolja = idPolja;
    }

    public String getNazivPolja() {
        return nazivPolja;
    }

    public void setNazivPolja(String nazivPolja) {
        this.nazivPolja = nazivPolja;
    }

    public tipPodatka getTipPodatka() {
        return tipPodatka;
    }

    public void setTipPodatka(tipPodatka tipPodatka) {
        this.tipPodatka = tipPodatka;
    }

    public String getRegexValidacija() {
        return regexValidacija;
    }

    public void setRegexValidacija(String regexValidacija) {
        this.regexValidacija = regexValidacija;
    }

    public int getStranica() {
        return stranica;
    }

    public void setStranica(int stranica) {
        this.stranica = stranica;
    }

    public int getRedosledObrade() {
        return redosledObrade;
    }

    public void setRedosledObrade(int redosledObrade) {
        this.redosledObrade = redosledObrade;
    }

    public boolean isPodrzavaOCR() {
        return podrzavaOCR;
    }

    public void setPodrzavaOCR(boolean podrzavaOCR) {
        this.podrzavaOCR = podrzavaOCR;
    }

    @Override
    public String toString() {
        return nazivPolja;
    }

    @Override
    public int hashCode() {
        // Poslovni ključ je nazivPolja — jedino polje koje OCR uparivanje stvarno koristi
        // (vidi SV20ObrazacController.pokreniOcr()) i koje mora biti stabilno po zapisu.
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.nazivPolja);
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
        final TipPolja other = (TipPolja) obj;
        return Objects.equals(this.nazivPolja, other.nazivPolja);
    }

    @Override
    public String vratiNazivTabele() {
        return "tippolja";
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
        return "nazivPolja, tipPodatka, regexValidacija, stranica, redosledObrade, podrzavaOCR, obaveznoPolje";
    }

    @Override
    public String vratiVrednostiZaUbacivanje() {
        return "'" + nazivPolja + "', '" + tipPodatka.name() + "', "
                + (regexValidacija != null ? "'" + regexValidacija + "'" : "NULL") + ", "
                + stranica + ", " + redosledObrade + ", " + podrzavaOCR + ", " + obaveznoPolje;
    }

    @Override
    public String vratiPrimarniKljuc() {
        return "tippolja.idPolja = " + idPolja;
    }

    @Override
    public ApstraktniDomenskiObjekat vratiObjekatIzRS(ResultSet rs) throws Exception {
        TipPolja tp = new TipPolja();
        tp.setIdPolja(rs.getInt("idPolja"));
        tp.setNazivPolja(rs.getString("nazivPolja"));
        tp.setTipPodatka(tipPodatka.valueOf(rs.getString("tipPodatka")));
        tp.setRegexValidacija(rs.getString("regexValidacija"));
        tp.setStranica(rs.getInt("stranica"));
        tp.setRedosledObrade(rs.getInt("redosledObrade"));
        tp.setPodrzavaOCR(rs.getBoolean("podrzavaOCR"));
        tp.setObaveznoPolje(rs.getBoolean("obaveznoPolje"));
        return tp;
    }

    @Override
    public String vratiVrednostiZaIzmenu() {
        return "nazivPolja = '" + nazivPolja + "', tipPodatka = '" + tipPodatka.name() + "', "
                + "regexValidacija = " + (regexValidacija != null ? "'" + regexValidacija + "'" : "NULL") + ", "
                + "stranica = " + stranica + ", "
                + "redosledObrade = " + redosledObrade + ", podrzavaOCR = " + podrzavaOCR + ", obaveznoPolje = " + obaveznoPolje;
    }

}
