package org.iota.ict.ixi.handler;

import com.google.protobuf.ByteString;
import org.iota.ict.ixi.protobuf.Model;
import org.iota.ict.ixi.protobuf.Request;
import org.iota.ict.ixi.protobuf.Response;
import org.iota.ict.ixi.protobuf.Wrapper;
import org.iota.ict.model.transaction.Transaction;

import java.io.IOException;

public class ProcessFindTransactionByHash {

    public static void process(Request.FindTransactionByHashRequest request, ClientHandler clientHandler) throws IOException {

        Transaction transaction = clientHandler.getIxi().findTransactionByHash(request.getHash());

        if(transaction == null) {
            Wrapper.WrapperMessage wrapperMessage = Wrapper.WrapperMessage.newBuilder()
                    .setMessageType(Wrapper.WrapperMessage.MessageType.FIND_TRANSACTION_BY_HASH_RESPONSE)
                    .build();
            clientHandler.getOutputStream().writeInt(wrapperMessage.toByteArray().length);
            wrapperMessage.writeTo(clientHandler.getOutputStream());
            return;
        }

        Model.Transaction ret = Model.Transaction.newBuilder()
                .setHash(transaction.hash)
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

        clientHandler.getOutputStream().writeInt(wrapperMessage.toByteArray().length);
        wrapperMessage.writeTo(clientHandler.getOutputStream());

    }

}
