package com.example.toto;

import com.example.toto.queue.messages.RxAbstractMessage;

import org.junit.Test;
import static org.junit.Assert.*;

public class QueuePackageTest {

    @Test
    public void testRxMessage(){
        String body = "{\n" +
                "  \"Type\":\"foo\"\n" +
                "}";
        TestMessage message = new TestMessage(body);

        assertEquals("foo",message.getType());
        assertEquals("foo",message.jsonify().get("Type").getAsString());
    }

    private class TestMessage extends RxAbstractMessage{

        public TestMessage(String json) {
            super(json);
        }
    }
}
