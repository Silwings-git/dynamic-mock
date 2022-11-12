package top.silwings.core.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import top.silwings.core.exceptions.NodeParseException;
import top.silwings.core.handler.tree.Node;
import top.silwings.core.handler.tree.dynamic.DynamicValueFactory;
import top.silwings.core.handler.tree.structure.ArrayNode;
import top.silwings.core.handler.tree.structure.ObjectNode;
import top.silwings.core.handler.tree.structure.StaticValueNode;

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

    public Node parse(final String jsonSource) {
        return this.doParse(JSON.parseObject(jsonSource));
    }

    /**
     * 将JSON对象解析成Node树
     *
     * @param json json实例
     * @return 节点树
     */
    private Node doParse(final JSON json) {

        final Node node;

        if (json instanceof JSONObject) {

            final JSONObject parseObject = (JSONObject) json;
            final ObjectNode objectNode = new ObjectNode();
            // 每遍历到一个JSONObject,添加一层ObjectNode
            for (final Map.Entry<String, Object> entry : parseObject.entrySet()) {
                final Object value = entry.getValue();
                objectNode.put(this.buildNode(entry.getKey()), this.buildNode(value));
            }
            node = objectNode;

        } else if (json instanceof JSONArray) {

            final JSONArray parseArray = (JSONArray) json;
            // 每遍历到一个JSONObject,添加一层ArrayNode
            final ArrayNode arrayNode = new ArrayNode();
            for (final Object value : parseArray) {
                arrayNode.add(this.buildNode(value));
            }
            node = arrayNode;

        } else {
            throw new NodeParseException();
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
        if (this.isJson(obj)) {

            return this.doParse((JSON) obj);

        } else if (this.isString(obj) && this.isDynamic((String) obj)) {

            final String str = (String) obj;
            // 在构建Expression时需要去掉表达式的首尾标志符,即 '${' 和 '}'
            return this.dynamicValueFactory.buildDynamicValue(str.substring(2, str.length() - 1));

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

    private boolean isJson(final Object obj) {
        return obj instanceof JSON;
    }

}