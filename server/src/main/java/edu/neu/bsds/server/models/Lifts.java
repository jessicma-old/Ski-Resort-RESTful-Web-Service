package edu.neu.bsds.server.models;

/**
 * Created by jessicamalloy on 4/15/18.
 */
public class Lifts {

    private String liftID;
    private Integer vertical;

    public Lifts() {
    }

    /**
     *
     * @param liftID String (1-40)
     * @param vertical Integer (200-500,100 increments)
     */
    public Lifts(String liftID, Integer vertical) {
        this.liftID = liftID;
        this.vertical = vertical;
    }

    public String getLiftID() {
        return liftID;
    }

    public void setLiftID(String liftID) {
        this.liftID = liftID;
    }

    public Integer getVertical() {
        return vertical;
    }

    public void setVertical(Integer vertical) {
        this.vertical = vertical;
    }
}
