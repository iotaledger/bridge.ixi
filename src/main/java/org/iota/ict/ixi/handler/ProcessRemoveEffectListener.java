package org.iota.ict.ixi.handler;

import org.iota.ict.ixi.protobuf.Request;

public class ProcessRemoveEffectListener {

    public static void process(Request.RemoveEffectListenerRequest request, ClientHandler clientHandler) {

        clientHandler.removeEffectListener(request.getEnvironment());

    }

}
