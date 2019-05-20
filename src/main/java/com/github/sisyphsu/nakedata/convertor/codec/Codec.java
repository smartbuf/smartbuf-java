package com.github.sisyphsu.nakedata.convertor.codec;

import com.github.sisyphsu.nakedata.convertor.Convertor;
import com.github.sisyphsu.nakedata.node.Node;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

/**
 * 数据转换适配器
 * <p>
 * encode函数约定为[to*], 负责将T类型序列化为目标数据, 输入参数仅接收T实例
 * <p>
 * decode函数约定为[from*], 负责将指定类型数据反序列化为T实例, 部分类型需要提供泛型类型
 * 1. POJO, 需要外界指定真实的POJO类型
 * 2. Array, 需要外界指定数组内部Type
 * 3. Collection, 需要外界提供集合泛型Type
 * 4. Map, 需要外界提供泛型Type
 *
 * @author sulin
 * @since 2019-05-12 16:01:19
 */
@SuppressWarnings("unchecked")
public abstract class Codec<T> {

    private Convertor root;

    public Convertor getRoot() {
        return root;
    }

    public void setRoot(Convertor root) {
        this.root = root;
    }

    /**
     * 支持的Java数据类型
     *
     * @return 泛型类型
     */
    public Class<T> support() {
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        return (Class<T>) type.getActualTypeArguments()[0];
    }

    /****************************** Encode方法 **********************************/

    /**
     * 将T实例转换为Boolean类型，支持boolean、Boolean、AtomicBoolean等
     *
     * @param t T实例, 可能为null
     * @return 转换结果
     */
    public Boolean toBoolean(T t) {
        throw new UnsupportedOperationException();
    }

    /**
     * 将T实例转换为Float类型，支持float、Float等类型，以及相关的封装类型
     *
     * @param t T实例, 可能为null
     * @return 转换结果
     */
    public Float toFloat(T t) {
        throw new UnsupportedOperationException();
    }

    /**
     * 将T实例转换为Double类型，支持double、Double、DoubleAdder、AtomicDouble等类型
     *
     * @param t T实例, 可能为null
     * @return 转换结果
     */
    public Double toDouble(T t) {
        throw new UnsupportedOperationException();
    }

    /**
     * 将T实例转换为Long类型，支持byte、short、int、long和它们的装箱类型，以及AtomicInteger等类型
     * 部分枚举值如时区也需要转换为Code
     *
     * @param t T实例, 可能为null
     * @return 转换结果
     */
    public Long toVarint(T t) {
        throw new UnsupportedOperationException();
    }

    /**
     * 将T实例转换为二进制类型, 部分支持高压缩序列化的类型，如时间、时间戳等。
     *
     * @param t T实例, 可能为null
     * @return 转换结果
     */
    public byte[] toBinary(T t) {
        throw new UnsupportedOperationException();
    }

    /**
     * 将T实例转换为字符串类型，如Map的键需要转换为String类型，尽管它不标准
     *
     * @param t T实例, 可能为null
     * @return 转换结果
     */
    public String toString(T t) {
        throw new UnsupportedOperationException();
    }

    /**
     * 将T实例转换为另外一种Object类型，如Reference<?>引用类型的拆卸等。
     *
     * @param t T实例
     * @return 转换结果
     */
    public Object toObject(T t) {
        throw new UnsupportedOperationException();
    }

    /**
     * 将T实例转换为集合、数组类型，如Object[]等可以封装为Collection统一处理。
     *
     * @param t T实例, 可能为null
     * @return 转换结果
     */
    public Collection toArray(T t) {
        throw new UnsupportedOperationException();
    }

    /**
     * 将T实例转换为Map类型，如POJO可以转换为Map<String, Object>
     *
     * @param t T实例, 可能为null
     * @return 转换结果
     */
    public Map toMap(T t) {
        throw new UnsupportedOperationException();
    }

    /**
     * 将T实例编码为Node类型，所有可以直接输出为Node的数据类型
     *
     * @param t T实例, 可能为null
     * @return 编码结果
     */
    public Node toNode(T t) {
        throw new UnsupportedOperationException();
    }

    /*********************************** Decode方法 *******************************/

    /**
     * 将Boolean数据解析为当前类型实例
     *
     * @param type T真实类型, 用于额外兼容Array、Collection、Map、POJO等类型
     * @param val  Boolean数据
     * @return 泛型实例
     */
    public T fromBoolean(Type type, Boolean val) {
        throw new UnsupportedOperationException();
    }

    /**
     * 将Float数据解析为当前类型实例，支持Float以及相关封装类型
     *
     * @param type T类型, 用于额外兼容泛型
     * @param val  Float数据
     * @return 泛型实例
     */
    public T fromFloat(Type type, Float val) {
        throw new UnsupportedOperationException();
    }

    /**
     * 将Double数据解析为当前类型实例，支持Double以及相关封装类型
     *
     * @param type T类型, 用于额外兼容泛型
     * @param val  Double数据
     * @return 泛型实例
     */
    public T fromDouble(Type type, Double val) {
        throw new UnsupportedOperationException();
    }

    /**
     * 将Long数据解析为当前类型实例，支持byte、short、int、long等，以及AtomicLong、AtomicInteger等封装类型？
     *
     * @param type T类型, 用于额外兼容泛型
     * @param val  Long数据
     * @return 泛型实例
     */
    public T fromVarint(Type type, Long val) {
        throw new UnsupportedOperationException();
    }

    /**
     * 将二进制数据解析为当前类型实例，支持所有提供了反序列化的数据类型，比如Date等
     *
     * @param type T类型, 用于额外兼容泛型
     * @param val  二进制数据
     * @return 泛型实例
     */
    public T fromBinary(Type type, byte[] val) {
        throw new UnsupportedOperationException();
    }

    /**
     * 将字符串解析为当前类型实例，可以用于支持如POJO等JSON解析等
     *
     * @param type T类型, 用于额外兼容泛型
     * @param val  字符串
     * @return 泛型实例
     */
    public T fromString(Type type, String val) {
        throw new UnsupportedOperationException();
    }

    /**
     * 将数组解析为当前类型实例，支持Object[]等转换为指定类型的Collection等。
     *
     * @param type T类型, 用于额外兼容泛型
     * @param val  数组
     * @return 泛型实例
     */
    public T fromArray(Type type, Collection val) {
        throw new UnsupportedOperationException();
    }

    /**
     * 将map解析为当前类型实例，支持对象序列化为POJO等。
     *
     * @param type T类型, 用于额外兼容泛型
     * @param map  Map数据
     * @return 泛型实例
     */
    public T fromMap(Type type, Map map) {
        throw new UnsupportedOperationException();
    }

    /**
     * 将Node解析为当前类型实例，对Node进行脱壳
     *
     * @param type T类型, 用于额外兼容泛型
     * @param node Node数据
     * @return 泛型实例
     */
    public T fromNode(Type type, Node node) {
        throw new UnsupportedOperationException();
    }

}
