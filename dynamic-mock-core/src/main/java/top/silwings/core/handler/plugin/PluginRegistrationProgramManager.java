package top.silwings.core.handler.plugin;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import top.silwings.core.exceptions.DynamicMockException;
import top.silwings.core.model.MockHandlerPluginInfoDto;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @ClassName PluginRegistrationProgramManager
 * @Description
 * @Author Silwings
 * @Date 2023/8/14 17:25
 * @Since
 **/
@Component
public class PluginRegistrationProgramManager {

    private final Map<String, PluginRegistrationProgram> pluginCodeRegistrationProgramMap;

    @Getter
    private final List<MockPluginInfo> mockPluginInfoList;

    public PluginRegistrationProgramManager(final List<PluginRegistrationProgram> pluginRegistrationProgramList) {
        this.pluginCodeRegistrationProgramMap = this.initPluginCodeRegistrationProgramMap(pluginRegistrationProgramList);
        this.mockPluginInfoList = this.initMockPluginInfoList(pluginRegistrationProgramList);
    }

    private List<MockPluginInfo> initMockPluginInfoList(final List<PluginRegistrationProgram> pluginRegistrationProgramList) {
        return pluginRegistrationProgramList
                .stream()
                .map(PluginRegistrationProgram::getMockPluginInfo)
                .map(MockPluginInfo::copyOf)
                .collect(Collectors.toList());
    }

    private Map<String, PluginRegistrationProgram> initPluginCodeRegistrationProgramMap(final List<PluginRegistrationProgram> pluginRegistrationProgramList) {
        return pluginRegistrationProgramList
                .stream()
                .collect(Collectors.toMap(e -> {
                            final MockPluginInfo mockPluginInfo = e.getMockPluginInfo();
                            if (null == mockPluginInfo) {
                                throw DynamicMockException.from("The plug-in registration program lacks plug-in information, and the plug-in registration program that does not meet the requirements:" + e.getClass().getName());
                            }
                            return mockPluginInfo.getPluginCode();
                        },
                        Function.identity(),
                        (v1, v2) -> {
                            throw DynamicMockException.from("The plug-in code is repeated, Repeated code: " + v2.getMockPluginInfo().getPluginCode());
                        }));
    }

    public PluginRegistrationProgramInfo findPluginRegistrationProgram(final MockHandlerPluginInfoDto mockHandlerPluginInfoDto) {
        return PluginRegistrationProgramInfo.of(mockHandlerPluginInfoDto.getPluginCode(), this.pluginCodeRegistrationProgramMap.get(mockHandlerPluginInfoDto.getPluginCode()));
    }

    @Getter
    @Setter
    public static class PluginRegistrationProgramInfo {
        private String pluginCode;
        private PluginRegistrationProgram pluginRegistrationProgram;

        public static PluginRegistrationProgramInfo of(final String pluginCode, final PluginRegistrationProgram pluginRegistrationProgram) {
            final PluginRegistrationProgramInfo programInfo = new PluginRegistrationProgramInfo();
            programInfo.setPluginCode(pluginCode);
            programInfo.setPluginRegistrationProgram(pluginRegistrationProgram);
            return programInfo;
        }
    }

}