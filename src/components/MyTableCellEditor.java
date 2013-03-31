package components;

import gui.GUI;

import java.awt.Component;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;

public class MyTableCellEditor extends DefaultCellEditor{
	private static final long serialVersionUID = 1L;
	private GUI instance;
	
	public MyTableCellEditor(GUI instance){
		super(new JComboBox());
		this.instance = instance;
	}
	
	
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		DefaultCellEditor cellEdit = new DefaultCellEditor(new JComboBox());

		JComboBox combo = (JComboBox)super.getTableCellEditorComponent(table, value, isSelected, row, column);
		combo.removeAllItems();
		
		//get elements to be put in the combobox
		String serviceType = (String)table.getValueAt(row, column - 1);
		List<String> userList = instance.getMatchingUsers(serviceType);
		for(String user : userList) {
			combo.addItem(user);
		}
		
		
		return combo;
	}
}
