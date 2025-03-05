package ui;

import dao.ProductDAO;
import models.Product;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

public class AddProductForm extends JFrame {
    private ProductDAO productDAO = new ProductDAO();

    public AddProductForm(int vendorId, Runnable onProductAdded) {
        setTitle("Add Product");
        setSize(600, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2, 10, 10));

        JLabel nameLabel = new JLabel("Product Name:");
        JTextField nameField = new JTextField();

        JLabel categoryLabel = new JLabel("Product Category:");
        JTextField categoryField = new JTextField();

        JLabel priceLabel = new JLabel("Price:");
        JTextField priceField = new JTextField();

        JLabel stockLabel = new JLabel("Stock Quantity:");
        JTextField stockField = new JTextField();

        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String category = categoryField.getText().trim();
            String priceText = priceField.getText().trim();
            String stockText = stockField.getText().trim();

            if (!name.isEmpty() && !priceText.isEmpty() && !stockText.isEmpty() && vendorId > 0) {
                try {
                    BigDecimal price = new BigDecimal(priceText);
                    int stock = Integer.parseInt(stockText);

                    Product newProduct = new Product(0, name, category, price, stock, vendorId);
                    productDAO.addProduct(newProduct);
                    JOptionPane.showMessageDialog(this, "Product added successfully!");

                    if (onProductAdded != null) {
                        onProductAdded.run(); // Trigger the callback to refresh product list
                    }

                    dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Price and Stock must be valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error creating product.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> dispose());

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(categoryLabel);
        panel.add(categoryField);
        panel.add(priceLabel);
        panel.add(priceField);
        panel.add(stockLabel);
        panel.add(stockField);
        panel.add(saveButton);
        panel.add(cancelButton);

        add(panel);
        setVisible(true);
    }
}
