package Server.commands;

import Common.Response;
import Common.core.SpaceMarine;
import Server.collection_utils.CollectionManager;

import java.io.Serializable;

public class RemoveGreaterCommand implements Command, Serializable {
    CollectionManager cm;
    private String[] marinesParam;
    private SpaceMarine spaceMarine;

    public RemoveGreaterCommand(CollectionManager cm){
        this.cm = cm;
    }

    @Override
    public  Object execute(){
        SpaceMarine candidate = this.spaceMarine;
        for (SpaceMarine sp : this.cm.getSynchronizedMarines()){
            if(sp.getName().equals(candidate.getName())){
                try{
                    candidate = sp;
                    this.cm.removeGreater(candidate);
                    return new Response("Команда " + this.descr() + " выполена успешно");
                } catch (Exception e){
                    return new Response("Команда " + this.descr() + "не выполнена, такой солдат не найден");
                }
            }
        }
        return new Response("Команда " + this.descr() + " не выполнена, возникла ошибка");
    }

    @Override
    public String descr() {
        return "remove_greater";
    }

    @Override
    public void setParams(String[] params) {
        this.marinesParam = params;
    }

    @Override
    public void setMarine(SpaceMarine spaceMarine) {
       this.spaceMarine = spaceMarine;
    }
}