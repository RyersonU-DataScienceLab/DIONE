package console;

import common.ApplicationProperties;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;

/**
 * The application entry-point for users and developers. PREST is a tool that accepts commands, each of which is
 * accompanied by one or more additional arguments (such as file paths for input). PREST can be used in either an
 * interactive or non-interactive mode.
 */
public class PrestConsoleApp {
    private static Logger logger = null;

    /**
     * Main method for launching the application
     *
     * @param args The PREST command to invoke, followed by one or more additional arguments to it.
     */
    public static void main(String[] args) {
        logger = Logger.getLogger(PrestConsoleApp.class.getName());

        // the application.properties file is needed by PREST to store some information
        File f = new File("application.properties");
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
                logger.error("Error creating application.properties file");
                System.exit(1);
            }
        }

        ApplicationProperties.initiateManual(ApplicationProperties.getPropertiesFileName());
        System.setProperty("log.home", ApplicationProperties.get("repositorylocation") + File.separator);

        startup(args);
    }

    /**
     * Called by the main() method of this class, attempts to create necessary application files required for
     * the execution of this software.
     *
     * @param args The PREST command to invoke, followed by one or more additional arguments to it. If no arguments
     *             are provided, then PREST will enter interactive mode.
     */
    public static void startup(String[] args) {
        String repository = ApplicationProperties.get("repositorylocation");
        if (repository == null) {
            logger.error("check your application.properties file, no repository location selected");
        }

        if (args == null || args.length == 0) {
            logger.info("No arguments provided, entering interactive mode.");
            InteractiveMode intMode = new InteractiveMode();
            intMode.start();
        } else {
            CommandExecutorWeb.parseAndExecuteCommand(args);
        }
    }
}
