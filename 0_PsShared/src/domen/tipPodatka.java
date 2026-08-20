/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package domen;

/**
 *
 * @author Vukasin Lukic
 */
public enum tipPodatka {
    TEXT, NUMERIC, ALPHANUMERIC, DATE, BOOLEAN;
        //obrati paznju da li se sa bazom uvek poklapa ...

    @Override
    public String toString() {
        switch (this) {
            case TEXT: return "Tekst";
            case NUMERIC: return "Broj";
            case ALPHANUMERIC: return "Alfanumerički";
            case DATE: return "Datum";
            case BOOLEAN: return "Da/Ne";
            default: return name();
        }
    }
}
