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
    private int pozicijaX;
    private int pozicijaY;
    private int sirina;
    private int visina;
    private int stranica;
    private int redosledObrade;
    private boolean podrzavaOCR;
    private boolean obaveznoPolje;

    public TipPolja() {
    }

    public TipPolja(int idPolja, String nazivPolja, tipPodatka tipPodatka, String regexValidacija, int pozicijaX, int pozicijaY, int sirina, int visina, int stranica, int redosledObrade, boolean podrzavaOCR, boolean obaveznoPolje) {
        this.idPolja = idPolja;
        this.nazivPolja = nazivPolja;
        this.tipPodatka = tipPodatka;
        this.regexValidacija = regexValidacija;
        this.pozicijaX = pozicijaX;
        this.pozicijaY = pozicijaY;
        this.sirina = sirina;
        this.visina = visina;
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

    public int getPozicijaX() {
        return pozicijaX;
    }

    public void setPozicijaX(int pozicijaX) {
        this.pozicijaX = pozicijaX;
    }

    public int getPozicijaY() {
        return pozicijaY;
    }

    public void setPozicijaY(int pozicijaY) {
        this.pozicijaY = pozicijaY;
    }

    public int getSirina() {
        return sirina;
    }

    public void setSirina(int sirina) {
        this.sirina = sirina;
    }

    public int getVisina() {
        return visina;
    }

    public void setVisina(int visina) {
        this.visina = visina;
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
        // Mora da bude konzistentan sa poljima koja equals() koristi (pozicija/dimenzije/OCR/regex/tip),
        // inace se krsi hashCode/equals ugovor.
        int hash = 7;
        hash = 31 * hash + this.pozicijaX;
        hash = 31 * hash + this.pozicijaY;
        hash = 31 * hash + this.sirina;
        hash = 31 * hash + this.visina;
        hash = 31 * hash + Boolean.hashCode(this.podrzavaOCR);
        hash = 31 * hash + Boolean.hashCode(this.obaveznoPolje);
        hash = 31 * hash + Objects.hashCode(this.regexValidacija);
        hash = 31 * hash + Objects.hashCode(this.tipPodatka);
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
        if (this.pozicijaX != other.pozicijaX) {
            return false;
        }
        if (this.pozicijaY != other.pozicijaY) {
            return false;
        }
        if (this.sirina != other.sirina) {
            return false;
        }
        if (this.visina != other.visina) {
            return false;
        }
        if (this.podrzavaOCR != other.podrzavaOCR) {
            return false;
        }
        if (this.obaveznoPolje != other.obaveznoPolje) {
            return false;
        }
        if (!Objects.equals(this.regexValidacija, other.regexValidacija)) {
            return false;
        }
        return this.tipPodatka == other.tipPodatka;
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
        return "nazivPolja, tipPodatka, regexValidacija, pozicijaX, pozicijaY, sirina, visina, stranica, redosledObrade, podrzavaOCR, obaveznoPolje";
    }

    @Override
    public String vratiVrednostiZaUbacivanje() {
        // pozicijaX/pozicijaY/sirina/visina su primitivni int i ne mogu direktno da nose NULL,
        // ali baza (CHECK chk_tp_pozicije) zahteva da ova polja budu NULL kada podrzavaOCR=false.
        // Zato se ovde eksplicitno salje NULL kad OCR nije podrzan, bez obzira na trenutnu int vrednost.
        String pozicije = podrzavaOCR
                ? pozicijaX + ", " + pozicijaY + ", " + sirina + ", " + visina
                : "NULL, NULL, NULL, NULL";
        return "'" + nazivPolja + "', '" + tipPodatka.name() + "', "
                + (regexValidacija != null ? "'" + regexValidacija + "'" : "NULL") + ", "
                + pozicije + ", "
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
        tp.setPozicijaX(rs.getInt("pozicijaX"));
        tp.setPozicijaY(rs.getInt("pozicijaY"));
        tp.setSirina(rs.getInt("sirina"));
        tp.setVisina(rs.getInt("visina"));
        tp.setStranica(rs.getInt("stranica"));
        tp.setRedosledObrade(rs.getInt("redosledObrade"));
        tp.setPodrzavaOCR(rs.getBoolean("podrzavaOCR"));
        tp.setObaveznoPolje(rs.getBoolean("obaveznoPolje"));
        return tp;
    }

    @Override
    public String vratiVrednostiZaIzmenu() {
        String pozicije = podrzavaOCR
                ? "pozicijaX = " + pozicijaX + ", pozicijaY = " + pozicijaY + ", sirina = " + sirina + ", visina = " + visina
                : "pozicijaX = NULL, pozicijaY = NULL, sirina = NULL, visina = NULL";
        return "nazivPolja = '" + nazivPolja + "', tipPodatka = '" + tipPodatka.name() + "', "
                + "regexValidacija = " + (regexValidacija != null ? "'" + regexValidacija + "'" : "NULL") + ", "
                + pozicije + ", stranica = " + stranica + ", "
                + "redosledObrade = " + redosledObrade + ", podrzavaOCR = " + podrzavaOCR + ", obaveznoPolje = " + obaveznoPolje;
    }

}