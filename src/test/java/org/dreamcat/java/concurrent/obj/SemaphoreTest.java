package org.dreamcat.java.concurrent.obj;

import java.util.concurrent.Semaphore;
import org.junit.Test;

/**
 * Create by tuke on 2020/4/24
 */
public class SemaphoreTest {

    @Test
    public void test() throws InterruptedException {
        final Semaphore sem = new Semaphore(1);
        new Thread(() -> {
            try {
                System.out.println("inc is waiting for a permit");
                sem.acquire();
                System.out.println("inc acquires a permit");
                for (int i = 0; i < 10; i++) {
                    System.out.println("count " + Shared.count++);
                    // allow context switch
                    Thread.sleep(10);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("inc releases a permit");
            sem.release();
        }).start();

        new Thread(() -> {
            try {
                System.out.println("dec is waiting for a permit");
                sem.acquire();
                System.out.println("dec acquires a permit");
                for (int i = 0; i < 5; i++) {
                    System.out.println("count " + Shared.count--);
                    // allow context switch
                    Thread.sleep(10);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("dec releases a permit");
            sem.release();
        }).start();

        Thread.sleep(1000);
    }

    @Test
    public void test2() throws InterruptedException {
        final Semaphore conSem = new Semaphore(0);
        final Semaphore prodSem = new Semaphore(1);

        new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    prodSem.acquire();
                    System.out.println("product " + ++Shared.count);
                    conSem.release();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    conSem.acquire();
                    System.out.println("consumer " + Shared.count);
                    prodSem.release();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

    }

    static class Shared {

        static int count = 0;
    }


}
