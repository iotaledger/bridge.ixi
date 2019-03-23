package org.iota.ict.ixi.handler;

import org.iota.ict.ixi.protobuf.Request;

public class ProcessRemoveEffectListenerRequest {

    public static void process(Request.RemoveEffectListenerRequest request, ClientHandler clientHandler) {

        clientHandler.removeEffectListener(request.getEnvironment());

    }

}
