package org.dreamcat.common.net;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.X509TrustManager;

/**
 * Create by tuke on 2018-09-13
 */
public final class SslUtil {

    private SslUtil() {
    }

    public static HostnameVerifier hostnameVerifier() {
        // The certificate's hostname-specific data should match the server hostname.
        return (requestedHost, remoteServerSession) ->
                requestedHost.equalsIgnoreCase(remoteServerSession.getPeerHost());
    }

    public static X509TrustManager unsafeX509TrustManager() {
        // Create a trust manager that does not validate certificate chains
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s)
                    throws CertificateException {
                // nop
            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s)
                    throws CertificateException {
                // nop
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[]{};
            }

        };
    }
}
