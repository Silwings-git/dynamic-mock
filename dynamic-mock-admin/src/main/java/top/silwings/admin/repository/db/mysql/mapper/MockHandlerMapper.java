package top.silwings.admin.repository.db.mysql.mapper;

import org.apache.ibatis.annotations.Mapper;
import tk.mybatis.mapper.common.base.insert.InsertSelectiveMapper;
import tk.mybatis.mapper.common.base.select.SelectOneMapper;
import tk.mybatis.mapper.common.condition.DeleteByConditionMapper;
import tk.mybatis.mapper.common.condition.UpdateByConditionSelectiveMapper;
import tk.mybatis.mapper.common.example.SelectCountByExampleMapper;
import tk.mybatis.mapper.common.rowbounds.SelectByConditionRowBoundsMapper;
import top.silwings.admin.repository.db.mysql.po.MockHandlerPo;

/**
 * @ClassName MockHandlerMapper
 * @Description
 * @Author Silwings
 * @Date 2022/11/15 22:33
 * @Since
 **/
@Mapper
public interface MockHandlerMapper extends InsertSelectiveMapper<MockHandlerPo>,
        SelectOneMapper<MockHandlerPo>,
        SelectCountByExampleMapper<MockHandlerPo>,
        SelectByConditionRowBoundsMapper<MockHandlerPo>,
        DeleteByConditionMapper<MockHandlerPo>,
        UpdateByConditionSelectiveMapper<MockHandlerPo> {


}