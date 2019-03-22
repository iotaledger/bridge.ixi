package org.iota.ict.ixi.requests;

import com.google.protobuf.ByteString;
import org.iota.ict.ixi.ClientHandler;
import org.iota.ict.ixi.protobuf.Model;
import org.iota.ict.ixi.protobuf.Request;
import org.iota.ict.ixi.protobuf.Response;
import org.iota.ict.ixi.protobuf.Wrapper;
import org.iota.ict.model.transaction.Transaction;

import java.io.IOException;

public class ProcessFindTransactionByHashRequest {

    public static void process(Request.FindTransactionByHashRequest request, ClientHandler clientHandler) throws IOException {

        Transaction transaction = clientHandler.getIxi().findTransactionByHash(request.getHash());

        Model.Transaction ret = Model.Transaction.newBuilder()
                .setHash(request.getHash())
                .setSignatureFragments(transaction.signatureFragments())
                .setExtraDataDigest(transaction.extraDataDigest())
                .setAddress(transaction.address())
                .setValue(ByteString.copyFrom(transaction.value.toByteArray()))
                .setIssuanceTimestamp(transaction.issuanceTimestamp)
                .setTimelockLowerBound(transaction.timelockLowerBound)
                .setTimelockUpperBound(transaction.timelockUpperBound)
                .setBundleNonce(transaction.bundleNonce())
                .setTrunkHash(transaction.trunkHash())
                .setBranchHash(transaction.branchHash())
                .setTag(transaction.tag())
                .setAttachmentTimestamp(transaction.attachmentTimestamp)
                .setAttachmentTimestampLowerBound(transaction.attachmentTimestampLowerBound)
                .setAttachmentTimestampUpperBound(transaction.attachmentTimestampUpperBound)
                .setNonce(transaction.nonce())
                .setDecodedSignatureFragments(transaction.decodedSignatureFragments())
                .setEssence(transaction.essence())
                .setIsBundleHead(transaction.isBundleHead)
                .setIsBundleTail(transaction.isBundleTail)
                .build();

        Response.FindTransactionByHashResponse response = Response.FindTransactionByHashResponse.newBuilder().setTransaction(ret).build();

        Wrapper.WrapperMessage wrapperMessage = Wrapper.WrapperMessage.newBuilder()
                .setMessageType(Wrapper.WrapperMessage.MessageType.FIND_TRANSACTION_BY_HASH_RESPONSE)
                .setFindTransactionByHashResponse(response)
                .build();

        wrapperMessage.writeDelimitedTo(clientHandler.getOutputStream());

    }

}
