/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package domen;

/**
 *
 * @author Vukasin Lukic
 */
public enum stepenStudija {
    OAS, MAS, DAS;
        //obrati paznju da li se sa bazom uvek poklapa ...

    @Override
    public String toString() {
        switch (this) {
            case OAS: return "Osnovne akademske studije";
            case MAS: return "Master akademske studije";
            case DAS: return "Doktorske akademske studije";
            default: return name();
        }
    }
}
