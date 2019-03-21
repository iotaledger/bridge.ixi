package org.iota.ict.ixi;

import org.iota.ict.eee.EffectListener;
import org.iota.ict.ixi.protobuf.Request;
import org.iota.ict.ixi.protobuf.Wrapper;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
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

        while(!interrupted()) {

            try {
                    Wrapper.WrapperMessage response = Wrapper.WrapperMessage.newBuilder().mergeFrom(socket.getInputStream()).build();

                System.out.println(response);

            } catch (IOException e) {
                    e.printStackTrace();
            }


        }

    }

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(20324);

        new Thread(() -> {

            try {

                Thread.sleep(1000);
                Socket socket = new Socket("127.0.0.1", 20324);

                Request.AddEffectListenerRequest request = Request.AddEffectListenerRequest.newBuilder().setEnvironment("TestEnvironment").build();
                Wrapper.WrapperMessage message = Wrapper.WrapperMessage.newBuilder().setAddEffectListenerRequest(request).build();

                System.out.println("DOIN");
                message.writeDelimitedTo(socket.getOutputStream());
                System.out.println("DONE");



            } catch (Exception e) {
                e.printStackTrace();
            }

        }).start();

        Socket client = serverSocket.accept();

        System.out.println("CONNECTED");



        Wrapper.WrapperMessage response = Wrapper.WrapperMessage.parseDelimitedFrom(client.getInputStream());
        System.out.println("BLA");
        System.out.println(response.getAddEffectListenerRequest().getEnvironment());

    }

}
