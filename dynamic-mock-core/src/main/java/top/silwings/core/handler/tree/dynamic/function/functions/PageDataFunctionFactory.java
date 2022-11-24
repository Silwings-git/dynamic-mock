package top.silwings.core.handler.tree.dynamic.function.functions;

import org.springframework.stereotype.Component;
import top.silwings.core.config.DynamicMockContext;
import top.silwings.core.exceptions.DynamicMockException;
import top.silwings.core.exceptions.DynamicValueCompileException;
import top.silwings.core.handler.MockHandlerContext;
import top.silwings.core.handler.tree.NodeInterpreter;
import top.silwings.core.handler.tree.dynamic.AbstractDynamicValue;
import top.silwings.core.handler.tree.dynamic.DynamicValue;
import top.silwings.core.handler.tree.dynamic.function.FunctionFactory;
import top.silwings.core.utils.CheckUtils;
import top.silwings.core.utils.ConvertUtils;
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

    private static final String SYMBOL = "#pageData(...)";

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
     * "body": "${#pageData(#search(<$.body.pageNum>,requestInfo),#search(<$.body.pageSize>,requestInfo),100,{\"code\": \"${#search(name)}\",\"status\": \"${#uuid()}\"})}"
     * }
     * 结果:
     * {"body":[{"code":"Misaka Mikoto","status":"36bef3d9-7732-4750-89bd-7ce3a1ad60d2"}]}
     */
    public static class PageDataFunction extends AbstractDynamicValue {

        private PageDataFunction(final List<DynamicValue> dynamicValueList) {
            super(dynamicValueList);
        }

        public static PageDataFunction from(final List<DynamicValue> dynamicValueList) {
            CheckUtils.hasEqualsSize(dynamicValueList, 4, DynamicValueCompileException.supplier("The PageData function requires 4 arguments."));
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
            final NodeInterpreter pageDataInterpreter = new NodeInterpreter(DynamicMockContext.getInstance().getJsonNodeParser().parse(childNodeValueList.get(3)));

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