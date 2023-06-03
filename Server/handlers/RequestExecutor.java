package Server.handlers;

import Client.ClientConnectionHandler;
import Common.Request;
import Common.Response;
import Server.collection_utils.CollectionManager;
import Server.collection_utils.ServerInvoker;
import Server.commands.Command;
import Server.db_utils.DBConnector;
import Server.server_utils.ServerConnectionHandler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.function.Function;
import java.util.function.Supplier;

public class RequestExecutor implements Function<Request, Response> {
    private final CollectionManager cm;
    private int clientId;
    private int collectionOwnerId;

    public RequestExecutor(CollectionManager cm){
        this.cm = cm;
    }

    @Override
    public Response apply(Request request) {
        if (!request.isCommandType()) {
            String login = request.getCommandName();
            String password = request.getCommandParams()[0];
            boolean isRegistered = false;
            if (request.getCommandParams()[1].equals("true")) {
                isRegistered = true;
            }
            if (!isRegistered) {
                try {
                    DBConnector dbConnector = new DBConnector();
                    dbConnector.registerUser(login, password);
                    ClientConnectionHandler.setLogin(login);
                    ClientConnectionHandler.setPassword(password);
                    ClientConnectionHandler.setIsAuthorized(true);
                    ServerConnectionHandler.collectionOwnerId = dbConnector.getUserId(login, password);
                 //   if dbConnector.getUserCollections(login, password);
                    this.clientId = dbConnector.getUserId(login, password);
                    Response response = new Response("Новый пользователь зарегистрировался " +  ServerConnectionHandler.collectionOwnerId);
                    return response;
                } catch (SQLException e) {
                    return new Response("Что-то пошло не так");
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }


            } else {
                try {
                    DBConnector dbConnector = new DBConnector();
                    Integer isLoginSuccessful = dbConnector.loginUser(login, password);
                    ClientConnectionHandler.setPassword(password);
                    ClientConnectionHandler.setLogin(login);
                    ClientConnectionHandler.setIsAuthorized(true);
                    if (isLoginSuccessful > 0) {
                        return new Response(isLoginSuccessful.toString());
                    } else {
                        return new Response("Что-то пошло не так");
                    }
                } catch (SQLException e) {
                    return new Response("Что-то пошло не так");
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }

            }
        }
        if (!request.getLogin().equals(ClientConnectionHandler.getLogin())) {
            if (!request.getPassword().equals(ClientConnectionHandler.getPassword())) {
                return new Response("Минус авторизация((");
            }

        }
        String commandName = request.getCommandName();
        if (commandName == null) {
            return new Response("Такой команды не существует\nПроверьте ввод");
        }
        String[] commandParams = request.getCommandParams();
        if (commandParams == null || commandParams.length == 0) {
            commandParams = null;
        }

        Command command = ServerInvoker.formCommand(commandName, commandParams);
        if (commandName.contains("add") || commandName.equals("remove_greater") || commandName.equals("remove_lower")){
            command.setMarine(request.getSpaceMarine());

        }
        Response response = (Response) command.execute();
        return response;
    }

}
