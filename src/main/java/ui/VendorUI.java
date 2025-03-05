// VendorUI.java
package ui;

import dao.VendorDAO;
import models.Vendor;

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

public class VendorUI extends JPanel {
    private Integer marketId; // Nullable to allow for "no marketId" case
    private VendorDAO vendorDAO;

    private JTable vendorTable;
    private DefaultTableModel tableModel;
    private Consumer<String> switchPanelCallback;
    private Map<Integer, Integer> rowToVendorIdMap = new HashMap<>();

    public VendorUI(Consumer<String> switchPanelCallback, int MID) {
        marketId = MID;
        this.switchPanelCallback = switchPanelCallback;
        setLayout(new BorderLayout());
        add(new JLabel("Vendor Management "), BorderLayout.NORTH);

        // Initialize VendorDAO
        vendorDAO = new VendorDAO();
        viewVendors();
        setSearchPanel();
    }

    private void viewVendors(){
        // Create Table for Vendor List
        tableModel = new DefaultTableModel(new String[]{
                "Vendor Name", "Contact", "View Products", "Edit", "Delete"}, 0);
        vendorTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2 || column == 3 || column == 4; // Only the "Products", "Edit" and "Delete" columns are editable
            }
        };

        // Add custom renderer and editor for "Edit" and "Delete" columns
        TableColumnModel columnModel = vendorTable.getColumnModel();
        columnModel.getColumn(2).setCellRenderer(new ActionButtonRenderer());
        columnModel.getColumn(2).setCellEditor(new ActionButtonEditor("View Products", this::viewProduct));
        columnModel.getColumn(3).setCellRenderer(new ActionButtonRenderer());
        columnModel.getColumn(3).setCellEditor(new ActionButtonEditor("Edit", this::editVendor));
        columnModel.getColumn(4).setCellRenderer(new ActionButtonRenderer());
        columnModel.getColumn(4).setCellEditor(new ActionButtonEditor("Delete", this::deleteVendor));

        columnModel.getColumn(0).setMinWidth(140);
        columnModel.getColumn(1).setMinWidth(200);
        columnModel.getColumn(2).setPreferredWidth(40);
        columnModel.getColumn(3).setPreferredWidth(20);
        columnModel.getColumn(4).setPreferredWidth(20);

        // Load initial data
        loadVendorData();

        // Add table to scroll pane
        add(new JScrollPane(vendorTable), BorderLayout.CENTER);
    }

    private void setSearchPanel(){
        // Add buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addVendorButton = new JButton("Add Vendor");
        addVendorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddVendorForm(marketId,VendorUI.this::loadVendorData);
            }


        });
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchPanelCallback.accept("VendorUI:" + marketId); // Switch to Product panel
            }


        });
        buttonPanel.add(addVendorButton);
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void loadVendorData() {
        try {
            List<Vendor> vendors;
            if (marketId != null && marketId !=-1) {
                vendors = vendorDAO.getVendorsByMarketId(marketId); // Filter by market ID
            } else {
                vendors = vendorDAO.getAllVendors(); // Load all vendors
            }
            updateVendorTableData(vendors);
        }catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving vendors.");
        }
    }

    private void updateVendorTableData(List<Vendor> vendors){
        tableModel.setRowCount(0); // Clear existing rows
        rowToVendorIdMap.clear(); // Clear previous mapping

        int rowIndex = 0;
        for (Vendor vendor : vendors) {
            tableModel.addRow(new Object[]{
                    vendor.getName(),
                    vendor.getContactInfo(),
                    "Product",
                    "Edit",
                    "Delete"
            });
            rowToVendorIdMap.put(rowIndex, vendor.getVendorId()); // Map row index to vendorId
            rowIndex++;
        }
    }

    private void editVendor(int row) {
        try {
            int vendorId = rowToVendorIdMap.get(row); // Get vendorId from the map
            Vendor vendor = vendorDAO.getVendorById(vendorId);
            new EditVendorForm(vendor);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error editing vendor.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteVendor(int row) {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this vendor?", "Delete Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int vendorId = rowToVendorIdMap.get(row); // Get vendorId from the map
                vendorDAO.deleteVendor(vendorId);
                loadVendorData();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting vendor.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void viewProduct(int row){
        try {
            int vendorId = rowToVendorIdMap.get(row);
            System.out.println("Switch to product panel for: " + vendorId);
            switchPanelCallback.accept("ProductUI:" + vendorId); // Switch to Product panel
        }catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving vendors.");
        }

    }


}
