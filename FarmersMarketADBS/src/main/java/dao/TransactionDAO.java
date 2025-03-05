package dao;

import models.Transaction;
import models.TransactionDetail;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TransactionDAO {

    // Add a transaction and return the generated transaction ID
    public int addTransaction(Transaction transaction) {
        String query = "INSERT INTO transaction (cID, transaction_date, total_amount) VALUES (?, ? , ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setInt(1, transaction.getCustomerId());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(transaction.getTransactionDate()));
            preparedStatement.setBigDecimal(3, transaction.getTotal_amount());
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    return resultSet.getInt(1); // Return the generated transaction ID
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if transaction creation fails
    }

    // Add a transaction detail
    public boolean addTransactionDetail(TransactionDetail transactionDetail) {
        String query = "INSERT INTO transaction_detail (tId, pId, qty, subtotal) VALUES (?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, transactionDetail.getTransactionId());
            preparedStatement.setInt(2, transactionDetail.getProductId());
            preparedStatement.setInt(3, transactionDetail.getQuantity());
            preparedStatement.setBigDecimal(4, transactionDetail.getTotalPrice());
            return preparedStatement.executeUpdate() > 0; // Return true if insert is successful

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Return false if insert fails
    }

    public Transaction getTransactionById(int transactionId) {
        String query = "SELECT * FROM transaction WHERE tID = ? limit 1";
        return getTransaction(query, Arrays.asList(transactionId));
    }

    public List<Transaction> getAllTransactions() {
        String query = "SELECT * FROM transaction";
        return getTransactions(query,Arrays.asList());
    }

    public List<TransactionDetail> getTransactionDetailsByTransactionId(int transactionId) {
        String query = "SELECT * FROM transaction_detail WHERE tID = ?";
        return getTransactionDetails(query, Arrays.asList(transactionId));
    }

    public List<List<String>> getAllTransactionWithDetails(String filter) {
        List<List<String>> results = new ArrayList<>();
        String query = "";
        List<Object> parameters = new ArrayList<>();
        if(filter == "") {
            query = "SELECT \n" +
                    "    td.transaction_date ,\n" +
                    "    c.cName ,\n" +
                    "    p.pName,\n" +
                    "    p.pCategory,\n" +
                    "    v.vName,\n" +
                    "    m.mName,\n" +
                    "    p.pPrice ,\n" +
                    "    td.qty,\n" +
                    "    td.subtotal\n" +
                    "FROM \n" +
                    "    transaction_with_details td\n" +
                    "JOIN \n" +
                    "    customer c ON td.cID = c.cID\n" +
                    "JOIN \n" +
                    "    product p ON td.pID = p.pID\n" +
                    "JOIN \n" +
                    "    vendor v ON p.vID = v.vID\n" +
                    "JOIN \n" +
                    "    market m ON v.mID = m.mID\n" +
                    "ORDER BY \n" +
                    "    td.transaction_date;";
        }else{
            query = "SELECT td.transaction_date, c.cName ,p.pName , p.pCategory , v.vName , m.mName , p.pPrice , td.qty , td.subtotal \n" +
                    "FROM transaction_with_details td\n" +
                    "JOIN customer c ON t.cID = c.cID\n" +
                    "JOIN product p ON td.pID = p.pID\n" +
                    "JOIN vendor v ON p.vID = v.vID\n" +
                    "JOIN market m ON v.mID = m.mID\n" +
                    "WHERE td.transaction_date like '%?%'\n" +
                    "or p.pName like '%?%'\n" +
                    "or p.pCategory like '%?%'\n" +
                    "or v.vName like '%?%'\n" +
                    "or m.mName like '%?%'\n" +
                    "ORDER BY td.transaction_date;";
            parameters.add(filter);
            parameters.add(filter);
            parameters.add(filter);
            parameters.add(filter);
            parameters.add(filter);
        }

        try (ResultSet resultSet = DBConnection.executeQuery(query, Arrays.asList())) {
            while (resultSet.next()) {
                System.out.println("TransactionDAO: resultSet: "+ resultSet);
                results.add(
                        Arrays.asList(
                                resultSet.getString(1),
                                resultSet.getString(2),
                                resultSet.getString(3),
                                resultSet.getString(4),
                                resultSet.getString(5),
                                resultSet.getString(6),
                                resultSet.getString(7),
                                resultSet.getString(8),
                                resultSet.getString(8))
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public List<List<String>> getCustomerSpendingOrder(String month, String year) {
        System.out.println(month);
        System.out.println(year);
        String query = "SELECT c.cName AS CustomerName, SUM(t.total_amount) AS TotalSpending\n" +
                "FROM customer c JOIN transaction t ON c.cID = t.cID\n" +
                "WHERE MONTH(t.transaction_date) = ? AND YEAR(t.transaction_date) = ?\n" +
                "GROUP BY c.cID, c.cName\n" +
                "ORDER BY TotalSpending DESC; ";
        return getReport(query, Arrays.asList(getMonthNumber(month),year));
    }

    public List<List<String>> getHighRevenueVendors(String month, String year) {
        String query = "SELECT v.vName AS VendorName,SUM(td.subtotal) AS TotalSales\n" +
                "FROM vendor v JOIN product p ON v.vID = p.vID\n" +
                "JOIN transaction_with_details td ON p.pID = td.pID\n" +
                "WHERE MONTH(td.transaction_date) = ? AND YEAR(td.transaction_date) = ?\n" +
                "GROUP BY v.vID\n" +
                "ORDER BY TotalSales DESC;";
        return getReport(query, Arrays.asList(getMonthNumber(month),year));
    }
    public List<List<String>> getHighRevenueMarkets(String month, String year) {
        String query = "SELECT m.mName AS MarketName, SUM(td.subtotal) AS TotalRevenue\n" +
                "FROM market m JOIN vendor v ON m.mID = v.mID\n" +
                "JOIN product p ON v.vID = p.vID\n" +
                "JOIN transaction_with_details td ON p.pID = td.pID\n" +
                "WHERE MONTH(td.transaction_date) = ? AND YEAR(td.transaction_date) = ?\n" +
                "GROUP BY m.mID, m.mName\n" +
                "ORDER BY TotalRevenue DESC\n" +
                ";\n";
        return getReport(query, Arrays.asList(getMonthNumber(month), year));
    }

    public List<List<String>> getHighRevenueProduct(String month, String year) {
        String query = "SELECT p.pName AS ProductName, SUM(td.subtotal) AS TotalRevenue\n" +
                "FROM product p JOIN transaction_with_details td ON p.pID = td.pID\n" +
                "WHERE MONTH(td.transaction_date) = ? AND YEAR(td.transaction_date) = ?\n" +
                "GROUP BY p.pID, p.pName\n" +
                "ORDER BY TotalRevenue DESC;";
        return getReport(query, Arrays.asList(getMonthNumber(month), year));
    }

    public List<List<String>> getReport(String query, List<Object> parameters) {
        List<List<String>> report = new ArrayList<>();
        try (ResultSet resultSet = DBConnection.executeQuery(query, parameters)) {
            while (resultSet.next()) {
                report.add(
                        Arrays.asList(
                                resultSet.getString(1),
                                resultSet.getString(2))
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return report;
    }

    public List<Transaction> getTransactions(String query, List<Object> parameters) {
        List<Transaction> transactions = new ArrayList<>();
        try (ResultSet resultSet = DBConnection.executeQuery(query, parameters)) {
            while (resultSet.next()) {
                transactions.add(mapResultSetToTransaction(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    public Transaction getTransaction(String query, List<Object> parameters) {

        try (ResultSet resultSet = DBConnection.executeQuery(query, parameters)) {
            while (resultSet.next()) {
                return mapResultSetToTransaction(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if no transaction is found
    }

    public List<TransactionDetail> getTransactionDetails(String query, List<Object> parameters) {
        List<TransactionDetail> transactionDetails = new ArrayList<>();
        try (ResultSet resultSet = DBConnection.executeQuery(query, parameters)) {
            while (resultSet.next()) {
                transactionDetails.add(mapResultSetToTransactionDetail(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactionDetails;
    }

    public TransactionDetail getTransactionDetail(String query, List<Object> parameters) {
        try (ResultSet resultSet = DBConnection.executeQuery(query, parameters)) {
            while (resultSet.next()) {
                return mapResultSetToTransactionDetail(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    // Helper method to map ResultSet to Transaction
    private Transaction mapResultSetToTransaction(ResultSet resultSet) throws SQLException {
        return new Transaction(
                resultSet.getInt("tID"),
                resultSet.getInt("cID"),
                resultSet.getTimestamp("transaction_date").toLocalDateTime(),
                resultSet.getBigDecimal("total_amount")
        );
    }

    // Helper method to map ResultSet to TransactionDetail
    private TransactionDetail mapResultSetToTransactionDetail(ResultSet resultSet) throws SQLException {
        return new TransactionDetail(
                resultSet.getInt("tdID"),
                resultSet.getInt("tID"),
                resultSet.getInt("pID"),
                resultSet.getInt("qty"),
                resultSet.getBigDecimal("subtotal")
        );
    }

    private Integer getMonthNumber(String month){
        switch(month){
            case "January":
                return 1;
            case "February":
                return 2;
            case "March":
                return 3;
            case "April":
                return 4;
            case "May":
                return 5;
            case "June":
                return 6;
            case "July":
                return 7;
            case "August":
                return 8;
            case "September":
                return 9;
            case "October":
                return 10;
            case "November":
                return 11;
            case "December":
                return 12;
            default:
                return 0;
        }
    }
}
