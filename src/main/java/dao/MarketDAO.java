// MarketDAO.java
package dao;

import models.Market;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MarketDAO {
    public List<Market> getAllMarkets() throws SQLException {
        List<Market> Markets = new ArrayList<>();
        String query = "SELECT * FROM market;";
        try (ResultSet resultSet = DBConnection.executeQuery(query, Arrays.asList())) {
            while (resultSet.next()) {
                Markets.add(mapResultSetToMarket(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Markets;
    }

    public void addMarket(Market Market) throws SQLException {
        String query = "INSERT INTO Market (mName, operatingHours, addressID) VALUES (?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, Market.getMName());
            preparedStatement.setString(2, Market.getOperatingHours());
            preparedStatement.setInt(3, Market.getAddressID());
            preparedStatement.executeUpdate();
        }
    }

    public void updateMarket(Market Market) throws SQLException {
        String query = "UPDATE market SET mName = ?, operatingHours = ?, addressID = ? WHERE mID = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, Market.getMName());
            preparedStatement.setString(2, Market.getOperatingHours());
            preparedStatement.setInt(3, Market.getAddressID());
            preparedStatement.setInt(4, Market.getMID());
            preparedStatement.executeUpdate();
        }
    }

    public void deleteMarket(int MarketID) throws SQLException {
        String query = "DELETE FROM market WHERE mID = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, MarketID);
            preparedStatement.executeUpdate();
        }
    }

    public List<Market> getMarketsBySearch(String searchText) {
        List<Market> markets = new ArrayList<>();
        String query = "SELECT m.mID, m.mName, m.operatingHours, m.addressID " +
                "FROM market m JOIN address a ON m.addressID = a.addressID " +
                "WHERE m.mName LIKE ? OR a.street LIKE ? OR a.zipCode LIKE ?";
        List<Object> parameters = Arrays.asList("%" + searchText + "%", "%" + searchText + "%", "%" + searchText + "%");

        try (ResultSet resultSet = DBConnection.executeQuery(query, parameters)) {
            while (resultSet.next()) {
                markets.add(mapResultSetToMarket(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return markets;

    }

    public Market getMarketById(int id) {
        String query = "SELECT * FROM market WHERE mID = ?";
        List<Object> parameters = Arrays.asList(id);
        try (ResultSet resultSet = DBConnection.executeQuery(query, parameters)) {
            while (resultSet.next()) {
                return mapResultSetToMarket(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Market mapResultSetToMarket(ResultSet resultSet) throws SQLException {
        return new Market(
                resultSet.getInt("mID"),
                resultSet.getString("mName"),
                resultSet.getString("operatingHours"),
                resultSet.getInt("addressID")
        );
    }
}

