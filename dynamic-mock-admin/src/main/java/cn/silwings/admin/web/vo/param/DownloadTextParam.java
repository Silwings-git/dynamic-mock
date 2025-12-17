package cn.silwings.admin.web.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import cn.silwings.admin.exceptions.DynamicMockAdminException;
import cn.silwings.admin.exceptions.ErrorCode;
import cn.silwings.core.utils.CheckUtils;

/**
 * @ClassName DownloadTextParam
 * @Description 下载文本文件参数
 * @Author Silwings
 * @Date 2022/12/10 14:22
 * @Since
 **/
@Getter
@Setter
@ApiModel(description = "下载文本文件参数")
public class DownloadTextParam {

    @ApiModelProperty(value = "文件名称.精确查询", required = true, example = "filename.json")
    private String fileName;

    public void validate() {
        CheckUtils.isNotBlank(this.fileName, DynamicMockAdminException.supplier(ErrorCode.VALID_EMPTY, "fileName"));
    }

}