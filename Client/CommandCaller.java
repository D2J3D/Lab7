package Client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class CommandCaller implements Serializable {
    private final String commandName; //TODO ПРОВЕРИТЬ НА БАГИ
    private final String[] commandParam;
    final static ArrayList<String> commandList = new ArrayList<>(Arrays.asList("exit", "remove_all_by_chapter" ,"help","show", "info", "remove_greater", "add", "add_if_max", "execute_script", "clear", "filter_contains_name", "print_unique_chapter", "remove_lower", "update_id", "remove_by_id"));
    public CommandCaller(String commandName, String[] commandParam){
        this.commandName = commandName;
        this.commandParam = commandParam;
    }
    public String getCommandName() {
        return commandName;
    }
    public String[] getCommandParam(){
        return commandParam;
    }


}
