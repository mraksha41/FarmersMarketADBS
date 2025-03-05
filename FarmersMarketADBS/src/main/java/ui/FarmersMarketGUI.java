package ui;

import dao.VendorDAO;

import javax.swing.*;
import java.awt.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;


public class FarmersMarketGUI extends JFrame {
    private JPanel mainPanel; // Panel to hold all cards
    private CardLayout cardLayout; // CardLayout for switching panels
    private VendorDAO vendorDAO;

    public FarmersMarketGUI() {
        vendorDAO = new VendorDAO();

        setTitle("Farmers Market Management");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Menu Bar
        JMenuBar menuBar = new JMenuBar();
        JMenu marketMenu = new JMenu("Market");
        JMenu vendorMenu = new JMenu("Vendor");
        JMenu productMenu = new JMenu("Product");
        JMenu customerMenu = new JMenu("Customer");
        JMenu transactionMenu = new JMenu("Transaction");
        JMenu transactionHistoryMenu = new JMenu("Transaction History");
        JMenu reportMenu = new JMenu("Revenue Report");
        JMenu inventoryMenu = new JMenu("Inventory Report");

        marketMenu.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                showPanel("MarketUI");
            }

            @Override
            public void menuDeselected(MenuEvent e) {
                // Do nothing on deselect
            }

            @Override
            public void menuCanceled(MenuEvent e) {
                // Do nothing on cancel
            }
        });
        vendorMenu.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                showPanel("VendorUI");
            }

            @Override
            public void menuDeselected(MenuEvent e) {
                // Do nothing on deselect
            }

            @Override
            public void menuCanceled(MenuEvent e) {
                // Do nothing on cancel
            }
        });
        productMenu.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                showPanel("ProductUI");
            }

            @Override
            public void menuDeselected(MenuEvent e) {
                // Do nothing on deselect
            }

            @Override
            public void menuCanceled(MenuEvent e) {
                // Do nothing on cancel
            }
        });
        customerMenu.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                showPanel("CustomerUI");
            }

            @Override
            public void menuDeselected(MenuEvent e) {
                // Do nothing on deselect
            }

            @Override
            public void menuCanceled(MenuEvent e) {
                // Do nothing on cancel
            }
        });
        transactionMenu.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                showPanel("TransactionUI");
            }

            @Override
            public void menuDeselected(MenuEvent e) {
                // Do nothing on deselect
            }

            @Override
            public void menuCanceled(MenuEvent e) {
                // Do nothing on cancel
            }
        });
        transactionHistoryMenu.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                showPanel("TransactionHistoryUI");
            }

            @Override
            public void menuDeselected(MenuEvent e) {
                // Do nothing on deselect
            }

            @Override
            public void menuCanceled(MenuEvent e) {
                // Do nothing on cancel
            }
        });
        inventoryMenu.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                showPanel("InventoryUI");
            }

            @Override
            public void menuDeselected(MenuEvent e) {
                // Do nothing on deselect
            }

            @Override
            public void menuCanceled(MenuEvent e) {
                // Do nothing on cancel
            }
        });
        reportMenu.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                showPanel("ReportUI");
            }

            @Override
            public void menuDeselected(MenuEvent e) {
                // Do nothing on deselect
            }

            @Override
            public void menuCanceled(MenuEvent e) {
                // Do nothing on cancel
            }
        });

        menuBar.add(marketMenu);
        menuBar.add(vendorMenu);
        menuBar.add(productMenu);
        menuBar.add(customerMenu);
        menuBar.add(transactionMenu);
        menuBar.add(transactionHistoryMenu);
        menuBar.add(inventoryMenu);
        menuBar.add(reportMenu);
        setJMenuBar(menuBar);

        // Main Panel with CardLayout
        mainPanel = new JPanel();
        cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);

        // Adding panels to CardLayout
        mainPanel.add(new MarketUI(this::switchPanel), "MarketUI");
        mainPanel.add(new VendorUI(this::switchPanel, -1), "VendorUI");
        mainPanel.add(new ProductUI(this::switchPanel,-1), "ProductUI");
        mainPanel.add(new CustomerUI(this::switchPanel), "CustomerUI");
        mainPanel.add(new TransactionUI(this::switchPanel), "TransactionUI");
        mainPanel.add(new TransactionHistoryUI(this::switchPanel), "TransactionHistoryUI");
        mainPanel.add(new InventoryUI(this::switchPanel), "InventoryUI");
        mainPanel.add(new ReportUI(), "ReportUI");

        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);

    }

    // Method to switch panels
    private void showPanel(String panelName) {
        System.out.println("Switching to panel: " + panelName); // Debugging log
        cardLayout.show(mainPanel, panelName);
    }

    private void switchPanel(String panelName) {
        if (panelName.contains("MarketUI")) {
            MarketUI marketUI = new MarketUI(this::switchPanel);
            mainPanel.add(marketUI, "MarketUI");
            cardLayout.show(mainPanel, panelName.split(":" )[0]);
        } else if(panelName.contains("VendorUI:")) {
            int marketId = Integer.parseInt(panelName.split(":" )[1]);
            VendorUI vendorUI = new VendorUI(this::switchPanel, marketId);
            mainPanel.add(vendorUI, "VendorUI");
            cardLayout.show(mainPanel, panelName.split(":" )[0]);
        } else if(panelName.contains("ProductUI:")) {
            int vendorId = Integer.parseInt(panelName.split(":" )[1]);
            ProductUI productUI = new ProductUI(this::switchPanel, vendorId);
            mainPanel.add(productUI, "ProductUI");
            cardLayout.show(mainPanel, panelName.split(":" )[0]);
        } else {
            System.out.println("Switching to Panel: " + panelName);
            cardLayout.show(mainPanel, panelName);
        }
    }

    public static void main(String[] args) {
        System.out.println("Hello, Farmers Market!");

        SwingUtilities.invokeLater(() -> {
            FarmersMarketGUI gui = new FarmersMarketGUI();
            gui.setVisible(true);
        });
    }
}
