package top.silwings.admin.repository.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.silwings.admin.common.DynamicMockBaseMapper;
import top.silwings.admin.repository.po.MockTaskLogPo;

/**
 * @ClassName MockTaskLogMapper
 * @Description 任务日志
 * @Author Silwings
 * @Date 2022/11/23 23:29
 * @Since
 **/
@Mapper
public interface MockTaskLogMapper extends DynamicMockBaseMapper<MockTaskLogPo> {
}