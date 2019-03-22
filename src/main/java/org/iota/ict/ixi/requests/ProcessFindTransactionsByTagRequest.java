package org.iota.ict.ixi.requests;

import org.iota.ict.ixi.Client;
import org.iota.ict.ixi.protobuf.Wrapper;

public class ProcessFindTransactionsByTagRequest extends AbstractRequest {

    public ProcessFindTransactionsByTagRequest(Wrapper.WrapperMessage request, Client clientHandler) {
        super(request, clientHandler);
    }

    @Override
    protected void process(Wrapper.WrapperMessage request, Client clientHandler) {

    }

}
