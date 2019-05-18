# Example 

user{
    id: 100
    name: "nick"
    tags: []
    settings: {
        hide: true
    }
}

# Step

1. match ObjectCodec#toMap -> map<Key, Value>
2. match Map#toNode; match Key#toString, Value#toNode

# 备注

- BigDecimal、BigInteger等直接通过byte[]编码即可，底层协议不需要额外支持这些数据。
- 需要增加Keyword类型，也可以叫做StringRef，用户支持enum、TimeZone、Locale、TimeUnit等单调字符串。
  也可以叫做*const*.