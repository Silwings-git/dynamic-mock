# 条件逻辑示例

## 示例 1: 基于请求方法的条件

根据 HTTP 方法返回不同响应。

```json
{
  "name": "请求方法判断",
  "httpMethods": ["GET", "POST", "PUT", "DELETE"],
  "requestUri": "/api/resource",
  "responses": [
    {
      "name": "GET - 获取资源",
      "support": [
        "${#search('method') == 'GET'}"
      ],
      "response": {
        "body": {
          "code": 200,
          "action": "READ",
          "data": {
            "id": 1,
            "name": "资源名称"
          }
        }
      }
    },
    {
      "name": "POST - 创建资源",
      "support": [
        "${#search('method') == 'POST'}"
      ],
      "response": {
        "status": 201,
        "body": {
          "code": 201,
          "action": "CREATE",
          "data": {
            "id": "${#random('int', 1000, 9999)}",
            "name": "${#search('jsonBody.name')}"
          }
        }
      }
    },
    {
      "name": "PUT - 更新资源",
      "support": [
        "${#search('method') == 'PUT'}"
      ],
      "response": {
        "body": {
          "code": 200,
          "action": "UPDATE",
          "message": "资源已更新"
        }
      }
    },
    {
      "name": "DELETE - 删除资源",
      "support": [
        "${#search('method') == 'DELETE'}"
      ],
      "response": {
        "status": 204,
        "body": {
          "code": 204,
          "action": "DELETE",
          "message": "资源已删除"
        }
      }
    }
  ]
}
```

**测试**:
```bash
curl -X GET http://localhost:8080/api/resource
curl -X POST http://localhost:8080/api/resource -H "Content-Type: application/json" -d '{"name":"新资源"}'
curl -X PUT http://localhost:8080/api/resource
curl -X DELETE http://localhost:8080/api/resource
```

---

## 示例 2: 基于用户角色的条件

根据请求头中的用户角色返回不同数据。

```json
{
  "name": "角色权限控制",
  "httpMethods": ["GET"],
  "requestUri": "/api/admin/dashboard",
  "responses": [
    {
      "name": "管理员",
      "support": [
        "${#search('headerMap.X-User-Role') == 'ADMIN'}"
      ],
      "response": {
        "body": {
          "code": 200,
          "role": "ADMIN",
          "permissions": ["READ", "WRITE", "DELETE", "MANAGE_USERS"],
          "data": {
            "totalUsers": 1000,
            "totalOrders": 5000,
            "revenue": 100000
          }
        }
      }
    },
    {
      "name": "普通用户",
      "support": [
        "${#search('headerMap.X-User-Role') == 'USER'}"
      ],
      "response": {
        "body": {
          "code": 200,
          "role": "USER",
          "permissions": ["READ"],
          "data": {
            "myOrders": 10
          }
        }
      }
    },
    {
      "name": "未授权",
      "support": [],
      "response": {
        "status": 403,
        "body": {
          "code": 403,
          "message": "没有访问权限"
        }
      }
    }
  ]
}
```

**测试**:
```bash
curl -X GET http://localhost:8080/api/admin/dashboard -H "X-User-Role: ADMIN"
curl -X GET http://localhost:8080/api/admin/dashboard -H "X-User-Role: USER"
curl -X GET http://localhost:8080/api/admin/dashboard
```

---

## 示例 3: 基于参数的条件

根据查询参数返回不同结果。

```json
{
  "name": "参数条件",
  "httpMethods": ["GET"],
  "requestUri": "/api/products",
  "responses": [
    {
      "name": "VIP 价格",
      "support": [
        "${#search('parameterMap.userLevel[0]') == 'VIP'}"
      ],
      "response": {
        "body": {
          "code": 200,
          "userLevel": "VIP",
          "products": [
            {"id": 1, "name": "商品 A", "price": 80},
            {"id": 2, "name": "商品 B", "price": 160}
          ],
          "discount": "20% OFF"
        }
      }
    },
    {
      "name": "普通价格",
      "support": [],
      "response": {
        "body": {
          "code": 200,
          "userLevel": "NORMAL",
          "products": [
            {"id": 1, "name": "商品 A", "price": 100},
            {"id": 2, "name": "商品 B", "price": 200}
          ]
        }
      }
    }
  ]
}
```

**测试**:
```bash
curl -X GET "http://localhost:8080/api/products?userLevel=VIP"
curl -X GET "http://localhost:8080/api/products"
```

---

## 示例 4: 多条件组合

使用多个条件表达式。

```json
{
  "name": "多条件组合",
  "httpMethods": ["POST"],
  "requestUri": "/api/order",
  "responses": [
    {
      "name": "VIP 用户大额订单",
      "support": [
        "${#search('headerMap.X-User-Level') == 'VIP'}",
        "${#search('jsonBody.amount') >= 1000}"
      ],
      "response": {
        "body": {
          "code": 200,
          "message": "VIP 大额订单，享受专属优惠",
          "discount": 0.7,
          "gift": "精美礼品"
        }
      }
    },
    {
      "name": "VIP 用户普通订单",
      "support": [
        "${#search('headerMap.X-User-Level') == 'VIP'}"
      ],
      "response": {
        "body": {
          "code": 200,
          "message": "VIP 订单",
          "discount": 0.8
        }
      }
    },
    {
      "name": "大额订单",
      "support": [
        "${#search('jsonBody.amount') >= 1000}"
      ],
      "response": {
        "body": {
          "code": 200,
          "message": "大额订单，享受优惠",
          "discount": 0.9
        }
      }
    },
    {
      "name": "普通订单",
      "support": [],
      "response": {
        "body": {
          "code": 200,
          "message": "订单已创建",
          "discount": 1.0
        }
      }
    }
  ]
}
```

**测试**:
```bash
# VIP + 大额
curl -X POST http://localhost:8080/api/order \
  -H "X-User-Level: VIP" \
  -H "Content-Type: application/json" \
  -d '{"amount": 1500}'

# 普通 + 大额
curl -X POST http://localhost:8080/api/order \
  -H "Content-Type: application/json" \
  -d '{"amount": 1500}'
```

---

## 示例 5: 基于 Cookie 的条件

根据 Cookie 判断用户状态。

```json
{
  "name": "Cookie 条件",
  "httpMethods": ["GET"],
  "requestUri": "/api/user/profile",
  "responses": [
    {
      "name": "已登录",
      "support": [
        "${#isNotBlank(#search('cookieMap.sessionId'))}"
      ],
      "response": {
        "body": {
          "code": 200,
          "loggedIn": true,
          "user": {
            "userId": "${#search('cookieMap.userId')}",
            "userName": "张三"
          }
        }
      }
    },
    {
      "name": "未登录",
      "support": [],
      "response": {
        "status": 401,
        "body": {
          "code": 401,
          "loggedIn": false,
          "message": "请先登录"
        }
      }
    }
  ]
}
```

**测试**:
```bash
# 未登录
curl -X GET http://localhost:8080/api/user/profile

# 已登录
curl -X GET http://localhost:8080/api/user/profile \
  -H "Cookie: sessionId=abc123; userId=1001"
```

---

## 示例 6: 基于请求体的条件

根据 JSON Body 内容判断。

```json
{
  "name": "请求体条件",
  "httpMethods": ["POST"],
  "requestUri": "/api/payment",
  "responses": [
    {
      "name": "余额不足",
      "support": [
        "${#search('jsonBody.amount') > #search('jsonBody.balance')}"
      ],
      "response": {
        "status": 402,
        "body": {
          "code": 402,
          "success": false,
          "message": "余额不足",
          "required": "${#search('jsonBody.amount')}",
          "available": "${#search('jsonBody.balance')}"
        }
      }
    },
    {
      "name": "支付成功",
      "support": [],
      "response": {
        "body": {
          "code": 200,
          "success": true,
          "message": "支付成功",
          "transactionId": "${#uuid()}",
          "amount": "${#search('jsonBody.amount')}"
        }
      }
    }
  ]
}
```

**测试**:
```bash
# 余额不足
curl -X POST http://localhost:8080/api/payment \
  -H "Content-Type: application/json" \
  -d '{"amount": 500, "balance": 300}'

# 支付成功
curl -X POST http://localhost:8080/api/payment \
  -H "Content-Type: application/json" \
  -d '{"amount": 200, "balance": 300}'
```

---

## 示例 7: 使用逻辑运算符

使用 `&&` 和 `||` 组合条件。

```json
{
  "name": "逻辑运算",
  "httpMethods": ["GET"],
  "requestUri": "/api/access",
  "responses": [
    {
      "name": "允许访问",
      "support": [
        "${(#search('headerMap.X-User-Role') == 'ADMIN') || (#search('headerMap.X-User-Role') == 'MANAGER' && #search('parameterMap.level[0]') == 'high')}"
      ],
      "response": {
        "body": {
          "code": 200,
          "access": true,
          "message": "允许访问"
        }
      }
    },
    {
      "name": "拒绝访问",
      "support": [],
      "response": {
        "status": 403,
        "body": {
          "code": 403,
          "access": false,
          "message": "拒绝访问"
        }
      }
    }
  ]
}
```

**测试**:
```bash
# 管理员 - 允许
curl -X GET http://localhost:8080/api/access -H "X-User-Role: ADMIN"

# 经理 + 高级 - 允许
curl -X GET "http://localhost:8080/api/access?level=high" -H "X-User-Role: MANAGER"

# 经理 + 普通 - 拒绝
curl -X GET "http://localhost:8080/api/access?level=low" -H "X-User-Role: MANAGER"
```

---

## 相关文档

- [核心概念](../02-core-concepts.md) - 响应集和条件匹配
- [Search 函数](../03-search-function.md) - 数据提取
- [常用模式](../06-cookbook.md) - 更多条件逻辑模式
