package ui;

import dao.AddressDAO;
import dao.MarketDAO;
import models.Address;
import models.Market;

import javax.swing.*;
import java.awt.*;

public class EditMarketForm extends JFrame {
    private MarketDAO marketDAO = new MarketDAO();
    private AddressDAO addressDAO = new AddressDAO();
    public EditMarketForm(Market market, Address address) {

        setTitle("Edit Market");
        setSize(600, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2, 10, 10));

        JLabel nameLabel = new JLabel("Market Name:");
        JTextField nameField = new JTextField(market.getMName());

        JLabel streetLabel = new JLabel("Street");
        JTextField streetField = new JTextField(address.getStreet());

        JLabel cityLabel = new JLabel("City");
        JTextField cityField = new JTextField(address.getCity());

        JLabel stateLabel = new JLabel("State");
        JTextField stateField = new JTextField(address.getState());

        JLabel zipCodeLabel = new JLabel("Zip Code:");
        JTextField zipCodeField = new JTextField(address.getZipCode());

        JLabel hoursLabel = new JLabel("Operating Hours:");
        JTextField hoursField = new JTextField(market.getOperatingHours());

        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            market.setMName(nameField.getText().trim());
            address.setStreet(streetField.getText().trim());
            address.setCity(cityField.getText().trim());
            address.setState(stateField.getText().trim());
            address.setZipCode(zipCodeField.getText().trim());
            market.setOperatingHours(hoursField.getText().trim());

            if (!market.getMName().isEmpty() && !address.getZipCode().isEmpty()) {
                try{
                    addressDAO.updateAddress(address);
                    marketDAO.updateMarket(market); // Update the market in the database
                    JOptionPane.showMessageDialog(this, "Market updated successfully!");
                    dispose();
                }catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error updating markets.");
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
