package dao;

import models.Product;
import models.Vendor;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductDAO {

    public int addProduct(Product product) {
        String query = "INSERT INTO product (pName, pCategory, pPrice, qtyInStock, vID) VALUES ('"
                + product.getpName() + "', '"
                + product.getpCategory() + "', "
                + product.getpPrice() + ", "
                + product.getQtyInStock() + ", "
                + product.getvID() + ")";
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
    public boolean updateProduct(Product product) {
        String query = "UPDATE product SET pName = '" + product.getpName() +
                "', pCategory = '" + product.getpCategory() +
                "', pPrice = " + product.getpPrice() +
                ", qtyInStock = " + product.getQtyInStock() +
                ", vID = " + product.getvID() +
                " WHERE pID = " + product.getpID();
        try {
            Connection connection = DBConnection.getConnection();
            Statement statement = connection.createStatement();
            return statement.executeUpdate(query) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean deleteProduct(int productId) {
        String query = "DELETE FROM product WHERE pID = " + productId;
        try {
            Connection connection = DBConnection.getConnection();
            Statement statement = connection.createStatement();
            return statement.executeUpdate(query) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM product";
        try {
            Connection connection = DBConnection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                products.add(mapResultSetToProduct(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }
    public Product getProductById(int productId) {
        String query = "SELECT * FROM product WHERE pID = " + productId;
        try {
            Connection connection = DBConnection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                return mapResultSetToProduct(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<Product> getProductsByVendorId(int vID){
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM product WHERE vID = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, vID);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                products.add(mapResultSetToProduct(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }
    public List<List<String>> getInventoryDetail(){
        List<List<String>> results = new ArrayList<>();
        String query =  "SELECT p.pName, v.vName, m.mName, p.qtyInStock\n" +
                    "FROM product p JOIN vendor v ON p.vID = v.vID\n" +
                    "JOIN market m ON v.mID = m.mID\n" +
                    "ORDER BY p.qtyInStock;\n";

        try (ResultSet resultSet = DBConnection.executeQuery(query, Arrays.asList())) {
            while (resultSet.next()) {
                System.out.println("TransactionDAO: resultSet: "+ resultSet);
                results.add(
                        Arrays.asList(
                                resultSet.getString(1),
                                resultSet.getString(2),
                                resultSet.getString(3),
                                resultSet.getString(4))
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }


    private Product mapResultSetToProduct(ResultSet resultSet) throws SQLException {
        Product product = new Product();
        product.setpID(resultSet.getInt("pID"));
        product.setpName(resultSet.getString("pName"));
        product.setpCategory(resultSet.getString("pCategory"));
        product.setpPrice(resultSet.getBigDecimal("pPrice"));
        product.setQtyInStock(resultSet.getInt("qtyInStock"));
        product.setvID(resultSet.getInt("vID"));
        return product;
    }


}
