# 分页示例

## 示例 1: 基础分页响应

使用 Page 函数生成基础分页数据。

```json
{
  "name": "基础分页",
  "httpMethods": ["GET"],
  "requestUri": "/api/users",
  "responses": [
    {
      "name": "分页数据",
      "response": {
        "body": {
          "code": 200,
          "data": {
            "page": 1,
            "size": 10,
            "total": 100,
            "list": "${#page(1, 10, 100, '{\"userId\":\"U${#random('int',1000)}\",\"userName\":\"用户${#random('int',100)}\",\"email\":\"user${#random('int',100)}@example.com\"}')}"
          }
        }
      }
    }
  ]
}
```

**测试**:
```bash
curl -X GET http://localhost:8080/api/users
```

**响应示例**:
```json
{
  "code": 200,
  "data": {
    "page": 1,
    "size": 10,
    "total": 100,
    "list": [
      {"userId": "U234", "userName": "用户 56", "email": "user78@example.com"},
      {"userId": "U567", "userName": "用户 12", "email": "user34@example.com"}
    ]
  }
}
```

---

## 示例 2: 动态分页参数

从请求参数中读取分页参数。

```json
{
  "name": "动态分页",
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
            "total": 500,
            "list": "${#page(\n  #search('parameterMap.page[0]', 'REQUESTINFO', 1),\n  #search('parameterMap.size[0]', 'REQUESTINFO', 10),\n  500,\n  '{\"productId\":\"P${#random('int',10000)}\",\"productName\":\"商品${#random('int',1000)}\",\"price\":${#random('int',10,1000)},\"stock\":${#random('int',0,100)}}',\n  true\n)}"
          }
        }
      }
    }
  ]
}
```

**测试**:
```bash
# 第 1 页，每页 10 条
curl -X GET "http://localhost:8080/api/products?page=1&size=10"

# 第 2 页，每页 20 条
curl -X GET "http://localhost:8080/api/products?page=2&size=20"
```

---

## 示例 3: 完整分页信息

包含完整分页元数据。

```json
{
  "name": "完整分页",
  "httpMethods": ["GET"],
  "requestUri": "/api/orders",
  "responses": [
    {
      "name": "分页数据",
      "response": {
        "body": {
          "code": 200,
          "data": {
            "pagination": {
              "currentPage": "${#search('parameterMap.page[0]', 'REQUESTINFO', 1)}",
              "pageSize": "${#search('parameterMap.size[0]', 'REQUESTINFO', 10)}",
              "totalItems": 1000,
              "totalPages": "${1000 / #search('parameterMap.size[0]', 'REQUESTINFO', 10)}",
              "hasPrevious": "${#search('parameterMap.page[0]', 'REQUESTINFO', 1) > 1}",
              "hasNext": "${#search('parameterMap.page[0]', 'REQUESTINFO', 1) < (1000 / #search('parameterMap.size[0]', 'REQUESTINFO', 10))}"
            },
            "items": "${#page(\n  #search('parameterMap.page[0]', 'REQUESTINFO', 1),\n  #search('parameterMap.size[0]', 'REQUESTINFO', 10),\n  1000,\n  '{\"orderId\":\"O${#random('int',100000)}\",\"orderNo\":\"ORD-${#now('yyyyMMdd')}-${#random('int',1000)}\",\"amount\":${#random('int',100,10000)},\"status\":\"${#selectIf(#random('boolean'),'PAID','PENDING')}\"}',\n  true\n)}"
          }
        }
      }
    }
  ]
}
```

**测试**:
```bash
curl -X GET "http://localhost:8080/api/orders?page=1&size=20"
```

---

## 示例 4: 从数据集分页

从预定义的数据集中分页筛选。

```json
{
  "name": "数据集分页",
  "httpMethods": ["GET"],
  "requestUri": "/api/categories",
  "customizeSpace": {
    "allCategories": [
      {"id": 1, "name": "电子产品", "itemCount": 1000},
      {"id": 2, "name": "服装鞋帽", "itemCount": 5000},
      {"id": 3, "name": "图书音像", "itemCount": 3000},
      {"id": 4, "name": "家居用品", "itemCount": 2000},
      {"id": 5, "name": "食品饮料", "itemCount": 1500},
      {"id": 6, "name": "美妆护肤", "itemCount": 800},
      {"id": 7, "name": "母婴用品", "itemCount": 600},
      {"id": 8, "name": "运动户外", "itemCount": 900},
      {"id": 9, "name": "汽车配件", "itemCount": 400},
      {"id": 10, "name": "其他", "itemCount": 200}
    ]
  },
  "responses": [
    {
      "name": "分页数据",
      "response": {
        "body": {
          "code": 200,
          "data": {
            "page": "${#search('parameterMap.page[0]', 'REQUESTINFO', 1)}",
            "size": "${#search('parameterMap.size[0]', 'REQUESTINFO', 10)}",
            "total": 10,
            "list": "${#page(\n  #search('parameterMap.page[0]', 'REQUESTINFO', 1),\n  #search('parameterMap.size[0]', 'REQUESTINFO', 10),\n  #search('allCategories', 'CUSTOMIZESPACE')\n)}"
          }
        }
      }
    }
  ]
}
```

**测试**:
```bash
curl -X GET "http://localhost:8080/api/categories?page=1&size=5"
```

---

## 示例 5: 带排序的分页

模拟带排序的分页响应。

```json
{
  "name": "排序分页",
  "httpMethods": ["GET"],
  "requestUri": "/api/articles",
  "responses": [
    {
      "name": "分页数据",
      "response": {
        "body": {
          "code": 200,
          "data": {
            "page": "${#search('parameterMap.page[0]', 'REQUESTINFO', 1)}",
            "size": "${#search('parameterMap.size[0]', 'REQUESTINFO', 10)}",
            "sort": "${#search('parameterMap.sort[0]', 'REQUESTINFO', 'createTime')}",
            "order": "${#search('parameterMap.order[0]', 'REQUESTINFO', 'DESC')}",
            "total": 500,
            "list": "${#page(\n  #search('parameterMap.page[0]', 'REQUESTINFO', 1),\n  #search('parameterMap.size[0]', 'REQUESTINFO', 10),\n  500,\n  '{\"articleId\":\"A${#random('int',10000)}\",\"title\":\"文章标题${#random('int',1000)}\",\"author\":\"作者${#random('int',100)}\",\"views\":${#random('int',0,10000)},\"createTime\":\"${#timeShift('yyyy-MM-dd HH:mm:ss', '', '-P' + #random('int',0,30) + 'D', )}\"}',\n  true\n)}"
          }
        }
      }
    }
  ]
}
```

**测试**:
```bash
# 按创建时间倒序
curl -X GET "http://localhost:8080/api/articles?page=1&size=10&sort=create_time&order=desc"

# 按浏览量正序
curl -X GET "http://localhost:8080/api/articles?page=1&size=10&sort=views&order=asc"
```

---

## 示例 6: 带筛选条件的分页

模拟带筛选的分页。

```json
{
  "name": "筛选分页",
  "httpMethods": ["GET"],
  "requestUri": "/api/users",
  "responses": [
    {
      "name": "分页数据",
      "response": {
        "body": {
          "code": 200,
          "data": {
            "page": "${#search('parameterMap.page[0]', 'REQUESTINFO', 1)}",
            "size": "${#search('parameterMap.size[0]', 'REQUESTINFO', 10)}",
            "filters": {
              "gender": "${#search('parameterMap.gender[0]')}",
              "ageRange": "${#search('parameterMap.ageRange[0]')}",
              "city": "${#search('parameterMap.city[0]')}"
            },
            "total": "${#selectIf(#isNotBlank(#search('parameterMap.gender[0]')), 300, 1000)}",
            "list": "${#page(\n  #search('parameterMap.page[0]', 'REQUESTINFO', 1),\n  #search('parameterMap.size[0]', 'REQUESTINFO', 10),\n  #selectIf(#isNotBlank(#search('parameterMap.gender[0]')), 300, 1000),\n  '{\"userId\":\"U${#random('int',10000)}\",\"userName\":\"用户${#random('int',1000)}\",\"gender\":\"${#selectIf(#random('boolean'),'M','F')}\",\"age\":${#random('int',18,60)},\"city\":\"${#selectIf(#random('boolean'),'北京','上海')}\"}',\n  true\n)}"
          }
        }
      }
    }
  ]
}
```

**测试**:
```bash
# 筛选女性用户
curl -X GET "http://localhost:8080/api/users?page=1&size=10&gender=F"

# 筛选北京用户
curl -X GET "http://localhost:8080/api/users?page=1&size=10&city=北京"
```

---

## 示例 7: 静态分页数据

使用静态数据模板（不解析表达式）。

```json
{
  "name": "静态分页",
  "httpMethods": ["GET"],
  "requestUri": "/api/static-items",
  "responses": [
    {
      "name": "分页数据",
      "response": {
        "body": {
          "code": 200,
          "data": {
            "page": 1,
            "size": 10,
            "total": 100,
            "list": "${#page(1, 10, 100, '{\"name\":\"固定商品\",\"price\":99.99}', false)}"
          }
        }
      }
    }
  ]
}
```

**测试**:
```bash
curl -X GET http://localhost:8080/api/static-items
```

**响应示例**:
```json
{
  "code": 200,
  "data": {
    "page": 1,
    "size": 10,
    "total": 100,
    "list": [
      {"name": "固定商品", "price": 99.99},
      {"name": "固定商品", "price": 99.99}
      // ... 10 条相同数据
    ]
  }
}
```

---

## 性能提示

### 分页性能对比

不同 Page 函数调用方式的性能（从快到慢）：

1. **静态数据模板** - 最快
   ```javascript
   #page(1, 10, 100, '{"name":"固定"}', false)
   ```

2. **静态数据集** - 快
   ```javascript
   #page(1, 10, #search('dataList'), false)
   ```

3. **动态数据模板** - 中等
   ```javascript
   #page(1, 10, 100, '{"name":"${#random()}"}, true)
   ```

4. **动态数据集** - 最慢
   ```javascript
   #page(1, 10, #search('dataList'), true)
   ```

---

## 相关文档

- [函数参考](../05-function-reference.md) - Page 函数详细说明
- [常用模式](../06-cookbook.md) - 分页响应模式
- [快速入门](../01-quick-start.md) - 基础示例
