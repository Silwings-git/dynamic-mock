package top.silwings.core.interpreter.expression.function.function_factory;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import top.silwings.core.exceptions.DynamicMockException;
import top.silwings.core.exceptions.DynamicValueCompileException;
import top.silwings.core.handler.MockHandlerContext;
import top.silwings.core.interpreter.Expression;
import top.silwings.core.interpreter.expression.function.AbstractFunctionExpression;
import top.silwings.core.interpreter.expression.function.FunctionFactory;
import top.silwings.core.interpreter.expression.function.FunctionInfo;
import top.silwings.core.interpreter.expression.function.FunctionReturnType;
import top.silwings.core.utils.CheckUtils;
import top.silwings.core.utils.TypeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @ClassName RandomFunctionFactory
 * @Description 随机数函数
 * @Author Silwings
 * @Date 2022/11/24 23:01
 * @Since
 **/
@Component
public class RandomFunctionFactory implements FunctionFactory {

    private static final FunctionInfo RANDOM_FUNCTION_INFO = FunctionInfo.builder()
            .functionName("Random")
            .minArgsNumber(0)
            .maxArgsNumber(3)
            .functionReturnType(FunctionReturnType.OBJECT)
            .build();

    private static final String SYMBOL = "#random(...)";

    @Override
    public FunctionInfo getFunctionInfo() {
        return RANDOM_FUNCTION_INFO;
    }

    @Override
    public boolean support(final String methodName) {
        return "random".equalsIgnoreCase(methodName);
    }

    @Override
    public RandomFunction buildFunction(final List<Expression> functionExpressionList) {
        CheckUtils.sizeBetween(functionExpressionList, RANDOM_FUNCTION_INFO.getMinArgsNumber(), RANDOM_FUNCTION_INFO.getMaxArgsNumber(), DynamicValueCompileException.supplier("Wrong number of parameters of Random function."));
        return RandomFunction.from(functionExpressionList);
    }

    public enum RandomType {

        INT("int"),
        LONG("long"),
        DOUBLE("double"),
        BOOLEAN("boolean"),
        ;
        private final String code;

        RandomType(final String code) {
            this.code = code;
        }

        public static RandomType valueOfCode(final String type) {
            for (final RandomType value : values()) {
                if (value.code.equalsIgnoreCase(type)) {
                    return value;
                }
            }
            return null;
        }

    }

    /**
     * 随机数函数
     * 共有四个重载形式:
     * #random()
     * #random(type)
     * #random(type,arg1)
     * #random(type,arg1,arg2)
     * <p>
     * #random()默认使用int类型随机数
     * <p>
     * type: 随机数类型.支持  int/long/double/boolean
     * <p>
     * #random(type,arg1)
     * arg1: 最大值(不含)
     * <p>
     * #random(type,arg1,arg2)
     * arg1: 最小值(含,必须大于0)
     * arg2: 最大值
     * <p>
     * 如果type为boolean类型,arg1,arg2不生效
     */
    public static class RandomFunction extends AbstractFunctionExpression {

        private RandomFunction(final List<Expression> functionExpressionList) {
            super(functionExpressionList);
        }

        private static RandomFunction from(final List<Expression> functionExpressionList) {
            return new RandomFunction(functionExpressionList);
        }

        @Override
        public Object doInterpret(final MockHandlerContext mockHandlerContext, final List<Object> childNodeValueList) {
            if (this.getNodeCount() > 0 && childNodeValueList.size() != this.getNodeCount()) {
                throw new DynamicMockException("Parameter incorrectly of `random` function");
            }

            final RandomType randomType = CollectionUtils.isEmpty(childNodeValueList) ? RandomType.INT : RandomType.valueOfCode(String.valueOf(childNodeValueList.get(0)));

            CheckUtils.isNotNull(randomType, DynamicMockException.supplier("The type of random is incorrect."));

            // 确保集合的第一个元素一定是type,方便接下来获取参数
            final List<Object> actualChildNodeValueList = new ArrayList<>();
            if (CollectionUtils.isEmpty(childNodeValueList)) {
                actualChildNodeValueList.add(RandomType.INT.code);
            } else {
                actualChildNodeValueList.addAll(childNodeValueList);
            }

            if (RandomType.INT.equals(randomType)) {

                return this.randomInt(actualChildNodeValueList);

            } else if (RandomType.LONG.equals(randomType)) {

                return this.randomLong(actualChildNodeValueList);

            } else if (RandomType.DOUBLE.equals(randomType)) {

                return this.randomDouble(actualChildNodeValueList);

            } else {

                return this.randomBoolean(actualChildNodeValueList);
            }
        }

        private Object randomBoolean(final List<Object> list) {
            return ThreadLocalRandom.current().nextBoolean();
        }

        private Object randomDouble(final List<Object> list) {

            final ThreadLocalRandom random = ThreadLocalRandom.current();

            final Object next;

            if (list.size() == 1) {

                next = random.nextDouble();

            } else if (list.size() == 2) {

                next = random.nextDouble(TypeUtils.toDouble(list.get(1)));

            } else {

                next = random.nextDouble(TypeUtils.toDouble(list.get(1)), TypeUtils.toDouble(list.get(2)));
            }

            return next;
        }

        private Object randomLong(final List<Object> list) {

            final ThreadLocalRandom random = ThreadLocalRandom.current();

            final Object next;

            if (list.size() == 1) {

                next = random.nextLong();

            } else if (list.size() == 2) {

                next = random.nextLong(TypeUtils.toLong(list.get(1)));

            } else {

                next = random.nextLong(TypeUtils.toLong(list.get(1)), TypeUtils.toLong(list.get(2)));
            }

            return next;
        }

        private Object randomInt(final List<Object> list) {

            final ThreadLocalRandom random = ThreadLocalRandom.current();

            final Object next;

            if (list.size() == 1) {

                next = random.nextInt();

            } else if (list.size() == 2) {

                next = random.nextInt(TypeUtils.toInteger(list.get(1)));

            } else {

                next = random.nextInt(TypeUtils.toInteger(list.get(1)), TypeUtils.toInteger(list.get(2)));
            }

            return next;
        }

        @Override
        protected String symbol() {
            return SYMBOL;
        }

    }


}