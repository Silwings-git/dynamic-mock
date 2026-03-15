# Dynamic-Mock 文档中心

> 欢迎使用 Dynamic-Mock！本指南将帮助你快速上手并深入了解 DSL 配置。

---

## 📖 学习路径

### 新手入门（推荐顺序）

```
1. 快速入门 (5 分钟)
   ↓
2. 核心概念 (30 分钟)
   ↓
3. Search 函数详解 (45 分钟) ⭐
   ↓
4. 常用模式 (60 分钟)
   ↓
5. 函数参考 (随用随查)
```

---

## 📚 文档导航

### 入门指南

| 文档 | 说明 | 预计时间 |
|------|------|----------|
| [🚀 快速入门](01-quick-start.md) | 5 分钟上手，创建第一个 Mock Handler | 5 分钟 |
| [📖 核心概念](02-core-concepts.md) | DSL 结构、表达式、响应集、任务集详解 | 30 分钟 |

### 核心主题

| 文档 | 说明 | 重要性 |
|------|------|--------|
| [🔍 Search 函数详解](03-search-function.md) | **最重要的函数**，数据提取完全指南 | ⭐⭐⭐ |
| [📋 RequestInfo 参考](04-request-info-reference.md) | 完整的请求信息字段说明 | ⭐⭐⭐ |

### 参考手册

| 文档 | 说明 |
|------|------|
| [📚 函数参考](05-function-reference.md) | 分类整理的所有内置函数（31 个） |
| [🍳 常用模式](06-cookbook.md) | 实战模式和示例集合 |

### 示例库

| 示例 | 说明 |
|------|------|
| [基础响应](07-examples/basic-response.md) | 静态响应、动态时间戳、UUID、延迟等 |
| [条件逻辑](07-examples/conditional-logic.md) | 基于方法、角色、参数的条件响应 |
| [分页](07-examples/pagination.md) | Page 函数的各种使用场景 |
| [HTTP 任务](07-examples/http-tasks.md) | 同步/异步任务、Webhook、定时回调 |

### 归档文档

| 文档 | 说明 |
|------|------|
| [旧版 API 文档](legacy/Api.md) | 原始综合文档（已弃用，仅供参考） |

---

## 🎯 快速查找

### 我想...

| 需求 | 查看文档 |
|------|----------|
| 快速创建一个 Mock Handler | [快速入门](01-quick-start.md) |
| 从请求中提取数据 | [Search 函数详解](03-search-function.md) |
| 了解有哪些函数可用 | [函数参考](05-function-reference.md) |
| 实现条件响应 | [条件逻辑示例](07-examples/conditional-logic.md) |
| 生成分页数据 | [分页示例](07-examples/pagination.md) |
| 配置 Webhook 通知 | [HTTP 任务示例](07-examples/http-tasks.md) |
| 查看常见使用模式 | [常用模式](06-cookbook.md) |
| 了解 RequestInfo 有哪些字段 | [RequestInfo 参考](04-request-info-reference.md) |

### 函数速查

| 功能 | 函数 |
|------|------|
| 提取数据 | `#search(path, scope, default)` |
| 分页 | `#page(page, size, total/template)` |
| 时间戳 | `#now(format)` / `#time(format)` |
| UUID | `#uuid()` |
| 随机数 | `#random(type, min, max)` |
| 字符串拼接 | `#concat(a, b, c)` / `#join(delim, a, b, c)` |
| 条件判断 | `#selectIf(cond, a, b)` |
| 比较 | `#eq(a, b)` / `#neq(a, b)` / `#isBlank(s)` |
| 缓存 | `#saveCache(key, value)` |
| 调试 | `#print(value)` |

---

## 💡 重点推荐

### 新手必读

1. **[快速入门](01-quick-start.md)** - 先跑起来，建立信心
2. **[Search 函数详解](03-search-function.md)** - 这是最重要的函数，务必掌握
3. **[常用模式](06-cookbook.md)** - 看看别人怎么用，快速上手

### 高手查阅

1. **[函数参考](05-function-reference.md)** - 需要时快速查找
2. **[RequestInfo 参考](04-request-info-reference.md)** - 了解可提取的数据字段
3. **[示例库](07-examples/)** - 复制粘贴，快速修改使用

---

## 🔧 工具与资源

### 在线工具

- **Web 管理界面**: `http://localhost:8080/dynamic-mock-admin/`
- **Swagger API**: `http://localhost:8080/swagger-ui.html`

### 调试技巧

1. 使用 `#print()` 函数打印中间结果
2. 在 Web 界面查看执行日志
3. 使用 Postman/curl 测试 Mock Handler

### 常见问题

查看 [快速入门 - 常见问题](01-quick-start.md#常见问题)

---

## 📝 文档更新记录

### v2.0 (2024-01) - 重构版

- ✅ 新增快速入门教程
- ✅ 新增核心概念详解
- ✅ **重点重构 Search 函数文档**
- ✅ **新增 RequestInfo 完整参考**
- ✅ 函数分类整理
- ✅ 新增常用模式手册
- ✅ 新增示例库

### v1.0 (旧版)

- 单一综合文档（已归档到 [legacy/Api.md](legacy/Api.md)）

---

## 🤝 反馈与支持

如有问题或建议，请：

1. 查看 [常用模式](06-cookbook.md) 是否有类似场景
2. 查看 [示例库](07-examples/) 是否有参考示例
3. 联系开发团队

---

**开始学习**: [快速入门 →](01-quick-start.md)
