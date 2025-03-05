package ui;

import dao.VendorDAO;
import models.Vendor;

import javax.swing.*;
import java.awt.*;

public class EditVendorForm extends JFrame {
    private VendorDAO vendorDAO = new VendorDAO();

    public EditVendorForm(Vendor vendor) {
        setTitle("Edit Vendor");
        setSize(600, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2, 10, 10));

        JLabel nameLabel = new JLabel("Vendor Name:");
        JTextField nameField = new JTextField(vendor.getName());

        JLabel contactLabel = new JLabel("Contact Information:");
        JTextField contactField = new JTextField(vendor.getContactInfo());

        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String contact = contactField.getText().trim();

            if (!name.isEmpty() && !contact.isEmpty()) {
                try {
                    vendor.setName(name);
                    vendor.setContactInfo(contact);
                    vendorDAO.updateVendor(vendor); // Update the vendor in the database
                    JOptionPane.showMessageDialog(this, "Vendor updated successfully!");
                    dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Market ID must be a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error updating vendor.", "Error", JOptionPane.ERROR_MESSAGE);
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
