package Server.commands;

import Common.Response;
import Common.core.SpaceMarine;
import Server.collection_utils.CollectionManager;
import Server.db_utils.DBConnector;
import Server.server_utils.ServerConnectionHandler;

import java.io.File;
import java.io.Serializable;
import java.sql.SQLException;

public class AddCommand implements Command, Serializable {
    private final CollectionManager cm;
    File file;
    private String[] marinesParam;
    SpaceMarine sp;

    public AddCommand(CollectionManager cm){
        this.cm = cm;
    }

    @Override
    public Object execute()  {
        cm.addMarine(sp);

        try {
            DBConnector dbConnector = new DBConnector();
            dbConnector.addSpaceMarine(sp, 1);
        } catch (SQLException e) {
            System.out.println("Новый элемент не добавлен");
        }
        return new Response("Новый элемент успешно добавлен");
    }

    @Override
    public String descr() {
        return null;
    }

    @Override
    public void setParams(String[] marinesParam) {
        this.marinesParam = marinesParam;
    }
    @Override
    public void setMarine(SpaceMarine sp) {
        this.sp = sp;
    }
}