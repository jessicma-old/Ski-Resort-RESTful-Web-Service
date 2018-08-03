package edu.neu.bsds.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by crystalgomes on 4/2/18.
 */


/**
 * Calculates and aggregates: latencies for each successful request, number of requests sent, number of successful
 * requests, wall time, throughput, 95th percentile latencies, and 99th percentile latencies.
 */

public class MyCalc {

    private long startTime;
    private List<Long> latencies; // ArrayList<Long> to collect latencies
    private int numRequestsSent;
    private int numSuccessfulRequests; // Should be the same as latencies.size()
    private long wallTime;
    private long percentile95;
    private long percentile99;
    private Map<Long, Long> latenciesWithTimeStamp;

    public MyCalc(long startTime) {

        this.startTime = startTime;
        this.latencies = new ArrayList<>();
        this.numRequestsSent = 0;
        this.numSuccessfulRequests = 0;
        this.wallTime = 0L;

    }

    public List<Long> getLatencies() {
        return latencies;
    }

    public void setLatencies(ArrayList<Long> latencies) {
        this.latencies = latencies;
    }

    public long getStartTime() {
        return startTime;
    }

    public int getNumRequestsSent() {
        return numRequestsSent;
    }

    public int getNumSuccessfulRequests() {
        return numSuccessfulRequests;
    }

    public void setNumSuccessfulRequests(int numSuccessfulRequests) {
        this.numSuccessfulRequests = numSuccessfulRequests;
    }

    public synchronized void incrementNumRequests() {
        this.numRequestsSent++;
    }

    public synchronized void incrementSuccessfulRequests() {
        this.numSuccessfulRequests++;
    }

    // A thread.run() calculates and adds its latency
    //public synchronized void collectLatency() {
        //this.latencies.add(getLatency());
    //}

    public synchronized void addLatency(Long latency, Long timeStamp) {
        this.latencies.add(latency);
        this.latenciesWithTimeStamp.put(timeStamp, latency);
    }

    public List<Long> sortLatencies(){
        List<Long> sortedLatencies = this.latencies;
        Collections.sort(sortedLatencies);
        //setLatencies(sortedLatencies);
        return sortedLatencies;
    }

    // Mean of all latencies, assuming the list has been sorted
    public long getMean(){

        if (this.latencies.isEmpty()){
            return 0L;
        }

        long sum = 0L;

        for (Long latency : this.latencies){
            sum += latency;
        }

        return sum / this.latencies.size(); // dividing longs
    }

    // Median of all latencies, assuming the list has been sorted
    // Float and long weirdness: https://stackoverflow.com/a/18117072
    public long getMedian(){
        int numberOfElements = this.latencies.size();

        if (this.latencies.isEmpty()) {
            return 0L;
        } else if (numberOfElements % 2 == 0){
            // average of middle two numbers
            int upperMiddleIndex = (numberOfElements / 2);
            int lowerMiddleIndex = upperMiddleIndex - 1;

            return (long)((float)latencies.get(lowerMiddleIndex) + latencies.get(upperMiddleIndex)) / 2;
        } else {
            // the middle number
            int middleIndex = (numberOfElements - 1) / 2;
            return latencies.get(middleIndex);
        }

    }

    // Calculate 95th percentile

    public long get95(){
        int length = this.latencies.size();
        if(length==0){
            return 0;
        } else {
            return this.latencies.get(Math.round((int)(length * 0.95)));
        }
    }

    // Calculate 99th percentile

    public long get99(){
        int length = this.latencies.size();
        if(length==0) {
            return 0;
        } else {
            return this.latencies.get(Math.round((int)(length * 0.99)));
        }
    }

    // Calculate wall time:
    // add up latencies of all threads: from "start time" right before any thread is spawned
    // to end time after all threads are complete.

    public void setWallTime(long endTime) {
        this.wallTime = endTime - getStartTime();
    }

    public long getWallTime() {

        return this.wallTime;

    }

    public long getLatency() {

        return System.currentTimeMillis() - this.startTime;
    }

    public double getThroughput() {
        double divisor = (double) getWallTime();
        return (double)getNumSuccessfulRequests() / divisor;
    }


    public void computePercentiles(){
        List<Long> sortedLatencies = new ArrayList<>();
        for(Long element : this.latencies){
            sortedLatencies.add(element);
        }
        Collections.sort(sortedLatencies);
        //setLatencies(sortedLatencies);

        int length = sortedLatencies.size();
        if(length==0){
            this.percentile95 = 0;
            this.percentile99 = 0;
        } else {
            this.percentile95 = sortedLatencies.get(Math.round((int)(length * 0.95)));
            this.percentile99 = sortedLatencies.get(Math.round((int)(length * 0.99)));
        }
    }

    public void outputStats(){
        computePercentiles();
        System.out.println("95% " + this.percentile95);
        System.out.println("99% " + this.percentile99);
        System.out.println("Median " + getMedian());
        System.out.println("Mean " + getMean());
        System.out.println("Number of requests sent: " + getNumRequestsSent());
        System.out.println("Number of successful requests: " + getNumSuccessfulRequests());
        System.out.println("Wall time: " + getWallTime());
        System.out.println("Throughput: " + getThroughput());

    }
}