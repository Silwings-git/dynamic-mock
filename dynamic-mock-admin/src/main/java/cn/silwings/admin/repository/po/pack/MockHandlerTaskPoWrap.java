package cn.silwings.admin.repository.po.pack;

import lombok.Getter;
import lombok.Setter;
import cn.silwings.admin.repository.po.MockHandlerConditionPo;
import cn.silwings.admin.repository.po.MockHandlerTaskPo;
import cn.silwings.admin.repository.po.MockHandlerTaskRequestPo;

import java.util.List;

/**
 * @ClassName MockHandlerTaskPoWrap
 * @Description
 * @Author Silwings
 * @Date 2023/8/7 16:25
 * @Since
 **/
@Getter
@Setter
public class MockHandlerTaskPoWrap {

    private MockHandlerTaskPo mockHandlerTaskPo;

    private List<MockHandlerConditionPo> mockHandlerConditionPoList;

    private MockHandlerTaskRequestPo mockHandlerTaskRequestPo;

}