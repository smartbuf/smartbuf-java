
# Introduce

smartbuf与json、xml一样，在序列化过程中仍然保留着数据的schema信息，因此它的易用性、可扩展性、可读性等与json类似，
在数据交换过程中，通讯双方不需要预先协定数据模型，且任意一方都可以按照需要修改数据模型，这一点对于系统迭代更新过程中的向下兼容非常重要。

这是否意味着smartbuf的序列化结果与json一样臃肿吗？

恰恰相反，得益于schema与data分区序列化的策略，smartbuf的压缩率甚至会比protobuf更高一些。
具体的性能、压缩率对比细节，可以参考接下来的section。

接下来具体介绍一下smartbuf的分区序列化的细节原理。

# 分区序列化

在smartbuf的设计中，对象可以拆分为三个部分：property、struct、body。

针对这样的设计，序列化过程中也会将不同的部分拆分在不同的分区中，互相之间通过uniqueId引用组成完整的数据。

## property池

属性池用于存储一些通用、标准的属性值，包括const、float、double、varint、string、symbol。

所有属性在这个池中都拥有递增且唯一的ID，在body中所有引用这些属性的地方，直接引用ID即可，这个ID往往都很小，只需要1~2byte。

在实际情况下，数据体中的某些属性有可能重复出现，使用JSON和ProtoBuf时候，重复出现的数据会被重复序列化，
但是得益于数据池的设计，smartbuf不需要重复序列化相同的属性。

const是一种特殊的数据，包括null、true、false、empty-string等，这些特殊的数据以常量的方式预定义在协议中。

## struct池

结构体不需要考虑其属性的数据类型，它只提供类似于动态语言的弱类型结构体。

结构池内部包括两部分，属性名池与结构体池，属性名池类似于string[]，而结构体池内部通过varuint引用属性名。

这样的设计有两个优点：
1. 不同的结构体可以复用相同的属性名，尤其是常用的属性名如name、id、timestamp、url等。
2. 在长连接通信时，可以将属性名、结构体缓存在context中，避免这些schema信息重复出现在每一个数据报文中，从而提供数据传输效率。

## body



## 实例演示

```proto
message User {
    int32 id = 1;
    string name = 2;
    int64 time = 3;
}
```

ProtoBuf:

`[0x08, 0xE9, 0x07, 0x12, 0x05, 0x68, 0x65, 0x6C, 0x6C, 0x6F, 0x18, 0x90, 0x4E]`

# Benchmark and performance comparison

# Usage

By now, `smartbuf` only support java language, you can install it by this maven dependency:

```xml
<dependency>
  <groupId>com.github.sisyphsu</groupId>
  <artifactId>smartbuf</artifactId>
  <version>0.0.1</version>
</dependency>
```
