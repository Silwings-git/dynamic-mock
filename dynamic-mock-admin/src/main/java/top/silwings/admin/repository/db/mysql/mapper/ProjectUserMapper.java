package top.silwings.admin.repository.db.mysql.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.silwings.admin.common.DynamicMockBaseMapper;
import top.silwings.admin.repository.db.mysql.po.ProjectUserPo;

/**
 * @ClassName ProjectUserMapper
 * @Description 项目与用户关系
 * @Author Silwings
 * @Date 2022/11/20 14:09
 * @Since
 **/
@Mapper
public interface ProjectUserMapper extends DynamicMockBaseMapper<ProjectUserPo> {
}