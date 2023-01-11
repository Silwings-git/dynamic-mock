package top.silwings.core.interpreter.json;

import org.springframework.stereotype.Component;
import top.silwings.core.interpreter.ExpressionTreeNode;
import top.silwings.core.interpreter.dynamic_expression.DynamicExpressionFactory;
import top.silwings.core.utils.JsonUtils;

import java.util.Collection;
import java.util.Map;

/**
 * @ClassName JsonExpressionParser
 * @Description Json表达式解析器
 * @Author Silwings
 * @Date 2022/10/29 18:54
 * @Since
 **/
@Component
public class JsonTreeParser {

    private final DynamicExpressionFactory dynamicExpressionFactory;

    public JsonTreeParser(final DynamicExpressionFactory dynamicExpressionFactory) {
        this.dynamicExpressionFactory = dynamicExpressionFactory;
    }

    public ExpressionTreeNode parse(final Object bean) {

        if (bean instanceof String) {
            return this.doParse(JsonUtils.toBean((String) bean));
        }
        return this.doParse(JsonUtils.toBean(JsonUtils.toJSONString(bean)));
    }

    /**
     * 将JSON对象解析成表达式树
     *
     * @param json json实例
     * @return 节点树
     */
    private ExpressionTreeNode doParse(final Object json) {

        final ExpressionTreeNode jsonNode;

        if (json instanceof Map) {

            final Map<?, ?> parseObject = (Map<?, ?>) json;
            final ObjectNode objectNode = new ObjectNode();
            // 每遍历到一个JSONObject,添加一层ObjectExpression
            for (final Map.Entry<?, ?> entry : parseObject.entrySet()) {
                final Object value = entry.getValue();
                objectNode.put(this.buildNode(entry.getKey()), this.buildNode(value));
            }
            jsonNode = objectNode;

        } else if (json instanceof Collection) {

            final Collection<?> parseList = (Collection<?>) json;
            // 每遍历到一个JSONObject,添加一层ArrayExpression
            final ArrayNode arrayNode = new ArrayNode();
            for (final Object value : parseList) {
                arrayNode.add(this.buildNode(value));
            }
            jsonNode = arrayNode;

        } else {

            jsonNode = this.buildNode(json);
        }

        return jsonNode;
    }

    /**
     * 根据obj的具体类型构建不同的表达式实例
     *
     * @param obj 节点中要存储的元素实例
     * @return 节点实例
     */
    private ExpressionTreeNode buildNode(final Object obj) {
        if (this.isMap(obj) || this.isCollection(obj)) {

            return this.doParse(obj);

        } else if (this.isString(obj) && DynamicExpressionFactory.isDynamic((String) obj)) {

            return this.dynamicExpressionFactory.buildDynamicValue((String) obj);

        } else {
            return StaticValueNode.from(obj);
        }
    }

    private boolean isCollection(final Object obj) {
        return obj instanceof Collection;
    }

    private boolean isString(final Object obj) {
        return obj instanceof String;
    }

    private boolean isMap(final Object obj) {
        return obj instanceof Map;
    }

}