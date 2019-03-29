package org.iota.ict.ixi;

import com.google.protobuf.ByteString;
import org.iota.ict.ixi.protobuf.Model;
import org.iota.ict.ixi.protobuf.Request;
import org.iota.ict.ixi.protobuf.Wrapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

public class TestSubmitTransactionBuilderRequest extends TestTemplate {

    @Test
    public void testSubmitTransactionBuilderRequest() throws IOException {

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

        Request.SubmitTransactionBuilderRequest submitTransactionBuilderRequest = Request.SubmitTransactionBuilderRequest.newBuilder().setTransactionBuilder(transactionBuilder).build();
        Wrapper.WrapperMessage submitTransactionBuilderRequestWrapper = Wrapper.WrapperMessage.newBuilder()
                .setMessageType(Wrapper.WrapperMessage.MessageType.SUBMIT_TRANSACTION_BUILDER_REQUEST)
                .setSubmitTransactionBuilderRequest(submitTransactionBuilderRequest)
                .build();

        sendMessage(submitTransactionBuilderRequestWrapper);

        Request.FindTransactionsByAddressRequest findTransactionsByAddressRequest = Request.FindTransactionsByAddressRequest.newBuilder().setAddress("TRANSACTION9BUILDER9TEST999999999999999999999999999999999999999999999999999999999").build();
        Wrapper.WrapperMessage findTransactionsByAddressRequestWrapper = Wrapper.WrapperMessage.newBuilder()
                .setMessageType(Wrapper.WrapperMessage.MessageType.FIND_TRANSACTIONS_BY_ADDRESS_REQUEST)
                .setFindTransactionsByAddressRequest(findTransactionsByAddressRequest)
                .build();

        sendMessage(findTransactionsByAddressRequestWrapper);

        Wrapper.WrapperMessage response = readMessage();
        List<Model.Transaction> transactionList = response.getFindTransactionsByAddressResponse().getTransactionList();
        Assert.assertEquals(1, transactionList.size());

        Model.Transaction transaction = transactionList.iterator().next();

        Assert.assertEquals("A99999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999", transaction.getSignatureFragments());
        Assert.assertEquals("B99999999999999999999999999999999999999999999999999999999999999999999999999999999", transaction.getExtraDataDigest());
        Assert.assertEquals(1, new BigInteger(transaction.getValue().toByteArray()).intValue());
        //Assert.assertEquals(issuanceTimestamp, transaction.issuanceTimestamp);
        Assert.assertEquals(3, transaction.getTimelockLowerBound());
        Assert.assertEquals(4, transaction.getTimelockUpperBound());
        Assert.assertEquals("D99999999999999999999999999", transaction.getBundleNonce());
        Assert.assertEquals("E99999999999999999999999999999999999999999999999999999999999999999999999999999999", transaction.getTrunkHash());
        Assert.assertEquals("F99999999999999999999999999999999999999999999999999999999999999999999999999999999", transaction.getBranchHash());
        Assert.assertEquals("G99999999999999999999999999", transaction.getTag());
        Assert.assertEquals(5, transaction.getAttachmentTimestamp());
        Assert.assertEquals(6, transaction.getAttachmentTimestampLowerBound());
        Assert.assertEquals(7, transaction.getAttachmentTimestampUpperBound());
        Assert.assertTrue(transaction.getIsBundleHead());
        Assert.assertTrue( transaction.getIsBundleTail());

    }

}
