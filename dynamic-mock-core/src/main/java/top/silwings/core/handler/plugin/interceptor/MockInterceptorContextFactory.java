package top.silwings.core.handler.plugin.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
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

    @PostConstruct
    public void init() {
        // 当不存在系统拦截器时,将集合初始化一下
        if (null == this.preMockInterceptorList) {
            this.preMockInterceptorList = new ArrayList<>();
        }
        if (null == this.preResponseInterceptorList) {
            this.preResponseInterceptorList = new ArrayList<>();
        }
    }

    public Builder builder() {
        return Builder.builder()
                .preMockInterceptorList(new ArrayList<>(this.preMockInterceptorList))
                .preResponseInterceptorList(new ArrayList<>(this.preResponseInterceptorList))
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