/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package operacije.student;

import domen.Student;
import java.sql.SQLException;
import operacije.ApstraktnaGenerickaOperacija;

/**
 *
 * @author Vukasin Lukic
 */
public class ObrisiStudentaOperacija extends ApstraktnaGenerickaOperacija {

    @Override
    protected void preduslovi(Object objekat) throws Exception {
        if (objekat == null || !(objekat instanceof Student)) {
            throw new Exception("Objekat mora biti tipa Student!");
        }
    }

    @Override
    protected void izvrsiOperaciju(Object objekat, String kljuc) throws Exception {
        try {
            broker.delete((Student) objekat);
        } catch (SQLException ex) {
            throw new Exception("Student se ne moze obrisati jer ima povezane SV-20 obrasce.");
        }
    }
}
