package org.iota.ict.ixi.requests;

import org.iota.ict.ixi.Client;
import org.iota.ict.ixi.protobuf.Request;
import org.iota.ict.ixi.protobuf.Response;
import org.iota.ict.ixi.protobuf.Wrapper;

import java.io.IOException;

public class ProcessDetermineApprovalConfidenceRequest {

    public static void process(Request.DetermineApprovalConfidenceRequest request, Client clientHandler) throws IOException {

        double approvalConfidence = clientHandler.getIxi().determineApprovalConfidence(request.getTransactionHash());

        Response.DetermineApprovalConfidenceResponse response = Response.DetermineApprovalConfidenceResponse.newBuilder().setApprovalConfidence(approvalConfidence).build();

        Wrapper.WrapperMessage wrapperMessage = Wrapper.WrapperMessage.newBuilder()
                .setMessageType(Wrapper.WrapperMessage.MessageType.DETERMINE_APPROVAL_CONFIDENCE_RESPONSE)
                .setDetermineApprovalConfidenceResponse(response)
                .build();

        wrapperMessage.writeDelimitedTo(clientHandler.getOutputStream());

    }

}
