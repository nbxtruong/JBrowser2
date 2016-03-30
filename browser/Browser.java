package browser;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

public class Browser implements FrameController {
	public final static String MENU_FILE = "File";
	public final static String MENU_ITEM_NEW = "New";
	public final static String MENU_ITEM_CLOSE = "Close";
	public final static String MENU_ITEM_QUIT = "Quit";
	public final static String DIALOG_QUIT_MSG = "Do you really want to quit ?";
	public final static String DIALOG_QUIT_TITLE = "Quit ?";
	public final static String TITLE = "Browser";
	public final static String VERSION = "2";

	private static final List<JFrame> frames = new ArrayList<JFrame>();

	private static String initialPage = "http://www.google.com.vn/";

	public static final String ICON = "images/Astrolabe.png";

	public void quit() {
		int answer = JOptionPane.showConfirmDialog(null, DIALOG_QUIT_MSG,
				DIALOG_QUIT_TITLE, JOptionPane.YES_NO_OPTION);
		if (answer == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}

	public JFrame createFrame() {
		JFrame frame = new BrowserFrame(this, initialPage);
		frame.setTitle(TITLE + " " + VERSION);
		int pos = 30 * (frames.size() % 5);
		frame.setLocation(pos, pos);
		frame.setPreferredSize(new Dimension(960, 720));
		frame.setIconImage(new ImageIcon(ICON).getImage());
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		frames.add(frame);
		return frame;
	}

	public void deleteFrame(JFrame frame) {
		if (frames.size() > 1) {
			frames.remove(frame);
			frame.dispose();
		} else {
			quit();
		}
	}

	public static void main(String[] args) {
		if (args.length > 0)
			initialPage = args[0];
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Browser().createFrame();
			}
		});
	}
}