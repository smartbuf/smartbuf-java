package com.github.sisyphsu.canoe.benchmark.simple;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sisyphsu.canoe.Canoe;
import com.github.sisyphsu.canoe.CanoePacket;
import com.github.sisyphsu.canoe.CanoeStream;
import com.github.sisyphsu.canoe.node.BeanNodeCodec;
import com.github.sisyphsu.canoe.node.Node;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Benchmark            Mode  Cnt     Score    Error  Units
 * PBenchmark.json      avgt    6   791.704 ± 13.024  ns/op
 * PBenchmark.packet    avgt    6  1665.642 ± 41.077  ns/op
 * PBenchmark.protobuf  avgt    6   201.356 ±  9.072  ns/op
 * PBenchmark.stream    avgt    6   952.161 ± 31.328  ns/op
 * <p>
 * Need more works to do to improve performace~
 * <p>
 * 690ns for Output.write???
 *
 * @author sulin
 * @since 2019-10-28 17:32:33
 */
@Warmup(iterations = 2, time = 2)
@Fork(2)
@Measurement(iterations = 3, time = 3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class PBenchmark {

    private static final BeanNodeCodec beanNodeCodec = new BeanNodeCodec();
    private static final ObjectMapper  OBJECT_MAPPER = new ObjectMapper();
    private static final UserModel     USER          = UserModel.random();
    private static final CanoeStream   STREAM        = new CanoeStream();

    static {
        Canoe.CODEC.installCodec(beanNodeCodec);
        beanNodeCodec.setFactory(Canoe.CODEC);
    }

    @Benchmark
    public void json() throws JsonProcessingException {
        OBJECT_MAPPER.writeValueAsString(USER.toModel());
    }

    @Benchmark
    public void packet() throws IOException {
        CanoePacket.serialize(USER.toModel());
    }

    @Benchmark
    public void stream() throws IOException {
        STREAM.serialize(USER.toModel());
    }

    @Benchmark
    public void protobuf() {
        USER.toPB().toByteArray();
    }

    //    @Benchmark
    public void toNode() {
//        USER.toModel(); // 27ns

//        Canoe.CODEC.getPipeline(UserModel.class, Node.class); // 11ns

        // 441ns, CodecContext cost 20ns
        // 178ns, 48ns if not convert value, 75ns if no Date
        // [date -> node] cost 100ns???
//        USER.setCreateTime(null);
//        beanNodeCodec.toNode(USER);
//        CodecContext.reset();

//         Canoe.CODEC.convert(date, Node.class); // 60ns

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
        Canoe.CODEC.convert(USER.toModel(), Node.class);
    }

}
