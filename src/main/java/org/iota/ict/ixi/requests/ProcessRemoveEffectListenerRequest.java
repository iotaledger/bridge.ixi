package org.iota.ict.ixi.requests;

import org.iota.ict.ixi.ClientHandler;
import org.iota.ict.ixi.protobuf.Request;

public class ProcessRemoveEffectListenerRequest {

    public static void process(Request.RemoveEffectListenerRequest request, ClientHandler clientHandler) {

        clientHandler.removeEffectListener(request.getEnvironment());

    }

}
