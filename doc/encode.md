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