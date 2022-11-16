package top.silwings.core.repository.db.mysql;

import org.apache.ibatis.annotations.Mapper;
import tk.mybatis.mapper.common.base.insert.InsertSelectiveMapper;
import tk.mybatis.mapper.common.base.select.SelectOneMapper;
import tk.mybatis.mapper.common.condition.DeleteByConditionMapper;
import tk.mybatis.mapper.common.condition.UpdateByConditionSelectiveMapper;
import tk.mybatis.mapper.common.example.SelectCountByExampleMapper;
import tk.mybatis.mapper.common.rowbounds.SelectByConditionRowBoundsMapper;
import top.silwings.core.repository.db.mysql.dao.MockHandlerDao;

/**
 * @ClassName MockHandlerMapper
 * @Description
 * @Author Silwings
 * @Date 2022/11/15 22:33
 * @Since
 **/
@Mapper
public interface MockHandlerMapper extends InsertSelectiveMapper<MockHandlerDao>,
        SelectOneMapper<MockHandlerDao>,
        SelectCountByExampleMapper<MockHandlerDao>,
        SelectByConditionRowBoundsMapper<MockHandlerDao>,
        DeleteByConditionMapper<MockHandlerDao>,
        UpdateByConditionSelectiveMapper<MockHandlerDao> {


}