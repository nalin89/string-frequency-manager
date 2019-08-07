package com.homeaway.saas.frequencyManager.service;

import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;
import java.util.HashMap;
import java.util.PriorityQueue;

public class ReverseIndex {

    @Value("${stringFrequencyThreshold}")
    private int frequencyThreshold;
    @Value("${initialLoadTimeGap}")
    private int initialLoadTimeGap;

    private HashMap<String, PriorityQueue<Long>> frequencyIndex = new HashMap();

    public synchronized void addEntry(String string, long timestamp){

        if (frequencyIndex.containsKey(string)){
            PriorityQueue<Long> priorityQueue = frequencyIndex.get(string);
            if (priorityQueue.size()>=frequencyThreshold){
                priorityQueue.poll();
            }
            priorityQueue.add(timestamp);
        } else {
            PriorityQueue<Long> newQueue = new PriorityQueue<>();
            newQueue.add(timestamp);
            frequencyIndex.put(string, newQueue);
        }

    }

    public boolean isValid(String string){

        if (frequencyIndex.containsKey(string) && frequencyIndex.get(string).size()>=frequencyThreshold){
            Long oldestTimestamp = frequencyIndex.get(string).peek();
            if (oldestTimestamp < Instant.now().minusMillis(initialLoadTimeGap*60*60*1000).toEpochMilli()){
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }
}
