package org.iota.ict.ixi;

import com.google.protobuf.ByteString;
import org.iota.ict.ixi.protobuf.Model;
import org.iota.ict.ixi.protobuf.Request;
import org.iota.ict.ixi.protobuf.Wrapper;
import org.iota.ict.ixi.util.Constants;
import org.iota.ict.model.transaction.Transaction;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.util.Set;

public class TestSubmitTransactionBuilderRequest extends TestTemplate {

    @Test
    public void testSubmitTransactionBuilderRequest() throws IOException, InterruptedException {

        // register external module to bridge
        Socket socket = new Socket("127.0.0.1", Constants.DEFAULT_BRIDGE_PORT);

        Model.TransactionBuilder transactionBuilder = Model.TransactionBuilder.newBuilder()
                .setSignatureFragments("A")
                .setExtraDataDigest("B")
                .setAddress("TRANSACTION9BUILDER9TEST999999999999999999999999999999999999999999999999999999999")
                .setValue(ByteString.copyFrom(new BigInteger("1").toByteArray()))
                .setIssuanceTimestamp(2)
                .setTimelockLowerBound(3)
                .setTimelockUpperBound(4)
                .setBundleNonce("D")
                .setTrunkHash("E")
                .setBranchHash("F")
                .setTag("G")
                .setAttachmentTimestamp(5)
                .setAttachmentTimestampLowerBound(6)
                .setAttachmentTimestampUpperBound(7)
                .setIsBundleHead(true)
                .setIsBundleTail(true)
                .build();

        Request.SubmitTransactionBuilderRequest request = Request.SubmitTransactionBuilderRequest.newBuilder().setTransactionBuilder(transactionBuilder).build();
        Wrapper.WrapperMessage message = Wrapper.WrapperMessage.newBuilder()
                .setMessageType(Wrapper.WrapperMessage.MessageType.SUBMIT_TRANSACTION_BUILDER_REQUEST)
                .setSubmitTransactionBuilderRequest(request)
                .build();

        message.writeDelimitedTo(socket.getOutputStream());

        Thread.sleep(3000);

        Set<Transaction> transactions = ict2.findTransactionsByAddress("TRANSACTION9BUILDER9TEST999999999999999999999999999999999999999999999999999999999");

        Assert.assertEquals(1, transactions.size());




    }

}
