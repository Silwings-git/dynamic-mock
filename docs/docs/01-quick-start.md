# 快速入门 - 5 分钟上手 Dynamic-Mock

## 什么是 Dynamic-Mock？

Dynamic-Mock 是一款动态 HTTP 请求模拟服务，支持通过表达式和函数生成动态 Mock 数据。与传统的静态 Mock 工具不同，Dynamic-Mock 可以根据请求参数、请求头、Cookie 等信息动态生成响应数据。

## 前置要求

- Java 8+
- MySQL 8.0+
- Maven 3.6+

## 三步快速启动

### 步骤 1: 初始化数据库

执行数据库初始化脚本：

```sql
-- 执行 docs/sql/init.sql
mysql -u root -p < docs/sql/init.sql
```

### 步骤 2: 配置数据库连接

修改后端配置文件（`dynamic-mock-admin/src/main/resources/application.yml`）：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/dynamic_mock?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
    username: root
    password: your_password
```

### 步骤 3: 启动应用

```bash
# 方式一：Maven 启动
cd dynamic-mock-admin
mvn spring-boot:run

# 方式二：IDE 运行
# 运行主类：cn.silwings.admin.DynamicMockAdminApplication
```

启动成功后，访问：`http://localhost:8080/dynamic-mock-admin/`

## 创建你的第一个 Mock Handler

### 示例：返回动态用户数据

创建一个 Mock Handler，根据请求中的用户 ID 返回不同的用户信息：

```json
{
  "name": "用户信息查询",
  "httpMethods": ["GET"],
  "requestUri": "/api/user/{userId}",
  "label": "user-module",
  "delayTime": 0,
  "customizeSpace": {
    "defaultUserName": "默认用户"
  },
  "responses": [
    {
      "name": "成功响应",
      "support": [],
      "delayTime": 0,
      "response": {
        "status": 200,
        "headers": {
          "Content-Type": ["application/json;charset=UTF-8"]
        },
        "body": {
          "code": 200,
          "message": "success",
          "data": {
            "userId": "${#search('pathParameterMap.userId')}",
            "userName": "${#search('pathParameterMap.userId') == '1001' ? '张三' : #search('customizeSpace.defaultUserName')}",
            "timestamp": "${#now('yyyy-MM-dd HH:mm:ss')}",
            "requestId": "${#uuid()}"
          }
        }
      }
    }
  ],
  "tasks": []
}
```

### 测试你的 Mock Handler

使用 curl 测试：

```bash
# 测试用户 ID 为 1001 的请求
curl -X GET http://localhost:8080/api/user/1001

# 预期响应：
{
  "code": 200,
  "message": "success",
  "data": {
    "userId": "1001",
    "userName": "张三",
    "timestamp": "2024-01-15 10:30:45",
    "requestId": "f47ac10b-58cc-4372-a567-0e02b2c3d479"
  }
}

# 测试其他用户 ID
curl -X GET http://localhost:8080/api/user/9999

# 预期响应：
{
  "code": 200,
  "message": "success",
  "data": {
    "userId": "9999",
    "userName": "默认用户",
    "timestamp": "2024-01-15 10:31:20",
    "requestId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890"
  }
}
```

## 关键概念速览

### 1. 动态表达式

使用 `${...}` 包裹的表达式会在运行时动态计算：

```json
{
  "timestamp": "${#now()}",           // 当前时间戳
  "uuid": "${#uuid()}",               // 生成 UUID
  "userId": "${#search('pathParameterMap.userId')}"  // 从路径参数提取
}
```

### 2. Search 函数

`#search()` 是最重要的函数，用于从不同范围提取数据：

```javascript
// 从路径参数提取
${#search('pathParameterMap.userId')}

// 从请求体提取
${#search('jsonBody.user.name')}

// 从查询参数提取（注意参数是数组）
${#search('parameterMap.keyword[0]')}

// 从自定义空间提取
${#search('defaultUserName', 'CUSTOMIZESPACE')}
```

### 3. 条件响应

通过 `support` 数组定义响应触发的条件：

```json
"support": [
  "${#search('pathParameterMap.userId') == '1001'}"
]
```

## 下一步

- 📖 [核心概念](02-core-concepts.md) - 深入学习 DSL 结构
- 🔍 [Search 函数详解](03-search-function.md) - 掌握数据提取技巧
- 📚 [函数参考](05-function-reference.md) - 查看所有可用函数
- 🍳 [常用模式](06-cookbook.md) - 实战模式示例

## 常见问题

### Q: 表达式不生效怎么办？

A: 检查以下几点：
1. 表达式是否使用 `${...}` 包裹
2. 函数名是否正确（不区分大小写）
3. 字符串参数是否使用单引号 `'`
4. 查看应用日志是否有解析错误

### Q: 如何调试表达式？

A: 使用 `#print()` 函数打印中间结果：

```json
{
  "debug": "${#print(#search('jsonBody'))}",
  "data": "${#search('jsonBody.user.name')}"
}
```

### Q: RequestInfo 有哪些可用字段？

A: 查看 [RequestInfo 完整参考](04-request-info-reference.md)

---

**提示**: 在 Web 页面中，可以切换到 JSON 编辑模式直接粘贴上述配置，也可以用图形化界面可视化配置。
