package top.silwings.admin.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.silwings.admin.auth.annotation.PermissionLimit;
import top.silwings.admin.common.Result;
import top.silwings.admin.exceptions.DynamicMockAdminException;
import top.silwings.admin.exceptions.ErrorCode;
import top.silwings.admin.model.TextFile;
import top.silwings.admin.service.FileService;
import top.silwings.admin.web.vo.param.DownloadTextParam;
import top.silwings.core.utils.CheckUtils;
import top.silwings.core.utils.ConvertUtils;
import top.silwings.core.utils.JsonUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * @ClassName FileController
 * @Description 文件
 * @Author Silwings
 * @Date 2022/12/10 13:12
 * @Since
 **/
@Slf4j
@RestController
@RequestMapping("/dynamic-mock/file")
@Api(value = "文件管理")
public class FileController {

    private final FileService fileService;

    public FileController(final FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/text/upload")
    @PermissionLimit
    @ApiOperation(value = "上传文本文件")
    public Result<String> uploadText(@RequestParam("file") final MultipartFile file) {

        final String fileOriginalFileName = ConvertUtils.getNoBlankOrDefault(file.getOriginalFilename(), "");

        CheckUtils.isTrue(fileOriginalFileName.endsWith(".json") || fileOriginalFileName.endsWith(".txt"), DynamicMockAdminException.supplier(ErrorCode.UNSUPPORTED_FILE_TYPE));

        final String jsonContent;
        try {
            jsonContent = new String(file.getBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("File read error.", e);
            throw DynamicMockAdminException.from(ErrorCode.FILE_READ_ERROR);
        }

        CheckUtils.isTrue(JsonUtils.isValidJson(jsonContent), DynamicMockAdminException.supplier(ErrorCode.CONTENT_FORMAT_ERROR));

        final TextFile textFile = this.fileService.save(file.getOriginalFilename(), jsonContent);

        return Result.ok(textFile.getFileName());
    }

    @PostMapping("/text/download")
    @PermissionLimit
    @ApiOperation(value = "下载文件")
    public void downloadText(@RequestBody final DownloadTextParam param, final HttpServletResponse response) {

        param.validate();

        final TextFile textFile = this.fileService.find(param.getFileName());

        response.setContentType("text/" + textFile.getSuffixName() + ";charset=UTF-8");

        try {
            try (final PrintWriter writer = response.getWriter()) {
                writer.print(textFile.getContent());
            }
        } catch (IOException e) {
            log.error("Download text error.", e);
            throw DynamicMockAdminException.from(ErrorCode.FILE_READ_ERROR);
        }
    }

}