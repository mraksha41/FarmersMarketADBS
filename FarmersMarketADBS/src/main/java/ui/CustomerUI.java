package ui;

import dao.CustomerDAO;
import models.Customer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class CustomerUI extends JPanel {
    private CustomerDAO customerDAO;
    private JTable customerTable;
    private DefaultTableModel tableModel;
    private Consumer<String> switchPanelCallback;
    private Map<Integer, Integer> rowToVendorIdMap = new HashMap<>();


    public CustomerUI(Consumer<String> switchPanelCallback) {
        this.switchPanelCallback = switchPanelCallback;
        this.customerDAO = new CustomerDAO();

        setLayout(new BorderLayout());
        add(new JLabel("Customer Management"), BorderLayout.NORTH);

        initializeTable();
        initializeButtons();
        loadCustomerData();
    }

    private void initializeTable() {
        tableModel = new DefaultTableModel(new String[]{
                "Name", "Email", "Phone", "Edit", "Delete"}, 0);
        customerTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3 || column == 4; // Only "Edit" and "Delete" columns are editable
            }
        };

        // Add custom renderer and editor for action buttons
        TableColumnModel columnModel = customerTable.getColumnModel();
        columnModel.getColumn(3).setCellRenderer(new ActionButtonRenderer());
        columnModel.getColumn(3).setCellEditor(new ActionButtonEditor("Edit", this::editCustomer));
        columnModel.getColumn(4).setCellRenderer(new ActionButtonRenderer());
        columnModel.getColumn(4).setCellEditor(new ActionButtonEditor("Delete", this::deleteCustomer));

        add(new JScrollPane(customerTable), BorderLayout.CENTER);
    }

    private void initializeButtons() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addCustomerButton = new JButton("Add Customer");
        JButton backButton = new JButton("Back");

        addCustomerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddCustomerForm(CustomerUI.this::loadCustomerData);
            }
        });

        backButton.addActionListener(e -> switchPanelCallback.accept("MainMenuUI")); // Switch back to main menu

        buttonPanel.add(addCustomerButton);
//        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadCustomerData() {
        try {
            List<Customer> customers = customerDAO.getAllCustomers();
            updateCustomerTableData(customers);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading customers.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateCustomerTableData(List<Customer> customers) {
        tableModel.setRowCount(0); // Clear existing rows
        rowToVendorIdMap.clear(); // Clear previous mapping
        int rowIndex = 0;
        for (Customer customer : customers) {
            tableModel.addRow(new Object[]{
                    customer.getName(),
                    customer.getEmail(),
                    customer.getPhone(),
                    "Edit",
                    "Delete"
            });
            rowToVendorIdMap.put(rowIndex, customer.getCustomerId()); // Map row index to vendorId
            rowIndex++;
        }
    }

    private void editCustomer(int row) {
        try {
            int customerId = rowToVendorIdMap.get(row);
            Customer customer = customerDAO.getCustomerById(customerId);
            new EditCustomerForm(customer, this::loadCustomerData);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error editing customer.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteCustomer(int row) {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this customer?", "Delete Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int customerId = rowToVendorIdMap.get(row);
                customerDAO.deleteCustomer(customerId);
                loadCustomerData();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting customer.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
