package dao;

import models.Vendor;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VendorDAO {
    public List<Vendor> getAllVendors() throws SQLException {
        List<Vendor> vendors = new ArrayList<>();
        String query = "SELECT * FROM vendor";

        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                vendors.add(new Vendor(
                        resultSet.getInt("vID"),
                        resultSet.getString("vName"),
                        resultSet.getString("vContact"),
                        resultSet.getInt("mID")

                ));
            }
        }
        return vendors;
    }

    public Vendor getVendorById(int id){
        String query = "SELECT * FROM vendor WHERE vID = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new Vendor(
                        resultSet.getInt("vID"),
                        resultSet.getString("vName"),
                        resultSet.getString("vContact"),
                        resultSet.getInt("mID")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Vendor> getVendorsByMarketId(int mID){
        List<Vendor> vendors = new ArrayList<>();
        String query = "SELECT * FROM vendor WHERE mID = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, mID);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                vendors.add(new Vendor(
                        resultSet.getInt("vID"),
                        resultSet.getString("vName"),
                        resultSet.getString("vContact"),
                        resultSet.getInt("mID")

                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vendors;
    }

    public void deleteVendor(int vendorID) throws SQLException {
        String query = "DELETE FROM vendor WHERE vID = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, vendorID);
            preparedStatement.executeUpdate();
        }
    }

    public void updateVendor(Vendor vendor) throws SQLException {
        String query = "UPDATE vendor SET vName = ?, vContact = ? WHERE vID = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, vendor.getName());
            preparedStatement.setString(2, vendor.getContactInfo());
            preparedStatement.setInt(3, vendor.getVendorId());
            preparedStatement.executeUpdate();
        }
    }

    public void addVendor(Vendor vendor) throws SQLException {
        String query = "INSERT INTO vendor (vName, vContact, mID) VALUES (?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, vendor.getName());
            preparedStatement.setString(2, vendor.getContactInfo());
            preparedStatement.setInt(3, vendor.getMarketId());
            preparedStatement.executeUpdate();
        }
    }

    public int getMarketIdByVendorId (int vendorId){
        int mID = -1;
        String query = "Select mID from vendor where vID =  ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, vendorId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                System.out.println(resultSet);
                mID = resultSet.getInt("mID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mID;
    }


}
