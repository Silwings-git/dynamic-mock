package cn.silwings.admin.repository;

import cn.silwings.admin.repository.po.pack.MockHandlerTaskPoWrap;
import cn.silwings.core.common.EnableStatus;
import cn.silwings.core.common.Identity;
import cn.silwings.core.model.TaskInfoDto;

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

    boolean deleteMockHandlerTask(Identity handlerId);

    void updateTaskEnableStatus(Identity handlerId, Identity taskId, EnableStatus enableStatus);
}