package ui;

import dao.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.function.Consumer;

public class TransactionHistoryUI extends JPanel {
    private Consumer<String> switchPanelCallback;
    private TransactionDAO transactionDAO = new TransactionDAO();
    private DefaultTableModel tableModel;
    private JTable transactionTable;

    public TransactionHistoryUI(Consumer<String> switchPanelCallback) {
        this.switchPanelCallback = switchPanelCallback;
        setLayout(new BorderLayout());
        add(new JLabel("Transaction History"), BorderLayout.NORTH);
        viewTransactions();
        setSearchPanel();
        setVisible(true);
    }

    private void viewTransactions(){
        tableModel = new DefaultTableModel(new String[]{
                "Date",
                "Customer",
                "Product",
                "Product Category",
                "Vendor",
                "Market",
                "Price",
                "Qty",
                "Sub Total"}, 0);

        transactionTable = new JTable(tableModel){};

        TableColumnModel columnModel = transactionTable.getColumnModel();

        columnModel.getColumn(0).setMinWidth(140);
        columnModel.getColumn(4).setMinWidth(140);
        columnModel.getColumn(5).setMinWidth(200);
        loadTransactionData("");

        add(new JScrollPane(transactionTable), BorderLayout.CENTER);
    }

    private void loadTransactionData(String filter) {
        // Simulated market data
        try {
            updateTransactionTableData(transactionDAO.getAllTransactionWithDetails(filter));
        }catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving transaction history.");
        }
    }

    private void updateTransactionTableData(List<List<String>> transactions) {
        tableModel.setRowCount(0);
        for (List<String> transaction : transactions) {
            System.out.println("TransactionHistoryUI::transaction: "+ transaction);
            tableModel.addRow(
                    new Object[]{
                            transaction.get(0),
                            transaction.get(1),
                            transaction.get(2),
                            transaction.get(3),
                            transaction.get(4),
                            transaction.get(5),
                            transaction.get(6),
                            transaction.get(7),
                            transaction.get(8)
                    }
            );
        }
    }

    private void setSearchPanel(){
        // Search Panel
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel searchLabel = new JLabel("Search Transaction:");
        JTextField searchField = new JTextField(15);
        JButton searchButton = new JButton("Search");
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        searchButton.addActionListener(e -> {
            String searchText = searchField.getText().trim();
            loadTransactionData(searchText);
        });

        JButton refreshButton = new JButton("Refresh");
        searchPanel.add(refreshButton);
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadTransactionData("");
            }
        });
        add(searchPanel, BorderLayout.SOUTH);
    }
}
