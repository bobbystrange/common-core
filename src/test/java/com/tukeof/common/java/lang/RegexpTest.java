package com.tukeof.common.java.lang;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.tukeof.common.util.ConsoleUtil.*;

/**
 * Create by tuke on 2019-05-15
 */
@Slf4j
public class RegexpTest {

    @Test
    public void test(){
        char ch = 0;

        Pattern pattern = Pattern.compile("\\w");
        for (;ch<1<<16-1; ch++){
            Matcher matcher = pattern.matcher(ch +"");
            log.info("{} `{}` match {}\t{}",
                    (int)ch,
                    Character.valueOf(ch).toString(),
                    pattern,
                    matcher.matches());
        }
    }
}
