package Server;

import Server.server_utils.ServerConnectionHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static void main(String[] args) throws IOException, SQLException {
        ServerSocketChannel server = ServerSocketChannel.open();
        server.socket().bind(new InetSocketAddress(8001));
        server.configureBlocking(false);
        ExecutorService readService = Executors.newFixedThreadPool(100);
        ExecutorService executeService = Executors.newCachedThreadPool();
        ExecutorService sendService = Executors.newCachedThreadPool();
        ServerConnectionHandler serverConnectionHandler = new ServerConnectionHandler(server, readService, executeService, sendService);

        serverConnectionHandler.start();

    }
}
