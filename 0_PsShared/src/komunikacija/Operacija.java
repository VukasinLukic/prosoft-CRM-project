/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package komunikacija;

/**
 *
 * @author Vukasin Lukic
 */
public enum Operacija {
    //sistemske operacije iz dokumentacije projekta.
    
    
    
    //1. ZAPOSLENI FAKULTETA 
    PRIJAVI_ZAPOSLENOG,             // signal 23: PrijaviZaposleniFakulteta
    KREIRAJ_ZAPOSLENOG,             // signal 6: KreirajZaposleniFakulteta
    PROMENI_ZAPOSLENOG,             // signal 30: PromeniZaposleniFakulteta
    OBRISI_ZAPOSLENOG,              // signal 13: ObrisiZaposleniFakulteta
    PRETRAZI_ZAPOSLENOG,            // signal 21: PretraziZaposleniFakulteta
    VRATI_SVE_ZAPOSLENE,            // signal 44: vratiListuSviZaposleniFakulteta
    VRATI_LISTU_ZAPOSLENIH,         

    //2. SV-20 OBRAZAC
    KREIRAJ_SV20_OBRAZAC,           // signal 4: KreirajŠV-20 Obrazac
    PROMENI_SV20_OBRAZAC,           // signal 27: PromeniŠV-20 Obrazac
    OBRISI_SV20_OBRAZAC,            // signal 10: ObrisiŠV-20 Obrazac
    PRETRAZI_SV20_OBRAZAC,          // signal 18: PretraziŠV-20 Obrazac
    VRATI_LISTU_SV20_OBRAZACA,      // signali 36-39: vratiListuŠV-20 Obrazac

    //3. STUDENT
    KREIRAJ_STUDENTA,               // signal 2: KreirajStudent
    PROMENI_STUDENTA,               // signal 25: PromeniStudent
    OBRISI_STUDENTA,                // signal 8: ObrisiStudent
    PRETRAZI_STUDENTA,              // signal 16: PretraziStudent 
    VRATI_SVE_STUDENTE,             // signal 40: vratiListuSviStudent
    VRATI_LISTU_STUDENATA,          // signali 33, 34: vratiListuStudent

    //4. TIP POLJA
    KREIRAJ_TIP_POLJA,              // signal 5: KreirajTipPolja
    PROMENI_TIP_POLJA,              // signal 29: PromeniTipPolja
    OBRISI_TIP_POLJA,               // signal 12: ObrisiTipPolja
    VRATI_SVE_TIPOVE_POLJA,         // signal 43: vratiListuSviTipPolja
    VRATI_LISTU_TIPOVA_POLJA,       // signal 46: vratiListuTipPolja

    //5. STUDIJSKI PROGRAM
    KREIRAJ_STUDIJSKI_PROGRAM,      // signal 3: KreirajStudijskiProgram
    PROMENI_STUDIJSKI_PROGRAM,      // signal 26: PromeniStudijskiProgram
    OBRISI_STUDIJSKI_PROGRAM,       // signal 9: ObrisiStudijskiProgram
    VRATI_SVE_STUDIJSKE_PROGRAME,   // signal 41: vratiListuSviStudijskiProgram
    VRATI_LISTU_STUDIJSKIH_PROGRAMA,// signal 35: vratiListuStudijskiProgram

    //6. TERMIN DEZURSTVA 
    UBACI_TERMIN_DEZURSTVA,         // signal 32: UbaciTerminDezurstva 
    PROMENI_TERMIN_DEZURSTVA,       // signal 28: PromeniTerminDezurstva
    OBRISI_TERMIN_DEZURSTVA,        // signal 11: ObrisiTerminDezurstva
    VRATI_SVE_TERMINE_DEZURSTVA,    // signal 42: vratiListuSviTerminDezurstva
    VRATI_LISTU_TERMINA_DEZURSTVA,  // signal 45: vratiListuTerminDezurstva 

    //7. STAVKE OBRASCA
    KREIRAJ_STAVKU_OBRASCA,         // signal 1: KreirajStavkeObrasca
    PROMENI_STAVKU_OBRASCA,         // signal 24: PromeniStavkeObrasca
    PRETRAZI_STAVKU_OBRASCA,        // signal 15: PretraziStavkeObrasca

    //8. ZAPOSLENI-TERMIN
    KREIRAJ_ZAPOSLENI_TERMIN,       // signal 7: KreirajZaposleni-Termin
    PROMENI_ZAPOSLENI_TERMIN,       // signal 31: PromeniZaposleni-Termin
    OBRISI_ZAPOSLENI_TERMIN,        // signal 14: ObrisiZaposleni-Termin
    PRETRAZI_ZAPOSLENI_TERMIN       // signal 22: PretraziZaposleni-Termin
}
