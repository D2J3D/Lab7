package Server.commands;

import Common.Response;
import Server.collection_utils.CollectionManager;

import java.util.stream.Collectors;

public class ClearCommand implements Command{
    private CollectionManager cm;
    public ClearCommand(CollectionManager cm){
        this.cm = cm;
    }

    @Override
    public  Object execute() {
        cm.clearMarines();
        return new Response("Команда clear выполнена успешно");
    }

    @Override
    public String descr() {
        return "Команда clear";
    }
}
