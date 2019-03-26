package org.iota.ict.ixi.handler;

import org.iota.ict.ixi.protobuf.Request;
import org.iota.ict.ixi.protobuf.Response;
import org.iota.ict.ixi.protobuf.Wrapper;

import java.io.IOException;

public class ProcessPollEffectRequest {

    public static void process(Request.PollEffectRequest request, ClientHandler clientHandler) throws IOException {

        String effect = clientHandler.getEffect(request.getEnvironment());

        if(effect == null) {
            Wrapper.WrapperMessage wrapperMessage = Wrapper.WrapperMessage.newBuilder()
                    .setMessageType(Wrapper.WrapperMessage.MessageType.POLL_EFFECT_REQUEST)
                    .build();
            wrapperMessage.writeDelimitedTo(clientHandler.getOutputStream());
            return;
        }

        Response.PollEffectResponse.Builder responseBuilder = Response.PollEffectResponse.newBuilder();
        responseBuilder.setEffect(effect);

        Wrapper.WrapperMessage wrapperMessage = Wrapper.WrapperMessage.newBuilder()
                .setMessageType(Wrapper.WrapperMessage.MessageType.POLL_EFFECT_REQUEST)
                .setPollEffectResponse(responseBuilder)
                .build();
        wrapperMessage.writeDelimitedTo(clientHandler.getOutputStream());

    }

}
