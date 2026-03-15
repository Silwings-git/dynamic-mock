# 常用模式手册

> **说明**: 本手册收集了 Dynamic-Mock 的常见使用模式和实战示例，可直接复制使用。

---

## 响应模式

### 1. 静态响应 + 动态时间戳

最简单的动态响应，适合快速 Mock 固定结构的接口。

```json
{
  "name": "固定响应",
  "httpMethods": ["GET"],
  "requestUri": "/api/status",
  "responses": [
    {
      "name": "成功",
      "response": {
        "status": 200,
        "body": {
          "code": 200,
          "message": "success",
          "timestamp": "${#now('yyyy-MM-dd HH:mm:ss')}",
          "requestId": "${#uuid()}"
        }
      }
    }
  ]
}
```

---

### 2. 条件响应（根据请求参数）

根据请求参数返回不同的响应内容。

```json
{
  "name": "条件响应",
  "httpMethods": ["GET"],
  "requestUri": "/api/user/{userId}",
  "responses": [
    {
      "name": "VIP 用户",
      "support": [
        "${#search('pathParameterMap.userId') == '1001'}"
      ],
      "response": {
        "body": {
          "code": 200,
          "data": {
            "userId": "1001",
            "userName": "张三",
            "userLevel": "VIP",
            "discount": 0.8
          }
        }
      }
    },
    {
      "name": "普通用户",
      "support": [],
      "response": {
        "body": {
          "code": 200,
          "data": {
            "userId": "${#search('pathParameterMap.userId')}",
            "userName": "普通用户",
            "userLevel": "NORMAL",
            "discount": 1.0
          }
        }
      }
    }
  ]
}
```

---

### 3. 错误模拟

模拟各种错误场景。

```json
{
  "name": "错误模拟",
  "httpMethods": ["POST"],
  "requestUri": "/api/order",
  "responses": [
    {
      "name": "参数错误",
      "support": [
        "${#isBlank(#search('jsonBody.userId'))}"
      ],
      "response": {
        "status": 400,
        "body": {
          "code": 400,
          "message": "userId 不能为空"
        }
      }
    },
    {
      "name": "服务器错误",
      "support": [
        "${#search('headerMap.X-Test-Error') == 'true'}"
      ],
      "response": {
        "status": 500,
        "body": {
          "code": 500,
          "message": "服务器内部错误"
        }
      }
    },
    {
      "name": "成功",
      "support": [],
      "response": {
        "status": 200,
        "body": {
          "code": 200,
          "data": {
            "orderId": "${#uuid()}",
            "status": "CREATED"
          }
        }
      }
    }
  ]
}
```

---

### 4. 分页响应

使用 Page 函数生成分页数据。

```json
{
  "name": "分页列表",
  "httpMethods": ["GET"],
  "requestUri": "/api/products",
  "responses": [
    {
      "name": "分页数据",
      "response": {
        "body": {
          "code": 200,
          "data": {
            "page": "${#search('parameterMap.page[0]', 'REQUESTINFO', 1)}",
            "size": "${#search('parameterMap.size[0]', 'REQUESTINFO', 10)}",
            "total": 100,
            "list": "${#page(\n  #search('parameterMap.page[0]', 'REQUESTINFO', 1),\n  #search('parameterMap.size[0]', 'REQUESTINFO', 10),\n  100,\n  '{\"productId\":\"P${#random('int',1000)}\",\"productName\":\"商品${#random('int',100)}\",\"price\":${#random('int',10,1000)}}',\n  true\n)}"
          }
        }
      }
    }
  ]
}
```

---

## 数据提取模式

### 5. 从不同来源组合数据

从路径参数、查询参数、请求头、Body 等多个来源提取数据。

```json
{
  "name": "数据组合",
  "httpMethods": ["POST"],
  "requestUri": "/api/search/{category}",
  "responses": [
    {
      "name": "响应",
      "body": {
        "pathParam": "${#search('pathParameterMap.category')}",
        "queryParam": "${#search('parameterMap.keyword[0]')}",
        "header": "${#search('headerMap.X-Request-ID')}",
        "cookie": "${#search('cookieMap.sessionId')}",
        "body": "${#search('jsonBody.searchText')}",
        "customized": "${#search('defaultKeyword', 'CUSTOMIZESPACE')}"
      }
    }
  ],
  "customizeSpace": {
    "defaultKeyword": "默认关键词"
  }
}
```

---

### 6. 提取嵌套 JSON 数据

从深层嵌套的 JSON 中提取数据。

```json
{
  "name": "嵌套提取",
  "httpMethods": ["POST"],
  "requestUri": "/api/order/create",
  "responses": [
    {
      "name": "响应",
      "body": {
        "userId": "${#search('jsonBody.user.id')}",
        "userName": "${#search('jsonBody.user.profile.name')}",
        "firstItemId": "${#search('jsonBody.order.items[0].itemId')}",
        "totalAmount": "${#search('jsonBody.order.totalAmount')}",
        "shippingAddress": "${#search('jsonBody.user.addresses[0].detail')}"
      }
    }
  ]
}
```

---

### 7. 提取表单数据

处理 `application/x-www-form-urlencoded` 请求。

```json
{
  "name": "表单处理",
  "httpMethods": ["POST"],
  "requestUri": "/api/login",
  "responses": [
    {
      "name": "登录成功",
      "support": [
        "${#equals(#search('formBody.username[0]'), 'admin')}"
      ],
      "response": {
        "body": {
          "code": 200,
          "token": "${#uuid()}",
          "username": "${#search('formBody.username[0]')}"
        }
      }
    },
    {
      "name": "登录失败",
      "support": [],
      "response": {
        "status": 401,
        "body": {
          "code": 401,
          "message": "用户名或密码错误"
        }
      }
    }
  ]
}
```

---

## 条件逻辑模式

### 8. Feature Flag 控制

通过自定义空间控制功能开关。

```json
{
  "name": "功能开关",
  "httpMethods": ["GET"],
  "requestUri": "/api/features",
  "customizeSpace": {
    "enableNewFeature": true,
    "enableBeta": false
  },
  "responses": [
    {
      "name": "新功能版本",
      "support": [
        "${#search('enableNewFeature', 'CUSTOMIZESPACE') == true}"
      ],
      "response": {
        "body": {
          "version": "2.0",
          "features": ["feature-a", "feature-b", "feature-c"]
        }
      }
    },
    {
      "name": "旧版本",
      "support": [],
      "response": {
        "body": {
          "version": "1.0",
          "features": ["feature-a"]
        }
      }
    }
  ]
}
```

---

### 9. A/B 测试

根据用户 ID 分流返回不同版本。

```json
{
  "name": "A/B 测试",
  "httpMethods": ["GET"],
  "requestUri": "/api/home",
  "responses": [
    {
      "name": "版本 A",
      "support": [
        "${#random('int', 100) < 50}"
      ],
      "response": {
        "body": {
          "version": "A",
          "layout": "classic",
          "colorScheme": "light"
        }
      }
    },
    {
      "name": "版本 B",
      "support": [],
      "response": {
        "body": {
          "version": "B",
          "layout": "modern",
          "colorScheme": "dark"
        }
      }
    }
  ]
}
```

---

### 10. 环境判断

根据请求头判断环境返回不同配置。

```json
{
  "name": "环境配置",
  "httpMethods": ["GET"],
  "requestUri": "/api/config",
  "responses": [
    {
      "name": "生产环境",
      "support": [
        "${#equals(#search('headerMap.X-Environment'), 'PROD')}"
      ],
      "response": {
        "body": {
          "apiEndpoint": "https://api.example.com",
          "debug": false,
          "logLevel": "ERROR"
        }
      }
    },
    {
      "name": "测试环境",
      "support": [],
      "response": {
        "body": {
          "apiEndpoint": "https://test-api.example.com",
          "debug": true,
          "logLevel": "DEBUG"
        }
      }
    }
  ]
}
```

---

## 任务模式

### 11. Webhook 通知

同步触发 Webhook 通知。

```json
{
  "name": "订单创建",
  "httpMethods": ["POST"],
  "requestUri": "/api/order/create",
  "tasks": [
    {
      "name": "发送通知",
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
          "userId": "${#search('jsonBody.userId')}",
          "timestamp": "${#now()}"
        }
      }
    }
  ],
  "responses": [
    {
      "name": "成功",
      "response": {
        "body": {
          "code": 200,
          "data": {
            "orderId": "${#search('jsonBody.orderId')}"
          }
        }
      }
    }
  ]
}
```

---

### 12. 定时回调

异步定时任务，模拟支付回调。

```json
{
  "name": "支付回调",
  "httpMethods": ["POST"],
  "requestUri": "/api/payment/initiate",
  "tasks": [
    {
      "name": "支付成功回调",
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
          "amount": "${#search('jsonBody.amount')}",
          "callbackTime": "${#now('yyyy-MM-dd HH:mm:ss')}"
        }
      }
    }
  ],
  "responses": [
    {
      "name": "受理成功",
      "response": {
        "body": {
          "code": 200,
          "message": "支付处理中，将在 12:00 回调"
        }
      }
    }
  ]
}
```

---

### 13. 链式请求

一个任务触发另一个请求。

```json
{
  "name": "链式调用",
  "httpMethods": ["POST"],
  "requestUri": "/api/batch/process",
  "tasks": [
    {
      "name": "第一步：获取数据",
      "async": false,
      "request": {
        "requestUrl": "http://data-service/fetch",
        "httpMethod": "GET"
      }
    },
    {
      "name": "第二步：处理数据",
      "async": false,
      "request": {
        "requestUrl": "http://process-service/handle",
        "httpMethod": "POST",
        "body": {
          "source": "data-service"
        }
      }
    }
  ]
}
```

---

## 高级模式

### 14. 请求计数

使用本地缓存记录请求次数。

```json
{
  "name": "请求计数",
  "httpMethods": ["GET"],
  "requestUri": "/api/counter",
  "responses": [
    {
      "name": "计数响应",
      "response": {
        "body": {
          "count": "${#saveCache('count', #search('count', 'LOCALCACHE', 0) + 1, 'VALUE')}",
          "message": "这是第 ${#search('count', 'LOCALCACHE')} 次访问"
        }
      }
    }
  ]
}
```

---

### 15. 状态模拟

模拟有状态的操作流程。

```json
{
  "name": "订单状态机",
  "httpMethods": ["GET"],
  "requestUri": "/api/order/{orderId}/status",
  "customizeSpace": {
    "statusFlow": ["CREATED", "PAID", "SHIPPED", "DELIVERED"]
  },
  "responses": [
    {
      "name": "状态查询",
      "response": {
        "body": {
          "orderId": "${#search('pathParameterMap.orderId')}",
          "status": "${#search('statusFlow[0]', 'CUSTOMIZESPACE')}",
          "statusHistory": [
            {"status": "CREATED", "time": "${#timeShift('yyyy-MM-dd HH:mm:ss', '', '-P2D', )}"},
            {"status": "PAID", "time": "${#timeShift('yyyy-MM-dd HH:mm:ss', '', '-P1D', )}"}
          ]
        }
      }
    }
  ]
}
```

---

### 16. 动态延迟

根据请求参数动态设置延迟时间。

```json
{
  "name": "动态延迟",
  "httpMethods": ["GET"],
  "requestUri": "/api/slow",
  "responses": [
    {
      "name": "延迟响应",
      "delayTime": "${#search('parameterMap.delay[0]', 'REQUESTINFO', 1000)}",
      "response": {
        "body": {
          "message": "响应完成",
          "actualDelay": "${#search('parameterMap.delay[0]', 'REQUESTINFO', 1000)}ms"
        }
      }
    }
  ]
}
```

---

## 调试技巧

### 17. 打印请求信息

使用 print 函数调试。

```json
{
  "name": "调试模式",
  "httpMethods": ["POST"],
  "requestUri": "/api/debug",
  "responses": [
    {
      "name": "调试信息",
      "response": {
        "body": {
          "requestInfo": "${#print(#search('REQUESTINFO'))}",
          "jsonBody": "${#print(#search('jsonBody'))}",
          "headers": "${#print(#search('headerMap'))}",
          "params": "${#print(#search('parameterMap'))}"
        }
      }
    }
  ]
}
```

---

### 18. 验证表达式

验证复杂表达式是否正确。

```json
{
  "name": "表达式验证",
  "responses": [
    {
      "name": "验证结果",
      "response": {
        "body": {
          "step1": "${#search('jsonBody.user.id')}",
          "step2": "${#concat('用户 ID:', #search('jsonBody.user.id'))}",
          "step3": "${#equals(#search('jsonBody.user.id'), '123')}",
          "final": "${#selectIf(#equals(#search('jsonBody.user.id'), '123'), '匹配', '不匹配')}"
        }
      }
    }
  ]
}
```

---

## 相关文档

- [快速入门](01-quick-start.md) - 5 分钟上手
- [核心概念](02-core-concepts.md) - DSL 结构
- [Search 函数](03-search-function.md) - 数据提取详解
- [函数参考](05-function-reference.md) - 所有函数说明
