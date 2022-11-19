package top.silwings.admin.repository.db.mysql.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.silwings.admin.common.DynamicMockBaseMapper;
import top.silwings.admin.repository.db.mysql.po.MockHandlerUniquePo;

/**
 * @ClassName MockHandlerUniqueMapper
 * @Description
 * @Author Silwings
 * @Date 2022/11/15 22:58
 * @Since
 **/
@Mapper
public interface MockHandlerUniqueMapper extends DynamicMockBaseMapper<MockHandlerUniquePo> {
}