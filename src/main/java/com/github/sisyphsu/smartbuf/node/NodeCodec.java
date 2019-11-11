package com.github.sisyphsu.smartbuf.node;

import com.github.sisyphsu.smartbuf.Type;
import com.github.sisyphsu.smartbuf.converter.BeanInfo;
import com.github.sisyphsu.smartbuf.converter.Codec;
import com.github.sisyphsu.smartbuf.converter.Converter;
import com.github.sisyphsu.smartbuf.node.array.*;
import com.github.sisyphsu.smartbuf.node.basic.*;
import com.github.sisyphsu.smartbuf.reflect.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * codec for array-node and basic-node
 *
 * @author sulin
 * @since 2019-06-10 21:02:55
 */
public final class NodeCodec extends Codec {

    @Converter
    public Boolean toValue(BooleanNode node) {
        return (Boolean) node.value();
    }

    @Converter
    public Long toValue(VarintNode node) {
        return (Long) node.value();
    }

    @Converter
    public Float toValue(FloatNode node) {
        return (Float) node.value();
    }

    @Converter
    public Double toValue(DoubleNode node) {
        return (Double) node.value();
    }

    @Converter
    public String toValue(StringNode node) {
        return (String) node.value();
    }

    @Converter
    public String toValue(SymbolNode node) {
        return (String) node.value();
    }

    @Converter
    public Collection toValue(ArrayNode node) {
        return (Collection) node.value();
    }

    @Converter
    public boolean[] toValue(BooleanArrayNode node) {
        return (boolean[]) node.value();
    }

    @Converter
    public byte[] toValue(ByteArrayNode node) {
        return (byte[]) node.value();
    }

    @Converter
    public short[] toValue(ShortArrayNode node) {
        return (short[]) node.value();
    }

    @Converter
    public int[] toValue(IntArrayNode node) {
        return (int[]) node.value();
    }

    @Converter
    public long[] toValue(LongArrayNode node) {
        return (long[]) node.value();
    }

    @Converter
    public float[] toValue(FloatArrayNode node) {
        return (float[]) node.value();
    }

    @Converter
    public double[] toValue(DoubleArrayNode node) {
        return (double[]) node.value();
    }

    @Converter
    public ArrayNode toNode(Object[] arr) {
        return new ArrayNode(Arrays.asList(arr));
    }

    @Converter
    public ArrayNode toNode(Collection coll) {
        return new ArrayNode(coll);
    }

    @Converter
    public BooleanArrayNode toNode(boolean[] booleans) {
        return new BooleanArrayNode(booleans);
    }

    @Converter
    public ByteArrayNode toNode(byte[] bytes) {
        return new ByteArrayNode(bytes);
    }

    @Converter
    public ShortArrayNode toNode(short[] shorts) {
        return new ShortArrayNode(shorts);
    }

    @Converter
    public IntArrayNode toNode(int[] ints) {
        return new IntArrayNode(ints);
    }

    @Converter
    public LongArrayNode toNode(long[] longs) {
        return new LongArrayNode(longs);
    }

    @Converter
    public FloatArrayNode toNode(float[] floats) {
        return new FloatArrayNode(floats);
    }

    @Converter
    public DoubleArrayNode toNode(double[] doubles) {
        return new DoubleArrayNode(doubles);
    }

    @Converter
    public BooleanNode toNode(Boolean b) {
        return BooleanNode.valueOf(b);
    }

    @Converter
    public VarintNode toNode(Byte b) {
        return VarintNode.valueOf(b);
    }

    @Converter
    public VarintNode toNode(Short s) {
        return VarintNode.valueOf(s);
    }

    @Converter
    public VarintNode toNode(Integer i) {
        return VarintNode.valueOf(i);
    }

    @Converter
    public VarintNode toNode(Long l) {
        return VarintNode.valueOf(l);
    }

    @Converter
    public FloatNode toNode(Float f) {
        return FloatNode.valueOf(f);
    }

    @Converter
    public DoubleNode toNode(Double d) {
        return DoubleNode.valueOf(d);
    }

    @Converter
    public StringNode toNode(String s) {
        return StringNode.valueOf(s);
    }

    @Converter
    public SymbolNode toSymbolNode(Enum e) {
        return SymbolNode.valueOf(e);
    }

    @Converter(distance = 101)
    public SymbolNode toSymbolNode(String str) {
        return SymbolNode.valueOf(str);
    }

    /**
     * decode ObjectNode to map, expose fields directly.
     */
    @Converter
    public Map<String, Object> toValue(ObjectNode node) {
        Map<String, Object> result = new HashMap<>();
        if (node != ObjectNode.EMPTY) {
            String[] keys = node.keys();
            Object[] values = node.values();
            for (int i = 0, len = keys.length; i < len; i++) {
                result.put(keys[i], values[i]);
            }
        }
        return result;
    }

    /**
     * convert ObjectNode to specified java bean
     */
    @Converter(extensible = true)
    public BeanInfo toObject(ObjectNode node, XType<?> type) {
        BeanWriter writer = BeanWriterBuilder.build(type.getRawType());
        BeanField[] fields = writer.getFields();
        Object[] values = new Object[fields.length];
        if (!node.isStable()) {
            Map<String, Object> map = toValue(node);
            for (int i = 0, len = fields.length; i < len; i++) {
                BeanField field = fields[i];
                values[i] = map.get(field.getName());
            }
        } else {
            int nodeIndex = 0;
            String[] nodeKeys = node.keys();
            Object[] nodeValues = node.values();
            for (int i = 0, len = fields.length; i < len; i++) {
                BeanField field = fields[i];
                String fieldName = field.getName();
                int compare = -1;
                while (nodeIndex < nodeValues.length
                    && (compare = fieldName.compareTo(nodeKeys[nodeIndex])) > 0) {
                    nodeIndex++;
                }
                if (compare != 0
                    || nodeValues[nodeIndex] == null) {
                    continue;
                }
                values[i] = nodeValues[nodeIndex];
            }
        }
        return new BeanInfo(writer, values);
    }

    /**
     * encode map to ObjectNode, pojo should be encoded as map first.
     */
    @Converter
    public ObjectNode toNode(Map<?, ?> map) {
        int len = map.size();
        if (len == 0) {
            return ObjectNode.EMPTY;
        }
        String[] keys = new String[len];
        Object[] values = new Object[len];
        int offset = 0;
        for (Object item : map.entrySet()) {
            Map.Entry entry = (Map.Entry) item;
            String key;
            if (entry.getKey() instanceof String) {
                key = (String) entry.getKey();
            } else {
                key = convert(entry.getKey(), String.class);
            }
            keys[offset] = key;
            values[offset] = entry.getValue();
            offset++;
        }
        return new ObjectNode(false, keys, values);
    }

    /**
     * Convert POJO to ObjectNode, for better performance
     */
    @Converter(distance = 1 << 16)
    public ObjectNode toNode(Object pojo) {
        BeanReader reader = BeanReaderBuilder.build(pojo.getClass());
        String[] names = reader.getFieldNames();
        Object[] values = reader.getValues(pojo);
        Type[] types = reader.getFieldTypes();
        return new ObjectNode(true, names, values, types);
    }

}
