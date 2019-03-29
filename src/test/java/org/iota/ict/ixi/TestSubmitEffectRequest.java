package org.iota.ict.ixi;

import org.iota.ict.ixi.protobuf.Request;
import org.iota.ict.ixi.protobuf.Wrapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class TestSubmitEffectRequest extends TestTemplate {

    @Test
    public void testSubmitEffectRequest() throws IOException, InterruptedException {

        // register listener
        Request.AddEffectListenerRequest addEffectListenerRequest = Request.AddEffectListenerRequest.newBuilder().setEnvironment("TestEnvironment").build();
        Wrapper.WrapperMessage addEffectListenerRequestWrapper = Wrapper.WrapperMessage.newBuilder()
                .setMessageType(Wrapper.WrapperMessage.MessageType.ADD_EFFECT_LISTENER_REQUEST)
                .setAddEffectListenerRequest(addEffectListenerRequest)
                .build();

        sendMessage(addEffectListenerRequestWrapper);
        Thread.sleep(1000);

        // build request message
        Request.SubmitEffectRequest submitEffectRequest = Request.SubmitEffectRequest.newBuilder().setEnvironment("TestEnvironment").setEffect("TestEffect").build();
        Wrapper.WrapperMessage submitEffectRequestWrapper = Wrapper.WrapperMessage.newBuilder()
                .setMessageType(Wrapper.WrapperMessage.MessageType.SUBMIT_EFFECT_REQUEST)
                .setSubmitEffectRequest(submitEffectRequest)
                .build();

        sendMessage(submitEffectRequestWrapper);

        // check if effect is available
        Request.TakeEffectRequest takeEffectRequest = Request.TakeEffectRequest.newBuilder().setEnvironment("TestEnvironment").build();
        Wrapper.WrapperMessage takeEffectRequestWrapper = Wrapper.WrapperMessage.newBuilder()
                .setMessageType(Wrapper.WrapperMessage.MessageType.TAKE_EFFECT_REQUEST)
                .setTakeEffectRequest(takeEffectRequest)
                .build();

        sendMessage(takeEffectRequestWrapper);

        // read response from bridge
        Wrapper.WrapperMessage response = readMessage();
        String effect = response.getTakeEffectResponse().getEffect();

        Assert.assertEquals("TestEffect", effect);

    }

}
