// MarketUI.java
package ui;

import dao.AddressDAO;
import dao.MarketDAO;
import models.Address;
import models.Market;

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

public class MarketUI extends JPanel {
    private MarketDAO marketDAO;
    private AddressDAO addressDAO;
    private JTable marketTable;
    private DefaultTableModel tableModel;
    private Consumer<String> switchPanelCallback;
    private Map<Integer, Integer> rowToVendorIdMap = new HashMap<>();

    public MarketUI(Consumer<String> switchPanelCallback) {
        this.switchPanelCallback = switchPanelCallback;
        setLayout(new BorderLayout());
        add(new JLabel("Market Management"), BorderLayout.NORTH);

        // Initialize MarketDAO
        marketDAO = new MarketDAO();
        addressDAO = new AddressDAO();

        viewMarkets();
        setSearchPanel();
    }

    private void viewMarkets(){
        // Table for Market List
        tableModel = new DefaultTableModel(new String[]{
                "Market Name",
                "Location",
                "Operating Hours",
                "View Vendors",
                "Edit",
                "Delete"}, 0);


        marketTable = new JTable(tableModel){
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3 || column == 4 || column == 5; // Only the "Vendors", "Edit" and "Delete" column are editable
            }
        };

        TableColumnModel columnModel = marketTable.getColumnModel();
        columnModel.getColumn(3).setCellRenderer(new ActionButtonRenderer());
        columnModel.getColumn(3).setCellEditor(new ActionButtonEditor("Vendors", this::viewVendors));
        columnModel.getColumn(4).setCellRenderer(new ActionButtonRenderer());
        columnModel.getColumn(4).setCellEditor(new ActionButtonEditor("Edit", this::editMarket));
        columnModel.getColumn(5).setCellRenderer(new ActionButtonRenderer());
        columnModel.getColumn(5).setCellEditor(new ActionButtonEditor("Delete", this::deleteMarket));


        columnModel.getColumn(0).setMinWidth(140);
        columnModel.getColumn(1).setMinWidth(200);
        columnModel.getColumn(3).setPreferredWidth(40);
        columnModel.getColumn(4).setPreferredWidth(20);
        columnModel.getColumn(5).setPreferredWidth(20);

        loadMarketData();

        add(new JScrollPane(marketTable), BorderLayout.CENTER);

    }

    private void setSearchPanel(){
        // Search Panel
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel searchLabel = new JLabel("Search Market by Zip Code:");
        JTextField searchField = new JTextField(15);
        JButton searchButton = new JButton("Search");
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        searchButton.addActionListener(e -> {
            String searchText = searchField.getText().trim();
            if (!searchText.isEmpty()) {
                filterMarketData(searchText);
            } else {
                loadMarketData(); // Reset to all markets if search is empty
            }
        });

        JButton addMarketButton = new JButton("Add Market");
        searchPanel.add(addMarketButton);
        addMarketButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddMarketForm();
            }
        });

        JButton refreshButton = new JButton("Refresh");
        searchPanel.add(refreshButton);
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchPanelCallback.accept("MarketUI"); // Switch to Product panel
            }


        });

        add(searchPanel, BorderLayout.SOUTH);
    }

    private void loadMarketData() {
        // Simulated market data
        try {
            updateMarketTableData(marketDAO.getAllMarkets());

        }catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving markets.");
        }
    }

    private void viewVendors(int row){
        try {
            Market market = marketDAO.getAllMarkets().get(row);
            System.out.println("Switch to vendor panel for: " + market.getMID());
            switchPanelCallback.accept("VendorUI:" + market.getMID()); // Switch to vendors panel
        }catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving vendors.");
        }

    }

    private void editMarket(int row) {
        try {
            int marketId = rowToVendorIdMap.get(row); // Get vendorId from the map
            System.out.println("MarketUI:::::: row "+ row);
            System.out.println("MarketUI:::::: marketId "+ marketId);
            Market market = marketDAO.getMarketById(marketId);
            Address address = addressDAO.getAddressById(market.getAddressID());
            new EditMarketForm(market, address);
        }catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving markets.");
        }
    }

    private void deleteMarket(int row) {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this market?", "Delete Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int marketId = rowToVendorIdMap.get(row); // Get vendorId from the map
                Market market = marketDAO.getMarketById(marketId);

                marketDAO.deleteMarket(market.getMID());
                loadMarketData(); // Refresh the table after deletion
            }catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error retrieving markets.");
            }
        }
    }

    private void filterMarketData(String searchText) {

        List<Market> filteredMarkets = marketDAO.getMarketsBySearch(searchText);
        updateMarketTableData(filteredMarkets);
    }

    private void updateMarketTableData(List<Market> markets) {
        tableModel.setRowCount(0); // Clear existing rows
        rowToVendorIdMap.clear(); // Clear previous mapping
        int rowIndex = 0;
        for (Market market : markets) {
            tableModel.addRow(
                    new Object[]{
                            market.getMName(),
                            getAddressDetails(market.getAddressID()),
                            market.getOperatingHours(),
                            "Vendors",
                            "Edit",
                            "Delete"}
            );
            rowToVendorIdMap.put(rowIndex, market.getMID()); // Map row index to vendorId
            rowIndex++;
        }
    }

    private String getAddressDetails(int addressID){
        Address address = AddressDAO.getAddressById(addressID);
        return address.getStreet() + ", " + address.getCity() + ", " + address.getState() + ", " + address.getZipCode();
    }


}

