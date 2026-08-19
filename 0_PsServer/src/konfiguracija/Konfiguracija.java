package konfiguracija;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author Vukasin Lukic
 */
public class Konfiguracija {

    private static Konfiguracija instanca;
    private Properties konfiguracija;

    public Konfiguracija() throws IOException {
        konfiguracija = new Properties();

        try (InputStream is = Konfiguracija.class.getResourceAsStream("/konfiguracija/dbconfig.properties")) {

            if (is == null) {
                throw new IOException("Nije pronadjen dbconfig.properties u /konfiguracija/");
            }

            konfiguracija.load(is);
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

        try (FileOutputStream fos = new FileOutputStream(
                "src/konfiguracija/dbconfig.properties")) {

            konfiguracija.store(fos, "Azhurirani parametri baze");

            System.out.println("Uspesno sacuvane izmene u konfiguraciji.");
        }
    }
}