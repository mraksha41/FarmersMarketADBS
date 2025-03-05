package ui;

import dao.AddressDAO;
import dao.MarketDAO;
import models.Address;
import models.Market;

import javax.swing.*;
import java.awt.*;

public class AddMarketForm extends JFrame {
    private MarketDAO marketDAO = new MarketDAO();
    private AddressDAO addressDAO = new AddressDAO();
    public AddMarketForm() {
        setTitle("Add Market");
        setSize(600, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2, 10, 10));

        JLabel nameLabel = new JLabel("Market Name:");
        JTextField nameField = new JTextField();

        JLabel streetLabel = new JLabel("Street");
        JTextField streetField = new JTextField();

        JLabel cityLabel = new JLabel("City");
        JTextField cityField = new JTextField();

        JLabel stateLabel = new JLabel("State");
        JTextField stateField = new JTextField();

        JLabel zipCodeLabel = new JLabel("Zip Code:");
        JTextField zipCodeField = new JTextField();

        JLabel hoursLabel = new JLabel("Operating Hours:");
        JTextField hoursField = new JTextField();

        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String street = streetField.getText().trim();
            String city = cityField.getText().trim();
            String state = stateLabel.getText().trim();
            String zipCode = zipCodeField.getText().trim();
            String hours = hoursField.getText().trim();

            if (!name.isEmpty() && !zipCode.isEmpty()) {
                try{
                    Address newAddress = new Address(0, street, city, state, zipCode);
                    int addressID = addressDAO.addAddress(newAddress);
                    if (addressID != -1) {
                        newAddress.setAddressID(addressID);

                        Market newMarket = new Market(0, name, hours, newAddress);
                        marketDAO.addMarket(newMarket);

                        JOptionPane.showMessageDialog(this, "Market added successfully!");
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to add address.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error creating markets.");
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> dispose());

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(streetLabel);
        panel.add(streetField);
        panel.add(cityLabel);
        panel.add(cityField);
        panel.add(stateLabel);
        panel.add(stateField);
        panel.add(zipCodeLabel);
        panel.add(zipCodeField);
        panel.add(hoursLabel);
        panel.add(hoursField);
        panel.add(saveButton);
        panel.add(cancelButton);

        add(panel);
        setVisible(true);
    }
}
