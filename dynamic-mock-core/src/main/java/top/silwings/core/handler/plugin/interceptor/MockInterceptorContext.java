package top.silwings.core.handler.plugin.interceptor;

import lombok.Getter;

import java.util.Collections;
import java.util.List;

/**
 * @ClassName MockInterceptorContext
 * @Description 拦截器上下文
 * @Author Silwings
 * @Date 2023/5/16 22:22
 * @Since
 **/
@Getter
public class MockInterceptorContext {

    private final List<PreMockInterceptor> preMockInterceptorList;

    private final List<PreResponseInterceptor> preResponseInterceptorList;

    private MockInterceptorContext(final List<PreMockInterceptor> preMockInterceptorList, final List<PreResponseInterceptor> preResponseInterceptorList) {
        this.preMockInterceptorList = preMockInterceptorList;
        this.preResponseInterceptorList = preResponseInterceptorList;
    }

    public static MockInterceptorContext of(final List<PreMockInterceptor> preMockInterceptorList, final List<PreResponseInterceptor> preResponseInterceptorList) {
        return new MockInterceptorContext(preMockInterceptorList, preResponseInterceptorList);
    }

    public static MockInterceptorContext empty() {
        return MockInterceptorContext.of(Collections.emptyList(), Collections.emptyList());
    }
}