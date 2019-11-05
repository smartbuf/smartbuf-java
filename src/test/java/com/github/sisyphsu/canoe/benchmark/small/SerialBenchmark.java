package com.github.sisyphsu.canoe.benchmark.small;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sisyphsu.canoe.Canoe;
import com.github.sisyphsu.canoe.CanoePacket;
import com.github.sisyphsu.canoe.CanoeStream;
import com.github.sisyphsu.canoe.node.Node;
import com.github.sisyphsu.canoe.transport.OutputBuffer;
import com.github.sisyphsu.canoe.transport.OutputDataPool;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Benchmark                 Mode  Cnt     Score    Error  Units
 * SerialBenchmark.json      avgt    6   722.865 ± 59.575  ns/op
 * SerialBenchmark.packet    avgt    6  1275.657 ± 43.733  ns/op
 * SerialBenchmark.protobuf  avgt    6   198.970 ±  2.297  ns/op
 * SerialBenchmark.stream    avgt    6   621.402 ± 19.251  ns/op
 * <p>
 * Need more works to do to improve performace~
 * <p>
 * 190ns for Output#scan
 * 500ns for Output#doWrite and others
 * <p>
 * Benchmark                 Mode  Cnt     Score    Error  Units
 * SerialBenchmark.json      avgt    6   677.343 ± 30.833  ns/op
 * SerialBenchmark.packet    avgt    6  1077.892 ± 14.631  ns/op
 * SerialBenchmark.protobuf  avgt    6   200.738 ±  5.638  ns/op
 * SerialBenchmark.stream    avgt    6   523.828 ± 10.290  ns/op
 * <p>
 * stream(523ns) = dataPool.output(162ns) + metaPool.output(0ns) + others(374ns)
 *
 * @author sulin
 * @since 2019-10-28 17:32:33
 */
@Warmup(iterations = 2, time = 2)
@Fork(2)
@Measurement(iterations = 3, time = 3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class SerialBenchmark {

    static final Date date = new Date();

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final UserModel    USER          = UserModel.random();
    private static final CanoeStream  STREAM        = new CanoeStream();

    static {
        try {
            STREAM.serialize(USER);
            STREAM.serialize(USER);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Benchmark
    public void json() throws JsonProcessingException {
        OBJECT_MAPPER.writeValueAsString(USER);
    }

    @Benchmark
    public void packet() throws IOException {
        CanoePacket.serialize(USER);
    }

    @Benchmark
    public void stream() throws IOException {
        STREAM.serialize(USER);
    }

    @Benchmark
    public void protobuf() {
        USER.toPB().toByteArray();
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

    @Benchmark
    public void dataPool() {
        // 92ns
        dataPool.reset();
        dataPool.registerVarint(USER.getId());
        dataPool.registerString(USER.getNickname());
        dataPool.registerString(USER.getPortrait());
        dataPool.registerFloat(USER.getScore());
        dataPool.registerVarint(USER.getLoginTimes());
        dataPool.registerVarint(USER.getCreateTime());
    }

    @Benchmark
    public void writeBody() {
        buffer.reset();
        buffer.writeVarUint(200);
        buffer.writeVarUint(20);
        buffer.writeVarUint(30);
        buffer.writeVarUint(30);
        buffer.writeVarUint(30);
        buffer.writeVarUint(30);
        buffer.writeVarUint(30);
        buffer.writeVarUint(30);
    }

    @Benchmark
    public void benchmark() {
//        STREAM.canoe.output.write(USER); // 374ns

//        STREAM.canoe.output.bodyBuf.reset();
//        STREAM.canoe.output.writeObject(USER); // 263ns

//        STREAM.canoe.output.metaPool.reset(); // 6ns
//        STREAM.canoe.output.metaPool.needOutput(); // 3ns
//        STREAM.canoe.output.dataPool.reset(); // 8ns
//        STREAM.canoe.output.dataPool.needOutput(); // 3ns

//        buffer.reset();

//        STREAM.canoe.output.dataPool.write(buffer); // 162ns

//        STREAM.canoe.output.metaPool.write(buffer); // first=365ns, following=3ns

//        Canoe.CODEC.convert(USER, Node.class); // 40ns
    }

}
