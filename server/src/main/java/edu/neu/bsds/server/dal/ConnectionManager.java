package edu.neu.bsds.server.dal;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 * ConnectionManager is used by DAO classes to open/close connections so they can access the
 * database.
 */
public class ConnectionManager {



    /** Get the connection to the database instance.
    public Connection getConnection() throws SQLException {
        Connection connection = null;
        try {
            Properties connectionProperties = new Properties();
            connectionProperties.put("user", this.user);
            connectionProperties.put("password", this.password);
            // Ensure the JDBC driver is loaded by retrieving the runtime Class descriptor.
            // Otherwise, Tomcat may have issues loading libraries in the proper order.
            // One alternative is calling this in the HttpServlet init() override.
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                throw new SQLException(e);
            }
            connection = DriverManager.getConnection(
                    "jdbc:mysql://" + this.hostName + ":" + this.port + "/" + this.schema,
                    connectionProperties);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return connection;
    }*/


    /** Get the connection to the database instance. */
    public Connection getConnection() throws SQLException {
        Connection connection = null;
        try {
            Context initialContext = new InitialContext();
            Context environmentContext = (Context) initialContext.lookup("java:comp/env");

            DataSource dataSource = (DataSource) environmentContext.lookup("jdbc/SkiRecordsDB");
            try {
                connection = dataSource.getConnection();
            } catch (SQLException e) {
                e.printStackTrace();
                throw e;
            }

        } catch(javax.naming.NamingException e) {
            e.printStackTrace();
            throw new SQLException(e);
        }

        return connection;
    }


    /** Close the connection to the database instance. */
    public void closeConnection(Connection connection) throws SQLException {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }


}
