package top.silwings.admin.service;

import top.silwings.admin.model.ProjectDto;
import top.silwings.core.common.Identity;
import top.silwings.core.model.MockHandlerDto;

import java.util.List;

/**
 * @ClassName MockHandlerRegisterService
 * @Description 处理器注册服务
 * @Author Silwings
 * @Date 2023/1/9 20:56
 * @Since
 **/
public interface MockHandlerRegisterService {

    String FILE_FLAG = "mockfile:";

    void registerAllEnableHandler(boolean terminateInError);

    void registerHandler(List<MockHandlerDto> mockHandlerDtoList, boolean terminateInError);

    void registerHandler(MockHandlerDto mockHandler, ProjectDto project);

    void registerHandler(MockHandlerDto mockHandlerDto);

    void unRegisterAllDisableHandler(boolean terminateInError);

    void unregisterHandler(Identity handlerIdList);

    void unregisterHandler(List<Identity> handlerIdList, boolean terminateInError);

    void refreshRegisteredHandler(boolean terminateInError);

    void refreshRegisteredHandlerByProject(Identity projectId);

    void refreshRegisteredHandlers(List<MockHandlerDto> mockHandlerDtoList, boolean terminateInError);

}