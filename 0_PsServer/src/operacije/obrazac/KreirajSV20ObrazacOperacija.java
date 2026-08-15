/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package operacije.obrazac;

import domen.SV20Obrazac;
import domen.StavkeObrasca;
import java.util.List;
import operacije.ApstraktnaGenerickaOperacija;

/**
 *
 * @author Vukasin Lukic
 */
public class KreirajSV20ObrazacOperacija extends ApstraktnaGenerickaOperacija {

    @Override
    protected void preduslovi(Object objekat) throws Exception {
        if (objekat == null || !(objekat instanceof SV20Obrazac)) {
            throw new Exception("Objekat mora biti tipa SV20Obrazac!");
        }
        SV20Obrazac o = (SV20Obrazac) objekat;
        if (o.getIndeks() == null) {
            throw new Exception("Student je obavezan!");
        }
        if (o.getIdZaposlenog() == null) {
            throw new Exception("Zaposleni je obavezan!");
        }
        if (o.getDatumUnosa() == null) {
            throw new Exception("Datum unosa je obavezan!");
        }
        if (o.getSkolskaGodina() <= 0) {
            throw new Exception("Skolska godina nije validna!");
        }
        if (o.getSemestar() <= 0) {
            throw new Exception("Semestar nije validan!");
        }
        if (o.getStatus() == null) {
            throw new Exception("Status je obavezan!");
        }
        if (o.getPutanjaDoFajla() == null || o.getPutanjaDoFajla().isEmpty()) {
            throw new Exception("Putanja do fajla je obavezna!");
        }
    }

    @Override
    protected void izvrsiOperaciju(Object objekat, String kljuc) throws Exception {
        SV20Obrazac o = (SV20Obrazac) objekat;
        
        // Prvo kreiraj obrazac
        broker.add(o);
        
        // Zatim kreiraj stavke ako postoje
        List<StavkeObrasca> stavke = o.getStavke();
        if (stavke != null && !stavke.isEmpty()) {
            for (StavkeObrasca stavka : stavke) {
                stavka.setIdObrazac(o);
                broker.add(stavka);
            }
        }
    }
}
