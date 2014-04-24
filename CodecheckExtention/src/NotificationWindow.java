import javax.swing.JOptionPane;


public class NotificationWindow {
	public static void display(String message, String titleMsg) {
		JOptionPane.showMessageDialog(
    			null, message, titleMsg,
                JOptionPane.ERROR_MESSAGE);
	}
}
