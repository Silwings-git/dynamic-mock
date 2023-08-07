package top.silwings.admin.repository;

import top.silwings.admin.repository.po.pack.MockHandlerTaskPoWrap;
import top.silwings.core.common.Identity;
import top.silwings.core.model.TaskInfoDto;

import java.util.List;

/**
 * @ClassName MockHandlerTaskRepository
 * @Description
 * @Author Silwings
 * @Date 2023/8/7 15:18
 * @Since
 **/
public interface MockHandlerTaskRepository {
    List<TaskInfoDto> queryMockHandlerTaskList(Identity handlerId);

    void insertMockHandlerTask(List<MockHandlerTaskPoWrap> mockHandlerTaskPoWrapList);

    boolean removeMockHandlerTask(Identity handlerId);
}