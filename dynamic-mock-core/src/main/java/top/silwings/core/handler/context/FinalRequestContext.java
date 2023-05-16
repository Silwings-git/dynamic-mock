package top.silwings.core.handler.context;

import lombok.Getter;

import java.util.Map;

/**
 * @ClassName FinalRequestContext
 * @Description 对RequestContext的不可变封装.
 * @Author Silwings
 * @Date 2023/5/16 21:53
 * @Since
 **/
@Getter
public class FinalRequestContext {

    /**
     * HTTP请求信息
     */
    private final RequestContext.RequestInfo requestInfo;

    /**
     * 本地缓存,可用于运行时函数向其中读写数据
     */
    private final Map<Object, Object> localCache;

    /**
     * 自定义空间
     */
    private final Map<String, Object> customizeSpace;

    private FinalRequestContext(final RequestContext.RequestInfo requestInfo, final Map<Object, Object> localCache, final Map<String, Object> customizeSpace) {
        this.requestInfo = requestInfo;
        this.localCache = localCache;
        this.customizeSpace = customizeSpace;
    }

    public static FinalRequestContext from(final RequestContext requestContext) {
        return new FinalRequestContext(requestContext.getRequestInfo(), requestContext.getLocalCache(), requestContext.getCustomizeSpace());
    }

}