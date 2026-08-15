/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package operacije.student;

import domen.ApstraktniDomenskiObjekat;
import domen.Student;
import java.util.List;
import operacije.ApstraktnaGenerickaOperacija;

/**
 *
 * @author Vukasin Lukic
 */
public class PretraziStudentaOperacija extends ApstraktnaGenerickaOperacija {
    
    private Student rezultat;

    @Override
    protected void preduslovi(Object objekat) throws Exception {
        if (objekat == null || !(objekat instanceof Student)) {
            throw new Exception("Objekat mora biti tipa Student!");
        }
    }

    @Override
    protected void izvrsiOperaciju(Object objekat, String kljuc) throws Exception {
        Student s = (Student) objekat;
        String uslov = " WHERE indeks = '" + s.getIndeks() + "'";
        
        List<ApstraktniDomenskiObjekat> lista = broker.getAll(new Student(), uslov);
        
        if (lista.isEmpty()) {
            throw new Exception("Student nije pronadjen!");
        }
        rezultat = (Student) lista.get(0);
    }
    
    public Student getRezultat() {
        return rezultat;
    }
}
