package com.prepos.analytics.util;

import com.prepos.analytics.CoreTopic;

import java.util.Map;

public class LeetcodeTagMapper {

    private static final Map<CoreTopic,String>TAG_MAP=Map.ofEntries(
            Map.entry(CoreTopic.GRAPH,"graph"),
            Map.entry(CoreTopic.DYNAMIC_PROGRAMMING,"dynamic-programming"),
            Map.entry(CoreTopic.RECURSION_BACKTRACKING,"backtracking"),
            Map.entry(CoreTopic.STACK_QUEUE,"stack"),
            Map.entry(CoreTopic.HASHING,"hash-table"),
            Map.entry(CoreTopic.SLIDING_WINDOW,"sliding-window"),
            Map.entry(CoreTopic.BINARY_SEARCH,"binary-search"),
            Map.entry(CoreTopic.TREE,"tree"),
            Map.entry(CoreTopic.TRIE,"trie"),
            Map.entry(CoreTopic.LINKED_LIST,"linked-list"),
            Map.entry(CoreTopic.GREEDY,"greedy"),
            Map.entry(CoreTopic.BIT_MANIPULATION,"bit-manipulation"),
            Map.entry(CoreTopic.ARRAY,"array"),
            Map.entry(CoreTopic.STRING,"string"),
            Map.entry(CoreTopic.ADVANCED_DATA_STRUCTURES,"trie")
    );
    public static String getTag(CoreTopic topic){
        return TAG_MAP.getOrDefault(topic,"array");
    }

}
