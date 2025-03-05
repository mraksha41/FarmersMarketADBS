package dao;

import models.Customer;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    public int addCustomer(Customer customer) {
        String query = "INSERT INTO customer (cName, cEmail, cContact) VALUES ('"
                + customer.getName() + "', '"
                + customer.getEmail() + "', '"
                + customer.getPhone() + "')";
        try {
            Connection connection = DBConnection.getConnection();
            Statement statement = connection.createStatement();
            int rowsAffected = statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);

            if (rowsAffected > 0) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public Customer getCustomerById(int customerId) {
        String query = "SELECT * FROM customer WHERE cID = " + customerId;
        try {
            Connection connection = DBConnection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                return mapResultSetToCustomer(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String query = "SELECT * FROM customer";
        try {
            Connection connection = DBConnection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                customers.add(mapResultSetToCustomer(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    public boolean updateCustomer(Customer customer) {
        String query = "UPDATE customer SET cName = '" + customer.getName() +
                "', cEmail = '" + customer.getEmail() +
                "', cContact = '" + customer.getPhone() +
                "' WHERE cID = " + customer.getCustomerId();
        try {
            Connection connection = DBConnection.getConnection();
            Statement statement = connection.createStatement();
            return statement.executeUpdate(query) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteCustomer(int customerId) {
        String query = "DELETE FROM customer WHERE cID = " + customerId;
        try {
            Connection connection = DBConnection.getConnection();
            Statement statement = connection.createStatement();
            return statement.executeUpdate(query) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Customer mapResultSetToCustomer(ResultSet resultSet) throws SQLException {
        Customer customer = new Customer();
        customer.setCustomerId(resultSet.getInt("cId"));
        customer.setName(resultSet.getString("cName"));
        customer.setEmail(resultSet.getString("cEmail"));
        customer.setPhone(resultSet.getString("cContact"));
        return customer;
    }
}
