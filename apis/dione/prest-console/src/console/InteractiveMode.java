package console;

import java.util.Scanner;

public class InteractiveMode {

    public void start() {
        System.out.println("Enter -help for detailed help commands, or -exit to quit.");

        Scanner stdin = new Scanner(System.in);
        while (true) {
            System.out.print("PREST> ");
            String command = stdin.nextLine();

            // check if first argument is the -exit command
            String[] commandArgs = command.split(" ");
            if (commandArgs[0].equals("-exit")) 
                System.exit(0);
            } else {
                CommandExecutor.parseAndExecuteCommand(commandArgs);
            }
        }
    }
}
