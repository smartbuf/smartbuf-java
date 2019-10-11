# 报文结构

报文头部需要几个byte汇总关键信息：
+ version(4b)：报文的版本号
+ streamFlag(1b)：报文是否为流模式，流模式需要启用contextSchema
+ hasSchema(1b): 是否存在Schema区
+ hasData(1b)：是否存在Data区
+ ?：预留flag

## Schema区

记录数据体内的元数据，包括变量名(name)、结构体(struct)、常量字符串(symbol)等，需要支持上下文schema。

**在head中通过1bit标记Schema区是否为空**

为每个分区前缀varint，其中1bit标明是否结束，1bit或3bit标明分区类型。Chunk模式只需要1个bit，Stream模式需要3bit。

### Chunk模式

Chunk模式相对简单一些，只需要编排以下数据即可：

+ names：存储全部使用到的变量名
+ structs：存储全部使用到的结构体，结构体的每个字段都是弱类型，只需要记录松散的结构即可

此模式下，name与struct全部可以自动编排从0开始递增的ID。
因为name与struct不可能混合使用，因此它们可以采用独立的ID序列。

此模式下，不需要支持symbol。

### Stream模式

Stream模式更加复杂一些，它需要考虑到上下文元数据：

+ nameAdd: Context中新增的变量名
+ nameDel：Context中废弃的变量名
+ names：不需要记录在Context中、临时使用的变量名
+ structAdd：Context中新增的结构体
+ structDel：Context中废弃的结构体
+ structs：不需要记录在Context中、临时使用的结构体
+ symbolAdd：Context中新增的常量
+ symbolDel：Context中废弃的常量

此模式下，需要结合Context与Temp来综合编排递增ID。
同时为了确保在上下文缺失的情况下，仍然可以部分地解析数据体，需要将上下文状态附着在报文中。

由于这些数据并不完整，可能有一些残缺，比如没有可能没有nameAdd等。
为减少冗余的0数据，在Schema头部需要用一个单独的varint标记整体状态

通过巧妙的flag顺序，确保大多数情况下，只需要1Byte即可表达完整的flags。

## data区

以分片的形式分组存储全部数据项，并为它们编排ID，需要支持上下文data。需要分片存放的数据类型应包括：

+ 常量区：应该内置null、true、false作为默认值，引用它们只需要消耗1Byte
+ varint区：记录全部varint值
+ float区：记录全部float值
+ double区：记录全部double值
+ string区：记录全部字符串

*废弃：由于在一个数据报文中，不一定会用到全部数据，所以需要额外的flags标记各个分区的状态*

**在head中通过1个bit标记data区是否为空**

每个分区都需要前缀varint标记分区头信息，1bit标记分区是否结束，2bit标记分区类型，剩余部分标记分区成员数量。
对于小于32个元素的分区，只需要1byte前缀。

在Stream模式下，需要将Context中的全部symbol后缀入data区作为补充数据。
按照既定的分区顺序，为全部数据项分配递增ID，然后在body中引用它们。

## body区

通过1个定义为`ref`的varint声明数据类型，此`ref`的通过2位bit标记数据项类型：

+ `primary`：此数据项为普通的原生数据，真正的数据类型及数据体都存放在`datapool`里面。
+ `struct`：此数据项为自定义的结构体类型，需要到`metadata`里面获取它的完整定义，接下来需要进入`struct`的解析逻辑。
+ `slice`：此数据项为复杂的数组类型，接下来需要进入`slice`的解析逻辑。

## NodeID处理

输出端处理任意Node时，需要预先输出一个nodeId(varint)告知接收方，接下来的数据的元数据。

1. 普通数据，即引用自dataPool的数据，支持：null、bool、varint、float、double、string、symbol
2. 数组区，即后续数据为数组片段，最好在varint中指明第一个片段的属性，包括：hasMore、elementType、size
3. 对象区，即后续数据为结构体，需要携带structId标明具体的结构体ID

数字的elementType有12种类型，不应该放在nodeId中，让nodeId更加简短，最好短至1byte。
具体slice的元数据，通过新的sliceId标记。

# 实例分析

通过一些具体实例分析一些此协议对各种不同的数据有什么样的效果。

## 原生数据

对于原生类型的数据，如varint、string等，它们并不涉及任何自定义schema，所以可以直接声明报文为Chunk模式。

以普通字符串`hello`为例，报文head直接就是1-Byte(00010011)，表示Chunk模式、无Schema、有Data。
然后通过1-Byte(0010001)声明为string数据分区, 此分区只有一个成员`hello`，它的dataId应该是3。
最终在body里面通过1-Byte(00000011)引用此字符串。

总计需要3Byte的描述信息+真实数据

## User对象

Stream模式下首次处理User时，需要增加上下文的元数据。

报文head值应该是1B(00011110), 表示Stream模式、有Schema、有Data。
输出Schema分片数据，包括nameAdd、structAdd，每个分区都需要额外的1B(01001001/00001001)前缀。
输出Data分片数据，包括varint、string，每个分区都需要额外的1B(01010001/00010001)前缀。

最终在body里面通过1-Byte作为根节点输出，前2bit表示数据类型为struct，剩余5bit组成structId引用结构体。
之后每个字段都通过varint的dataId引用自数据区。

总计需要6Byte的描述信息+真实数据。

如果是第二次，则Schema区可以服用，报文head中schema标记为0，也不需要schema分区。数据体可以精简许多。

# 思考

## float和double是否可以优化处理？
