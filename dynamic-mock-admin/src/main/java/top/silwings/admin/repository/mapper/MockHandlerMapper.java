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
            " handler_id AS handlerId," +
            " project_id AS projectId," +
            " enable_status AS enableStatus," +
            " name AS name," +
            " http_methods AS httpMethods," +
            " request_uri AS requestUri," +
            " label AS label," +
            " delay_time AS delayTime," +
            " customize_space AS customizeSpace," +
            " author AS author," +
            " create_time AS createTime," +
            " update_time AS updateTime," +
            " increment_version AS incrementVersion" +
            " FROM dm_mock_handler" +
            " WHERE handler_id = #{handlerId}" +
            " FOR UPDATE")
    MockHandlerPo findPoForUpdate(@Param("handlerId") Integer handlerId);

}