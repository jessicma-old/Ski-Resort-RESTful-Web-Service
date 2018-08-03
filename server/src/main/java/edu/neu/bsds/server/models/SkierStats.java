package edu.neu.bsds.server.models;

/**
 * Created by jessicamalloy on 3/27/18.
 */
public class SkierStats {

    private Integer totalVert;
    private Integer numRides;


    public SkierStats() {

    }

    public SkierStats(Integer totalVert, Integer numRides) {
        this.totalVert = totalVert;
        this.numRides = numRides;
    }

    public Integer getTotalVert() {
        return totalVert;
    }

    public void setTotalVert(Integer totalVert) {
        this.totalVert = totalVert;
    }

    public Integer getNumRides() {
        return numRides;
    }

    public void setNumRides(Integer numRides) {
        this.numRides = numRides;
    }

    @Override
    public String toString() {
        return "SkierStats[" +
                "totalVert=" + totalVert +
                ", numRides=" + numRides +
                ']';
    }
}
