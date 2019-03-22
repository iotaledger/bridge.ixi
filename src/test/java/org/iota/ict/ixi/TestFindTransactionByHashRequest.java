package org.iota.ict.ixi;

import org.iota.ict.Ict;
import org.iota.ict.ixi.protobuf.Model;
import org.iota.ict.ixi.protobuf.Request;
import org.iota.ict.ixi.protobuf.Wrapper;
import org.iota.ict.ixi.util.Constants;
import org.iota.ict.model.transaction.Transaction;
import org.iota.ict.model.transaction.TransactionBuilder;
import org.iota.ict.utils.properties.EditableProperties;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.Socket;
import java.util.Set;

public class TestFindTransactionByHashRequest {

    @Test
    public void testFindTransactionByHashRequest() throws IOException, InterruptedException {

        // create two Ict instances
        EditableProperties properties1 = new EditableProperties().host("localhost").port(1337).minForwardDelay(0).maxForwardDelay(10).guiEnabled(false);
        Ict ict1 = new Ict(properties1.toFinal());

        EditableProperties properties2 = new EditableProperties().host("localhost").port(1338).minForwardDelay(0).maxForwardDelay(10).guiEnabled(false);
        Ict ict2 = new Ict(properties2.toFinal());

        addNeighborToIct(ict1,ict2);
        addNeighborToIct(ict2,ict1);

        // register bridge module to Ict1
        Bridge bridge = new Bridge(ict1);
        new Thread(() -> bridge.run()).start();

        // send transaction from Ict2 to Ict1
        Transaction transaction = new TransactionBuilder().build();
        ict2.submit(transaction);
        Thread.sleep(500);

        // register external module to bridge
        Socket socket = new Socket("127.0.0.1", Constants.DEFAULT_BRIDGE_PORT);

        // request transaction by hash
        Request.FindTransactionByHashRequest request = Request.FindTransactionByHashRequest.newBuilder().setHash(transaction.hash).build();
        Wrapper.WrapperMessage message = Wrapper.WrapperMessage.newBuilder()
                .setMessageType(Wrapper.WrapperMessage.MessageType.FIND_TRANSACTION_BY_HASH_REQUEST)
                .setFindTransactionByHashRequest(request)
                .build();

        message.writeDelimitedTo(socket.getOutputStream());

        // read response from bridge
        Wrapper.WrapperMessage response = Wrapper.WrapperMessage.parseDelimitedFrom(socket.getInputStream());
        Model.Transaction protobuf = response.getFindTransactionByHashResponse().getTransaction();

        Assert.assertEquals(transaction.hash, protobuf.getHash());

        ict1.terminate();
        ict2.terminate();

    }

    private static void addNeighborToIct(Ict ict, Ict neighbor) {
        EditableProperties properties = ict.getProperties().toEditable();
        Set<String> neighbors = properties.neighbors();
        neighbors.add(neighbor.getAddress().getHostName() + ":" + neighbor.getAddress().getPort());
        properties.neighbors(neighbors);
        ict.updateProperties(properties.toFinal());
    }

}
