package browser;

import history.History;
import history.HistoryBar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class BrowserFrame extends JFrame implements Observer {

	public static final String LINK_OPEN_STRING = "Open ";
	public static final String LINK_DEFAULT_STRING = " ";
	public static final String FAILURE_URL = "file:Failure.html";

	private JEditorPane pane;
	private HistoryBar toolbar;
	private History<String> history;
	private FrameController controller;

	// label to show links
	private JLabel label;

	/**
	 * The constructor runs the browser. It displays the main frame with the
	 * fetched initialPage
	 * 
	 * @param initialPage
	 *            the first page to show
	 */
	 BrowserFrame(FrameController controller, String initialPage) {

		// set the controller
		this.controller = controller;

		// set up the editor pane
		pane = new JEditorPane();
		pane.setEditable(false);

		JScrollPane scrollPane = new JScrollPane(pane);

		// label to show links
		label = new JLabel(LINK_DEFAULT_STRING);

		pane.addHyperlinkListener(new HyperlinkListener() {
			public void hyperlinkUpdate(HyperlinkEvent evt) {
				// Mouse enters on the link
				if (evt.getEventType() == HyperlinkEvent.EventType.ENTERED)
					label.setText(LINK_OPEN_STRING + evt.getURL().toString());
				// Link activate
				if (evt.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					String selectedURL = evt.getURL().toString();
					history.add(selectedURL);
				}
				// Mouse exits the link
				if (evt.getEventType() == HyperlinkEvent.EventType.EXITED)
					label.setText(LINK_DEFAULT_STRING);
			}
		});

		history = new History<String>();
		history.addObserver(this);
		toolbar = new HistoryBar(history);
		history.add(initialPage);

		// Menus
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu menu = new JMenu(Browser.MENU_FILE);
		menuBar.add(menu);

		createMenuItem(menu, Browser.MENU_ITEM_NEW, new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				BrowserFrame.this.controller.createFrame();
			}
		});

		createMenuItem(menu, Browser.MENU_ITEM_CLOSE, new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				BrowserFrame.this.controller.deleteFrame(BrowserFrame.this);
			}
		});

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				BrowserFrame.this.controller.deleteFrame(BrowserFrame.this);
			}
		});
		
		createMenuSeparator(menu);

		createMenuItem(menu, Browser.MENU_ITEM_QUIT, new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				BrowserFrame.this.controller.quit();
			}
		});

		// Set up the toolbar and scrollbar in the contentpane of the frame
		JPanel contentPane = new JPanel(new BorderLayout());
		contentPane.add(toolbar, BorderLayout.NORTH);
		contentPane.add(scrollPane, BorderLayout.CENTER);
		contentPane.add(label, BorderLayout.SOUTH);
		setContentPane(contentPane);
	}

	private void createMenuItem(JMenu menu, String name, ActionListener action) {
		JMenuItem menuItem = new JMenuItem(name);
		menuItem.addActionListener(action);
		menu.add(menuItem);
	}

	private void createMenuSeparator(JMenu menu) {
		JSeparator separator = new JSeparator();
		separator.setForeground(Color.lightGray);
		menu.add(separator);
	}

	public void update(Observable observable, Object arg) {
		if (! observable.equals(history)) {
			return;
		}
		setPage(history.get());
	}

	protected void setPage(String url) {
		try {
			pane.setPage(url);
		} catch (Exception e) {
			try {
				pane.setPage(FAILURE_URL);
			} catch (Exception e2) {
				System.err.println(e2.getMessage());
			}
		}
	}
}