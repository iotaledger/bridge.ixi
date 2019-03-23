package org.iota.ict.ixi.handler;

import org.iota.ict.ixi.protobuf.Model;
import org.iota.ict.ixi.protobuf.Request;
import org.iota.ict.model.transaction.Transaction;
import org.iota.ict.model.transaction.TransactionBuilder;

import java.math.BigInteger;

public class ProcessSubmitTransactionBuilderRequest {

    public static void process(Request.SubmitTransactionBuilderRequest request, ClientHandler clientHandler) {

        Model.TransactionBuilder protoBuf = request.getTransactionBuilder();

        TransactionBuilder transactionBuilder = new TransactionBuilder();
        transactionBuilder.signatureFragments = protoBuf.getSignatureFragments();
        transactionBuilder.extraDataDigest = protoBuf.getExtraDataDigest();
        transactionBuilder.address = protoBuf.getAddress();
        transactionBuilder.value = new BigInteger(protoBuf.getValue().toByteArray());
        transactionBuilder.issuanceTimestamp = protoBuf.getIssuanceTimestamp();
        transactionBuilder.timelockLowerBound = protoBuf.getTimelockLowerBound();
        transactionBuilder.timelockUpperBound = protoBuf.getTimelockUpperBound();
        transactionBuilder.bundleNonce = protoBuf.getBundleNonce();
        transactionBuilder.trunkHash = protoBuf.getTrunkHash();
        transactionBuilder.branchHash = protoBuf.getBranchHash();
        transactionBuilder.tag = protoBuf.getTag();
        transactionBuilder.attachmentTimestamp = protoBuf.getAttachmentTimestamp();
        transactionBuilder.attachmentTimestampLowerBound = protoBuf.getAttachmentTimestampLowerBound();
        transactionBuilder.attachmentTimestampUpperBound = protoBuf.getAttachmentTimestampUpperBound();
        transactionBuilder.isBundleHead = protoBuf.getIsBundleHead();
        transactionBuilder.isBundleTail = protoBuf.getIsBundleTail();

        Transaction transaction = transactionBuilder.build();
        clientHandler.getIxi().submit(transaction);

    }

}
