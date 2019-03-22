package org.iota.ict.ixi;

import org.iota.ict.eee.EffectListener;
import org.iota.ict.ixi.protobuf.Wrapper;
import org.iota.ict.ixi.requests.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Thread which handles incoming connections on the given socket.
 */
public class Client extends Thread {

    private Socket socket;
    private Ixi ixi;
    private Map<String, EffectListener<String>> registeredListener = new HashMap<>();

    public Client(Socket socket, Ixi ixi) {
        this.socket = socket;
        this.ixi = ixi;
        start();
    }

    @Override
    public void run() {

        try {

            while (!interrupted()) {

                Wrapper.WrapperMessage request = Wrapper.WrapperMessage.parseDelimitedFrom(socket.getInputStream());
                Wrapper.WrapperMessage.MessageType type = request.getMessageType();

                switch (type) {

                    case FIND_TRANSACTIONS_BY_ADDRESS_REQUEST: {
                        new ProcessFindTransactionsByAddressRequest(request, this);
                        break;
                    }

                    case FIND_TRANSACTIONS_BY_TAG_REQUEST: {
                        new ProcessFindTransactionsByTagRequest(request, this);
                        break;
                    }

                    case FIND_TRANSACTIONS_BY_HASH_REQUEST: {
                        new ProcessFindTransactionByHashRequest(request, this);
                        break;
                    }

                    case SUBMIT_TRANSACTION_BUILDER_REQUEST: {
                        new ProcessSubmitTransactionBuilderRequest(request, this);
                        break;
                    }

                    case SUBMIT_TRANSACTION_REQUEST: {
                        new ProcessSubmitEffectRequest(request, this);
                        break;
                    }

                    case DETERMINE_APPROVAL_CONFIDENCE_REQUEST: {
                        new ProcessDetermineApprovalConfidenceRequest(request, this);
                        break;
                    }

                    case ADD_EFFECT_LISTENER_REQUEST: {
                        new ProcessAddEffectListenerRequest(request, this);
                        break;
                    }

                    case REMOVE_EFFECT_LISTENER_REQUEST: {
                        new ProcessRemoveEffectListenerRequest(request, this);
                        break;
                    }

                    case SUBMIT_EFFECT_REQUEST: {
                        new ProcessSubmitEffectRequest(request, this);
                        break;
                    }

                }

            }

        } catch (IOException e) {
            System.err.println("Error while processing client.");
            e.printStackTrace();
        } finally {
            try { socket.close(); } catch (Exception x) { ; }
        }

    }

    public OutputStream getOutputStream() throws IOException {
        return socket.getOutputStream();
    }

    public Ixi getIxi() {
        return ixi;
    }

}
