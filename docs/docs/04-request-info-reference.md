# RequestInfo 完整参考

> **说明**: 本文档是 `RequestContext.RequestInfo` 类的完整 API 参考。RequestInfo 是 Search 函数 `REQUESTINFO` 范围的数据来源。

---

## 快速参考表

| 字段 | 类型 | 提取示例 | 典型值 |
|------|------|----------|--------|
| **路径参数** | | | |
| `pathParameterMap` | Map<String,String> | `#search('pathParameterMap.id')` | `{"id":"123"}` |
| **查询参数** | | | |
| `parameterMap` | Map<String,List> | `#search('parameterMap.name[0]')` | `{"name":["张三"]}` |
| `queryString` | String | `#search('queryString')` | `"name=张三&age=18"` |
| **请求体** | | | |
| `jsonBody` | Map | `#search('jsonBody.user.name')` | `{"user":{"name":"张三"}}` |
| `textBody` | String | `#search('textBody')` | `"plain text"` |
| `formBody` | Map<String,List> | `#search('formBody.username[0]')` | `{"username":["admin"]}` |
| `body` | Object | `#search('body')` | 任意类型 |
| **请求头** | | | |
| `headerMap` | Map<String,String> | `#search('headerMap.Authorization')` | `{"Authorization":"Bearer xxx"}` |
| `headers` | List<Map> | `#search('headers[0].key')` | `[{"Authorization":"Bearer xxx"}]` |
| **Cookie** | | | |
| `cookieMap` | Map<String,String> | `#search('cookieMap.sessionId')` | `{"sessionId":"abc123"}` |
| `cookies` | List<Cookie> | `#search('cookies[0].value')` | `[{name:"sessionId"}]` |
| **请求信息** | | | |
| `method` | String | `#search('method')` | `"POST"` |
| `requestURI` | String | `#search('requestURI')` | `"/api/user/123"` |
| `requestURL` | String | `#search('requestURL')` | `"http://localhost:8080/api/user/123"` |
| `servletPath` | String | `#search('servletPath')` | `"/api"` |
| `contextPath` | String | `#search('contextPath')` | `"/dynamic-mock"` |
| `pathInfo` | String | `#search('pathInfo')` | `"/user/123"` |
| **其他** | | | |
| `authType` | String | `#search('authType')` | `"BASIC"` |
| `remoteUser` | String | `#search('remoteUser')` | `"admin"` |
| `pathTranslated` | String | `#search('pathTranslated')` | `null` |

---

## 详细字段说明

### 路径参数

#### pathParameterMap

```java
Map<String, String> pathParameterMap
```

**说明**: 路径模板参数映射。当 Mock Handler 的 `requestUri` 包含路径变量（如 `/api/user/{id}`）时，实际请求中的对应值会被提取到此 Map。

**来源**: `HttpServletRequest` 路径匹配结果

**提取示例**:
```javascript
// Mock Handler 配置: requestUri = "/api/user/{userId}"
// 实际请求：GET /api/user/123

${#search('pathParameterMap.userId')}
// 返回："123"

// 多个路径参数
// requestUri = "/api/org/{orgId}/user/{userId}"
${#search('pathParameterMap.orgId')}
${#search('pathParameterMap.userId')}
```

**典型值**:
```json
{
  "userId": "123",
  "orgId": "org-001"
}
```

**注意事项**:
- 仅在 `requestUri` 包含路径变量时才有值
- 路径变量需要在 Mock Handler 的 `requestUri` 中显式声明

---

### 查询参数

#### parameterMap

```java
Map<String, List<String>> parameterMap
```

**说明**: 查询参数和表单参数的统一映射。**值是数组**，因为同一个参数名可能出现多次。

**来源**: `HttpServletRequest.getParameterMap()`

**提取示例**:
```javascript
// GET /api/search?keyword=java&keyword=spring&page=1

${#search('parameterMap.keyword[0]')}  // "java"
${#search('parameterMap.keyword[1]')}  // "spring"
${#search('parameterMap.page[0]')}     // "1"

// POST form-data
// Content-Type: application/x-www-form-urlencoded
// username=admin&password=123

${#search('parameterMap.username[0]')}  // "admin"
${#search('parameterMap.password[0]')}  // "123"
```

**典型值**:
```json
{
  "keyword": ["java", "spring"],
  "page": ["1"],
  "size": ["10"]
}
```

**⚠️ 重要提示**:
1. **必须通过索引访问**: `parameterMap.name[0]` ✅，`parameterMap.name` ❌
2. 查询参数和表单参数都在此字段
3. 所有值都是字符串数组

---

#### queryString

```java
String queryString
```

**说明**: 原始的查询字符串（URL 中 `?` 后面的部分）

**来源**: `HttpServletRequest.getQueryString()`

**提取示例**:
```javascript
// GET /api/search?keyword=java&page=1

${#search('queryString')}
// 返回："keyword=java&page=1"
```

**典型值**: `"keyword=java&page=1"`

**使用场景**:
- 需要原样转发查询字符串时
- 需要手动解析查询参数时

---

### 请求体

#### jsonBody

```java
Map<String, Object> jsonBody
```

**说明**: JSON 格式请求体的解析结果。仅当 `Content-Type` 包含 `application/json` 时有效。

**来源**: 解析 `HttpServletRequest` 的输入流

**提取示例**:
```javascript
// POST /api/user
// Content-Type: application/json
// Body: {"user":{"name":"张三","age":18},"tags":["admin","vip"]}

${#search('jsonBody.user.name')}        // "张三"
${#search('jsonBody.user.age')}         // 18
${#search('jsonBody.tags[0]')}          // "admin"
${#search('jsonBody.tags[1]')}          // "vip"

// 嵌套提取
${#search('jsonBody.user.address.city')}
```

**典型值**:
```json
{
  "user": {
    "name": "张三",
    "age": 18
  },
  "tags": ["admin", "vip"]
}
```

**注意事项**:
1. 仅当 `Content-Type` 包含 `application/json` 时解析
2. 支持任意深度的嵌套提取
3. 数组通过索引访问：`jsonBody.items[0].id`

---

#### textBody

```java
String textBody
```

**说明**: 文本格式请求体。当 `Content-Type` 为 `text/plain` 或其他非 JSON/表单格式时使用。

**来源**: 读取 `HttpServletRequest` 的输入流

**提取示例**:
```javascript
// POST /api/echo
// Content-Type: text/plain
// Body: Hello World

${#search('textBody')}
// 返回："Hello World"
```

**典型值**: `"Hello World"`

---

#### formBody

```java
Map<String, List<String>> formBody
```

**说明**: 表单格式请求体。仅当 `Content-Type` 包含 `application/x-www-form-urlencoded` 时有效。

**来源**: 解析请求体字符串

**提取示例**:
```javascript
// POST /api/login
// Content-Type: application/x-www-form-urlencoded
// Body: username=admin&password=123

${#search('formBody.username[0]')}  // "admin"
${#search('formBody.password[0]')}  // "123"
```

**典型值**:
```json
{
  "username": ["admin"],
  "password": ["123"]
}
```

---

#### body

```java
Object body
```

**说明**: 原始请求体。实际类型取决于 `Content-Type`：
- JSON: `Map<String, Object>`
- 表单：`Map<String, List<String>>`
- 文本：`String`

**来源**: 根据 Content-Type 自动选择

**提取示例**:
```javascript
// 通常建议使用 jsonBody/formBody/textBody
// body 字段用于特殊场景

${#search('body')}
```

---

### 请求头

#### headerMap

```java
Map<String, String> headerMap
```

**说明**: 请求头键值映射。**推荐使用此字段**提取请求头。

**来源**: `HttpServletRequest.getHeaderNames()` + `getHeader()`

**提取示例**:
```javascript
// GET /api/user
// Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
// X-Request-ID: req-123
// Content-Type: application/json

${#search('headerMap.Authorization')}     // "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
${#search('headerMap.X-Request-ID')}      // "req-123"
${#search('headerMap.Content-Type')}      // "application/json"
```

**典型值**:
```json
{
  "Authorization": "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "X-Request-ID": "req-123",
  "Content-Type": "application/json"
}
```

**注意事项**:
- 请求头名称不区分大小写
- 如果有重复的请求头，返回最后一个值

---

#### headers

```java
List<Map<String, String>> headers
```

**说明**: 请求头列表。**支持重复的请求头**（同一个键有多个值）。

**来源**: 遍历 `HttpServletRequest.getHeaderNames()`

**提取示例**:
```javascript
// 提取第一个请求头
${#search('headers[0].key')}
${#search('headers[0].value')}

// 遍历所有请求头（需要配合其他函数）
```

**典型值**:
```json
[
  {"Authorization": "Bearer xxx"},
  {"Content-Type": "application/json"},
  {"X-Request-ID": "req-123"}
]
```

**使用场景**:
- 需要处理重复请求头时
- 需要遍历所有请求头时

---

### Cookie

#### cookieMap

```java
Map<String, String> cookieMap
```

**说明**: Cookie 键值映射。**推荐使用此字段**提取 Cookie。

**来源**: `HttpServletRequest.getCookies()` 转换

**提取示例**:
```javascript
// Cookie: sessionId=abc123; token=xyz789

${#search('cookieMap.sessionId')}  // "abc123"
${#search('cookieMap.token')}      // "xyz789"
```

**典型值**:
```json
{
  "sessionId": "abc123",
  "token": "xyz789"
}
```

---

#### cookies

```java
List<javax.servlet.http.Cookie> cookies
```

**说明**: Cookie 对象列表。每个 Cookie 对象包含完整的属性。

**来源**: `HttpServletRequest.getCookies()`

**提取示例**:
```javascript
// 提取第一个 Cookie 的值
${#search('cookies[0].value')}

// 提取 Cookie 的完整信息
${#search('cookies[0].name')}
${#search('cookies[0].domain')}
${#search('cookies[0].path')}
${#search('cookies[0].maxAge')}
```

**典型值**:
```json
[
  {
    "name": "sessionId",
    "value": "abc123",
    "domain": "example.com",
    "path": "/",
    "maxAge": 3600,
    "secure": true
  }
]
```

---

### 请求信息

#### method

```java
String method
```

**说明**: HTTP 请求方法

**来源**: `HttpServletRequest.getMethod()`

**提取示例**:
```javascript
${#search('method')}
// 返回："GET", "POST", "PUT", "DELETE" 等
```

**典型值**: `"POST"`

---

#### requestURI

```java
String requestURI
```

**说明**: 请求 URI（不包含协议和主机）

**来源**: `HttpServletRequest.getRequestURI()`

**提取示例**:
```javascript
// 请求：http://localhost:8080/api/user/123

${#search('requestURI')}
// 返回："/api/user/123"
```

**典型值**: `"/api/user/123"`

---

#### requestURL

```java
String requestURL
```

**说明**: 请求 URL（完整 URL，不包含查询参数）

**来源**: `HttpServletRequest.getRequestURL().toString()`

**提取示例**:
```javascript
// 请求：http://localhost:8080/api/user/123?name=张三

${#search('requestURL')}
// 返回："http://localhost:8080/api/user/123"
```

**典型值**: `"http://localhost:8080/api/user/123"`

---

#### servletPath

```java
String servletPath
```

**说明**: Servlet 映射路径

**来源**: `HttpServletRequest.getServletPath()`

**提取示例**:
```javascript
// 请求：http://localhost:8080/api/user/123
// Servlet 映射：/api/*

${#search('servletPath')}
// 返回："/api"
```

**典型值**: `"/api"`

---

#### contextPath

```java
String contextPath
```

**说明**: 应用上下文路径

**来源**: `HttpServletRequest.getContextPath()`

**提取示例**:
```javascript
// 部署在：http://localhost:8080/dynamic-mock/

${#search('contextPath')}
// 返回："/dynamic-mock"
```

**典型值**: `"/dynamic-mock"` 或 `""`（根部署）

---

#### pathInfo

```java
String pathInfo
```

**说明**: 路径信息（Servlet 路径后的部分）

**来源**: `HttpServletRequest.getPathInfo()`

**提取示例**:
```javascript
// 请求：http://localhost:8080/api/user/123
// Servlet 映射：/api/*

${#search('pathInfo')}
// 返回："/user/123"
```

**典型值**: `"/user/123"`

---

### 其他字段

#### authType

```java
String authType
```

**说明**: 认证类型

**来源**: `HttpServletRequest.getAuthType()`

**典型值**: `"BASIC"`, `"DIGEST"`, `"FORM"`, `"CLIENT_CERT"`, `null`

---

#### remoteUser

```java
String remoteUser
```

**说明**: 远程用户（已认证的用户名）

**来源**: `HttpServletRequest.getRemoteUser()`

**典型值**: `"admin"` 或 `null`

---

#### pathTranslated

```java
String pathTranslated
```

**说明**: 翻译后的文件系统路径（已废弃）

**来源**: `HttpServletRequest.getPathTranslated()`

**典型值**: `null`（大多数情况）

---

## 使用技巧

### 1. 组合提取

```javascript
// 从不同来源组合数据
{
  "userId": "${#search('pathParameterMap.userId')}",
  "userName": "${#search('jsonBody.userName')}",
  "token": "${#search('headerMap.Authorization')}",
  "sessionId": "${#search('cookieMap.sessionId')}"
}
```

### 2. 条件判断

```javascript
// 根据请求方法返回不同响应
"support": [
  "${#search('method') == 'POST'}"
]
```

### 3. 默认值

```javascript
// 提取分页参数，带默认值
"page": "${#search('parameterMap.page[0]', 'REQUESTINFO', 1)}",
"size": "${#search('parameterMap.size[0]', 'REQUESTINFO', 10)}"
```

### 4. 嵌套提取

```javascript
// 深度嵌套的 JSON
${#search('jsonBody.order.items[0].product.category.name')}
```

---

## 相关文档

- [Search 函数详解](03-search-function.md) - 如何使用 Search 提取数据
- [核心概念](02-core-concepts.md) - DSL 结构说明
- [函数参考](05-function-reference.md) - 其他可用函数
