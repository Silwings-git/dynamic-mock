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

        sections.add(buildQuickStartSection());
        sections.add(buildMockHandlerSection());
        sections.add(buildRequestInfoSection());
        sections.add(buildSearchFunctionSection());
        sections.add(buildDynamicExpressionSection());
        sections.add(buildLogicOperatorSection());
        sections.add(buildJsonPathDetailedSection());

        return sections;
    }

    private DslDocumentResult.Section buildQuickStartSection() {
        DslDocumentResult.Section section = new DslDocumentResult.Section();
        section.setTitle("快速开始");
        section.setDescription("3分钟快速上手 DSL 配置");

        List<DslDocumentResult.SubSection> subSections = new ArrayList<>();

        DslDocumentResult.SubSection example = new DslDocumentResult.SubSection();
        example.setTitle("示例：简单响应");
        example.setContent(Arrays.asList(
                "根据请求参数动态返回用户信息",
                "支持条件判断和数据提取"
        ));
        example.setExamples(Arrays.asList(
                "httpMethods: [\"POST\"]",
                "requestUri: \"/api/user\"",
                "responses[0].support: [\"#search('type') == 'vip'\"]",
                "responses[0].response.body: {\"code\":200,\"data\":{\"name\":\"#search('name')\",\"level\":#search('level')}}"
        ));
        subSections.add(example);

        DslDocumentResult.SubSection steps = new DslDocumentResult.SubSection();
        steps.setTitle("配置步骤");
        steps.setContent(Arrays.asList(
                "1. 选择请求方式和路径匹配规则",
                "2. 配置响应条件（支持多条件组合）",
                "3. 编写响应体（支持动态表达式）",
                "4. 启用处理器立即生效"
        ));
        subSections.add(steps);

        section.setSubSections(subSections);
        return section;
    }

    private DslDocumentResult.Section buildMockHandlerSection() {
        DslDocumentResult.Section section = new DslDocumentResult.Section();
        section.setTitle("Mock Handler 结构");
        section.setDescription("Mock Handler包含4个部分：基础信息、自定义参数空间(customizeSpace)、响应集(responses)、任务集(tasks)");

        List<DslDocumentResult.SubSection> subSections = new ArrayList<>();

        DslDocumentResult.SubSection basicInfo = new DslDocumentResult.SubSection();
        basicInfo.setTitle("基础信息");
        basicInfo.setContent(Arrays.asList(
                "httpMethods：适用的请求方式集（POST/GET/PUT/DELETE）",
                "requestUri：适用的请求路径（支持通配符）",
                "delayTime：延迟执行时间（单位：毫秒）",
                "name/label：处理器名称和标签"
        ));
        basicInfo.setExamples(Arrays.asList(
                "httpMethods: [\"POST\", \"PUT\"]",
                "requestUri: \"/api/**\"",
                "delayTime: 100"
        ));
        subSections.add(basicInfo);

        DslDocumentResult.SubSection customizeSpace = new DslDocumentResult.SubSection();
        customizeSpace.setTitle("自定义参数空间");
        customizeSpace.setContent(Arrays.asList(
                "预设置全局变量供响应集和任务集使用",
                "key:value结构，key值不允许重复"
        ));
        customizeSpace.setExamples(Arrays.asList(
                "{ \"productCode\": \"PC001\", \"baseUser\": { \"name\": \"张三\" } }",
                "#search('code', 'customizeSpace') - 访问自定义空间"
        ));
        subSections.add(customizeSpace);

        DslDocumentResult.SubSection responses = new DslDocumentResult.SubSection();
        responses.setTitle("响应集");
        responses.setContent(Arrays.asList(
                "声明最终返回给客户端的响应数据",
                "可配置多个响应，通过条件动态判断使用哪个响应"
        ));
        responses.setExamples(Arrays.asList(
                "responses[0].support: [\"#search('status') == 'active'\"]",
                "responses[0].response: {\"status\":200,\"body\":{...}}"
        ));
        subSections.add(responses);

        DslDocumentResult.SubSection tasks = new DslDocumentResult.SubSection();
        tasks.setTitle("任务集");
        tasks.setContent(Arrays.asList(
                "配置多个发起HTTP请求的任务",
                "同步任务(async=false)在响应前执行，异步任务(async=true)为定时任务"
        ));
        tasks.setExamples(Arrays.asList(
                "tasks[0].async: false",
                "tasks[0].request: {\"requestUrl\":\"http://example.com\"}",
                "tasks[1].async: true, tasks[1].cron: \"0 0/5 * * * ?\""
        ));
        subSections.add(tasks);

        section.setSubSections(subSections);
        return section;
    }

    private DslDocumentResult.Section buildRequestInfoSection() {
        DslDocumentResult.Section section = new DslDocumentResult.Section();
        section.setTitle("RequestInfo 完整参考");
        section.setDescription("RequestInfo 包含请求的所有信息，可通过 #search() 函数访问");

        List<DslDocumentResult.SubSection> subSections = new ArrayList<>();

        DslDocumentResult.SubSection basicInfo = new DslDocumentResult.SubSection();
        basicInfo.setTitle("基础信息");
        basicInfo.setContent(Arrays.asList(
                "method：HTTP方法",
                "requestURI：请求URI",
                "requestURL：请求URL",
                "servletPath：Servlet路径",
                "contextPath：上下文路径",
                "pathInfo：路径信息",
                "pathTranslated：翻译后的路径",
                "queryString：查询字符串",
                "remoteUser：远程用户",
                "authType：授权类型"
        ));
        basicInfo.setExamples(Arrays.asList(
                "#search('method') - 获取HTTP方法",
                "#search('requestURI') - 获取请求URI",
                "#search('requestURL') - 获取请求URL",
                "#search('servletPath') - 获取Servlet路径",
                "#search('contextPath') - 获取上下文路径",
                "#search('pathInfo') - 获取路径信息",
                "#search('pathTranslated') - 获取翻译后的路径",
                "#search('queryString') - 获取查询字符串",
                "#search('remoteUser') - 获取远程用户",
                "#search('authType') - 获取授权类型"
        ));
        subSections.add(basicInfo);

        DslDocumentResult.SubSection headers = new DslDocumentResult.SubSection();
        headers.setTitle("请求头");
        headers.setContent(Arrays.asList(
                "headers：请求头列表（数组格式）",
                "headerMap：请求头键值映射（Map格式）"
        ));
        headers.setExamples(Arrays.asList(
                "#search('headers') - 获取所有请求头列表",
                "#search('headers.Content-Type') - 获取Content-Type请求头",
                "#search('headers[0].Content-Type') - 通过索引访问请求头",
                "#search('headerMap') - 获取请求头Map",
                "#search('headerMap.Content-Type') - 通过Map方式获取请求头"
        ));
        subSections.add(headers);

        DslDocumentResult.SubSection cookies = new DslDocumentResult.SubSection();
        cookies.setTitle("Cookie");
        cookies.setContent(Arrays.asList(
                "cookies：Cookie列表（Cookie对象数组）",
                "cookieMap：Cookie键值映射（Map格式）"
        ));
        cookies.setExamples(Arrays.asList(
                "#search('cookies') - 获取所有Cookie列表",
                "#search('cookies[0].name') - 获取第一个Cookie的名称",
                "#search('cookies[0].value') - 获取第一个Cookie的值",
                "#search('cookieMap') - 获取Cookie Map",
                "#search('cookieMap.sessionId') - 通过Map方式获取Cookie值"
        ));
        subSections.add(cookies);

        DslDocumentResult.SubSection body = new DslDocumentResult.SubSection();
        body.setTitle("请求体");
        body.setContent(Arrays.asList(
                "body：请求体（原始对象，JSON格式时为Map）",
                "jsonBody：JSON格式请求体（Map格式）",
                "textBody：文本格式请求体（String格式）",
                "formBody：form-urlencoded格式请求体（Map<String, List<String>>格式）",
                "parameterMap：form-data/查询参数（Map<String, List<String>>格式）"
        ));
        body.setExamples(Arrays.asList(
                "#search('body') - 获取原始请求体",
                "#search('body.userId') - 从JSON请求体获取字段",
                "#search('jsonBody') - 获取JSON格式请求体",
                "#search('jsonBody.username') - 从JSON请求体获取字段",
                "#search('textBody') - 获取文本格式请求体",
                "#search('formBody') - 获取form-urlencoded格式请求体",
                "#search('formBody.username[0]') - 从form-body获取字段值",
                "#search('parameterMap') - 获取form-data/查询参数",
                "#search('parameterMap.username[0]') - 从参数Map获取字段值"
        ));
        subSections.add(body);

        DslDocumentResult.SubSection pathParams = new DslDocumentResult.SubSection();
        pathParams.setTitle("路径参数");
        pathParams.setContent(Arrays.asList(
                "pathParameterMap：路径参数键值映射"
        ));
        pathParams.setExamples(Arrays.asList(
                "#search('pathParameterMap') - 获取所有路径参数",
                "#search('pathParameterMap.id') - 获取路径参数id的值",
                "#search('pathParameterMap.userId') - 获取路径参数userId的值"
        ));
        subSections.add(pathParams);

        section.setSubSections(subSections);
        return section;
    }

    private DslDocumentResult.Section buildSearchFunctionSection() {
        DslDocumentResult.Section section = new DslDocumentResult.Section();
        section.setTitle("#search 函数深度指南");
        section.setDescription("#search() 是最核心的函数，用于从各种数据源中提取数据");

        List<DslDocumentResult.SubSection> subSections = new ArrayList<>();

        DslDocumentResult.SubSection signature = new DslDocumentResult.SubSection();
        signature.setTitle("函数签名");
        signature.setContent(Arrays.asList(
                "#search(key, scope, defaultValue)",
                "key：要搜索的键或JSON路径",
                "scope：搜索范围（可选，默认requestInfo）",
                "defaultValue：默认值（可选）"
        ));
        signature.setExamples(Arrays.asList(
                "#search('code') - 从requestInfo搜索code",
                "#search('$.user.name') - 使用JSON路径",
                "#search('code', 'customizeSpace') - 从自定义空间搜索"
        ));
        subSections.add(signature);

        DslDocumentResult.SubSection scopes = new DslDocumentResult.SubSection();
        scopes.setTitle("搜索范围");
        scopes.setContent(Arrays.asList(
                "requestInfo：请求信息（默认）",
                "customizeSpace：自定义参数空间",
                "localCache：本地缓存"
        ));
        scopes.setExamples(Arrays.asList(
                "#search('code') - 等同于 #search('code', 'requestInfo')",
                "#search('code', 'customizeSpace') - 从自定义空间",
                "#search('code', 'localCache', 'default') - 从缓存，找不到返回default"
        ));
        subSections.add(scopes);

        DslDocumentResult.SubSection jsonPath = new DslDocumentResult.SubSection();
        jsonPath.setTitle("JSON路径语法");
        jsonPath.setContent(Arrays.asList(
                "$：根对象",
                "$.field：访问字段",
                "$.items[0]：访问数组元素",
                "$.items[*]：访问所有数组元素",
                "详见【JSON 路径】章节获取完整的 JSONPath 语法参考"
        ));
        jsonPath.setExamples(Arrays.asList(
                "#search('$.userId') - 获取userId",
                "#search('$.user.name') - 获取嵌套字段",
                "#search('$.items[*].price') - 获取所有价格"
        ));
        subSections.add(jsonPath);

        DslDocumentResult.SubSection useCases = new DslDocumentResult.SubSection();
        useCases.setTitle("典型用例");
        useCases.setContent(Arrays.asList(
                "从请求体提取字段用于条件判断",
                "从请求头获取认证信息",
                "从查询参数获取分页信息"
        ));
        useCases.setExamples(Arrays.asList(
                "${#search('status') == 'active'} - 条件判断",
                "#search('headers.Authorization') - 获取Token",
                "#search('query.page', 1) - 获取页码，默认1"
        ));
        subSections.add(useCases);

        section.setSubSections(subSections);
        return section;
    }

    private DslDocumentResult.Section buildDynamicExpressionSection() {
        DslDocumentResult.Section section = new DslDocumentResult.Section();
        section.setTitle("动态表达式");
        section.setDescription("动态表达式是实现动态逻辑的核心，以 ${ 开头，} 结尾");

        List<DslDocumentResult.SubSection> subSections = new ArrayList<>();

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

        DslDocumentResult.SubSection stringRule = new DslDocumentResult.SubSection();
        stringRule.setTitle("字符串规则");
        stringRule.setContent(Arrays.asList(
                "所有字符串必须使用单引号 ' 包裹",
                "函数名不需要加引号，但参数中的字符串需要加单引号"
        ));
        stringRule.setExamples(Arrays.asList(
                "#search('code', 'customizeSpace')",
                "${#search('status') == 'active'}"
        ));
        subSections.add(stringRule);

        DslDocumentResult.SubSection escape = new DslDocumentResult.SubSection();
        escape.setTitle("转义字符");
        escape.setContent(Arrays.asList(
                "动态表达式中使用 ^ 作为转义符（而不是 \\）",
                "嵌套字符串时需要转义单引号"
        ));
        escape.setExamples(Arrays.asList(
                "#page(1,10,100,'{\"name\":\"#search(^'code^')\"}',false)",
                "#search(^'key^') - 搜索key字符串"
        ));
        subSections.add(escape);

        section.setSubSections(subSections);
        return section;
    }

    private DslDocumentResult.Section buildBuiltInFunctionsSection() {
        DslDocumentResult.Section section = new DslDocumentResult.Section();
        section.setTitle("内置函数目录");
        section.setDescription("Dynamic-Mock 提供25+内置函数，支持数据生成、转换、判断等操作");

        List<DslDocumentResult.SubSection> subSections = new ArrayList<>();

        DslDocumentResult.SubSection search = new DslDocumentResult.SubSection();
        search.setTitle("#search(key, scope, defaultValue)");
        search.setContent(Arrays.asList(
                "从指定范围搜索数据",
                "支持JSON路径和普通键名"
        ));
        search.setExamples(Arrays.asList(
                "#search('code')",
                "#search('$.user.name')"
        ));
        subSections.add(search);

        DslDocumentResult.SubSection page = new DslDocumentResult.SubSection();
        page.setTitle("#page(pageNo, pageSize, total, data, needTotal)");
        page.setContent(Arrays.asList(
                "生成分页数据",
                "返回包含分页信息的JSON对象"
        ));
        page.setExamples(Arrays.asList(
                "#page(1,10,100,{\"list\":[...]},true)",
                "#page(1,10,100,'{\"name\":\"张三\"}',false)"
        ));
        subSections.add(page);

        DslDocumentResult.SubSection equals = new DslDocumentResult.SubSection();
        equals.setTitle("#equals(obj1, obj2)");
        equals.setContent(Arrays.asList(
                "判断两个对象是否相等"
        ));
        equals.setExamples(Arrays.asList(
                "#equals(#search('status'),'active')",
                "${#equals(#search('type'),1)}"
        ));
        subSections.add(equals);

        DslDocumentResult.SubSection notEquals = new DslDocumentResult.SubSection();
        notEquals.setTitle("#notEquals(obj1, obj2)");
        notEquals.setContent(Arrays.asList(
                "判断两个对象是否不相等"
        ));
        notEquals.setExamples(Arrays.asList(
                "#notEquals(#search('status'),'inactive')"
        ));
        subSections.add(notEquals);

        DslDocumentResult.SubSection isEmpty = new DslDocumentResult.SubSection();
        isEmpty.setTitle("#isEmpty(obj)");
        isEmpty.setContent(Arrays.asList(
                "判断对象是否为空"
        ));
        isEmpty.setExamples(Arrays.asList(
                "#isEmpty(#search('code'))",
                "${#isEmpty(#search('items'))}"
        ));
        subSections.add(isEmpty);

        DslDocumentResult.SubSection isNotEmpty = new DslDocumentResult.SubSection();
        isNotEmpty.setTitle("#isNotEmpty(obj)");
        isNotEmpty.setContent(Arrays.asList(
                "判断对象是否不为空"
        ));
        isNotEmpty.setExamples(Arrays.asList(
                "#isNotEmpty(#search('items'))"
        ));
        subSections.add(isNotEmpty);

        DslDocumentResult.SubSection length = new DslDocumentResult.SubSection();
        length.setTitle("#length(obj)");
        length.setContent(Arrays.asList(
                "获取对象长度（字符串长度或数组大小）"
        ));
        length.setExamples(Arrays.asList(
                "#length(#search('name'))",
                "#length(#search('items'))"
        ));
        subSections.add(length);

        DslDocumentResult.SubSection contains = new DslDocumentResult.SubSection();
        contains.setTitle("#contains(obj, target)");
        contains.setContent(Arrays.asList(
                "判断对象是否包含目标字符串或元素"
        ));
        contains.setExamples(Arrays.asList(
                "#contains(#search('name'),'张')",
                "#contains(#search('items'),1)"
        ));
        subSections.add(contains);

        DslDocumentResult.SubSection notContains = new DslDocumentResult.SubSection();
        notContains.setTitle("#notContains(obj, target)");
        notContains.setContent(Arrays.asList(
                "判断对象是否不包含目标字符串或元素"
        ));
        notContains.setExamples(Arrays.asList(
                "#notContains(#search('name'),'李')"
        ));
        subSections.add(notContains);

        DslDocumentResult.SubSection startsWith = new DslDocumentResult.SubSection();
        startsWith.setTitle("#startsWith(obj, prefix)");
        startsWith.setContent(Arrays.asList(
                "判断字符串是否以指定前缀开头"
        ));
        startsWith.setExamples(Arrays.asList(
                "#startsWith(#search('code'),'ABC')"
        ));
        subSections.add(startsWith);

        DslDocumentResult.SubSection endsWith = new DslDocumentResult.SubSection();
        endsWith.setTitle("#endsWith(obj, suffix)");
        endsWith.setContent(Arrays.asList(
                "判断字符串是否以指定后缀结尾"
        ));
        endsWith.setExamples(Arrays.asList(
                "#endsWith(#search('code'),'.json')"
        ));
        subSections.add(endsWith);

        DslDocumentResult.SubSection toLowerCase = new DslDocumentResult.SubSection();
        toLowerCase.setTitle("#toLowerCase(obj)");
        toLowerCase.setContent(Arrays.asList(
                "将字符串转换为小写"
        ));
        toLowerCase.setExamples(Arrays.asList(
                "#toLowerCase(#search('name'))"
        ));
        subSections.add(toLowerCase);

        DslDocumentResult.SubSection toUpperCase = new DslDocumentResult.SubSection();
        toUpperCase.setTitle("#toUpperCase(obj)");
        toUpperCase.setContent(Arrays.asList(
                "将字符串转换为大写"
        ));
        toUpperCase.setExamples(Arrays.asList(
                "#toUpperCase(#search('name'))"
        ));
        subSections.add(toUpperCase);

        DslDocumentResult.SubSection trim = new DslDocumentResult.SubSection();
        trim.setTitle("#trim(obj)");
        trim.setContent(Arrays.asList(
                "去除字符串首尾空格"
        ));
        trim.setExamples(Arrays.asList(
                "#trim(#search('name'))"
        ));
        subSections.add(trim);

        DslDocumentResult.SubSection substring = new DslDocumentResult.SubSection();
        substring.setTitle("#substring(obj, start, end)");
        substring.setContent(Arrays.asList(
                "截取字符串（左闭右开区间）"
        ));
        substring.setExamples(Arrays.asList(
                "#substring(#search('code'),0,3)"
        ));
        subSections.add(substring);

        DslDocumentResult.SubSection replace = new DslDocumentResult.SubSection();
        replace.setTitle("#replace(obj, target, replacement)");
        replace.setContent(Arrays.asList(
                "替换字符串中的目标字符串"
        ));
        replace.setExamples(Arrays.asList(
                "#replace(#search('name'),'张','李')"
        ));
        subSections.add(replace);

        DslDocumentResult.SubSection replaceAll = new DslDocumentResult.SubSection();
        replaceAll.setTitle("#replaceAll(obj, regex, replacement)");
        replaceAll.setContent(Arrays.asList(
                "使用正则表达式替换字符串"
        ));
        replaceAll.setExamples(Arrays.asList(
                "#replaceAll(#search('code'),'\\\\d','X')"
        ));
        subSections.add(replaceAll);

        DslDocumentResult.SubSection split = new DslDocumentResult.SubSection();
        split.setTitle("#split(obj, delimiter)");
        split.setContent(Arrays.asList(
                "按分隔符分割字符串为数组"
        ));
        split.setExamples(Arrays.asList(
                "#split(#search('code'),',')"
        ));
        subSections.add(split);

        DslDocumentResult.SubSection join = new DslDocumentResult.SubSection();
        join.setTitle("#join(array, delimiter)");
        join.setContent(Arrays.asList(
                "将数组元素用分隔符连接成字符串"
        ));
        join.setExamples(Arrays.asList(
                "#join(#split(#search('code'),','),'-')"
        ));
        subSections.add(join);

        DslDocumentResult.SubSection parseInt = new DslDocumentResult.SubSection();
        parseInt.setTitle("#parseInt(obj)");
        parseInt.setContent(Arrays.asList(
                "将字符串转换为整数"
        ));
        parseInt.setExamples(Arrays.asList(
                "#parseInt(#search('page'))"
        ));
        subSections.add(parseInt);

        DslDocumentResult.SubSection parseDouble = new DslDocumentResult.SubSection();
        parseDouble.setTitle("#parseDouble(obj)");
        parseDouble.setContent(Arrays.asList(
                "将字符串转换为浮点数"
        ));
        parseDouble.setExamples(Arrays.asList(
                "#parseDouble(#search('price'))"
        ));
        subSections.add(parseDouble);

        DslDocumentResult.SubSection str = new DslDocumentResult.SubSection();
        str.setTitle("#str(obj)");
        str.setContent(Arrays.asList(
                "将对象转换为字符串"
        ));
        str.setExamples(Arrays.asList(
                "#str(#search('code'))",
                "#str(123)"
        ));
        subSections.add(str);

        DslDocumentResult.SubSection now = new DslDocumentResult.SubSection();
        now.setTitle("#now(pattern)");
        now.setContent(Arrays.asList(
                "获取当前时间戳，可指定格式"
        ));
        now.setExamples(Arrays.asList(
                "#now()",
                "#now('yyyy-MM-dd HH:mm:ss')"
        ));
        subSections.add(now);

        DslDocumentResult.SubSection dateAdd = new DslDocumentResult.SubSection();
        dateAdd.setTitle("#dateAdd(date, field, amount)");
        dateAdd.setContent(Arrays.asList(
                "对日期进行加法运算"
        ));
        dateAdd.setExamples(Arrays.asList(
                "#dateAdd(#now(), 'day', 7)",
                "#dateAdd(#search('date'), 'hour', -2)"
        ));
        subSections.add(dateAdd);

        DslDocumentResult.SubSection dateDiff = new DslDocumentResult.SubSection();
        dateDiff.setTitle("#dateDiff(date1, date2, field)");
        dateDiff.setContent(Arrays.asList(
                "计算两个日期之间的差值"
        ));
        dateDiff.setExamples(Arrays.asList(
                "#dateDiff(#now(), #search('startTime'), 'day')"
        ));
        subSections.add(dateDiff);

        DslDocumentResult.SubSection randomInt = new DslDocumentResult.SubSection();
        randomInt.setTitle("#randomInt(min, max)");
        randomInt.setContent(Arrays.asList(
                "生成指定范围内的随机整数"
        ));
        randomInt.setExamples(Arrays.asList(
                "#randomInt(1, 100)",
                "#randomInt(0, #length(#search('items'))-1)"
        ));
        subSections.add(randomInt);

        DslDocumentResult.SubSection randomStr = new DslDocumentResult.SubSection();
        randomStr.setTitle("#randomStr(length, type)");
        randomStr.setContent(Arrays.asList(
                "生成随机字符串",
                "type: 0-数字, 1-字母, 2-数字字母, 3-中文"
        ));
        randomStr.setExamples(Arrays.asList(
                "#randomStr(6, 0)",
                "#randomStr(10, 2)"
        ));
        subSections.add(randomStr);

        DslDocumentResult.SubSection saveCache = new DslDocumentResult.SubSection();
        saveCache.setTitle("#saveCache(key, value, expireTime)");
        saveCache.setContent(Arrays.asList(
                "保存数据到本地缓存",
                "expireTime: 过期时间（毫秒），0表示永不过期"
        ));
        saveCache.setExamples(Arrays.asList(
                "#saveCache('token', #search('token'), 3600000)",
                "#saveCache('count', 1, 0)"
        ));
        subSections.add(saveCache);

        DslDocumentResult.SubSection getCache = new DslDocumentResult.SubSection();
        getCache.setTitle("#getCache(key, defaultValue)");
        getCache.setContent(Arrays.asList(
                "从本地缓存获取数据"
        ));
        getCache.setExamples(Arrays.asList(
                "#getCache('token')",
                "#getCache('count', 0)"
        ));
        subSections.add(getCache);

        DslDocumentResult.SubSection clearCache = new DslDocumentResult.SubSection();
        clearCache.setTitle("#clearCache(key)");
        clearCache.setContent(Arrays.asList(
                "清除本地缓存中的指定键"
        ));
        clearCache.setExamples(Arrays.asList(
                "#clearCache('token')"
        ));
        subSections.add(clearCache);

        DslDocumentResult.SubSection http = new DslDocumentResult.SubSection();
        http.setTitle("#http(url, method, headers, body)");
        http.setContent(Arrays.asList(
                "发起HTTP请求并返回响应",
                "返回包含status、headers、body的JSON对象"
        ));
        http.setExamples(Arrays.asList(
                "#http('http://example.com', 'GET', {}, null)",
                "#http('http://example.com', 'POST', {'Content-Type':'application/json'}, '{\"key\":\"value\"}')"
        ));
        subSections.add(http);

        DslDocumentResult.SubSection eval = new DslDocumentResult.SubSection();
        eval.setTitle("#eval(expr)");
        eval.setContent(Arrays.asList(
                "执行动态表达式字符串"
        ));
        eval.setExamples(Arrays.asList(
                "#eval('#search(\"code\")')",
                "#eval('${1+1}')"
        ));
        subSections.add(eval);

        DslDocumentResult.SubSection ifElse = new DslDocumentResult.SubSection();
        ifElse.setTitle("#ifElse(condition, trueValue, falseValue)");
        ifElse.setContent(Arrays.asList(
                "三元表达式函数版本"
        ));
        ifElse.setExamples(Arrays.asList(
                "#ifElse(#search('age') > 18, 'adult', 'minor')",
                "#ifElse(#isEmpty(#search('name')), 'unknown', #search('name'))"
        ));
        subSections.add(ifElse);

        DslDocumentResult.SubSection switchFunc = new DslDocumentResult.SubSection();
        switchFunc.setTitle("#switch(value, case1, result1, case2, result2, default)");
        switchFunc.setContent(Arrays.asList(
                "switch表达式函数版本"
        ));
        switchFunc.setExamples(Arrays.asList(
                "#switch(#search('status'), 1, 'active', 2, 'inactive', 'unknown')",
                "#switch(#search('type'), 'vip', 'premium', 'normal', 'standard', 'guest')"
        ));
        subSections.add(switchFunc);

        section.setSubSections(subSections);
        return section;
    }

    private DslDocumentResult.Section buildLogicOperatorSection() {
        DslDocumentResult.Section section = new DslDocumentResult.Section();
        section.setTitle("运算符");
        section.setDescription("动态表达式支持逻辑运算和数值运算");

        List<DslDocumentResult.SubSection> subSections = new ArrayList<>();

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
                "${#search('status') == 'active' && #search('level') >= 5} - 多条件判断",
                "${#search('type') == 'vip' || #search('level') >= 10} - VIP或高级用户"
        ));
        subSections.add(logic);

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
                "${'Hello' + ' ' + 'World'} => Hello World",
                "${#search('total') / 100} - 转换为元",
                "${#search('index') % 2 == 0} - 判断偶数"
        ));
        subSections.add(arithmetic);

        section.setSubSections(subSections);
        return section;
    }

    private DslDocumentResult.Section buildJsonPathDetailedSection() {
        DslDocumentResult.Section section = new DslDocumentResult.Section();
        section.setTitle("JSON 路径");
        section.setDescription("JSONPath 是一种从 JSON 文档中提取信息的查询语言，类似于 XPath 用于 XML。Dynamic-Mock 支持完整的 JSONPath 语法，用于在 #search() 函数中提取嵌套数据。");

        List<DslDocumentResult.SubSection> subSections = new ArrayList<>();

        // 1. What is JSONPath?
        DslDocumentResult.SubSection whatIsJsonPath = new DslDocumentResult.SubSection();
        whatIsJsonPath.setTitle("什么是 JSONPath？");
        whatIsJsonPath.setContent(Arrays.asList(
                "JSONPath 是一种从 JSON 文档中提取数据的查询语言",
                "语法简洁，易于学习和使用",
                "支持字段访问、数组索引、过滤器和通配符"
        ));
        whatIsJsonPath.setExamples(Arrays.asList(
                "$.store.book[0].title - 从嵌套结构提取数据",
                "$.users[?(@.age > 18)] - 使用过滤器筛选数据",
                "$..author - 递归 descent 查找所有 author 字段"
        ));
        subSections.add(whatIsJsonPath);

        // 2. Basic Syntax
        DslDocumentResult.SubSection basicSyntax = new DslDocumentResult.SubSection();
        basicSyntax.setTitle("基本语法");
        basicSyntax.setContent(Arrays.asList(
                "$ : 根对象（所有 JSONPath 表达式都以 $ 开头）",
                ".field : 点号表示法，访问对象的字段",
                "[index] : 中括号表示法，访问数组元素或对象字段"
        ));
        basicSyntax.setExamples(Arrays.asList(
                "$ - 整个 JSON 文档",
                "$.name - 访问根对象的 name 字段",
                "$['name'] - 等价于 $.name，使用中括号表示法",
                "$.user.name - 访问嵌套对象的字段"
        ));
        subSections.add(basicSyntax);

        // 3. Field Access
        DslDocumentResult.SubSection fieldAccess = new DslDocumentResult.SubSection();
        fieldAccess.setTitle("字段访问");
        fieldAccess.setContent(Arrays.asList(
                "简单字段访问：使用点号或中括号",
                "嵌套字段访问：连续使用点号"
        ));
        fieldAccess.setExamples(Arrays.asList(
                "#search('$.name') - 获取 name 字段",
                "#search('$.user.name') - 获取嵌套的 name 字段",
                "#search('$.user.address.city') - 访问多层嵌套",
                "#search('$.headers[\"Content-Type\"]') - 访问带特殊字符的字段名"
        ));
        subSections.add(fieldAccess);

        // 4. Array Access
        DslDocumentResult.SubSection arrayAccess = new DslDocumentResult.SubSection();
        arrayAccess.setTitle("数组访问");
        arrayAccess.setContent(Arrays.asList(
                "通过索引访问数组元素（从 0 开始）",
                "负索引从数组末尾开始（-1 表示最后一个元素）",
                "切片语法获取数组子集"
        ));
        arrayAccess.setExamples(Arrays.asList(
                "#search('$.items[0]') - 获取第一个元素",
                "#search('$.items[2]') - 获取第三个元素",
                "#search('$.items[-1]') - 获取最后一个元素",
                "#search('$.items[-2]') - 获取倒数第二个元素",
                "#search('$.items[0:2]') - 获取前两个元素（切片）",
                "#search('$.items[1:]') - 从第二个元素开始获取所有"
        ));
        subSections.add(arrayAccess);

        // 5. Wildcards
        DslDocumentResult.SubSection wildcards = new DslDocumentResult.SubSection();
        wildcards.setTitle("通配符");
        wildcards.setContent(Arrays.asList(
                "* : 匹配所有字段或数组元素",
                ".. : 递归 descent，查找所有匹配的字段（无论嵌套层级）"
        ));
        wildcards.setExamples(Arrays.asList(
                "#search('$.items[*]') - 获取所有数组元素",
                "#search('$.items[*].price') - 获取所有元素的 price 字段",
                "#search('$..price') - 递归查找所有 price 字段",
                "#search('$.store.*') - 获取 store 对象的所有字段"
        ));
        subSections.add(wildcards);

        // 6. Filters
        DslDocumentResult.SubSection filters = new DslDocumentResult.SubSection();
        filters.setTitle("过滤器");
        filters.setContent(Arrays.asList(
                "?() : 过滤器表达式，用于筛选数组元素",
                "@ : 当前元素的引用",
                "支持比较运算符：==, !=, >, >=, <, <=",
                "支持逻辑运算符：&&, ||"
        ));
        filters.setExamples(Arrays.asList(
                "#search('$.items[?(@.price > 100)]') - 价格大于 100 的商品",
                "#search('$.items[?(@.name == '手机')]') - 名称为 '手机' 的商品",
                "#search('$.items[?(@.price >= 50 && @.price <= 100)]') - 价格在 50-100 之间",
                "#search('$.users[?(@.age > 18 && @.status == 'active')]') - 成年且活跃的用户",
                "#search('$.items[?(@.category != 'electronics')]') - 非电子产品"
        ));
        subSections.add(filters);

        // 7. Common Patterns
        DslDocumentResult.SubSection commonPatterns = new DslDocumentResult.SubSection();
        commonPatterns.setTitle("常用模式");
        commonPatterns.setContent(Arrays.asList(
                "从请求体提取嵌套字段",
                "获取数组长度并用于条件判断",
                "提取数组中的特定字段组成新数组",
                "递归查找嵌套对象中的字段"
        ));
        commonPatterns.setExamples(Arrays.asList(
                "#search('$.user.profile.email') - 提取嵌套的邮箱字段",
                "#length(#search('$.items')) - 获取数组长度",
                "#search('$.items[*].name') - 提取所有商品名称",
                "#search('$..id') - 递归查找所有 id 字段（用于复杂嵌套）",
                "#search('$.headers.Authorization[0]') - 获取请求头数组的第一个值"
        ));
        subSections.add(commonPatterns);

        // 8. Pitfalls
        DslDocumentResult.SubSection pitfalls = new DslDocumentResult.SubSection();
        pitfalls.setTitle("常见陷阱");
        pitfalls.setContent(Arrays.asList(
                "字段名包含特殊字符时必须使用中括号表示法",
                "数组索引从 0 开始，不是 1",
                "递归 descent .. 可能匹配多个结果，注意返回类型",
                "过滤器中的 @ 必须放在括号内",
                "空值处理：建议使用默认值参数避免 NullPointerException"
        ));
        pitfalls.setExamples(Arrays.asList(
                "#search('$.headers[\"Content-Type\"]') - 字段名包含连字符，必须用中括号",
                "#search('$.items[0]') - 错误：使用 1 会获取第二个元素",
                "#search('$.items[?(@.price > 100)]', []) - 过滤器无匹配时返回空数组",
                "#search('$.optionalField', 'default') - 使用默认值避免空指针"
        ));
        subSections.add(pitfalls);

        section.setSubSections(subSections);
        return section;
    }


}
