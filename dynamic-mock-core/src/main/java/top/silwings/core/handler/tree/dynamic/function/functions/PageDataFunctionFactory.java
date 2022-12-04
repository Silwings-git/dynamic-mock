package top.silwings.core.handler.tree.dynamic.function.functions;

import org.springframework.stereotype.Component;
import top.silwings.core.config.DynamicMockContext;
import top.silwings.core.exceptions.DynamicMockException;
import top.silwings.core.exceptions.DynamicValueCompileException;
import top.silwings.core.handler.MockHandlerContext;
import top.silwings.core.handler.tree.NodeInterpreter;
import top.silwings.core.handler.tree.dynamic.AbstractDynamicValue;
import top.silwings.core.handler.tree.dynamic.DynamicValue;
import top.silwings.core.handler.tree.dynamic.DynamicValueFactory;
import top.silwings.core.handler.tree.dynamic.function.FunctionFactory;
import top.silwings.core.handler.tree.dynamic.function.FunctionInfo;
import top.silwings.core.handler.tree.structure.StaticValueNode;
import top.silwings.core.utils.CheckUtils;
import top.silwings.core.utils.ConvertUtils;
import top.silwings.core.utils.JsonUtils;
import top.silwings.core.utils.TypeUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @ClassName PageDataFunctionFactory
 * @Description 分页数据
 * @Author Silwings
 * @Date 2022/11/13 17:56
 * @Since
 **/
@Component
public class PageDataFunctionFactory implements FunctionFactory {

    private static final FunctionInfo PAGE_DATA_FUNCTION_INFO = FunctionInfo.builder()
            .functionName("PageData")
            .minArgsNumber(4)
            .maxArgsNumber(4)
            .build();

    private static final String SYMBOL = "#pageData(...)";

    @Override
    public FunctionInfo getFunctionInfo() {
        return PAGE_DATA_FUNCTION_INFO;
    }

    @Override
    public boolean support(final String methodName) {
        return "pageData".equalsIgnoreCase(methodName);
    }

    @Override
    public DynamicValue buildFunction(final List<DynamicValue> dynamicValueList) {
        return PageDataFunction.from(dynamicValueList);
    }

    /**
     * 分页数据函数
     * #pageData(#search(当前页),#search(每页数量),总数据量,数据)
     * 注意声明表达式
     * 示例:
     * {
     * "body": "${#pageData(#search(<$.body.pageNum>,requestInfo),#search(<$.body.pageSize>,requestInfo),100,<{\"code\": \"${#search(name)}\",\"status\": \"${#uuid()}\"}>)}"
     * }
     * 结果:
     * {"body":[{"code":"Misaka Mikoto","status":"36bef3d9-7732-4750-89bd-7ce3a1ad60d2"}]}
     * 注意事项：
     * 1.数据可以为表达式，也可以为json字符串，其中允许存表达式。
     * 2.数据必须使用“<>”包裹。
     */
    public static class PageDataFunction extends AbstractDynamicValue {

        private PageDataFunction(final List<DynamicValue> dynamicValueList) {
            super(dynamicValueList);
        }

        public static PageDataFunction from(final List<DynamicValue> dynamicValueList) {
            CheckUtils.sizeBetween(dynamicValueList, PAGE_DATA_FUNCTION_INFO.getMinArgsNumber(), PAGE_DATA_FUNCTION_INFO.getMaxArgsNumber(), DynamicValueCompileException.supplier("Wrong number of parameters of PageData function."));
            return new PageDataFunction(dynamicValueList);
        }

        @Override
        public Object doInterpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {

            if (this.getNodeCount() != childNodeValueList.size() || this.getNodeCount() != 4) {
                throw new DynamicMockException("Parameter incorrectly of `pageData` function. expect: 4 , actual: " + childNodeValueList.size());
            }

            // 计算当前页返回数量
            final int pageNum = ConvertUtils.getNoNullOrDefault(TypeUtils.toInteger(childNodeValueList.get(0)), -1);
            final int pageSize = ConvertUtils.getNoNullOrDefault(TypeUtils.toInteger(childNodeValueList.get(1)), -1);
            final int total = ConvertUtils.getNoNullOrDefault(TypeUtils.toInteger(childNodeValueList.get(2)), -1);

            if (pageNum <= 0 || pageSize < 0 || total < 0) {
                throw new DynamicMockException("Parameter specification error of `pageData` function." +
                        " PageNum should be greater than or equal to 0, actual: " + childNodeValueList.get(0) +
                        " . PageSize should be greater than 0, actual: " + childNodeValueList.get(1) +
                        " . Total should be greater than 0, actual: " + childNodeValueList.get(2));
            }

            final int returnSize = Math.min(total - (pageNum - 1) * pageSize, pageSize);

            if (returnSize <= 0 || null == childNodeValueList.get(3)) {
                return Collections.emptyList();
            }

            // 分页数据解释器
            final NodeInterpreter pageDataInterpreter;

            final Object resultData = childNodeValueList.get(3);

            if (resultData instanceof String && !JsonUtils.isValidJson((String) resultData)) {
                final DynamicValueFactory dynamicValueFactory = DynamicMockContext.getInstance().getDynamicValueFactory();

                final String resultStr = (String) resultData;

                if (dynamicValueFactory.isDynamic(resultStr)) {

                    pageDataInterpreter = new NodeInterpreter(dynamicValueFactory.buildDynamicValue(resultStr));
                } else {
                    pageDataInterpreter = new NodeInterpreter(StaticValueNode.from(resultData));
                }
            } else {
                pageDataInterpreter = new NodeInterpreter(DynamicMockContext.getInstance().getJsonNodeParser().parse(resultData));
            }

            return Stream.iterate(0, t -> t + 1)
                    .limit(returnSize)
                    .map(i -> pageDataInterpreter.interpret(mockHandlerContext))
                    .collect(Collectors.toList());
        }

        @Override
        protected String symbol() {
            return SYMBOL;
        }
    }

}