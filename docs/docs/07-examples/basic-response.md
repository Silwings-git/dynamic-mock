# 基础响应示例

## 示例 1: 简单静态响应

最基础的 Mock Handler，返回固定内容。

```json
{
  "name": "简单静态响应",
  "httpMethods": ["GET"],
  "requestUri": "/api/hello",
  "responses": [
    {
      "name": "成功",
      "response": {
        "status": 200,
        "headers": {
          "Content-Type": ["application/json;charset=UTF-8"]
        },
        "body": {
          "code": 200,
          "message": "Hello World",
          "data": {
            "greeting": "你好",
            "target": "World"
          }
        }
      }
    }
  ]
}
```

**测试**:
```bash
curl -X GET http://localhost:8080/api/hello
```

---

## 示例 2: 带动态时间戳

每次请求返回不同的时间戳。

```json
{
  "name": "动态时间戳",
  "httpMethods": ["GET"],
  "requestUri": "/api/time",
  "responses": [
    {
      "name": "成功",
      "response": {
        "body": {
          "code": 200,
          "timestamp": "${#now('yyyy-MM-dd HH:mm:ss')}",
          "timestampMs": "${#now()}",
          "date": "${#now('yyyy-MM-dd')}",
          "time": "${#now('HH:mm:ss')}"
        }
      }
    }
  ]
}
```

**测试**:
```bash
curl -X GET http://localhost:8080/api/time
```

---

## 示例 3: 带 UUID 的请求追踪

为每次请求生成唯一的追踪 ID。

```json
{
  "name": "请求追踪",
  "httpMethods": ["GET"],
  "requestUri": "/api/trace",
  "responses": [
    {
      "name": "成功",
      "response": {
        "headers": {
          "X-Request-ID": ["${#uuid()}"],
          "X-Trace-ID": ["${#uuid(,,true)}"]
        },
        "body": {
          "code": 200,
          "requestId": "${#uuid()}",
          "message": "请求已处理"
        }
      }
    }
  ]
}
```

**测试**:
```bash
curl -X GET http://localhost:8080/api/trace -v
```

---

## 示例 4: 随机数据响应

生成随机用户数据。

```json
{
  "name": "随机用户",
  "httpMethods": ["GET"],
  "requestUri": "/api/random-user",
  "responses": [
    {
      "name": "成功",
      "response": {
        "body": {
          "code": 200,
          "data": {
            "userId": "${#uuid(,,true)}",
            "age": "${#random('int', 18, 60)}",
            "score": "${#random('double', 0, 100)}",
            "isActive": "${#random('boolean')}"
          }
        }
      }
    }
  ]
}
```

**测试**:
```bash
curl -X GET http://localhost:8080/api/random-user
```

---

## 示例 5: 带延迟的响应

模拟慢接口。

```json
{
  "name": "慢接口",
  "httpMethods": ["GET"],
  "requestUri": "/api/slow",
  "delayTime": 2000,
  "responses": [
    {
      "name": "成功",
      "delayTime": 1000,
      "response": {
        "body": {
          "code": 200,
          "message": "响应完成（总延迟 3 秒）",
          "timestamp": "${#now('yyyy-MM-dd HH:mm:ss.SSS')}"
        }
      }
    }
  ]
}
```

**测试**:
```bash
time curl -X GET http://localhost:8080/api/slow
```

---

## 示例 6: 自定义响应头

设置自定义响应头。

```json
{
  "name": "自定义响应头",
  "httpMethods": ["GET"],
  "requestUri": "/api/custom-headers",
  "responses": [
    {
      "name": "成功",
      "response": {
        "status": 200,
        "headers": {
          "Content-Type": ["application/json;charset=UTF-8"],
          "X-API-Version": ["1.0.0"],
          "X-RateLimit-Limit": ["100"],
          "X-RateLimit-Remaining": ["${#random('int', 0, 100)}"],
          "Cache-Control": ["no-cache"],
          "X-Request-Time": ["${#now('yyyy-MM-dd HH:mm:ss')}"]
        },
        "body": {
          "code": 200,
          "message": "响应包含自定义头"
        }
      }
    }
  ]
}
```

**测试**:
```bash
curl -X GET http://localhost:8080/api/custom-headers -v
```

---

## 示例 7: 不同 HTTP 状态码

返回不同的 HTTP 状态码。

```json
{
  "name": "错误响应",
  "httpMethods": ["GET"],
  "requestUri": "/api/error/{code}",
  "responses": [
    {
      "name": "404 Not Found",
      "support": [
        "${#search('pathParameterMap.code') == '404'}"
      ],
      "response": {
        "status": 404,
        "body": {
          "code": 404,
          "message": "资源未找到"
        }
      }
    },
    {
      "name": "500 Server Error",
      "support": [
        "${#search('pathParameterMap.code') == '500'}"
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
      "name": "401 Unauthorized",
      "support": [
        "${#search('pathParameterMap.code') == '401'}"
      ],
      "response": {
        "status": 401,
        "body": {
          "code": 401,
          "message": "未授权"
        }
      }
    },
    {
      "name": "默认成功",
      "support": [],
      "response": {
        "status": 200,
        "body": {
          "code": 200,
          "message": "成功"
        }
      }
    }
  ]
}
```

**测试**:
```bash
curl -X GET http://localhost:8080/api/error/404
curl -X GET http://localhost:8080/api/error/500
curl -X GET http://localhost:8080/api/error/200
```

---

## 示例 8: JSON 和文本响应

支持不同 Content-Type。

```json
{
  "name": "文本响应",
  "httpMethods": ["GET"],
  "requestUri": "/api/text",
  "responses": [
    {
      "name": "XML 响应",
      "support": [
        "${#contains(#search('headerMap.Accept'), 'xml')}"
      ],
      "response": {
        "headers": {
          "Content-Type": ["application/xml;charset=UTF-8"]
        },
        "body": "<?xml version=\"1.0\"?><response><code>200</code><message>XML Response</message></response>"
      }
    },
    {
      "name": "HTML 响应",
      "support": [
        "${#contains(#search('headerMap.Accept'), 'text/html')}"
      ],
      "response": {
        "headers": {
          "Content-Type": ["text/html;charset=UTF-8"]
        },
        "body": "<html><body><h1>HTML Response</h1></body></html>"
      }
    },
    {
      "name": "JSON 响应",
      "support": [],
      "response": {
        "headers": {
          "Content-Type": ["application/json;charset=UTF-8"]
        },
        "body": {
          "code": 200,
          "message": "JSON Response"
        }
      }
    }
  ]
}
```

**测试**:
```bash
curl -X GET http://localhost:8080/api/text -H "Accept: application/xml"
curl -X GET http://localhost:8080/api/text -H "Accept: text/html"
curl -X GET http://localhost:8080/api/text
```

---

## 相关文档

- [快速入门](../01-quick-start.md)
- [核心概念](../02-core-concepts.md)
- [常用模式](../06-cookbook.md)
