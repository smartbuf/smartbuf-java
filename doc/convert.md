# doc

提供Class<?>实例与标准类型之间的双向转换，内部采用类似图算法的链式转换。

标准类型包括：Null, Bool, Float, Double, Varint, String, Binary, String, BigInteger, BigDecimal, Array

数组、集合类型统一封装为Array，应避免内存复制。

不支持自定义对象，需要通过Cglib的反射进行`map<=>object`的序列化&反序列化。

应该支持codec配置独立的priority，即优先级。