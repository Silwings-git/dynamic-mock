package cn.silwings.admin.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.silwings.admin.auth.annotation.PermissionLimit;
import cn.silwings.admin.common.Result;
import cn.silwings.admin.web.vo.result.DslDocumentResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName DocumentController
 * @Description DSL文档接口
 * @Author Silwings
 * @Date 2025/12/17
 **/
@RestController
@RequestMapping("/dynamic-mock/doc")
@Api(value = "DSL文档")
public class DocumentController {

    @PostMapping("/dsl")
    @PermissionLimit(limit = false)
    @ApiOperation(value = "获取DSL文档")
    public Result<DslDocumentResult> getDslDocument() {
        DslDocumentResult result = new DslDocumentResult();
        result.setSections(buildDslDocSections());
        return Result.ok(result);
    }

    private List<DslDocumentResult.Section> buildDslDocSections() {
        List<DslDocumentResult.Section> sections = new ArrayList<>();

        // 1. Mock Handler 整体结构
        sections.add(buildMockHandlerSection());

        // 2. 动态表达式
        sections.add(buildDynamicExpressionSection());

        // 3. 逻辑运算符
        sections.add(buildLogicOperatorSection());

        // 4. JSON路径语法
        sections.add(buildJsonPathSection());

        return sections;
    }

    private DslDocumentResult.Section buildMockHandlerSection() {
        DslDocumentResult.Section section = new DslDocumentResult.Section();
        section.setTitle("Mock Handler 结构");
        section.setDescription("Mock Handler包含4个部分：基础信息、自定义参数空间(customizeSpace)、响应集(responses)、任务集(tasks)");

        List<DslDocumentResult.SubSection> subSections = new ArrayList<>();

        // 基础信息
        DslDocumentResult.SubSection basicInfo = new DslDocumentResult.SubSection();
        basicInfo.setTitle("基础信息");
        basicInfo.setContent(Arrays.asList(
                "1. httpMethods：适用的请求方式集，规定了当前处理器可以处理的请求方式",
                "2. requestUri：适用的请求路径，与httpMethods组合使用",
                "3. delayTime：延迟执行时间（单位：毫秒），设置匹配请求后延迟多久开始执行",
                "4. name/label：处理器名称和标签，仅用于识别"
        ));
        subSections.add(basicInfo);

        // 自定义参数空间
        DslDocumentResult.SubSection customizeSpace = new DslDocumentResult.SubSection();
        customizeSpace.setTitle("自定义参数空间 (customizeSpace)");
        customizeSpace.setContent(Arrays.asList(
                "允许预设置全局变量，供响应集和任务集使用",
                "key:value结构，key值不允许重复",
                "示例：{ \"productCode\": \"PC001\", \"baseUser\": { \"name\": \"张三\" } }"
        ));
        subSections.add(customizeSpace);

        // 响应集
        DslDocumentResult.SubSection responses = new DslDocumentResult.SubSection();
        responses.setTitle("响应集 (responses)");
        responses.setContent(Arrays.asList(
                "声明最终返回给客户端的响应数据",
                "可配置多个响应，通过条件动态判断使用哪个响应",
                "字段：name(名称)、support(适用条件集)、delayTime(延迟时间)、response(响应信息)",
                "条件集(support)中所有表达式为true时才会使用该响应",
                "response包含：status(HTTP响应码)、headers(响应头)、body(响应体)"
        ));
        subSections.add(responses);

        // 任务集
        DslDocumentResult.SubSection tasks = new DslDocumentResult.SubSection();
        tasks.setTitle("任务集 (tasks)");
        tasks.setContent(Arrays.asList(
                "配置多个发起HTTP请求的任务",
                "分为同步任务(async=false)和异步任务(async=true)",
                "同步任务：在响应前执行，忽略cron和执行次数",
                "异步任务：定时任务，使用cron表达式控制执行周期和次数",
                "字段：name、support、async、cron、numberOfExecute、request",
                "request包含：requestUrl、httpMethod、headers、body、uriVariables"
        ));
        subSections.add(tasks);

        section.setSubSections(subSections);
        return section;
    }

    private DslDocumentResult.Section buildDynamicExpressionSection() {
        DslDocumentResult.Section section = new DslDocumentResult.Section();
        section.setTitle("动态表达式");
        section.setDescription("动态表达式是实现动态逻辑的核心，以 ${ 开头，} 结尾");

        List<DslDocumentResult.SubSection> subSections = new ArrayList<>();

        // 基本语法
        DslDocumentResult.SubSection basic = new DslDocumentResult.SubSection();
        basic.setTitle("基本语法");
        basic.setContent(Arrays.asList(
                "格式：${ 表达式内容 }",
                "分为函数表达式和逻辑表达式两类",
                "函数表达式：#函数名(参数列表)，如 #search('code')",
                "逻辑表达式：非函数表达式的动态表达式，如 ${1+1==2}"
        ));
        basic.setExamples(Arrays.asList(
                "${#search('code','customizeSpace')} - 从自定义空间搜索code",
                "${#page(1,10,100,'{\"name\":\"张三\"}',false)} - 生成分页数据",
                "${#equals(#search('status'),'active')} - 判断status是否为active"
        ));
        subSections.add(basic);

        // 字符串规则
        DslDocumentResult.SubSection stringRule = new DslDocumentResult.SubSection();
        stringRule.setTitle("字符串规则");
        stringRule.setContent(Arrays.asList(
                "⚠️ 重要：所有字符串必须使用单引号 ' 包裹",
                "函数名不需要加引号，但参数中的字符串需要加单引号",
                "示例：#search('code', 'customizeSpace')"
        ));
        subSections.add(stringRule);

        // 转义字符
        DslDocumentResult.SubSection escape = new DslDocumentResult.SubSection();
        escape.setTitle("转义字符");
        escape.setContent(Arrays.asList(
                "动态表达式中使用 ^ 作为转义符（而不是 \\）",
                "嵌套字符串时需要转义单引号",
                "示例：#page(1,10,100,'{\"name\":\"#search(^'code^')\"}',false)"
        ));
        subSections.add(escape);

        section.setSubSections(subSections);
        return section;
    }

    private DslDocumentResult.Section buildLogicOperatorSection() {
        DslDocumentResult.Section section = new DslDocumentResult.Section();
        section.setTitle("运算符");
        section.setDescription("动态表达式支持逻辑运算和数值运算");

        List<DslDocumentResult.SubSection> subSections = new ArrayList<>();

        // 逻辑运算符
        DslDocumentResult.SubSection logic = new DslDocumentResult.SubSection();
        logic.setTitle("逻辑运算符");
        logic.setContent(Arrays.asList(
                "== : 等于",
                "!= : 不等于",
                "> : 大于",
                ">= : 大于等于",
                "< : 小于",
                "<= : 小于等于",
                "&& : 逻辑与",
                "|| : 逻辑或"
        ));
        logic.setExamples(Arrays.asList(
                "${#search('age') > 18} - 年龄大于18",
                "${#search('status') == 'active' && #search('level') >= 5} - 多条件判断"
        ));
        subSections.add(logic);

        // 数值运算符
        DslDocumentResult.SubSection arithmetic = new DslDocumentResult.SubSection();
        arithmetic.setTitle("数值运算符");
        arithmetic.setContent(Arrays.asList(
                "+ : 加法（非数值类型时为字符串拼接）",
                "- : 减法",
                "* : 乘法",
                "/ : 除法",
                "% : 取余"
        ));
        arithmetic.setExamples(Arrays.asList(
                "${1 + 1} => 2",
                "${#search('price') * 0.8} - 打8折",
                "${'Hello' + ' ' + 'World'} => Hello World"
        ));
        subSections.add(arithmetic);

        section.setSubSections(subSections);
        return section;
    }

    private DslDocumentResult.Section buildJsonPathSection() {
        DslDocumentResult.Section section = new DslDocumentResult.Section();
        section.setTitle("JSON 路径");
        section.setDescription("使用JSON路径从请求信息或自定义空间中提取数据");

        List<DslDocumentResult.SubSection> subSections = new ArrayList<>();

        DslDocumentResult.SubSection syntax = new DslDocumentResult.SubSection();
        syntax.setTitle("路径语法");
        syntax.setContent(Arrays.asList(
                "$ : 根对象",
                "$.field : 访问字段",
                "$.user.name : 嵌套字段访问",
                "$.items[0] : 访问数组元素",
                "$.items[0].price : 数组元素的字段"
        ));
        syntax.setExamples(Arrays.asList(
                "#search('$.userId') - 获取userId",
                "#search('$.user.name') - 获取用户名",
                "#search('$.items[0].price') - 获取第一个商品的价格",
                "#search('$.headers.Content-Type') - 获取请求头"
        ));
        subSections.add(syntax);

        DslDocumentResult.SubSection searchScope = new DslDocumentResult.SubSection();
        searchScope.setTitle("搜索范围");
        searchScope.setContent(Arrays.asList(
                "requestInfo : 请求信息（默认）包含headers、body、cookies等",
                "customizeSpace : 自定义参数空间",
                "localCache : 本地缓存（通过SaveCache函数添加）"
        ));
        searchScope.setExamples(Arrays.asList(
                "#search('code') - 默认从requestInfo搜索",
                "#search('code', 'customizeSpace') - 从自定义空间搜索",
                "#search('code', 'localCache', 'default') - 从缓存搜索，找不到返回default"
        ));
        subSections.add(searchScope);

        section.setSubSections(subSections);
        return section;
    }
}
