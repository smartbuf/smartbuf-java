1. Refactor Transport.Output, optimize its performance. 

2. Node pool design

DataNode -> BooleanNode, NullNode, DoubleNode, FloatNode, StringNode, SymbolNode, VarintNode
ObjectNode -> Struct(String[])
ArrayNode -> Object[]
NativeArrayNode -> boolean[], byte[], short[], int[], long[], float[], double[], char[]->(String) 
