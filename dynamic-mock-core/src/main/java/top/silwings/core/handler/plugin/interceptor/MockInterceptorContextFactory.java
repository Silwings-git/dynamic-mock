package top.silwings.core.handler.plugin.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.silwings.core.utils.ConvertUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @ClassName InterceptorManager
 * @Description 拦截器管理器
 * @Author Silwings
 * @Date 2023/5/16 22:13
 * @Since
 **/
@Component
public class MockInterceptorContextFactory {

    @Autowired(required = false)
    private List<PreMockInterceptor> preMockInterceptorList;

    @Autowired(required = false)
    private List<PreResponseInterceptor> preResponseInterceptorList;

    public Builder builder() {
        return Builder.builder()
                .preMockInterceptorList(new ArrayList<>(ConvertUtils.getNoNullOrDefault(this.preMockInterceptorList, Collections.emptyList())))
                .preResponseInterceptorList(new ArrayList<>(ConvertUtils.getNoNullOrDefault(this.preResponseInterceptorList, Collections.emptyList())))
                .build();
    }

    @lombok.Builder
    public static class Builder {

        private List<PreMockInterceptor> preMockInterceptorList;

        private List<PreResponseInterceptor> preResponseInterceptorList;

        public Builder register(final PreMockInterceptor preMockInterceptor) {
            this.preMockInterceptorList.add(preMockInterceptor);
            return this;
        }

        public Builder register(final PreResponseInterceptor preResponseInterceptor) {
            this.preResponseInterceptorList.add(preResponseInterceptor);
            return this;
        }

        public MockInterceptorContext build() {
            return MockInterceptorContext.of(this.preMockInterceptorList, this.preResponseInterceptorList);
        }

    }

}