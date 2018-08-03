package edu.neu.bsds.server.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import edu.neu.bsds.server.models.Lifts;

/**
 * Created by jessicamalloy on 4/15/18.
 */
public class LiftDao {

    protected ConnectionManager connectionManager;

    // Single pattern: instantiation is limited to one object.
    private static LiftDao instance = null;
    protected LiftDao() {
        connectionManager = new ConnectionManager();
    }
    public static LiftDao getInstance() {
        if(instance == null) {
            instance = new LiftDao();
        }
        return instance;
    }


    /**
     * Save the Lifts instance by storing it in your MySQL instance.
     * This runs a INSERT statement.
     */
    public Lifts create(Lifts lifts) throws SQLException {
        String insertLiftData = "INSERT INTO LiftData(LiftID,Vertical) VALUES(?,?);";
        Connection connection = null;
        PreparedStatement insertStmt = null;
        try {
            connection = connectionManager.getConnection();
            insertStmt = connection.prepareStatement(insertLiftData);
            // PreparedStatement allows us to substitute specific types into the query template.
            // For an overview, see:
            // http://docs.oracle.com/javase/tutorial/jdbc/basics/prepared.html.
            // http://docs.oracle.com/javase/7/docs/api/java/sql/PreparedStatement.html
            // For nullable fields, you can check the property first and then call setNull()
            // as applicable.
            insertStmt.setString(1, lifts.getLiftID());
            insertStmt.setInt(2, lifts.getVertical());

            // Note that we call executeUpdate(). This is used for a INSERT/UPDATE/DELETE
            // statements, and it returns an int for the row counts affected (or 0 if the
            // statement returns nothing). For more information, see:
            // http://docs.oracle.com/javase/7/docs/api/java/sql/PreparedStatement.html
            // I'll leave it as an exercise for you to write UPDATE/DELETE methods.
            insertStmt.executeUpdate();

            // Note 1: if this was an UPDATE statement, then the person fields should be
            // updated before returning to the caller.
            // Note 2: there are no auto-generated keys, so no update to perform on the
            // input param person.
            return lifts;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if(connection != null) {
                connection.close();
            }
            if(insertStmt != null) {
                insertStmt.close();
            }
        }
    }


    /**
     * Get the Lifts record by fetching it from your MySQL instance.
     * This runs a SELECT statement and returns a single Lifts instance.
     */
    public Lifts getVerticalFromID(String liftID) throws SQLException {
        String selectLift = "SELECT LiftID,Vertical FROM LiftData WHERE LiftID=?;";
        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet results = null;
        try {
            connection = connectionManager.getConnection();
            selectStmt = connection.prepareStatement(selectLift);
            selectStmt.setString(1, liftID);
            // Note that we call executeQuery(). This is used for a SELECT statement
            // because it returns a result set. For more information, see:
            // http://docs.oracle.com/javase/7/docs/api/java/sql/PreparedStatement.html
            // http://docs.oracle.com/javase/7/docs/api/java/sql/ResultSet.html
            results = selectStmt.executeQuery();
            // You can iterate the result set (although the example below only retrieves
            // the first record). The cursor is initially positioned before the row.
            // Furthermore, you can retrieve fields by name and by type.
            if(results.next()) {
                String resultLiftID = results.getString("LiftID");
                Integer vertical  = results.getInt("Vertical");
                Lifts lift = new Lifts(resultLiftID,vertical);
                return lift;
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
