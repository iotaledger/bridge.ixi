package org.iota.ict.ixi;

import org.iota.ict.ixi.protobuf.Request;
import org.iota.ict.ixi.protobuf.Wrapper;
import org.iota.ict.ixi.util.Constants;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.Socket;

public class TestSubmitEffectRequest extends TestTemplate {

    @Test
    public void testSubmitEffectRequest() throws IOException, InterruptedException {

        // register module to bridge
        Socket socket = new Socket("127.0.0.1", Constants.DEFAULT_BRIDGE_PORT);

        // register listener
        Request.AddEffectListenerRequest addEffectListenerRequest = Request.AddEffectListenerRequest.newBuilder().setEnvironment("TestEnvironment").build();
        Wrapper.WrapperMessage addEffectListenerRequestWrapper = Wrapper.WrapperMessage.newBuilder()
                .setMessageType(Wrapper.WrapperMessage.MessageType.ADD_EFFECT_LISTENER_REQUEST)
                .setAddEffectListenerRequest(addEffectListenerRequest)
                .build();

        addEffectListenerRequestWrapper.writeDelimitedTo(socket.getOutputStream());

        Thread.sleep(1000);

        // build request message
        Request.SubmitEffectRequest submitEffectRequest = Request.SubmitEffectRequest.newBuilder().setEnvironment("TestEnvironment").setEffect("TestEffect").build();
        Wrapper.WrapperMessage submitEffectRequestWrapper = Wrapper.WrapperMessage.newBuilder()
                .setMessageType(Wrapper.WrapperMessage.MessageType.SUBMIT_EFFECT_REQUEST)
                .setSubmitEffectRequest(submitEffectRequest)
                .build();

        submitEffectRequestWrapper.writeDelimitedTo(socket.getOutputStream());

        Thread.sleep(1000);

        // check if effect is available
        Request.PollEffectRequest getEffectRequest = Request.PollEffectRequest.newBuilder().setEnvironment("TestEnvironment").build();
        Wrapper.WrapperMessage getEffectRequestMessage = Wrapper.WrapperMessage.newBuilder()
                .setMessageType(Wrapper.WrapperMessage.MessageType.POLL_EFFECT_REQUEST)
                .setPollEffectRequest(getEffectRequest)
                .build();

        getEffectRequestMessage.writeDelimitedTo(socket.getOutputStream());

        // read response from bridge
        Wrapper.WrapperMessage response = Wrapper.WrapperMessage.parseDelimitedFrom(socket.getInputStream());
        String effect = response.getPollEffectResponse().getEffect();

        Assert.assertEquals("TestEffect", effect);

    }

}
