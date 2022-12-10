package top.silwings.admin.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import top.silwings.admin.DynamicMockAdminApplication;
import top.silwings.admin.auth.UserAuthInfo;
import top.silwings.admin.auth.UserHolder;
import top.silwings.admin.common.PageResult;
import top.silwings.admin.common.Result;
import top.silwings.admin.common.Role;
import top.silwings.admin.service.MockHandlerService;
import top.silwings.admin.web.setup.SetUp;
import top.silwings.admin.web.vo.param.DeleteMockHandlerParam;
import top.silwings.admin.web.vo.param.EnableStatusParam;
import top.silwings.admin.web.vo.param.FindMockHandlerParam;
import top.silwings.admin.web.vo.param.MockHandlerInfoParam;
import top.silwings.admin.web.vo.param.QueryMockHandlerParam;
import top.silwings.admin.web.vo.param.SaveProjectParam;
import top.silwings.admin.web.vo.result.MockHandlerInfoResult;
import top.silwings.admin.web.vo.result.MockHandlerSummaryResult;
import top.silwings.core.common.EnableStatus;
import top.silwings.core.common.Identity;
import top.silwings.core.config.MockHandlerHolder;
import top.silwings.core.handler.MockHandlerFactory;
import top.silwings.core.handler.MockHandlerPoint;
import top.silwings.core.model.MockHandlerDto;
import top.silwings.core.utils.JsonUtils;

import java.nio.charset.StandardCharsets;

/**
 * @ClassName MockHandlerControllerTest
 * @Description
 * @Author Silwings
 * @Date 2022/11/16 22:11
 * @Since
 **/
@Slf4j
@Transactional
@SpringBootTest(classes = DynamicMockAdminApplication.class)
@RunWith(SpringRunner.class)
public class MockHandlerControllerTest {

    final SaveProjectParam projectParam = new SaveProjectParam();
    @Autowired
    private MockHandlerController mockHandlerController;
    @Autowired
    private ProjectController projectController;
    @Autowired
    private MockHandlerPoint mockHandlerPoint;

    @Autowired
    private MockHandlerService mockHandlerService;

    @Autowired
    private MockHandlerFactory mockHandlerFactory;

    @Test
    public void mockHandler() {

        this.login();

        final MockHandlerInfoParam infoParam = this.save();

        final Identity handlerId = infoParam.getHandlerId();

        final Identity projectId = infoParam.getProjectId();

        this.find(handlerId);
        this.query(projectId);
        this.enable(handlerId);
        this.request(infoParam);

        this.update(handlerId);
        this.disable(handlerId);
        this.delete(handlerId);
    }

    private void login() {
        UserHolder.setUser(UserAuthInfo.builder().role(Role.ADMIN_USER.getCode()).build());
    }

    private MockHandlerInfoParam save() {

        this.projectParam.setProjectName("JUNIT_TEST");

        final Result<Identity> save = this.projectController.save(projectParam);

        this.projectParam.setProjectId(save.getData());

        final MockHandlerInfoParam infoParam = SetUp.buildHandler(save.getData());

        final Result<Identity> result = this.mockHandlerController.save(infoParam);
        Assert.assertNotNull(result.getData());
        infoParam.setHandlerId(result.getData());

        return infoParam;
    }

    private void update(final Identity handlerId) {

        final MockHandlerInfoParam update = SetUp.update(handlerId);
        update.setProjectId(this.projectParam.getProjectId());

        final Result<Identity> result = this.mockHandlerController.save(update);

        Assert.assertEquals(result.getData(), handlerId);
    }

    private void find(final Identity handlerId) {

        final FindMockHandlerParam param = new FindMockHandlerParam();
        param.setHandlerId(handlerId);

        final Result<MockHandlerInfoResult> voResult = this.mockHandlerController.find(param);

        Assert.assertNotNull(voResult.getData());
    }

    private void query(final Identity projectId) {

        final QueryMockHandlerParam param = new QueryMockHandlerParam();
        param.setProjectId(projectId);

        final PageResult<MockHandlerSummaryResult> pageResult = this.mockHandlerController.query(param);

        Assert.assertNotNull(pageResult.getPageData());
    }

    private void enable(final Identity handlerId) {

        final EnableStatusParam enableStatusParam = new EnableStatusParam();
        enableStatusParam.setHandlerId(handlerId);
        enableStatusParam.setEnableStatus(EnableStatus.ENABLE.code());
        final Result<Void> result = this.mockHandlerController.updateEnableStatus(enableStatusParam);

        Assert.assertNotNull(result);
    }

    private void disable(final Identity handlerId) {
        final EnableStatusParam enableStatusParam = new EnableStatusParam();
        enableStatusParam.setHandlerId(handlerId);
        enableStatusParam.setEnableStatus(EnableStatus.DISABLE.code());
        final Result<Void> result = this.mockHandlerController.updateEnableStatus(enableStatusParam);

        Assert.assertNotNull(result);
    }

    private void delete(final Identity handlerId) {

        final DeleteMockHandlerParam param = new DeleteMockHandlerParam();
        param.setHandlerId(handlerId);

        final Result<Void> result = this.mockHandlerController.delete(param);

        Assert.assertNotNull(result);
    }

    private void request(final MockHandlerInfoParam infoParam) {

        final MockHandlerDto mockHandler = this.mockHandlerService.find(infoParam.getHandlerId());
        MockHandlerHolder.set(this.mockHandlerFactory.buildMockHandler(mockHandler));

        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod(HttpMethod.GET.name());
        request.addHeader("content-type", "application/json");
        request.setRequestURI(infoParam.getRequestUri());
        request.addParameter("execute", "1");
        request.setContent("{\"pageNum\": \"1\",\"pageSize\": \"10\"}".getBytes(StandardCharsets.UTF_8));

        final ResponseEntity<Object> response = this.mockHandlerPoint.executeMock(request);

        Assert.assertNotNull(response.getBody());

        log.info("Test request result: {}", JsonUtils.toJSONString(response));
    }
}