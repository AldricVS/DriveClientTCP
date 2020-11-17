/**
 * 
 */
package gui.components.document_filters;

import java.awt.Toolkit;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * Document filter that permits to write only prices, written like ddd.ff or ddd,ff.
 * Some examples :
 * <ul>
 * 	<li>15.99 is valid</li>
 * 	<li>15.999 is not valid</li>
 * 	<li>1225,99 is not valid</li>
 * 	<li>999,99 is valid</li>
 * </ul>
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
public class PriceDocumentFilter extends DocumentFilter {
	private static final int MAX_NUMBER_DIGITS = 6;
	private static final String REGEX = "^\\d{1,3}[.,]?\\d{0,2}$";

	@Override
	public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
			throws BadLocationException {
		
		//check if the total length of the new text is not too big and allow only digits
		if((fb.getDocument().getLength() + string.length()) <= MAX_NUMBER_DIGITS && string.matches(REGEX))  {
			//replace in string to append only characters that are digits 
			fb.insertString(offset, string, attr);
		}else {
			Toolkit.getDefaultToolkit().beep();
		}
	}

	@Override
	public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
			throws BadLocationException {
		String resultString = fb.getDocument().getText(0, fb.getDocument().getLength()) + text;
		//check if the total length of the new text is not too big and allow only digits
		if((fb.getDocument().getLength() + text.length()) <= MAX_NUMBER_DIGITS && resultString.matches(REGEX)) {
			//replace in string to append only characters that are digits 
			fb.insertString(offset, text, attrs);
		}else {
			Toolkit.getDefaultToolkit().beep();
		}

	}

}
