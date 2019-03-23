package org.iota.ict.ixi;

import org.iota.ict.eee.Environment;
import org.iota.ict.ixi.protobuf.Request;
import org.iota.ict.ixi.protobuf.Wrapper;
import org.iota.ict.ixi.util.Constants;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.Socket;

public class TestAddEffectListenerRequest extends TestTemplate {

    @Test
    public void testAddEffectListenerRequest() throws IOException, InterruptedException {

        // register module to bridge
        Socket socket = new Socket("127.0.0.1", Constants.DEFAULT_BRIDGE_PORT);

        // add effect listener
        Request.AddEffectListenerRequest request = Request.AddEffectListenerRequest.newBuilder().setEnvironment("TestEnvironment").build();
        Wrapper.WrapperMessage message = Wrapper.WrapperMessage.newBuilder()
                .setMessageType(Wrapper.WrapperMessage.MessageType.ADD_EFFECT_LISTENER_REQUEST)
                .setAddEffectListenerRequest(request)
                .build();

        message.writeDelimitedTo(socket.getOutputStream());

        Thread.sleep(1000);

        // submit effect to environment
        ict1.submitEffect(new Environment("TestEnvironment"), "TestEffect");

        Thread.sleep(1000);

        // check if effect is available
        Request.GetEffectRequest getEffectRequest = Request.GetEffectRequest.newBuilder().setEnvironment("TestEnvironment").build();
        Wrapper.WrapperMessage getEffectRequestMessage = Wrapper.WrapperMessage.newBuilder()
                .setMessageType(Wrapper.WrapperMessage.MessageType.GET_EFFECT_REQUEST)
                .setGetEffectRequest(getEffectRequest)
                .build();

        getEffectRequestMessage.writeDelimitedTo(socket.getOutputStream());

        // read response from bridge
        Wrapper.WrapperMessage response = Wrapper.WrapperMessage.parseDelimitedFrom(socket.getInputStream());
        String effect = response.getGetEffectResponse().getEffect();

        Assert.assertEquals("TestEffect", effect);

    }

}
