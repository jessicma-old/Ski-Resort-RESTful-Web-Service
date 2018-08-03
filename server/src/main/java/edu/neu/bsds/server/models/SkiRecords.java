package edu.neu.bsds.server.models;

/**
 *
 */
public class SkiRecords {

    private String skierID;
    private Integer dayNumber;
    private String timestamp;
    private String resortID;
    private String liftID;

    public SkiRecords() {
    }

    /**
     *
     * @param resortID String (numeric)
     * @param dayNumber 1-365 representing the operational day in the season of the resort, eg opening day is Day 1
     * @param timestamp String (0-360 representing number of minutes after lifts open (9am) until they close (3pm)
     * @param skierID String (unique, numeric)
     * @param liftID String (1-40)
     */
    public SkiRecords(String resortID, Integer dayNumber, String timestamp, String skierID, String liftID) {
        this.resortID = resortID;
        this.dayNumber = dayNumber;
        this.timestamp = timestamp;
        this.skierID = skierID;
        this.liftID = liftID;
    }

    public String getResortID() {
        return resortID;
    }

    public void setResortID(String resortID) {
        this.resortID = resortID;
    }

    public Integer getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(Integer dayNumber) {
        this.dayNumber = dayNumber;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSkierID() {
        return skierID;
    }

    public void setSkierID(String skierID) {
        this.skierID = skierID;
    }

    public String getLiftID() {
        return liftID;
    }

    public void setLiftID(String liftID) {
        this.liftID = liftID;
    }
}
