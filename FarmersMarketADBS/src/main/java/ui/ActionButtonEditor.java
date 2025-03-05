package ui;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

public class ActionButtonEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
    private final JButton button;
    private final Consumer<Integer> action;
    private int row;

    public ActionButtonEditor(String label, Consumer<Integer> action) {
        this.button = new JButton(label);
        this.action = action;
        button.addActionListener(this);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.row = row;
        button.setText(value != null ? value.toString() : "");
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return button.getText();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        action.accept(row);
        fireEditingStopped();
    }
}