package ui;

import dao.CustomerDAO;
import models.Customer;

import javax.swing.*;
import java.awt.*;

public class AddCustomerForm extends JFrame {
    private CustomerDAO customerDAO = new CustomerDAO();

    public AddCustomerForm(Runnable onCustomerAdded) {
        setTitle("Add Customer");
        setSize(600, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2, 10, 10));

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();

        JLabel phoneLabel = new JLabel("Phone:");
        JTextField phoneField = new JTextField();

        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();

            if (!name.isEmpty() && !email.isEmpty() && !phone.isEmpty()) {
                try {
                    Customer newCustomer = new Customer(0, name, email, phone);
                    customerDAO.addCustomer(newCustomer);
                    JOptionPane.showMessageDialog(this, "Customer added successfully!");

                    if (onCustomerAdded != null) {
                        onCustomerAdded.run(); // Trigger the callback to refresh customer list
                    }

                    dispose();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error creating customer.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> dispose());

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(phoneLabel);
        panel.add(phoneField);
        panel.add(saveButton);
        panel.add(cancelButton);

        add(panel);
        setVisible(true);
    }
}
