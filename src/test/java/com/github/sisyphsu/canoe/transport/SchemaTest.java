package com.github.sisyphsu.canoe.transport;

import com.github.sisyphsu.canoe.node.std.BooleanNode;
import com.github.sisyphsu.canoe.node.std.SymbolNode;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author sulin
 * @since 2019-10-12 17:03:16
 */
public class SchemaTest {

    @Test
    public void testEmpty() throws IOException {
        Schema outputSchema = new Schema(true);
        Schema inputSchema = trans(outputSchema);
        assert isEqual(outputSchema, inputSchema);
    }

    @Test
    public void testError() {
        InputReader reader = new InputReader(new ByteArrayInputStream(new byte[]{0b00010100, 0, 0, 0})::read);
        Schema tmp = new Schema(false);
        try {
            tmp.read(reader);
            assert false;
        } catch (Exception e) {
            assert e instanceof RuntimeException;
        }
        reader = new InputReader(new ByteArrayInputStream(new byte[]{0b00010010, 0, 0, 0})::read);
        try {
            tmp.read(reader);
            assert false;
        } catch (Exception e) {
            assert e instanceof RuntimeException;
        }
    }

    @Test
    public void testModeError() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1 << 16);
        Output output = new Output(outputStream::write, false);
        output.write(BooleanNode.valueOf(true));

        Input input = new Input(new ByteArrayInputStream(outputStream.toByteArray())::read, true);
        try {
            input.read();
            assert false;
        } catch (Exception e) {
            assert true;
        }
    }

    @Test
    public void testSequenceError() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1 << 16);
        Output output = new Output(outputStream::write, true);
        output.write(SymbolNode.valueOf("BLACK"));
        output.write(SymbolNode.valueOf("WHITE"));
        byte[] bytes = outputStream.toByteArray();
        byte[] data = new byte[bytes.length / 2];
        System.arraycopy(bytes, bytes.length / 2, data, 0, bytes.length / 2);
        Input input = new Input(new ByteArrayInputStream(data)::read, true);
        try {
            input.read();
            assert false;
        } catch (Exception e) {
            assert true;
        }
    }

    @RepeatedTest(100)
    public void testCover() throws IOException {
        int[] nameIds = new int[RandomUtils.nextInt(2, 64)];
        for (int j = 0; j < nameIds.length; j++) {
            nameIds[j] = j;
        }

        Schema schema = new Schema(true);

        List<Integer> codes = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
        Collections.shuffle(codes);
        for (Integer code : codes) {
            switch (code) {
                case 1:
                    schema.tmpFloats.add(RandomUtils.nextFloat());
                    assert isEqual(schema, trans(schema));
                    break;
                case 2:
                    schema.tmpDoubles.add(RandomUtils.nextDouble());
                    assert isEqual(schema, trans(schema));
                    break;
                case 3:
                    schema.tmpVarints.add(RandomUtils.nextLong());
                    assert isEqual(schema, trans(schema));
                    break;
                case 4:
                    schema.tmpStrings.add(RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(10, 100)));
                    assert isEqual(schema, trans(schema));
                    break;
                case 5:
                    schema.tmpNames.add(RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(2, 64)));
                    assert isEqual(schema, trans(schema));
                    break;
                case 6:
                    schema.tmpStructs.add(nameIds);
                    assert isEqual(schema, trans(schema));
                    break;
                case 7:
                    schema.cxtNameAdded.add(RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(10, 100)));
                    assert isEqual(schema, trans(schema));
                    break;
                case 8:
                    schema.cxtNameExpired.add(RandomUtils.nextInt(1, 1000));
                    assert isEqual(schema, trans(schema));
                    break;
                case 9:
                    schema.cxtStructAdded.add(nameIds);
                    assert isEqual(schema, trans(schema));
                    break;
                case 10:
                    schema.cxtStructExpired.add(RandomUtils.nextInt(1, 1000));
                    assert isEqual(schema, trans(schema));
                    break;
                case 11:
                    schema.cxtSymbolAdded.add(RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(2, 64)));
                    assert isEqual(schema, trans(schema));
                    break;
                case 12:
                    schema.cxtSymbolExpired.add(RandomUtils.nextInt(1, 1000));
                    assert isEqual(schema, trans(schema));
                    break;
            }
        }
    }

    @Test
    public void testStreamMode() throws IOException {
        Schema schema = new Schema(true);
        // add data
        for (int i = 0; i < 100; i++) {
            int[] nameIds = new int[RandomUtils.nextInt(2, 64)];
            for (int j = 0; j < nameIds.length; j++) {
                nameIds[j] = j;
            }
            schema.tmpFloats.add(RandomUtils.nextFloat());
            schema.tmpDoubles.add(RandomUtils.nextDouble());
            schema.tmpVarints.add(RandomUtils.nextLong());
            schema.tmpStrings.add(RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(10, 100)));
            schema.tmpNames.add(RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(2, 64)));
            schema.tmpStructs.add(nameIds);
            schema.cxtSymbolAdded.add(RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(2, 64)));
            schema.cxtSymbolExpired.add(RandomUtils.nextInt(1, 1000));
            schema.cxtNameAdded.add(RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(10, 100)));
            schema.cxtNameExpired.add(RandomUtils.nextInt(10, 10000));
            schema.cxtStructAdded.add(nameIds);
            schema.cxtStructExpired.add(RandomUtils.nextInt(1, 1000));
        }

        Schema newSchema = trans(schema);
        assert isEqual(schema, newSchema);
    }

    @Test
    public void testBlockMode() throws IOException {
        Schema schema = new Schema(false);
        // add data
        for (int i = 0; i < 100; i++) {
            int[] nameIds = new int[RandomUtils.nextInt(2, 64)];
            for (int j = 0; j < nameIds.length; j++) {
                nameIds[j] = j;
            }
            schema.tmpFloats.add(RandomUtils.nextFloat());
            schema.tmpDoubles.add(RandomUtils.nextDouble());
            schema.tmpVarints.add(RandomUtils.nextLong());
            schema.tmpStrings.add(RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(10, 100)));
            schema.tmpNames.add(RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(2, 64)));
            schema.tmpStructs.add(nameIds);
        }

        Schema newSchema = trans(schema);
        assert isEqual(schema, newSchema);
    }

    // help for test's reuse
    private static Schema trans(Schema schema) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1 << 16);
        OutputWriter outputWriter = new OutputWriter(outputStream::write);
        schema.output(outputWriter);

        byte[] bytes = outputStream.toByteArray();
        Schema result = new Schema(true);
        InputReader reader = new InputReader(new ByteArrayInputStream(bytes)::read);
        result.read(reader);
        return result;
    }

    private static boolean isEqual(Schema s1, Schema s2) {
        return s1.stream == s2.stream
            && s1.sequence == s2.sequence
            && s1.head == s2.head
            && s1.hasTmpMeta == s2.hasTmpMeta
            && s1.hasCxtMeta == s2.hasCxtMeta
            && s1.tmpFloats.equals(s2.tmpFloats)
            && s1.tmpDoubles.equals(s2.tmpDoubles)
            && s1.tmpVarints.equals(s2.tmpVarints)
            && s1.tmpStrings.equals(s2.tmpStrings)
            && s1.tmpNames.equals(s2.tmpNames)
            && s1.tmpStructs.equals(s2.tmpStructs)
            && s1.cxtSymbolAdded.equals(s2.cxtSymbolAdded)
            && s1.cxtSymbolExpired.equals(s2.cxtSymbolExpired)
            && s1.cxtNameAdded.equals(s2.cxtNameAdded)
            && s1.cxtNameExpired.equals(s2.cxtNameExpired)
            && s1.cxtStructAdded.equals(s2.cxtStructAdded)
            && s1.cxtStructExpired.equals(s2.cxtStructExpired);
    }

}
