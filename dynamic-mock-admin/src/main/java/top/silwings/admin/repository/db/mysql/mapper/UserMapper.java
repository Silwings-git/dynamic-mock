package top.silwings.admin.repository.db.mysql.mapper;

import org.apache.ibatis.annotations.Mapper;
import tk.mybatis.mapper.common.base.insert.InsertSelectiveMapper;
import tk.mybatis.mapper.common.condition.DeleteByConditionMapper;
import tk.mybatis.mapper.common.condition.UpdateByConditionSelectiveMapper;
import tk.mybatis.mapper.common.rowbounds.SelectByConditionRowBoundsMapper;
import top.silwings.admin.repository.db.mysql.po.UserPo;

/**
 * @ClassName UserMapper
 * @Description
 * @Author Silwings
 * @Date 2022/11/19 15:58
 * @Since
 **/
@Mapper
public interface UserMapper extends SelectByConditionRowBoundsMapper<UserPo>,
        InsertSelectiveMapper<UserPo>,
        DeleteByConditionMapper<UserPo>,
        UpdateByConditionSelectiveMapper<UserPo> {

}