/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package domen;

/**
 *
 * @author Vukasin Lukic
 */
public enum tipTermina {
    PRVA_SMENA, DRUGA_SMENA, TRECA_SMENA;
    //obrati paznju da li se sa bazom uvek poklapa ...

    @Override
    public String toString() {
        switch (this) {
            case PRVA_SMENA: return "Prva smena";
            case DRUGA_SMENA: return "Druga smena";
            case TRECA_SMENA: return "Treća smena";
            default: return name();
        }
    }
}
