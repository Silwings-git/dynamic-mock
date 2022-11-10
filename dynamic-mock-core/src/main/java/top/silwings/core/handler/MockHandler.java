package top.silwings.core.handler;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.Map;

/**
 * @ClassName MockHandler
 * @Description Mock模拟
 * @Author Silwings
 * @Date 2022/11/10 21:51
 * @Since
 **/
@Getter
@Builder
public class MockHandler {

    /**
     * 唯一标识符
     */
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 支持的请求方式
     */
    private List<HttpMethod> httpMethods;

    /**
     * 支持的请求地址
     */
    private String requestUri;

    /**
     * 延迟执行时间
     */
    private int delayTime;

    /**
     * 自定义空间
     */
    private Map<String, Object> customizeSpace;


    public boolean support(final RequestInfo requestInfo) {
        return false;
    }

    public MockResponse mock(final Context context) {
        return null;
    }

}