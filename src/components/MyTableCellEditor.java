package components;

import gui.GUI;

import java.awt.Component;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import constants.StatusMessages;
import constants.Symbols;

public class MyTableCellEditor extends DefaultCellEditor {
	private static final long serialVersionUID = 1L;
	private GUI instanceGUI;

	public MyTableCellEditor(GUI instanceGUI) {
		super(new JComboBox());
		this.instanceGUI = instanceGUI;
	}

	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {

		DefaultCellEditor cellEdit = new DefaultCellEditor(new JComboBox());

		JComboBox combo = (JComboBox) super.getTableCellEditorComponent(table,
				value, isSelected, row, column);
		((JLabel) combo.getRenderer())
				.setHorizontalAlignment(SwingConstants.CENTER);
		combo.removeAllItems();

		String serviceName = (String) table.getValueAt(row, column - 1);
		HashMap<String, String> userStatusAssoc;

		if (instanceGUI.getUser().isEmptyServiceList()
				|| instanceGUI.getUser().isEmptyService(serviceName))
			combo.addItem(StatusMessages.inactive.toUpperCase());
		else {
			userStatusAssoc = instanceGUI.getUser().getUserStatus(serviceName);
			Set<String> keySet = userStatusAssoc.keySet();
			Iterator<String> userNameIt = keySet.iterator();

			while (userNameIt.hasNext()) {
				String userName = userNameIt.next();
				String status = userStatusAssoc.get(userName);
				combo.addItem(userName + "   " + status.toUpperCase());
			}
			Object element = combo.getItemAt(0);
			combo.getModel().setSelectedItem(element);
		}

		return combo;
	}
}
