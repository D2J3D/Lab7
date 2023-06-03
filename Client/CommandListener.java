package Client;

import java.util.Arrays;
import java.util.Scanner;

public class CommandListener {
    public static CommandCaller readCommand(Scanner sc){
        String[] input = sc.nextLine().split(" ");
        String commandName = input[0];
        if (CommandCaller.commandList.contains(commandName)){
            return new CommandCaller(input[0], Arrays.copyOfRange(input, 1, input.length));
        }
        return null;
    }
}
