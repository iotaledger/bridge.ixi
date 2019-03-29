package org.iota.ict.ixi.handler;

import org.iota.ict.Ict;
import org.iota.ict.eee.EffectListenerQueue;
import org.iota.ict.eee.Environment;
import org.iota.ict.ixi.Ixi;
import org.iota.ict.ixi.protobuf.Wrapper;
import org.iota.ict.utils.RestartableThread;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Thread which handles incoming connections on the given socket.
 */
public class ClientHandler extends RestartableThread {

    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private Ixi ixi;
    private Map<String, EffectListenerQueue<String>> effectListenerByEnvironment = new HashMap<>();

    public ClientHandler(Socket socket, Ixi ixi) throws IOException {
        super(null);
        this.socket = socket;
        this.inputStream = new DataInputStream(socket.getInputStream());
        this.outputStream = new DataOutputStream(socket.getOutputStream());
        this.ixi = ixi;
        start();
    }

    @Override
    public void run() {

        while(true) {

            Wrapper.WrapperMessage request;

            try {

                int bufferLength = inputStream.readInt();
                byte[] buffer = new byte[bufferLength];
                inputStream.readFully(buffer, 0, bufferLength);
                request = Wrapper.WrapperMessage.parseFrom(buffer);

                if(request == null)
                    throw new Exception();

            } catch (Throwable t) {
                t.printStackTrace();
                Ict.LOGGER.info("[Bridge.ixi] Terminating client...");
                try { socket.close(); } catch (Exception x) { ; }
                return;
            }

            Wrapper.WrapperMessage.MessageType type = request.getMessageType();

            System.out.println(type.name());

            try {

                switch (type) {

                    case FIND_TRANSACTIONS_BY_ADDRESS_REQUEST: {
                        ProcessFindTransactionsByAddress.process(request.getFindTransactionsByAddressRequest(), this);
                        break;
                    }

                    case FIND_TRANSACTIONS_BY_TAG_REQUEST: {
                        ProcessFindTransactionsByTag.process(request.getFindTransactionsByTagRequest(), this);
                        break;
                    }

                    case FIND_TRANSACTION_BY_HASH_REQUEST: {
                        ProcessFindTransactionByHash.process(request.getFindTransactionByHashRequest(), this);
                        break;
                    }

                    case SUBMIT_TRANSACTION_BUILDER_REQUEST: {
                        ProcessSubmitTransactionBuilder.process(request.getSubmitTransactionBuilderRequest(), this);
                        break;
                    }

                    case SUBMIT_TRANSACTION_BYTES_REQUEST: {
                        ProcessSubmitTransactionBytes.process(request.getSubmitTransactionBytesRequest(), this);
                        break;
                    }

                    case ADD_EFFECT_LISTENER_REQUEST: {
                        ProcessAddEffectListener.process(request.getAddEffectListenerRequest(), this);
                        break;
                    }

                    case REMOVE_EFFECT_LISTENER_REQUEST: {
                        ProcessRemoveEffectListener.process(request.getRemoveEffectListenerRequest(), this);
                        break;
                    }

                    case SUBMIT_EFFECT_REQUEST: {
                        ProcessSubmitEffect.process(request.getSubmitEffectRequest(), this);
                        break;
                    }

                    case POLL_EFFECT_REQUEST: {
                        ProcessPollEffect.process(request.getPollEffectRequest(), this);
                        break;
                    }

                    case TAKE_EFFECT_REQUEST: {
                        ProcessTakeEffect.process(request.getTakeEffectRequest(), this);
                        break;
                    }

                }

            } catch (Throwable t) {
                Ict.LOGGER.info("[Bridge.ixi] Error while processing client: " + t.getMessage());
                try { socket.close(); } catch (Exception x) { ; }
                return;
            }

        }

    }

    @Override
    public void onTerminate() {
        try { socket.close(); } catch (Exception x) { ; }
    }

    public DataOutputStream getOutputStream() {
        return outputStream;
    }

    public Ixi getIxi() {
        return ixi;
    }

    public void addEffectListener(String environment) {
        EffectListenerQueue<String> effectListener = new EffectListenerQueue<>(new Environment(environment));
        ixi.addListener(effectListener);
        effectListenerByEnvironment.put(environment, effectListener);
    }

    public void removeEffectListener(String environment) {
        ixi.removeListener(effectListenerByEnvironment.get(environment));
        effectListenerByEnvironment.remove(environment);
    }

    public String pollEffect(String environment) {
        return effectListenerByEnvironment.get(environment).pollEffect();
    }

    public String takeEffect(String environment) throws InterruptedException {
        return effectListenerByEnvironment.get(environment).takeEffect();
    }

}