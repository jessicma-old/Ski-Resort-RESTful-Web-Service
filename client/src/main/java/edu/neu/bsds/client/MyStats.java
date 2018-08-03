package edu.neu.bsds.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jessicamalloy on 4/26/18.
 */
public class MyStats {

    private Long wallTime;
    private Integer numRequests;
    private Integer numSuccessfulRequests; // Should be the same as latencies.size()
    private Map<Long, List<Long>> latenciesWithTimeStamp;
    private Boolean inProgress;

    public MyStats() {
        this.wallTime = System.currentTimeMillis();
        this.numRequests = 0;
        this.numSuccessfulRequests = 0;
        this.latenciesWithTimeStamp = new HashMap<>();
        this.inProgress = Boolean.TRUE;
    }

    /* GETTERS */
    /* ------------------------- */

    public Long getWallTime() {
        return wallTime;
    }

    public Integer getNumRequests() {
        return numRequests;
    }

    public Integer getNumSuccessfulRequests() {
        return numSuccessfulRequests;
    }

    public Map<Long, List<Long>> getLatenciesWithTimeStamp() {
        return latenciesWithTimeStamp;
    }


    /* UPDATE METHODS */
    /* ------------------------- */

    public synchronized void incrementNumRequests() {
        this.numRequests++;
    }

    public synchronized void incrementNumSuccessfulRequests() {
        this.numSuccessfulRequests++;
    }

    public synchronized void addLatency(Long timestamp, Long latency) {
        List<Long> latencies;
        if(this.latenciesWithTimeStamp.containsKey(timestamp)){
            latencies = this.latenciesWithTimeStamp.get(timestamp);
        } else {
            latencies = new ArrayList<>();
        }
        latencies.add(latency);
        this.latenciesWithTimeStamp.put(timestamp,latencies);
    }


    /* COMPUTE STATS METHODS */
    /* ------------------------- */

    public void outputStats(){

        List<Long> sortedLatencies = getSortedLatencies();

        System.out.println("Wall time: " + getWallTime());
        System.out.println("Number of requests sent: " + getNumRequests());
        System.out.println("Number of successful requests: " + getNumSuccessfulRequests());
        System.out.println("Throughput: " + getThroughput());
        System.out.println("Mean latency: " + getMean());
        System.out.println("Median latency: " + getMedian(sortedLatencies));
        System.out.println("95th percentile: " + get95(sortedLatencies));
        System.out.println("99th percentile: " + get99(sortedLatencies));
    }

    public void setWallTime(Long endTime) {
        if(this.inProgress) {
            this.wallTime = endTime - this.wallTime;
            this.inProgress = Boolean.FALSE;
        }
    }

    public List<Long> getAllLatencies(){
        Collection<List<Long>> latencyLists = this.latenciesWithTimeStamp.values();
        List<Long> allLatencies = new ArrayList<>();
        for(List<Long> latency : latencyLists){
            allLatencies.addAll(latency);
        }
        return allLatencies;
    }

    private List<Long> getSortedLatencies(){
        List<Long> sortedLatencies = this.getAllLatencies();
        Collections.sort(sortedLatencies);
        return sortedLatencies;
    }

    public Long getMean(){
        List<Long> latencies = this.getAllLatencies();

        long sum = 0L;
        if (latencies.isEmpty()){
            return sum;
        } else {
            for (Long latency : latencies) {
                sum += latency;
            }
            return sum / latencies.size();
        }
    }

    public long getMedian(List<Long> sortedLatencies){

        int numberOfElements = sortedLatencies.size();

        if (sortedLatencies.isEmpty()) {
            return 0L;
        } else if (numberOfElements % 2 == 0){
            // average of middle two numbers
            int upperMiddleIndex = (numberOfElements / 2);
            int lowerMiddleIndex = upperMiddleIndex - 1;
            return (long)((float)sortedLatencies.get(lowerMiddleIndex) + sortedLatencies.get(upperMiddleIndex)) / 2;
        } else {
            // the middle number
            int middleIndex = (numberOfElements - 1) / 2;
            return sortedLatencies.get(middleIndex);
        }
    }

    public long get95(List<Long> sortedLatencies){
        int length = sortedLatencies.size();
        if(length==0){
            return 0;
        } else {
            return sortedLatencies.get(Math.round((int)(length * 0.95)));
        }
    }

    // Calculate 99th percentile

    public long get99(List<Long> sortedLatencies){
        int length = sortedLatencies.size();
        if(length==0) {
            return 0;
        } else {
            return sortedLatencies.get(Math.round((int)(length * 0.99)));
        }
    }

    public double getThroughput() {
        if(!this.inProgress){
            double divisor = (double) getWallTime();
            return (double)getNumSuccessfulRequests() / divisor;
        } else {
            return -1;
        }
    }





}
