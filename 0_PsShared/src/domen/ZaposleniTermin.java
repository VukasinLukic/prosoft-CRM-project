/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package domen;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Vukasin Lukic
 */
public class ZaposleniTermin implements ApstraktniDomenskiObjekat {

    private ZaposleniFakulteta idZaposlenog;
    private TerminDezurstva idTerminDezurstva;
    private Date datum;
    private int brojSati;
    private boolean vanredan;

    public ZaposleniTermin() {
    }

    public ZaposleniTermin(ZaposleniFakulteta idZaposlenog, TerminDezurstva idTerminDezurstva, Date datum, int brojSati, boolean vanredan) {
        this.idZaposlenog = idZaposlenog;
        this.idTerminDezurstva = idTerminDezurstva;
        this.datum = datum;
        this.brojSati = brojSati;
        this.vanredan = vanredan;
    }

    public ZaposleniFakulteta getIdZaposlenog() {
        return idZaposlenog;
    }

    public void setIdZaposlenog(ZaposleniFakulteta idZaposlenog) {
        this.idZaposlenog = idZaposlenog;
    }

    public TerminDezurstva getIdTerminDezurstva() {
        return idTerminDezurstva;
    }

    public void setIdTerminDezurstva(TerminDezurstva idTerminDezurstva) {
        this.idTerminDezurstva = idTerminDezurstva;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public int getBrojSati() {
        return brojSati;
    }

    public void setBrojSati(int brojSati) {
        this.brojSati = brojSati;
    }

    public boolean isVanredan() {
        return vanredan;
    }

    public void setVanredan(boolean vanredan) {
        this.vanredan = vanredan;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        return (idZaposlenog != null ? idZaposlenog.getIme() + " " + idZaposlenog.getPrezime() : "N/A")
                + " - " + (datum != null ? sdf.format(datum) : "N/A");
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.idZaposlenog);
        hash = 31 * hash + Objects.hashCode(this.idTerminDezurstva);
        hash = 31 * hash + Objects.hashCode(this.datum);
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
        final ZaposleniTermin other = (ZaposleniTermin) obj;
        if (!Objects.equals(this.idZaposlenog, other.idZaposlenog)) {
            return false;
        }
        if (!Objects.equals(this.idTerminDezurstva, other.idTerminDezurstva)) {
            return false;
        }
        return Objects.equals(this.datum, other.datum);
    }

    @Override
    public String vratiNazivTabele() {
        return "ZaposleniTermin";
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
        return "idZaposlenog, idTerminDezurstva, datum, brojSati, vanredan";
    }

    @Override
    public String vratiVrednostiZaUbacivanje() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return idZaposlenog.getIdZaposlenog() + ", "
                + idTerminDezurstva.getIdTerminDezurstva() + ", '"
                + sdf.format(datum) + "', " + brojSati + ", " + vanredan;
    }

    @Override
    public String vratiPrimarniKljuc() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return "zaposleniTermin.idZaposlenog = " + idZaposlenog.getIdZaposlenog()
                + " AND zaposleniTermin.idTerminDezurstva = " + idTerminDezurstva.getIdTerminDezurstva()
                + " AND zaposleniTermin.datum = '" + sdf.format(datum) + "'";
    }

    @Override
    public ApstraktniDomenskiObjekat vratiObjekatIzRS(ResultSet rs) throws Exception {
        ZaposleniTermin zt = new ZaposleniTermin();

        ZaposleniFakulteta z = new ZaposleniFakulteta();
        z.setIdZaposlenog(rs.getInt("idZaposlenog"));
        zt.setIdZaposlenog(z);

        TerminDezurstva td = new TerminDezurstva();
        td.setIdTerminDezurstva(rs.getInt("idTerminDezurstva"));
        zt.setIdTerminDezurstva(td);

        zt.setDatum(rs.getDate("datum"));
        zt.setBrojSati(rs.getInt("brojSati"));
        zt.setVanredan(rs.getBoolean("vanredan"));

        return zt;
    }

    @Override
    public String vratiVrednostiZaIzmenu() {
        return "brojSati = " + brojSati + ", vanredan = " + vanredan;
    }
}
