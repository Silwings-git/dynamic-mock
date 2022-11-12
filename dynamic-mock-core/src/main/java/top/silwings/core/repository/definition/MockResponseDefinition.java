package top.silwings.core.repository.definition;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;
import top.silwings.core.utils.ConvertUtils;

import java.util.Collections;
import java.util.Map;

/**
 * @ClassName ResultResultInfoDefinition
 * @Description 返回值信息
 * @Author Silwings
 * @Date 2022/11/9 23:54
 * @Since
 **/
@Getter
@Setter
public class MockResponseDefinition {

    /**
     * 状态码
     */
    private Integer status;

    /**
     * 响应头
     */
    private Map<String, String> headers;

    /**
     * 响应体
     */
    private String body;

    public void setStatus(final Integer status) {
        this.status = status;
    }

    public Map<String, String> getHeaders() {
        return ConvertUtils.getNoNullOrDefault(this.headers, Collections.emptyMap());
    }

    public Object getBody() {
        return JSON.isValidObject(this.body) ? JSON.parseObject(this.body) : this.body;
    }
}