package gui;

import java.awt.Font;

/**
 * Class containing only constants that define resolution of application and size of elements (as font).
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outtlok.fr>
 */
public class GuiConstants {
	
	public static final int RATIO_WIDTH = 16;
	
	public static final int RATIO_HEIGHT = 9;
	
	public static final int HEIGHT = 960;
	
	//width constant must not be changed (set with height and ratio values)
	public static final int WIDTH = HEIGHT * RATIO_WIDTH/RATIO_HEIGHT;
	
	public static final int FONT_SIZE = WIDTH / 80;
	
	public static final Font BASE_FONT = new Font("Arial", Font.PLAIN, FONT_SIZE);
	
	public static final Font ITALIC_FONT = new Font("Arial", Font.ITALIC, FONT_SIZE);
}
