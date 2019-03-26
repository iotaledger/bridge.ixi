package org.iota.ict.ixi.handler;

import org.iota.ict.eee.EffectListenerQueue;
import org.iota.ict.ixi.Ixi;
import org.iota.ict.ixi.protobuf.Wrapper;
import org.iota.ict.utils.RestartableThread;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Thread which handles incoming connections on the given socket.
 */
public class ClientHandler extends RestartableThread {

    private Socket socket;
    private Ixi ixi;
    private Map<String, EffectListenerQueue<String>> effectListenerByEnvironment = new HashMap<>();

    public ClientHandler(Socket socket, Ixi ixi) {
        super(null);
        this.socket = socket;
        this.ixi = ixi;
        start();
    }

    @Override
    public void run() {

        try {

            while (isRunning()) {

                Wrapper.WrapperMessage request = Wrapper.WrapperMessage.parseDelimitedFrom(socket.getInputStream());
                Wrapper.WrapperMessage.MessageType type = request.getMessageType();

                switch (type) {

                    case FIND_TRANSACTIONS_BY_ADDRESS_REQUEST: {
                        ProcessFindTransactionsByAddressRequest.process(request.getFindTransactionsByAddressRequest(), this);
                        break;
                    }

                    case FIND_TRANSACTIONS_BY_TAG_REQUEST: {
                        ProcessFindTransactionsByTagRequest.process(request.getFindTransactionsByTagRequest(), this);
                        break;
                    }

                    case FIND_TRANSACTION_BY_HASH_REQUEST: {
                        ProcessFindTransactionByHashRequest.process(request.getFindTransactionByHashRequest(), this);
                        break;
                    }

                    case SUBMIT_TRANSACTION_BUILDER_REQUEST: {
                        ProcessSubmitTransactionBuilderRequest.process(request.getSubmitTransactionBuilderRequest(), this);
                        break;
                    }

                    case SUBMIT_TRANSACTION_BYTES_REQUEST: {
                        ProcessSubmitTransactionBytesRequest.process(request.getSubmitTransactionBytesRequest(), this);
                        break;
                    }

                    case ADD_EFFECT_LISTENER_REQUEST: {
                        ProcessAddEffectListenerRequest.process(request.getAddEffectListenerRequest(), this);
                        break;
                    }

                    case REMOVE_EFFECT_LISTENER_REQUEST: {
                        ProcessRemoveEffectListenerRequest.process(request.getRemoveEffectListenerRequest(), this);
                        break;
                    }

                    case SUBMIT_EFFECT_REQUEST: {
                        ProcessSubmitEffectRequest.process(request.getSubmitEffectRequest(), this);
                        break;
                    }

                    case POLL_EFFECT_REQUEST: {
                        ProcessPollEffectRequest.process(request.getPollEffectRequest(), this);
                        break;
                    }

                }

            }

        } catch (IOException e) {
            System.err.println("Error while processing client.");
            e.printStackTrace();
        }
    }

    @Override
    public void onTerminate() {
        try { socket.close(); } catch (Exception x) { ; }
    }

    public OutputStream getOutputStream() throws IOException {
        return socket.getOutputStream();
    }

    public Ixi getIxi() {
        return ixi;
    }

    public void addEffectListener(EffectListenerQueue<String> effectListener) {
        ixi.addListener(effectListener);
        effectListenerByEnvironment.put(effectListener.getEnvironment().toString(), effectListener);
    }

    public void removeEffectListener(String environment) {
        ixi.removeListener(effectListenerByEnvironment.get(environment));
        effectListenerByEnvironment.remove(environment);
    }

    public String getEffect(String environment) {
        return effectListenerByEnvironment.get(environment).pollEffect();
    }

}