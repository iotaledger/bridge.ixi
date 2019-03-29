package org.iota.ict.ixi.handler;

import org.iota.ict.ixi.protobuf.Request;

public class ProcessAddEffectListener {

    public static void process(Request.AddEffectListenerRequest request, ClientHandler clientHandler) {

        clientHandler.addEffectListener(request.getEnvironment());

    }

}
