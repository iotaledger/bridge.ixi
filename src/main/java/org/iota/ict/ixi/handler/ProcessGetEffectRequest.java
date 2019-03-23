package org.iota.ict.ixi.handler;

import org.iota.ict.ixi.protobuf.Request;
import org.iota.ict.ixi.protobuf.Response;
import org.iota.ict.ixi.protobuf.Wrapper;

import java.io.IOException;

public class ProcessGetEffectRequest {

    public static void process(Request.GetEffectRequest request, ClientHandler clientHandler) throws IOException {

        String effect = clientHandler.getEffect(request.getEnvironment());

        if(effect == null) {
            Wrapper.WrapperMessage wrapperMessage = Wrapper.WrapperMessage.newBuilder()
                    .setMessageType(Wrapper.WrapperMessage.MessageType.GET_EFFECT_RESPONSE)
                    .build();
            wrapperMessage.writeDelimitedTo(clientHandler.getOutputStream());
            return;
        }

        Response.GetEffectResponse.Builder responseBuilder = Response.GetEffectResponse.newBuilder();
        responseBuilder.setEffect(effect);

        Wrapper.WrapperMessage wrapperMessage = Wrapper.WrapperMessage.newBuilder()
                .setMessageType(Wrapper.WrapperMessage.MessageType.GET_EFFECT_RESPONSE)
                .setGetEffectResponse(responseBuilder)
                .build();
        wrapperMessage.writeDelimitedTo(clientHandler.getOutputStream());

    }

}
