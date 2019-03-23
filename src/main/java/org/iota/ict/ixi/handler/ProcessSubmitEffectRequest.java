package org.iota.ict.ixi.handler;

import org.iota.ict.eee.Environment;
import org.iota.ict.ixi.protobuf.Request;

public class ProcessSubmitEffectRequest {

    public static void process(Request.SubmitEffectRequest request, ClientHandler clientHandler) {

        String effect = request.getEffect();
        String environment = request.getEnvironment();

        clientHandler.getIxi().submitEffect(new Environment(environment), effect);

    }

}
