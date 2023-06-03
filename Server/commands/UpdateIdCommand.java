package Server.commands;

import Common.Response;
import Common.core.SpaceMarine;
import Server.collection_utils.CollectionManager;

import java.io.Serializable;

public class UpdateIdCommand implements Command, Serializable {
    private final CollectionManager cm;
    private Integer newId;
    private Integer oldId;
    public UpdateIdCommand(CollectionManager cm){
        this.cm = cm;
    }
    @Override
    public synchronized Object execute() {
        SpaceMarine candidate = new SpaceMarine();
        for (SpaceMarine sp : this.cm.getSynchronizedMarines()){
            if(sp.getId().equals(candidate.getId())){
                System.out.println("Found this guy ID №" + sp.getId());
                try{
                    candidate = sp;
                    if (String.valueOf(newId).length() > 0){
                        candidate.setId(newId);
                        return new Response("ID is changed to " + newId);
                    } else{
                        return new Response("Failed to change id, try again");
                    }

                } catch (Exception e){
                    System.out.println(e + "!");
                }
                break;
            }
        }
        return new Response("При выполнении команды" + this.descr()  + " возникла ошибка");

    }

    @Override
    public String descr() {
        return "update id command";
    }

    @Override
    public void setParams(String[] params) {
        this.newId = Integer.parseInt(params[1]);
        this.oldId = Integer.parseInt(params[0]);
    }
}
