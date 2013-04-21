package components;

import gui.GUI;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import user.User;
import user.UserTransferStatus;
import constants.StatusMessages;
import constants.Symbols;

public class MyTableCellRenderer extends DefaultTableCellRenderer{
	private static final long serialVersionUID = 1L;
	private GUI instanceGUI;

	public MyTableCellRenderer(GUI instanceGUI) {
		super();
		this.instanceGUI = instanceGUI;
		setHorizontalAlignment(JLabel.CENTER);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, 
		boolean isSelected, boolean hasFocus, int row, int column) {  

		String serviceName = (String) table.getValueAt(row, column - 1);
		User user = instanceGUI.getUser();
		JLabel label;

		// Check if transfer active for this 
		UserTransferStatus transferStatus = user.getServiceTransfer(serviceName);
		if (transferStatus != null) {
			JPanel panel = new JPanel(new GridLayout(1, 2));
			JProgressBar progress = new JProgressBar(0, 100);
			JLabel progressLabel = new JLabel(
					transferStatus.getSeller()+" - "+transferStatus.getStatus());
			
			progress.setValue(transferStatus.getProgress());
			progress.setStringPainted(true);
			progress.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));
			progress.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
			
			panel.add(progressLabel);
			panel.add(progress);
			
			return panel;
		}
		
		// Add "Inactive" if the servicelist is empty
		if (user.isEmptyServiceList() || user.isEmptyService(serviceName)) {
			label = new JLabel(StatusMessages.inactive.toUpperCase());
			label.setHorizontalAlignment(SwingConstants.CENTER);
			
			return label;
		}

		// Return a choose option message if no option from the combo
		// has yet been chosen
		String val = (String)value;
		
		if (val.equalsIgnoreCase(StatusMessages.inactive)) {
			label = new JLabel(Symbols.chooseString);
			label.setHorizontalAlignment(SwingConstants.CENTER);
			return label;
		}
		
		
		// Return selection from combo (if unchanged)
		HashMap<String, String> matches = 
				instanceGUI.getUser().getServiceStatus(serviceName);
		Set<String> users = matches.keySet();
		boolean unchanged = false;
		String newStatus = null;
		
		for (String match : users) {
			if (val.contains(match)) {
				if (val.contains(matches.get(match))) {
					unchanged = true;
					break;
				}
				
				newStatus = match + " - " + matches.get(match);
			}
		}
		
		if (!unchanged) {
			val = newStatus;
		}
		
		label = new JLabel(val);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		
		return label;
	}
}
