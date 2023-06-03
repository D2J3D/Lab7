package Client;

import Common.Asker;
import Common.Request;
import Common.Response;
import Common.Serializer;
import Common.core.SpaceMarine;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class ClientConnectionHandler {

    public static String login;
    public static String password;
    private static boolean isAuthorized = false;

    public static Request authorizeClient() throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Вы зарегистрированы? [1/0]");
        Request request;
        int needsRegistration = sc.nextInt();
        if (needsRegistration == 1){
            System.out.print("Введите логин: ");
            login = sc.next();
            System.out.print("Введите пароль: ");
            password = sc.next();
            String[] paramsToSend = new String[2];
            paramsToSend[0] = password;
            paramsToSend[1] = "true";
            request = new Request(login, paramsToSend, false, login, password);
        }else{
            System.out.println("Пройдите процедуру регистрации");
            System.out.print("Придумайте логин: ");
            //String[] userInput = sc.next().split("_");
            login = sc.next();
            System.out.print("Придумайте пароль: ");
            password = sc.next();
            String[] paramsToSend = new String[2];
            paramsToSend[0] = password;
            paramsToSend[1] = "false";
            Request request2 =  new Request(login, paramsToSend, false, login, password);
            return request2;
        }
        return request;
    }

    static void startSelectorLoop(SocketChannel channel, Scanner sc, Selector selector) throws IOException, ClassNotFoundException, InterruptedException {
        do {
            selector.select();
        } while (startIteratorLoop(channel, sc, selector));
    }

    private static boolean startIteratorLoop(SocketChannel channel, Scanner sc, Selector selector) throws IOException, ClassNotFoundException, InterruptedException {
        Set<SelectionKey> readyKeys = selector.selectedKeys();
        Iterator<SelectionKey> iterator = readyKeys.iterator();
        while (iterator.hasNext()) {
            SelectionKey key = iterator.next();
            iterator.remove();
            if (key.isReadable()) {
                SocketChannel clientChannel = (SocketChannel) key.channel();
                Response response = receive(clientChannel);
                System.out.println("Ответ сервера:");
                System.out.println("----------------------------------------");
                System.out.println(response.getMessage());
                if (response.getMessage().equalsIgnoreCase("show")){
                    System.out.println(response.getSpaceMarines());
                }
                System.out.println("----------------------------------------");
                clientChannel.register(selector, SelectionKey.OP_WRITE);
            } else if (key.isWritable()) {
                try {
                    if (!isAuthorized){
                        Request request = authorizeClient();
                        SocketChannel client = (SocketChannel) key.channel();
                        send(client, request);
                        ClientConnectionHandler.setLogin(login);
                        ClientConnectionHandler.setPassword(password);
                        client.register(selector, SelectionKey.OP_READ);
                        isAuthorized = true;
                        continue;
                    }
                    CommandCaller clientCommand = CommandListener.readCommand(sc);
                    Request request;
                    if (clientCommand == null){
                        request = new Request(null, true, login, password);
                    } else{
                        if (clientCommand.getCommandName().contains("add") || clientCommand.getCommandName().equals("remove_greater") || clientCommand.getCommandName().equals("remove_lower")){
                            Asker asker = new Asker(sc);
                            SpaceMarine requestSpaceMarine = asker.ask();
                            request = new Request(clientCommand.getCommandName(), requestSpaceMarine, true, login, password);
                        }
                        else{
                            request = new Request(clientCommand.getCommandName(), clientCommand.getCommandParam(), true, login, password);
                        }

                    }
                    SocketChannel client = (SocketChannel) key.channel();
                    if (clientCommand != null && clientCommand.getCommandName().equalsIgnoreCase("exit")) {
                        try {
                            send(client, request);
                            System.out.println("До свидания!");
                            System.exit(0);
                        } catch (Exception e) {
                            System.out.println("Сервер не доступен. Команда не будет зарегистрирована на сервере.");
                            System.out.println("До свидания!");
                            System.exit(0);
                        }
                    } else {
                        send(client, request);
                        client.register(selector, SelectionKey.OP_READ);
                    }

                } catch (NullPointerException | IllegalArgumentException  e) {
                    System.out.println(e.getMessage());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                /*
                else{
                    isAuthorized = true;
                    Request requestToAuthorize = authorizeClient();
                    SocketChannel client = (SocketChannel) key.channel();
                    send(client, requestToAuthorize);
                    client.register(selector, SelectionKey.OP_READ);
                }
                 */
            }
        }
        return true;
    }

    public static void setLogin(String login) {
        ClientConnectionHandler.login = login;
    }

    public static void setPassword(String password) {
        ClientConnectionHandler.password = password;
    }

    public static void setIsAuthorized(boolean isAuthorized) {
        ClientConnectionHandler.isAuthorized = isAuthorized;
    }

    public static String getPassword() {
        return password;
    }

    public static String getLogin() {
        return login;
    }

    public static void send(SocketChannel clientChannel, Request request) throws IOException {
        ByteBuffer buffer = Serializer.serialiseRequest(request);
        clientChannel.write(buffer);
    }
    public static Response receive(SocketChannel clientChannel) throws IOException, ClassNotFoundException {
        ByteBuffer readBuffer = ByteBuffer.allocate(clientChannel.socket().getReceiveBufferSize());
        clientChannel.read(readBuffer);
        return Serializer.deserializeResponse(readBuffer.array());
    }

}

