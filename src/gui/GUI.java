package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import mediator.IMediatorGUI;
import mediator.Mediator;

public class GUI extends JFrame implements IGUI, ActionListener {

	private static final long serialVersionUID = 1L;
	private final int maxFieldLen = 20;
	private JButton logInButton;
	private JTextField userText;
	private JPasswordField passText;
	private JLabel userLabel;
	private JLabel passLabel;
	private JLabel pictureUsr;
	private JRadioButton sellerButton;
	private JRadioButton buyerButton;
	private String username;
	private String password;
	private String userType;
	private IMediatorGUI med;

	public GUI() {
		super("Auction House");
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		med = new Mediator();
		initGuiObjects();
	}

	private void initGuiObjects() {
		JPanel jp = new JPanel(new GridBagLayout());
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();
		
		jp.setSize(400, 100);
		getContentPane().add(jp);

		constraints.anchor = GridBagConstraints.NORTHWEST;
		
		gbl.layoutContainer(this);
		gbl.setConstraints(jp, constraints);
		jp.setLayout(gbl);

		putUsernameInFrame(constraints, jp);
		putPasswordInFrame(constraints, jp);
		putRadioButtons(constraints, jp);
		loginButtonActions(constraints, jp);
		
		setSize(new Dimension(500, 400));
		this.setVisible(true);

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
		userText = new JTextField(maxFieldLen);
		userLabel = new JLabel("Username: ");
		/* set username field in page */
		constraints.gridx = 0;
		constraints.gridy = 1;
		jp.add(userLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 1;
		jp.add(userText, constraints);
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
		passText = new JPasswordField(maxFieldLen);
		passLabel = new JLabel("Password: ");

		/* set username field in page */
		constraints.gridx = 0;
		constraints.gridy = 2;
		jp.add(passLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 2;
		jp.add(passText, constraints);
	}
	
	private void loginButtonActions(GridBagConstraints constraints, JPanel jp) {		
		logInButton = new JButton("Log In");
		
		constraints.gridx = 1;
		constraints.gridy = 4;
		constraints.anchor = GridBagConstraints.CENTER;
		
		jp.add(logInButton, constraints);
		
		
		logInButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String userT = userText.getText();
				String passT = passText.getText();
				
				username = userT;
				password = passT;
			}
			
		});
	}
	
	private void putRadioButtons(GridBagConstraints constraints,JPanel jp){
		
		JPanel radioPanel = new JPanel(new GridLayout(0, 1));
		sellerButton = new JRadioButton("Seller");
		buyerButton = new JRadioButton("Buyer");
		pictureUsr = new JLabel(createImageIcon("images/SellerOrBuyer.png"));
		
		sellerButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				userType = "seller";
			}
		});
		
		buyerButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				userType = "buyer";
			}
		});
		
		pictureUsr.setPreferredSize(new Dimension(177,122));
		
		radioPanel.add(buyerButton);
		radioPanel.add(sellerButton);
		
		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.fill = GridBagConstraints.VERTICAL;
		jp.add(radioPanel, constraints);
		
		constraints.anchor = GridBagConstraints.EAST;
		jp.add(pictureUsr, constraints);
		
		
	}
	
	 /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = GUI.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
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
