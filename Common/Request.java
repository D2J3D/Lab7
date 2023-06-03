package Common;

import Common.core.SpaceMarine;
import Server.db_utils.DBConnector;

import java.io.Serializable;
import java.sql.SQLException;

public class Request implements Serializable {

    private final String commandName;
    private final String[] commandParams;
    private final boolean commandType;
    private final String login;
    private final String password;
    public final Integer userId;
    private final SpaceMarine spaceMarine;
    private  Integer collectionId;


    public Request(String commandName, SpaceMarine spaceMarine, boolean commandType, String login, String password) throws SQLException {
        this.commandName = commandName;
        this.spaceMarine = spaceMarine;
        this.commandParams = null;
        this.commandType = commandType;
        this.login = login;
        this.password = password;
        this.userId = new DBConnector().getUserId(login, password);
    }
    public Request(String commandName, boolean commandType, String login, String password) throws SQLException {
        this.commandName = commandName;
        this.spaceMarine = null;
        this.commandParams = null;
        this.commandType = commandType;
        this.login = login;
        this.password = password;
        this.userId = new DBConnector().getUserId(login, password);
    }
    public Request(String commandName, String[] commandParams, boolean commandType, String login, String password) throws SQLException {
        this.commandName = commandName;
        this.commandParams = commandParams;
        this.commandType = commandType;
        this.login = login;
        this.password = password;
        this.userId = new DBConnector().getUserId(login, password);
        this.spaceMarine = null;
    }


    public String getCommandName(){
        return this.commandName;
    }
    public String[] getCommandParams(){
        return this.commandParams;
    }
    public boolean isCommandType() {
        return commandType;
    }
    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public SpaceMarine getSpaceMarine() {
        return spaceMarine;
    }


}
