package org.iota.ict.ixi;

import org.iota.ict.ixi.protobuf.Request;
import org.iota.ict.ixi.protobuf.Wrapper;
import org.junit.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ProtobufTest {

    @Test
    public void testSerializationAndDeserializationOverSocket() {

        final int port = 20324;
        ServerSocket serverSocket = null;

        try {

            serverSocket = new ServerSocket(port);

            new Thread(() -> {

                Socket socket = null;

                try {

                    Thread.sleep(500);
                    socket  = new Socket("127.0.0.1", port);

                    Request.AddEffectListenerRequest request = Request.AddEffectListenerRequest.newBuilder().setEnvironment("TestEnvironment").build();
                    Wrapper.WrapperMessage message = Wrapper.WrapperMessage.newBuilder().setAddEffectListenerRequest(request).build();

                    message.writeDelimitedTo(socket.getOutputStream());

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try { socket.close(); } catch (Exception x) { ; }
                }

            }).start();

            Socket client = serverSocket.accept();

            Wrapper.WrapperMessage response = Wrapper.WrapperMessage.parseDelimitedFrom(client.getInputStream());

            Assert.assertTrue(response.hasAddEffectListenerRequest());
            Assert.assertEquals("TestEnvironment", response.getAddEffectListenerRequest().getEnvironment());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { serverSocket.close(); } catch (Exception x) { ; }
        }

    }

    @Test
    public void testMultipleSerializationAndDeserializationOverSocket() {

        final int port = 20324;
        ServerSocket serverSocket = null;

        try {

            serverSocket = new ServerSocket(port);

            new Thread(() -> {

                Socket socket = null;

                try {

                    Thread.sleep(500);
                    socket  = new Socket("127.0.0.1", port);

                    Request.AddEffectListenerRequest request1 = Request.AddEffectListenerRequest.newBuilder().setEnvironment("TestEnvironment").build();
                    Wrapper.WrapperMessage message1 = Wrapper.WrapperMessage.newBuilder().setAddEffectListenerRequest(request1).build();

                    message1.writeDelimitedTo(socket.getOutputStream());

                    Request.SubmitEffectRequest request2 = Request.SubmitEffectRequest.newBuilder().setEffect("TestEffect").setEnvironment("TestEnvironment").build();
                    Wrapper.WrapperMessage message2 = Wrapper.WrapperMessage.newBuilder().setSubmitEffectRequest(request2).build();

                    message2.writeDelimitedTo(socket.getOutputStream());

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try { socket.close(); } catch (Exception x) { ; }
                }

            }).start();

            Socket client = serverSocket.accept();

            Wrapper.WrapperMessage response1 = Wrapper.WrapperMessage.parseDelimitedFrom(client.getInputStream());

            Assert.assertTrue(response1.hasAddEffectListenerRequest());
            Assert.assertEquals("TestEnvironment", response1.getAddEffectListenerRequest().getEnvironment());

            Wrapper.WrapperMessage response2 = Wrapper.WrapperMessage.parseDelimitedFrom(client.getInputStream());

            Assert.assertTrue(response2.hasSubmitEffectRequest());
            Assert.assertEquals("TestEnvironment", response2.getSubmitEffectRequest().getEnvironment());
            Assert.assertEquals("TestEffect", response2.getSubmitEffectRequest().getEffect());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { serverSocket.close(); } catch (Exception x) { ; }
        }

    }

}
