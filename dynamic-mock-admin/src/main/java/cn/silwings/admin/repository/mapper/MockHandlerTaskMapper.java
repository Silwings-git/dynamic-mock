package cn.silwings.admin.repository.mapper;

import org.apache.ibatis.annotations.Mapper;
import cn.silwings.admin.common.DynamicMockBaseMapper;
import cn.silwings.admin.repository.po.MockHandlerTaskPo;

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