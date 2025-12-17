package cn.silwings.admin.auth;

/**
 * @ClassName WebContext
 * @Description web上下文
 * @Author Silwings
 * @Date 2022/11/22 22:47
 * @Since
 **/
public class WebContextHolder {

    private static final ThreadLocal<WebContext> TL = new ThreadLocal<>();

    private WebContextHolder() {
        throw new AssertionError();
    }

    public static void setContext(final WebContext webContext) {
        TL.remove();
        TL.set(webContext);
    }

    public static void remove() {
        TL.remove();
    }

    public static WebContext getWebContext() {
        return TL.get();
    }

    /**
     * 获取当前语言
     */
    public static String getLanguage() {

        final WebContext webContext = getWebContext();

        if (null == webContext) {
            return WebContext.defaultLanguage();
        }

        return webContext.getLanguage();
    }
}