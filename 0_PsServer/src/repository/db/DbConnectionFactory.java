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
    private Connection konekcija;
    private String url;
    private String username;
    private String password;

    public DbConnectionFactory() throws SQLException {
        try {
            url = konfiguracija.Konfiguracija.getInstanca().getProperty("db.url");
            username = konfiguracija.Konfiguracija.getInstanca().getProperty("db.username");
            password = konfiguracija.Konfiguracija.getInstanca().getProperty("db.password");
            kreirajKonekciju();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Greska se desila u DbConnectionFactory");

        }
    }

    private void kreirajKonekciju() throws SQLException {
        konekcija = DriverManager.getConnection(url, username, password);
        konekcija.setAutoCommit(false);

    }

    public static DbConnectionFactory getInstanca() {
        if (instanca == null) {
            try {
                instanca = new DbConnectionFactory();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return instanca;

    }

    public Connection getKonekcija() {
        try {
            if (konekcija == null || konekcija.isClosed()) {
                kreirajKonekciju();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return konekcija;
    }

}
