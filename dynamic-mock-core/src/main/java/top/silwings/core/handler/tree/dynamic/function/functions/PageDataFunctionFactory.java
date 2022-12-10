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
            .minArgsNumber(3)
            .maxArgsNumber(5)
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
     * 1.#pageData(#search(当前页),#search(每页数量),总数据量,数据)
     * 2.#pageData(#search(当前页),#search(每页数量),总数据量,数据,数据是否动态)
     * 3.#pageData(#search(当前页),#search(每页数量),数据集)
     * 其中,1,2函数的数据支持json格式和文本格式
     * 3函数的数据集仅支持集合对象
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
        public List<Object> doInterpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {

            if (this.getNodeCount() != childNodeValueList.size()) {
                throw DynamicMockException.from("Parameter incorrectly of `pageData` function. expect: " + this.getNodeCount() + " , actual: " + childNodeValueList.size());
            }

            final int pageNum = ConvertUtils.getNoNullOrDefault(TypeUtils.toInteger(childNodeValueList.get(0)), -1);
            final int pageSize = ConvertUtils.getNoNullOrDefault(TypeUtils.toInteger(childNodeValueList.get(1)), -1);
            final Object arg3 = childNodeValueList.get(2);
            if (arg3 instanceof List || this.isListStr(arg3)) {

                return this.pageFromList(pageNum, pageSize, arg3 instanceof List ? (List) arg3 : JsonUtils.toList((String) arg3, Object.class));

            } else if (childNodeValueList.size() > 3) {

                final int total = ConvertUtils.getNoNullOrDefault(TypeUtils.toInteger(arg3), -1);
                final Object pageItem = childNodeValueList.get(3);
                final boolean dynamic = childNodeValueList.size() >= 5 && TypeUtils.toBooleanValue(childNodeValueList.get(4));
                return this.pageFormData(pageNum, pageSize, total, pageItem, dynamic, mockHandlerContext);

            } else {

                throw DynamicMockException.from("Parameter incorrectly of `pageData` : " + JsonUtils.toJSONString(childNodeValueList));
            }
        }

        private boolean isListStr(final Object arg) {

            if (arg instanceof String) {
                final String argStr = (String) arg;
                if (argStr.startsWith("[") && argStr.endsWith("]") && JsonUtils.isValidJson(argStr)) {
                    return true;
                }
            }

            return false;
        }

        private List<Object> pageFormData(final int pageNum, final int pageSize, final int total, final Object pageItem, final boolean dynamic, final MockHandlerContext mockHandlerContext) {

            final int returnSize = getReturnSize(pageNum, pageSize, total);

            if (returnSize <= 0 || null == pageItem) {
                return Collections.emptyList();
            }

            if (dynamic) {
                // 分页数据解释器
                final NodeInterpreter pageDataInterpreter;

                if (pageItem instanceof String && !JsonUtils.isValidJson((String) pageItem)) {
                    final DynamicValueFactory dynamicValueFactory = DynamicMockContext.getInstance().getDynamicValueFactory();

                    final String resultStr = (String) pageItem;

                    if (DynamicValueFactory.isDynamic(resultStr)) {

                        pageDataInterpreter = new NodeInterpreter(dynamicValueFactory.buildDynamicValue(resultStr));
                    } else {
                        pageDataInterpreter = new NodeInterpreter(StaticValueNode.from(pageItem));
                    }
                } else {
                    pageDataInterpreter = new NodeInterpreter(DynamicMockContext.getInstance().getJsonNodeParser().parse(pageItem));
                }

                return Stream.iterate(0, t -> t + 1)
                        .limit(returnSize)
                        .map(i -> pageDataInterpreter.interpret(mockHandlerContext))
                        .collect(Collectors.toList());
            } else {
                return Stream.iterate(0, t -> t + 1)
                        .limit(returnSize)
                        .map(i -> pageItem)
                        .collect(Collectors.toList());
            }
        }

        private int getReturnSize(final int pageNum, final int pageSize, final int total) {
            if (pageNum <= 0 || pageSize < 0 || total < 0) {
                throw new DynamicMockException("Parameter specification error of `pageData` function." +
                        " PageNum should be greater than or equal to 0, actual: " + pageNum +
                        " . PageSize should be greater than 0, actual: " + pageSize +
                        " . Total should be greater than 0, actual: " + total);
            }

            return Math.min(total - (pageNum - 1) * pageSize, pageSize);
        }

        private List<Object> pageFromList(final int pageNum, final int pageSize, final List<Object> pageDataSourceList) {

            final int returnSize = this.getReturnSize(pageNum, pageSize, pageDataSourceList.size());

            final int start = (pageNum - 1) * pageSize;

            return Stream.iterate(start, t -> t + 1)
                    .limit(returnSize)
                    .map(pageDataSourceList::get)
                    .collect(Collectors.toList());
        }

        @Override
        protected String symbol() {
            return SYMBOL;
        }
    }

}