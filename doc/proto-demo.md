以下讨论的模型例子为：

```golang
struct User{
    id int64
    name string
}
```

# 元数据

元数据包括字段名与数据结构，字段名如name和id，数据结构如User，他们必须要独立出去以方便复用。

+ name和id抽取出去放入namePool中
+ {id:?, name:?}这样的结构放入structPool中

# 分区

全部数据值放入一个大的数据池中，通过分片来区分数据类型。
针对User，需要设计一个DataPool，其中包括2个分片，第一个分片的数据类型为varint，第二个分片的数据类型为string。

每个分片需要前缀一个varint标明分片的数据类型和数量，如果数据少于2bit=4个，则分片前缀只需要1Byte，其中

+ 1bit标明分片是否结束
+ 4bit标明数据类型

在User的例子中，DataPool需要2Byte额外描述分片，最终输出的数据格式类似这样

```
[metadata] symbolPool, structPool
[datapool] varintSlice, stringSlice
[body]
```

## Data格式：

通过1个定义为`ref`的varint声明数据项，此`ref`的通过2位bit标记数据项类型：

+ `primary`：此数据项为普通的原生数据，真正的数据类型及数据体都存放在`datapool`里面。
+ `struct`：此数据项为自定义的结构体类型，需要到`metadata`里面获取它的完整定义，接下来需要进入`struct`的解析逻辑。
+ `slice`：此数据项为复杂的数组类型，接下来需要进入`slice`的解析逻辑。

## Struct格式

需要前缀一个varint类型的flag预标记struct的头信息，主要用于集中描述null和bool属性。

确保content部分不需要考虑null和bool属性之后，按照结构体中预定义的字段列表依次输出或解析普通的数据，进入`Data格式`即可。

## Slice格式

Slice可以理解为特殊的Array，Array由无数个分片组成。
对于数据类型单一的Array，只需要一个Slice来表达，如果数组类型不单一，则需要多个Slice来组成了。

每个Slice需要一个varint作为元数据，其中：

+ 1bit：表示Slice是否还有后续，即Array是否完整
+ 3bit：表示Slice的数据类型，如null、bool、byte、short、int、long、double、float等原生类型，这些array需要特殊处理
+ Xbit：如果是struct类型，则需要额外描述结构ID
+ 剩余：表示Slice的真实长度

数据类型如果是primary类型，则直接读写处理。否则进入循环的`Data格式`环节，逐个读取`struct`和`array`等格式的数据。

primary数组的元素不需要放入DataPool里面，包括bool、byte、short、int、long、double、float。

字符串需要放入常量池里面，因为字符串中可能存在一些枚举、常量等类似的数据。

其他如array、struct等类型的slice需要通过`data`递归解析。

# 上下文元数据

支持stream协议中上下文复用元数据，可复用的元数据包括

+ symbol即字段名
+ struct即结构体，各个语言中的对象结构
+ const即一些常量字符串，主要针对枚举
