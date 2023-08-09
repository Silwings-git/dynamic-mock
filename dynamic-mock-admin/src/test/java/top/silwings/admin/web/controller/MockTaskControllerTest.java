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
import top.silwings.admin.auth.UserAuthInfo;
import top.silwings.admin.auth.UserHolder;
import top.silwings.admin.common.DeleteTaskLogType;
import top.silwings.admin.common.Result;
import top.silwings.admin.common.Role;
import top.silwings.admin.web.setup.SetUp;
import top.silwings.admin.web.vo.param.BatchUnregisterParam;
import top.silwings.admin.web.vo.param.DeleteTaskLogParam;
import top.silwings.admin.web.vo.param.MockHandlerInfoParam;
import top.silwings.admin.web.vo.param.QueryTaskLogParam;
import top.silwings.admin.web.vo.param.QueryTaskParam;
import top.silwings.core.common.Identity;

/**
 * @ClassName MockTaskControllerTest
 * @Description Mock Handler Task Log
 * @Author Silwings
 * @Date 2022/11/25 21:42
 * @Since
 **/
@Slf4j
@Transactional
@SpringBootTest(classes = DynamicMockAdminApplication.class)
@RunWith(SpringRunner.class)
public class MockTaskControllerTest {

    @Autowired
    private SetUp setUp;

    @Autowired
    private MockTaskController mockTaskController;

    @Autowired
    private MockHandlerController mockHandlerController;

    @Test
    public void task() {

        UserHolder.setUser(UserAuthInfo.builder().userId(Identity.from(-1)).role(Role.ADMIN_USER.getCode()).build());

        final Identity projectId = this.setUp.createProject();
        this.query(projectId);
        this.unregister(projectId);
        this.queryTaskLog(projectId);
        this.delTaskLog(projectId);
    }

    private void query(final Identity projectId) {

        final QueryTaskParam queryTaskParam = new QueryTaskParam();
        queryTaskParam.setProjectId(projectId);

        Assert.assertNotNull(this.mockTaskController.query(queryTaskParam));
    }

    private void unregister(final Identity projectId) {

        final BatchUnregisterParam unregisterTaskParam = new BatchUnregisterParam();
        unregisterTaskParam.setProjectId(projectId);
        unregisterTaskParam.setInterrupt(false);

        Assert.assertNotNull(this.mockTaskController.batchUnregister(unregisterTaskParam));
    }

    private void queryTaskLog(final Identity projectId) {

        final MockHandlerInfoParam handlerInfoParam = this.save(projectId);

        final QueryTaskLogParam queryTaskLogParam = new QueryTaskLogParam();
        queryTaskLogParam.setHandlerId(handlerInfoParam.getHandlerId());

        Assert.assertNotNull(this.mockTaskController.queryTaskLog(queryTaskLogParam));
    }

    private void delTaskLog(final Identity projectId) {

        final MockHandlerInfoParam handlerInfoParam = this.save(projectId);

        final DeleteTaskLogParam deleteTaskLogParam = new DeleteTaskLogParam();
        deleteTaskLogParam.setHandlerId(handlerInfoParam.getHandlerId());
        deleteTaskLogParam.setDeleteType(DeleteTaskLogType.ALL.code());

        Assert.assertNotNull(this.mockTaskController.delTaskLog(deleteTaskLogParam));
    }

    private MockHandlerInfoParam save(final Identity projectId) {

        final MockHandlerInfoParam infoParam = SetUp.buildHandler(projectId);

        final Result<Identity> result = this.mockHandlerController.save(infoParam);
        infoParam.setHandlerId(result.getData());

        return infoParam;
    }

}