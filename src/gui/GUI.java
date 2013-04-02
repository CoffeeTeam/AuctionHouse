package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

import components.MyTableCellEditor;
import components.MyTableCellRenderer;
import components.PopupActionListener;

import user.User;
import util.Image;
import verifier.JTextFieldVerifier;

import constants.ComponentNames;
import constants.ErrorMessages;
import constants.Sizes;
import constants.StatusMessages;
import constants.Symbols;
import constants.UserTypes;

import mediator.IMediatorGUI;
import mediator.Mediator;

public class GUI extends JFrame implements IGUI, ActionListener {

	private static final long serialVersionUID = 1L;

	private JButton logInButton;
	private JButton logOutButton;
	private JRadioButton sellerButton;
	private JRadioButton buyerButton;
	private JRadioButton invisibleButton;

	private JTextField userText;
	private JPasswordField passText;

	private JLabel userLabel;
	private JLabel passLabel;
	private JLabel pictureUsr;

	private IMediatorGUI med;

	private JPanel panelCards;
	private CardLayout cardLayout;

	private User user;

	/**
	 * Associations between page number (in order of appearance) and page name
	 * 
	 * @author tom
	 * 
	 */
	private enum Page {
		Page1("logIn"), Page2("servicesPanel"), Page3("logOut");

		private String name;
		private JPanel panel;

		private Page(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public JPanel getPanel() {
			return panel;
		}

		public void setPanel(JPanel panel) {
			this.panel = panel;
		}
	}

	public GUI() {
		super("Auction House");

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		// Center the window on startup
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((dim.width - Sizes.frameWidth) / 2,
				(dim.height - Sizes.frameHeight) / 2);

		// Initialize panels
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.NORTHWEST;
		gbl.layoutContainer(this);

		// Initialize the layout
		cardLayout = new CardLayout();

		panelCards = new JPanel(cardLayout);
		getContentPane().add(panelCards);

		// Init. the first screen (log in)
		JPanel logInPanel = new JPanel(new GridBagLayout());
		gbl.setConstraints(logInPanel, constraints);
		logInPanel.setLayout(gbl);
		Page.Page1.setPanel(logInPanel);
		panelCards.add(Page.Page1.getPanel(), Page.Page1.getName());

		// Init. the second screen (displaying user services)
		JPanel servicesPanel = new JPanel(new GridBagLayout());
		servicesPanel.setLayout(new BorderLayout());
		Page.Page2.setPanel(servicesPanel);
		panelCards.add(Page.Page2.getPanel(), Page.Page2.getName());

		// Init. last screen (log out)
		JPanel logOutPanel = new JPanel(new GridBagLayout());
		gbl.setConstraints(logOutPanel, constraints);
		logOutPanel.setLayout(gbl);
		Page.Page3.setPanel(logOutPanel);
		panelCards.add(Page.Page3.getPanel(), Page.Page3.getName());

		med = new Mediator();
		user = new User();
		initGuiObjects(constraints);
	}

	private void initGuiObjects(GridBagConstraints constraints) {
		JPanel logInPanel = Page.Page1.getPanel();

		putUsernameInFrame(constraints, logInPanel);
		putPasswordInFrame(constraints, logInPanel);
		putRadioButtons(constraints, logInPanel);
		loginButtonActions(constraints, logInPanel);

		setSize(new Dimension(Sizes.frameWidth, Sizes.frameHeight));
		this.setVisible(true);

		// Show first "page" (the log in screen)
		cardLayout.show(panelCards, Page.Page1.getName());

	}

	/**
	 * Puts the username label and the input field for the username in the panel
	 * 
	 * @param constraints
	 *            used to set the positions of the elements
	 * @param jp
	 *            the panel used to put the elements in
	 */
	private void putUsernameInFrame(GridBagConstraints constraints, JPanel jp) {
		userText = new JTextField(Sizes.maxTextFieldLen);
		userLabel = new JLabel(ComponentNames.username);

		/* set username field in page */
		constraints.gridx = 0;
		constraints.gridy = 1;
		jp.add(userLabel, constraints);

		// set border for some space around it
		userLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

		constraints.gridx = 1;
		constraints.gridy = 1;
		jp.add(userText, constraints);

		// set input verifier
		userText.setInputVerifier(new JTextFieldVerifier());
	}

	/**
	 * Position the password elements in the page
	 * 
	 * @param constraints
	 *            object used to set the position of the elements
	 * @param jp
	 *            the panel used to put the elements in
	 */
	private void putPasswordInFrame(GridBagConstraints constraints, JPanel jp) {
		passText = new JPasswordField(Sizes.maxTextFieldLen);
		passLabel = new JLabel(ComponentNames.passw);

		/* set username field in page */
		constraints.gridx = 0;
		constraints.gridy = 2;
		jp.add(passLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 2;
		jp.add(passText, constraints);

		// set input verifier
		passText.setInputVerifier(new JTextFieldVerifier());
	}

	private void putRadioButtons(GridBagConstraints constraints, JPanel jp) {
		ButtonGroup group = new ButtonGroup();
		JPanel radioPanel = new JPanel(new GridLayout(0, 1));

		sellerButton = new JRadioButton(ComponentNames.sellerRadioButton);
		buyerButton = new JRadioButton(ComponentNames.buyerRadioButton);
		invisibleButton = new JRadioButton();

		pictureUsr = new JLabel(
				Image.createImageIcon(ComponentNames.logInPicPath));

		sellerButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				user.setUserType(UserTypes.seller);

			}
		});

		buyerButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				user.setUserType(UserTypes.buyer);
			}
		});

		pictureUsr.setPreferredSize(new Dimension(177, 122));

		invisibleButton.setVisible(false);
		radioPanel.add(buyerButton);
		radioPanel.add(sellerButton);
		radioPanel.add(invisibleButton);

		// Logically group radio buttons
		group.add(buyerButton);
		group.add(sellerButton);
		group.add(invisibleButton);

		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.fill = GridBagConstraints.VERTICAL;
		jp.add(radioPanel, constraints);

		constraints.anchor = GridBagConstraints.EAST;
		jp.add(pictureUsr, constraints);

	}

	private void loginButtonActions(GridBagConstraints constraints, JPanel jp) {
		final GUI owner = this;
		final GridBagConstraints constr = constraints;

		logInButton = new JButton(ComponentNames.logInButton);

		constraints.gridx = 1;
		constraints.gridy = 4;
		constraints.anchor = GridBagConstraints.CENTER;

		jp.add(logInButton, constraints);

		logInButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				user.setUsername(userText.getText());
				user.setPassword(passText.getPassword().toString());

				// Validate given input
				if (user.getUsername().isEmpty()
						|| !(sellerButton.isSelected() || buyerButton
								.isSelected())) {
					JOptionPane.showMessageDialog(owner,
							ErrorMessages.logInErrMsg);
					return;
				}

				// Add services table
				List<String> serviceList = logInUser();
				if (serviceList.isEmpty()) {
					JOptionPane.showMessageDialog(owner,
							ErrorMessages.logInInvalidOpt);
					return;
				}

				// Show second "page" (the users services screen)
				cardLayout.show(panelCards, Page.Page2.getName());

				// Add the table containing services info
				JPanel servicesPanel = Page.Page2.getPanel();

				// Add user message
				JLabel userLabel = new JLabel(ComponentNames.welcomeUserMsg
						+ user.getUsername());
				userLabel.setForeground(Color.RED);
				userLabel.setBorder(BorderFactory.createEmptyBorder(5, 15, 10,
						5));
				servicesPanel.add(userLabel, BorderLayout.PAGE_START);

				int noServices = serviceList.size();
				Object[][] data = new Object[noServices][2];
				int i = 0;

				for (; i < noServices; i++) {
					data[i][0] = serviceList.get(i);
					data[i][1] = StatusMessages.inactive.toUpperCase();
				}

				JTable table = new JTable(data,
						ComponentNames.servicesColumnNames);

				JScrollPane scrollPane = new JScrollPane(table);
				table.setFillsViewportHeight(true);

				// set the cells to be combo boxes
				TableColumn userColumn0, userColumn1;
				
				DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
				centerRenderer.setHorizontalAlignment(JLabel.CENTER);
				
				userColumn0 = table.getColumnModel().getColumn(0);
				userColumn0.setPreferredWidth(100);
				userColumn0.setCellRenderer(centerRenderer);

				userColumn1 = table.getColumnModel().getColumn(1);
				userColumn1.setPreferredWidth(300);
				userColumn1.setCellEditor(new MyTableCellEditor(owner));
				userColumn1.setCellRenderer(new MyTableCellRenderer(owner));

				servicesPanel.add(scrollPane);

				// add logout button to the panel
				logOutButtonAction(servicesPanel);

				addTableListener(table);
			}

		});
	}

	public void logOutButtonAction(JPanel servicesPanel) {
		logOutButton = new JButton(ComponentNames.logOutButton);

		servicesPanel.add(logOutButton, BorderLayout.PAGE_END);

		logOutButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				// show the Log In page again
				cardLayout.show(panelCards, Page.Page1.getName());

				resetUserData();
				Page.Page2.panel.removeAll();
			}
		});
	}

	/**
	 * Add a pop-up menu on right click
	 * 
	 * @param table
	 *            JTable on which the listener is added
	 */
	public void addTableListener(JTable table) {

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				JTable source = (JTable) e.getSource();
				int row = source.rowAtPoint(e.getPoint());

				/*
				 * show menu only on right click and if the click was made
				 * inside the table area
				 */
				if (e.isPopupTrigger()
						&& (row >= 0 && row < source.getRowCount())) {
					int column = source.columnAtPoint(e.getPoint());

					// initialize context menu
					JPopupMenu contextMenu = new JPopupMenu();
					if (createPopUpMenu(contextMenu, source, row, column))
						contextMenu.show(e.getComponent(), e.getX(), e.getY());
				}
			}

		});

	}

	/**
	 * Create a popup menu on right click on a cell
	 * 
	 * @param contextMenu
	 * @param column
	 * @return boolean indicating if a popup menu should be displayed or not
	 */
	public boolean createPopUpMenu(JPopupMenu contextMenu, JTable table,
			int row, int column) {
		JMenuItem item;
		boolean toDisplayMenu = false;
		ActionListener actionListener = new PopupActionListener(table, row,
				column, this);
		String serviceName;

		switch (column) {
		case 0:
			serviceName = (String) table.getValueAt(row, column);
			// add service menu with different entries for buyer and for seller
			if (user.getUserType().equals(UserTypes.buyer)) {
				// Launch Offer Request option
				item = new JMenuItem(ComponentNames.buyerServiceMenu[0]);
				item.addActionListener(actionListener);
				if (!user.isEmptyService(serviceName))
					item.setEnabled(false);
				contextMenu.add(item);

				// Drop Offer Request option
				item = new JMenuItem(ComponentNames.buyerServiceMenu[1]);
				item.addActionListener(actionListener);
				if (user.isEmptyService(serviceName) ||
						user.getServiceTransfer(serviceName) != null)
					item.setEnabled(false);
				contextMenu.add(item);
				toDisplayMenu = true;
			}

			break;
		case 1:
			serviceName = (String) table.getValueAt(row, column-1);
			// add menu for those who will sell products for buyers
			if (user.getUserType().equals(UserTypes.buyer)) {
				// Accept offer
				item = new JMenuItem(ComponentNames.buyerUserMenu[0]);
				item.addActionListener(actionListener);
				// check that status is ACTIVE and not yet accepted
				if (user.getServiceTransfer(serviceName) != null ||
						user.isEmptyService(serviceName))
					item.setEnabled(false);
				contextMenu.add(item);

				// Refuse offer
				item = new JMenuItem(ComponentNames.buyerUserMenu[1]);
				item.addActionListener(actionListener);
				// check that status is ACTIVE
				if (user.getServiceTransfer(serviceName) != null ||
						user.isEmptyService(serviceName))
					item.setEnabled(false);
				contextMenu.add(item);

			} else {
				// Make offer
				item = new JMenuItem(ComponentNames.sellerServiceMenu[0]);
				item.addActionListener(actionListener);
				contextMenu.add(item);

				// Drop auction
				item = new JMenuItem(ComponentNames.sellerServiceMenu[1]);
				item.addActionListener(actionListener);
				contextMenu.add(item);
			}
			toDisplayMenu = true;
			break;
		}

		return toDisplayMenu;
	}

	public void resetUserData() {
		List<String> emptyList = new Vector<String>();
		this.user.setUsername(Symbols.emptyString);
		this.user.setPassword(Symbols.emptyString);
		this.user.setUserServiceList(emptyList);

		// Reset buyer/seller selection
		invisibleButton.doClick();

		// Reset textfields
		userText.setText(Symbols.emptyString);
		passText.setText(Symbols.emptyString);
	}

	public List<String> logInUser() {
		List<String> serviceList = null;

		// Get user's service list
		if (user.getUserType().equals(UserTypes.buyer)) {
			serviceList = med
					.logInBuyer(user.getUsername(), user.getPassword());
		} else {
			if (user.getUserType().equals(UserTypes.seller)) {
				serviceList = med.logInSeller(user.getUsername(),
						user.getPassword());
			} else {
				System.err.println("User type undefined");
				System.exit(1);
			}
		}
		user.setUserServiceList(serviceList);
		if(user.getUserType().equals(UserTypes.seller)) {
			for(String service : serviceList) {
				this.launchOffer(service);
			}
		}
		return serviceList;
	}

	@Override
	public void launchOffer(String serviceName) {
		List<String> userList = med.getUsers(serviceName);
		this.user.setUserListForService(serviceName, userList);
		Page.Page2.panel.repaint();
	}

	@Override
	public void dropOffer(String serviceName) {
		this.user.emptyUserListForService(serviceName);
		Page.Page2.panel.repaint();
	}
	
	@Override
	public void acceptOffer(String serviceName, String seller) {
		this.user.startTransfer(serviceName, seller);
		Page.Page2.panel.repaint();
	}

	@Override
	public void refuseOffer(String serviceName, String seller) {
		this.user.refuseOffer(serviceName, seller);
		Page.Page2.panel.repaint();
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public void interruptTransfer(String serviceName) {
		user.endTransfer(serviceName);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public IMediatorGUI getMed() {
		return med;
	}

	public void setMed(IMediatorGUI med) {
		this.med = med;
	}

	@Override
	public void updateServices(String serviceName, String userName) {
		// TODO Auto-generated method stub
		user.addUserToService(serviceName, userName);
		Page.Page2.panel.repaint();
	}

	public static void repaintUserTable() {
		Page.Page2.panel.repaint();
	}
	
	public static void main(String args[]) {
		new GUI();
	}

	

}
