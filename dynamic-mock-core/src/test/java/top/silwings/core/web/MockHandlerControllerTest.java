package top.silwings.core.web;

import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.NoHandlerFoundException;
import top.silwings.core.MockSpringApplication;
import top.silwings.core.common.EnableStatus;
import top.silwings.core.common.Identity;
import top.silwings.core.common.PageResult;
import top.silwings.core.common.Result;
import top.silwings.core.utils.JsonUtils;
import top.silwings.core.web.vo.EnableStatusVo;
import top.silwings.core.web.vo.MockHandlerInfoResultVo;

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
@SpringBootTest(classes = MockSpringApplication.class)
@RunWith(SpringRunner.class)
public class MockHandlerControllerTest {

    @Autowired
    private MockHandlerController mockHandlerController;

    @Autowired
    private MockHandlerPoint mockHandlerPoint;

    @Test
    public void create() {
        final Identity handlerId = this.save();
        this.update(handlerId);
        this.find(handlerId);
        this.query();
        this.enable(handlerId);
        this.request();
        this.disable(handlerId);
        this.delete(handlerId);
    }

    private Identity save() {

        final Result<Identity> result = this.mockHandlerController.save(MockHandlerSetUp.buildTestMockHandlerInfoVo());

        Assert.assertNotNull(result.getData());

        return result.getData();
    }

    private void update(final Identity handlerId) {

        final Result<Identity> result = this.mockHandlerController.save(MockHandlerSetUp.buildTestMockHandlerInfoVo(handlerId));

        Assert.assertEquals(result.getData(), handlerId);
    }

    private void find(final Identity handlerId) {

        final Result<MockHandlerInfoResultVo> voResult = this.mockHandlerController.find(handlerId.stringValue());

        Assert.assertNotNull(voResult.getData());
    }

    private void query() {

        final PageResult<MockHandlerInfoResultVo> pageResult = this.mockHandlerController.query(1, 10, null, null, null, null);

        Assert.assertNotNull(pageResult.getPageData());
    }

    private void enable(final Identity handlerId) {

        final EnableStatusVo enableStatusVo = new EnableStatusVo();
        enableStatusVo.setHandlerId(handlerId.stringValue());
        enableStatusVo.setEnableStatus(EnableStatus.ENABLE.code());
        final Result<Void> result = this.mockHandlerController.updateEnableStatus(enableStatusVo);

        Assert.assertNotNull(result);
    }

    private void disable(final Identity handlerId) {
        final EnableStatusVo enableStatusVo = new EnableStatusVo();
        enableStatusVo.setHandlerId(handlerId.stringValue());
        enableStatusVo.setEnableStatus(EnableStatus.DISABLE.code());
        final Result<Void> result = this.mockHandlerController.updateEnableStatus(enableStatusVo);

        Assert.assertNotNull(result);
    }

    private void delete(final Identity handlerId) {

        final Result<Void> result = this.mockHandlerController.delete(handlerId.stringValue());

        Assert.assertNotNull(result);
    }

    private void request() {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod(HttpMethod.GET.name());
        request.addHeader("content-type", "application/json");
        request.setRequestURI("/test");
        request.addParameter("execute", "1");
        request.setContent("{\"pageNum\": \"1\",\"pageSize\": \"10\"}".getBytes(StandardCharsets.UTF_8));

        try {
            final ResponseEntity<Object> response = this.mockHandlerPoint.executeMock(new NoHandlerFoundException("GET", "", new HttpHeaders()), request);

            Assert.assertNotNull(response.getBody());

            log.info("Test request result: {}", JsonUtils.toJSONString(response, SerializerFeature.WriteMapNullValue));

        } catch (NoHandlerFoundException e) {
            Assert.assertTrue(false);
        }
    }
}