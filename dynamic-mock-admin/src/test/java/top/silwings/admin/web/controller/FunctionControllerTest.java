package top.silwings.admin.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import top.silwings.admin.DynamicMockAdminApplication;
import top.silwings.admin.common.PageResult;
import top.silwings.admin.web.vo.result.FunctionInfoResult;
import top.silwings.core.handler.tree.dynamic.function.FunctionFactory;

import java.util.List;

/**
 * @ClassName FunctionControllerTest
 * @Description 函数信息
 * @Author Silwings
 * @Date 2022/11/25 21:37
 * @Since
 **/
@Slf4j
@Transactional
@SpringBootTest(classes = DynamicMockAdminApplication.class)
@RunWith(SpringRunner.class)
public class FunctionControllerTest {

    @Autowired
    private FunctionController functionController;

    @Autowired
    private List<FunctionFactory> functionFactoryList;

    @Test
    public void function() {
        final PageResult<FunctionInfoResult> pageResult = this.functionController.query();

        Assert.assertEquals(pageResult.getPageData().size(), this.functionFactoryList.size() + 2);
    }

}