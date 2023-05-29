package top.silwings.core.handler.plugin.executors.js;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import top.silwings.core.handler.context.MockPluginContext;
import top.silwings.core.handler.plugin.PluginInterfaceType;
import top.silwings.core.handler.plugin.executors.PluginExecutor;
import top.silwings.core.handler.plugin.interfaces.Ordered;

import java.io.IOException;

/**
 * @ClassName PreMockJavaScriptScriptExecutor
 * @Description PreMock的js脚本执行器
 * @Author Silwings
 * @Date 2023/5/29 19:52
 * @Since
 **/
public class PreMockJSScriptExecutor implements PluginExecutor<Void> {

    private final Context context;

    private final Value preMockFunction;

    private final Value orderedFunction;

    private PreMockJSScriptExecutor(final String script) {
        this.context = Context.newBuilder("js").build();
        this.context.eval("js", script);
        this.preMockFunction = this.context.getBindings("js")
                .getMember(this.getPluginInterfaceType().getInterfaceClass().getDeclaredMethods()[0].getName());
        this.orderedFunction = this.context.getBindings("js")
                .getMember(Ordered.class.getDeclaredMethods()[0].getName());
    }

    public static PreMockJSScriptExecutor from(final String script) {
        return new PreMockJSScriptExecutor(script);
    }

    @Override
    public PluginInterfaceType getPluginInterfaceType() {
        return PluginInterfaceType.PRE_MOCK;
    }

    @Override
    public Void execute(final MockPluginContext mockPluginContext) {
        final Value pluginScriptContextValue = this.context.asValue(mockPluginContext);
        this.preMockFunction.execute(pluginScriptContextValue);
        return null;
    }

    @Override
    public int getOrder() {
        if (null != this.orderedFunction) {
            final Value orderInt = this.orderedFunction.execute();
            if (null != orderInt) {
                return orderInt.asInt();
            }
        }
        return 0;
    }

    @Override
    public void close() throws IOException {
        this.context.close();
    }

}