package Client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.UnresolvedAddressException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Client {
    private static int PORT = 8001;

    private static String HOST = "localhost";

    private static final int maxPort = 65535;

    private static final Scanner SCANNER = new Scanner(System.in);

    private static SocketChannel clientChannel;

    private static boolean reconnectionMode = false;


    private static int attempts = 0;

    public static void main(String[] args) {

        try{
            clientChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));
            System.out.println("Клиент подключен.");
            clientChannel.configureBlocking(false);
            Selector selector = Selector.open();
            clientChannel.register(selector, SelectionKey.OP_WRITE);
            attempts = 0;
            ClientConnectionHandler.startSelectorLoop(clientChannel, SCANNER, selector);
        } catch (ClassNotFoundException e) {
            System.out.println("Попытка сериализовать несериализуемый объект.");
        } catch (InterruptedException e) {
            System.out.println("Соединение было прервано во время бездействия. Перезапустите клиента.");
        } catch (UnresolvedAddressException e) {
            System.out.println("Сервер с этим хостом не найден. Попробуйте снова.");
            main(args);
        } catch (IOException e) {
            System.out.println("Сервер недоступен. Переподключение, попытка #" + (attempts + 1));
            reconnectionMode = true;
            if (attempts == 4) {
                System.out.println("Переподключение не удалось. Попробуйте подключиться позднее.");
                System.exit(1);
            }
            attempts++;
            main(args);
        } catch (NoSuchElementException e) {
            System.out.println("Принудительное завершение работы.");
            System.exit(1);
        }
    }

}
