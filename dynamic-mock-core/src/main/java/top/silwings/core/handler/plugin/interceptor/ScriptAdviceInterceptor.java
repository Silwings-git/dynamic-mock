package top.silwings.core.handler.plugin.interceptor;

import top.silwings.core.exceptions.ScriptExecException;
import top.silwings.core.handler.MockWorkflowControl;
import top.silwings.core.handler.context.FinalRequestContext;
import top.silwings.core.handler.response.MockResponse;

/**
 * @ClassName ScriptAdviceInterceptor
 * @Description
 * @Author Silwings
 * @Date 2023/5/16 23:45
 * @Since
 **/
public class ScriptAdviceInterceptor {

    public static PreMockScriptErrorInterceptor from(final PreMockInterceptor preMockInterceptor) {
        return PreMockScriptErrorInterceptor.from(preMockInterceptor);
    }

    public static PreResponseScriptErrorInterceptor from(final PreResponseInterceptor preMockInterceptor) {
        return PreResponseScriptErrorInterceptor.from(preMockInterceptor);
    }

    public static class PreMockScriptErrorInterceptor implements PreMockInterceptor {

        private final PreMockInterceptor preMockInterceptor;

        private PreMockScriptErrorInterceptor(final PreMockInterceptor preMockInterceptor) {
            this.preMockInterceptor = preMockInterceptor;
        }

        public static PreMockScriptErrorInterceptor from(final PreMockInterceptor preMockInterceptor) {
            return new PreMockScriptErrorInterceptor(preMockInterceptor);
        }

        @Override
        public void execute(final FinalRequestContext requestContext, final MockWorkflowControl mockWorkflowControl) {
            try {
                this.preMockInterceptor.execute(requestContext, mockWorkflowControl);
            } catch (Exception e) {
                throw new ScriptExecException(e);
            }
        }
    }

    public static class PreResponseScriptErrorInterceptor implements PreResponseInterceptor {

        private final PreResponseInterceptor preResponseInterceptor;

        private PreResponseScriptErrorInterceptor(final PreResponseInterceptor preResponseInterceptor) {
            this.preResponseInterceptor = preResponseInterceptor;
        }

        public static PreResponseScriptErrorInterceptor from(final PreResponseInterceptor preMockInterceptor) {
            return new PreResponseScriptErrorInterceptor(preMockInterceptor);
        }

        @Override
        public void execute(final MockResponse response, final FinalRequestContext requestContext, final MockWorkflowControl mockWorkflowControl) {
            try {
                this.preResponseInterceptor.execute(response, requestContext, mockWorkflowControl);
            } catch (Exception e) {
                throw new ScriptExecException(e);
            }
        }
    }

}