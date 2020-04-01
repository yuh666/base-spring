package org.example.spring.main;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.AbstractSelector;
import java.nio.channels.spi.SelectorProvider;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class NioServer {

    public static void main(String[] args) throws IOException {

        HashMap<String, String> redis = new HashMap<>();

        SelectorProvider provider = SelectorProvider.provider();
        AbstractSelector selector = provider.openSelector();

        ServerSocketChannel serverSocketChannel = provider.openServerSocketChannel();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(9999));
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            int select = selector.select();
            if (select > 0) {
                Set<SelectionKey> keys = selector.selectedKeys();
                for (SelectionKey key : keys) {
                    if (key.isAcceptable()) {
                        ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                        SocketChannel socketChannel = channel.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ);
                        socketChannel.write(ByteBuffer.wrap("redis >".getBytes()));
                    } else if (key.isReadable()) {
                        SocketChannel channel = (SocketChannel) key.channel();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        channel.read(byteBuffer);
                        String cmd = new String(byteBuffer.array(), 0, byteBuffer.position());
                        String[] cmdArray = cmd.split("\\s+");
                        if (cmdArray.length >= 2) {
                            if ("set".equals(cmdArray[0])) {
                                redis.put(cmdArray[1], cmdArray[2]);
                                channel.write(ByteBuffer.wrap("OK\n".getBytes()));
                            } else if ("get".equals(cmdArray[0])) {
                                String s = redis.get(cmdArray[1]);
                                channel.write(ByteBuffer.wrap(
                                        (s == null ? "(NIL)\n" : s + "\n").getBytes()));
                            }
                        }
                        channel.write(ByteBuffer.wrap("redis >".getBytes()));
                    }
                }
                keys.clear();
            }
        }


    }
}
