package top.silwings.admin.repository.db.mysql;

import org.apache.ibatis.annotations.Mapper;
import tk.mybatis.mapper.common.ConditionMapper;
import tk.mybatis.mapper.common.special.InsertListMapper;
import top.silwings.admin.repository.db.mysql.dao.MockHandlerUniqueDao;

/**
 * @ClassName MockHandlerUniqueMapper
 * @Description
 * @Author Silwings
 * @Date 2022/11/15 22:58
 * @Since
 **/
@Mapper
public interface MockHandlerUniqueMapper extends InsertListMapper<MockHandlerUniqueDao>,
        ConditionMapper<MockHandlerUniqueDao> {
}