package top.silwings.admin.repository.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.silwings.admin.common.DynamicMockBaseMapper;
import top.silwings.admin.repository.po.MockHandlerTaskPo;

/**
 * @ClassName MockHandlerTaskMapper
 * @Description
 * @Author Silwings
 * @Date 2023/8/7 14:48
 * @Since
 **/
@Mapper
public interface MockHandlerTaskMapper extends DynamicMockBaseMapper<MockHandlerTaskPo> {
}