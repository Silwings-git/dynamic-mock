package top.silwings.core.web;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.silwings.core.web.vo.MockHandlerInfoResultVo;
import top.silwings.core.web.vo.MockHandlerInfoVo;
import top.silwings.core.web.vo.converter.MockHandlerVoConverter;
import top.silwings.core.common.Identity;
import top.silwings.core.common.PageData;
import top.silwings.core.common.PageParam;
import top.silwings.core.common.PageResult;
import top.silwings.core.common.Result;
import top.silwings.core.repository.MockHandlerRepository;
import top.silwings.core.repository.dto.MockHandlerDto;

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

    private final MockHandlerRepository mockHandlerRepository;

    private final MockHandlerVoConverter mockHandlerVoConverter;

    public MockHandlerController(final MockHandlerRepository mockHandlerRepository, final MockHandlerVoConverter mockHandlerVoConverter) {
        this.mockHandlerRepository = mockHandlerRepository;
        this.mockHandlerVoConverter = mockHandlerVoConverter;
    }

    @PostMapping
    public Result<Long> save(@RequestBody final MockHandlerInfoVo mockHandlerInfoVo) {

        final MockHandlerDto mockHandlerDto = this.mockHandlerVoConverter.convert(mockHandlerInfoVo);

        final Identity handlerId;

        if (null == mockHandlerDto.getHandlerId()) {
            handlerId = this.mockHandlerRepository.create(mockHandlerDto);
        } else {
            handlerId = this.mockHandlerRepository.update(mockHandlerDto);
        }

        return Result.ok(handlerId.getId());
    }

    @GetMapping("/{handlerId}")
    public Result<MockHandlerInfoResultVo> find(@PathVariable("handlerId") final Long handlerId) {

        final MockHandlerDto mockHandlerDto = this.mockHandlerRepository.find(Identity.from(handlerId));

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

        final PageData<MockHandlerDto> pageData = this.mockHandlerRepository.query(null, name, httpMethod, requestUri, label, PageParam.of(pageNum, pageSize));

        final List<MockHandlerInfoResultVo> mockHandlerInfoVoList = pageData.getList().stream()
                .map(this.mockHandlerVoConverter::convert)
                .collect(Collectors.toList());

        return PageResult.ok(mockHandlerInfoVoList, pageData.getTotal());
    }

    @DeleteMapping("/{handlerId}")
    public Result<Void> delete(@PathVariable("handlerId") final Long handlerId) {

        this.mockHandlerRepository.delete(Identity.from(handlerId));

        return Result.ok();
    }

}