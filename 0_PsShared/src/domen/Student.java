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
public class Student implements ApstraktniDomenskiObjekat {

    private String indeks;
    private String jmbg;
    private String ime;
    private String prezime;
    private String mestoRodjenja;
    private String adresaStanovanja;
    private StudijskiProgram studijskiProgram;  //obratiti paznju na ovo u dokumentaciji pise da idStudProgram ipak bolje je uraditi ovako . 

    public Student() {
    }

    public Student(String indeks, String jmbg, String ime, String prezime, String mestoRodjenja, String adresaStanovanja, StudijskiProgram studijskiProgram) {
        this.indeks = indeks;
        this.jmbg = jmbg;
        this.ime = ime;
        this.prezime = prezime;
        this.mestoRodjenja = mestoRodjenja;
        this.adresaStanovanja = adresaStanovanja;
        this.studijskiProgram = studijskiProgram;
    }

    public String getIndeks() {
        return indeks;
    }

    public void setIndeks(String indeks) {
        this.indeks = indeks;
    }

    public String getJmbg() {
        return jmbg;
    }

    public void setJmbg(String jmbg) {
        this.jmbg = jmbg;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getMestoRodjenja() {
        return mestoRodjenja;
    }

    public void setMestoRodjenja(String mestoRodjenja) {
        this.mestoRodjenja = mestoRodjenja;
    }

    public String getAdresaStanovanja() {
        return adresaStanovanja;
    }

    public void setAdresaStanovanja(String adresaStanovanja) {
        this.adresaStanovanja = adresaStanovanja;
    }

    public StudijskiProgram getStudijskiProgram() {
        return studijskiProgram;
    }

    public void setStudijskiProgram(StudijskiProgram studijskiProgram) {
        this.studijskiProgram = studijskiProgram;
    }

    @Override
    public String toString() {
        return "Student{" + "ime=" + ime + ", prezime=" + prezime + '}';
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.jmbg);
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
        final Student other = (Student) obj;
        return Objects.equals(this.jmbg, other.jmbg);
    }

    @Override
    public String vratiNazivTabele() {
        return "student";
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
        return "indeks, jmbg, ime, prezime, mestoRodjenja, adresaStanovanja, idStudProgram";
    }

    @Override
    public String vratiVrednostiZaUbacivanje() {
        return "'" + indeks + "', '" + jmbg + "', '" + ime + "', '" + prezime + "', '" + mestoRodjenja + "', '" + adresaStanovanja + "', " + studijskiProgram.getIdStudProgram();
    }

    @Override
    public String vratiPrimarniKljuc() {
        return "student.indeks= '" + indeks + "'";
    }

    @Override
    public ApstraktniDomenskiObjekat vratiObjekatIzRS(ResultSet rs) throws Exception {
        Student s = new Student();
        s.setIndeks(rs.getString("indeks"));
        s.setJmbg(rs.getString("jmbg"));
        s.setIme(rs.getString("ime"));
        s.setPrezime(rs.getString("prezime"));
        s.setMestoRodjenja(rs.getString("mestoRodjenja"));
        s.setAdresaStanovanja(rs.getString("adresaStanovanja"));
        StudijskiProgram sp = new StudijskiProgram();
        sp.setIdStudProgram(rs.getInt("idStudProgram"));
        s.setStudijskiProgram(sp);
        return s;

    }

    @Override
    public String vratiVrednostiZaIzmenu() {
        return "jmbg = '" + jmbg + "', ime = '" + ime + "', prezime = '" + prezime + "', "
                + "mestoRodjenja = '" + mestoRodjenja + "', adresaStanovanja = '" + adresaStanovanja + "', "
                + "idStudProgram = " + studijskiProgram.getIdStudProgram();
    }

}
