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

                    case DETERMINE_APPROVAL_CONFIDENCE_REQUEST: {
                        ProcessDetermineApprovalConfidenceRequest.process(request.getDetermineApprovalConfidenceRequest(), this);
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
