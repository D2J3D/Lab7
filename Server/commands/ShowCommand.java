package Server.commands;

import Common.Response;
import Server.collection_utils.CollectionManager;

import java.util.stream.Collectors;

public class ShowCommand implements Command{
    private CollectionManager cm;
    public ShowCommand(CollectionManager cm){
        this.cm = cm;
    }

    @Override
    public  Object execute() {
        if (cm.getSynchronizedMarines().size() == 0){
            return new Response("Коллекция пуста");
        }
        return new Response(cm.showMarines().stream().map(Object::toString).collect(Collectors.joining("\n")));
    }

    @Override
    public String descr() {
        return "Команда show";
    }
}
