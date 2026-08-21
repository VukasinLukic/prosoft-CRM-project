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

/**
 *
 * @author Vukasin Lukic
 */
public class SV20Obrazac implements ApstraktniDomenskiObjekat {

    private int idObrazac;
    private Date datumUnosa;
    private int skolskaGodina;
    private int semestar;
    private Status status;
    private String putanjaDoFajla;
    private boolean ocrIzvrseno;
    private int brojUspesnihStavki;
    private int brojNeuspesnihStavki;
    private ZaposleniFakulteta idZaposlenog;
    private Student indeks;
    private List<StavkeObrasca> stavke;

    public SV20Obrazac() {
    }

    public SV20Obrazac(int idObrazac, Date datumUnosa, int skolskaGodina, int semestar, Status status, String putanjaDoFajla, boolean ocrIzvrseno, int brojUspesnihStavki, int brojNeuspesnihStavki, ZaposleniFakulteta idZaposlenog, Student indeks, List<StavkeObrasca> stavke) {
        this.idObrazac = idObrazac;
        this.datumUnosa = datumUnosa;
        this.skolskaGodina = skolskaGodina;
        this.semestar = semestar;
        this.status = status;
        this.putanjaDoFajla = putanjaDoFajla;
        this.ocrIzvrseno = ocrIzvrseno;
        this.brojUspesnihStavki = brojUspesnihStavki;
        this.brojNeuspesnihStavki = brojNeuspesnihStavki;
        this.idZaposlenog = idZaposlenog;
        this.indeks = indeks;
        this.stavke = stavke;
    }

    public int getIdObrazac() {
        return idObrazac;
    }

    public void setIdObrazac(int idObrazac) {
        this.idObrazac = idObrazac;
    }

    public Date getDatumUnosa() {
        return datumUnosa;
    }

    public void setDatumUnosa(Date datumUnosa) {
        this.datumUnosa = datumUnosa;
    }

    public int getSkolskaGodina() {
        return skolskaGodina;
    }

    public void setSkolskaGodina(int skolskaGodina) {
        this.skolskaGodina = skolskaGodina;
    }

    public int getSemestar() {
        return semestar;
    }

    public void setSemestar(int semestar) {
        this.semestar = semestar;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getPutanjaDoFajla() {
        return putanjaDoFajla;
    }

    public void setPutanjaDoFajla(String putanjaDoFajla) {
        this.putanjaDoFajla = putanjaDoFajla;
    }

    public boolean isOcrIzvrseno() {
        return ocrIzvrseno;
    }

    public void setOcrIzvrseno(boolean ocrIzvrseno) {
        this.ocrIzvrseno = ocrIzvrseno;
    }

    public int getBrojUspesnihStavki() {
        return brojUspesnihStavki;
    }

    public void setBrojUspesnihStavki(int brojUspesnihStavki) {
        this.brojUspesnihStavki = brojUspesnihStavki;
    }

    public int getBrojNeuspesnihStavki() {
        return brojNeuspesnihStavki;
    }

    public void setBrojNeuspesnihStavki(int brojNeuspesnihStavki) {
        this.brojNeuspesnihStavki = brojNeuspesnihStavki;
    }

    public ZaposleniFakulteta getIdZaposlenog() {
        return idZaposlenog;
    }

    public void setIdZaposlenog(ZaposleniFakulteta idZaposlenog) {
        this.idZaposlenog = idZaposlenog;
    }

    public Student getIndeks() {
        return indeks;
    }

    public void setIndeks(Student indeks) {
        this.indeks = indeks;
    }

    public List<StavkeObrasca> getStavke() {
        return stavke;
    }

    public void setStavke(List<StavkeObrasca> stavke) {
        this.stavke = stavke;
    }

    @Override
    public String vratiNazivTabele() {
        return "sv20obrazac";
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
        return "datumUnosa, skolskaGodina, semestar, status, putanjaFajla, ocrIzvrseno, brojUspesnihStavki, brojNeuspesnihStavki, idZaposlenog, indeks";
    }

    @Override
    public String vratiVrednostiZaUbacivanje() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return "'" + sdf.format(datumUnosa) + "', " + skolskaGodina + ", " + semestar + ", '"
                + status.name() + "', " + (putanjaDoFajla != null ? "'" + putanjaDoFajla + "'" : "NULL") + ", " + ocrIzvrseno + ", "
                + brojUspesnihStavki + ", " + brojNeuspesnihStavki + ", "
                + idZaposlenog.getIdZaposlenog() + ", '" + indeks.getIndeks() + "'";
    }

    @Override
    public String vratiPrimarniKljuc() {
        return "sv20obrazac.idObrazac = " + idObrazac;
    }

    @Override
    public ApstraktniDomenskiObjekat vratiObjekatIzRS(ResultSet rs) throws Exception {
        SV20Obrazac o = new SV20Obrazac();
        o.setIdObrazac(rs.getInt("idObrazac"));
        o.setDatumUnosa(rs.getDate("datumUnosa"));
        o.setSkolskaGodina(rs.getInt("skolskaGodina"));
        o.setSemestar(rs.getInt("semestar"));
        o.setStatus(Status.valueOf(rs.getString("status").toUpperCase()));
        o.setPutanjaDoFajla(rs.getString("putanjaFajla"));
        o.setOcrIzvrseno(rs.getBoolean("ocrIzvrseno"));
        o.setBrojUspesnihStavki(rs.getInt("brojUspesnihStavki"));
        o.setBrojNeuspesnihStavki(rs.getInt("brojNeuspesnihStavki"));

        ZaposleniFakulteta z = new ZaposleniFakulteta();
        z.setIdZaposlenog(rs.getInt("idZaposlenog"));
        o.setIdZaposlenog(z);

        Student s = new Student();
        s.setIndeks(rs.getString("indeks"));
        o.setIndeks(s);

        return o;
    }

    @Override
    public String vratiVrednostiZaIzmenu() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return "datumUnosa = '" + sdf.format(datumUnosa) + "', skolskaGodina = " + skolskaGodina
                + ", semestar = " + semestar + ", status = '" + status.name() + "', "
                + "putanjaFajla = " + (putanjaDoFajla != null ? "'" + putanjaDoFajla + "'" : "NULL") + ", ocrIzvrseno = " + ocrIzvrseno
                + ", brojUspesnihStavki = " + brojUspesnihStavki + ", brojNeuspesnihStavki = " + brojNeuspesnihStavki
                + ", idZaposlenog = " + idZaposlenog.getIdZaposlenog() + ", indeks = '" + indeks.getIndeks() + "'";
    }

}
