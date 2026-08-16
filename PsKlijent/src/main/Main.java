/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

/**
 *
 * @author Vukasin Lukic
 */
public class Main {

    public static void main(String[] args) {
        //pozivanje teme za formu.
        try {
            com.formdev.flatlaf.FlatLightLaf.setup();
        } catch (Exception ex) {
            System.err.println("Greska: " + ex.getMessage());
        }
        
        
        cordinator.Cordinator.getInstanca().otvoriLoginFormu();
    }
}
