# Dynamic-Mock 前端项目

这是 Dynamic-Mock 项目的 Vue 3 前端应用。

## 项目简介

Dynamic-Mock 前端管理平台，基于 Vue 3 + Vite + Element Plus 构建，为 Dynamic-Mock 后端服务提供完整的 Web 管理界面。

## 技术栈

- **框架**: Vue 3 (Composition API)
- **构建工具**: Vite 5
- **UI 组件库**: Element Plus
- **路由管理**: Vue Router 4
- **状态管理**: Pinia
- **HTTP 客户端**: Axios
- **开发语言**: JavaScript

## 项目结构

```
dynamic-mock-web/
├── public/                     # 静态资源
├── src/
│   ├── api/                    # API 接口定义
│   │   ├── auth.js            # 认证相关
│   │   ├── project.js         # 项目管理
│   │   ├── mockHandler.js     # Mock 处理器
│   │   ├── mockTask.js        # Mock 任务
│   │   ├── user.js            # 用户管理
│   │   ├── function.js        # 函数文档
│   │   ├── plugin.js          # 插件管理
│   │   └── file.js            # 文件管理
│   ├── assets/                # 资源文件
│   ├── components/            # 公共组件
│   │   └── Layout/           # 布局组件
│   │       ├── MainLayout.vue
│   │       ├── Header.vue
│   │       └── Sidebar.vue
│   ├── router/                # 路由配置
│   │   └── index.js
│   ├── stores/                # Pinia 状态管理
│   │   ├── auth.js           # 认证状态
│   │   └── project.js        # 项目状态
│   ├── utils/                 # 工具函数
│   │   ├── request.js        # Axios 封装
│   │   └── auth.js           # 认证工具
│   ├── views/                 # 页面组件
│   │   ├── Login.vue         # 登录页
│   │   ├── Project/          # 项目管理
│   │   ├── MockHandler/      # Mock 处理器
│   │   ├── MockTask/         # Mock 任务
│   │   ├── User/             # 用户管理
│   │   ├── Function/         # 函数文档
│   │   └── Plugin/           # 插件管理
│   ├── App.vue               # 根组件
│   └── main.js               # 入口文件
├── .env.development           # 开发环境配置
├── .env.production            # 生产环境配置
├── .gitignore
├── index.html                 # HTML 模板
├── package.json               # 项目依赖
├── vite.config.js            # Vite 配置
└── README.md                  # 项目说明
```

## 快速开始

### 前置要求

- Node.js >= 16
- npm >= 7

### 安装依赖

```bash
cd dynamic-mock-web
npm install
```

### 开发环境运行

```bash
npm run dev
```

访问 http://localhost:5173

### 生产环境构建

```bash
npm run build
```

构建产物将生成在 `dist/` 目录。

## 环境配置

### 开发环境 (.env.development)

```
VITE_API_BASE_URL=http://localhost:8080
VITE_APP_TITLE=Dynamic-Mock 管理平台
```

### 生产环境 (.env.production)

```
VITE_API_BASE_URL=/
VITE_APP_TITLE=Dynamic-Mock 管理平台
```

## 部署方式

### 方式一：嵌入式部署（推荐）

将前端构建产物嵌入到 Java 项目中：

1. 构建前端项目：
```bash
npm run build
```

2. 复制构建产物到 Java 项目：
```bash
cp -r dist/* ../dynamic-mock-admin/src/main/resources/static/
```

3. 启动 Java 应用，前端将随后端一起启动

### 方式二：独立部署

1. 修改 `.env.production` 中的 `VITE_API_BASE_URL` 为后端服务地址

2. 构建项目：
```bash
npm run build
```

3. 使用 Nginx 部署 `dist` 目录：

```nginx
server {
    listen 80;
    server_name your-domain.com;

    location / {
        root /path/to/dist;
        index index.html;
        try_files $uri $uri/ /index.html;
    }

    location /dynamic-mock/ {
        proxy_pass http://backend-server:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

## 功能模块

### 已实现

- ✅ 项目基础架构
- ✅ Axios 请求封装和拦截器
- ✅ Vue Router 路由配置
- ✅ Pinia 状态管理
- ✅ 主布局框架（Header、Sidebar、MainLayout）
- ✅ 登录页面和认证功能
- ✅ 项目管理模块（完整）
- ✅ Mock 处理器列表页面
- ✅ 所有 API 接口封装

### 待完善

- ⏳ Mock 处理器编辑页面（表单模式 + JSON 模式）
- ⏳ Mock 处理器详情页面
- ⏳ 快照管理页面
- ⏳ 运行中任务页面
- ⏳ 任务日志页面
- ⏳ 用户管理页面
- ⏳ 修改密码页面
- ⏳ 函数文档页面
- ⏳ 插件管理页面

## 开发指南

### 添加新页面

1. 在 `src/views/` 创建页面组件
2. 在 `src/router/index.js` 添加路由配置
3. 如需在侧边栏显示，在 `src/components/Layout/Sidebar.vue` 添加菜单项

### API 接口调用

所有 API 都已封装在 `src/api/` 目录下，直接引入使用：

```javascript
import { queryProjects } from '@/api/project'

const data = await queryProjects({ pageNo: 1, pageSize: 10 })
```

### 状态管理

使用 Pinia 进行状态管理：

```javascript
import { useAuthStore } from '@/stores/auth'

const authStore = useAuthStore()
console.log(authStore.user)
```

### 路由守卫

路由守卫已在 `router/index.js` 中配置：
- 自动进行登录验证
- 自动设置页面标题
- 支持管理员权限验证（通过 meta.requireAdmin）

## 代码规范

- 使用 ESLint 进行代码检查
- 使用 Prettier 进行代码格式化
- 组件使用 Vue 3 Composition API
- 使用 `<script setup>` 语法

## 注意事项

1. **认证机制**:
   - 后端需要在响应中返回 token
   - Token 存储在 localStorage
   - 请求时自动添加 Authorization 头

2. **响应格式**:
   - 后端统一响应格式: `{ code, msg, data }`
   - 分页响应格式: `{ code, msg, data: { list, total } }`

3. **权限控制**:
   - 管理员权限通过 `isAdmin` 状态判断
   - 需要管理员权限的路由添加 `meta: { requireAdmin: true }`

4. **项目切换**:
   - 项目选择器在 Header 组件
   - 当前项目 ID 存储在 localStorage
   - 切换项目后自动刷新相关数据

## 后续开发建议

1. **Mock 处理器编辑器**：这是最复杂的页面，建议分步实现：
   - 先实现表单模式的基础信息编辑
   - 再实现响应集和任务集的编辑
   - 最后实现 JSON 模式（可使用 Monaco Editor）

2. **公共组件封装**：
   - JSON 编辑器组件
   - Key-Value 列表编辑组件
   - 条件表达式编辑组件

3. **性能优化**：
   - 路由懒加载（已配置）
   - 组件按需加载
   - 添加加载骨架屏

4. **测试**：
   - 添加单元测试
   - 添加 E2E 测试

## 相关文档

- [Vue 3 文档](https://cn.vuejs.org/)
- [Vite 文档](https://cn.vitejs.dev/)
- [Element Plus 文档](https://element-plus.org/zh-CN/)
- [Vue Router 文档](https://router.vuejs.org/zh/)
- [Pinia 文档](https://pinia.vuejs.org/zh/)

## License

[项目许可证]
