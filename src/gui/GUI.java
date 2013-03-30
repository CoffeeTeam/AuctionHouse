package gui;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import util.Image;
import verifier.JTextFieldVerifier;

import constants.ComponentNames;
import constants.ErrorMessages;
import constants.Sizes;
import constants.UserTypes;

import mediator.IMediatorGUI;
import mediator.Mediator;

public class GUI extends JFrame implements IGUI, ActionListener {

	private static final long serialVersionUID = 1L;
	
	private JButton logInButton;
	private JRadioButton sellerButton;
	private JRadioButton buyerButton;
	
	private JTextField userText;
	private JPasswordField passText;
	
	private JLabel userLabel;
	private JLabel passLabel;
	private JLabel pictureUsr;
	
	private String username;
	private String password;
	private String userType;
	
	private IMediatorGUI med;
	
	private JPanel panelCards;
	private CardLayout cardLayout;

	/**
	 * Associations between page number (in order of appearance) and page name
	 * @author tom
	 *
	 */
	private enum Page {
		Page1("logIn"),
		Page2("servicesPanel"),
		Page3("logOut");
		
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
		setLocation((dim.width - Sizes.frameWidth)/2, (dim.height - Sizes.frameHeight)/2);
		
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
		gbl.setConstraints(servicesPanel, constraints);
		servicesPanel.setLayout(gbl);
		Page.Page2.setPanel(servicesPanel);
		panelCards.add(Page.Page2.getPanel(), Page.Page2.getName());
		
		// Init. last screen (log out)
		JPanel logOutPanel = new JPanel(new GridBagLayout());
		gbl.setConstraints(logOutPanel, constraints);
		logOutPanel.setLayout(gbl);
		Page.Page3.setPanel(logOutPanel);
		panelCards.add(Page.Page3.getPanel(), Page.Page3.getName());
		
		med = new Mediator();
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
	
	
	private void loginButtonActions(GridBagConstraints constraints, JPanel jp) {	
		final GUI owner = this;
		
		logInButton = new JButton(ComponentNames.logInButton);
		
		constraints.gridx = 1;
		constraints.gridy = 4;
		constraints.anchor = GridBagConstraints.CENTER;
		
		jp.add(logInButton, constraints);
		
		logInButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				username = userText.getText();
				password = passText.getText();
				
				// Validate given input
				if (username.isEmpty() || 
						!(sellerButton.isSelected() || buyerButton.isSelected())) {
					JOptionPane.showMessageDialog(owner, ErrorMessages.logInErrMsg);
					return;
				}
				
				// Show second "page" (the users services screen)
				cardLayout.show(panelCards, Page.Page2.getName());
				logInUser();
			}
			
		});
	}
	
	private void putRadioButtons(GridBagConstraints constraints,JPanel jp){
		ButtonGroup group = new ButtonGroup();
		JPanel radioPanel = new JPanel(new GridLayout(0, 1));
		
		sellerButton = new JRadioButton(ComponentNames.sellerRadioButton);
		buyerButton = new JRadioButton(ComponentNames.buyerRadioButton);
		pictureUsr = new JLabel(Image.createImageIcon(ComponentNames.logInPicPath));
		
		sellerButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				userType = UserTypes.seller;
					
			}
		});
		
		buyerButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				userType = UserTypes.buyer;
				
			}
		});
		
		pictureUsr.setPreferredSize(new Dimension(177,122));
		
		radioPanel.add(buyerButton);
		radioPanel.add(sellerButton);
		
		// Logically group radio buttons
		group.add(buyerButton);
		group.add(sellerButton);
		
		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.fill = GridBagConstraints.VERTICAL;
		jp.add(radioPanel, constraints);
		
		constraints.anchor = GridBagConstraints.EAST;
		jp.add(pictureUsr, constraints);
		
		
	}
	
    public void logInUser() {
    	List<String> serviceList;
    	
    	if(userType.equals(UserTypes.buyer)){
    		serviceList = med.logInBuyer(username, password);
    	} else {
    		if(userType.equals(UserTypes.seller)) {
    			serviceList = med.logInSeller(username, password);
    		} else {
    			System.err.println("User type undefined");
    			System.exit(1);
    		}
    	}
    }
    
	@Override
	public void updateServices(List<String> offers) {
		// TODO Auto-generated method stub

	}

	@Override
	public void interruptTransfer(String seller, String serviceName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}
	
	public static void main(String args[]) {
		GUI gui = new GUI();
	}

}
