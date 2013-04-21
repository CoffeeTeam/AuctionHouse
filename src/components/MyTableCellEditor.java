package components;

import gui.GUI;

import java.awt.Component;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import user.User;

import constants.StatusMessages;

public class MyTableCellEditor extends DefaultCellEditor {
	private static final long serialVersionUID = 1L;
	private GUI instanceGUI;

	public MyTableCellEditor(GUI instanceGUI) {

		super(new JComboBox<Object>());
		this.instanceGUI = instanceGUI;
	}

	@SuppressWarnings("unchecked")
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {

		String serviceName = (String) table.getValueAt(row, column - 1);
		HashMap<String, String> userStatusAssoc;
		User user = instanceGUI.getUser();

		
		// Check if a transfer has been started => 
		//					return null so as not to edit a thing
		if (user.getServiceTransfer(serviceName) != null)
			return null;
		
		// Add "Inactive" if the servicelist is empty
		if (user.isEmptyServiceList() || user.isEmptyService(serviceName)) {
			JLabel label = new JLabel(StatusMessages.inactive.toUpperCase());
			label.setHorizontalAlignment(SwingConstants.CENTER);
			return label;
		}

		// Add matching users to a combobox
		JComboBox<String> combo = (JComboBox<String>) super.getTableCellEditorComponent(
				table, value, isSelected, row, column);
		userStatusAssoc = instanceGUI.getUser().getServiceStatus(serviceName);
		Set<String> keySet = userStatusAssoc.keySet();
		Iterator<String> userNameIt = keySet.iterator();

		// Center elements in the combo
		((JLabel) combo.getRenderer())
				.setHorizontalAlignment(SwingConstants.CENTER);
		// Clear combo
		combo.removeAllItems();

		while (userNameIt.hasNext()) {
			String userName = userNameIt.next();
			String status = userStatusAssoc.get(userName);
			combo.addItem(userName + "   " + status.toUpperCase());
		}
		Object element = combo.getItemAt(0);
		combo.getModel().setSelectedItem(element);

		return combo;
	}
}
