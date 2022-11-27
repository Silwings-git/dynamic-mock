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
import top.silwings.admin.common.Result;
import top.silwings.admin.common.Role;
import top.silwings.admin.web.vo.param.DeleteProjectParam;
import top.silwings.admin.web.vo.param.QueryProjectParam;
import top.silwings.admin.web.vo.param.SaveProjectParam;
import top.silwings.core.common.Identity;

import java.util.UUID;

/**
 * @ClassName ProjectControllerTest
 * @Description
 * @Author Silwings
 * @Date 2022/11/25 21:58
 * @Since
 **/
@Slf4j
@Transactional
@SpringBootTest(classes = DynamicMockAdminApplication.class)
@RunWith(SpringRunner.class)
public class ProjectControllerTest {

    @Autowired
    private ProjectController projectController;

    @Test
    public void project() {
        UserHolder.setUser(UserAuthInfo.builder().role(Role.ADMIN_USER.getCode()).build());

        final SaveProjectParam saveProjectParam = this.save();
        this.query(saveProjectParam);
        this.queryAll();
        this.delete(saveProjectParam.getProjectId());
    }

    public SaveProjectParam save() {

        final SaveProjectParam saveProjectParam = new SaveProjectParam();
        saveProjectParam.setProjectName(UUID.randomUUID().toString().substring(0, 10));
        saveProjectParam.setBaseUri("/" + UUID.randomUUID().toString().substring(0, 10));

        final Result<Identity> saveResult = this.projectController.save(saveProjectParam);
        Assert.assertNotNull(saveResult);

        saveProjectParam.setProjectId(saveResult.getData());
        Assert.assertNotNull(this.projectController.save(saveProjectParam));

        return saveProjectParam;
    }


    public void query(final SaveProjectParam projectParam) {

        final QueryProjectParam queryProjectParam = new QueryProjectParam();

        Assert.assertNotNull(this.projectController.query(queryProjectParam));

        queryProjectParam.setProjectName(projectParam.getProjectName());

        Assert.assertNotNull(this.projectController.query(queryProjectParam));
    }

    public void queryAll() {
        Assert.assertNotNull(this.projectController.queryOwnAll());
    }

    public void delete(final Identity projectId) {

        final DeleteProjectParam deleteProjectParam = new DeleteProjectParam();
        deleteProjectParam.setProjectId(projectId);

        Assert.assertNotNull(this.projectController.delete(deleteProjectParam));
    }

}