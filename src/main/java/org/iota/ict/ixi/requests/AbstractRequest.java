package org.iota.ict.ixi.requests;

import org.iota.ict.ixi.Client;
import org.iota.ict.ixi.protobuf.Wrapper;

public abstract class AbstractRequest {

    public AbstractRequest(Wrapper.WrapperMessage request, Client clientHandler) {
        process(request, clientHandler);
    }

    protected abstract void process(Wrapper.WrapperMessage request, Client clientHandler);

}
