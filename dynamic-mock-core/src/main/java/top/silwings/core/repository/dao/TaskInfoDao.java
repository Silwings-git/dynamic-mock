package top.silwings.core.repository.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName TaskInfoDao
 * @Description 任务持久化类
 * @Author Silwings
 * @Date 2022/11/13 22:43
 * @Since
 **/
@Getter
@Setter
public class TaskInfoDao {

    /**
     * 任务名称
     */
    private String name;

    /**
     * 条件表达式
     */
    private String support;

    public static void main(String[] args) {
        final JSONObject jsonObject = JSON.parseObject(getStr());

        System.out.println("jsonObject = " + jsonObject);
    }

    private static String getStr() {
        return "{\n" +
                "  \"id\": \"id\",\n" +
                "  \"name\": \"Demo Mock Handler\",\n" +
                "  \"httpMethods\": [\n" +
                "    \"GET\"\n" +
                "  ],\n" +
                "  \"requestUri\": \"/user/query\",\n" +
                "  \"label\": \"用户管理\",\n" +
                "  \"delayTime\": 0,\n" +
                "  \"customizeSpace\": {\n" +
                "    \"uid\": \"${#uuid()}\",\n" +
                "    \"name\": \"Misaka Mikoto\",\n" +
                "    \"age\": 18\n" +
                "  },\n" +
                "  \"responses\": [\n" +
                "    {\n" +
                "      \"name\": \"Result Body\",\n" +
                "      \"support\": [\n" +
                "        \"${#search(age) >= 18}\"\n" +
                "      ],\n" +
                "      \"delayTime\": 0,\n" +
                "      \"response\": {\n" +
                "        \"status\": 200,\n" +
                "        \"headers\": {\n" +
                "          \"Content-Type\": \"application/json;charset=UTF-8\",\n" +
                "          \"Date\": \"Wed, 09 Nov 2022 14:25:02 GMT\"\n" +
                "        },\n" +
                "        \"body\": {\n" +
                "          \"name\": \"${#search(customSpace.name)}\",\n" +
                "          \"age\": \"${#search(customSpace.age)}\"\n" +
                "        }\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"Result Msg\",\n" +
                "      \"support\": [\n" +
                "        \"${#search(age) < 18}\"\n" +
                "      ],\n" +
                "      \"delayTime\": 0,\n" +
                "      \"response\": {\n" +
                "        \"status\": 200,\n" +
                "        \"headers\": {\n" +
                "          \"Content-Type\": \"application/json;charset=UTF-8\",\n" +
                "          \"Date\": \"Wed, 09 Nov 2022 14:25:02 GMT\"\n" +
                "        },\n" +
                "        \"body\": \"${#search(customSpace.name)}\"\n" +
                "      }\n" +
                "    }\n" +
                "  ],\n" +
                "  \"tasks\": [\n" +
                "    {\n" +
                "      \"name\": \"Http Task\",\n" +
                "      \"support\": [\n" +
                "        \"${#isBlank(#search(customizeSpace.uid))}\"\n" +
                "      ],\n" +
                "      \"async\": false,\n" +
                "      \"cron\": \"* * * * * ?\",\n" +
                "      \"numberOfExecute\": \"1\",\n" +
                "      \"request\": {\n" +
                "        \"requestUrl\": \"http://localhost:8088/hello/word\",\n" +
                "        \"httpMethod\": \"POST\",\n" +
                "        \"headers\": {\n" +
                "          \"authToken\": \"8888888888\"\n" +
                "        },\n" +
                "        \"body\": {\n" +
                "          \"timestemp\": \"${#now()}\",\n" +
                "          \"name\": \"${#search(customSpace.name)}\",\n" +
                "          \"id\": \"${#uuid()}\"\n" +
                "        },\n" +
                "        \"uriVariables\": {\n" +
                "          \"${#search(requestBody.name)}\": \"${${#search(requestBody.userList)}}\",\n" +
                "          \"timestemp\": \"${#now()}\",\n" +
                "          \"index\": [\n" +
                "            \"1\",\n" +
                "            \"2\"\n" +
                "          ]\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}\n" +
                "\n";
    }


}