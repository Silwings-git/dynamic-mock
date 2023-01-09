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
import top.silwings.admin.web.vo.param.QueryFunctionInfoParam;
import top.silwings.admin.web.vo.result.FunctionInfoResult;
import top.silwings.core.handler.tree.dynamic.function.FunctionFactory;
import top.silwings.core.handler.tree.dynamic.function.FunctionReturnType;

import java.util.List;
import java.util.stream.Collectors;

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
        final QueryFunctionInfoParam queryFunctionInfoParam = new QueryFunctionInfoParam();

        final PageResult<FunctionInfoResult> pageResult = this.functionController.query(queryFunctionInfoParam);
        Assert.assertEquals(pageResult.getPageData().size(), this.functionFactoryList.size() + 2);

        queryFunctionInfoParam.setFunctionReturnType(FunctionReturnType.BOOLEAN);
        final PageResult<FunctionInfoResult> pageResultB = this.functionController.query(queryFunctionInfoParam);
        Assert.assertEquals(pageResultB.getPageData().size(),pageResult.getPageData().stream()
                .filter(info -> FunctionReturnType.BOOLEAN.equals(info.getFunctionReturnType())).count());
    }

}