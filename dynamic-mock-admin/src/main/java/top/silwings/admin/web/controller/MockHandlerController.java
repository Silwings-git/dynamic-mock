package top.silwings.admin.web.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.silwings.admin.application.MockHandlerApplication;
import top.silwings.admin.common.PageData;
import top.silwings.admin.common.PageParam;
import top.silwings.admin.web.vo.EnableStatusVo;
import top.silwings.admin.web.vo.MockHandlerInfoResultVo;
import top.silwings.admin.web.vo.MockHandlerInfoVo;
import top.silwings.admin.web.vo.converter.MockHandlerVoConverter;
import top.silwings.core.common.EnableStatus;
import top.silwings.core.common.Identity;
import top.silwings.core.common.PageResult;
import top.silwings.core.common.Result;
import top.silwings.core.model.dto.MockHandlerDto;
import top.silwings.core.model.dto.QueryConditionDto;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName MockHandlerController
 * @Description 模拟器程序增删改查接口
 * @Author Silwings
 * @Date 2022/11/14 22:10
 * @Since
 **/
@RestController
@RequestMapping("/dynamic/mock/handler")
public class MockHandlerController {

    private final MockHandlerApplication mockHandlerApplication;

    private final MockHandlerVoConverter mockHandlerVoConverter;

    public MockHandlerController(final MockHandlerApplication mockHandlerApplication, final MockHandlerVoConverter mockHandlerVoConverter) {
        this.mockHandlerApplication = mockHandlerApplication;
        this.mockHandlerVoConverter = mockHandlerVoConverter;
    }

    @PostMapping
    public Result<Identity> save(@RequestBody final MockHandlerInfoVo mockHandlerInfoVo) {

        final MockHandlerDto mockHandlerDto = this.mockHandlerVoConverter.convert(mockHandlerInfoVo);

        final Identity handlerId = this.mockHandlerApplication.save(mockHandlerDto);

        return Result.ok(handlerId);
    }

    @GetMapping("/{handlerId}")
    public Result<MockHandlerInfoResultVo> find(@PathVariable("handlerId") final String handlerId) {

        final MockHandlerDto mockHandlerDto = this.mockHandlerApplication.find(Identity.from(handlerId));

        final MockHandlerInfoResultVo mockHandlerInfoVo = this.mockHandlerVoConverter.convert(mockHandlerDto);

        return Result.ok(mockHandlerInfoVo);
    }

    @GetMapping("/{pageNum}/{pageSize}")
    public PageResult<MockHandlerInfoResultVo> query(@PathVariable("pageNum") final Integer pageNum,
                                                     @PathVariable("pageSize") final Integer pageSize,
                                                     @RequestParam("name") final String name,
                                                     @RequestParam("httpMethod") final String httpMethod,
                                                     @RequestParam("requestUri") final String requestUri,
                                                     @RequestParam("label") final String label) {

        final QueryConditionDto queryCondition = QueryConditionDto.builder()
                .handlerIdList(null)
                .name(name)
                .httpMethod(httpMethod)
                .requestUri(requestUri)
                .label(label)
                .build();

        final PageData<MockHandlerDto> pageData = this.mockHandlerApplication.query(queryCondition, PageParam.of(pageNum, pageSize));

        final List<MockHandlerInfoResultVo> mockHandlerInfoVoList = pageData.getList().stream()
                .map(this.mockHandlerVoConverter::convert)
                .collect(Collectors.toList());

        return PageResult.ok(mockHandlerInfoVoList, pageData.getTotal());
    }

    @DeleteMapping("/{handlerId}")
    public Result<Void> delete(@PathVariable("handlerId") final String handlerId) {

        this.mockHandlerApplication.delete(Identity.from(handlerId));

        return Result.ok();
    }

    @PostMapping("/enableStatus")
    public Result<Void> updateEnableStatus(@RequestBody final EnableStatusVo enableStatusVo) {

        this.mockHandlerApplication.updateEnableStatus(Identity.from(enableStatusVo.getHandlerId()), EnableStatus.valueOf(enableStatusVo.getEnableStatus()));

        return Result.ok();
    }

}