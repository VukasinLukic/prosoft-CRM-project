package konfiguracija;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
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

        try (FileOutputStream fos = new FileOutputStream(putanjaZaUpis())) {

            konfiguracija.store(fos, "Azhurirani parametri baze");

            System.out.println("Uspesno sacuvane izmene u konfiguraciji.");
        }
    }

    /**
     * Isti fajl koji je ucitan preko classloader-a (getResourceAsStream) na pocetku, a ne fiksna
     * relativna putanja - radi bez obzira da li se aplikacija pokrece iz NetBeans-a ili iz jar-a
     * (dokle god se dbconfig.properties nalazi van jar-a, npr. u build/classes tokom razvoja).
     * Ako se putanja iz nekog razloga ne moze odrediti, koristi se stara relativna putanja kao fallback.
     */
    private String putanjaZaUpis() {
        URL resurs = Konfiguracija.class.getResource("/konfiguracija/dbconfig.properties");
        if (resurs != null) {
            try {
                File f = new File(resurs.toURI());
                return f.getAbsolutePath();
            } catch (URISyntaxException | IllegalArgumentException ex) {
                // pada na fallback ispod (npr. resurs je unutar jar-a i ne moze da se pise direktno)
            }
        }
        return "src/konfiguracija/dbconfig.properties";
    }
}