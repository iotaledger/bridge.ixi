package org.iota.ict.ixi;

import org.iota.ict.ixi.handler.ClientHandler;
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
            serverSocket = new ServerSocket(Constants.DEFAULT_BRIDGE_PORT);
        } catch (IOException e) {
            System.err.println("Error while staring server socket.");
            e.printStackTrace();
            try { serverSocket.close(); } catch(Exception x) { ; }
            return;
        }

        System.out.println("Bridge.ixi successfully started at port "+Constants.DEFAULT_BRIDGE_PORT+".");

        while(isRunning()) {

            Socket clientSocket = null;

            try {

                clientSocket = serverSocket.accept();
                subWorkers.add(new ClientHandler(clientSocket, ixi));

            } catch (IOException e) {
                System.err.println("Error while waiting for new clients.");
                e.printStackTrace();
                try { clientSocket.close(); } catch(Exception x) { ; }
            }

        }

    }

    @Override
    public void onTerminate() {
        try { serverSocket.close(); } catch(Exception e) { ; }
    }

}
