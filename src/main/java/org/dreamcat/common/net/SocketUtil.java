package org.dreamcat.common.net;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Create by tuke on 2018-09-13
 */
public class SocketUtil {

    // scheme
    private static final String CLIENT_AGREEMENT = "TLS";
    // cert type
    private static final String CLIENT_TRUST_KEYSTORE_P12 = "PKCS12";
    private static final String CLIENT_TRUST_KEYSTORE_BKS = "BKS";
    private static HostnameVerifier hostnameVerifier = null;
    private static X509TrustManager x509TrustManager = null;

    public static SSLSocketFactory sslSocketFactoryForBKS(
            String certPath, String certPassword)
            throws IOException, NoSuchAlgorithmException,
            KeyStoreException, CertificateException,
            UnrecoverableKeyException, KeyManagementException {
        return sslSocketFactory(certPath, certPassword, CLIENT_TRUST_KEYSTORE_BKS);
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static SSLSocketFactory sslSocketFactoryForP12(
            String certPath, String certPassword)
            throws IOException, NoSuchAlgorithmException,
            KeyStoreException, CertificateException,
            UnrecoverableKeyException, KeyManagementException {
        return sslSocketFactory(certPath, certPassword, CLIENT_TRUST_KEYSTORE_P12);
    }

    public static SSLSocketFactory sslSocketFactory(
            String certPath, String certPassword, String keyStoreType)
            throws IOException, NoSuchAlgorithmException,
            KeyStoreException, CertificateException,
            UnrecoverableKeyException, KeyManagementException {

        SSLContext sslContext;
        try (InputStream is = new FileInputStream(certPath)) {
            sslContext = SSLContext.getInstance(CLIENT_AGREEMENT);
            KeyStore tks = KeyStore.getInstance(keyStoreType);

            tks.load(is, certPassword.toCharArray());

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(tks, certPassword.toCharArray());

            sslContext.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());

            return sslContext.getSocketFactory();
        }
    }

    public static HostnameVerifier hostnameVerifier() {
        if (hostnameVerifier == null) {
            hostnameVerifier = (s, sslSession) -> true;
        }
        return hostnameVerifier;
    }

    public static X509TrustManager x509TrustManager() {
        if (x509TrustManager == null) {
            x509TrustManager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[]{};
                }

            };
        }
        return x509TrustManager;
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static Socket newSocket(
            final String host, final int port,
            int soTimeout) throws IOException {
        Socket socket = new Socket(host, port);
        socket.setTcpNoDelay(true);
        socket.setSoTimeout(soTimeout);
        return socket;
    }

    public static Socket newSocket(
            final String host, final int port, final boolean ssl,
            SSLSocketFactory sslSocketFactory, SSLParameters sslParameters,
            HostnameVerifier hostnameVerifier) throws IOException {

        Socket socket = new Socket();
        //socket.setReuseAddress(true);
        //socket.setKeepAlive(true);
        socket.setTcpNoDelay(true);
        //socket.setSoLinger(true, 0);
        //socket.connect(new InetSocketAddress(host, port), connectionTimeout);
        //socket.setSoTimeout(soTimeout);

        if (null == sslSocketFactory) {
            sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        }
        socket = sslSocketFactory.createSocket(socket, host, port, true);
        if (null != sslParameters) {
            ((SSLSocket) socket).setSSLParameters(sslParameters);
        }
        if ((null != hostnameVerifier) &&
                (!hostnameVerifier.verify(host, ((SSLSocket) socket).getSession()))) {
            String message = String.format(
                    "The connection to '%s:%d' failed ssl/tls hostname verification.", host, port);
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
