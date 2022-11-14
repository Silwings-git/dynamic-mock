package top.silwings.core.repository;

import top.silwings.core.common.PageData;
import top.silwings.core.common.PageParam;
import top.silwings.core.repository.dto.MockHandlerDto;

/**
 * @ClassName MockHandlerRepository
 * @Description MockHandler定义存储库
 * @Author Silwings
 * @Date 2022/11/9 22:06
 * @Since
 **/
public interface MockHandlerRepository {
    String create(MockHandlerDto mockHandlerDto);

    String update(MockHandlerDto mockHandlerDto);

    MockHandlerDto get(String id);

    PageData<MockHandlerDto> query(String name, String httpMethod, String requestUri, String label, PageParam pageParam);

    void delete(String id);
}