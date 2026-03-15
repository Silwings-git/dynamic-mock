# Search 函数详解 - 数据提取核心

> **重要性**: Search 函数是 Dynamic-Mock **最核心、最常用**的函数。它让你可以从请求信息、自定义空间、本地缓存中提取任意数据，实现动态逻辑。

## 快速决策树

```
我需要从哪里提取数据？
│
├─ HTTP 请求信息（请求头、参数、Body 等）
│  └─ 使用 REQUESTINFO 范围（默认）
│     ├─ 路径参数 → #search('pathParameterMap.xxx')
│     ├─ 查询参数 → #search('parameterMap.xxx[0]')
│     ├─ JSON Body → #search('jsonBody.xxx.yyy')
│     ├─ 请求头 → #search('headerMap.Authorization')
│     └─ Cookie → #search('cookieMap.sessionId')
│
├─ Mock Handler 自定义配置
│  └─ 使用 CUSTOMIZESPACE 范围
│     └─ #search('key', 'CUSTOMIZESPACE')
│
└─ 运行时缓存的数据
   └─ 使用 LOCALCACHE 范围
      └─ #search('key', 'LOCALCACHE')
```

---

## 五种重载形式

### 1. 基础搜索（默认从 RequestInfo）

```javascript
#search(jsonPath)
```

**示例**:
```javascript
// 从请求信息提取 userId
${#search('pathParameterMap.userId')}

// 等价于
${#search('pathParameterMap.userId', 'REQUESTINFO')}
```

---

### 2. 指定搜索范围

```javascript
#search(jsonPath, searchScope)
```

**searchScope 支持的值**:
| 值 | 说明 | 数据来源 |
|----|------|----------|
| `REQUESTINFO` | 请求信息（默认） | HttpServletRequest |
| `CUSTOMIZESPACE` | 自定义参数空间 | Mock Handler 配置 |
| `LOCALCACHE` | 本地缓存 | 运行时缓存 |

**示例**:
```javascript
// 从自定义空间提取
${#search('productCode', 'CUSTOMIZESPACE')}

// 从本地缓存提取
${#search('cachedData', 'LOCALCACHE')}
```

---

### 3. 带默认值的搜索

```javascript
#search(jsonPath, searchScope, defaultValue)
```

**示例**:
```javascript
// 如果找不到 userLevel，返回 'NORMAL'
${#search('headerMap.userLevel', 'REQUESTINFO', 'NORMAL')}

// 从自定义空间提取，找不到返回默认值
${#search('config.timeout', 'CUSTOMIZESPACE', 3000)}
```

---

### 4. 从 JSON 对象搜索

```javascript
#search(jsonPath, jsonObject)
```

**示例**:
```javascript
// 从内联 JSON 提取
${#search('user.name', '{"user":{"name":"张三","age":18}}')}

// 从 Search 函数返回的 JSON 继续提取
${#search('data.list[0].id', #search('jsonBody', 'REQUESTINFO'))}
```

---

### 5. 从 JSON 对象搜索（带默认值）

```javascript
#search(jsonPath, jsonObject, defaultValue)
```

**示例**:
```javascript
// 从 JSON 提取，找不到返回默认值
${#search('user.email', '{"user":{"name":"张三"}}', 'no-email@example.com')}
```

---

## RequestInfo 范围详解

### 什么是 RequestInfo？

当 HTTP 请求到达 Dynamic-Mock 时，系统会将 `HttpServletRequest` 解析为 `RequestInfo` 对象。Search 函数的默认搜索范围就是这个对象。

### RequestInfo 完整字段

```json
{
  "authType": "",              // 认证类型
  "contextPath": "",           // 应用上下文路径
  "cookies": [],               // Cookie 数组
  "cookieMap": {},             // Cookie 键值映射
  "headers": [],               // 请求头数组（支持重复键）
  "headerMap": {},             // 请求头键值映射
  "method": "",                // HTTP 方法
  "pathInfo": "",              // 路径信息
  "pathTranslated": "",        // 翻译后的路径
  "queryString": "",           // 查询字符串（?后的部分）
  "remoteUser": "",            // 远程用户
  "requestURI": "",            // 请求 URI
  "requestURL": "",            // 请求 URL
  "servletPath": "",           // Servlet 路径
  "body": {},                  // 请求体（原始）
  "jsonBody": {},              // JSON 格式请求体（Map）
  "textBody": "",              // 文本格式请求体
  "formBody": {},              // form-urlencoded 格式
  "parameterMap": {},          // 表单/查询参数（值为数组）
  "pathParameterMap": {}       // 路径参数
}
```

### 字段分组说明

#### 1️⃣ 路径相关

| 字段 | 类型 | 示例值 | 使用场景 |
|------|------|--------|----------|
| `pathParameterMap` | Map | `{"id": "123"}` | RESTful 路径参数 |
| `requestURI` | String | `/api/user/123` | 完整请求路径 |
| `servletPath` | String | `/api` | Servlet 映射路径 |
| `contextPath` | String | `/dynamic-mock` | 应用上下文 |

**提取示例**:
```javascript
// 提取路径参数 {id}
${#search('pathParameterMap.id')}

// 提取完整 URI
${#search('requestURI')}
```

---

#### 2️⃣ 查询参数相关

| 字段 | 类型 | 示例值 | 说明 |
|------|------|--------|------|
| `parameterMap` | Map<String, List> | `{"name": ["张三"], "age": ["18"]}` | 查询参数 + 表单参数 |
| `queryString` | String | `name=张三&age=18` | 原始查询字符串 |

**⚠️ 重要**: `parameterMap` 的值是**数组**，需要通过索引访问！

**提取示例**:
```javascript
// ✅ 正确：访问第一个值
${#search('parameterMap.name[0]')}

// ❌ 错误：直接访问会得到数组对象
${#search('parameterMap.name')}

// 获取查询参数数量
${#search('parameterMap.name').length}
```

---

#### 3️⃣ 请求体相关

| 字段 | 类型 | 示例值 | 适用场景 |
|------|------|--------|----------|
| `jsonBody` | Map | `{"user": {"name": "张三"}}` | Content-Type: application/json |
| `textBody` | String | `plain text` | Content-Type: text/plain |
| `formBody` | Map<String, List> | `{"username": ["admin"]}` | Content-Type: application/x-www-form-urlencoded |
| `body` | Object | 任意 | 原始请求体 |

**提取示例**:
```javascript
// 从 JSON Body 提取嵌套字段
${#search('jsonBody.user.name')}
${#search('jsonBody.order.items[0].productId')}

// 从表单 Body 提取（注意是数组）
${#search('formBody.username[0]')}

// 获取原始文本
${#search('textBody')}
```

---

#### 4️⃣ 请求头相关

| 字段 | 类型 | 示例值 | 说明 |
|------|------|--------|------|
| `headerMap` | Map | `{"Content-Type": "application/json"}` | 推荐使用的字段 |
| `headers` | List<Map> | `[{"Content-Type": "application/json"}]` | 支持重复键 |

**提取示例**:
```javascript
// 提取请求头
${#search('headerMap.Authorization')}
${#search('headerMap.X-Request-ID')}

// 提取 Content-Type
${#search('headerMap.Content-Type')}
```

---

#### 5️⃣ Cookie 相关

| 字段 | 类型 | 示例值 | 说明 |
|------|------|--------|------|
| `cookieMap` | Map | `{"sessionId": "abc123"}` | 推荐使用的字段 |
| `cookies` | List<Cookie> | `[{name: "sessionId", value: "abc123"}]` | Cookie 对象数组 |

**提取示例**:
```javascript
// 提取 Cookie
${#search('cookieMap.sessionId')}
${#search('cookieMap.token')}
```

---

#### 6️⃣ 其他字段

| 字段 | 类型 | 示例值 |
|------|------|--------|
| `method` | String | `"GET"`, `"POST"` |
| `authType` | String | `"BASIC"`, `"DIGEST"` |
| `remoteUser` | String | `"admin"` |
| `requestURL` | String | `"http://localhost:8080/api/user/123"` |

---

## CUSTOMIZESPACE 范围详解

### 什么是自定义参数空间？

自定义参数空间是在创建 Mock Handler 时预定义的变量集合，在 `customizeSpace` 字段中配置。

### 配置示例

```json
{
  "customizeSpace": {
    "productCode": "PC001",
    "skuList": ["SC001", "SC002", "SC003"],
    "config": {
      "timeout": 3000,
      "retry": 3
    },
    "users": [
      {"id": 1, "name": "张三"},
      {"id": 2, "name": "李四"}
    ]
  }
}
```

### 提取示例

```javascript
// 提取简单值
${#search('productCode', 'CUSTOMIZESPACE')}

// 提取嵌套对象
${#search('config.timeout', 'CUSTOMIZESPACE')}

// 提取数组元素
${#search('skuList[0]', 'CUSTOMIZESPACE')}
${#search('users[0].name', 'CUSTOMIZESPACE')}

// 带默认值
${#search('notExist', 'CUSTOMIZESPACE', '默认值')}
```

---

## LOCALCACHE 范围详解

### 什么是本地缓存？

本地缓存是请求级别的临时存储空间，通过 `#saveCache()` 函数写入，通过 `#search()` 读取。

### 使用场景

- 保存中间计算结果
- 在多个表达式间共享数据
- 复杂逻辑的状态存储

### 示例

```javascript
// 保存数据到缓存
${#saveCache('userId', #search('pathParameterMap.userId'))}

// 从缓存读取
${#search('userId', 'LOCALCACHE')}

// 保存复杂对象
${#saveCache('userInfo', #search('jsonBody.user'))}

// 从缓存提取嵌套字段
${#search('userInfo.name', 'LOCALCACHE')}
```

---

## 实战示例

### 场景 1: 从不同来源组合数据

```json
{
  "response": {
    "body": {
      "userId": "${#search('pathParameterMap.userId')}",
      "userName": "${#search('jsonBody.userName')}",
      "token": "${#search('headerMap.Authorization')}",
      "sessionId": "${#search('cookieMap.sessionId')}",
      "productCode": "${#search('productCode', 'CUSTOMIZESPACE')}",
      "timestamp": "${#now()}"
    }
  }
}
```

---

### 场景 2: 条件逻辑

```json
{
  "responses": [
    {
      "name": "VIP 用户",
      "support": [
        "${#search('headerMap.userLevel') == 'VIP'}"
      ],
      "response": {
        "body": {
          "discount": 0.8,
          "message": "尊敬的 VIP 用户"
        }
      }
    },
    {
      "name": "普通用户",
      "support": [],
      "response": {
        "body": {
          "discount": 1.0,
          "message": "您好"
        }
      }
    }
  ]
}
```

---

### 场景 3: 分页数据

```json
{
  "response": {
    "body": {
      "page": "${#search('parameterMap.page[0]', 'REQUESTINFO', 1)}",
      "size": "${#search('parameterMap.size[0]', 'REQUESTINFO', 10)}",
      "data": "${#page(
        #search('parameterMap.page[0]', 'REQUESTINFO', 1),
        #search('parameterMap.size[0]', 'REQUESTINFO', 10),
        100,
        '{\"name\":\"用户${#random('int',100)}\"}'
      )}"
    }
  }
}
```

---

### 场景 4: 嵌套 JSON 提取

```javascript
// 假设请求体为:
// {
//   "order": {
//     "items": [
//       {"productId": "P001", "quantity": 2},
//       {"productId": "P002", "quantity": 1}
//     ]
//   }
// }

// 提取第一个商品的 ID
${#search('jsonBody.order.items[0].productId')}

// 提取商品总数
${#search('jsonBody.order.items').length}
```

---

## 常见错误与解决方案

### ❌ 错误 1: 忘记 parameterMap 的值是数组

```javascript
// ❌ 错误
${#search('parameterMap.userId')}
// 得到：["123"] (数组对象)

// ✅ 正确
${#search('parameterMap.userId[0]')}
// 得到："123" (字符串)
```

---

### ❌ 错误 2: 嵌套表达式引号未转义

```javascript
// ❌ 错误：引号冲突
${#search('data.#search('key')', json)}

// ✅ 正确：内层引号转义
${#search('data.#search(^'key^')', json)}
```

---

### ❌ 错误 3: JSON 路径错误

```javascript
// 假设 jsonBody = {"user": {"name": "张三"}}

// ❌ 错误：路径不存在
${#search('jsonBody.userName')}

// ✅ 正确：完整路径
${#search('jsonBody.user.name')}
```

---

### ❌ 错误 4: 范围拼写错误

```javascript
// ❌ 错误
${#search('key', 'REQUEST_INFO')}
${#search('key', 'CustomizeSpace')}

// ✅ 正确
${#search('key', 'REQUESTINFO')}
${#search('key', 'CUSTOMIZESPACE')}
```

---

## 性能提示

1. **优先使用 headerMap/cookieMap**：比遍历 headers/cookies 数组更快
2. **避免重复搜索**: 将常用值保存到 LOCALCACHE
3. **使用默认值避免空指针**: `${#search('key', 'scope', 'default')}`

```javascript
// ✅ 推荐：缓存重复使用的值
${#saveCache('userId', #search('pathParameterMap.userId'))}
${#search('userId', 'LOCALCACHE')}
${#search('userId', 'LOCALCACHE')}
```

---

## 下一步

- 📚 [RequestInfo 完整参考](04-request-info-reference.md) - 所有 20+ 字段的详细说明
- 🍳 [常用模式](06-cookbook.md) - 更多实战示例
- 📖 [函数参考](05-function-reference.md) - 其他 30+ 函数
