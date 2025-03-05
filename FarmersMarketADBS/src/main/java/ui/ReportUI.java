package ui;

import dao.TransactionDAO;
import models.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReportUI extends JPanel {
    private JComboBox<String> monthComboBox;
    private JComboBox<String> yearComboBox;
    private JComboBox<String> reportTypeComboBox;
    private JButton generateReportButton;
    private JTable reportTable;
    private DefaultTableModel tableModel;
    private TransactionDAO transactionDAO;

    public ReportUI() {
        setLayout(new BorderLayout());
        transactionDAO = new TransactionDAO();

        // Create a panel for dropdowns and button
        JPanel filterPanel = new JPanel(new GridLayout(4, 2, 10, 10));

        // Dropdown for Month
        monthComboBox = new JComboBox<>(getMonths());
        filterPanel.add(new JLabel("Select Month:"));
        filterPanel.add(monthComboBox);

        // Dropdown for Year
        yearComboBox = new JComboBox<>(getYears());
        filterPanel.add(new JLabel("Select Year:"));
        filterPanel.add(yearComboBox);

        // Dropdown for Report Type
        reportTypeComboBox = new JComboBox<>(getReportTypes().toArray(new String[0]));
        filterPanel.add(new JLabel("Select Report Type:"));
        filterPanel.add(reportTypeComboBox);

        // Generate Report Button
        generateReportButton = new JButton("Generate Report");
        generateReportButton.addActionListener(e -> generateReport());
        filterPanel.add(new JLabel("")); // Empty space for alignment
        filterPanel.add(generateReportButton);

        add(filterPanel, BorderLayout.NORTH);

        // Create table for displaying the report
        tableModel = new DefaultTableModel(new String[]{"Column 1", "Column 2"}, 0);
        reportTable = new JTable(tableModel);
        add(new JScrollPane(reportTable), BorderLayout.CENTER);
    }

    // Populate Month Dropdown
    private String[] getMonths() {
        return new String[]{"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};
    }

    // Populate Year Dropdown
    private String[] getYears() {
        return new String[]{"2022", "2023", "2024", "2025"};
    }

    // Populate Market Dropdown (Example Placeholder)
    private List<String> getReportTypes() {
        return Arrays.asList(
                "High Spending Customers",
                "High Revenue Vendor",
                "High Revenue Market",
                "High Revenue Product");
    }

    // Generate Report Logic
    private void generateReport() {
        String selectedMonth = (String) monthComboBox.getSelectedItem();
        String selectedYear = (String) yearComboBox.getSelectedItem();
        String selectedReportType = (String) reportTypeComboBox.getSelectedItem();
        List<List<String>> report = new ArrayList<>();
        switch(selectedReportType){
            case "High Spending Customers":
                report = transactionDAO.getCustomerSpendingOrder(selectedMonth, selectedYear);
                break;
            case "High Revenue Vendor":
                report = transactionDAO.getHighRevenueVendors(selectedMonth, selectedYear);
                break;
            case "High Revenue Market":
                report = transactionDAO.getHighRevenueMarkets(selectedMonth, selectedYear);
                break;
            case "High Revenue Product":
                report = transactionDAO.getHighRevenueProduct(selectedMonth, selectedYear);
                break;
            default:
                break;
        }


        // Clear existing rows
        tableModel.setRowCount(0);

        if (selectedMonth != null && selectedYear != null && selectedReportType != null) {
            for (int i = 0; i < report.size(); i++) { // Example rows
                tableModel.addRow(new Object[]{
                        report.get(i).get(0),
                        report.get(i).get(1),
//                        report.get(i).get(2)
                });
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select all fields!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
