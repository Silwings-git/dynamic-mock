package cn.silwings.admin.repository.mapper;

import org.apache.ibatis.annotations.Mapper;
import cn.silwings.admin.common.DynamicMockBaseMapper;
import cn.silwings.admin.repository.po.ProjectPo;

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