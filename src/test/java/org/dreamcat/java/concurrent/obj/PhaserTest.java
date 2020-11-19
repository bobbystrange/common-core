package org.dreamcat.java.concurrent.obj;

import java.util.concurrent.Phaser;
import org.junit.Test;

/**
 * Create by tuke on 2020/4/24
 */
public class PhaserTest {

    @Test
    public void test() throws InterruptedException {
        Phaser phaser = new Phaser(1);
        for (int i = 1; i <= 3; i++) {
            int finalI = i;
            phaser.register();
            new Thread(() -> {
                //System.out.printf("%d registers\n", finalI);

                System.out.printf("%d beginning phase one\n", finalI);
                phaser.arriveAndAwaitAdvance();
                System.out.printf("%d beginning phase two\n", finalI);
                phaser.arriveAndAwaitAdvance();
                System.out.printf("%d beginning phase three\n", finalI);
                phaser.arriveAndDeregister();
                //System.out.printf("%d de-registers\n", finalI);
            }).start();
        }

        int currentPhaser;
        currentPhaser = phaser.getPhase();
        phaser.arriveAndAwaitAdvance();
        System.out.printf("phaser %s complete\n", currentPhaser);

        currentPhaser = phaser.getPhase();
        phaser.arriveAndAwaitAdvance();
        System.out.printf("phaser %s complete\n", currentPhaser);

        currentPhaser = phaser.getPhase();
        phaser.arriveAndAwaitAdvance();
        System.out.printf("phaser %s complete\n", currentPhaser);

        phaser.arriveAndDeregister();
        if (phaser.isTerminated()) {
            System.out.println("phaser is terminated");
        }

        Thread.sleep(1000);
    }
}
