package top.silwings.admin.repository.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.silwings.admin.common.DynamicMockBaseMapper;
import top.silwings.admin.repository.po.MockHandlerTaskRequestPo;

/**
 * @ClassName MockHandlerTaskRequestMapper
 * @Description
 * @Author Silwings
 * @Date 2023/8/7 14:49
 * @Since
 **/
@Mapper
public interface MockHandlerTaskRequestMapper extends DynamicMockBaseMapper<MockHandlerTaskRequestPo> {
}