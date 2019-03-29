package org.iota.ict.ixi;

import org.iota.ict.ixi.protobuf.Model;
import org.iota.ict.ixi.protobuf.Request;
import org.iota.ict.ixi.protobuf.Wrapper;
import org.iota.ict.model.transaction.Transaction;
import org.iota.ict.model.transaction.TransactionBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class TestFindTransactionByAddressRequest extends TestTemplate {

    @Test
    public void testFindTransactionByAddressRequest() throws IOException, InterruptedException {

        // address to test
        String address = "ADDRESS9TEST999999999999999999999999999999999999999999999999999999999999999999999";

        // send transaction from Ict2 to Ict1
        TransactionBuilder transactionBuilder1 = new TransactionBuilder();
        TransactionBuilder transactionBuilder2 = new TransactionBuilder();
        transactionBuilder1.address = address;
        transactionBuilder2.address = address;

        Transaction transaction1 = transactionBuilder1.build();
        Transaction transaction2 = transactionBuilder2.build();

        ict2.submit(transaction1);
        ict2.submit(transaction2);

        Thread.sleep(1000);

        // request transaction by address
        Request.FindTransactionsByAddressRequest findTransactionsByAddressRequest = Request.FindTransactionsByAddressRequest.newBuilder().setAddress(address).build();
        Wrapper.WrapperMessage findTransactionsByAddressWrapper = Wrapper.WrapperMessage.newBuilder()
                .setMessageType(Wrapper.WrapperMessage.MessageType.FIND_TRANSACTIONS_BY_ADDRESS_REQUEST)
                .setFindTransactionsByAddressRequest(findTransactionsByAddressRequest)
                .build();

        sendMessage(findTransactionsByAddressWrapper);

        // read response from bridge
        Wrapper.WrapperMessage response = readMessage();
        List<Model.Transaction> transactions = response.getFindTransactionsByAddressResponse().getTransactionList();

        Assert.assertEquals(2, transactions.size());

    }

}
