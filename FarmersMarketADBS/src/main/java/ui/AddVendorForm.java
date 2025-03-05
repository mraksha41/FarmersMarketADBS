package ui;

import dao.VendorDAO;
import models.Vendor;

import javax.swing.*;
import java.awt.*;

public class AddVendorForm extends JFrame {
    private VendorDAO vendorDAO = new VendorDAO();

    public AddVendorForm(int marketId, Runnable onVendorAdded) {
        setTitle("Add Vendor");
        setSize(600, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2, 10, 10));

        JLabel nameLabel = new JLabel("Vendor Name:");
        JTextField nameField = new JTextField();

        JLabel contactLabel = new JLabel("Contact Information:");
        JTextField contactField = new JTextField();

        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String contact = contactField.getText().trim();

            if (!name.isEmpty() && !contact.isEmpty() && marketId>0) {
                try {
                    Vendor newVendor = new Vendor(0, name, contact, marketId);
                    vendorDAO.addVendor(newVendor);
                    JOptionPane.showMessageDialog(this, "Vendor added successfully!");
                    if (onVendorAdded != null) {
                        onVendorAdded.run(); // Trigger the callback to refresh customer list
                    }
                    dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Market ID must be a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error creating vendor.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> dispose());

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(contactLabel);
        panel.add(contactField);
        panel.add(saveButton);
        panel.add(cancelButton);

        add(panel);
        setVisible(true);
    }
}