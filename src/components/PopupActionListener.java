package components;

import gui.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTable;

public class PopupActionListener implements ActionListener{
	private JTable table;
	private int row;
	private int column;
	private GUI gui;
	
	public PopupActionListener(JTable table, int row, int column, GUI gui) {
		this.table = table;
		this.row = row;
		this.column = column;
		this.gui = gui;
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("Selected: " + arg0.getActionCommand());
		System.out.println("Element " + table.getValueAt(row, column));
	}

}
