# HTTP 任务示例

## 示例 1: 同步任务 - Webhook 通知

在响应前同步触发 Webhook 通知。

```json
{
  "name": "订单创建通知",
  "httpMethods": ["POST"],
  "requestUri": "/api/order/create",
  "tasks": [
    {
      "name": "发送通知",
      "support": [],
      "async": false,
      "request": {
        "requestUrl": "http://notification-service/webhook/order-created",
        "httpMethod": "POST",
        "headers": {
          "Content-Type": ["application/json"],
          "X-Auth-Token": ["secret-webhook-token"]
        },
        "body": {
          "eventType": "ORDER_CREATED",
          "orderId": "${#search('jsonBody.orderId')}",
          "userId": "${#search('jsonBody.userId')}",
          "amount": "${#search('jsonBody.amount')}",
          "timestamp": "${#now('yyyy-MM-dd HH:mm:ss')}"
        }
      }
    }
  ],
  "responses": [
    {
      "name": "创建成功",
      "response": {
        "status": 201,
        "body": {
          "code": 201,
          "message": "订单创建成功，通知已发送",
          "data": {
            "orderId": "${#search('jsonBody.orderId')}",
            "orderNo": "ORD-${#now('yyyyMMddHHmmss')}"
          }
        }
      }
    }
  ]
}
```

**测试**:
```bash
curl -X POST http://localhost:8080/api/order/create \
  -H "Content-Type: application/json" \
  -d '{"orderId":"O123","userId":"U456","amount":199.99}'
```

---

## 示例 2: 异步任务 - 定时回调

配置异步定时任务，模拟延迟回调。

```json
{
  "name": "支付结果回调",
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
        "requestUrl": "http://merchant-service/api/callback",
        "httpMethod": "POST",
        "headers": {
          "Content-Type": ["application/json"],
          "X-Callback-Sign": ["${#uuid(,,true)}"]
        },
        "body": {
          "orderId": "${#search('jsonBody.orderId')}",
          "transactionId": "${#uuid()}",
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
          "message": "支付处理中，将在今天 12:00 回调",
          "data": {
            "paymentId": "${#uuid()}",
            "estimatedCallbackTime": "今天 12:00"
          }
        }
      }
    }
  ]
}
```

**测试**:
```bash
curl -X POST http://localhost:8080/api/payment/initiate \
  -H "Content-Type: application/json" \
  -d '{"orderId":"O123","amount":299.99,"needCallback":true}'
```

---

## 示例 3: 多任务链式调用

配置多个同步任务，按顺序执行。

```json
{
  "name": "数据处理流水线",
  "httpMethods": ["POST"],
  "requestUri": "/api/data/process",
  "tasks": [
    {
      "name": "步骤 1: 数据验证",
      "support": [],
      "async": false,
      "request": {
        "requestUrl": "http://validation-service/validate",
        "httpMethod": "POST",
        "headers": {
          "Content-Type": ["application/json"]
        },
        "body": {
          "data": "${#search('jsonBody.data')}",
          "rules": ["NOT_NULL", "FORMAT_CHECK"]
        }
      }
    },
    {
      "name": "步骤 2: 数据转换",
      "support": [],
      "async": false,
      "request": {
        "requestUrl": "http://transform-service/transform",
        "httpMethod": "POST",
        "headers": {
          "Content-Type": ["application/json"]
        },
        "body": {
          "sourceData": "${#search('jsonBody.data')}",
          "targetFormat": "JSON"
        }
      }
    },
    {
      "name": "步骤 3: 数据保存",
      "support": [],
      "async": false,
      "request": {
        "requestUrl": "http://storage-service/save",
        "httpMethod": "POST",
        "headers": {
          "Content-Type": ["application/json"]
        },
        "body": {
          "data": "${#search('jsonBody.data')}",
          "storageType": "DATABASE"
        }
      }
    }
  ],
  "responses": [
    {
      "name": "处理成功",
      "response": {
        "body": {
          "code": 200,
          "message": "数据处理完成（验证→转换→保存）",
          "data": {
            "processId": "${#uuid()}",
            "timestamp": "${#now('yyyy-MM-dd HH:mm:ss')}"
          }
        }
      }
    }
  ]
}
```

**测试**:
```bash
curl -X POST http://localhost:8080/api/data/process \
  -H "Content-Type: application/json" \
  -d '{"data":"test data"}'
```

---

## 示例 4: 条件任务

根据条件决定是否执行任务。

```json
{
  "name": "条件通知",
  "httpMethods": ["POST"],
  "requestUri": "/api/order/submit",
  "tasks": [
    {
      "name": "VIP 客户通知",
      "support": [
        "${#search('headerMap.X-Customer-Level') == 'VIP'}"
      ],
      "async": false,
      "request": {
        "requestUrl": "http://vip-service/notify",
        "httpMethod": "POST",
        "body": {
          "message": "VIP 客户订单已提交",
          "orderId": "${#search('jsonBody.orderId')}"
        }
      }
    },
    {
      "name": "普通通知",
      "support": [
        "${#search('headerMap.X-Customer-Level') != 'VIP'}"
      ],
      "async": false,
      "request": {
        "requestUrl": "http://notification-service/notify",
        "httpMethod": "POST",
        "body": {
          "message": "订单已提交",
          "orderId": "${#search('jsonBody.orderId')}"
        }
      }
    },
    {
      "name": "大额订单通知",
      "support": [
        "${#search('jsonBody.amount') >= 1000}"
      ],
      "async": true,
      "cron": "0 0 9 * * ?",
      "numberOfExecute": "1",
      "request": {
        "requestUrl": "http://big-order-service/review",
        "httpMethod": "POST",
        "body": {
          "orderId": "${#search('jsonBody.orderId')}",
          "amount": "${#search('jsonBody.amount')}",
          "reviewType": "MANUAL"
        }
      }
    }
  ],
  "responses": [
    {
      "name": "提交成功",
      "response": {
        "body": {
          "code": 200,
          "message": "订单已提交",
          "data": {
            "orderId": "${#search('jsonBody.orderId')}"
          }
        }
      }
    }
  ]
}
```

**测试**:
```bash
# VIP 客户
curl -X POST http://localhost:8080/api/order/submit \
  -H "X-Customer-Level: VIP" \
  -H "Content-Type: application/json" \
  -d '{"orderId":"O123","amount":500}'

# 大额订单
curl -X POST http://localhost:8080/api/order/submit \
  -H "Content-Type: application/json" \
  -d '{"orderId":"O456","amount":1500}'
```

---

## 示例 5: 带 URI 变量的任务

在任务 URL 中使用 URI 变量。

```json
{
  "name": "RESTful 任务",
  "httpMethods": ["POST"],
  "requestUri": "/api/users/{userId}/orders",
  "tasks": [
    {
      "name": "更新用户统计",
      "support": [],
      "async": false,
      "request": {
        "requestUrl": "http://user-service/users/{userId}/stats",
        "httpMethod": "PUT",
        "uriVariables": {
          "userId": ["${#search('pathParameterMap.userId')}"]
        },
        "body": {
          "action": "INCREMENT_ORDER_COUNT",
          "orderId": "${#search('jsonBody.orderId')}"
        }
      }
    }
  ],
  "responses": [
    {
      "name": "创建成功",
      "response": {
        "status": 201,
        "body": {
          "code": 201,
          "data": {
            "orderId": "${#search('jsonBody.orderId')}",
            "userId": "${#search('pathParameterMap.userId')}"
          }
        }
      }
    }
  ]
}
```

**测试**:
```bash
curl -X POST http://localhost:8080/api/users/U123/orders \
  -H "Content-Type: application/json" \
  -d '{"orderId":"O456"}'
```

---

## 示例 6: 异步任务管理

配置可管理的异步任务。

```json
{
  "name": "定期数据同步",
  "httpMethods": ["POST"],
  "requestUri": "/api/sync/start",
  "tasks": [
    {
      "name": "数据同步任务",
      "support": [
        "${#search('jsonBody.enabled') == true}"
      ],
      "async": true,
      "cron": "${#search('jsonBody.cron', 'REQUESTINFO', '0 */5 * * * ?')}",
      "numberOfExecute": "${#search('jsonBody.executeCount', 'REQUESTINFO', '0')}",
      "request": {
        "requestUrl": "http://sync-service/sync",
        "httpMethod": "POST",
        "headers": {
          "Content-Type": ["application/json"],
          "X-Sync-Source": ["dynamic-mock"]
        },
        "body": {
          "syncType": "${#search('jsonBody.syncType')}",
          "targetSystem": "${#search('jsonBody.targetSystem')}",
          "timestamp": "${#now()}"
        }
      }
    }
  ],
  "responses": [
    {
      "name": "启动成功",
      "response": {
        "body": {
          "code": 200,
          "message": "同步任务已启动",
          "data": {
            "taskId": "${#uuid()}",
            "cron": "${#search('jsonBody.cron', 'REQUESTINFO', '0 */5 * * * ?')}",
            "executeCount": "${#search('jsonBody.executeCount', 'REQUESTINFO', '0')}"
          }
        }
      }
    }
  ]
}
```

**测试**:
```bash
# 每 5 分钟执行一次，无限次
curl -X POST http://localhost:8080/api/sync/start \
  -H "Content-Type: application/json" \
  -d '{"enabled":true,"syncType":"FULL","targetSystem":"ERP"}'

# 每 1 小时执行一次，执行 10 次
curl -X POST http://localhost:8080/api/sync/start \
  -H "Content-Type: application/json" \
  -d '{"enabled":true,"cron":"0 0 * * * ?","executeCount":"10","syncType":"INCREMENTAL","targetSystem":"CRM"}'
```

---

## 示例 7: 任务错误处理

模拟任务执行失败场景。

```json
{
  "name": "带错误处理的任务",
  "httpMethods": ["POST"],
  "requestUri": "/api/async-task",
  "tasks": [
    {
      "name": "主任务",
      "support": [
        "${#search('headerMap.X-Test-Error') != 'true'}"
      ],
      "async": false,
      "request": {
        "requestUrl": "http://task-service/execute",
        "httpMethod": "POST",
        "body": {
          "taskId": "${#search('jsonBody.taskId')}"
        }
      }
    },
    {
      "name": "错误补偿任务",
      "support": [
        "${#search('headerMap.X-Test-Error') == 'true'}"
      ],
      "async": true,
      "cron": "0 0 12 * * ?",
      "numberOfExecute": "1",
      "request": {
        "requestUrl": "http://compensation-service/compensate",
        "httpMethod": "POST",
        "body": {
          "taskId": "${#search('jsonBody.taskId')}",
          "reason": "主任务执行失败",
          "compensateTime": "${#now('yyyy-MM-dd HH:mm:ss')}"
        }
      }
    }
  ],
  "responses": [
    {
      "name": "成功",
      "support": [
        "${#search('headerMap.X-Test-Error') != 'true'}"
      ],
      "response": {
        "body": {
          "code": 200,
          "message": "任务执行成功"
        }
      }
    },
    {
      "name": "失败 - 已安排补偿",
      "support": [
        "${#search('headerMap.X-Test-Error') == 'true'}"
      ],
      "response": {
        "status": 500,
        "body": {
          "code": 500,
          "message": "任务执行失败，已安排补偿任务",
          "compensateTime": "今天 12:00"
        }
      }
    }
  ]
}
```

**测试**:
```bash
# 正常执行
curl -X POST http://localhost:8080/api/async-task \
  -H "Content-Type: application/json" \
  -d '{"taskId":"T123"}'

# 模拟错误
curl -X POST http://localhost:8080/api/async-task \
  -H "Content-Type: application/json" \
  -H "X-Test-Error: true" \
  -d '{"taskId":"T456"}'
```

---

## Cron 表达式参考

### 格式

```
秒 分 时 日 月 周
```

### 常用示例

| 表达式 | 说明 |
|--------|------|
| `0 0 12 * * ?` | 每天 12:00 执行 |
| `0 0/5 * * * ?` | 每 5 分钟执行 |
| `0 0 * * * ?` | 每小时执行 |
| `0 0 9 * * ?` | 每天 9:00 执行 |
| `0 0 9-18 * * ?` | 每天 9:00-18:00 每小时执行 |
| `0 0 12 ? * WED` | 每周三 12:00 执行 |
| `0 0 12 1 * ?` | 每月 1 号 12:00 执行 |
| `0 0 0 1 1 ?` | 每年 1 月 1 号 0:00 执行 |

---

## 相关文档

- [核心概念](../02-core-concepts.md) - 任务集说明
- [常用模式](../06-cookbook.md) - 任务模式
- [函数参考](../05-function-reference.md) - 相关函数
