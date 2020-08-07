package org.dreamcat.java.nio.socket;

import org.dreamcat.common.net.SocketUtil;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.dreamcat.common.util.FormatUtil.log;
import static org.dreamcat.common.util.FormatUtil.println;

/**
 * Create by tuke on 2020/4/7
 */
public class SocketChannelLoopTest {
    private static final int bufferSize = 4 * 1024;

    @Test
    public void test() throws InterruptedException {
        ExecutorService es = Executors.newCachedThreadPool();
        es.submit(this::server);
        es.submit(this::client);
        es.awaitTermination(5000, TimeUnit.SECONDS);
    }

    private void client() {
        try (SocketChannel socket = SocketChannel.open()) {
            socket.configureBlocking(false);
            Selector selector = Selector.open();
            socket.register(selector, SelectionKey.OP_CONNECT);
            socket.connect(new InetSocketAddress("localhost", 8192));

            while (socket.isOpen()) {
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iter = keys.iterator();
                while (iter.hasNext()) {
                    SelectionKey key = iter.next();
                    iter.remove();

                    if (key.isConnectable()) {
                        socket.finishConnect();
                        println("client connected to server");

                        socket.write(ByteBuffer.wrap("1".getBytes()));
                        log("client wrote 1 to server");
                        socket.register(selector, SelectionKey.OP_READ);
                    } else if (key.isReadable()) {
                        ByteBuffer buffer = ByteBuffer.allocate(1024 * 4);
                        int readSize;
                        try {
                            if ((readSize = socket.read(buffer)) > 0) {
                                long num = Long.parseLong(new String(buffer.array(), 0, readSize));
                                println("client received:", num);

                                buffer.clear();
                                buffer.put(String.valueOf(++num).getBytes());
                                buffer.flip();
                                socket.write(buffer);
                                log("client sent {} to {}", num, SocketUtil.format(socket.getRemoteAddress()));
                                socket.register(selector, SelectionKey.OP_READ);
                            }
                        } catch (IOException e) {
                            println("client error:", e.getMessage());
                            key.cancel();
                            socket.close();
                        }
                    }
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        } catch (IOException e) {
            log("{}, failed to connect to {}", e.getMessage(), "localhost:8192");
        }
    }

    private void server() {
        ServerSocketChannel server;
        Selector selector;
        try {
            server = ServerSocketChannel.open();
            server.configureBlocking(false);
            server.bind(new InetSocketAddress("localhost", 8192));
            selector = Selector.open();
            server.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            println(e.getMessage());
            throw new RuntimeException(e);
        }

        println("server started");
        while (true) {
            try {
                selector.select();
                Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
                while (iter.hasNext()) {
                    SelectionKey key = iter.next();
                    iter.remove();

                    if (key.isAcceptable()) {
                        SocketChannel socket = server.accept();
                        socket.configureBlocking(false);

                        println("server accepted a connection from ", SocketUtil.format(socket.getRemoteAddress()));
                        socket.register(selector, SelectionKey.OP_READ);
                    } else if (key.isReadable()) {
                        SocketChannel socket = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(bufferSize);

                        int readSize;
                        try {
                            if ((readSize = socket.read(buffer)) > 0) {
                                long num = Long.parseLong(new String(buffer.array(), 0, readSize));
                                println("server received:", num);

                                buffer.clear();
                                buffer.put(String.valueOf(num).getBytes());
                                // read mode
                                buffer.flip();
                                socket.write(buffer);
                                log("server sent {} to {}", num, SocketUtil.format(socket.getRemoteAddress()));
                                socket.register(selector, SelectionKey.OP_READ);
                            }
                        } catch (IOException e) {
                            println(e.getMessage());
                            key.cancel();
                            socket.close();
                        }


                    }
                }
            } catch (IOException e) {
                println("server error: " + e.getMessage());
                break;
            }
        }
    }

}
