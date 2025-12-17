package cn.silwings.admin.web.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpMethod;
import cn.silwings.admin.exceptions.DynamicMockAdminException;
import cn.silwings.admin.exceptions.ErrorCode;
import cn.silwings.core.common.Identity;
import cn.silwings.core.utils.CheckUtils;

import java.util.List;
import java.util.Map;

/**
 * @ClassName MockHandlerInfoVo
 * @Description 模拟处理器信息
 * @Author Silwings
 * @Date 2022/11/14 0:15
 * @Since
 **/
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "模拟处理器信息")
public class MockHandlerInfoParam {

    @ApiModelProperty(value = "项目id", required = true, example = "P1")
    private Identity projectId;

    @ApiModelProperty(value = "处理器id", required = true, example = "H1")
    private Identity handlerId;

    @ApiModelProperty(value = "处理器名称", required = true, example = "获取用户信息")
    private String name;

    @ApiModelProperty(value = "支持的请求方式,至少包含一个合法的http请求方法", required = true)
    private List<String> httpMethods;

    @ApiModelProperty(value = "支持的请求URI", required = true, example = "/findUser")
    private String requestUri;

    @ApiModelProperty(value = "处理器标签", example = "User Admin")
    private String label;

    @ApiModelProperty(value = "延迟处理时间", required = true, example = "0")
    private Integer delayTime;

    @ApiModelProperty(value = "自定义参数空间")
    private Map<String, Object> customizeSpace;

    @ApiModelProperty(value = "校验信息")
    private CheckInfoParam checkInfo;

    @ApiModelProperty(value = "响应信息集")
    private List<MockResponseInfoParam> responses;

    @ApiModelProperty(value = "Task信息集")
    private List<SaveTaskInfoParam> tasks;

    @ApiModelProperty(value = "插件信息集")
    private List<MockHandlerPluginInfoParam> pluginInfos;

    public void validate() {
        CheckUtils.isNotNull(this.projectId, DynamicMockAdminException.supplier(ErrorCode.VALID_EMPTY, "projectId"));
        CheckUtils.isNotBlank(this.name, DynamicMockAdminException.supplier(ErrorCode.VALID_EMPTY, "name"));
        CheckUtils.hasMinimumSize(this.httpMethods, 1, DynamicMockAdminException.supplier(ErrorCode.VALID_EMPTY, "httpMethods"));
        this.httpMethods.forEach(method -> CheckUtils.isNotNull(HttpMethod.resolve(method), DynamicMockAdminException.supplier(ErrorCode.VALID_ERROR, "httpMethods")));
        CheckUtils.isNotBlank(this.requestUri, DynamicMockAdminException.supplier(ErrorCode.VALID_EMPTY, "requestUri"));
        if (null != this.checkInfo) {
            this.checkInfo.validate();
        }
        if (CollectionUtils.isNotEmpty(this.pluginInfos)) {
            this.pluginInfos.forEach(MockHandlerPluginInfoParam::validate);
        }
    }
}