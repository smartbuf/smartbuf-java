package com.github.sisyphsu.canoe.transport;

import com.github.sisyphsu.canoe.converter.CodecFactory;
import com.github.sisyphsu.canoe.node.NodeCodec;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.Date;

/**
 * @author sulin
 * @since 2019-11-04 16:33:52
 */
public class OutputTest {

    private CodecFactory factory = CodecFactory.Instance;

    @BeforeEach
    void setUp() {
        factory.installCodec(NodeCodec.class);
    }

    @Test
    public void test() {
        Output output = new Output(factory, true);

        assert output.write(null).length == 2;
        assert output.write(BigInteger.valueOf(1000L)).length > 2;
        assert output.write(new Bean()).length > 2;
    }

    @Data
    public static class Bean {
        private int             id    = RandomUtils.nextInt();
        private String          name  = "hello";
        private Date            time  = new Date();
        private BitSet          flags = BitSet.valueOf(RandomUtils.nextBytes(8));
        private Collection<Tag> tags  = Arrays.asList(new Tag(1, "tag1"), null, new Tag(3, "tag3"));
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Tag {
        private int    code;
        private String name;
    }

}
