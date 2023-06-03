package Server.server_utils;

import Common.Request;
import Common.Response;
import Common.core.SpaceMarines;
import Server.collection_utils.CollectionManager;
import Server.collection_utils.ServerInvoker;
import Server.db_utils.DBConnector;
import Server.handlers.RequestExecutor;
import Server.handlers.RequestReader;
import Server.handlers.ResponseSender;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ServerConnectionHandler {
    private volatile Selector selector;
    private final Set<SelectionKey> workingKeys = Collections.synchronizedSet(new HashSet<>());
    private final ExecutorService readService;
    private final ExecutorService executeService;
    private final ExecutorService sendService;
    private final ServerSocketChannel server;
    public static Integer collectionOwnerId = 1;
    private final DBConnector dbConnector = new DBConnector();
    private CollectionManager cm;


    public ServerConnectionHandler(ServerSocketChannel server, ExecutorService readService, ExecutorService executeService, ExecutorService sendService) throws SQLException {
        this.server = server;
        this.readService = readService;
        this.executeService = executeService;
        this.sendService = sendService;
    }

    public ServerInvoker fillCollection() throws SQLException {
        SpaceMarines spaceMarines = new SpaceMarines(dbConnector.fillUserCollection(collectionOwnerId));
        this.cm = new CollectionManager(spaceMarines);
        return new ServerInvoker(cm);
    }
    public void start() throws FileNotFoundException, SQLException {
        Console console = new Console();
        console.start();
        startServer();
        readService.shutdown();
        executeService.shutdown();
        sendService.shutdown();
    }

    private void accept(ServerSocketChannel channel) throws IOException, SQLException {
        SocketChannel socketChannel = channel.accept();
        System.out.println("Новый клиент: " + socketChannel.getLocalAddress());
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
    }

    public void startServer() throws SQLException {
        fillCollection();
        System.out.println("Сервер готов к работе");
        try{
            selector = Selector.open();
            server.configureBlocking(false);
            server.register(selector, SelectionKey.OP_ACCEPT);
            while (server.isOpen()) {
                if (selector.select(100) != 0) {
                    Set<SelectionKey> readyKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = readyKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        if (key.isValid() && !workingKeys.contains(key)) {
                            if (key.isAcceptable()) {

                                accept(server);
                            } else if (key.isReadable()) {
                                workingKeys.add(key);
                                System.out.println("Клиент " + ((SocketChannel) key.channel()).getLocalAddress() + " прислал сообщение ");
                                Supplier<Request> requestReader = new RequestReader(key);
                                Function<Request, Response> requestExecutor = new RequestExecutor(cm);
                                Consumer<Response> responseSender = new ResponseSender(key, workingKeys);
                                CompletableFuture
                                        .supplyAsync(requestReader, readService)
                                        .thenApplyAsync(requestExecutor, executeService)
                                        .thenAcceptAsync(responseSender, sendService);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}
