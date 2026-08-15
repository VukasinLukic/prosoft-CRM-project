/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package operacije.student;

import domen.Student;
import operacije.ApstraktnaGenerickaOperacija;

/**
 *
 * @author Vukasin Lukic
 */
public class KreirajStudentaOperacija extends ApstraktnaGenerickaOperacija {

    @Override
    protected void preduslovi(Object objekat) throws Exception {
        if (objekat == null || !(objekat instanceof Student)) {
            throw new Exception("Objekat mora biti tipa Student!");
        }
        Student s = (Student) objekat;
        if (s.getIndeks() == null || !jeValidanIndeks(s.getIndeks())) {
            throw new Exception("Indeks mora biti u formatu gggg/bbbb!");
        }

        if (s.getJmbg() == null || !jeSamoCifreDuzine(s.getJmbg(), 13)) {
            throw new Exception("JMBG mora imati tacno 13 cifara!");
        }
        if (!kontrolnaCifraJmbgIspravna(s.getJmbg())) {
            throw new Exception("JMBG nije ispravan (pogresna kontrolna cifra)!");
        }
        if (s.getIme() == null || s.getIme().isEmpty()) {
            throw new Exception("Ime je obavezno!");
        }
        if (s.getPrezime() == null || s.getPrezime().isEmpty()) {
            throw new Exception("Prezime je obavezno!");
        }
        if (s.getStudijskiProgram() == null) {
            throw new Exception("Studijski program je obavezan!");
        }

        int upisnaGodina = Integer.parseInt(s.getIndeks().substring(0, 4));
        int godinaRodjenja = godinaRodjenjaIzJmbg(s.getJmbg());
        if (upisnaGodina - godinaRodjenja <= 16) {
            throw new Exception("Student mora biti stariji od 16 godina u trenutku upisa!");
        }
    }

    // // Izvor: OpenSource GitHub kod. - Java JMBG Validation Snippet
    private boolean kontrolnaCifraJmbgIspravna(String jmbg) {
        if (jmbg == null || jmbg.length() != 13) {
            return false;
        }

        int[] tezine = {7, 6, 5, 4, 3, 2, 7, 6, 5, 4, 3, 2};
        int suma = 0;

        for (int i = 0; i < 12; i++) {
            suma += Character.getNumericValue(jmbg.charAt(i)) * tezine[i];
        }

        int kontrolna = 11 - (suma % 11);
        if (kontrolna >= 10) {
            kontrolna = 0;
        }

        return kontrolna == Character.getNumericValue(jmbg.charAt(12));
    }

    // Pozicije 5-7 (GGG) kodiraju godinu rođenja; prag 800 razdvaja 1800-1999 od 2000-2799.
    private int godinaRodjenjaIzJmbg(String jmbg) {
        int ggg = Integer.parseInt(jmbg.substring(4, 7));
        return ggg < 800 ? 2000 + ggg : 1000 + ggg;
    }

    @Override
    protected void izvrsiOperaciju(Object objekat, String kljuc) throws Exception {
        broker.add((Student) objekat);
    }

    private boolean jeSamoCifreDuzine(String tekst, int ocekivanaDuzina) {
        if (tekst.length() != ocekivanaDuzina) {
            return false;
        }
        for (int i = 0; i < tekst.length(); i++) {
            if (!Character.isDigit(tekst.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    // Proverava format gggg/bbbb
    private boolean jeValidanIndeks(String indeks) {
        if (indeks.length() != 9) {
            return false;
        }
        if (indeks.charAt(4) != '/') {
            return false;
        }

        for (int i = 0; i < 4; i++) {
            if (!Character.isDigit(indeks.charAt(i))) {
                return false;
            }
        }

        for (int i = 5; i < 9; i++) {
            if (!Character.isDigit(indeks.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
