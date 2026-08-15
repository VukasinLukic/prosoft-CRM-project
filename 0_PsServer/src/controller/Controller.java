/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import domen.ApstraktniDomenskiObjekat;
import domen.SV20Obrazac;
import domen.StavkeObrasca;
import domen.Student;
import domen.StudijskiProgram;
import domen.TerminDezurstva;
import domen.TipPolja;
import domen.ZaposleniFakulteta;
import domen.ZaposleniTermin;
import java.util.List;
import operacije.LoginOperacija;
import operacije.OdjaviZaposlenogOperacija;
import operacije.obrazac.KreirajSV20ObrazacOperacija;
import operacije.obrazac.ObrisiSV20ObrazacOperacija;
import operacije.obrazac.PretraziSV20ObrazacOperacija;
import operacije.obrazac.PromeniSV20ObrazacOperacija;
import operacije.obrazac.VratiListuSV20ObrazacaOperacija;
import operacije.obrazac.VratiSveSV20ObrasceOperacija;
import operacije.stavke.KreirajStavkuObrascaOperacija;
import operacije.stavke.PretraziStavkeObrascaOperacija;
import operacije.stavke.PromeniStavkuObrascaOperacija;
import operacije.stavke.VratiStavkeObrascaOperacija;
import operacije.student.KreirajStudentaOperacija;
import operacije.student.ObrisiStudentaOperacija;
import operacije.student.PretraziStudentaOperacija;
import operacije.student.PromeniStudentaOperacija;
import operacije.student.VratiListuStudenataOperacija;
import operacije.student.VratiSveStudenteOperacija;
import operacije.studijskiprogram.KreirajStudijskiProgramOperacija;
import operacije.studijskiprogram.ObrisiStudijskiProgramOperacija;
import operacije.studijskiprogram.PromeniStudijskiProgramOperacija;
import operacije.studijskiprogram.VratiListuStudijskihProgramaOperacija;
import operacije.studijskiprogram.VratiSveStudijskeProgrameOperacija;
import operacije.termindezurstva.ObrisiTerminDezurstvaOperacija;
import operacije.termindezurstva.PromeniTerminDezurstvaOperacija;
import operacije.termindezurstva.UbaciTerminDezurstvaOperacija;
import operacije.termindezurstva.VratiListuTerminaDezurstvaOperacija;
import operacije.termindezurstva.VratiSveTermineDezurstvaOperacija;
import operacije.tippolja.KreirajTipPoljaOperacija;
import operacije.tippolja.ObrisiTipPoljaOperacija;
import operacije.tippolja.PromeniTipPoljaOperacija;
import operacije.tippolja.VratiListuTipovaPoljaOperacija;
import operacije.tippolja.VratiSveTipovePoljaOperacija;
import operacije.zaposleni.KreirajZaposlenogOperacija;
import operacije.zaposleni.ObrisiZaposlenogOperacija;
import operacije.zaposleni.PretraziZaposlenogOperacija;
import operacije.zaposleni.PromeniZaposlenogOperacija;
import operacije.zaposleni.VratiListuZaposlenihOperacija;
import operacije.zaposleni.VratiSveZaposleneOperacija;
import operacije.zaposlenitermin.KreirajZaposleniTerminOperacija;
import operacije.zaposlenitermin.ObrisiZaposleniTerminOperacija;
import operacije.zaposlenitermin.PretraziZaposleniTerminOperacija;
import operacije.zaposlenitermin.PromeniZaposleniTerminOperacija;
import operacije.zaposlenitermin.VratiListuZaposlenihTerminaOperacija;
import operacije.zaposlenitermin.VratiSveZaposleneTermineOperacija;

/**
 *
 * @author Vukasin Lukic
 */
public class Controller {

    private static Controller instanca;

    private Controller() {
    }

    public static Controller getInstanca() {
        if (instanca == null) {
            instanca = new Controller();
        }
        return instanca;
    }

    //LOGIN 
    public ZaposleniFakulteta login(ZaposleniFakulteta z) throws Exception {
        LoginOperacija lo = new LoginOperacija();
        lo.izvrsi(z, null);
        return lo.getZaposleni();
    }

    public void odjava(String korisnickoIme) throws Exception {
        OdjaviZaposlenogOperacija op = new OdjaviZaposlenogOperacija();
        op.izvrsi(korisnickoIme, null);
    }

    //Zaposleni fakulteta fje
    public void kreirajZaposlenog(ZaposleniFakulteta z) throws Exception {
        KreirajZaposlenogOperacija op = new KreirajZaposlenogOperacija();
        op.izvrsi(z, null);
    }

    public void promeniZaposlenog(ZaposleniFakulteta z) throws Exception {
        PromeniZaposlenogOperacija op = new PromeniZaposlenogOperacija();
        op.izvrsi(z, null);
    }

    public void obrisiZaposlenog(ZaposleniFakulteta z) throws Exception {
        ObrisiZaposlenogOperacija op = new ObrisiZaposlenogOperacija();
        op.izvrsi(z, null);
    }

    public ZaposleniFakulteta pretraziZaposlenog(ZaposleniFakulteta z) throws Exception {
        PretraziZaposlenogOperacija op = new PretraziZaposlenogOperacija();
        op.izvrsi(z, null);
        return op.getRezultat();
    }

    public List<ApstraktniDomenskiObjekat> vratiSveZaposlene() throws Exception {
        VratiSveZaposleneOperacija op = new VratiSveZaposleneOperacija();
        op.izvrsi(null, null);
        return op.getLista();
    }

    public List<ApstraktniDomenskiObjekat> vratiListuZaposlenih(String kriterijum) throws Exception {
        VratiListuZaposlenihOperacija op = new VratiListuZaposlenihOperacija();
        op.izvrsi(null, kriterijum);
        return op.getLista();
    }

    //Student operacije
    public void kreirajStudenta(Student s) throws Exception {
        KreirajStudentaOperacija op = new KreirajStudentaOperacija();
        op.izvrsi(s, null);
    }

    public void promeniStudenta(Student s) throws Exception {
        PromeniStudentaOperacija op = new PromeniStudentaOperacija();
        op.izvrsi(s, null);
    }

    public void obrisiStudenta(Student s) throws Exception {
        ObrisiStudentaOperacija op = new ObrisiStudentaOperacija();
        op.izvrsi(s, null);
    }

    public Student pretraziStudenta(Student s) throws Exception {
        PretraziStudentaOperacija op = new PretraziStudentaOperacija();
        op.izvrsi(s, null);
        return op.getRezultat();
    }

    public List<ApstraktniDomenskiObjekat> vratiSveStudente() throws Exception {
        VratiSveStudenteOperacija op = new VratiSveStudenteOperacija();
        op.izvrsi(null, null);
        return op.getLista();
    }

    public List<ApstraktniDomenskiObjekat> vratiListuStudenata(String kriterijum) throws Exception {
        VratiListuStudenataOperacija op = new VratiListuStudenataOperacija();
        op.izvrsi(null, kriterijum);
        return op.getLista();
    }

    //Operacije nad StudijskimProgramom
    public void kreirajStudijskiProgram(StudijskiProgram sp) throws Exception {
        KreirajStudijskiProgramOperacija op = new KreirajStudijskiProgramOperacija();
        op.izvrsi(sp, null);
    }

    public void promeniStudijskiProgram(StudijskiProgram sp) throws Exception {
        PromeniStudijskiProgramOperacija op = new PromeniStudijskiProgramOperacija();
        op.izvrsi(sp, null);
    }

    public void obrisiStudijskiProgram(StudijskiProgram sp) throws Exception {
        ObrisiStudijskiProgramOperacija op = new ObrisiStudijskiProgramOperacija();
        op.izvrsi(sp, null);
    }

    public List<ApstraktniDomenskiObjekat> vratiSveStudijskePrograme() throws Exception {
        VratiSveStudijskeProgrameOperacija op = new VratiSveStudijskeProgrameOperacija();
        op.izvrsi(null, null);
        return op.getLista();
    }

    public List<ApstraktniDomenskiObjekat> vratiListuStudijskihPrograma(String kriterijum) throws Exception {
        VratiListuStudijskihProgramaOperacija op = new VratiListuStudijskihProgramaOperacija();
        op.izvrsi(null, kriterijum);
        return op.getLista();
    }

    //operacije nad terminima dezurstva
    public void ubaciTerminDezurstva(TerminDezurstva td) throws Exception {
        UbaciTerminDezurstvaOperacija op = new UbaciTerminDezurstvaOperacija();
        op.izvrsi(td, null);
    }

    public void promeniTerminDezurstva(TerminDezurstva td) throws Exception {
        PromeniTerminDezurstvaOperacija op = new PromeniTerminDezurstvaOperacija();
        op.izvrsi(td, null);
    }

    public void obrisiTerminDezurstva(TerminDezurstva td) throws Exception {
        ObrisiTerminDezurstvaOperacija op = new ObrisiTerminDezurstvaOperacija();
        op.izvrsi(td, null);
    }

    public List<ApstraktniDomenskiObjekat> vratiSveTermineDezurstva() throws Exception {
        VratiSveTermineDezurstvaOperacija op = new VratiSveTermineDezurstvaOperacija();
        op.izvrsi(null, null);
        return op.getLista();
    }

    public List<ApstraktniDomenskiObjekat> vratiListuTerminaDezurstva(String kriterijum) throws Exception {
        VratiListuTerminaDezurstvaOperacija op = new VratiListuTerminaDezurstvaOperacija();
        op.izvrsi(null, kriterijum);
        return op.getLista();
    }

    //operacije za tipPolja
    public void kreirajTipPolja(TipPolja tp) throws Exception {
        KreirajTipPoljaOperacija op = new KreirajTipPoljaOperacija();
        op.izvrsi(tp, null);
    }

    public void promeniTipPolja(TipPolja tp) throws Exception {
        PromeniTipPoljaOperacija op = new PromeniTipPoljaOperacija();
        op.izvrsi(tp, null);
    }

    public void obrisiTipPolja(TipPolja tp) throws Exception {
        ObrisiTipPoljaOperacija op = new ObrisiTipPoljaOperacija();
        op.izvrsi(tp, null);
    }

    public List<ApstraktniDomenskiObjekat> vratiSveTipovePolja() throws Exception {
        VratiSveTipovePoljaOperacija op = new VratiSveTipovePoljaOperacija();
        op.izvrsi(null, null);
        return op.getLista();
    }

    public List<ApstraktniDomenskiObjekat> vratiListuTipovaPolja(String kriterijum) throws Exception {
        VratiListuTipovaPoljaOperacija op = new VratiListuTipovaPoljaOperacija();
        op.izvrsi(null, kriterijum);
        return op.getLista();
    }

    //operacije nad sv20 obrazscima
    public void kreirajSV20Obrazac(SV20Obrazac o) throws Exception {
        KreirajSV20ObrazacOperacija op = new KreirajSV20ObrazacOperacija();
        op.izvrsi(o, null);
    }

    public void promeniSV20Obrazac(SV20Obrazac o) throws Exception {
        PromeniSV20ObrazacOperacija op = new PromeniSV20ObrazacOperacija();
        op.izvrsi(o, null);
    }

    public void obrisiSV20Obrazac(SV20Obrazac o) throws Exception {
        ObrisiSV20ObrazacOperacija op = new ObrisiSV20ObrazacOperacija();
        op.izvrsi(o, null);
    }

    public SV20Obrazac pretraziSV20Obrazac(SV20Obrazac o) throws Exception {
        PretraziSV20ObrazacOperacija op = new PretraziSV20ObrazacOperacija();
        op.izvrsi(o, null);
        return op.getRezultat();
    }

    public List<ApstraktniDomenskiObjekat> vratiSveSV20Obrasce() throws Exception {
        VratiSveSV20ObrasceOperacija op = new VratiSveSV20ObrasceOperacija();
        op.izvrsi(null, null);
        return op.getLista();
    }

    public List<ApstraktniDomenskiObjekat> vratiListuSV20Obrazaca(String kriterijum) throws Exception {
        VratiListuSV20ObrazacaOperacija op = new VratiListuSV20ObrazacaOperacija();
        op.izvrsi(null, kriterijum);
        return op.getLista();
    }

    //operacije nad stavkama obrasca
    public void kreirajStavkuObrasca(StavkeObrasca s) throws Exception {
        KreirajStavkuObrascaOperacija op = new KreirajStavkuObrascaOperacija();
        op.izvrsi(s, null);
    }

    public void promeniStavkuObrasca(StavkeObrasca s) throws Exception {
        PromeniStavkuObrascaOperacija op = new PromeniStavkuObrascaOperacija();
        op.izvrsi(s, null);
    }

    public List<ApstraktniDomenskiObjekat> pretraziStavkeObrasca(StavkeObrasca s) throws Exception {
        PretraziStavkeObrascaOperacija op = new PretraziStavkeObrascaOperacija();
        op.izvrsi(s, null);
        return op.getLista();
    }

    public List<ApstraktniDomenskiObjekat> vratiStavkeObrasca(String kriterijum) throws Exception {
        VratiStavkeObrascaOperacija op = new VratiStavkeObrascaOperacija();
        op.izvrsi(null, kriterijum);
        return op.getLista();
    }

    //operacije nad terminom zaposlenog
    public void kreirajZaposleniTermin(ZaposleniTermin zt) throws Exception {
        KreirajZaposleniTerminOperacija op = new KreirajZaposleniTerminOperacija();
        op.izvrsi(zt, null);
    }

    public void promeniZaposleniTermin(ZaposleniTermin zt) throws Exception {
        PromeniZaposleniTerminOperacija op = new PromeniZaposleniTerminOperacija();
        op.izvrsi(zt, null);
    }

    public void obrisiZaposleniTermin(ZaposleniTermin zt) throws Exception {
        ObrisiZaposleniTerminOperacija op = new ObrisiZaposleniTerminOperacija();
        op.izvrsi(zt, null);
    }

    public ZaposleniTermin pretraziZaposleniTermin(ZaposleniTermin zt) throws Exception {
        PretraziZaposleniTerminOperacija op = new PretraziZaposleniTerminOperacija();
        op.izvrsi(zt, null);
        return op.getRezultat();
    }

    public List<ApstraktniDomenskiObjekat> vratiSveZaposleneTermine() throws Exception {
        VratiSveZaposleneTermineOperacija op = new VratiSveZaposleneTermineOperacija();
        op.izvrsi(null, null);
        return op.getLista();
    }

    public List<ApstraktniDomenskiObjekat> vratiListuZaposlenihTermina(String kriterijum) throws Exception {
        VratiListuZaposlenihTerminaOperacija op = new VratiListuZaposlenihTerminaOperacija();
        op.izvrsi(null, kriterijum);
        return op.getLista();
    }
}
