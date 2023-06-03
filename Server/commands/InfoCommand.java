package Server.commands;

import Common.Response;
import Server.collection_utils.CollectionManager;

import java.io.Serializable;

public class InfoCommand implements Command, Serializable {
    private final CollectionManager cm;
    public InfoCommand(CollectionManager cm){
        this.cm = cm;
    }
    @Override
    public Object execute() {
        return new Response(cm.showCollectionInfo());
    }

    @Override
    public String descr() {
        return "info";
    }
}