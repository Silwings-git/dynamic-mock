package cn.silwings.admin.repository.mapper;

import org.apache.ibatis.annotations.Mapper;
import cn.silwings.admin.common.DynamicMockBaseMapper;
import cn.silwings.admin.repository.po.UserPo;

/**
 * @ClassName UserMapper
 * @Description
 * @Author Silwings
 * @Date 2022/11/19 15:58
 * @Since
 **/
@Mapper
public interface UserMapper extends DynamicMockBaseMapper<UserPo> {

}