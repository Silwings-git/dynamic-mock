package top.silwings.core.handler;

import org.springframework.stereotype.Component;
import top.silwings.core.handler.tree.Node;
import top.silwings.core.handler.tree.dynamic.DynamicValueFactory;
import top.silwings.core.handler.tree.structure.ArrayNode;
import top.silwings.core.handler.tree.structure.ObjectNode;
import top.silwings.core.handler.tree.structure.StaticValueNode;
import top.silwings.core.utils.JsonUtils;

import java.util.Collection;
import java.util.Map;

/**
 * @ClassName JsonNodeReader
 * @Description
 * @Author Silwings
 * @Date 2022/10/29 18:54
 * @Since
 **/
@Component
public class JsonNodeParser {

    private final DynamicValueFactory dynamicValueFactory;

    public JsonNodeParser(final DynamicValueFactory dynamicValueFactory) {
        this.dynamicValueFactory = dynamicValueFactory;
    }

    public Node parse(final Object bean) {
        if (bean instanceof String) {
            return this.doParse(JsonUtils.toBean((String) bean));
        }
        return this.doParse(JsonUtils.toBean(JsonUtils.toJSONString(bean)));
    }

    /**
     * 将JSON对象解析成Node树
     *
     * @param json json实例
     * @return 节点树
     */
    private Node doParse(final Object json) {

        final Node node;

        if (json instanceof Map) {

            final Map<?, ?> parseObject = (Map<?,?>) json;
            final ObjectNode objectNode = new ObjectNode();
            // 每遍历到一个JSONObject,添加一层ObjectNode
            for (final Map.Entry<?, ?> entry : parseObject.entrySet()) {
                final Object value = entry.getValue();
                objectNode.put(this.buildNode(entry.getKey()), this.buildNode(value));
            }
            node = objectNode;

        } else {

            final Collection<?> parseList = (Collection<?>) json;
            // 每遍历到一个JSONObject,添加一层ArrayNode
            final ArrayNode arrayNode = new ArrayNode();
            for (final Object value : parseList) {
                arrayNode.add(this.buildNode(value));
            }
            node = arrayNode;
        }

        return node;
    }

    /**
     * 根据obj的具体类型构建不同的Node实例
     *
     * @param obj 节点中要存储的元素实例
     * @return Node实例
     */
    private Node buildNode(final Object obj) {
        if (this.isMap(obj)) {

            return this.doParse(obj);

        } else if (this.isString(obj) && this.isDynamic((String) obj)) {

            return this.dynamicValueFactory.buildDynamicValue((String) obj);

        } else {
            return StaticValueNode.from(obj);
        }
    }

    private boolean isDynamic(final String str) {
        // 仅以${开头,}结尾的视为动态表达式
        return str.startsWith("${") && str.endsWith("}");
    }

    private boolean isString(final Object obj) {
        return obj instanceof String;
    }

    private boolean isMap(final Object obj) {
        return obj instanceof Map;
    }

}