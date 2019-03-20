package org.iota.ict.ixi;

import org.iota.ict.ixi.util.Constants;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Bridge extends IxiModule {

    private ServerSocket serverSocket;

    public Bridge(Ixi ixi) {
        super(ixi);
    }

    @Override
    public void run() {

        try {
            serverSocket = new ServerSocket(Constants.DEFAULT_PORT);
        } catch (IOException e) {
            System.err.println("Error while staring server socket: " + e.getMessage());
            try { serverSocket.close(); } catch(Exception x) { ; }
            return;
        }

        while(true) {

            Socket client = null;

            try {

                client = serverSocket.accept();
                new ClientHandler(client, ixi);

            } catch (IOException e) {
                System.err.println("Error while accepting client: " + e.getMessage());
                try { client.close(); } catch(Exception x) { ; }
            }

        }

    }

}
