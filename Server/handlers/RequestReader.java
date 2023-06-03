package Server.handlers;

import Common.Request;
import Common.Serializer;
import Server.server_utils.ServerConnectionHandler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.function.Supplier;

public class RequestReader implements Supplier {
    private final SelectionKey key;
    public RequestReader(SelectionKey key){
        this.key = key;
    }
    @Override
    public Request get() {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer readBuffer = ByteBuffer.allocate(4096);
        try {
            socketChannel.read(readBuffer);
            Request request = Serializer.deserializeRequest(readBuffer.array());
            ServerConnectionHandler.collectionOwnerId = request.userId;

            return request;
        } catch (IOException e) {
            System.out.println("Клиент отключился.");
            try {
                socketChannel.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            return null;
        } catch (ClassNotFoundException e) {
            System.out.println("Попытка десериализовать неправильный объект.");
            return null;
        }
    }
}
