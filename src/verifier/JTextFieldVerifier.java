package verifier;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;

import constants.Symbols;

/**
 * Validates an input in a JTextField 
 * 
 * @author tom
 *
 */
public class JTextFieldVerifier extends InputVerifier {

	/**
	 * Checks whether the given input exists and is not all blanks
	 */
	@Override
	public boolean verify(JComponent input) {
		String text = ((JTextField)input).getText(); 
		
		return  text != null && !text.trim().equals(Symbols.emptyString);
	}

}
