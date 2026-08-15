/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import niti.ObradaKlijentskihZahteva;

/**
 *
 * @author Vukasin Lukic
 */
public class Server extends Thread {

    private boolean kraj = false;
    private ServerSocket serversocket;
    private final List<ObradaKlijentskihZahteva> klijenti;

    public Server() {
        klijenti = new ArrayList<>();
    }

    @Override
    public void run() {
        try {
            serversocket = new ServerSocket(9000);
            System.out.println("Server pokrenut na portu 9000");

            while (!kraj) {
                try {
                    Socket s = serversocket.accept();
                    System.out.println("Klijent se povezao");

                    ObradaKlijentskihZahteva obz = new ObradaKlijentskihZahteva(s);
                    klijenti.add(obz);
                    obz.start();
                } catch (SocketException se) {
                    if (kraj) {
                        System.out.println("Server je uredno zaustavljen.");
                        break;
                    }
                    throw se;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void zaustaviServer() throws IOException {
        kraj = true;

        for (ObradaKlijentskihZahteva k : klijenti) {
            k.prekini();
        }

        if (serversocket != null && !serversocket.isClosed()) {
            serversocket.close(); // prekida accept()
        }

        System.out.println("Server je zaustavljen");
    }
}
