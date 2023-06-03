package Server.commands;

import Common.Response;
import Server.collection_utils.CollectionManager;

public class RemoveByIdCommand implements Command{
    private final CollectionManager cm;
    Integer id;

    public RemoveByIdCommand(CollectionManager cm){
        this.cm = cm;
    }
    @Override
    public Object execute() {
        cm.setSynchronizedMarines(cm.removeAllById(id));
        return new Response("Команда " + this.descr() + " выполена успешно");
    }

    @Override
    public String descr() {
        return "remove_by_id";
    }

    @Override
    public void setParams(String[] params) {
        this.id = Integer.parseInt(params[0]);
    }
}
