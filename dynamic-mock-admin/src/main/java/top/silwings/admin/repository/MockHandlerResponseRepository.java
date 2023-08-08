package top.silwings.admin.repository;

import top.silwings.admin.repository.po.pack.MockHandlerResponsePoWrap;
import top.silwings.core.common.Identity;
import top.silwings.core.model.MockResponseInfoDto;

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
}