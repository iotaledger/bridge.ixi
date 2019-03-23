package org.iota.ict.ixi;

import org.iota.ict.ixi.handler.ClientHandler;
import org.iota.ict.ixi.util.Constants;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Bridge extends IxiModule {

    private ServerSocket serverSocket;
    private List<ClientHandler> clients = new ArrayList();
    private boolean terminate;

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

        System.out.println("bridge.ixi successfully started at port "+Constants.DEFAULT_BRIDGE_PORT);

        while(!terminate) {

            Socket clientSocket = null;

            try {

                clientSocket = serverSocket.accept();
                clients.add(new ClientHandler(clientSocket, ixi));

            } catch (IOException e) {
                System.err.println("Error while accepting client.");
                e.printStackTrace();
                try { clientSocket.close(); } catch(Exception x) { ; }
            }

        }

    }

    public void terminate() {
        terminate = true;
        try { serverSocket.close(); } catch(Exception e) { ; }
        for(ClientHandler client: clients)
            client.interrupt();
    }

}
