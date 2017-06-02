package console;

import java.nio.file.Paths;

/**
 * Parses and executes commands for PREST from a string array or the command-line interface. This class is used for
 * both interactive and non-interactive mode.
 */
public class CommandExecutorWeb extends CommandExecutor{
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
        if(args.length > 0)
        {
          for(int i=0;i < args.length; i++)
          {
            String[] arg = args[i].split(" ");
            if (arg[0].equalsIgnoreCase("-addProject")) {
                if (arg.length < 2) {
                    System.out.println("Error: this command requires exactly one argument.");
                } else {
                    prestCommand.addNewProjectCmd(Paths.get(arg[1]));
                }
            } else if (arg[0].equalsIgnoreCase("-setRepository")) {
                if (arg.length < 2) {
                    System.out.println("Error: this command requires exactly one argument.");
                } else {
                    prestCommand.setRepoCmd(arg[1]);
                }
            } else if (arg[0].equalsIgnoreCase("-parse")) {
                if (arg.length < 2) {
                    System.out.println("Error: this command requires exactly one argument.");
                } else {
                    prestCommand.parseManualCmd(arg[1]);
                }
            } else if (arg[0].equalsIgnoreCase("-logFilter")) {
                if (arg.length < 2) {
                    System.out.println("Error: this command requires exactly one argument.");
                } else {
                    prestCommand.logFiltering(arg[1]);
                }
            } else if (arg[0].equalsIgnoreCase("-convertCsvToArff")) {
                if (arg.length < 2) {
                    System.out.println("Error: this command requires exactly one argument.");
                } else {
                    prestCommand.convertProjectCsvToArff(arg[1]);
                }
            } else if (arg[0].equalsIgnoreCase("-predict")) {
                if (arg.length < 3) {
                    System.out.println("Error: this command requires exactly two arguments.");
                } else {
                    prestCommand.predict(args[1], args[2], "", "", "");
                }
            }
          }
        }
    }
}
