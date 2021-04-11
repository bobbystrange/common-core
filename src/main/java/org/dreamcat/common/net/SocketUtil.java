package org.dreamcat.common.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 * Create by tuke on 2018-09-13
 */
public final class SocketUtil {

    private SocketUtil() {
    }

    public static Socket newSocket(
            final String host, final int port,
            int soTimeout) throws IOException {
        Socket socket = new Socket(host, port);
        socket.setTcpNoDelay(true);
        socket.setSoTimeout(soTimeout);
        return socket;
    }

    public static Socket newSocket(
            final String host, final int port,
            SSLSocketFactory sslSocketFactory, SSLParameters sslParameters,
            HostnameVerifier hostnameVerifier) throws IOException {

        Socket socket = new Socket();
        //socket.setReuseAddress(true);
        //socket.setKeepAlive(true);
        socket.setTcpNoDelay(true);
        //socket.setSoLinger(true, 0);
        //socket.connect(new InetSocketAddress(host, port), connectionTimeout);
        //socket.setSoTimeout(soTimeout);

        if (sslSocketFactory == null) {
            sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        }
        socket = sslSocketFactory.createSocket(socket, host, port, true);
        if (null != sslParameters) {
            ((SSLSocket) socket).setSSLParameters(sslParameters);
        }
        if ((hostnameVerifier != null) &&
                (!hostnameVerifier.verify(host, ((SSLSocket) socket).getSession()))) {
            String message = String.format(
                    "The connection to '%s:%d' failed ssl/tls hostname verification.", host, port);
            if (!socket.isClosed()) {
                socket.close();
            }
            throw new RuntimeException(message);
        }
        return socket;
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static String format(Socket socket) {
        return socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
    }

    public static String format(SocketAddress socketAddress) {
        InetSocketAddress address = (InetSocketAddress) socketAddress;
        return address.getHostString() + ":" + address.getPort();
    }

}
