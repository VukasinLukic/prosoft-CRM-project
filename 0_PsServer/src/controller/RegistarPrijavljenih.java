/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;


import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author Vukasin Lukic
 */

public class RegistarPrijavljenih {

    private static final List<String> prijavljeniKorisnici = new ArrayList<>();

    public static synchronized boolean prijavi(String korisnickoIme) {
        if (prijavljeniKorisnici.contains(korisnickoIme)) {
            return false;
        }
        prijavljeniKorisnici.add(korisnickoIme);
        return true;
    }

    public static synchronized void odjavi(String korisnickoIme) {
        prijavljeniKorisnici.remove(korisnickoIme);
    }

    public static synchronized boolean jePrijavljen(String korisnickoIme) {
        return prijavljeniKorisnici.contains(korisnickoIme);
    }
}
