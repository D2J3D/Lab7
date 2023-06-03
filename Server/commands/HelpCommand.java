package Server.commands;

import Server.collection_utils.CollectionManager;

import Common.Response;
import Server.collection_utils.CollectionManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class HelpCommand implements Command, Serializable {
    private final CollectionManager cm;
    private Map<String, String> commands = new HashMap<>();
    public HelpCommand(CollectionManager cm){
        this.cm = cm;
    }
    @Override
    public Object execute() {
        commands.put("show", "вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
        commands.put("add {element}",  "добавить новый элемент в коллекцию");
        commands.put("update id {element}", "обновить значение элемента коллекции, id которого равен заданному");
        commands.put("remove_by_id id", "удалить элемент из коллекции по его id");
        commands.put("clear", "очистить коллекцию");
        commands.put("execute_script file_name", "считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде,\n в котором их вводит пользователь в интерактивном режиме.");
        commands.put("exit", "завершить программу (без сохранения в файл)");
        commands.put("add_if_max {element}", "добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции");
        commands.put("remove_greater {element}", "удалить из коллекции все элементы, превышающие заданный");
        commands.put("remove_lower {element}", "удалить из коллекции все элементы, меньшие, чем заданный");
        commands.put("remove_all_by_chapter chapter", "удалить из коллекции все элементы, значение поля chapter которого эквивалентно заданному");
        commands.put("filter_contains_name name" , "вывести элементы, значение поля name которых содержит заданную подстроку");
        commands.put("print_unique_chapter", "вывести уникальные значения поля chapter всех элементов в коллекции");
        for (String key : commands.keySet()){
            System.out.println(key + " : " + commands.get(key));
        }
        ArrayList<String> commandsValues = new ArrayList<>(commands.values());
        ArrayList<String> commandsKeys = new ArrayList<>(commands.values());
        for (int i = 0; i < commandsValues.size(); i++){
            commandsKeys.set(i, commandsKeys.get(i) + " - " + commandsValues.get(i));
        }
        return new Response(commands.keySet().stream().map(x -> x + " - " + commands.get(x)).collect(Collectors.joining("\n")));
    }



    @Override
    public String descr() {
        return "help";
    }
}