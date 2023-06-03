package Server.handlers;

import Common.Response;
import Common.Serializer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.function.Consumer;

public class ResponseSender implements Consumer<Response> {
    /**
     * Ключ сокета для связи с клиентом.
     */
    private final SelectionKey key;

    private final Set<SelectionKey> workingKeys;

    /**
     * Конструктор обработчика отправки ответа клиенту.
     * @param key ключ сокета для связи с клиентом
     */
    public ResponseSender(SelectionKey key, Set<SelectionKey> workingKeys) {
        this.key = key;
        this.workingKeys = workingKeys;
    }

    /**
     * Отправка ответа клиенту
     * @param response ответ сервера
     */
    @Override
    public void accept(Response response) {
        System.out.println("Началась отправка данных клиенту");
        SocketChannel socketChannel = (SocketChannel) key.channel();

        if (response == null) {
            response = new Response("Сервер не смог обработать запрос.");
        }
        try {
            ///
            ByteBuffer buffer = Serializer.serialiseResponse(response);
            socketChannel.write(buffer);
        } catch (IOException e) {
            return;
        }
        System.out.println("Сервер отправил ответ клиенту.");
        workingKeys.remove(key);
    }
}
