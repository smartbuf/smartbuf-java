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

    public Boolean toBoolean(T t) {
        throw new UnsupportedOperationException();
    }

    public Long toVarint(T t) {
        throw new UnsupportedOperationException();
    }

    public BigInteger toBigInteger() {
        throw new UnsupportedOperationException();
    }

    public Float toFloat(T t) {
        throw new UnsupportedOperationException();
    }

    public Double toDouble(T t) {
        throw new UnsupportedOperationException();
    }

    public BigDecimal toBigDecimal(T t) {
        throw new UnsupportedOperationException();
    }

    public byte[] toBinary(T t) {
        throw new UnsupportedOperationException();
    }

    public String toString(T t) {
        throw new UnsupportedOperationException();
    }

    public Collection toArray() {
        throw new UnsupportedOperationException();
    }

    public Map<Object, Object> toMap() {
        throw new UnsupportedOperationException();
    }

    public Node toNode(T t) {
        throw new UnsupportedOperationException();
    }

    /******************************************************************/

    public T fromBoolean(Boolean val) {
        throw new UnsupportedOperationException();
    }

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
