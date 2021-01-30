package org.dreamcat.java.text;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Create by tuke on 2019-03-19
 */
@Ignore
public class DateFormatTest {

    @Test
    public void format() throws InterruptedException {
        SimpleDateFormat sdf = new SimpleDateFormat(
                "E, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);

        ExecutorService executor = Executors.newFixedThreadPool(128);
        int i = 0;
        int mod;
        while (i++ < 2 << 16) {
            final int seq = i;

            Date date = new Date((long) (System.currentTimeMillis() * seq / 1024.0));
            String dateString = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US)
                    .format(date);

            executor.execute(() -> {
                String concurrentDateString = sdf.format(date);
                System.out.printf("[%d] <%s> <%s>\n",
                        seq, sdf.format(new Date()),
                        concurrentDateString);
                if (!concurrentDateString.equals(dateString)) {
                    throw new RuntimeException();
                }
            });
        }

        executor.awaitTermination(10, TimeUnit.SECONDS);
    }


    @Test
    public void local() throws InterruptedException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("E, dd MMM yyyy HH:mm:ss 'GMT'");

        ExecutorService executor = Executors.newFixedThreadPool(128);
        int i = 1;
        int mod;
        while (i++ <= 2 << 16) {
            final int seq = i;

            LocalDateTime dateTime = LocalDateTime.now().minusDays(i);
            String dateString = dateTime.format(
                    DateTimeFormatter.ofPattern("E, dd MMM yyyy HH:mm:ss 'GMT'"));

            executor.execute(() -> {
                String concurrentDateString = dateTime.format(dtf);
                System.out.printf("[%d] <%s> <%s>\n",
                        seq, LocalDateTime.now().format(dtf),
                        concurrentDateString);
                if (!concurrentDateString.equals(dateString)) {
                    throw new RuntimeException();
                }
            });
        }

        executor.awaitTermination(10, TimeUnit.SECONDS);
    }

}
