import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import bluej.extensions.BPackage;
import bluej.extensions.BlueJ;
import bluej.extensions.PackageNotFoundException;
import bluej.extensions.ProjectNotOpenException;


public class SubmitProblem extends AbstractAction {
	
	protected BlueJ bluej;               
    protected ConfigInfo extCfg;
    private ExtensionInformation extNfo;
    
	public SubmitProblem() {
		extNfo = ExtensionInformation.getInstance();
	       
        try { 
            bluej = extNfo.getBlueJ();
            extCfg = extNfo.getConfig();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        
		putValue(AbstractAction.NAME, "Submit Problem");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		submitProblem();
	}

	protected void submitProblem() {
		if (bluej.getOpenProjects().length == 0) {
			NotificationWindow.display("Please open the project you want to submit.", 
					"Project Not Found!");
			return ;
		}
		
		try { 
            BPackage currentPackage = bluej.getCurrentPackage();
            if (currentPackage != null) {
            	PostProblemFrame frame = new PostProblemFrame(currentPackage.getDir());
            	frame.setVisible(true);
            	/*
            	Thread actionThread = new Thread(new PostProblemThread(currentPackage.getDir()));
                actionThread.start();
                */
            } else {
                NotificationWindow.display("No Current Package!", "Error");
            }
        } catch (ProjectNotOpenException e) {
        	NotificationWindow.display("Project Not Open!", "Error");
            e.printStackTrace();
        } catch (PackageNotFoundException e) {
        	NotificationWindow.display("Package Not Found!", "Error");
            e.printStackTrace();
        }
		 
		
	}
}
