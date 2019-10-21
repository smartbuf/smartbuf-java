package com.github.sisyphsu.datube.proto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sisyphsu.datube.convertor.CodecFactory;
import com.github.sisyphsu.datube.node.ArrayNodeCodec;
import com.github.sisyphsu.datube.node.BasicNodeCodec;
import com.github.sisyphsu.datube.node.BeanNodeCodec;
import com.github.sisyphsu.datube.node.Node;
import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.github.sisyphsu.datube.proto.IOTest.*;

/**
 * @author sulin
 * @since 2019-10-21 20:40:21
 */
public class IOFullTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private ArrayNodeCodec codec = new ArrayNodeCodec();

    @BeforeEach
    void setUp() {
        CodecFactory.Instance.installCodec(BasicNodeCodec.class);
        CodecFactory.Instance.installCodec(ArrayNodeCodec.class);
        CodecFactory.Instance.installCodec(BeanNodeCodec.class);
        codec.setFactory(CodecFactory.Instance);
    }

    @Test
    public void test() throws IOException {
        Message msg = new Message();
        for (int i = 0; i < 10; i++) {
            msg.receivers.add(new Receiver());
        }
        Node node = codec.convert(msg, Node.class);
        Object obj = transIO(node);
        Message recvMsg = codec.convert(obj, Message.class);
        assert msg.equals(recvMsg);

        String json = MAPPER.writeValueAsString(msg);
        System.out.println("json: \t" + json.getBytes().length);
        System.out.println("datube: \t" + bytes.length);
    }

    @Data
    public static class Message {
        private int            id        = RandomUtils.nextInt();
        private boolean        enable    = RandomUtils.nextBoolean();
        private float          score1    = RandomUtils.nextFloat();
        private double         score2    = RandomUtils.nextDouble();
        private String         text      = RandomStringUtils.random(40);
        private long           timestamp = System.currentTimeMillis();
        private List<Receiver> receivers = new ArrayList<>();
    }

    @Data
    public static class Receiver {
        private int    userId     = RandomUtils.nextInt();
        private String iconUrl    = RandomStringUtils.random(20);
        private String remark     = RandomStringUtils.random(16);
        private int    followNum  = RandomUtils.nextInt(2, 200);
        private int    fanNum     = RandomUtils.nextInt(2, 200);
        private long   createTime = System.currentTimeMillis();
        private long   updateTime = System.currentTimeMillis();
    }

}
