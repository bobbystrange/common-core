package org.dreamcat.java.nio.socket;

import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Create by tuke on 2020/4/26
 */
@Slf4j
public class MultiplexerServer implements Runnable, Closeable {
    private final Selector selector;
    private final ServerSocketChannel server;
    private volatile boolean stop;

    public MultiplexerServer(int port) throws IOException {
        this.selector = Selector.open();
        this.server = ServerSocketChannel.open();
        server.configureBlocking(false);
        // Note that, requested maximum length of the queue of incoming connections.
        server.socket().bind(new InetSocketAddress(port), 1024);
        server.register(selector, SelectionKey.OP_ACCEPT);
    }

    @Override
    public void close() throws IOException {
        this.stop();
        if (server != null && server.isOpen()) {
            server.close();
        }
        if (selector != null && selector.isOpen()) {
            selector.close();
        }
    }

    public void stop() {
        this.stop = true;
    }

    @Override
    public void run() {
        while (!stop) {
            try {
                selector.select(1_000);
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iter = keys.iterator();
                while (iter.hasNext()) {
                    SelectionKey key = iter.next();
                    iter.remove();

                    if (!key.isValid()) continue;
                    try {
                        acceptKey(key);
                    } catch (Exception ex) {
                        key.cancel();
                        if (key.channel() != null) {
                            key.channel().close();
                        }
                    }
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }

        try {
            selector.close();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void acceptKey(SelectionKey key) throws IOException {
        if (key.isAcceptable()) {
            ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
            SocketChannel sc = ssc.accept();
            sc.configureBlocking(false);
            // add the new connection to selector
            sc.register(selector, SelectionKey.OP_READ);
        } else if (key.isReadable()) {
            SocketChannel sc = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(4096);
            int readSize = sc.read(buffer);
            if (readSize > 0) {
                buffer.flip();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);
                // do some stuff
                doOnReceive(sc, bytes);
            } else if (readSize < 0) {
                key.cancel();
                sc.close();
            }
        }
    }

    public void doOnReceive(SocketChannel sc, byte[] msg) throws IOException {
        // just write back
        sc.write(ByteBuffer.wrap(msg));
    }
}
