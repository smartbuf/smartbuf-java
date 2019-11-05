package com.github.sisyphsu.canoe.benchmark.small;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sisyphsu.canoe.CanoePacket;
import com.github.sisyphsu.canoe.CanoeStream;
import com.github.sisyphsu.canoe.transport.OutputBuffer;
import com.github.sisyphsu.canoe.transport.OutputDataPool;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Benchmark                 Mode  Cnt    Score    Error  Units
 * SerialBenchmark.json      avgt    6  819.909 ± 29.310  ns/op
 * SerialBenchmark.packet    avgt    6  770.033 ± 17.766  ns/op
 * SerialBenchmark.protobuf  avgt    6  227.747 ± 11.092  ns/op
 * SerialBenchmark.stream    avgt    6  453.703 ± 10.799  ns/op
 * <p>
 * stream(485ns) = (unknwon)90ns + writeObject(166ns) + writeHeadBuf(162ns) + copyResult(20ns) + (reset+others)20ns
 * writeObject(166ns) = registerData(90ns) + objectNode(40ns) + 36ns(ifelse+bodyBuf)
 *
 * @author sulin
 * @since 2019-10-28 17:32:33
 */
@Warmup(iterations = 2, time = 2)
@Fork(2)
@Measurement(iterations = 3, time = 3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class SmallBenchmark {

    static final Date date = new Date();

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final UserModel    user          = UserModel.random();
    private static final CanoeStream  STREAM        = new CanoeStream();

    static {
        try {
            STREAM.serialize(user);
            STREAM.serialize(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Benchmark
    public void json() throws JsonProcessingException {
        OBJECT_MAPPER.writeValueAsString(user);
    }

    @Benchmark
    public void packet() throws IOException {
        CanoePacket.serialize(user);
    }

    @Benchmark
    public void stream() throws IOException {
        STREAM.serialize(user);
    }

    @Benchmark
    public void protobuf() {
        user.toPB().toByteArray();
    }

    //    @Benchmark
    public void toNode() {
//        USER.toModel(); // 27ns

//        Canoe.CODEC.getPipeline(UserModel.class, Node.class); // 11ns -> 5ns

        // 441ns, CodecContext cost 20ns
        // 123ns, 48ns if not convert value, 75ns if no Date
        // [date -> node] cost 100ns???
//        USER.setCreateTime(null);
//        beanNodeCodec.toNode(USER);
//        CodecContext.reset();

        // 123ns
//        beanNodeCodec.toNode(USER);
//        CodecContext.reset();

//        Canoe.CODEC.convert(date, Node.class); // 60ns -> 17ns

        // 380ns, Convert all fields into Node
//        BeanHelper helper = BeanHelper.valueOf(USER.getClass());
//        String[] names = helper.getNames();
//        Object[] values = helper.getValues(USER);
//        for (int i = 0, len = values.length; i < len; i++) {
//            values[i] = values[i];
//        }
//        new ObjectNode(true, names, values);

        // 263ns = 27ns(toModel) + 11ns(getPipeline) + 178ns(BeanNodeCodec.toNode)
        // Pipeline.convert cost 50ns ???
        // use ASM optimize ConverterPipeline, 263ns -> 155ns
//        Canoe.CODEC.convert(USER, Node.class);

        // 169ns = toModel[27ns] + beanNodeCodec#toNode[123ns] + getPipeline[5ns] + [14ns]
        // CodecContext/ThreadLocal may cost 20~30ns
    }


    static OutputBuffer   buffer   = new OutputBuffer(1 << 20);
    static OutputDataPool dataPool = new OutputDataPool(1 << 10);

    //    @Benchmark
    public void dataPool() {
        // 110ns
        dataPool.reset();
        dataPool.registerVarint(user.getId());
        dataPool.registerString(user.getNickname());
        dataPool.registerString(user.getPortrait());
        dataPool.registerFloat(user.getScore());
        dataPool.registerVarint(user.getLoginTimes());
        dataPool.registerVarint(user.getCreateTime());
    }

    //    @Benchmark
    public void writeBody() {
        // 138ns
        buffer.reset();
        buffer.writeVarInt(user.getId()); // 12ns for randomInt
        buffer.writeString(user.getNickname()); // 48ns for char[12]
        buffer.writeString(user.getPortrait()); // 52ns for char[24]
        buffer.writeFloat(user.getScore()); // 1.6ns
        buffer.writeVarInt(user.getLoginTimes()); // 3ns for [10,10000]
        buffer.writeVarInt(user.getCreateTime()); // 7.5ns for timestamp
    }

    //    @Benchmark
    public void benchmark() {
//        STREAM.canoe.output.write(USER); // 374ns

//        STREAM.canoe.output.bodyBuf.reset();
//        STREAM.canoe.output.writeObject(USER); // 166ns

//        STREAM.canoe.output.metaPool.reset(); // 6ns
//        STREAM.canoe.output.metaPool.needOutput(); // 3ns
//        STREAM.canoe.output.dataPool.reset(); // 8ns
//        STREAM.canoe.output.dataPool.needOutput(); // 3ns

        buffer.reset();

        STREAM.canoe.output.dataPool.write(buffer); // 162ns

//        STREAM.canoe.output.metaPool.write(buffer); // first=365ns, following=3ns

//        Canoe.CODEC.convert(USER, Node.class); // 40ns
    }

}
