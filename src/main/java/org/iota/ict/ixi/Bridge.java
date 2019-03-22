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
            System.err.println("Error while staring server socket.");
            e.printStackTrace();
            try { serverSocket.close(); } catch(Exception x) { ; }
            return;
        }

        while(true) {

            Socket clientSocket = null;

            try {

                clientSocket = serverSocket.accept();
                new Client(clientSocket, ixi);

            } catch (IOException e) {
                System.err.println("Error while accepting client.");
                e.printStackTrace();
                try { clientSocket.close(); } catch(Exception x) { ; }
            }

        }

    }

}
