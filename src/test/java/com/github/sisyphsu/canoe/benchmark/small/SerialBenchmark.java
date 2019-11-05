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
 * SerialBenchmark.json      avgt    6   764.649 ±  4.576  ns/op
 * SerialBenchmark.packet    avgt    6  1074.742 ± 26.246  ns/op
 * SerialBenchmark.protobuf  avgt    6   203.787 ±  6.665  ns/op
 * SerialBenchmark.stream    avgt    6   519.900 ±  7.455  ns/op
 * <p>
 * stream(523ns) = dataPool.output(162ns) + convertObjectNode(40ns) + body.output(90ns) + others(230ns)
 * <p>
 * Move `instanceof ObjectNode` to first case, improved a little(10%):
 * Benchmark                 Mode  Cnt    Score    Error  Units
 * SerialBenchmark.json      avgt    6  747.340 ± 17.980  ns/op
 * SerialBenchmark.packet    avgt    6  997.599 ± 36.124  ns/op
 * SerialBenchmark.protobuf  avgt    6  204.565 ±  5.205  ns/op
 * SerialBenchmark.stream    avgt    6  459.287 ±  7.873  ns/op
 * <p>
 * Benchmark                 Mode  Cnt     Score    Error  Units
 * SerialBenchmark.json      avgt    6   735.111 ± 71.427  ns/op
 * SerialBenchmark.packet    avgt    6  1004.536 ± 16.042  ns/op
 * SerialBenchmark.protobuf  avgt    6   201.449 ±  3.240  ns/op
 * SerialBenchmark.stream    avgt    6   461.718 ± 16.687  ns/op
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

    //    @Benchmark
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

    //    @Benchmark
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

    //    @Benchmark
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
