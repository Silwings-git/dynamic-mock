package top.silwings.admin.repository.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.silwings.admin.common.DynamicMockBaseMapper;
import top.silwings.admin.repository.po.ProjectPo;

/**
 * @ClassName ProjectMapper
 * @Description
 * @Author Silwings
 * @Date 2022/11/19 19:36
 * @Since
 **/
@Mapper
public interface ProjectMapper extends DynamicMockBaseMapper<ProjectPo> {
}