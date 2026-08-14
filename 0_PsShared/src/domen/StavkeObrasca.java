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
public class StavkeObrasca implements ApstraktniDomenskiObjekat {

    private int idStavke;
    private SV20Obrazac idObrazac;
    private String ocrVrednost;
    private String korigovanaVrednost;
    private double nivoPodudarnosti;
    private boolean ocrUspesno;
    private TipPolja idPolja;

    public StavkeObrasca() {
    }

    public StavkeObrasca(int idStavke, SV20Obrazac idObrazac, String ocrVrednost, String korigovanaVrednost, double nivoPodudarnosti, boolean ocrUspesno, TipPolja idPolja) {
        this.idStavke = idStavke;
        this.idObrazac = idObrazac;
        this.ocrVrednost = ocrVrednost;
        this.korigovanaVrednost = korigovanaVrednost;
        this.nivoPodudarnosti = nivoPodudarnosti;
        this.ocrUspesno = ocrUspesno;
        this.idPolja = idPolja;
    }

    public TipPolja getIdPolja() {
        return idPolja;
    }

    public void setIdPolja(TipPolja idPolja) {
        this.idPolja = idPolja;
    }

    public int getIdStavke() {
        return idStavke;
    }

    public void setIdStavke(int idStavke) {
        this.idStavke = idStavke;
    }

    public SV20Obrazac getIdObrazac() {
        return idObrazac;
    }

    public void setIdObrazac(SV20Obrazac idObrazac) {
        this.idObrazac = idObrazac;
    }

    public String getOcrVrednost() {
        return ocrVrednost;
    }

    public void setOcrVrednost(String ocrVrednost) {
        this.ocrVrednost = ocrVrednost;
    }

    public String getKorigovanaVrednost() {
        return korigovanaVrednost;
    }

    public void setKorigovanaVrednost(String korigovanaVrednost) {
        this.korigovanaVrednost = korigovanaVrednost;
    }

    public double getNivoPodudarnosti() {
        return nivoPodudarnosti;
    }

    public void setNivoPodudarnosti(double nivoPodudarnosti) {
        this.nivoPodudarnosti = nivoPodudarnosti;
    }

    public boolean isOcrUspesno() {
        return ocrUspesno;
    }

    public void setOcrUspesno(boolean ocrUspesno) {
        this.ocrUspesno = ocrUspesno;
    }

    @Override
    public String toString() {
        return "StavkeObrasca{" + "idStavke=" + idStavke + ", idObrazac=" + idObrazac + '}';
    }

    @Override
    public String vratiNazivTabele() {
        return "stavkeobrasca";
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
        return "idObrazac, ocrVrednost, korigovanaVrednost, nivoPodudarnosti, ocrUspesno, idPolja";
    }

    @Override
    public String vratiVrednostiZaUbacivanje() {
        return idObrazac.getIdObrazac() + ", "
                + (ocrVrednost != null ? "'" + ocrVrednost + "'" : "NULL") + ", "
                + (korigovanaVrednost != null ? "'" + korigovanaVrednost + "'" : "NULL") + ", "
                + nivoPodudarnosti + ", " + ocrUspesno + ", " + idPolja.getIdPolja();
    }

    @Override
    public String vratiPrimarniKljuc() {
        return "stavkeobrasca.idStavke = " + idStavke;
    }

    @Override
    public ApstraktniDomenskiObjekat vratiObjekatIzRS(ResultSet rs) throws Exception {
        StavkeObrasca so = new StavkeObrasca();
        so.setIdStavke(rs.getInt("idStavke"));
        so.setOcrVrednost(rs.getString("ocrVrednost"));
        so.setKorigovanaVrednost(rs.getString("korigovanaVrednost"));
        so.setNivoPodudarnosti(rs.getDouble("nivoPodudarnosti"));
        so.setOcrUspesno(rs.getBoolean("ocrUspesno"));

        SV20Obrazac o = new SV20Obrazac();
        o.setIdObrazac(rs.getInt("idObrazac"));
        so.setIdObrazac(o);

        TipPolja tp = new TipPolja();
        tp.setIdPolja(rs.getInt("idPolja"));
        so.setIdPolja(tp);

        return so;
    }

    @Override
    public String vratiVrednostiZaIzmenu() {
        return "idObrazac = " + idObrazac.getIdObrazac() + ", "
                + "ocrVrednost = " + (ocrVrednost != null ? "'" + ocrVrednost + "'" : "NULL") + ", "
                + "korigovanaVrednost = " + (korigovanaVrednost != null ? "'" + korigovanaVrednost + "'" : "NULL") + ", "
                + "nivoPodudarnosti = " + nivoPodudarnosti + ", ocrUspesno = " + ocrUspesno + ", "
                + "idPolja = " + idPolja.getIdPolja();
    }

}
