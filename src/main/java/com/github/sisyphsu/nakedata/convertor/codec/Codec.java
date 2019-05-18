package com.github.sisyphsu.nakedata.convertor.codec;

import com.github.sisyphsu.nakedata.convertor.Convertor;
import com.github.sisyphsu.nakedata.node.Node;

import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Map;

/**
 * 数据转换适配器
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
     * @param t T实例
     * @return 转换结果
     */
    public Boolean toBoolean(T t) {
        throw new UnsupportedOperationException();
    }

    /**
     * 将T实例转换为Long类型，支持byte、short、int、long和它们的装箱类型，以及AtomicInteger等类型
     * 部分枚举值如时区也需要转换为Code
     *
     * @param t T实例
     * @return 转换结果
     */
    public Long toVarint(T t) {
        throw new UnsupportedOperationException();
    }

    /**
     * 将T实例转换为Float类型，支持float、Float等类型，以及相关的封装类型
     *
     * @param t T实例
     * @return 转换结果
     */
    public Float toFloat(T t) {
        throw new UnsupportedOperationException();
    }

    /**
     * 将T实例转换为Double类型，支持double、Double、DoubleAdder、AtomicDouble等类型
     *
     * @param t T实例
     * @return 转换结果
     */
    public Double toDouble(T t) {
        throw new UnsupportedOperationException();
    }

    /**
     * 将T实例转换为BigInteger类型，支持BigInteger等类型
     *
     * @param t T实例
     * @return 转换结果
     */
    public BigInteger toBigInteger(T t) {
        throw new UnsupportedOperationException();
    }

    /**
     * 将T实例转换为BigDecimal类型，支持BigDecimal等类型
     *
     * @param t T实例
     * @return 转换结果
     */
    public BigDecimal toBigDecimal(T t) {
        throw new UnsupportedOperationException();
    }

    /**
     * 将T实例转换为二进制类型, 部分支持高压缩序列化的类型，如时间、时间戳等。
     *
     * @param t T实例
     * @return 转换结果
     */
    public byte[] toBinary(T t) {
        throw new UnsupportedOperationException();
    }

    /**
     * 将T实例转换为字符串类型，如Map的键需要转换为String类型，尽管它不标准
     *
     * @param t T实例
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
     * 将T实例转换为集合、数组类型
     * TODO: 感觉没有必要，直接优化ArrayNode即可
     *
     * @param t T实例
     * @return 转换结果
     */
    public Collection toArray(T t) {
        throw new UnsupportedOperationException();
    }

    /**
     * 将T实例转换为Map类型，如POJO可以转换为Map<String, Object>
     *
     * @param t T实例
     * @return 转换结果
     */
    public Map toMap(T t) {
        throw new UnsupportedOperationException();
    }

    /**
     * 将T实例编码为Node类型
     *
     * @param t T实例
     * @return 编码结果
     */
    public Node toNode(T t) {
        throw new UnsupportedOperationException();
    }

    /******************************************************************/

    /**
     * 将Boolean数据转换为当前类型实例
     *
     * @param val Boolean数据
     * @return 泛型实例
     */
    public T fromBoolean(Boolean val) {
        throw new UnsupportedOperationException();
    }

    /**
     * 将Long数据转换为当前类型实例
     *
     * @param val Long数据
     * @return 泛型数据
     */
    public T fromVarint(Long val) {
        throw new UnsupportedOperationException();
    }

    public T fromBigInteger(BigInteger val) {
        throw new UnsupportedOperationException();
    }

    public T fromFloat(Float val) {
        throw new UnsupportedOperationException();
    }

    public T fromDouble(Double val) {
        throw new UnsupportedOperationException();
    }

    public T fromBigDecimal(BigDecimal val) {
        throw new UnsupportedOperationException();
    }

    public T fromBinary(byte[] val) {
        throw new UnsupportedOperationException();
    }

    public T fromString(String val) {
        throw new UnsupportedOperationException();
    }

    public T fromArray(Collection val) {
        throw new UnsupportedOperationException();
    }

    public T fromMap(Map map) {
        throw new UnsupportedOperationException();
    }

    public T fromNode(Node node) {
        throw new UnsupportedOperationException();
    }

}
