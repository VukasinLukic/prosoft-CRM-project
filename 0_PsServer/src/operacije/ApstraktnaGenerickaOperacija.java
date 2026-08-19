/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package operacije;

import domen.ApstraktniDomenskiObjekat;
import repository.db.DbRepository;
import repository.db.DbRepositoryGeneric;

/**
 *
 * @author Vukasin Lukic
 */
public abstract class ApstraktnaGenerickaOperacija {

    protected final repository.Repository<ApstraktniDomenskiObjekat> broker;

    public ApstraktnaGenerickaOperacija() {
        this.broker = new DbRepositoryGeneric();
    }

    public final void izvrsi(Object objekat, String kljuc) throws Exception {
        try {
            preduslovi(objekat);
            zapocniTransakciju();
            izvrsiOperaciju(objekat, kljuc);
            potvrdiTransakciju();
        } catch (Exception e) {
            ponistiTransakciju();
            throw e;
        } finally {
            ugasiKonekciju();
        }

    }

    protected abstract void preduslovi(Object objekat) throws Exception;

    private void zapocniTransakciju() throws Exception {
        ((DbRepository<ApstraktniDomenskiObjekat>) broker).connect();
    }

    protected abstract void izvrsiOperaciju(Object objekat, String kljuc) throws Exception;

    private void ponistiTransakciju() throws Exception {
        ((DbRepository<ApstraktniDomenskiObjekat>) broker).rollback();
    }

    private void ugasiKonekciju() throws Exception {
        ((DbRepository<ApstraktniDomenskiObjekat>) broker).disconnect();
    }

    private void potvrdiTransakciju() throws Exception {
        ((DbRepository<ApstraktniDomenskiObjekat>) broker).commit();
    }

}
