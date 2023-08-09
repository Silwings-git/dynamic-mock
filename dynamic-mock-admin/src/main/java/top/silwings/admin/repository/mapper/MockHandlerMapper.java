package top.silwings.admin.repository.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import top.silwings.admin.common.DynamicMockBaseMapper;
import top.silwings.admin.repository.po.MockHandlerPo;

/**
 * @ClassName MockHandlerMapper
 * @Description
 * @Author Silwings
 * @Date 2022/11/15 22:33
 * @Since
 **/
@Mapper
public interface MockHandlerMapper extends DynamicMockBaseMapper<MockHandlerPo> {

    @Select(" SELECT" +
            " handler_id,project_id,enable_status,name,http_methods,request_uri,label,delay_time,customize_space,author,create_time,update_time,increment_version" +
            " FROM dm_mock_handler" +
            " WHERE handler_id = #{handlerId}" +
            " FOR UPDATE")
    MockHandlerPo findPoForUpdate(@Param("handlerId") Integer handlerId);

}