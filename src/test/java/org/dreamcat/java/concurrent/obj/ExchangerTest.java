package org.dreamcat.java.concurrent.obj;

import java.util.concurrent.Exchanger;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Create by tuke on 2020/4/24
 */
@Ignore
public class ExchangerTest {

    @Test
    public void test() throws InterruptedException {
        Exchanger<Integer> exchanger = new Exchanger<>();

        new Thread(() -> {
            for (int i = 1; i <= 99; i += 2) {
                try {
                    int n = exchanger.exchange(i);
                    System.out.println("odd receives " + n);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(() -> {
            for (int i = 2; i <= 100; i += 2) {
                try {
                    int n = exchanger.exchange(i);
                    System.out.println("even receives " + n);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        Thread.sleep(1000);
    }
}
