package components;

import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class JPanelRenderer extends JPanel implements TableCellRenderer{
	private static final long serialVersionUID = 1L;

	@Override
	public Component getTableCellRendererComponent(
							JTable table, Object value,
				            boolean isSelected, boolean hasFocus,
				            int row, int column) {
        // TODO
		return this;
	}

}
