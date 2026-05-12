package com.prepos.analytics;

import java.util.HashMap;
import java.util.Map;

public class TopicMapper {
    private static final Map<String,CoreTopic> topicMap=new HashMap<>();
    static{
        topicMap.put("Array",CoreTopic.ARRAY);
        topicMap.put("String",CoreTopic.STRING);
        topicMap.put("Hash Table",CoreTopic.HASHING);
        topicMap.put("Linked List",CoreTopic.LINKED_LIST);
        topicMap.put("Stack",CoreTopic.STACK_QUEUE);
        topicMap.put("Queue",CoreTopic.STACK_QUEUE);
        topicMap.put("Binary Search",CoreTopic.BINARY_SEARCH);
        topicMap.put("Sliding Window",CoreTopic.SLIDING_WINDOW);
        topicMap.put("Tree",CoreTopic.TREE);
        topicMap.put("Binary Tree",CoreTopic.TREE);
        topicMap.put("Graph Theory", CoreTopic.GRAPH);
        topicMap.put("Depth-First Search", CoreTopic.GRAPH);
        topicMap.put("Breadth-First Search", CoreTopic.GRAPH);
        topicMap.put("Recursion", CoreTopic.RECURSION_BACKTRACKING);
        topicMap.put("Backtracking", CoreTopic.RECURSION_BACKTRACKING);
        topicMap.put("Dynamic Programming", CoreTopic.DYNAMIC_PROGRAMMING);
        topicMap.put("Greedy", CoreTopic.GREEDY);
        topicMap.put("Bit Manipulation", CoreTopic.BIT_MANIPULATION);
        topicMap.put("Trie", CoreTopic.TRIE);
        topicMap.put("Segment Tree", CoreTopic.ADVANCED_DATA_STRUCTURES);
        topicMap.put("Binary Indexed Tree", CoreTopic.ADVANCED_DATA_STRUCTURES);
    }
    public static CoreTopic getTopic(String topicName){
        return topicMap.get(topicName);
    }
}
