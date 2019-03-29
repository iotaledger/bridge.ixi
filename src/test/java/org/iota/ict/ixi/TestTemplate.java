package org.iota.ict.ixi;

import org.iota.ict.Ict;
import org.iota.ict.ixi.protobuf.Wrapper;
import org.iota.ict.ixi.util.Constants;
import org.iota.ict.utils.properties.EditableProperties;
import org.junit.After;
import org.junit.Before;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Set;

public abstract class TestTemplate {

    protected Ict ict1;
    protected Ict ict2;

    private Socket bridgeClient;
    protected DataInputStream inputStream;
    protected DataOutputStream outputStream;

    @Before
    public void setup() throws Throwable {
        EditableProperties properties1 = new EditableProperties().host("localhost").port(1337).minForwardDelay(0).maxForwardDelay(10).guiEnabled(false);
        ict1 = new Ict(properties1.toFinal());
        EditableProperties properties2 = new EditableProperties().host("localhost").port(1338).minForwardDelay(0).maxForwardDelay(10).guiEnabled(false);
        ict2 = new Ict(properties2.toFinal());
        addNeighborToIct(ict1,ict2);
        addNeighborToIct(ict2,ict1);
        ict1.getModuleHolder().loadVirtualModule(Bridge.class, "Bridge.ixi");
        ict1.getModuleHolder().startAllModules();
        bridgeClient = new Socket("127.0.0.1", Constants.DEFAULT_BRIDGE_PORT);
        inputStream = new DataInputStream(bridgeClient.getInputStream());
        outputStream = new DataOutputStream(bridgeClient.getOutputStream());
    }

    @After
    public void tearDown() {
        ict1.terminate();
        ict2.terminate();
        try { bridgeClient.close(); } catch (Exception e) { ; }
    }

    private static void addNeighborToIct(Ict ict, Ict neighbor) {
        EditableProperties properties = ict.getProperties().toEditable();
        Set<String> neighbors = properties.neighbors();
        neighbors.add(neighbor.getAddress().getHostName() + ":" + neighbor.getAddress().getPort());
        properties.neighbors(neighbors);
        ict.updateProperties(properties.toFinal());
    }

    public  Wrapper.WrapperMessage readMessage() throws IOException {
        int bufferLength = inputStream.readInt();
        byte[] buffer = new byte[bufferLength];
        inputStream.readFully(buffer, 0, bufferLength);
        return Wrapper.WrapperMessage.parseFrom(buffer);
    }

    public void sendMessage(Wrapper.WrapperMessage message) throws IOException {
        outputStream.writeInt(message.toByteArray().length);
        message.writeTo(outputStream);
    }

}
