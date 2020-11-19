package org.dreamcat.common.io;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SerializableUtil {

    public static <T extends Serializable> byte[] toBytes(T obj)
            throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(128);
                ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(obj);
            oos.flush();
            return baos.toByteArray();
        }
    }

    public static <T extends Serializable> byte[] toBytes(List<T> objs)
            throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(128);
                ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            for (T obj : objs) {
                oos.writeObject(obj);
            }
            oos.flush();
            return baos.toByteArray();
        }
    }

    public static Object fromBytes(byte[] bytes)
            throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(bytes))) {
            return ois.readObject();
        }
    }

    public static List<Object> fromBytes(byte[] bytes, int count)
            throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(bytes))) {
            List<Object> list = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                list.add(ois.readObject());
            }
            return list;
        }
    }

}
