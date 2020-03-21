package org.dreamcat.common.crypto;

import org.dreamcat.common.util.Base64Util;
import org.dreamcat.common.util.ByteUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Create by tuke on 2019-02-17
 */
public enum MessageDigestEnum {
    MD2("MD2"),
    MD5("MD5"),
    SHA_1("SHA-1"),
    SHA_224("SHA-224"),
    SHA_256("SHA-256"),
    SHA_384("SHA-384"),
    SHA_512("SHA-512"),
    SHA3_224("SHA3-224"),
    SHA3_256("SHA3-256"),
    SHA3_384("SHA3-384"),
    SHA3_512("SHA3-512");

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    private final String algorithm;
    private volatile int bufferSize;

    MessageDigestEnum(String algorithm) {
        this.algorithm = algorithm;
        // 4K Buffer Area
        this.bufferSize = 4096;
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    public byte[] digest(byte[] input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        return md.digest(input);
    }

    public byte[] digest(InputStream input) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        final int size = bufferSize;
        final byte[] buffer = new byte[size];
        int readSize = input.read(buffer, 0, size);

        while (readSize > 0) {
            md.update(buffer, 0, readSize);
            readSize = input.read(buffer, 0, size);
        }
        return md.digest();
    }

    public byte[] digest(ByteBuffer input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        md.update(input);
        return md.digest();
    }

    public byte[] digest(InputStream input, OutputStream output) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        final int size = bufferSize;
        final byte[] buffer = new byte[size];
        int readSize = input.read(buffer, 0, size);

        while (readSize > 0) {
            md.update(buffer, 0, readSize);
            output.write(buffer, 0, readSize);
            readSize = input.read(buffer, 0, size);
        }
        return md.digest();
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    public String digestAsHex(byte[] input) throws NoSuchAlgorithmException {
        return ByteUtil.hex(digest(input));
    }

    public String digestAsHex(InputStream input) throws NoSuchAlgorithmException, IOException {
        return ByteUtil.hex(digest(input));
    }

    public String digestAsHex(ByteBuffer input) throws NoSuchAlgorithmException {
        return ByteUtil.hex(digest(input));
    }

    public String digestAsHex(InputStream input, OutputStream output) throws NoSuchAlgorithmException, IOException {
        return ByteUtil.hex(digest(input, output));
    }
    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    public String digestAsBase64(byte[] input) throws NoSuchAlgorithmException {
        return Base64Util.encodeAsString(digest(input));
    }

    public String digestAsBase64(InputStream input) throws NoSuchAlgorithmException, IOException {
        return Base64Util.encodeAsString(digest(input));
    }

    public String digestAsBase64(ByteBuffer input) throws NoSuchAlgorithmException {
        return Base64Util.encodeAsString(digest(input));
    }

    public String digestAsBase64(InputStream input, OutputStream output) throws NoSuchAlgorithmException, IOException {
        return Base64Util.encodeAsString(digest(input, output));
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public MessageDigestEnum withBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
        return this;
    }
}
