package common;

import console.PrestConsoleApp;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.Properties;

public class ApplicationProperties {
    private static final String propertiesFileName = "application.properties";
    private static Properties prop;
    private static Logger logger = Logger.getLogger(PrestConsoleApp.class.getName());

    public static String getPropertiesFileName() {
        return propertiesFileName;
    }

    public static synchronized void initiateManual(String propertiesFileFullPath) {
        initiate(propertiesFileFullPath);
    }

    private static synchronized void initiate(String propertiesFileFullPath) {
        prop = new Properties();

        if (propertiesFileFullPath == null) {
            System.out.println("Missing propertiesFile parameter");
            return;
        }

        // logger.info("Setting properties file name to " + propertiesFileFullPath);

        try {
            InputStream is = new java.io.FileInputStream(propertiesFileFullPath);
            prop.load(is);
        } catch (Exception e) {
            e.printStackTrace();

            logger.error("Could not read or find file '" + propertiesFileFullPath + "'");
            logger.error("Property file is needed for the application to operate.");
        }
    }

    public static String get(String key) {
        return prop.getProperty(key);
    }

    public static void set(String key, String value) {
        String oldValue = prop.getProperty(key);
        if ((oldValue == null) && !prop.containsKey(key)) {
            // intentionally left blank
        } else {
          if(key != null && value != null)
          {
            prop.setProperty(key, value);
          }
        }
    }

    public static String get(String key, String defaultValue) {
        return get(key, defaultValue, true);
    }

    public static String get(String key, String defaultValue, boolean expected) {
        String value = prop.getProperty(key);

        if ((value == null) && !prop.containsKey(key)) {
            if (expected) {
                // intentionally left blank
            }
        }

        return (prop.getProperty(key, defaultValue));
    }

    /**
     * Sets the repository location for PREST by writing to a file.
     *
     * @param propFilePath Absolute or relative path to the properties file
     * @param fullPath Absolute or relative path to the repository
     */
    public static void setRepositoryLocation(String propFilePath, String fullPath) {
        // if repository folder doesn't already exist, create it
        File repo = new File(fullPath);
        if (!repo.exists())
            repo.mkdirs();

        // add the repository location to the Properties object
        set("repositorylocation", propFilePath);

        // write the repository location to a file
        Writer output = null;
        try {
            output = new BufferedWriter(new FileWriter(new File(propFilePath != null ? propFilePath : (propertiesFileName))));
            output.write("repositorylocation = " + fullPath);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                output.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
