package cn.silwings.core.config;

import cn.silwings.core.handler.MockHandler;

/**
 * @ClassName MockHandlerHolder
 * @Description
 * @Author Silwings
 * @Date 2022/12/8 20:08
 * @Since
 **/
public class MockHandlerHolder {

    private static final ThreadLocal<MockHandler> TL = new ThreadLocal<>();

    private MockHandlerHolder() {
        throw new AssertionError();
    }

    public static MockHandler get() {
        return TL.get();
    }

    public static void set(final MockHandler mockHandler) {
        TL.set(mockHandler);
    }

    public static void remove() {
        TL.remove();
    }

}