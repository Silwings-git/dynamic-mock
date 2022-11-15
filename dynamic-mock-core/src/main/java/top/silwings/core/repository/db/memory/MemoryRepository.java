package top.silwings.core.repository.db.memory;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;
import top.silwings.core.common.PageData;
import top.silwings.core.common.PageParam;
import top.silwings.core.repository.MockHandlerRepository;
import top.silwings.core.repository.dto.MockHandlerDto;

/**
 * @ClassName MemoryRepository
 * @Description 内存存储库
 * @Author Silwings
 * @Date 2022/11/9 22:06
 * @Since
 **/
@ConditionalOnMissingBean(MockHandlerRepository.class)
@Component
public class MemoryRepository implements MockHandlerRepository {

    @Override
    public String create(final MockHandlerDto mockHandlerDto) {
        // TODO_Silwings: 2022/11/15
        return null;
    }

    @Override
    public String update(final MockHandlerDto mockHandlerDto) {
        // TODO_Silwings: 2022/11/15
        return null;
    }

    @Override
    public MockHandlerDto get(final String id) {
        // TODO_Silwings: 2022/11/15
        return null;
    }

    @Override
    public PageData<MockHandlerDto> query(final String name, final String httpMethod, final String requestUri, final String label, final PageParam pageParam) {
        // TODO_Silwings: 2022/11/15
        return null;
    }

    @Override
    public void delete(final String id) {
        // TODO_Silwings: 2022/11/15
    }
}