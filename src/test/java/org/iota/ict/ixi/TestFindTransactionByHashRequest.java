package org.iota.ict.ixi;

import org.iota.ict.ixi.protobuf.Model;
import org.iota.ict.ixi.protobuf.Request;
import org.iota.ict.ixi.protobuf.Wrapper;
import org.iota.ict.model.transaction.Transaction;
import org.iota.ict.model.transaction.TransactionBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class TestFindTransactionByHashRequest extends TestTemplate {

    @Test
    public void testFindTransactionByHashRequest() throws IOException, InterruptedException {

        // send transaction from Ict2 to Ict1
        Transaction transaction = new TransactionBuilder().build();
        ict2.submit(transaction);
        Thread.sleep(1000);

        // request transaction by hash
        Request.FindTransactionByHashRequest findTransactionByHashRequest = Request.FindTransactionByHashRequest.newBuilder().setHash(transaction.hash).build();
        Wrapper.WrapperMessage findTransactionByHashRequestWrapper = Wrapper.WrapperMessage.newBuilder()
                .setMessageType(Wrapper.WrapperMessage.MessageType.FIND_TRANSACTION_BY_HASH_REQUEST)
                .setFindTransactionByHashRequest(findTransactionByHashRequest)
                .build();

        sendMessage(findTransactionByHashRequestWrapper);

        // read response from bridge
        Wrapper.WrapperMessage response = readMessage();
        Model.Transaction protobuf = response.getFindTransactionByHashResponse().getTransaction();

        Assert.assertEquals(transaction.hash, protobuf.getHash());

    }

}
