package konfiguracija;

/**
 *
 * @author Vukasin Lukic
 */


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Konfiguracija {

    private static Konfiguracija instanca;
    private Properties konfiguracija;
    
    
    private final String RELATIVNA_PUTANJA = "dbconfig.properties"; 

    public Konfiguracija() throws IOException {
        konfiguracija = new Properties();
        
        // čitanje 
        try (FileInputStream fis = new FileInputStream(RELATIVNA_PUTANJA)) {
            konfiguracija.load(fis);
            System.out.println("Uspesno ucitana konfiguracija.");
        }
    }

    public static Konfiguracija getInstanca() throws IOException {
        if (instanca == null) {
            instanca = new Konfiguracija();
        }
        return instanca;
    }

    public String getProperty(String key) {
        return konfiguracija.getProperty(key, "n/a");
    }

    public void setProperty(String key, String value) {
        konfiguracija.setProperty(key, value);
    }

    
    public void sacuvajIzmene() throws IOException {
        try (FileOutputStream fos = new FileOutputStream(RELATIVNA_PUTANJA)) {
            konfiguracija.store(fos, "Azhurirani parametri baze");
            System.out.println("Uspesno sacuvane izmene u konfiguraciji.");
        }
    }
}