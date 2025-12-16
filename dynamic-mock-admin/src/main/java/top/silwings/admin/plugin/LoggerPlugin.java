package top.silwings.admin.plugin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.silwings.core.handler.context.MockPluginContext;
import top.silwings.core.handler.plugin.MockHandlerPluginInfo;
import top.silwings.core.handler.plugin.MockPluginInfo;
import top.silwings.core.handler.plugin.PluginInterfaceType;
import top.silwings.core.handler.plugin.PluginMetaData;
import top.silwings.core.handler.plugin.PluginParamFieldType;
import top.silwings.core.handler.plugin.PluginParamRule;
import top.silwings.core.handler.plugin.PluginRegistrationProgram;
import top.silwings.core.handler.plugin.executors.PluginExecutor;
import top.silwings.core.model.MockHandlerDto;
import top.silwings.core.utils.JsonUtils;

import java.util.ArrayList;

@Slf4j
@Component
public class LoggerPlugin implements PluginRegistrationProgram {

    @Override
    public PluginExecutor<?> newPluginExecutor(final MockHandlerPluginInfo mockHandlerPluginInfo, final MockHandlerDto definition) {
        return new PluginExecutor<Void>() {

            @Override
            public PluginInterfaceType getPluginInterfaceType() {
                return PluginInterfaceType.PRE_RESPONSE;
            }

            @Override
            public Void execute(final MockPluginContext mockPluginContext) {
                String prefix = String.valueOf(mockHandlerPluginInfo.getPluginParam().get("prefix"));
                if (null == prefix) {
                    prefix = "LoggerPlugin";
                }
                final String contextJson = JsonUtils.toJSONString(mockPluginContext);
                log.info("[{}]-请求响应信息: {}", prefix, contextJson);
                return null;
            }
        };
    }

    @Override
    public MockPluginInfo getMockPluginInfo() {
        final ArrayList<PluginParamRule> pluginParamRuleList = new ArrayList<>();
        pluginParamRuleList.add(PluginParamRule.builder()
                .fieldName("prefix")
                .fieldType(PluginParamFieldType.STRING)
                .required(false)
                .description("输出的日志的前缀")
                .example("一个演示前缀")
                .build());

        return MockPluginInfo.builder()
                .pluginCode("loggerPlugin")
                .pluginName("LoggerPlugin")
                .description("Mock响应日志插件")
                .version("0.0.1")
                .metadata(PluginMetaData.builder().pluginParamRuleList(pluginParamRuleList).build())
                .build();
    }
}
