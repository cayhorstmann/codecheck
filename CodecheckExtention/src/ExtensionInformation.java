import java.io.File;

import bluej.extensions.BlueJ;


public class ExtensionInformation {
	private static ExtensionInformation instance = null;
    private BlueJ bluej = null;
    private ConfigInfo extCfg = null;

    /** 
     * Constructor: Singleton Class that Holds Ext. Information
     * 
     * */
    private ExtensionInformation() {

    }
  
    /** 
     * Retrieves Configuration Object that reads from Property File
     * 
     * @return Configuration Object
     * */
    public ConfigInfo getConfig() throws IllegalStateException {
        if (extCfg == null) {
            throw new IllegalStateException(
                "ConfigInfo object not initialized");
        }

        return extCfg;
    }
    
    /** 
     * Retrieves BlueJ API Object
     * 
     * @return BlueJ Object
     * */
    public BlueJ getBlueJ() throws IllegalStateException {
        if (bluej == null) {
           throw new IllegalStateException(
                "BlueJ object not initialized.");
        }

        return bluej;
    }

    /** 
     * Initializes BlueJ Object
     * 
     * */
    public void setValue(BlueJ bluej) throws IllegalStateException {
        if (this.bluej != null) {
            throw new IllegalStateException(
                "BlueJ object already initialized.");
        }

        this.bluej = bluej;
    }

    /** 
     * Initializes Configuration Object
     * 
     * */
    public void setValue(ConfigInfo extCfg) throws IllegalStateException {
        if (this.extCfg != null) {
            throw new IllegalStateException(
                "ConfigInfo object already initialized.");
        }
        
        this.extCfg = extCfg;
    }
    
	/** 
	* Retrives Single Instance of This Class
	* 
	* @return Instance of ExtensionInformation Object
	* */
    public static ExtensionInformation getInstance() {
        if (instance == null) {
             instance = new ExtensionInformation();
        }
        return instance;
    }
}
