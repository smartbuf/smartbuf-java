package com.github.sisyphsu.canoe.transport;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sisyphsu.canoe.convertor.CodecFactory;
import com.github.sisyphsu.canoe.model.MessageOuterClass;
import com.github.sisyphsu.canoe.node.ArrayNodeCodec;
import com.github.sisyphsu.canoe.node.BasicNodeCodec;
import com.github.sisyphsu.canoe.node.BeanNodeCodec;
import com.github.sisyphsu.canoe.node.Node;
import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.github.sisyphsu.canoe.transport.IOTest.*;

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
        MMessage msg = new MMessage();
        for (int i = 0; i < 10; i++) {
            msg.receivers.add(new MReceiver());
        }
        Node node = CodecFactory.Instance.convert(msg, Node.class);
        Object obj = transIO(node);
        MMessage recvMsg = CodecFactory.Instance.convert(obj, MMessage.class);
        assert msg.equals(recvMsg);

        MessageOuterClass.Message msg2 = convert(msg);
        byte[] protoBytes = msg2.toByteArray();

        String json = MAPPER.writeValueAsString(msg);
        System.out.println("json: " + json.getBytes().length);
        System.out.println("datube: " + bytes.length);
        System.out.println("protobuf: " + protoBytes.length);

        /*
        json: 2348
        datube: 1308
        protobuf: 1261
         */
        // Object's metadata has 96 chars, if it's reused, datube will be around 1210, save 4%
    }

    static MessageOuterClass.Message convert(MMessage msg) {
        List<MessageOuterClass.Receiver> receivers = new ArrayList<>();
        for (MReceiver receiver : msg.receivers) {
            receivers.add(MessageOuterClass.Receiver.newBuilder()
                .setUserId(receiver.userId)
                .setIconUrl(receiver.iconUrl)
                .setRemark(receiver.remark)
                .setFollowNum(receiver.followNum)
                .setFanNum(receiver.fanNum)
                .setCreateTime(receiver.createTime)
                .setUpdateTime(receiver.updateTime)
                .build());
        }
        return MessageOuterClass.Message.newBuilder()
            .setId(msg.id)
            .setEnable(msg.enable)
            .setScore1(msg.score1)
            .setScore2(msg.score2)
            .setTimestamp(msg.timestamp)
            .setText(msg.text)
            .addAllReceivers(receivers)
            .build();
    }

    @Data
    public static class MMessage {
        private int             id        = RandomUtils.nextInt();
        private boolean         enable    = RandomUtils.nextBoolean();
        private float           score1    = RandomUtils.nextFloat();
        private double          score2    = RandomUtils.nextDouble();
        private String          text      = RandomStringUtils.random(40);
        private long            timestamp = System.currentTimeMillis();
        private List<MReceiver> receivers = new ArrayList<>();
    }

    @Data
    public static class MReceiver {
        private int    userId     = RandomUtils.nextInt();
        private String iconUrl    = RandomStringUtils.random(20);
        private String remark     = RandomStringUtils.random(16);
        private int    followNum  = RandomUtils.nextInt(2, 200);
        private int    fanNum     = RandomUtils.nextInt(2, 200);
        private long   createTime = System.currentTimeMillis();
        private long   updateTime = System.currentTimeMillis();
    }

}
