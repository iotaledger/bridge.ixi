package org.iota.ict.ixi.handler;

import org.iota.ict.ixi.protobuf.Request;
import org.iota.ict.ixi.protobuf.Response;
import org.iota.ict.ixi.protobuf.Wrapper;

import java.io.IOException;

public class ProcessTakeEffect {

    public static void process(Request.TakeEffectRequest request, ClientHandler clientHandler) throws IOException, InterruptedException {

        String effect = clientHandler.takeEffect(request.getEnvironment());

        if(effect == null) {
            Wrapper.WrapperMessage wrapperMessage = Wrapper.WrapperMessage.newBuilder()
                    .setMessageType(Wrapper.WrapperMessage.MessageType.POLL_EFFECT_REQUEST)
                    .build();
            clientHandler.getOutputStream().writeInt(wrapperMessage.toByteArray().length);
            wrapperMessage.writeTo(clientHandler.getOutputStream());
            return;
        }

        Response.TakeEffectResponse.Builder responseBuilder = Response.TakeEffectResponse.newBuilder();
        responseBuilder.setEffect(effect);

        Wrapper.WrapperMessage wrapperMessage = Wrapper.WrapperMessage.newBuilder()
                .setMessageType(Wrapper.WrapperMessage.MessageType.TAKE_EFFECT_RESPONSE)
                .setTakeEffectResponse(responseBuilder)
                .build();

        clientHandler.getOutputStream().writeInt(wrapperMessage.toByteArray().length);
        wrapperMessage.writeTo(clientHandler.getOutputStream());

    }

}
