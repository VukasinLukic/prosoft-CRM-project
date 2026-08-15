/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package komunikacija;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 *
 * @author Vukasin Lukic
 */
public class Primalac {

    private Socket socket;
    private ObjectInputStream in;

    public Primalac(Socket socket) {

        this.socket = socket;
        try {
            this.in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public Object primi() {
        try {
            return in.readObject();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
