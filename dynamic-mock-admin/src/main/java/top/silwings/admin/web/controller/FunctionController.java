package top.silwings.admin.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.silwings.admin.auth.annotation.PermissionLimit;
import top.silwings.admin.common.PageResult;
import top.silwings.admin.web.vo.param.QueryFunctionInfoParam;
import top.silwings.admin.web.vo.result.FunctionInfoResult;
import top.silwings.core.interpreter.dynamic_expression.function.FunctionFactory;
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
    @PermissionLimit(limit = false)
    @ApiOperation(value = "查询全部函数信息")
    public PageResult<FunctionInfoResult> query(@RequestBody final QueryFunctionInfoParam queryFunctionInfoParam) {

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
                .functionReturnType(top.silwings.core.interpreter.dynamic_expression.function.FunctionReturnType.OBJECT)
                .description("动态函数，使用字符串拼接的方式动态构建表达式。可以将多个字符串片段拼接成完整的动态表达式并执行。")
                .example("#Dynamic('#Search($.', 'userId', ')')\n" +
                        "#Dynamic('#', 'Time', '()')")
                .build();

        // 添加一个常量函数
        final FunctionInfoResult constant = FunctionInfoResult.builder()
                .functionName("Constant")
                .minArgsNumber(0)
                .maxArgsNumber(Integer.MAX_VALUE)
                .functionReturnType(top.silwings.core.interpreter.dynamic_expression.function.FunctionReturnType.OBJECT)
                .description("常量函数，直接返回指定的常量值。可以是字符串、数字、布尔值等任意类型的常量。")
                .example("#Constant('hello')\n" +
                        "#Constant(123)\n" +
                        "#Constant(true)")
                .build();

        // 常量函数排在Dynamic之前
        functionInfoResultList.add(0, dynamic);
        functionInfoResultList.add(0, constant);

        final List<FunctionInfoResult> returnList = this.filter(queryFunctionInfoParam, functionInfoResultList);

        return PageResult.ok(returnList, returnList.size());
    }

    private List<FunctionInfoResult> filter(final QueryFunctionInfoParam queryFunctionInfoParam, final List<FunctionInfoResult> functionInfoResultList) {

        if (null == queryFunctionInfoParam.getFunctionReturnType()) {
            return functionInfoResultList;
        }

        return functionInfoResultList.stream()
                .filter(info -> null != info.getFunctionReturnType())
                .filter(info -> info.getFunctionReturnType().equals(queryFunctionInfoParam.getFunctionReturnType()))
                .collect(Collectors.toList());
    }

}