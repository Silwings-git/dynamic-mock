package cn.silwings.admin.data_migration.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import cn.silwings.admin.common.DynamicMockBaseMapper;
import cn.silwings.admin.data_migration.DataMigrationMockHandlerPo;

import java.util.List;

/**
 * @ClassName DataMigrationMapper
 * @Description
 * @Author Silwings
 * @Date 2023/8/10 1:55
 * @Since
 **/
@Mapper
public interface DataMigrationMapper extends DynamicMockBaseMapper<DataMigrationMockHandlerPo> {

    @Select("SELECT DISTINCT handler_id FROM dm_mock_handler_response")
    List<String> queryHandlerIdInResponse();

    @Select("SELECT DISTINCT handler_id FROM dm_mock_handler_condition")
    List<String> queryHandlerIdInCondition();

    @Select("SELECT DISTINCT handler_id FROM dm_mock_handler_task")
    List<String> queryHandlerIdInTask();
}