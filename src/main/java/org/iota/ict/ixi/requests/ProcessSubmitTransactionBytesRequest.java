package org.iota.ict.ixi.requests;

import org.iota.ict.ixi.ClientHandler;
import org.iota.ict.ixi.protobuf.Request;
import org.iota.ict.model.transaction.Transaction;

public class ProcessSubmitTransactionBytesRequest {

    public static void process(Request.SubmitTransactionBytesRequest request, ClientHandler clientHandler) {

        Transaction transaction = new Transaction(request.getTransactionBytes().toByteArray());
        clientHandler.getIxi().submit(transaction);

    }

}
