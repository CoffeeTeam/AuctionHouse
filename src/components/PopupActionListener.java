package components;

import gui.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTable;

import constants.ComponentNames;

public class PopupActionListener implements ActionListener {
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
		String actionName = arg0.getActionCommand();
		String serviceName = null;
		String user = null;

		if (column == 1) {
			user = ((String) table.getValueAt(row, column)).split("\\s+")[0];
			serviceName = (String) table.getValueAt(row, column - 1);
		} else {
			serviceName = (String) table.getValueAt(row, column);
		}
		System.out.println("Element " + table.getValueAt(row, column));

		// verify action name

		// Launch Offer Request
		if (actionName.equals(ComponentNames.buyerServiceMenu[0])) {
			gui.getMed().launchService(serviceName, gui.getUser().getUsername());
			gui.launchOfferRequest(serviceName);
		} else
		// Drop offer request
		if (actionName.equals(ComponentNames.buyerServiceMenu[1])) {
			gui.dropOfferRequest(serviceName);
		} else
		// Accept offer
		if (actionName.equals(ComponentNames.buyerUserMenu[0])) {
			gui.getMed().acceptOfferGui(user, serviceName);
			gui.acceptOffer(serviceName, user);
		} else
		// refuse offer
		if (actionName.equals(ComponentNames.buyerUserMenu[1])) {
			gui.refuseOffer(serviceName, user);
		} else
		// Make offer
		if (actionName.equals(ComponentNames.sellerServiceMenu[0])) {
			gui.getMed().launchService(serviceName, gui.getUser().getUsername(), user);
			gui.updateBuyersStatus(serviceName);
		} else
		// Drop auction
		if (actionName.equals(ComponentNames.sellerServiceMenu[1])) {
			gui.getMed().dropService(serviceName, gui.getUser().getUsername());
			gui.dropOfferRequest(serviceName);
		}

	}

}
