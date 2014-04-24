import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Connection {

	private final static int URL_DIALOG_HEIGHT = 70;
    private final static int URL_DIALOG_WIDTH = 720;
    
    private JTextField urlField = new JTextField(60);
    private JDialog urlDialog;
    
    private Thread actionThread;
    private File projectDir;
    
    public Connection() {
    	urlField.addKeyListener(new EnterKeyListener());
    }
    
    private ActionListener acceptAndGetProblemListener = new ActionListener() {
        public void actionPerformed(ActionEvent event) {
            if (urlField.getText().length() > 0) {
                urlDialog.setVisible(false);
                urlDialog.dispose();

                // OK Action (user is ready to log in)
                actionThread = new Thread(new RetrieveProblemThread(projectDir, urlField.getText()));
                actionThread.start(); 
            } else {
            	NotificationWindow.display("Please provide the url of problem", "No URL");
            }
        }
    };
    
    /**
     * This EnterKeyListener invokes the accept action
     * when the enter button is pressed. 
     * 
     * */ 
    public class EnterKeyListener extends KeyAdapter {
        public void keyPressed(KeyEvent evt) {
            if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
                acceptAndGetProblemListener.actionPerformed(null);
            }
        }
    }
    
    public void urlDialog(String urlString, File projectDir) {
    	this.projectDir = projectDir;
        // create modal dialog
        urlDialog = new JDialog(
            ExtensionInformation.getInstance().getBlueJ().getCurrentFrame(),
            "Get Problem from URL", true);

        // set properties
        urlDialog.setSize(URL_DIALOG_WIDTH, URL_DIALOG_HEIGHT);
        urlDialog.setResizable(false);

        // create container for widgets
        Container container = urlDialog.getContentPane();
        container.setLayout(new BorderLayout());

        // create widgets for panel
        JPanel urlPanel = new JPanel(new GridLayout(1, 1));
        JPanel urlLine = new JPanel(new BorderLayout());
        JLabel urlLabel = new JLabel("  URL:");

        // add widgets to panel
        urlLine.add(urlLabel, BorderLayout.WEST);
        urlLine.add(urlField, BorderLayout.EAST);
        urlPanel.add(urlLine);

        // add panel with widgets to container and create new panel
        container.add(urlPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());

        // create buttons
        JButton okButton = new JButton("OK");
        okButton.addKeyListener(new EnterKeyListener());
        okButton.addActionListener(acceptAndGetProblemListener);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                // Cancel Action here
                urlDialog.setVisible(false);
                urlDialog.dispose();
                urlField.setText("");
            }
        });
        
        // add buttons to panel
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        // add to bottom of container and set visible
        container.add(buttonPanel, BorderLayout.SOUTH);
        urlDialog.setLocation(100, 100);
        urlDialog.setVisible(true);
    }
}