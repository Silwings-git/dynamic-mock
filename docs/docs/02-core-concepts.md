# 核心概念 - DSL 结构详解

## Mock Handler 整体结构

Mock Handler 是 Dynamic-Mock 的核心配置单元，每个 Mock Handler 代表一个可以响应特定 URI 请求的处理器。Mock Handler 使用 JSON 格式定义，系统会预解析为执行树，当请求匹配时快速执行。

### 完整结构

```json
{
  "name": "处理器名称",
  "httpMethods": ["GET", "POST"],
  "requestUri": "/api/user/{id}",
  "label": "模块标签",
  "delayTime": 0,
  "customizeSpace": {},
  "responses": [],
  "tasks": []
}
```

### 四大组成部分

```
┌─────────────────────────────────────────┐
│           Mock Handler                  │
├─────────────────────────────────────────┤
│  1. 基础信息 (Basic Info)               │
│     - name, httpMethods, requestUri     │
│     - label, delayTime                  │
├─────────────────────────────────────────┤
│  2. 自定义参数空间 (Customize Space)    │
│     - 预定义的全局变量                   │
├─────────────────────────────────────────┤
│  3. 响应集 (Responses)                  │
│     - 条件响应配置                       │
│     - 返回给客户端的数据                 │
├─────────────────────────────────────────┤
│  4. 任务集 (Tasks)                      │
│     - 同步/异步 HTTP 任务                 │
│     - 回调、Webhook 等场景               │
└─────────────────────────────────────────┘
```

---

## 1. 基础信息 (Basic Info)

### 字段说明

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `name` | String | ✅ | 处理器名称，仅用于标识 |
| `httpMethods` | Array | ✅ | 支持的 HTTP 方法列表 |
| `requestUri` | String | ✅ | 请求路径，支持 Ant 风格通配符 |
| `label` | String | ❌ | 模块标签，用于分组管理 |
| `delayTime` | Number | ❌ | 延迟执行时间（毫秒） |

### 示例

```json
{
  "name": "创建订单接口",
  "httpMethods": ["POST"],
  "requestUri": "/api/order/create",
  "label": "order-module",
  "delayTime": 500
}
```

### 路径匹配规则

`requestUri` 支持 Ant 风格通配符：

| 通配符 | 说明 | 示例 |
|--------|------|------|
| `?` | 匹配单个字符 | `/api/user/?` 匹配 `/api/user/1` |
| `*` | 匹配单层路径 | `/api/*/user` 匹配 `/api/v1/user` |
| `**` | 匹配多层路径 | `/api/**` 匹配 `/api/v1/user/list` |
| `{var}` | 路径变量 | `/api/user/{id}` 可提取 `id` |

---

## 2. 自定义参数空间 (Customize Space)

自定义参数空间用于预定义全局变量，供响应集和任务集复用。

### 使用场景

- 定义固定的业务编码（如产品码、商户码）
- 预定义常用的数据结构模板
- 配置环境相关的参数

### 示例

```json
{
  "customizeSpace": {
    "productCode": "PC001",
    "skuCodeList": ["SC001", "SC002", "SC003"],
    "baseUser": {
      "code": "UC001",
      "name": "张三",
      "level": 5
    },
    "errorCodes": {
      "SUCCESS": 200,
      "NOT_FOUND": 404,
      "SERVER_ERROR": 500
    }
  }
}
```

### 在表达式中使用

```json
{
  "response": {
    "body": {
      "productCode": "${#search('productCode', 'CUSTOMIZESPACE')}",
      "userName": "${#search('baseUser.name', 'CUSTOMIZESPACE')}",
      "errorCode": "${#search('errorCodes.SUCCESS', 'CUSTOMIZESPACE')}"
    }
  }
}
```

---

## 3. 响应集 (Responses)

响应集定义返回给客户端的数据。支持配置多个响应，通过条件动态选择。

### 结构

```json
{
  "responses": [
    {
      "name": "响应名称",
      "support": ["条件表达式 1", "条件表达式 2"],
      "delayTime": 0,
      "response": {
        "status": 200,
        "headers": {
          "Content-Type": ["application/json;charset=UTF-8"]
        },
        "body": {
          "key": "value"
        }
      }
    }
  ]
}
```

### 字段说明

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `name` | String | ✅ | 响应名称，仅用于标识 |
| `support` | Array | ❌ | 条件表达式数组，全部为 true 时触发 |
| `delayTime` | Number | ❌ | 延迟响应时间（毫秒） |
| `response` | Object | ✅ | HTTP 响应配置 |

### 条件匹配逻辑

- `support` 为空数组或不配置：默认匹配（总是返回此响应）
- `support` 有多个表达式：全部为 `true` 时才匹配
- 多个响应都匹配：返回**第一个**匹配的响应

### 示例：条件响应

```json
{
  "responses": [
    {
      "name": "VIP 用户响应",
      "support": [
        "${#search('headerMap.userLevel') == 'VIP'}"
      ],
      "response": {
        "body": {
          "message": "尊敬的 VIP 用户，您好！",
          "discount": 0.8
        }
      }
    },
    {
      "name": "普通用户响应",
      "support": [],
      "response": {
        "body": {
          "message": "您好！",
          "discount": 1.0
        }
      }
    }
  ]
}
```

### 响应体支持动态表达式

```json
{
  "response": {
    "status": 200,
    "headers": {
      "X-Request-ID": ["${#uuid()}"],
      "X-Timestamp": ["${#now('yyyy-MM-dd HH:mm:ss')}"]
    },
    "body": {
      "code": "${#search('errorCodes.SUCCESS', 'CUSTOMIZESPACE')}",
      "data": {
        "userId": "${#search('pathParameterMap.userId')}",
        "items": "${#page(1, 10, 100, '{\"name\":\"商品${#random('int',100)}\"}')}",
        "timestamp": "${#now()}"
      }
    }
  }
}
```

---

## 4. 任务集 (Tasks)

任务集用于配置 HTTP 请求任务，支持同步和异步两种模式。

### 同步任务 vs 异步任务

| 特性 | 同步任务 | 异步任务 |
|------|----------|----------|
| `async` | `false` | `true` |
| 执行时机 | Mock Handler 执行过程中同步调用 | 注册到定时任务管理器，独立执行 |
| 阻塞 | 阻塞后续处理 | 不阻塞 |
| cron 表达式 | 忽略 | 必需 |
| 执行次数 | 忽略 | 可配置 |

### 结构

```json
{
  "tasks": [
    {
      "name": "任务名称",
      "support": ["条件表达式"],
      "async": false,
      "cron": "* * * * * ?",
      "numberOfExecute": 1,
      "request": {
        "requestUrl": "http://example.com/webhook",
        "httpMethod": "POST",
        "headers": {
          "Content-Type": ["application/json"]
        },
        "body": {
          "event": "order_created",
          "orderId": "${#search('jsonBody.orderId')}"
        },
        "uriVariables": {}
      }
    }
  ]
}
```

### 字段说明

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `name` | String | ✅ | 任务名称 |
| `support` | Array | ❌ | 条件表达式，全部为 true 时执行 |
| `async` | Boolean | ❌ | 是否异步执行，默认 `false` |
| `cron` | String | ❌ | 异步任务 cron 表达式 |
| `numberOfExecute` | String | ❌ | 异步任务执行次数 |
| `request` | Object | ✅ | HTTP 请求配置 |

### 示例：同步任务（Webhook）

```json
{
  "tasks": [
    {
      "name": "订单创建通知",
      "support": [],
      "async": false,
      "request": {
        "requestUrl": "http://notification-service/webhook",
        "httpMethod": "POST",
        "headers": {
          "Content-Type": ["application/json"],
          "X-Auth-Token": ["secret-token"]
        },
        "body": {
          "eventType": "ORDER_CREATED",
          "orderId": "${#search('jsonBody.orderId')}",
          "timestamp": "${#now()}"
        }
      }
    }
  ]
}
```

### 示例：异步任务（定时回调）

```json
{
  "tasks": [
    {
      "name": "支付结果回调",
      "support": [
        "${#search('jsonBody.needCallback') == true}"
      ],
      "async": true,
      "cron": "0 0 12 * * ?",
      "numberOfExecute": "1",
      "request": {
        "requestUrl": "http://merchant-service/callback",
        "httpMethod": "POST",
        "body": {
          "orderId": "${#search('jsonBody.orderId')}",
          "status": "PAID",
          "callbackTime": "${#now('yyyy-MM-dd HH:mm:ss')}"
        }
      }
    }
  ]
}
```

---

## 动态表达式语法

### 基本语法

动态表达式使用 `${...}` 包裹，在运行时动态计算：

```json
{
  "staticValue": "固定值",
  "dynamicValue": "${#now()}",
  "nestedExpression": "${#search('jsonBody.' + #search('key'))}"
}
```

### 表达式类型

#### 1. 函数表达式

```javascript
${#functionName(arg1, arg2)}
```

- 函数名不区分大小写
- 参数使用英文逗号分隔
- 字符串参数使用单引号 `'`

#### 2. 逻辑表达式

```javascript
${1 + 1 == 2}
${#search('age') >= 18 && #search('status') == 'ACTIVE'}
```

支持的运算符：

| 类型 | 运算符 |
|------|--------|
| 比较 | `==`, `!=`, `>`, `>=`, `<`, `<=` |
| 逻辑 | `&&`, `||` |
| 算术 | `+`, `-`, `*`, `/`, `%` |

#### 3. 嵌套表达式与转义

当表达式嵌套时，内层表达式的单引号需要转义（使用 `^`）：

```javascript
// 错误：引号冲突
${#search('data.#search('key')', json)}

// 正确：内层引号转义
${#search('data.#search(^'key^')', json)}
```

---

## 执行流程

```
请求到达
    ↓
匹配 Mock Handler (httpMethods + requestUri)
    ↓
执行前置延迟 (delayTime)
    ↓
执行同步任务 (Tasks with async=false)
    ↓
评估响应条件 (Responses.support)
    ↓
选择第一个匹配的响应
    ↓
执行响应延迟 (response.delayTime)
    ↓
返回响应给客户端
    ↓
注册异步任务 (Tasks with async=true)
```

---

## 下一步

- 🔍 [Search 函数详解](03-search-function.md) - 深入学习数据提取
- 📚 [RequestInfo 参考](04-request-info-reference.md) - 完整的请求信息字段
- 🍳 [常用模式](06-cookbook.md) - 实战模式示例
