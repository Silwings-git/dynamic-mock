package top.silwings.core.web;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.silwings.core.common.PageData;
import top.silwings.core.common.PageParam;
import top.silwings.core.common.PageResult;
import top.silwings.core.common.Result;
import top.silwings.core.repository.MockHandlerRepository;
import top.silwings.core.repository.dto.MockHandlerDto;
import top.silwings.core.web.vo.MockHandlerInfoVo;
import top.silwings.core.web.vo.converter.MockHandlerConverter;

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

    private final MockHandlerConverter mockHandlerConverter;

    public MockHandlerController(final MockHandlerRepository mockHandlerRepository, final MockHandlerConverter mockHandlerConverter) {
        this.mockHandlerRepository = mockHandlerRepository;
        this.mockHandlerConverter = mockHandlerConverter;
    }

    @PostMapping
    public Result<String> create(@RequestBody final MockHandlerInfoVo mockHandlerInfoVo) {

        final MockHandlerDto mockHandlerDto = this.mockHandlerConverter.convert(mockHandlerInfoVo);

        final String id = this.mockHandlerRepository.create(mockHandlerDto);

        return Result.ok(id);
    }

    @PutMapping
    public Result<String> update(@RequestBody final MockHandlerInfoVo mockHandlerInfoVo) {

        final MockHandlerDto mockHandlerDto = this.mockHandlerConverter.convert(mockHandlerInfoVo);

        final String id = this.mockHandlerRepository.update(mockHandlerDto);

        return Result.ok(id);
    }

    @GetMapping("/{id}")
    public Result<MockHandlerInfoVo> get(@PathVariable("id") final String id) {

        final MockHandlerDto mockHandlerDto = this.mockHandlerRepository.get(id);

        final MockHandlerInfoVo mockHandlerInfoVo = this.mockHandlerConverter.convert(mockHandlerDto);

        return Result.ok(mockHandlerInfoVo);
    }

    @GetMapping("/{pageNum}/{pageSize}")
    public PageResult<MockHandlerInfoVo> query(@PathVariable("pageNum") final Integer pageNum,
                                               @PathVariable("pageSize") final Integer pageSize,
                                               @RequestParam("name") final String name,
                                               @RequestParam("httpMethod") final String httpMethod,
                                               @RequestParam("requestUri") final String requestUri,
                                               @RequestParam("label") final String label) {

        final PageData<MockHandlerDto> pageData = this.mockHandlerRepository.query(name, httpMethod, requestUri, label, PageParam.of(pageNum, pageSize));

        final List<MockHandlerInfoVo> mockHandlerInfoVoList = pageData.getList().stream()
                .map(this.mockHandlerConverter::convert)
                .collect(Collectors.toList());

        return PageResult.ok(mockHandlerInfoVoList, pageData.getTotal());
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable("id") final String id) {

        this.mockHandlerRepository.delete(id);

        return Result.ok();
    }

}