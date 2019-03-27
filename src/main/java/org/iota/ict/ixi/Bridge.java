package org.iota.ict.ixi;

import org.iota.ict.Ict;
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
            Ict.LOGGER.info("[Bridge.ixi] Cannot start server at port "+Constants.DEFAULT_BRIDGE_PORT+":" + e.getMessage());
            try { serverSocket.close(); } catch(Exception x) { ; }
            return;
        }

        Ict.LOGGER.info("[Bridge.ixi] Server successfully started at port "+Constants.DEFAULT_BRIDGE_PORT+".");

        while(isRunning()) {

            Socket clientSocket = null;

            try {

                clientSocket = serverSocket.accept();
                subWorkers.add(new ClientHandler(clientSocket, ixi));

            } catch (Exception e) {
                try { clientSocket.close(); } catch(Exception x) { ; }
            }

        }

    }

    @Override
    public void onTerminate() {
        Ict.LOGGER.info("[Bridge.ixi] Terminating server...");
        try { serverSocket.close(); } catch(Exception e) { ; }
    }

}
