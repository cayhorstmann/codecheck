import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import bluej.extensions.BPackage;
import bluej.extensions.MenuGenerator;

public class MenuBuilder extends MenuGenerator {
	
	public final static String EXTNAME = "CodeCheck Extention";
	
	private GetProblem aGetProblem;
	private SubmitProblem aSubmitProblem;
	
	public MenuBuilder() {
		aGetProblem = new GetProblem();
		aSubmitProblem = new SubmitProblem();
	}
	
	public JMenuItem getToolsMenuItem(BPackage aPackage) {
		JMenu serverMenu = new JMenu(EXTNAME);
	        
        JMenuItem getProblem = new JMenuItem(aGetProblem);
        getProblem.setMnemonic(KeyEvent.VK_G);
        getProblem.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_G, ActionEvent.ALT_MASK));
        serverMenu.add(getProblem);
	        
        JMenuItem submitProblem = new JMenuItem(aSubmitProblem);
        submitProblem.setMnemonic(KeyEvent.VK_U);
        submitProblem.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_U, ActionEvent.ALT_MASK));
        serverMenu.add(submitProblem);
        
	    return serverMenu;
	}
	
	/** 
     * The methods which will be called in the main class when
     * each of the different menus are about to be invoked.
     * 
     * */
    public void notifyPostToolsMenu(BPackage bp, JMenuItem jmi) {
        System.out.println("Post on Tools menu");
    }
}
