package org.iota.ict.ixi;

import org.iota.ict.ixi.protobuf.Request;
import org.iota.ict.ixi.protobuf.Wrapper;
import org.junit.*;

import java.io.*;

public class ProtobufTest {

    private final File OUTPUT_FILE = new File("test.txt");

    @Before
    public void setup() {
        OUTPUT_FILE.delete();
    }

    @After
    public void tearDown() {
        OUTPUT_FILE.delete();
    }

    @Test
    public void testSerializationAndDeserialization() throws IOException {

        Request.AddEffectListenerRequest request = Request.AddEffectListenerRequest.newBuilder().setEnvironment("TestEnvironment").build();
        Wrapper.WrapperMessage message = Wrapper.WrapperMessage.newBuilder().setAddEffectListenerRequest(request).build();

        message.writeTo(new FileOutputStream(OUTPUT_FILE));

        Wrapper.WrapperMessage response = Wrapper.WrapperMessage.newBuilder().mergeFrom(new FileInputStream(OUTPUT_FILE)).build();

        Assert.assertTrue(response.hasAddEffectListenerRequest());
        Assert.assertEquals("TestEnvironment", response.getAddEffectListenerRequest().getEnvironment());

    }

}
