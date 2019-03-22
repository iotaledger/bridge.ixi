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

        long issuanceTimestamp = System.currentTimeMillis();
        Model.TransactionBuilder transactionBuilder = Model.TransactionBuilder.newBuilder()
                .setSignatureFragments("A")
                .setExtraDataDigest("B")
                .setAddress("TRANSACTION9BUILDER9TEST")
                .setValue(ByteString.copyFrom(new BigInteger("1").toByteArray()))
                .setIssuanceTimestamp(issuanceTimestamp)
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

        Thread.sleep(1000);

        Set<Transaction> transactions = ict1.findTransactionsByAddress("TRANSACTION9BUILDER9TEST999999999999999999999999999999999999999999999999999999999");

        Assert.assertEquals(1, transactions.size());

        Transaction transaction = transactions.iterator().next();

        Assert.assertEquals("A99999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999", transaction.signatureFragments());
        Assert.assertEquals("B99999999999999999999999999999999999999999999999999999999999999999999999999999999", transaction.extraDataDigest());
        Assert.assertEquals(1, transaction.value.intValue());
        Assert.assertEquals(issuanceTimestamp, transaction.issuanceTimestamp);
        Assert.assertEquals(3, transaction.timelockLowerBound);
        Assert.assertEquals(4, transaction.timelockUpperBound);
        Assert.assertEquals("D99999999999999999999999999", transaction.bundleNonce());
        Assert.assertEquals("E99999999999999999999999999999999999999999999999999999999999999999999999999999999", transaction.trunkHash());
        Assert.assertEquals("F99999999999999999999999999999999999999999999999999999999999999999999999999999999", transaction.branchHash());
        Assert.assertEquals("G99999999999999999999999999", transaction.tag());
        Assert.assertEquals(5, transaction.attachmentTimestamp);
        Assert.assertEquals(6, transaction.attachmentTimestampLowerBound);
        Assert.assertEquals(7, transaction.attachmentTimestampUpperBound);
        Assert.assertTrue(transaction.isBundleHead);
        Assert.assertTrue( transaction.isBundleTail);

    }

}
