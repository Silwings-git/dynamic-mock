package top.silwings.core.repository.db.mysql;

import org.springframework.stereotype.Component;
import top.silwings.core.common.PageData;
import top.silwings.core.common.PageParam;
import top.silwings.core.repository.MockHandlerRepository;
import top.silwings.core.repository.dto.MockHandlerDto;

/**
 * @ClassName MockHandlerRepository
 * @Description
 * @Author Silwings
 * @Date 2022/11/15 22:35
 * @Since
 **/
@Component
public class MockHandlerMySqlRepository implements MockHandlerRepository {

    private MockHandlerMapper mockHandlerMapper;

    public MockHandlerMySqlRepository(final MockHandlerMapper mockHandlerMapper) {
        this.mockHandlerMapper = mockHandlerMapper;
    }

    @Override
    public String create(final MockHandlerDto mockHandlerDto) {
        return null;
    }

    @Override
    public String update(final MockHandlerDto mockHandlerDto) {
        return null;
    }

    @Override
    public MockHandlerDto get(final String id) {
        return null;
    }

    @Override
    public PageData<MockHandlerDto> query(final String name, final String httpMethod, final String requestUri, final String label, final PageParam pageParam) {
        return null;
    }

    @Override
    public void delete(final String id) {

    }
}