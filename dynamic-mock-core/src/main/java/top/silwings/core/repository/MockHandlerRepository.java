package top.silwings.core.repository;

import top.silwings.core.common.Identity;
import top.silwings.core.common.PageData;
import top.silwings.core.common.PageParam;
import top.silwings.core.repository.dto.MockHandlerDto;

import java.util.List;

/**
 * @ClassName MockHandlerRepository
 * @Description MockHandler定义存储库
 * @Author Silwings
 * @Date 2022/11/9 22:06
 * @Since
 **/
public interface MockHandlerRepository {
    Identity create(MockHandlerDto mockHandlerDto);

    Identity update(MockHandlerDto mockHandlerDto);

    MockHandlerDto find(Identity handlerId);

    PageData<MockHandlerDto> query(List<Long> handlerIdList, String name, String httpMethod, String requestUri, String label, PageParam pageParam);

    void delete(Identity handlerId);
}