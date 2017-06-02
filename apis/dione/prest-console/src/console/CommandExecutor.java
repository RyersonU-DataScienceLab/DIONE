package console;

import java.nio.file.Paths;

/**
 * Parses and executes commands for PREST from a string array or the command-line interface. This class is used for
 * both interactive and non-interactive mode.
 */
public class CommandExecutor {
    /**
     * Accepts an array of Strings containing a command, followed by arguments for that command. For example,
     * <pre><code>new String[] {"-setRepository", "C:\Users\Your Name\repository"}.</pre></code> This method is used
     * for both interactive and non-interactive mode.
     *
     * @param args A command to PREST, followed by arguments for that command.
     */
    public static void parseAndExecuteCommand(String[] args) {
        PrestCommand prestCommand = new PrestCommand();
        //console run format change

        if (args[0].equalsIgnoreCase("-addProject")) {
            if (args.length < 2) {
                System.out.println("Error: this command requires exactly one argument.");
            } else {
                prestCommand.addNewProjectCmd(Paths.get(args[1]));
            }
        } else if (args[0].equalsIgnoreCase("-setRepository")) {
            if (args.length < 2) {
                System.out.println("Error: this command requires exactly one argument.");
            } else {
                prestCommand.setRepoCmd(args[1]);
            }
        } else if (args[0].equalsIgnoreCase("-parse")) {
            if (args.length < 2) {
                System.out.println("Error: this command requires exactly one argument.");
            } else {
                prestCommand.parseManualCmd(args[1]);
            }
        } else if (args[0].equalsIgnoreCase("-logFilter")) {
            if (args.length < 2) {
                System.out.println("Error: this command requires exactly one argument.");
            } else {
                prestCommand.logFiltering(args[1]);
            }
        } else if (args[0].equalsIgnoreCase("-convertCsvToArff")) {
            if (args.length < 2) {
                System.out.println("Error: this command requires exactly one argument.");
            } else {
                prestCommand.convertProjectCsvToArff(args[1]);
            }
        } else if (args[0].equalsIgnoreCase("-predict")) {
            if (args.length < 3) {
                System.out.println("Error: this command requires exactly two arguments.");
            } else {
                prestCommand.predict(args[1], args[2], "", "", "");
            }
        } else if (args[0].equalsIgnoreCase("-help")) {
            listDetailedCommandLineOptions();
        } else {
            listCommandLineOptions();
        }
    }

    /**
     * Private helper method for printing concise help guide, for whenever the user enters an invalid command or
     * bad syntax.
     */
    private static void listCommandLineOptions() {
        System.out.println("You entered the wrong syntax or an invalid command. Enter -help for a more detailed guide.");
        System.out.println("Note: you can also enter interactive mode by supplying PREST with no arguments.");
        System.out.println("\nList of PREST commands:");
        System.out.println("-help");
        System.out.println("-setRepository    [repoPath]");
        System.out.println("-addProject       [projectDirectory]");
        System.out.println("-parse            [projectName]");
        System.out.println("-convertCsvToArff [projectName]");
        System.out.println("-logFilter        [arffFilePath]");
        System.out.println("-predict          [trainFilePath] [testFilePath]");
    }

    /**
     * Private helper method for printing detailed help guide, for whenever the user requests it using the
     * -help command
     */
    private static void listDetailedCommandLineOptions() {
        System.out.println("===============");
        System.out.println("Quick Reference");
        System.out.println("===============");
        System.out.println("-help");
        System.out.println("-exit");
        System.out.println("-setRepository    [repoPath]");
        System.out.println("-addProject       [projectDirectory]");
        System.out.println("-parse            [projectName]");
        System.out.println("-convertCsvToArff [projectName]");
        System.out.println("-logFilter        [arffFilePath]");
        System.out.println("-predict          [trainFilePath] [testFilePath]\n");

        System.out.println("==================");
        System.out.println("Commands Explained");
        System.out.println("==================");
        System.out.println("Prints out this help usage guide.");
        System.out.println("-help");

        System.out.println("Exit the interactive mode of PREST.");
        System.out.println("-exit");

        System.out.println("The repository is the file directory in which the output CSV files of two other commands (i.e. -parse, -convertCsvToArff) will be located.");
        System.out.println("-setRepository [repoPath]\n");

        System.out.println("A project is a folder containing source files. A folder of the same name will be created in the repository that was specified by the -setRepository command. " +
                "You may supply either a relative or absolute path to the project directory.");
        System.out.println("-addProject [projectDirectory]\n");

        System.out.println("After you use -addProject, you can use -parse with the name of the project to extract software metrics from it.");
        System.out.println("-parse [projectName]\n");

        System.out.println("After you use -parse, you can convert the generated CSV files into ARFF format.");
        System.out.println("-convertCsvToArff [projectName]\n");

        System.out.println("The -logFilter command applies Weka's log filtering algorithm on an ARFF file.");
        System.out.println("-logFilter [arffFilePath]\n");

        System.out.println("The -predict command accepts 2 ARFF files as input and uses the Naive Bayes algorithm to " +
                "predict the defect proneness of each components of the test project.");
        System.out.println("-predict [trainFile] [testFile]\n");
    }
}
