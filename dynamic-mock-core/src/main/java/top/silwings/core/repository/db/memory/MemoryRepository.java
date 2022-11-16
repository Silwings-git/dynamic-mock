package top.silwings.core.repository.db.memory;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;
import top.silwings.core.common.EnableStatus;
import top.silwings.core.common.Identity;
import top.silwings.core.common.PageData;
import top.silwings.core.common.PageParam;
import top.silwings.core.repository.MockHandlerRepository;
import top.silwings.core.repository.dto.MockHandlerDto;
import top.silwings.core.repository.dto.QueryConditionDto;

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
    public Identity create(final MockHandlerDto mockHandlerDto) {
        return null;
    }

    @Override
    public Identity update(final MockHandlerDto mockHandlerDto) {
        return null;
    }

    @Override
    public MockHandlerDto find(final Identity handlerId) {
        return null;
    }

    @Override
    public PageData<MockHandlerDto> query(final QueryConditionDto queryCondition, final PageParam pageParam) {
        return null;
    }

    @Override
    public void delete(final Identity handlerId) {

    }

    @Override
    public void updateEnableStatus(final Identity handlerId, final EnableStatus enableStatus) {

    }
}