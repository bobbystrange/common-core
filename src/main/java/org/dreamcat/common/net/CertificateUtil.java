package org.dreamcat.common.net;

import org.dreamcat.common.util.Base64Util;

import javax.crypto.Cipher;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;

public class CertificateUtil {

    private static final String KEY_STORE = "JKS";
    private static final String X509 = "X.509";

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    private static final String SunX509 = "SunX509";
    private static final String SSL = "SSL";

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static SSLSocketFactory getSSLSocketFactory(
            String password, String keyStorePath, String trustKeyStorePath
    ) throws Exception {
        KeyManagerFactory keyManagerFactory = KeyManagerFactory
                .getInstance(SunX509);
        KeyStore keyStore = getKeyStore(keyStorePath, password);
        keyManagerFactory.init(keyStore, password.toCharArray());

        TrustManagerFactory trustManagerFactory = TrustManagerFactory
                .getInstance(SunX509);
        KeyStore trustkeyStore = getKeyStore(trustKeyStorePath, password);
        trustManagerFactory.init(trustkeyStore);

        SSLContext sslContext = SSLContext.getInstance(SSL);
        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory
                .getTrustManagers(), null);

        return sslContext.getSocketFactory();
    }

    public static void configSSLSocketFactory(
            HttpsURLConnection conn, String password, String keyStorePath, String trustKeyStorePath
    ) throws Exception {
        conn.setSSLSocketFactory(getSSLSocketFactory(password, keyStorePath,
                trustKeyStorePath));
    }

    public static String sign(
            byte[] sign, String keyStorePath, String alias, String password
    ) throws Exception {
        X509Certificate x509Certificate = (X509Certificate) getCertificate(
                keyStorePath, alias, password);
        KeyStore ks = getKeyStore(keyStorePath, password);
        PrivateKey privateKey = (PrivateKey) ks.getKey(alias, password
                .toCharArray());

        Signature signature = Signature.getInstance(x509Certificate
                .getSigAlgName());
        signature.initSign(privateKey);
        signature.update(sign);
        return Base64Util.encodeToString(signature.sign());
    }

    public static boolean verify(
            byte[] data, String sign, String certificatePath) throws Exception {
        X509Certificate x509Certificate = (X509Certificate) getCertificate(certificatePath);
        PublicKey publicKey = x509Certificate.getPublicKey();
        Signature signature = Signature.getInstance(x509Certificate
                .getSigAlgName());
        signature.initVerify(publicKey);
        signature.update(data);
        return signature.verify(Base64Util.decode(sign));

    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    public static byte[] encryptByPrivateKey(
            byte[] data, String keyStorePath, String alias, String password
    ) throws Exception {
        PrivateKey privateKey = getPrivateKey(keyStorePath, alias, password);
        return encrypt(data, privateKey);


    }

    public static byte[] decryptByPrivateKey(
            byte[] data, String keyStorePath, String alias, String password
    ) throws Exception {
        PrivateKey privateKey = getPrivateKey(keyStorePath, alias, password);
        return decrypt(data, privateKey);

    }

    public static byte[] encryptByPublicKey(byte[] data, String certificatePath)
            throws Exception {
        PublicKey publicKey = getPublicKey(certificatePath);
        return encrypt(data, publicKey);

    }

    public static byte[] decryptByPublicKey(byte[] data, String certificatePath)
            throws Exception {
        PublicKey publicKey = getPublicKey(certificatePath);
        return decrypt(data, publicKey);
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    // publish cert verify
    public static boolean verifyCertificate(String certificatePath) {
        return verifyCertificate(new Date(), certificatePath);
    }

    // publish cert verify
    public static boolean verifyCertificate(Date date, String certificatePath) {
        boolean status = true;
        try {
            Certificate certificate = getCertificate(certificatePath);
            status = verifyCertificate(date, certificate);
        } catch (Exception e) {
            status = false;
        }
        return status;
    }

    // private cert verify
    public static boolean verifyCertificate(
            Date date, String keyStorePath, String alias, String password) {
        boolean status = true;
        try {
            Certificate certificate = getCertificate(keyStorePath, alias, password);
            status = verifyCertificate(date, certificate);
        } catch (Exception e) {
            status = false;
        }
        return status;
    }

    // private cert verify
    public static boolean verifyCertificate(
            String keyStorePath, String alias, String password) {
        return verifyCertificate(new Date(), keyStorePath, alias, password);
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    private static PrivateKey getPrivateKey(String keyStorePath, String alias,
                                            String password) throws Exception {
        KeyStore ks = getKeyStore(keyStorePath, password);
        return (PrivateKey) ks.getKey(alias, password.toCharArray());
    }

    private static PublicKey getPublicKey(String certificatePath)
            throws Exception {
        Certificate certificate = getCertificate(certificatePath);
        return certificate.getPublicKey();
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    private static Certificate getCertificate(String certificatePath) throws Exception {
        CertificateFactory certificateFactory = CertificateFactory
                .getInstance(X509);
        try (FileInputStream istream = new FileInputStream(certificatePath)) {
            return certificateFactory.generateCertificate(istream);
        }
    }

    private static Certificate getCertificate(
            String keyStorePath, String alias, String password) throws Exception {
        KeyStore ks = getKeyStore(keyStorePath, password);
        return ks.getCertificate(alias);
    }

    private static KeyStore getKeyStore(String keyStorePath, String password)
            throws Exception {
        try (FileInputStream istream = new FileInputStream(keyStorePath)) {
            KeyStore ks = KeyStore.getInstance(KEY_STORE);
            ks.load(istream, password.toCharArray());
            return ks;
        }
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    private static boolean verifyCertificate(Date date, Certificate certificate) {
        boolean status = true;
        try {
            X509Certificate x509Certificate = (X509Certificate) certificate;
            x509Certificate.checkValidity(date);
        } catch (Exception e) {
            status = false;
        }
        return status;
    }

    private static byte[] encrypt(byte[] data, Key someKey) throws Exception {
        return cipher(data, someKey, Cipher.ENCRYPT_MODE);
    }

    private static byte[] decrypt(byte[] data, Key someKey) throws Exception {
        return cipher(data, someKey, Cipher.DECRYPT_MODE);
    }

    private static byte[] cipher(byte[] data, Key someKey, int mode) throws Exception {
        Cipher cipher = Cipher.getInstance(someKey.getAlgorithm());
        cipher.init(mode, someKey);
        cipher.update(data);
        return cipher.doFinal();
    }


}
