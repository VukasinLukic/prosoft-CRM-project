/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package komunikacija;

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
import java.util.List;

/**
 *
 * @author Vukasin Lukic
 */
public class Komunikacija {

    private Socket socket;
    private static Komunikacija instanca;
    private Posiljac posiljac;
    private Primalac primalac;

    public Komunikacija() {
    }

    public static Komunikacija getInstanca() {
        if (instanca == null) {
            instanca = new Komunikacija();
        }
        return instanca;
    }

    public void konekcija() throws Exception {
        try {
            socket = new Socket("localhost", 9000);
            posiljac = new Posiljac(socket);
            primalac = new Primalac(socket);

        } catch (IOException e) {
            throw new Exception("Nije moguce povezati se na server!");

        }
    }

    public void zatvoriKonekciju() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (Exception e) {
        }

    }

    //operacije login i odjava
    public ZaposleniFakulteta login(String username, String password) throws Exception {
        ZaposleniFakulteta z = new ZaposleniFakulteta();
        z.setKorisnickoIme(username);
        z.setSifra(password);
        Zahtev zahtev = new Zahtev(Operacija.PRIJAVI_ZAPOSLENOG, z);
        posiljac.posalji(zahtev);
        Odgovor odgovor = (Odgovor) primalac.primi();
        if (odgovor.getOdgovor() instanceof Exception) {
            throw (Exception) odgovor.getOdgovor();
        }
        if (odgovor.getOdgovor() instanceof String && ((String) odgovor.getOdgovor()).startsWith("GRESKA")) {
            throw new Exception((String) odgovor.getOdgovor());
        }

        return (ZaposleniFakulteta) odgovor.getOdgovor();
    }

    public void odjaviZaposlenog(String korisnickoIme) throws Exception {
        Zahtev zahtev = new Zahtev(Operacija.ODJAVI_ZAPOSLENOG, korisnickoIme);
        posiljac.posalji(zahtev);

        Odgovor odgovor = (Odgovor) primalac.primi();
        proveraGreske(odgovor);
    }

    //helper metoda ako se desi greska pri odjavi ... 
    private void proveraGreske(Odgovor odgovor) throws Exception {
        if (odgovor.getOdgovor() instanceof Exception) {
            throw (Exception) odgovor.getOdgovor();
        }
        if (odgovor.getOdgovor() instanceof String) {
            String poruka = (String) odgovor.getOdgovor();
            if (poruka.startsWith("GRESKA")) {
                throw new Exception(poruka);
            }
        }
    }

    //operacije nad zaposlenom 
    public void kreirajZaposlenog(ZaposleniFakulteta z) throws Exception {
        Zahtev zahtev = new Zahtev(Operacija.KREIRAJ_ZAPOSLENOG, z);
        posiljac.posalji(zahtev);
        Odgovor odgovor = (Odgovor) primalac.primi();
        proveraGreske(odgovor);
    }

    public void promeniZaposlenog(ZaposleniFakulteta z) throws Exception {
        Zahtev zahtev = new Zahtev(Operacija.PROMENI_ZAPOSLENOG, z);
        posiljac.posalji(zahtev);

        Odgovor odgovor = (Odgovor) primalac.primi();
        proveraGreske(odgovor);
    }

    public void obrisiZaposlenog(ZaposleniFakulteta z) throws Exception {
        Zahtev zahtev = new Zahtev(Operacija.OBRISI_ZAPOSLENOG, z);
        posiljac.posalji(zahtev);

        Odgovor odgovor = (Odgovor) primalac.primi();
        proveraGreske(odgovor);
    }

    public ZaposleniFakulteta pretraziZaposlenog(ZaposleniFakulteta z) throws Exception {
        Zahtev zahtev = new Zahtev(Operacija.PRETRAZI_ZAPOSLENOG, z);
        posiljac.posalji(zahtev);

        Odgovor odgovor = (Odgovor) primalac.primi();
        proveraGreske(odgovor);

        return (ZaposleniFakulteta) odgovor.getOdgovor();
    }

    public List<ZaposleniFakulteta> vratiSveZaposlene() throws Exception {
        Zahtev zahtev = new Zahtev(Operacija.VRATI_SVE_ZAPOSLENE, null);
        posiljac.posalji(zahtev);

        Odgovor odgovor = (Odgovor) primalac.primi();
        proveraGreske(odgovor);

        return (List<ZaposleniFakulteta>) odgovor.getOdgovor();
    }

    public List<ZaposleniFakulteta> vratiListuZaposlenih(String kriterijum) throws Exception {
        Zahtev zahtev = new Zahtev(Operacija.VRATI_LISTU_ZAPOSLENIH, kriterijum);
        posiljac.posalji(zahtev);

        Odgovor odgovor = (Odgovor) primalac.primi();
        proveraGreske(odgovor);

        return (List<ZaposleniFakulteta>) odgovor.getOdgovor();
    }

    //Operacije nad studentom
    public void kreirajStudenta(Student s) throws Exception {
        Zahtev zahtev = new Zahtev(Operacija.KREIRAJ_STUDENTA, s);
        posiljac.posalji(zahtev);

        Odgovor odgovor = (Odgovor) primalac.primi();
        proveraGreske(odgovor);
    }

    public void promeniStudenta(Student s) throws Exception {
        Zahtev zahtev = new Zahtev(Operacija.PROMENI_STUDENTA, s);
        posiljac.posalji(zahtev);

        Odgovor odgovor = (Odgovor) primalac.primi();
        proveraGreske(odgovor);
    }

    public void obrisiStudenta(Student s) throws Exception {
        Zahtev zahtev = new Zahtev(Operacija.OBRISI_STUDENTA, s);
        posiljac.posalji(zahtev);

        Odgovor odgovor = (Odgovor) primalac.primi();
        proveraGreske(odgovor);
    }

    public Student pretraziStudenta(Student s) throws Exception {
        Zahtev zahtev = new Zahtev(Operacija.PRETRAZI_STUDENTA, s);
        posiljac.posalji(zahtev);

        Odgovor odgovor = (Odgovor) primalac.primi();
        proveraGreske(odgovor);

        return (Student) odgovor.getOdgovor();
    }

    public List<Student> vratiSveStudente() throws Exception {
        Zahtev zahtev = new Zahtev(Operacija.VRATI_SVE_STUDENTE, null);
        posiljac.posalji(zahtev);

        Odgovor odgovor = (Odgovor) primalac.primi();
        proveraGreske(odgovor);

        return (List<Student>) odgovor.getOdgovor();
    }

    public List<Student> vratiListuStudenata(String kriterijum) throws Exception {
        Zahtev zahtev = new Zahtev(Operacija.VRATI_LISTU_STUDENATA, kriterijum);
        posiljac.posalji(zahtev);

        Odgovor odgovor = (Odgovor) primalac.primi();
        proveraGreske(odgovor);

        return (List<Student>) odgovor.getOdgovor();
    }

    //operacije nad studijskim programom 
    public void kreirajStudijskiProgram(StudijskiProgram sp) throws Exception {
        Zahtev zahtev = new Zahtev(Operacija.KREIRAJ_STUDIJSKI_PROGRAM, sp);
        posiljac.posalji(zahtev);

        Odgovor odgovor = (Odgovor) primalac.primi();
        proveraGreske(odgovor);
    }

    public void promeniStudijskiProgram(StudijskiProgram sp) throws Exception {
        Zahtev zahtev = new Zahtev(Operacija.PROMENI_STUDIJSKI_PROGRAM, sp);
        posiljac.posalji(zahtev);

        Odgovor odgovor = (Odgovor) primalac.primi();
        proveraGreske(odgovor);
    }

    public void obrisiStudijskiProgram(StudijskiProgram sp) throws Exception {
        Zahtev zahtev = new Zahtev(Operacija.OBRISI_STUDIJSKI_PROGRAM, sp);
        posiljac.posalji(zahtev);

        Odgovor odgovor = (Odgovor) primalac.primi();
        proveraGreske(odgovor);
    }

    public List<StudijskiProgram> vratiSveStudijskePrograme() throws Exception {
        Zahtev zahtev = new Zahtev(Operacija.VRATI_SVE_STUDIJSKE_PROGRAME, null);
        posiljac.posalji(zahtev);

        Odgovor odgovor = (Odgovor) primalac.primi();
        proveraGreske(odgovor);

        return (List<StudijskiProgram>) odgovor.getOdgovor();
    }

    public List<StudijskiProgram> vratiListuStudijskihPrograma(String kriterijum) throws Exception {
        Zahtev zahtev = new Zahtev(Operacija.VRATI_LISTU_STUDIJSKIH_PROGRAMA, kriterijum);
        posiljac.posalji(zahtev);

        Odgovor odgovor = (Odgovor) primalac.primi();
        proveraGreske(odgovor);

        return (List<StudijskiProgram>) odgovor.getOdgovor();
    }

    //operacije nad terminom dezurstva
    public void ubaciTerminDezurstva(TerminDezurstva td) throws Exception {
        Zahtev zahtev = new Zahtev(Operacija.UBACI_TERMIN_DEZURSTVA, td);
        posiljac.posalji(zahtev);

        Odgovor odgovor = (Odgovor) primalac.primi();
        proveraGreske(odgovor);
    }

    public void promeniTerminDezurstva(TerminDezurstva td) throws Exception {
        Zahtev zahtev = new Zahtev(Operacija.PROMENI_TERMIN_DEZURSTVA, td);
        posiljac.posalji(zahtev);

        Odgovor odgovor = (Odgovor) primalac.primi();
        proveraGreske(odgovor);
    }

    public void obrisiTerminDezurstva(TerminDezurstva td) throws Exception {
        Zahtev zahtev = new Zahtev(Operacija.OBRISI_TERMIN_DEZURSTVA, td);
        posiljac.posalji(zahtev);

        Odgovor odgovor = (Odgovor) primalac.primi();
        proveraGreske(odgovor);
    }

    public List<TerminDezurstva> vratiSveTermineDezurstva() throws Exception {
        Zahtev zahtev = new Zahtev(Operacija.VRATI_SVE_TERMINE_DEZURSTVA, null);
        posiljac.posalji(zahtev);

        Odgovor odgovor = (Odgovor) primalac.primi();
        proveraGreske(odgovor);

        return (List<TerminDezurstva>) odgovor.getOdgovor();
    }

    public List<TerminDezurstva> vratiListuTerminaDezurstva(String kriterijum) throws Exception {
        Zahtev zahtev = new Zahtev(Operacija.VRATI_LISTU_TERMINA_DEZURSTVA, kriterijum);
        posiljac.posalji(zahtev);

        Odgovor odgovor = (Odgovor) primalac.primi();
        proveraGreske(odgovor);

        return (List<TerminDezurstva>) odgovor.getOdgovor();
    }

    //operacije nad tipom polja 
    public void kreirajTipPolja(TipPolja tp) throws Exception {
        Zahtev zahtev = new Zahtev(Operacija.KREIRAJ_TIP_POLJA, tp);
        posiljac.posalji(zahtev);

        Odgovor odgovor = (Odgovor) primalac.primi();
        proveraGreske(odgovor);
    }

    public void promeniTipPolja(TipPolja tp) throws Exception {
        Zahtev zahtev = new Zahtev(Operacija.PROMENI_TIP_POLJA, tp);
        posiljac.posalji(zahtev);

        Odgovor odgovor = (Odgovor) primalac.primi();
        proveraGreske(odgovor);
    }

    public void obrisiTipPolja(TipPolja tp) throws Exception {
        Zahtev zahtev = new Zahtev(Operacija.OBRISI_TIP_POLJA, tp);
        posiljac.posalji(zahtev);

        Odgovor odgovor = (Odgovor) primalac.primi();
        proveraGreske(odgovor);
    }

    public List<TipPolja> vratiSveTipovePolja() throws Exception {
        Zahtev zahtev = new Zahtev(Operacija.VRATI_SVE_TIPOVE_POLJA, null);
        posiljac.posalji(zahtev);

        Odgovor odgovor = (Odgovor) primalac.primi();
        proveraGreske(odgovor);

        return (List<TipPolja>) odgovor.getOdgovor();
    }

    public List<TipPolja> vratiListuTipovaPolja(String kriterijum) throws Exception {
        Zahtev zahtev = new Zahtev(Operacija.VRATI_LISTU_TIPOVA_POLJA, kriterijum);
        posiljac.posalji(zahtev);

        Odgovor odgovor = (Odgovor) primalac.primi();
        proveraGreske(odgovor);

        return (List<TipPolja>) odgovor.getOdgovor();
    }

    //operacije nad sv20 obrazcom 
    public void kreirajSV20Obrazac(SV20Obrazac o) throws Exception {
        Zahtev zahtev = new Zahtev(Operacija.KREIRAJ_SV20_OBRAZAC, o);
        posiljac.posalji(zahtev);

        Odgovor odgovor = (Odgovor) primalac.primi();
        proveraGreske(odgovor);
    }

    public void promeniSV20Obrazac(SV20Obrazac o) throws Exception {
        Zahtev zahtev = new Zahtev(Operacija.PROMENI_SV20_OBRAZAC, o);
        posiljac.posalji(zahtev);

        Odgovor odgovor = (Odgovor) primalac.primi();
        proveraGreske(odgovor);
    }

    public void obrisiSV20Obrazac(SV20Obrazac o) throws Exception {
        Zahtev zahtev = new Zahtev(Operacija.OBRISI_SV20_OBRAZAC, o);
        posiljac.posalji(zahtev);

        Odgovor odgovor = (Odgovor) primalac.primi();
        proveraGreske(odgovor);
    }

    public SV20Obrazac pretraziSV20Obrazac(SV20Obrazac o) throws Exception {
        Zahtev zahtev = new Zahtev(Operacija.PRETRAZI_SV20_OBRAZAC, o);
        posiljac.posalji(zahtev);

        Odgovor odgovor = (Odgovor) primalac.primi();
        proveraGreske(odgovor);

        return (SV20Obrazac) odgovor.getOdgovor();
    }

    public List<SV20Obrazac> vratiListuSV20Obrazaca(String kriterijum) throws Exception {
        Zahtev zahtev = new Zahtev(Operacija.VRATI_LISTU_SV20_OBRAZACA, kriterijum);
        posiljac.posalji(zahtev);

        Odgovor odgovor = (Odgovor) primalac.primi();
        proveraGreske(odgovor);

        return (List<SV20Obrazac>) odgovor.getOdgovor();
    }

    //operacije nad stavkom obrasca
    public void kreirajStavkuObrasca(StavkeObrasca s) throws Exception {
        Zahtev zahtev = new Zahtev(Operacija.KREIRAJ_STAVKU_OBRASCA, s);
        posiljac.posalji(zahtev);

        Odgovor odgovor = (Odgovor) primalac.primi();
        proveraGreske(odgovor);
    }

    public void promeniStavkuObrasca(StavkeObrasca s) throws Exception {
        Zahtev zahtev = new Zahtev(Operacija.PROMENI_STAVKU_OBRASCA, s);
        posiljac.posalji(zahtev);

        Odgovor odgovor = (Odgovor) primalac.primi();
        proveraGreske(odgovor);
    }

    public List<StavkeObrasca> pretraziStavkeObrasca(StavkeObrasca s) throws Exception {
        Zahtev zahtev = new Zahtev(Operacija.PRETRAZI_STAVKU_OBRASCA, s);
        posiljac.posalji(zahtev);

        Odgovor odgovor = (Odgovor) primalac.primi();
        proveraGreske(odgovor);

        return (List<StavkeObrasca>) odgovor.getOdgovor();
    }

    //operacije nad terminom zaposlenog
    public void kreirajZaposleniTermin(ZaposleniTermin zt) throws Exception {
        Zahtev zahtev = new Zahtev(Operacija.KREIRAJ_ZAPOSLENI_TERMIN, zt);
        posiljac.posalji(zahtev);

        Odgovor odgovor = (Odgovor) primalac.primi();
        proveraGreske(odgovor);
    }

    public void promeniZaposleniTermin(ZaposleniTermin zt) throws Exception {
        Zahtev zahtev = new Zahtev(Operacija.PROMENI_ZAPOSLENI_TERMIN, zt);
        posiljac.posalji(zahtev);

        Odgovor odgovor = (Odgovor) primalac.primi();
        proveraGreske(odgovor);
    }

    public void obrisiZaposleniTermin(ZaposleniTermin zt) throws Exception {
        Zahtev zahtev = new Zahtev(Operacija.OBRISI_ZAPOSLENI_TERMIN, zt);
        posiljac.posalji(zahtev);

        Odgovor odgovor = (Odgovor) primalac.primi();
        proveraGreske(odgovor);
    }

    public ZaposleniTermin pretraziZaposleniTermin(ZaposleniTermin zt) throws Exception {
        Zahtev zahtev = new Zahtev(Operacija.PRETRAZI_ZAPOSLENI_TERMIN, zt);
        posiljac.posalji(zahtev);

        Odgovor odgovor = (Odgovor) primalac.primi();
        proveraGreske(odgovor);

        return (ZaposleniTermin) odgovor.getOdgovor();
    }
}
