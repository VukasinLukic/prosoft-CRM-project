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
public class PromeniStudentaOperacija extends ApstraktnaGenerickaOperacija{
    @Override
    protected void preduslovi(Object objekat) throws Exception {
        if (objekat == null || !(objekat instanceof Student)) {
            throw new Exception("Objekat mora biti tipa Student!");
        }
        Student s = (Student) objekat;
        if (s.getIndeks() == null || s.getIndeks().isEmpty()) {
            throw new Exception("Indeks je obavezan!");
        }
    }

    @Override
    protected void izvrsiOperaciju(Object objekat, String kljuc) throws Exception {
        broker.edit((Student) objekat);
    }
}
