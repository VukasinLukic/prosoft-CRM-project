/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package niti;

import controller.Controller;
import controller.RegistarPrijavljenih;
import domen.SV20Obrazac;
import domen.StavkeObrasca;
import domen.Student;
import domen.StudijskiProgram;
import domen.TerminDezurstva;
import domen.TipPolja;
import domen.ZaposleniFakulteta;
import domen.ZaposleniTermin;
import java.io.IOException;
import java.net.Socket;
import komunikacija.Odgovor;
import static komunikacija.Operacija.KREIRAJ_STAVKU_OBRASCA;
import static komunikacija.Operacija.KREIRAJ_STUDENTA;
import static komunikacija.Operacija.KREIRAJ_STUDIJSKI_PROGRAM;
import static komunikacija.Operacija.KREIRAJ_SV20_OBRAZAC;
import static komunikacija.Operacija.KREIRAJ_TIP_POLJA;
import static komunikacija.Operacija.KREIRAJ_ZAPOSLENI_TERMIN;
import static komunikacija.Operacija.KREIRAJ_ZAPOSLENOG;
import static komunikacija.Operacija.OBRISI_STUDENTA;
import static komunikacija.Operacija.OBRISI_STUDIJSKI_PROGRAM;
import static komunikacija.Operacija.OBRISI_SV20_OBRAZAC;
import static komunikacija.Operacija.OBRISI_TERMIN_DEZURSTVA;
import static komunikacija.Operacija.OBRISI_TIP_POLJA;
import static komunikacija.Operacija.OBRISI_ZAPOSLENI_TERMIN;
import static komunikacija.Operacija.OBRISI_ZAPOSLENOG;
import static komunikacija.Operacija.PRETRAZI_STAVKU_OBRASCA;
import static komunikacija.Operacija.PRETRAZI_STUDENTA;
import static komunikacija.Operacija.PRETRAZI_SV20_OBRAZAC;
import static komunikacija.Operacija.PRETRAZI_ZAPOSLENI_TERMIN;
import static komunikacija.Operacija.PRETRAZI_ZAPOSLENOG;
import static komunikacija.Operacija.PRIJAVI_ZAPOSLENOG;
import static komunikacija.Operacija.PROMENI_STAVKU_OBRASCA;
import static komunikacija.Operacija.PROMENI_STUDENTA;
import static komunikacija.Operacija.PROMENI_STUDIJSKI_PROGRAM;
import static komunikacija.Operacija.PROMENI_SV20_OBRAZAC;
import static komunikacija.Operacija.PROMENI_TERMIN_DEZURSTVA;
import static komunikacija.Operacija.PROMENI_TIP_POLJA;
import static komunikacija.Operacija.PROMENI_ZAPOSLENI_TERMIN;
import static komunikacija.Operacija.PROMENI_ZAPOSLENOG;
import static komunikacija.Operacija.UBACI_TERMIN_DEZURSTVA;
import static komunikacija.Operacija.VRATI_LISTU_STUDENATA;
import static komunikacija.Operacija.VRATI_LISTU_STUDIJSKIH_PROGRAMA;
import static komunikacija.Operacija.VRATI_LISTU_SV20_OBRAZACA;
import static komunikacija.Operacija.VRATI_LISTU_TERMINA_DEZURSTVA;
import static komunikacija.Operacija.VRATI_LISTU_TIPOVA_POLJA;
import static komunikacija.Operacija.VRATI_LISTU_ZAPOSLENIH;
import static komunikacija.Operacija.VRATI_SVE_STUDENTE;
import static komunikacija.Operacija.VRATI_SVE_STUDIJSKE_PROGRAME;
import static komunikacija.Operacija.VRATI_SVE_TERMINE_DEZURSTVA;
import static komunikacija.Operacija.VRATI_SVE_TIPOVE_POLJA;
import static komunikacija.Operacija.VRATI_SVE_ZAPOSLENE;
import komunikacija.Posiljac;
import komunikacija.Primalac;
import komunikacija.Zahtev;

/**
 *
 * @author Vukasin Lukic
 */
public class ObradaKlijentskihZahteva extends Thread {

    Socket socket;
    Primalac primalac;
    Posiljac posiljac;
    boolean kraj = false;
    
    // ako se socket neočekivano prekine (ugasi prozor bez odjave), korisnik
    // mogao osloboditi iz RegistarPrijavljenih i ne ostane zaglavljen
    private String prijavljenKorisnik;

    public ObradaKlijentskihZahteva(Socket socket) {
        this.socket = socket;
        posiljac = new Posiljac(socket);
        primalac = new Primalac(socket);
    }

    @Override
    public void run() {
        while (!kraj) {
            try {
                Zahtev zahtev = (Zahtev) primalac.primi();
                if (zahtev == null) {
                    break;
                }
                
                Odgovor odgovor = new Odgovor();
                
                try {
                    switch (zahtev.getOperacija()) {
                        
                        //login operacije
                        case PRIJAVI_ZAPOSLENOG:
                            ZaposleniFakulteta zLogin = (ZaposleniFakulteta) zahtev.getParametar();
                            ZaposleniFakulteta ulogovani = Controller.getInstanca().login(zLogin);
                            prijavljenKorisnik = ulogovani.getKorisnickoIme();
                            odgovor.setOdgovor(ulogovani);
                            break;

                        case ODJAVI_ZAPOSLENOG:
                            String korisnickoImeOdjava = (String) zahtev.getParametar();
                            Controller.getInstanca().odjava(korisnickoImeOdjava);
                            prijavljenKorisnik = null;
                            odgovor.setOdgovor("Uspesno odjavljen!");
                            break;

                        // operacije zaposlenog
                        case KREIRAJ_ZAPOSLENOG:
                            ZaposleniFakulteta zKreiraj = (ZaposleniFakulteta) zahtev.getParametar();
                            Controller.getInstanca().kreirajZaposlenog(zKreiraj);
                            odgovor.setOdgovor("Zaposleni uspesno kreiran!");
                            break;
                            
                        case PROMENI_ZAPOSLENOG:
                            ZaposleniFakulteta zPromeni = (ZaposleniFakulteta) zahtev.getParametar();
                            Controller.getInstanca().promeniZaposlenog(zPromeni);
                            odgovor.setOdgovor("Zaposleni uspesno promenjen!");
                            break;
                            
                        case OBRISI_ZAPOSLENOG:
                            ZaposleniFakulteta zObrisi = (ZaposleniFakulteta) zahtev.getParametar();
                            Controller.getInstanca().obrisiZaposlenog(zObrisi);
                            odgovor.setOdgovor("Zaposleni uspesno obrisan!");
                            break;
                            
                        case PRETRAZI_ZAPOSLENOG:
                            ZaposleniFakulteta zPretrazi = (ZaposleniFakulteta) zahtev.getParametar();
                            odgovor.setOdgovor(Controller.getInstanca().pretraziZaposlenog(zPretrazi));
                            break;
                            
                        case VRATI_SVE_ZAPOSLENE:
                            odgovor.setOdgovor(Controller.getInstanca().vratiSveZaposlene());
                            break;
                            
                        case VRATI_LISTU_ZAPOSLENIH:
                            String kritZap = (String) zahtev.getParametar();
                            odgovor.setOdgovor(Controller.getInstanca().vratiListuZaposlenih(kritZap));
                            break;

                        // operacije za studenta
                        case KREIRAJ_STUDENTA:
                            Student sKreiraj = (Student) zahtev.getParametar();
                            Controller.getInstanca().kreirajStudenta(sKreiraj);
                            odgovor.setOdgovor("Student uspesno kreiran!");
                            break;
                            
                        case PROMENI_STUDENTA:
                            Student sPromeni = (Student) zahtev.getParametar();
                            Controller.getInstanca().promeniStudenta(sPromeni);
                            odgovor.setOdgovor("Student uspesno promenjen!");
                            break;
                            
                        case OBRISI_STUDENTA:
                            Student sObrisi = (Student) zahtev.getParametar();
                            Controller.getInstanca().obrisiStudenta(sObrisi);
                            odgovor.setOdgovor("Student uspesno obrisan!");
                            break;
                            
                        case PRETRAZI_STUDENTA:
                            Student sPretrazi = (Student) zahtev.getParametar();
                            odgovor.setOdgovor(Controller.getInstanca().pretraziStudenta(sPretrazi));
                            break;
                            
                        case VRATI_SVE_STUDENTE:
                            odgovor.setOdgovor(Controller.getInstanca().vratiSveStudente());
                            break;
                            
                        case VRATI_LISTU_STUDENATA:
                            String kritStud = (String) zahtev.getParametar();
                            odgovor.setOdgovor(Controller.getInstanca().vratiListuStudenata(kritStud));
                            break;

                        //operacije oko studijskog programa
                        case KREIRAJ_STUDIJSKI_PROGRAM:
                            StudijskiProgram spKreiraj = (StudijskiProgram) zahtev.getParametar();
                            Controller.getInstanca().kreirajStudijskiProgram(spKreiraj);
                            odgovor.setOdgovor("Studijski program uspesno kreiran!");
                            break;
                            
                        case PROMENI_STUDIJSKI_PROGRAM:
                            StudijskiProgram spPromeni = (StudijskiProgram) zahtev.getParametar();
                            Controller.getInstanca().promeniStudijskiProgram(spPromeni);
                            odgovor.setOdgovor("Studijski program uspesno promenjen!");
                            break;
                            
                        case OBRISI_STUDIJSKI_PROGRAM:
                            StudijskiProgram spObrisi = (StudijskiProgram) zahtev.getParametar();
                            Controller.getInstanca().obrisiStudijskiProgram(spObrisi);
                            odgovor.setOdgovor("Studijski program uspesno obrisan!");
                            break;
                            
                        case VRATI_SVE_STUDIJSKE_PROGRAME:
                            odgovor.setOdgovor(Controller.getInstanca().vratiSveStudijskePrograme());
                            break;
                            
                        case VRATI_LISTU_STUDIJSKIH_PROGRAMA:
                            String kritSP = (String) zahtev.getParametar();
                            odgovor.setOdgovor(Controller.getInstanca().vratiListuStudijskihPrograma(kritSP));
                            break;

                        // oko termina dez
                        case UBACI_TERMIN_DEZURSTVA:
                            TerminDezurstva tdUbaci = (TerminDezurstva) zahtev.getParametar();
                            Controller.getInstanca().ubaciTerminDezurstva(tdUbaci);
                            odgovor.setOdgovor("Termin dezurstva uspesno ubacen!");
                            break;
                            
                        case PROMENI_TERMIN_DEZURSTVA:
                            TerminDezurstva tdPromeni = (TerminDezurstva) zahtev.getParametar();
                            Controller.getInstanca().promeniTerminDezurstva(tdPromeni);
                            odgovor.setOdgovor("Termin dezurstva uspesno promenjen!");
                            break;
                            
                        case OBRISI_TERMIN_DEZURSTVA:
                            TerminDezurstva tdObrisi = (TerminDezurstva) zahtev.getParametar();
                            Controller.getInstanca().obrisiTerminDezurstva(tdObrisi);
                            odgovor.setOdgovor("Termin dezurstva uspesno obrisan!");
                            break;
                            
                        case VRATI_SVE_TERMINE_DEZURSTVA:
                            odgovor.setOdgovor(Controller.getInstanca().vratiSveTermineDezurstva());
                            break;
                            
                        case VRATI_LISTU_TERMINA_DEZURSTVA:
                            String kritTD = (String) zahtev.getParametar();
                            odgovor.setOdgovor(Controller.getInstanca().vratiListuTerminaDezurstva(kritTD));
                            break;

                        //oko toipa polja
                        case KREIRAJ_TIP_POLJA:
                            TipPolja tpKreiraj = (TipPolja) zahtev.getParametar();
                            Controller.getInstanca().kreirajTipPolja(tpKreiraj);
                            odgovor.setOdgovor("Tip polja uspesno kreiran!");
                            break;
                            
                        case PROMENI_TIP_POLJA:
                            TipPolja tpPromeni = (TipPolja) zahtev.getParametar();
                            Controller.getInstanca().promeniTipPolja(tpPromeni);
                            odgovor.setOdgovor("Tip polja uspesno promenjen!");
                            break;
                            
                        case OBRISI_TIP_POLJA:
                            TipPolja tpObrisi = (TipPolja) zahtev.getParametar();
                            Controller.getInstanca().obrisiTipPolja(tpObrisi);
                            odgovor.setOdgovor("Tip polja uspesno obrisan!");
                            break;
                            
                        case VRATI_SVE_TIPOVE_POLJA:
                            odgovor.setOdgovor(Controller.getInstanca().vratiSveTipovePolja());
                            break;
                            
                        case VRATI_LISTU_TIPOVA_POLJA:
                            String kritTP = (String) zahtev.getParametar();
                            odgovor.setOdgovor(Controller.getInstanca().vratiListuTipovaPolja(kritTP));
                            break;

                        // sv20 obrazac fje
                        case KREIRAJ_SV20_OBRAZAC:
                            SV20Obrazac oKreiraj = (SV20Obrazac) zahtev.getParametar();
                            Controller.getInstanca().kreirajSV20Obrazac(oKreiraj);
                            odgovor.setOdgovor("SV20 obrazac uspesno kreiran!");
                            break;
                            
                        case PROMENI_SV20_OBRAZAC:
                            SV20Obrazac oPromeni = (SV20Obrazac) zahtev.getParametar();
                            Controller.getInstanca().promeniSV20Obrazac(oPromeni);
                            odgovor.setOdgovor("SV20 obrazac uspesno promenjen!");
                            break;
                            
                        case OBRISI_SV20_OBRAZAC:
                            SV20Obrazac oObrisi = (SV20Obrazac) zahtev.getParametar();
                            Controller.getInstanca().obrisiSV20Obrazac(oObrisi);
                            odgovor.setOdgovor("SV20 obrazac uspesno obrisan!");
                            break;
                            
                        case PRETRAZI_SV20_OBRAZAC:
                            SV20Obrazac oPretrazi = (SV20Obrazac) zahtev.getParametar();
                            odgovor.setOdgovor(Controller.getInstanca().pretraziSV20Obrazac(oPretrazi));
                            break;
                            
                        case VRATI_LISTU_SV20_OBRAZACA:
                            String kritObr = (String) zahtev.getParametar();
                            odgovor.setOdgovor(Controller.getInstanca().vratiListuSV20Obrazaca(kritObr));
                            break;

                        // fje stavka obrasca
                        case KREIRAJ_STAVKU_OBRASCA:
                            StavkeObrasca soKreiraj = (StavkeObrasca) zahtev.getParametar();
                            Controller.getInstanca().kreirajStavkuObrasca(soKreiraj);
                            odgovor.setOdgovor("Stavka obrasca uspesno kreirana!");
                            break;
                            
                        case PROMENI_STAVKU_OBRASCA:
                            StavkeObrasca soPromeni = (StavkeObrasca) zahtev.getParametar();
                            Controller.getInstanca().promeniStavkuObrasca(soPromeni);
                            odgovor.setOdgovor("Stavka obrasca uspesno promenjena!");
                            break;
                            
                        case PRETRAZI_STAVKU_OBRASCA:
                            StavkeObrasca soPretrazi = (StavkeObrasca) zahtev.getParametar();
                            odgovor.setOdgovor(Controller.getInstanca().pretraziStavkeObrasca(soPretrazi));
                            break;

                        //fje oko termina zaposlenog
                        case KREIRAJ_ZAPOSLENI_TERMIN:
                            ZaposleniTermin ztKreiraj = (ZaposleniTermin) zahtev.getParametar();
                            Controller.getInstanca().kreirajZaposleniTermin(ztKreiraj);
                            odgovor.setOdgovor("Zaposleni termin uspesno kreiran!");
                            break;
                            
                        case PROMENI_ZAPOSLENI_TERMIN:
                            ZaposleniTermin ztPromeni = (ZaposleniTermin) zahtev.getParametar();
                            Controller.getInstanca().promeniZaposleniTermin(ztPromeni);
                            odgovor.setOdgovor("Zaposleni termin uspesno promenjen!");
                            break;
                            
                        case OBRISI_ZAPOSLENI_TERMIN:
                            ZaposleniTermin ztObrisi = (ZaposleniTermin) zahtev.getParametar();
                            Controller.getInstanca().obrisiZaposleniTermin(ztObrisi);
                            odgovor.setOdgovor("Zaposleni termin uspesno obrisan!");
                            break;
                            
                        case PRETRAZI_ZAPOSLENI_TERMIN:
                            ZaposleniTermin ztPretrazi = (ZaposleniTermin) zahtev.getParametar();
                            odgovor.setOdgovor(Controller.getInstanca().pretraziZaposleniTermin(ztPretrazi));
                            break;

                        default:
                            odgovor.setOdgovor("Nepoznata operacija!");
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    odgovor.setOdgovor("GRESKA: " + e.getMessage());
                }
                
                posiljac.posalji(odgovor);
                
            } catch (Exception e) {
                System.out.println("Klijent disconnected ili greska: " + e.getMessage());
                break;
            }
        }
        
        if (prijavljenKorisnik != null) {
            RegistarPrijavljenih.odjavi(prijavljenKorisnik);
            prijavljenKorisnik = null;
        }
    }

    public void prekini() throws IOException {
        kraj = true;
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        interrupt();
    }
}
