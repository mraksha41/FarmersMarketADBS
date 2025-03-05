package ui;

import dao.ProductDAO;
import models.Product;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

public class EditProductForm extends JFrame {
    private ProductDAO productDAO = new ProductDAO();

    public EditProductForm(Product product, Runnable onProductUpdated) {
        setTitle("Edit Product");
        setSize(600, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2, 10, 10));

        JLabel nameLabel = new JLabel("Product Name:");
        JTextField nameField = new JTextField(product.getpName());

        JLabel categoryLabel = new JLabel("Product Category:");
        JTextField categoryField = new JTextField(product.getpCategory());

        JLabel priceLabel = new JLabel("Price:");
        JTextField priceField = new JTextField(product.getpPrice().toString());

        JLabel stockLabel = new JLabel("Stock Quantity:");
        JTextField stockField = new JTextField(String.valueOf(product.getQtyInStock()));

        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String category = categoryField.getText().trim();
            String priceText = priceField.getText().trim();
            String stockText = stockField.getText().trim();

            if (!name.isEmpty() && !priceText.isEmpty() && !stockText.isEmpty()) {
                try {
                    BigDecimal price = new BigDecimal(priceText);
                    int stock = Integer.parseInt(stockText);

                    product.setpName(name);
                    product.setpCategory(category);
                    product.setpPrice(price);
                    product.setQtyInStock(stock);

                    productDAO.updateProduct(product); // Update the product in the database
                    JOptionPane.showMessageDialog(this, "Product updated successfully!");

                    if (onProductUpdated != null) {
                        onProductUpdated.run(); // Trigger the callback to refresh the product list
                    }

                    dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Price and Stock must be valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error updating product.", "Error", JOptionPane.ERROR_MESSAGE);
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
