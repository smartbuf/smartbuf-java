# 如何将树形的Node转换为Packet

需要有一个类似于Context的实例，维护上下文以及name、struct、data的共享。

在获得Node实例之后，需要扫描Node以初始化Package，提取全部的name、struct、data等数据以整理。。。

先输出报文的head、schema、data，然后再根据Node输出body。

输出body的过程中，可能需要频繁的调用map#get反查dataId，每次反查可能耗时2ns。

## 目标

只扫描一次Node树，尽量避免反查，即根据Node的值到数据分区中反查id，反查次数过多也会影响性能的。

在创建Node时，即提前为需要复用的数据项分配相同的实例。这就需要在创建Node时，就提前介入Context了。

在ObjectNode节点创建时，也需要分配可复用的ContextType等。

但是不宜设计的过于麻烦，应该优先考虑完成度，先用看起来性能可能有缺陷的方案实现一版，待完善后再优化。