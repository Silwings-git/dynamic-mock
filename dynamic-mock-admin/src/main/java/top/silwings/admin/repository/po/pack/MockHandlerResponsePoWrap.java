package top.silwings.admin.repository.po.pack;

import lombok.Getter;
import lombok.Setter;
import top.silwings.admin.repository.po.MockHandlerConditionPo;
import top.silwings.admin.repository.po.MockHandlerResponseItemPo;
import top.silwings.admin.repository.po.MockHandlerResponsePo;

import java.util.List;

/**
 * @ClassName MockHandlerResponsePoWrap
 * @Description
 * @Author Silwings
 * @Date 2023/8/8 13:45
 * @Since
 **/
@Getter
@Setter
public class MockHandlerResponsePoWrap {

    /**
     * 主体信息
     */
    private MockHandlerResponsePo mockHandlerResponsePo;

    /**
     * 条件信息
     */
    private List<MockHandlerConditionPo> mockHandlerConditionPoList;

    /**
     * 响应信息
     */
    private MockHandlerResponseItemPo mockHandlerResponseItemPo;

}