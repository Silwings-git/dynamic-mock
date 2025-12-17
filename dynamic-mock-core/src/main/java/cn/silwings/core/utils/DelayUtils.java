package cn.silwings.core.utils;

import cn.silwings.core.exceptions.DynamicMockException;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName ThreadDelayUtils
 * @Description
 * @Author Silwings
 * @Date 2022/11/12 19:15
 * @Since
 **/
public class DelayUtils {

    private DelayUtils() {
        throw new AssertionError();
    }

    public static void delay(final int delayTime, final TimeUnit timeUnit) {
        if (delayTime > 0) {
            try {
                timeUnit.sleep(delayTime);
            } catch (InterruptedException e) {
                throw new DynamicMockException(e);
            }
        }
    }

}