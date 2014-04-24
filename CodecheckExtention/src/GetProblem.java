import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import bluej.extensions.BPackage;
import bluej.extensions.BlueJ;
import bluej.extensions.PackageNotFoundException;
import bluej.extensions.ProjectNotOpenException;


public class GetProblem extends AbstractAction {

	protected BlueJ bluej;               
    protected ConfigInfo extCfg;
    private ExtensionInformation extNfo;
    
	private String urlProblem = "";
	
	public GetProblem() {
		extNfo = ExtensionInformation.getInstance();
	       
        try { 
            bluej = extNfo.getBlueJ();
            extCfg = extNfo.getConfig();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        
		putValue(AbstractAction.NAME, "Get Problem");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		getProblem();
	}

	protected void getProblem() {
		if (bluej.getOpenProjects().length == 0) {
			NotificationWindow.display("Please create New Project.", 
					"Project Not Found!");
			return ;
		}
			
		try { 
            BPackage currentPackage = bluej.getCurrentPackage();
            if (currentPackage != null) {
            	Connection connection = new Connection();
    			connection.urlDialog(urlProblem, currentPackage.getDir());
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
