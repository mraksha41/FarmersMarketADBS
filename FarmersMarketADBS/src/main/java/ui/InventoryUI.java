package ui;

import dao.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.function.Consumer;

public class InventoryUI extends JPanel {
    private Consumer<String> switchPanelCallback;
    private ProductDAO productDAO = new ProductDAO();
    private DefaultTableModel tableModel;
    private JTable inventoryTable;

    public InventoryUI(Consumer<String> switchPanelCallback) {
        this.switchPanelCallback = switchPanelCallback;
        setLayout(new BorderLayout());
        add(new JLabel("Inventory Report"), BorderLayout.NORTH);
        viewInventory();
        setSearchPanel();
        setVisible(true);
    }

    private void viewInventory(){
        tableModel = new DefaultTableModel(new String[]{
                "Product",
                "Vendor",
                "Market",
                "Quantity in Stock"
        }, 0);

        inventoryTable = new JTable(tableModel){};
        loadInventoryData();
        add(new JScrollPane(inventoryTable), BorderLayout.CENTER);
    }

    private void loadInventoryData() {
        // Simulated market data
        try {
            updateInventoryTableData(productDAO.getInventoryDetail());
        }catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving inventory data.");
        }
    }

    private void updateInventoryTableData(List<List<String>> inventory) {
        tableModel.setRowCount(0);
        for (List<String> product : inventory) {
            System.out.println("InventoryUI::Inventory: "+ product);
            tableModel.addRow(
                    new Object[]{
                            product.get(0),
                            product.get(1),
                            product.get(2),
                            product.get(3)
                    }
            );
        }
    }

    private void setSearchPanel(){
        // Search Panel
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JButton refreshButton = new JButton("Refresh");
        searchPanel.add(refreshButton);
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadInventoryData();
            }
        });
        add(searchPanel, BorderLayout.SOUTH);
    }
}
