package util;

import java.sql.*;
import java.util.List;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/farmers_market";
    private static final String USER = "root";
    private static final String PASSWORD = "uShankar@5134";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Execute a SELECT query and return the ResultSet
    public static ResultSet executeQuery(String query, List<Object> parameters) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = prepareStatement(connection, query, parameters);
        return preparedStatement.executeQuery();
    }

    // Execute an INSERT, UPDATE, or DELETE query and return the number of rows affected
    public static int executeUpdate(String query, List<Object> parameters) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = prepareStatement(connection, query, parameters)) {
            return preparedStatement.executeUpdate();
        }
    }

    // Prepare the statement and set parameters
    private static PreparedStatement prepareStatement(Connection connection, String query, List<Object> parameters) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        for (int i = 0; i < parameters.size(); i++) {
            preparedStatement.setObject(i + 1, parameters.get(i));
        }
        return preparedStatement;
    }
}
