package ui;

import dao.*;
import models.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class TransactionUI extends JPanel {
    private CustomerDAO customerDAO = new CustomerDAO();
    private MarketDAO marketDAO = new MarketDAO();
    private VendorDAO vendorDAO = new VendorDAO();
    private ProductDAO productDAO = new ProductDAO();
    private TransactionDAO transactionDAO = new TransactionDAO();

    private JComboBox<Customer> customerComboBox;
    private JComboBox<Market> marketComboBox;
    private JComboBox<Vendor> vendorComboBox;
    private JComboBox<Product> productComboBox;
    private JComboBox<Integer> qtyComboBox;
    private JButton addToCartButton;
    private JTable cartTable;
    private DefaultTableModel cartTableModel;
    private JLabel totalAmountLabel;

    private JTable productTable;
    private DefaultTableModel productTableModel;
    private List<Product> selectedProducts = new ArrayList<>();
    private Map<Integer, Integer> rowIdMap = new HashMap<>();
    int rowIndex = 0;

    public TransactionUI(Consumer<String> switchPanelCallback) {
        setLayout(new BorderLayout());

        // Top Panel: Selection
        JPanel topPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        customerComboBox = new JComboBox<>(getCustomers().toArray(new Customer[0]));
        marketComboBox = new JComboBox<>(getMarkets().toArray(new Market[0]));
        vendorComboBox = new JComboBox<>();
        productComboBox = new JComboBox<>();
        qtyComboBox = new JComboBox<>();
        addToCartButton = new JButton("Add to Cart");


        customerComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Customer) {
                    Customer customer = (Customer) value;
                    setText(customer.getName()); // Display only the vendor name
                }
                return this;
            }
        });

        marketComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Market) {
                    Market market = (Market) value;
                    setText(market.getMName());
                }
                return this;
            }
        });

        vendorComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Vendor) {
                    Vendor vendor = (Vendor) value;
                    setText(vendor.getName());
                }
                return this;
            }
        });

        productComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Product) {
                    Product product = (Product) value;
                    setText(product.getpName()+" ($" + product.getpPrice()+")");
                }
                return this;
            }
        });

        marketComboBox.addActionListener(e -> {
            Market selectedMarket = (Market) marketComboBox.getSelectedItem();
            if (selectedMarket != null) {
                updateVendors(selectedMarket.getMID());
            }
        });

        vendorComboBox.addActionListener(e -> {
            Vendor selectedVendor = (Vendor) vendorComboBox.getSelectedItem();
            if (selectedVendor != null) {
                updateProducts(selectedVendor.getVendorId());
            }
        });

        productComboBox.addActionListener(e -> {
            Product selectedProduct = (Product) productComboBox.getSelectedItem();
            if (selectedProduct != null) {
                updateQty(selectedProduct.getQtyInStock());
            }
        });

        topPanel.add(new JLabel("Select Customer:"));
        topPanel.add(customerComboBox);
        topPanel.add(new JLabel("Select Market:"));
        topPanel.add(marketComboBox);
        topPanel.add(new JLabel("Select Vendor:"));
        topPanel.add(vendorComboBox);
        topPanel.add(new JLabel("Select Product:"));
        topPanel.add(productComboBox);
        topPanel.add(new JLabel("Enter Quantity:"));
        topPanel.add(qtyComboBox);
        topPanel.add(new JLabel(""));
        topPanel.add(addToCartButton);
        add(topPanel, BorderLayout.NORTH);

        // Center Panel: Cart Table
        cartTableModel = new DefaultTableModel(new String[]{"Product", "Quantity", "Price", "Total"}, 0);
        cartTable = new JTable(cartTableModel);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder("Cart"));
        centerPanel.add(new JScrollPane(cartTable), BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // Bottom Panel: Total Amount and Confirm Button
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        totalAmountLabel = new JLabel("Total Amount: $0.00");
        JButton confirmTransactionButton = new JButton("Confirm Transaction");
        confirmTransactionButton.addActionListener(e -> confirmTransaction());

        bottomPanel.add(totalAmountLabel, BorderLayout.WEST);
        bottomPanel.add(confirmTransactionButton, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);

        // Add-to-Cart Button Listener
        addToCartButton.addActionListener(e -> addToCart());

        setVisible(true);
    }

    private List<Customer> getCustomers() {
        try {
            return customerDAO.getAllCustomers();
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

    private List<Market> getMarkets() {
        try {
            return marketDAO.getAllMarkets();
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }

    }

    private void updateVendors(int marketId) {
        vendorComboBox.removeAllItems();
        List<Vendor> vendors = getVendorsByMarket(marketId); // Fetch vendors for the selected market
        for (Vendor vendor : vendors) {
            vendorComboBox.addItem(vendor);
        }
    }

    private void updateProducts(int vendorId) {
        productComboBox.removeAllItems();
        List<Product> products = getProductsByVendorId(vendorId); // Fetch vendors for the selected market
        for (Product product : products) {
            productComboBox.addItem(product);
        }
    }

    private void updateQty(int qty) {
        qtyComboBox.removeAllItems();
        for(int i = 1; i <=qty; i++){
            qtyComboBox.addItem(i);
        }
    }

    private List<Vendor> getVendorsByMarket(int marketId) {
        // Replace with actual query to fetch vendors for the selected market
        return vendorDAO.getVendorsByMarketId(marketId);
    }

    private List<Product> getProductsByVendorId(int vendorId) {
        List<Product> products = productDAO.getProductsByVendorId(vendorId);
        return products;
    }


    private void addToCart() {
        Customer customer = (Customer) customerComboBox.getSelectedItem();
        Market market = (Market) marketComboBox.getSelectedItem();
        Vendor vendor = (Vendor) vendorComboBox.getSelectedItem();
        Product product = (Product) productComboBox.getSelectedItem();
        Integer quantity = (Integer) qtyComboBox.getSelectedItem();

        if (customer == null || market == null || vendor == null || product == null || quantity == null) {
            JOptionPane.showMessageDialog(this, "Please select all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            BigDecimal totalPrice = product.getpPrice().multiply(BigDecimal.valueOf(quantity));

            // Add to transaction database (placeholder logic, replace with actual database call)
            System.out.println("Transaction added: Customer " + customer.getName() + ", Product: " + product.getpName());

            // Add to Cart Table
            cartTableModel.addRow(new Object[]{
                    product.getpName(),
                    quantity,
                    "$" + product.getpPrice(),
                    "$" + totalPrice
            });
            rowIdMap.put(rowIndex, product.getpID()); // Map row index to vendorId
            rowIndex++;

            updateTotalAmount();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to add to cart.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeFromCart(int row) {
        cartTableModel.removeRow(row);
        updateTotalAmount();
    }

    private void updateTotalAmount() {
        BigDecimal totalAmount = BigDecimal.ZERO;
        try {
            for (int i = 0; i < cartTableModel.getRowCount(); i++) {
                String priceString = (String) cartTableModel.getValueAt(i, 2); // Example: "$3.50"
                String quantityString = cartTableModel.getValueAt(i, 1).toString(); // Example: "2"
                BigDecimal price = new BigDecimal(priceString.replace("$", "").trim());
                int quantity = Integer.parseInt(quantityString.trim());
                totalAmount = totalAmount.add(price.multiply(BigDecimal.valueOf(quantity)));
            }
            totalAmountLabel.setText("Total Amount: $" + totalAmount);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid data in the cart table. Please check the price or quantity.", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void confirmTransaction() {
        if (cartTableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No items in the cart to confirm the transaction.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Customer customer = (Customer) customerComboBox.getSelectedItem();
            if (customer == null) {
                JOptionPane.showMessageDialog(this, "Please select a customer.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Add Transaction
            String totalAmountString = totalAmountLabel.getText().replace("Total Amount: $","");
            BigDecimal totalAmount = new BigDecimal(totalAmountString.trim());
            Transaction transaction = new Transaction(0, customer.getCustomerId(), LocalDateTime.now(), totalAmount);
            int transactionId = transactionDAO.addTransaction(transaction);

            if (transactionId == -1) {
                JOptionPane.showMessageDialog(this, "Failed to create transaction.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Add Transaction Details
            for (int i = 0; i < cartTableModel.getRowCount(); i++) {
                int productId = rowIdMap.get(i);
                int quantity = (int) cartTableModel.getValueAt(i, 1);
                String priceString = (String) cartTableModel.getValueAt(i, 2);
                BigDecimal price = new BigDecimal(priceString.replace("$", "").trim());
                BigDecimal totalPrice = price.multiply(BigDecimal.valueOf(quantity));

                TransactionDetail detail = new TransactionDetail(0, transactionId, productId, quantity, totalPrice);
                transactionDAO.addTransactionDetail(detail);
            }

            // Display Confirmation and Reset Cart
            JOptionPane.showMessageDialog(this, "Transaction confirmed successfully!");
            reset();


        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error confirming transaction: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void reset(){
        cartTableModel.setRowCount(0); // Clear the cart
        updateTotalAmount(); // Reset the total amount label
        rowIdMap  = new HashMap<>();
        rowIndex = 0;
//        TransactionHistoryUI.loadTransactionData("");
    }

    // Placeholder for retrieving Product by name (Replace with actual database query)
    private Product getProductByName(String productName) {
        ProductDAO productDAO = new ProductDAO();
        List<Product> products = productDAO.getAllProducts();
        return products.stream()
                .filter(product -> product.getpName().equalsIgnoreCase(productName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Product not found: " + productName));
    }

}
