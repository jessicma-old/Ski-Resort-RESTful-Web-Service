package edu.neu.bsds.server.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import edu.neu.bsds.server.models.SkiRecords;
import edu.neu.bsds.server.models.SkierStats;

/**
 * Created by jessicamalloy on 4/15/18.
 */
public class SkiRecordsDao {
    protected ConnectionManager connectionManager;

    // Single pattern: instantiation is limited to one object.
    private static SkiRecordsDao instance = null;
    protected SkiRecordsDao() {
        connectionManager = new ConnectionManager();
    }
    public static SkiRecordsDao getInstance() {
        if(instance == null) {
            instance = new SkiRecordsDao();
        }
        return instance;
    }

    /**
     * Save the SkiRecords instance by storing it in your MySQL instance.
     * This runs a INSERT statement.
     */
    public SkiRecords create(SkiRecords skiRecord) throws SQLException {
        String insertSkiRecord = "INSERT INTO SkiRecords(SkierID,DayNumber,TimeScanned,LiftID," +
                "ResortID) VALUES(?,?,?,?,?);";
        Connection connection = null;
        PreparedStatement insertStmt = null;
        try {
            connection = connectionManager.getConnection();
            insertStmt = connection.prepareStatement(insertSkiRecord);

            insertStmt.setString(1, skiRecord.getSkierID());
            insertStmt.setInt(2, skiRecord.getDayNumber());
            insertStmt.setString(3, skiRecord.getTimestamp());
            insertStmt.setString(4, skiRecord.getLiftID());
            insertStmt.setString(5, skiRecord.getResortID());

            insertStmt.executeUpdate();


            return skiRecord;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if(insertStmt != null) {
                insertStmt.close();
            }
            if(connection != null) {
                connection.close();
            }
        }
    }

    //**
    public List<SkiRecords> poolCreate(List<SkiRecords> skiRecords) throws SQLException {

        String insertSkiRecord = "INSERT INTO SkiRecords(SkierID,DayNumber,TimeScanned,LiftID," +
                "ResortID) VALUES(?,?,?,?,?);";
        Connection connection = null;
        PreparedStatement insertStmt = null;
        try {
            connection = connectionManager.getConnection();

            for(SkiRecords skiRecord: skiRecords){
                insertStmt = connection.prepareStatement(insertSkiRecord);
                insertStmt.setString(1, skiRecord.getSkierID());
                insertStmt.setInt(2, skiRecord.getDayNumber());
                insertStmt.setString(3, skiRecord.getTimestamp());
                insertStmt.setString(4, skiRecord.getLiftID());
                insertStmt.setString(5, skiRecord.getResortID());
                insertStmt.addBatch();
            }

            insertStmt.executeBatch();
            return skiRecords;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if(insertStmt != null) {
                insertStmt.close();
            }
            if(connection != null) {
                connection.close();
            }
        }
    }//*/


    /**
     * Get the Lifts record by fetching it from your MySQL instance.
     * This runs a SELECT statement and returns a single Lifts instance.
     */
    public SkierStats getStatsForSkierDay(String skierID, Integer dayNumber) throws SQLException {
        String selectLift = "SELECT SUM(LiftData.Vertical) AS TotalVert, COUNT(*) AS NumRides " +
                "FROM SkiRecords INNER JOIN LiftData ON SkiRecords.LiftID = LiftData.LiftID " +
                "WHERE SkierID = ? AND DayNumber = ?;";
        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet results = null;
        try {
            connection = connectionManager.getConnection();
            selectStmt = connection.prepareStatement(selectLift);
            selectStmt.setString(1, skierID);
            selectStmt.setInt(2, dayNumber);
            // Note that we call executeQuery(). This is used for a SELECT statement
            // because it returns a result set. For more information, see:
            // http://docs.oracle.com/javase/7/docs/api/java/sql/PreparedStatement.html
            // http://docs.oracle.com/javase/7/docs/api/java/sql/ResultSet.html
            results = selectStmt.executeQuery();
            // You can iterate the result set (although the example below only retrieves
            // the first record). The cursor is initially positioned before the row.
            // Furthermore, you can retrieve fields by name and by type.
            if(results.next()) {
                Integer totalVertical = results.getInt("TotalVert");
                Integer numRides  = results.getInt("NumRides");
                SkierStats skierStats = new SkierStats(totalVertical,numRides);
                return skierStats;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if(connection != null) {
                connection.close();
            }
            if(selectStmt != null) {
                selectStmt.close();
            }
            if(results != null) {
                results.close();
            }
        }
        return null;
    }


}
