package cn.silwings.admin.repository;

import cn.silwings.admin.repository.po.pack.MockHandlerResponsePoWrap;
import cn.silwings.core.common.EnableStatus;
import cn.silwings.core.common.Identity;
import cn.silwings.core.model.MockResponseInfoDto;

import java.util.List;

/**
 * @ClassName MockHandlerResponseRepository
 * @Description
 * @Author Silwings
 * @Date 2023/8/8 13:42
 * @Since
 **/
public interface MockHandlerResponseRepository {
    List<MockResponseInfoDto> queryMockHandlerResponseList(Identity handlerId);

    boolean deleteMockHandlerResponse(Identity handlerId);

    void insertMockHandlerResponse(List<MockHandlerResponsePoWrap> mockHandlerResponsePoWrapList);

    void updateByHandlerAndResponseId(Identity handlerId, MockResponseInfoDto responseInfoDto);

    void updateResponseEnableStatus(Identity handlerId, Identity responseId, EnableStatus enableStatus);
}