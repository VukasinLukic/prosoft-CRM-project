/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package operacije.stavke;

import domen.ApstraktniDomenskiObjekat;
import domen.StavkeObrasca;
import java.util.List;
import operacije.ApstraktnaGenerickaOperacija;

/**
 *
 * @author Vukasin Lukic
 */
public class KreirajStavkuObrascaOperacija extends ApstraktnaGenerickaOperacija {

    @Override
    protected void preduslovi(Object objekat) throws Exception {
        if (objekat == null || !(objekat instanceof StavkeObrasca)) {
            throw new Exception("Objekat mora biti tipa StavkeObrasca!");
        }
        StavkeObrasca s = (StavkeObrasca) objekat;
        if (s.getIdObrazac() == null) {
            throw new Exception("Obrazac je obavezan!");
        }
        if (s.getIdPolja() == null) {
            throw new Exception("Tip polja je obavezan!");
        }
    }

    @Override
    protected void izvrsiOperaciju(Object objekat, String kljuc) throws Exception {
        StavkeObrasca s = (StavkeObrasca) objekat;

        // idStavke se numerise po obrascu (idObrazac, idStavke) je slozen primarni kljuc,
        // idStavke NIJE auto_increment u bazi, pa se sledeca vrednost racuna rucno.
        String uslov = " WHERE idObrazac = " + s.getIdObrazac().getIdObrazac();
        List<ApstraktniDomenskiObjekat> postojece = broker.getAll(new StavkeObrasca(), uslov);
        int sledeciIdStavke = 1;
        for (ApstraktniDomenskiObjekat ado : postojece) {
            int idStavke = ((StavkeObrasca) ado).getIdStavke();
            if (idStavke >= sledeciIdStavke) {
                sledeciIdStavke = idStavke + 1;
            }
        }
        s.setIdStavke(sledeciIdStavke);

        broker.add(s);
    }
}
