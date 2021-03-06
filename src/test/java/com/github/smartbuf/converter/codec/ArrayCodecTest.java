package com.github.smartbuf.converter.codec;

import com.github.smartbuf.converter.CodecFactory;
import com.github.smartbuf.reflect.TypeRef;
import com.github.smartbuf.reflect.XTypeUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.*;

/**
 * @author sulin
 * @since 2019-08-03 15:51:12
 */
@Slf4j
public class ArrayCodecTest {

    private ArrayCodec codec = new ArrayCodec();

    @BeforeEach
    void setUp() {
        codec.setFactory(CodecFactory.Instance);
    }

    @Test
    public void testObjectArray() {
        BigInteger[] arr = new BigInteger[]{BigInteger.valueOf(100), BigInteger.valueOf(200)};
        assert arr == codec.toArray(arr, XTypeUtils.toXType(Object[].class));

        Object[] objects = codec.toArray(arr, XTypeUtils.toXType(String[].class));
        assert objects instanceof String[];

        String[] strs = (String[]) objects;
        assert strs[0].equals("100");
        assert strs[1].equals("200");

        objects = codec.toArray(strs, XTypeUtils.toXType(new TypeRef<Optional<String>[]>() {
        }.getType()));
        assert objects.length == 2;
        assert objects[0] instanceof Optional;

        objects = codec.toArray(Arrays.asList(1L, null, 3L), XTypeUtils.toXType(Integer[].class));
        assert Objects.deepEquals(objects, new Integer[]{1, null, 3});
    }

    @Test
    public void testCollectionToArray() {
        byte[] bs1 = new byte[]{1, 3, 5};
        byte[] bs2 = new byte[]{4, 6, 8};
        Collection<BitSet> sets = Arrays.asList(BitSet.valueOf(bs1), BitSet.valueOf(bs2));

        Object[] objects = codec.toArray(sets, XTypeUtils.toXType(new TypeRef<byte[][]>() {
        }.getType()));
        assert objects instanceof byte[][];
        byte[][] byteArrs = (byte[][]) objects;
        assert Arrays.equals(byteArrs[0], bs1);
        assert Arrays.equals(byteArrs[1], bs2);

        Object[] objArr2 = codec.toArray(sets, XTypeUtils.toXType(new TypeRef<BitSet[]>() {
        }.getType()));
        assert objArr2 instanceof BitSet[];
        BitSet[] bitSets = (BitSet[]) objArr2;
        assert bitSets[0].equals(BitSet.valueOf(bs1));
        assert bitSets[1].equals(BitSet.valueOf(bs2));
    }

    @Test
    public void testPrimaryArray() {
        boolean[] bools = new boolean[]{true, false};
        Boolean[] booleans = new Boolean[]{true, false};
        assert Arrays.equals(codec.toBoolArray(booleans), bools);
        assert Arrays.equals(codec.toBooleanArray(bools), booleans);

        char[] chars = new char[]{'a', 'b', 'c'};
        Character[] characters = new Character[]{'a', 'b', 'c'};
        assert Arrays.equals(codec.toCharacterArray(chars), characters);
        assert Arrays.equals(codec.toCharArray(characters), chars);

        byte[] bs = new byte[]{1, 2, 3};
        Byte[] bytes = new Byte[]{1, 2, 3};
        assert Arrays.equals(codec.toByteArray(bs), bytes);
        assert Arrays.equals(codec.toByteArray(bytes), bs);

        short[] shorts = new short[]{1, 3, 7};
        Short[] shortArr = new Short[]{1, 3, 7};
        assert Arrays.equals(codec.toShortArray(shortArr), shorts);
        assert Arrays.equals(codec.toShortArray(shorts), shortArr);

        int[] ints = new int[]{1, 10};
        Integer[] integers = new Integer[]{1, 10};
        assert Arrays.equals(codec.toIntArray(integers), ints);
        assert Arrays.equals(codec.toIntegerArray(ints), integers);

        long[] longs = new long[]{1, 10, 100};
        Long[] longArr = new Long[]{1L, 10L, 100L};
        assert Arrays.equals(codec.toLongArray(longArr), longs);
        assert Arrays.equals(codec.toLongArray(longs), longArr);

        float[] floats = new float[]{1, 0.1f};
        Float[] floatArr = new Float[]{1f, 0.1f};
        assert Arrays.equals(codec.toFloatArray(floats), floatArr);
        assert Arrays.equals(codec.toFloatArray(floatArr), floats);

        double[] doubles = new double[]{1.0, 1.01, 0.001};
        Double[] doubleArr = new Double[]{1.0, 1.01, 0.001};
        assert Arrays.equals(codec.toDoubleArray(doubleArr), doubles);
        assert Arrays.equals(codec.toDoubleArray(doubles), doubleArr);
    }

}
