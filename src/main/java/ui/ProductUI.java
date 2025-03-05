package ui;

import dao.ProductDAO;
import dao.VendorDAO;
import models.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ProductUI extends JPanel {
    private int vendorId;
    private ProductDAO productDAO;
    private VendorDAO vendorDAO;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private Consumer<String> switchPanelCallback;
    private Map<Integer, Integer> rowToVendorIdMap = new HashMap<>();

    public ProductUI(Consumer<String> switchPanelCallback, int vendorId) {
        this.vendorId = vendorId;
        this.switchPanelCallback = switchPanelCallback;
        this.productDAO = new ProductDAO();
        this.vendorDAO = new VendorDAO();

        setLayout(new BorderLayout());
        add(new JLabel("Product Management"), BorderLayout.NORTH);

        initializeTable();
        initializeButtons();
        loadProductData();
    }

    private void initializeTable() {
        tableModel = new DefaultTableModel(new String[]{
                "Product ID", "Product Name", "Price", "Stock", "Edit", "Delete"}, 0);
        productTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4 || column == 5; // Only "Edit" and "Delete" columns are editable
            }
        };

        // Add custom editor and renderer for action buttons
        productTable.getColumnModel().getColumn(4).setCellRenderer(new ActionButtonRenderer());
        productTable.getColumnModel().getColumn(4).setCellEditor(new ActionButtonEditor("Edit", this::editProduct));
        productTable.getColumnModel().getColumn(5).setCellRenderer(new ActionButtonRenderer());
        productTable.getColumnModel().getColumn(5).setCellEditor(new ActionButtonEditor("Delete", this::deleteProduct));

        add(new JScrollPane(productTable), BorderLayout.CENTER);
    }

    private void initializeButtons() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addProductButton = new JButton("Add Product");
        JButton backButton = new JButton("Back");

        addProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddProductForm(vendorId, ProductUI.this::loadProductData);
            }
        });

        backButton.addActionListener(e -> switchPanelCallback.accept("VendorUI:" + vendorDAO.getMarketIdByVendorId(vendorId)));

        buttonPanel.add(addProductButton);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadProductData() {
        try {
            List<Product> products = productDAO.getProductsByVendorId(vendorId);
            updateProductTableData(products);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading products.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateProductTableData(List<Product> products) {
        tableModel.setRowCount(0); // Clear existing rows
        rowToVendorIdMap.clear(); // Clear previous mapping
        int rowIndex = 0;
        for (Product product : products) {
            tableModel.addRow(new Object[]{
                    product.getpName(),
                    product.getpCategory(),
                    product.getpPrice(),
                    product.getQtyInStock(),
                    "Edit",
                    "Delete"
            });
            rowToVendorIdMap.put(rowIndex, product.getpID()); // Map row index to vendorId
            rowIndex++;
        }
    }

    private void editProduct(int row) {
        try {
            int productId = rowToVendorIdMap.get(row);
            Product product = productDAO.getProductById(productId);
            new EditProductForm(product, ProductUI.this::loadProductData);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error editing product.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteProduct(int row) {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this product?", "Delete Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int productId = rowToVendorIdMap.get(row); // Get vendorId from the map
                productDAO.deleteProduct(productId);
                loadProductData(); // Refresh the table
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting product.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
