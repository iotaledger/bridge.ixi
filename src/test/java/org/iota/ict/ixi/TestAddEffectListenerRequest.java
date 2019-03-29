package org.iota.ict.ixi;

import org.iota.ict.eee.EffectListenerQueue;
import org.iota.ict.eee.Environment;
import org.iota.ict.ixi.protobuf.Request;
import org.iota.ict.ixi.protobuf.Wrapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class TestAddEffectListenerRequest extends TestTemplate {

    @Test
    public void testAddEffectListenerRequest() throws IOException, InterruptedException {

        // add effect listener
        Request.AddEffectListenerRequest addEffectListenerRequest = Request.AddEffectListenerRequest.newBuilder().setEnvironment("TestEnvironment").build();
        Wrapper.WrapperMessage addEffectListenerRequestWrapper = Wrapper.WrapperMessage.newBuilder()
                .setMessageType(Wrapper.WrapperMessage.MessageType.ADD_EFFECT_LISTENER_REQUEST)
                .setAddEffectListenerRequest(addEffectListenerRequest)
                .build();

        sendMessage(addEffectListenerRequestWrapper);
        Thread.sleep(1000);

        // submit effect to environment
        ict1.submitEffect(new Environment("TestEnvironment"), "TestEffect");

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
