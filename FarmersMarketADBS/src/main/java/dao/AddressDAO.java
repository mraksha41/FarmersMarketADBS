// AddressDAO.java
package dao;

import models.Address;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AddressDAO {
    public List<Address> getAllAddresses() throws SQLException {
        List<Address> addresses = new ArrayList<>();
        String query = "SELECT * FROM Address";

        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                addresses.add(new Address(
                        resultSet.getInt("AddressID"),
                        resultSet.getString("Street"),
                        resultSet.getString("City"),
                        resultSet.getString("State"),
                        resultSet.getString("ZipCode")
                ));
            }
        }

        return addresses;
    }

    public int addAddress(Address address) throws SQLException {
        String query = "INSERT INTO Address (street, city, state, zipCode) VALUES (?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, address.getStreet());
            preparedStatement.setString(2, address.getCity());
            preparedStatement.setString(3, address.getState());
            preparedStatement.setString(4, address.getZipCode());
            preparedStatement.executeUpdate();

            System.out.println("preparedStatement.getGeneratedKeys()" + preparedStatement.getGeneratedKeys());

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                return generatedKeys.getInt(1); // Return the generated addressID
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if insertion fails
    }

    public void updateAddress(Address address) throws SQLException {
        String query = "UPDATE address SET street = ?, city = ?, state = ?, zipCode = ? WHERE addressID = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, address.getStreet());
            preparedStatement.setString(2, address.getCity());
            preparedStatement.setString(3, address.getState());
            preparedStatement.setString(4, address.getZipCode());
            preparedStatement.setInt(5, address.getAddressID());
            preparedStatement.executeUpdate();
        }
    }

    public void deleteAddress(int addressID) throws SQLException {
        String query = "DELETE FROM address WHERE addressID = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, addressID);
            preparedStatement.executeUpdate();
        }
    }

    public static Address getAddressById(int addressID) {
        String query = "SELECT * FROM address WHERE addressID = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, addressID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new Address(
                        resultSet.getInt("addressID"),
                        resultSet.getString("street"),
                        resultSet.getString("city"),
                        resultSet.getString("state"),
                        resultSet.getString("zipCode")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if no address is found
    }

}