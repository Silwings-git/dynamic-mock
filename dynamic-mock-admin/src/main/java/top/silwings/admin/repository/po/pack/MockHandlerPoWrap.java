package top.silwings.admin.repository.po.pack;

import lombok.Getter;
import lombok.Setter;
import top.silwings.admin.repository.po.MockHandlerPo;

import java.util.List;

/**
 * @ClassName MockHandlerComponentsPo
 * @Description
 * @Author Silwings
 * @Date 2023/8/7 16:15
 * @Since
 **/
@Getter
@Setter
public class MockHandlerPoWrap {

    private MockHandlerPo mockHandlerPo;

    private List<MockHandlerTaskPoWrap> mockHandlerTaskPoWrapList;

}