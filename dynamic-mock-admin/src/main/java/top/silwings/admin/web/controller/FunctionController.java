package top.silwings.admin.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.silwings.admin.auth.annotation.PermissionLimit;
import top.silwings.admin.common.PageResult;
import top.silwings.admin.web.vo.result.FunctionInfoResult;
import top.silwings.core.handler.tree.dynamic.function.FunctionFactory;
import top.silwings.core.utils.ConvertUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName FunctionController
 * @Description 函数信息
 * @Author Silwings
 * @Date 2022/11/25 21:18
 * @Since
 **/
@RestController
@RequestMapping("/dynamic-mock/mock/handler/function")
@Api(value = "函数信息")
public class FunctionController {

    private final List<FunctionFactory> functionFactoryList;

    @Autowired(required = false)
    public FunctionController(final List<FunctionFactory> functionFactoryList) {
        this.functionFactoryList = ConvertUtils.getNoNullOrDefault(functionFactoryList, Collections.emptyList());
    }

    @PostMapping("/query")
    @PermissionLimit()
    @ApiOperation(value = "查询全部函数信息")
    public PageResult<FunctionInfoResult> query() {

        final List<FunctionInfoResult> functionInfoResultList = this.functionFactoryList.stream()
                .map(FunctionFactory::getFunctionInfo)
                .sorted()
                .map(FunctionInfoResult::from)
                .collect(Collectors.toList());

        // 添加一个Dynamic函数
        final FunctionInfoResult dynamic = FunctionInfoResult.builder()
                .functionName("Dynamic")
                .minArgsNumber(0)
                .maxArgsNumber(Integer.MAX_VALUE)
                .build();

        // 添加一个常量函数
        final FunctionInfoResult constant = FunctionInfoResult.builder()
                .functionName("Constant")
                .minArgsNumber(0)
                .maxArgsNumber(Integer.MAX_VALUE)
                .build();

        // 常量函数排在Dynamic之前
        functionInfoResultList.add(0, dynamic);
        functionInfoResultList.add(0, constant);

        return PageResult.ok(functionInfoResultList, functionInfoResultList.size());
    }

}