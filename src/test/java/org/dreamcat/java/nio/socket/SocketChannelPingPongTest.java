package org.dreamcat.java.nio.socket;

import lombok.extern.slf4j.Slf4j;
import org.dreamcat.common.net.SocketUtil;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.dreamcat.common.util.FormatUtil.*;

/**
 * Create by tuke on 2020/4/7
 */
@Slf4j
public class SocketChannelPingPongTest {
    private static final int bufferSize = 4 * 1024;

    @Test
    public void test() throws InterruptedException {
        ExecutorService es = Executors.newCachedThreadPool();
        es.submit(this::pong);
        es.submit(this::pingNoChannel);
        es.submit(this::ping);
        es.awaitTermination(3, TimeUnit.SECONDS);
    }

    private void ping() {
        try (SocketChannel socket = SocketChannel.open()) {
            socket.connect(new InetSocketAddress("localhost", 8192));
            log("client connected server {}", SocketUtil.format(socket.getRemoteAddress()));

            socket.write(ByteBuffer.wrap("PING".getBytes()));
            log("client sent PING to {}", SocketUtil.format(socket.getRemoteAddress()));
            ByteBuffer buffer = ByteBuffer.allocate(1024 * 4);
            int readSize = socket.read(buffer);
            log("client received: ({})", new String(buffer.array(), 0, readSize));
        } catch (IOException e) {
            printf("%s, failed to connect to %s\n", e.getMessage(), "localhost:8192");
        } finally {
            println("client closed");
        }
    }

    private void pingNoChannel() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }
        try (Socket socket = new Socket("localhost", 8192)) {
            log("client2 connected server {}", SocketUtil.format(socket));
            try (InputStream ins = socket.getInputStream();
                 OutputStream outs = socket.getOutputStream()) {
                outs.write("PING".getBytes());
                log("client2 sent PING to {}", SocketUtil.format(socket));
                byte[] buf = new byte[bufferSize];
                int readSize = ins.read(buf);
                log("client2 received: ({})", new String(buf, 0, readSize));
            }
        } catch (IOException e) {
            printf("%s, failed to connect to %s\n", e.getMessage(), "localhost:8192");
        } finally {
            println("client2 closed");
        }
    }

    private void pong() {
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
                // block until
                selector.select();
                Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
                while (iter.hasNext()) {
                    SelectionKey key = iter.next();
                    iter.remove();

                    if (key.isAcceptable()) {
                        SocketChannel socket = server.accept();
                        socket.configureBlocking(false);

                        println("server accepted a connection from ", SocketUtil.format(socket.getRemoteAddress()));
                        //socket.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(bufferSize));
                        socket.register(selector, SelectionKey.OP_READ);
                    } else if (key.isReadable()) {
                        SocketChannel socket = (SocketChannel) key.channel();
                        //ByteBuffer buffer = (ByteBuffer) key.attachment();
                        ByteBuffer buffer = ByteBuffer.allocate(bufferSize);

                        int readSize;
                        try {
                            if ((readSize = socket.read(buffer)) > 0) {
                                println("server received:", new String(buffer.array(), 0, readSize));

                                buffer.clear();
                                buffer.put("PONG".getBytes());
                                // read mode
                                buffer.flip();
                                socket.write(buffer);
                                log("server sent PONG to {}", SocketUtil.format(socket.getRemoteAddress()));
                                //socket.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(bufferSize));
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

