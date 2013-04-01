package components;

import gui.GUI;

import java.awt.Component;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import constants.StatusMessages;
import constants.Symbols;

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
		((JLabel)combo.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
		combo.removeAllItems();
		
		combo.addItem(StatusMessages.inactive.toUpperCase());
		
		return combo;
	}
}
