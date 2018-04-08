package edu.neu.bsds.models;

/**
 * Created by jessicamalloy on 3/27/18.
 */
public class MyVert {

    private Integer totalVert;
    private Integer numRides;


    public MyVert() {

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
        return "MyVert[" +
                "totalVert=" + totalVert +
                ", numRides=" + numRides +
                ']';
    }
}
