package top.silwings.core.interpreter.dynamic_expression.function.function_factory;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import top.silwings.core.config.DynamicMockContext;
import top.silwings.core.exceptions.DynamicMockException;
import top.silwings.core.exceptions.DynamicValueCompileException;
import top.silwings.core.handler.MockHandlerContext;
import top.silwings.core.interpreter.ExpressionInterpreter;
import top.silwings.core.interpreter.ExpressionTreeNode;
import top.silwings.core.interpreter.dynamic_expression.DynamicExpressionFactory;
import top.silwings.core.interpreter.dynamic_expression.function.AbstractFunctionExpression;
import top.silwings.core.interpreter.dynamic_expression.function.FunctionFactory;
import top.silwings.core.interpreter.dynamic_expression.function.FunctionInfo;
import top.silwings.core.interpreter.dynamic_expression.function.FunctionReturnType;
import top.silwings.core.interpreter.json.StaticValueNode;
import top.silwings.core.utils.CheckUtils;
import top.silwings.core.utils.ConvertUtils;
import top.silwings.core.utils.JsonUtils;
import top.silwings.core.utils.TypeUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @ClassName PageFunctionFactory
 * @Description 分页数据
 * @Author Silwings
 * @Date 2022/11/13 17:56
 * @Since
 **/
@Component
public class PageFunctionFactory implements FunctionFactory {

    private static final FunctionInfo PAGE_FUNCTION_INFO = FunctionInfo.builder()
            .functionName("Page")
            .minArgsNumber(3)
            .maxArgsNumber(5)
            .functionReturnType(FunctionReturnType.LIST)
            .build();

    private static final String SYMBOL = "#page(...)";

    @Override
    public FunctionInfo getFunctionInfo() {
        return PAGE_FUNCTION_INFO;
    }

    @Override
    public boolean support(final String methodName) {
        return "page".equalsIgnoreCase(methodName);
    }

    @Override
    public PageFunction buildFunction(final List<ExpressionTreeNode> functionExpressionList) {
        CheckUtils.sizeBetween(functionExpressionList, PAGE_FUNCTION_INFO.getMinArgsNumber(), PAGE_FUNCTION_INFO.getMaxArgsNumber(), DynamicValueCompileException.supplier("Wrong number of parameters of Page function."));
        return PageFunction.from(functionExpressionList);
    }

    /**
     * 分页数据函数
     * 1.#page(当前页,每页数量,总数据量,数据模板)
     * 2.#page(当前页,每页数量,总数据量,数据模板,数据是否动态)
     * 3.#page(当前页,每页数量,数据集)
     * 其中,1,2函数的数据支持json格式和文本格式
     * 3函数的数据集仅支持集合对象
     */
    public static class PageFunction extends AbstractFunctionExpression {

        private PageFunction(final List<ExpressionTreeNode> functionExpressionList) {
            super(functionExpressionList);
        }

        private static PageFunction from(final List<ExpressionTreeNode> functionExpressionList) {
            return new PageFunction(functionExpressionList);
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        @Override
        public List<Object> doInterpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {

            if (this.getNodeCount() != childNodeValueList.size()) {
                throw DynamicMockException.from("Parameter incorrectly of `page` function. expect: " + this.getNodeCount() + " , actual: " + childNodeValueList.size());
            }

            final int pageNum = ConvertUtils.getNoNullOrDefault(TypeUtils.toInteger(childNodeValueList.get(0)), -1);
            final int pageSize = ConvertUtils.getNoNullOrDefault(TypeUtils.toInteger(childNodeValueList.get(1)), -1);
            final Object arg3 = childNodeValueList.get(2);

            if (arg3 instanceof List
                    || (arg3 instanceof String && JsonUtils.isValidListJson((String) arg3))) {

                final boolean dynamic = childNodeValueList.size() < 4 || TypeUtils.toBooleanValue(childNodeValueList.get(3));

                return this.pageFromList(pageNum, pageSize, arg3 instanceof List ? (List) arg3 : JsonUtils.toList((String) arg3, Object.class), dynamic, mockHandlerContext);

            } else if (childNodeValueList.size() > 3) {

                final int total = ConvertUtils.getNoNullOrDefault(TypeUtils.toInteger(arg3), -1);
                final Object pageItem = childNodeValueList.get(3);
                final boolean dynamic = childNodeValueList.size() < 5 || TypeUtils.toBooleanValue(childNodeValueList.get(4));
                return this.pageFormData(pageNum, pageSize, total, pageItem, dynamic, mockHandlerContext);
            }

            throw DynamicMockException.from("Parameter incorrectly of `page` : " + JsonUtils.toJSONString(childNodeValueList));
        }

        private List<Object> pageFormData(final int pageNum, final int pageSize, final int total, final Object pageItem, final boolean dynamic, final MockHandlerContext mockHandlerContext) {

            final int returnSize = getReturnSize(pageNum, pageSize, total);

            if (returnSize <= 0 || null == pageItem) {
                return Collections.emptyList();
            }

            final Stream<Object> stream = Stream.iterate(0, t -> t + 1)
                    .limit(returnSize)
                    .map(i -> pageItem);

            if (dynamic) {
                final ExpressionInterpreter pageInterpreter = this.buildNodeInterpreter(pageItem);
                return stream
                        .map(i -> pageInterpreter.interpret(mockHandlerContext))
                        .collect(Collectors.toList());
            }

            return stream.collect(Collectors.toList());
        }

        private int getReturnSize(final int pageNum, final int pageSize, final int total) {
            if (pageNum <= 0 || pageSize < 0 || total < 0) {
                throw new DynamicMockException("Parameter specification error of `page` function." +
                        " PageNum should be greater than or equal to 0, actual: " + pageNum +
                        " . PageSize should be greater than 0, actual: " + pageSize +
                        " . Total should be greater than 0, actual: " + total);
            }

            return Math.min(total - (pageNum - 1) * pageSize, pageSize);
        }

        private List<Object> pageFromList(final int pageNum, final int pageSize, final List<Object> pageDataList, final boolean dynamic, final MockHandlerContext mockHandlerContext) {

            final int returnSize = this.getReturnSize(pageNum, pageSize, pageDataList.size());

            if (returnSize <= 0 || CollectionUtils.isEmpty(pageDataList)) {
                return Collections.emptyList();
            }

            final int start = (pageNum - 1) * pageSize;

            final Stream<Object> objectStream = Stream.iterate(start, t -> t + 1)
                    .limit(returnSize)
                    .map(pageDataList::get);

            if (dynamic) {
                return objectStream
                        .map(this::buildNodeInterpreter)
                        .map(interpreter -> interpreter.interpret(mockHandlerContext))
                        .collect(Collectors.toList());
            }

            return objectStream.collect(Collectors.toList());
        }

        private ExpressionInterpreter buildNodeInterpreter(final Object obj) {

            final ExpressionInterpreter pageInterpreter;

            if (obj instanceof String && !JsonUtils.isValidJson((String) obj)) {

                final DynamicExpressionFactory dynamicExpressionFactory = DynamicMockContext.getInstance().getDynamicExpressionFactory();

                final String resultStr = (String) obj;

                if (DynamicExpressionFactory.isDynamic(resultStr)) {

                    pageInterpreter = new ExpressionInterpreter(dynamicExpressionFactory.buildDynamicValue(resultStr));
                } else {

                    pageInterpreter = new ExpressionInterpreter(StaticValueNode.from(obj));
                }
            } else {
                pageInterpreter = new ExpressionInterpreter(DynamicMockContext.getInstance().getJsonTreeParser().parse(obj));
            }

            return pageInterpreter;
        }

        @Override
        protected String symbol() {
            return SYMBOL;
        }
    }

}