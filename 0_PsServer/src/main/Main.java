/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import forme.ServerskaForma;

public class Main {

    public static void main(String[] args) {
        try {
            // Postavljanje  FlatLaf teme
            com.formdev.flatlaf.FlatDarkLaf.setup();

        } catch (Exception ex) {
            System.err.println("Greska pri inicijalizaciji FlatLaf teme: " + ex.getMessage());
        }

        java.awt.EventQueue.invokeLater(() -> {
            ServerskaForma sf = new ServerskaForma();
            sf.setLocationRelativeTo(null);
            sf.setVisible(true);
        });
    }
}
