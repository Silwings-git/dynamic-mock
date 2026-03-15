# 函数参考手册

> **说明**: 本手册按功能分类整理了 Dynamic-Mock 支持的所有内置函数。函数名不区分大小写。

---

## 快速索引

### 📊 数据访问（3 个）
- [`search`](#1-search) - 数据搜索
- [`page`](#2-page) - 分页数据生成
- [`toBean`](#3-tobean) - JSON 转对象

### 🔤 字符串操作（8 个）
- [`concat`](#4-concat) - 字符串拼接
- [`join`](#5-join) - 分隔符连接
- [`substring`](#6-substring) - 子字符串
- [`startsWith`](#7-startswith) - 前缀检查
- [`endsWith`](#8-endswith) - 后缀检查
- [`urlEncode`](#9-urlencode) - URL 编码
- [`urlDecode`](#10-urldecode) - URL 解码
- [`base64Encode`](#29-base64encode) - Base64 编码
- [`base64Decode`](#30-base64decode) - Base64 解码

### 🔍 比较判断（8 个）
- [`equals`/`eq`](#11-equals) - 相等判断
- [`noEquals`/`neq`](#12-noequals) - 不等判断
- [`isBlank`](#13-isblank) - 空白检查
- [`isNotBlank`](#14-isnotblank) - 非空白检查
- [`isEmpty`](#15-isempty) - 空集合检查
- [`isNotEmpty`](#16-isnotempty) - 非空集合检查
- [`isNull`](#17-isnull) - null 检查
- [`isNotNull`](#18-isnotnull) - 非 null 检查
- [`contains`](#19-contains) - 包含检查

### 📅 日期时间（4 个）
- [`now`](#20-now) - 当前时间
- [`time`](#27-time) - 时间戳
- [`timeShift`](#23-timeshift) - 时移
- [`date`](#28-date) - 日期格式化

### 🎲 随机唯一（2 个）
- [`random`](#21-random) - 随机数
- [`uuid`](#22-uuid) - UUID 生成

### 🔀 逻辑控制（2 个）
- [`selectIf`](#24-selectif) - 条件选择
- [`parseJsonString`](#25-parsejsonstring) - JSON 解析

### 💾 缓存操作（1 个）
- [`saveCache`](#26-savecache) - 保存缓存

### 🔧 工具函数（3 个）
- [`print`](#31-print) - 调试打印
- [`toJsonString`](#32-tojsonstring) - 对象转 JSON 字符串
- [`parseJsonString`](#25-parsejsonstring) - JSON 字符串解析

---

## 数据访问函数

### 1. Search

**用途**: 从请求信息、自定义空间、本地缓存或 JSON 对象中提取数据

**签名**:
```javascript
#search(jsonPath)
#search(jsonPath, searchScope)
#search(jsonPath, searchScope, defaultValue)
#search(jsonPath, jsonObject)
#search(jsonPath, jsonObject, defaultValue)
```

**参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `jsonPath` | String | ✅ | JSON 路径表达式 |
| `searchScope` | String | ❌ | 搜索范围：`REQUESTINFO`（默认）、`CUSTOMIZESPACE`、`LOCALCACHE` |
| `defaultValue` | Any | ❌ | 找不到时的默认值 |
| `jsonObject` | Object | ❌ | 要搜索的 JSON 对象 |

**返回值**: `Object` - 提取的值

**示例**:
```javascript
// 从路径参数提取
${#search('pathParameterMap.userId')}

// 从 JSON Body 提取
${#search('jsonBody.user.name')}

// 从自定义空间提取
${#search('productCode', 'CUSTOMIZESPACE')}

// 带默认值
${#search('headerMap.userLevel', 'REQUESTINFO', 'NORMAL')}

// 从 JSON 对象提取
${#search('user.name', '{"user":{"name":"张三"}}')}
```

**相关**: [Search 函数详解](03-search-function.md)

---

### 2. Page

**用途**: 生成分页数据或从数据集中筛选

**签名**:
```javascript
// 数据模板式
#page(currentPage, pageSize, total, dataTemplate)
#page(currentPage, pageSize, total, dataTemplate, isDynamic)

// 数据集筛选式
#page(currentPage, pageSize, dataSet)
#page(currentPage, pageSize, dataSet, isDynamic)
```

**参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `currentPage` | Number | ✅ | 当前页码 |
| `pageSize` | Number | ✅ | 每页数量 |
| `total` | Number | ✅ | 总数据量（模板式） |
| `dataTemplate` | String | ✅ | 数据模板（JSON 或字符串） |
| `dataSet` | Array | ✅ | 数据集 |
| `isDynamic` | Boolean | ❌ | 是否动态解析表达式，默认 `true` |

**返回值**: `Array` - 分页数据

**示例**:
```javascript
// 生成 100 条模拟数据
${#page(1, 10, 100, '{"name":"用户${#random('int',100)}","age":${#random('int',18,60)}}')}

// 从数据集筛选
${#page(1, 10, #search('jsonBody.dataList'))}

// 静态数据（不解析表达式）
${#page(1, 10, 100, '{"name":"固定值"}', false)}
```

**性能**: 静态 > 动态，数据集 > 数据模板

---

### 3. ToBean

**用途**: 将 JSON 字符串转换为 JSON 对象

**签名**:
```javascript
#toBean(jsonString)
```

**参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `jsonString` | String | ✅ | JSON 格式字符串（使用双引号） |

**返回值**: `Object` - JSON 对象

**示例**:
```javascript
// 转换为对象
${#toBean('{"name":"张三","age":18}')}

// 转换为数组
${#toBean('[1,2,3]')}

// 配合 Search 使用
${#search('user.name', #toBean('{"user":{"name":"张三"}}'))}
```

---

## 字符串操作函数

### 4. Concat

**用途**: 拼接多个参数

**签名**:
```javascript
#concat(arg1, arg2, ...)
```

**参数**: 不限数量和类型

**返回值**: `String`

**示例**:
```javascript
${#concat('Hello', ' ', 'World')}  // "Hello World"
${#concat(1, 2, 3)}                // "123"
${#concat()}                       // ""
```

---

### 5. Join

**用途**: 使用分隔符连接多个参数

**签名**:
```javascript
#join(delimiter, arg1, arg2, ...)
```

**参数**:
| 参数 | 类型 | 必填 |
|------|------|------|
| `delimiter` | String | ✅ |
| `args` | Any | ✅ |

**返回值**: `String`

**示例**:
```javascript
${#join('-', 1, 2, 3)}      // "1-2-3"
${#join('#', 'a', 'b', 'c')} // "a#b#c"
${#join('', 'x', 'y', 'z')}  // "xyz"
```

---

### 6. SubString

**用途**: 截取子字符串

**签名**:
```javascript
#substring(str, beginIndex, endIndex)
```

**示例**:
```javascript
${#substring('Hello World', 0, 5)}  // "Hello"
```

---

### 7. StartsWith

**用途**: 判断是否以指定前缀开头

**签名**:
```javascript
#startsWith(str, prefix)
#startsWith(str, prefix, ignoreCase)
```

**示例**:
```javascript
${#startsWith('abc', 'a')}           // true
${#startsWith('abc', 'A', true)}     // true
${#startsWith('abc', 'A')}           // false
```

---

### 8. EndsWith

**用途**: 判断是否以指定后缀结尾

**签名**:
```javascript
#endsWith(str, suffix)
#endsWith(str, suffix, ignoreCase)
```

**示例**:
```javascript
${#endsWith('abc', 'c')}           // true
${#endsWith('abc', 'C', true)}     // true
```

---

### 9. UrlEncode

**用途**: URL 编码

**签名**:
```javascript
#urlEncode(text)
```

**示例**:
```javascript
${#urlEncode('https://example.com?a=1&b=2')}
// 返回："https%3A%2F%2Fexample.com%3Fa%3D1%26b%3D2"
```

---

### 10. UrlDecode

**用途**: URL 解码

**签名**:
```javascript
#urlDecode(text)
```

**示例**:
```javascript
${#urlDecode('https%3A%2F%2Fexample.com')}
// 返回："https://example.com"
```

---

### 29. Base64Encode

**用途**: Base64 编码

**签名**:
```javascript
#base64Encode(text)
#base64Encode(text, asString)
```

**参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `text` | String/Byte[] | ✅ | 要编码的内容 |
| `asString` | Boolean | ❌ | 是否以字符串返回，默认 `true` |

**返回值**: `String` 或 `Byte[]`

**示例**:
```javascript
${#base64Encode('Hello World!')}  // "SGVsbG8gV29ybGQh"
```

---

### 30. Base64Decode

**用途**: Base64 解码

**签名**:
```javascript
#base64Decode(text)
#base64Decode(text, asString)
```

**示例**:
```javascript
${#base64Decode('SGVsbG8gV29ybGQh')}  // "Hello World!"
```

---

## 比较判断函数

### 11. Equals (eq)

**用途**: 判断两个值是否相等

**签名**:
```javascript
#equals(arg1, arg2)
#eq(arg1, arg2)
```

**返回值**: `Boolean`

**示例**:
```javascript
${#equals(1, 1)}      // true
${#eq('a', 'a')}      // true
${#eq(1, 2)}          // false
```

---

### 12. NoEquals (neq)

**用途**: 判断两个值是否不相等

**签名**:
```javascript
#noEquals(arg1, arg2)
#neq(arg1, arg2)
```

**返回值**: `Boolean`

**示例**:
```javascript
${#neq(1, 2)}  // true
${#neq(1, 1)}  // false
```

---

### 13. IsBlank

**用途**: 判断字符串是否为空或空白

**签名**:
```javascript
#isBlank(str)
```

**返回值**: `Boolean`

**示例**:
```javascript
${#isBlank('')}      // true
${#isBlank('  ')}    // true
${#isBlank('a')}     // false
```

---

### 14. IsNotBlank

**用途**: 判断字符串是否非空非空白

**签名**:
```javascript
#isNotBlank(str)
```

**返回值**: `Boolean`

**示例**:
```javascript
${#isNotBlank('a')}  // true
${#isNotBlank('')}   // false
```

---

### 15. IsEmpty

**用途**: 判断集合是否为空

**签名**:
```javascript
#isEmpty(collection)
```

**参数**: `List` 或 `Map`

**返回值**: `Boolean`

**示例**:
```javascript
${#isEmpty([])}      // true
${#isEmpty([1,2,3])} // false
```

---

### 16. IsNotEmpty

**用途**: 判断集合是否非空

**签名**:
```javascript
#isNotEmpty(collection)
```

**返回值**: `Boolean`

---

### 17. IsNull

**用途**: 判断是否为 null

**签名**:
```javascript
#isNull(value)
```

**返回值**: `Boolean`

---

### 18. IsNotNull

**用途**: 判断是否非 null

**签名**:
```javascript
#isNotNull(value)
```

**返回值**: `Boolean`

---

### 19. Contains

**用途**: 判断集合或字符串是否包含指定元素

**签名**:
```javascript
#contains(collection, element)
#contains(string, substring)
```

**返回值**: `Boolean`

**示例**:
```javascript
${#contains([1,2,3], 1)}     // true
${#contains('abc', 'b')}     // true
${#contains('abc', 'x')}     // false
```

---

## 日期时间函数

### 20. Now

**用途**: 获取当前时间

**签名**:
```javascript
#now()
#now(format)
```

**参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `format` | String | ❌ | 时间格式，如 `yyyy-MM-dd HH:mm:ss` |

**返回值**: `Long`（时间戳）或 `String`（格式化时间）

**示例**:
```javascript
${#now()}                           // 1670154841950
${#now('yyyy-MM-dd')}               // "2024-01-15"
${#now('yyyy-MM-dd HH:mm:ss')}      // "2024-01-15 10:30:45"
```

---

### 27. Time

**用途**: 获取时间戳或格式化时间

**签名**:
```javascript
#time()
#time(format)
```

**示例**:
```javascript
${#time()}              // 1692096471702
${#time('yyyy-MM-dd')}  // "2023-08-15"
${#time('/1000')}       // 1692096471 (秒级时间戳)
```

---

### 23. TimeShift

**用途**: 时移计算

**签名**:
```javascript
#timeshift(format, dateToShift, valueToShift, locale)
```

**参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `format` | String | ❌ | 日期格式 |
| `dateToShift` | String | ❌ | 要转换的日期 |
| `valueToShift` | String | ❌ | 时间偏移量（如 `P10DT-1H`） |
| `locale` | String | ❌ | 语言环境（如 `zh_CN`） |

**示例**:
```javascript
${#timeshift('yyyy-MM-dd HH:mm:ss', '2023-06-16 12:00:00', 'P10DT-1H-5M5S', )}
// "2023-06-26 10:55:05"
```

---

### 28. Date

**用途**: 日期格式化

**签名**:
```javascript
#date(timestamp, format)
```

---

## 随机唯一函数

### 21. Random

**用途**: 生成随机数

**签名**:
```javascript
#random()
#random(type)
#random(type, max)
#random(type, min, max)
```

**参数**:
| 参数 | 类型 | 说明 |
|------|------|------|
| `type` | String | `int`, `long`, `double`, `boolean` |
| `max` | Number | 最大值（不包含） |
| `min` | Number | 最小值（包含） |

**返回值**: `Number` 或 `Boolean`

**示例**:
```javascript
${#random()}                    // -962406019 (int)
${#random('boolean')}           // true
${#random('int', 100)}          // 62 (0-99)
${#random('int', 10, 20)}       // 15 (10-19)
```

---

### 22. UUID

**用途**: 生成 UUID

**签名**:
```javascript
#uuid()
#uuid(prefix, length, replace)
```

**参数**:
| 参数 | 类型 | 说明 |
|------|------|------|
| `prefix` | String | UUID 前缀 |
| `length` | Number | 长度限制 |
| `replace` | Boolean | 是否移除 `-` |

**示例**:
```javascript
${#uuid()}                    // "f47ac10b-58cc-4372-a567-0e02b2c3d479"
${#uuid(,,true)}              // "6c62c8fe7e8f438689c168dbcd794b8a"
${#uuid('UserCode', 10, true)} // "UserCode676d529aaa"
```

---

## 逻辑控制函数

### 24. SelectIf

**用途**: 三元运算符

**签名**:
```javascript
#selectIf(condition, argA, argB)
```

**返回值**: `condition` 为 `true` 返回 `argA`，否则返回 `argB`

**示例**:
```javascript
${#selectIf(true, 1, 2)}   // 1
${#selectIf(10 > 0, 'Y', 'N')}  // "Y"
```

---

### 25. ParseJsonString

**用途**: 解析 JSON 字符串并执行其中的动态表达式

**签名**:
```javascript
#parseJsonString(jsonString)
#parseJsonString(jsonString, isDynamic)
```

**示例**:
```javascript
// 静态解析
${#parseJsonString('{"name":"张三"}', false)}

// 动态解析
${#parseJsonString('{"name":"${#search('name')}"}', true)}
```

---

## 缓存操作函数

### 26. SaveCache

**用途**: 向本地缓存写入数据

**签名**:
```javascript
#saveCache(key, value)
#saveCache(key, value, returnType)
```

**参数**:
| 参数 | 类型 | 说明 |
|------|------|------|
| `key` | Any | 缓存键 |
| `value` | Any | 缓存值 |
| `returnType` | String | `KEY`/`VALUE`/`ALL`，默认 `VALUE` |

**示例**:
```javascript
${#saveCache('userId', 123)}                    // 123
${#saveCache('userId', 123, 'KEY')}             // "userId"
${#saveCache('userId', 123, 'ALL')}             // {"key":"userId","value":123}

// 配合 Search 使用
${#search('userId', 'LOCALCACHE')}
```

---

## 工具函数

### 31. Print

**用途**: 调试打印

**签名**:
```javascript
#print(value)
```

**返回值**: 原样返回输入值

**示例**:
```javascript
${#print(#search('jsonBody'))}
// 日志中打印：{user={name=张三}}
// 返回：{user={name=张三}}
```

---

### 32. ToJsonString

**用途**: 对象转 JSON 字符串

**签名**:
```javascript
#toJsonString(object)
#tjs(object)
```

**示例**:
```javascript
${#toJsonString(#toBean('{"name":"张三"}'))}
// 返回："{\"name\":\"张三\"}"
```

---

## 函数速查表

```
数据访问:
  #search(path, [scope], [default])
  #page(page, size, total/template, [dynamic])
  #toBean(jsonString)

字符串:
  #concat(a, b, c)
  #join(delimiter, a, b, c)
  #substring(str, start, end)
  #startsWith(str, prefix, [ignoreCase])
  #endsWith(str, suffix, [ignoreCase])
  #urlEncode(text) / #urlDecode(text)
  #base64Encode(text) / #base64Decode(text)

比较:
  #eq(a, b) / #neq(a, b)
  #isBlank(s) / #isNotBlank(s)
  #isEmpty(c) / #isNotEmpty(c)
  #isNull(v) / #isNotNull(v)
  #contains(collection, element)

时间:
  #now([format])
  #time([format])
  #timeshift(format, date, shift, locale)

随机:
  #random([type], [min], [max])
  #uuid([prefix], [length], [replace])

逻辑:
  #selectIf(cond, a, b)
  #parseJsonString(json, [dynamic])

缓存:
  #saveCache(key, value, [returnType])

工具:
  #print(value)
  #toJsonString(object)
```

---

## 相关文档

- [Search 函数详解](03-search-function.md) - 深入使用 Search
- [RequestInfo 参考](04-request-info-reference.md) - 可用数据字段
- [常用模式](06-cookbook.md) - 实战示例
