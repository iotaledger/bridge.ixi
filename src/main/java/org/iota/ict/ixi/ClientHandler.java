package org.iota.ict.ixi;

import org.iota.ict.eee.EffectListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

/**
 * Thread which handles incoming connections on the given socket.
 */
public class ClientHandler extends Thread {

    private Socket socket;
    private Ixi ixi;
    private Map<String, EffectListener<String>> registeredListener = new HashMap<>();

    public ClientHandler(Socket socket, Ixi ixi) {
        this.socket = socket;
        this.ixi = ixi;
        start();
    }

    @Override
    public void run() {

        BufferedReader reader = null;
        PrintWriter writer = null;
        try {

             reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             writer = new PrintWriter(socket.getOutputStream());

            while(!interrupted()) {
                

            }

            reader.close();
            writer.close();

        } catch (SocketException e) {
            try { reader.close(); } catch (Exception x) { ; }
            try { writer.close(); } catch (Exception x) { ; }
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
