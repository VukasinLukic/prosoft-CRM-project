/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository.db;

import java.io.IOException;
import java.sql.*;
import konfiguracija.Konfiguracija;

/**
 *
 * @author Vukasin Lukic
 */
public class DbConnectionFactory {

    private static DbConnectionFactory instanca;
    private final String url;
    private final String username;
    private final String password;

    // thread local sprecava race condition da jedna nit moze da zatvori konekcuju dok je druga koristi. jedna nit jedan korisnik
    private final ThreadLocal<Connection> konekcija = new ThreadLocal<>();

    private DbConnectionFactory() throws IOException {
        url = Konfiguracija.getInstanca().getProperty("db.url");
        username = Konfiguracija.getInstanca().getProperty("db.username");
        password = Konfiguracija.getInstanca().getProperty("db.password");
    }

    public static DbConnectionFactory getInstanca() {
        if (instanca == null) {
            try {
                instanca = new DbConnectionFactory();
            } catch (IOException ex) {
                System.getLogger(DbConnectionFactory.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }
        return instanca;
    }

    public Connection getKonekcija() {
        try {
            Connection k = konekcija.get();
            if (k == null || k.isClosed()) {
                k = DriverManager.getConnection(url, username, password);
                k.setAutoCommit(false);
                konekcija.set(k);
            }
        } catch (SQLException ex) {
            System.getLogger(DbConnectionFactory.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return konekcija.get();
    }
}
