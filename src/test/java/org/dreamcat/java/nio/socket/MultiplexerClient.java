package org.dreamcat.java.nio.socket;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;

/**
 * Create by tuke on 2020/4/26
 */
@Slf4j
@Ignore
public class MultiplexerClient implements Runnable, Closeable {

    private final Selector selector;
    private final SocketChannel client;
    private volatile boolean stop;

    public MultiplexerClient(String host, int port) throws IOException {
        selector = Selector.open();
        client = SocketChannel.open();
        client.configureBlocking(false);

        client.register(selector, SelectionKey.OP_CONNECT);
        client.connect(new InetSocketAddress(host, port));
    }

    @Override
    public void close() throws IOException {
        this.stop();
        if (client != null && client.isOpen()) {
            client.close();
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
        SocketChannel sc = (SocketChannel) key.channel();
        if (key.isConnectable()) {
            if (sc.finishConnect()) {
                sc.register(selector, SelectionKey.OP_READ);
            }
        } else if (key.isReadable()) {

        }
    }
}
