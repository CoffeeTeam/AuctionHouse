package components;

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;

public class ServicesCellEditor extends DefaultCellEditor {

	private static final long serialVersionUID = 4840398164521164852L;

	public ServicesCellEditor() {
		super(new JComboBox<String>());
	}
	
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {

		return null;
	}
}
