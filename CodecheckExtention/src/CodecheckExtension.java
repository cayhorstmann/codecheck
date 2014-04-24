import java.net.URL;

import bluej.extensions.BlueJ;
import bluej.extensions.Extension;
import bluej.extensions.event.PackageEvent;
import bluej.extensions.event.PackageListener;

public class CodecheckExtension extends Extension implements PackageListener {

	private static String EXTURL = "http://horstman.com/codecheck/";
	
	@Override
	public void packageClosing(PackageEvent arg0) {
	}

	@Override
	public void packageOpened(PackageEvent arg0) {
	}

	@Override
	public String getName() {
		return "CodeCheck Extension";
	}

	@Override
	public String getVersion() {
		return "2014.02";
	}

	@Override
	public boolean isCompatible() {
		return true;
	}

	@Override
	public void startup(BlueJ bluej) {
		ConfigInfo extCfg     = new ConfigInfo();
		
		ExtensionInformation extNfo = ExtensionInformation.getInstance();
        extNfo.setValue(bluej);
        extNfo.setValue(extCfg);
        
		// build menu
        bluej.setMenuGenerator(new MenuBuilder());
        bluej.addPackageListener(this);
	}
	
	/*
     * Returns a URL where you can find info on this extension.
     */
    public URL getURL ()
    {
        try
        {
            return new URL(EXTURL);
        }
        catch ( Exception e )
        {
            // The link is either dead or otherwise unreachable
            System.out.println ("CodeCheck Extension: getURL: Exception=" + e.getMessage());
            return null;
        }
    }
}
