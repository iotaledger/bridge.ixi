package org.iota.ict.ixi.handler;

import org.iota.ict.ixi.protobuf.Request;
import org.iota.ict.model.transaction.Transaction;

public class ProcessSubmitTransactionBytes {

    public static void process(Request.SubmitTransactionBytesRequest request, ClientHandler clientHandler) {

        Transaction transaction = new Transaction(request.getTransactionBytes().toByteArray());
        clientHandler.getIxi().submit(transaction);

    }

}
