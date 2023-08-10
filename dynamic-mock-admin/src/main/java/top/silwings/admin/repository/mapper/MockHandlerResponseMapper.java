package top.silwings.admin.repository.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import top.silwings.admin.common.DynamicMockBaseMapper;
import top.silwings.admin.repository.po.MockHandlerResponsePo;

/**
 * @ClassName MockHandlerResponseMapper
 * @Description
 * @Author Silwings
 * @Date 2023/8/8 10:47
 * @Since
 **/
@Mapper
public interface MockHandlerResponseMapper extends DynamicMockBaseMapper<MockHandlerResponsePo> {

    @Select(" SELECT " +
            " response_id AS responseId, " +
            " handler_id AS handlerId, " +
            " name AS name, " +
            " delay_time AS delayTime, " +
            " sort AS sort " +
            " FROM dm_mock_handler_response" +
            " WHERE" +
            " handler_id = #{handlerId}" +
            " AND response_id = #{responseId}")
    MockHandlerResponsePo findPoForUpdate(@Param("handlerId") Integer handlerId, @Param("responseId") Integer responseId);
}