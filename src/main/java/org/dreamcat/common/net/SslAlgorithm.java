package org.dreamcat.common.net;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Create by tuke on 2018-09-13
 */
@Getter
@RequiredArgsConstructor
public enum SslAlgorithm {
    SSL("SSL"),
    TLS("TLS");

    // cert type
    private static final String CLIENT_TRUST_KEYSTORE_P12 = "PKCS12";
    private static final String CLIENT_TRUST_KEYSTORE_BKS = "BKS";

    private final String algorithm;

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public SSLSocketFactory sslSocketFactoryForNoKey(TrustManager[] trustManagers) {
        try {
            final SSLContext sslContext = SSLContext.getInstance(algorithm);
            sslContext.init(null, trustManagers, new java.security.SecureRandom());
            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }

    public SSLSocketFactory sslSocketFactoryForBKS(
            String certPath, String certPassword)
            throws IOException, NoSuchAlgorithmException,
            KeyStoreException, CertificateException,
            UnrecoverableKeyException, KeyManagementException {
        return sslSocketFactory(certPath, certPassword, CLIENT_TRUST_KEYSTORE_BKS);
    }

    public SSLSocketFactory sslSocketFactoryForP12(
            String certPath, String certPassword)
            throws IOException, NoSuchAlgorithmException,
            KeyStoreException, CertificateException,
            UnrecoverableKeyException, KeyManagementException {
        return sslSocketFactory(certPath, certPassword, CLIENT_TRUST_KEYSTORE_P12);
    }

    public SSLSocketFactory sslSocketFactory(
            String certPath, String certPassword, String keyStoreType)
            throws IOException, NoSuchAlgorithmException,
            KeyStoreException, CertificateException,
            UnrecoverableKeyException, KeyManagementException {

        SSLContext sslContext;
        try (InputStream is = new FileInputStream(certPath)) {
            sslContext = SSLContext.getInstance(algorithm);
            KeyStore tks = KeyStore.getInstance(keyStoreType);

            tks.load(is, certPassword.toCharArray());

            KeyManagerFactory keyManagerFactory = KeyManagerFactory
                    .getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(tks, certPassword.toCharArray());

            sslContext.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());

            return sslContext.getSocketFactory();
        }
    }

}
