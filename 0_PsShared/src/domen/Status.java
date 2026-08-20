/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package domen;

/**
 *
 * @author Vukasin Lukic
 */
public enum Status {
    PODNET, U_OBRADI, VRACEN_NA_KOREKCIJU, ODOBREN, ODBIJEN;
        //obrati paznju da li se sa bazom uvek poklapa ...

    @Override
    public String toString() {
        switch (this) {
            case PODNET: return "Podnet";
            case U_OBRADI: return "U obradi";
            case VRACEN_NA_KOREKCIJU: return "Vraćen na korekciju";
            case ODOBREN: return "Odobren";
            case ODBIJEN: return "Odbijen";
            default: return name();
        }
    }
}
