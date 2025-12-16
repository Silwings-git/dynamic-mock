# Dynamic-Mock 前端开发需求文档（PRD）

## 1. 项目概述

### 1.1 项目背景
Dynamic-Mock是一个动态HTTP请求模拟响应服务系统，后端已完成开发。现需要开发前端管理界面，为用户提供便捷的可视化操作界面来管理Mock处理器、任务、项目等。

### 1.2 技术栈要求
- **前端框架**: Vue.js 3 (Composition API)
- **构建工具**: Vite
- **UI组件库**: Element Plus（推荐）或 Ant Design Vue
- **路由管理**: Vue Router 4
- **状态管理**: Pinia
- **HTTP客户端**: Axios
- **代码编辑器**: Monaco Editor（用于JSON编辑）
- **样式**: CSS3 / SCSS

### 1.3 部署方式
- **嵌入式部署（优先）**: 前端打包后的静态资源嵌入到Java项目的`resources/static`目录，随Java应用一起启动
- **独立部署（兼容）**: 前端项目支持独立部署为单独的Web服务，通过配置API地址连接后端

### 1.4 设计风格
- **简约**: 界面简洁清晰，去除冗余元素
- **高效**: 操作流程简短，常用功能易于访问
- **响应式**: 支持桌面端和平板设备
- **一致性**: 统一的交互模式和视觉语言

---

## 2. 功能模块设计

### 2.1 用户认证模块

#### 2.1.1 登录页面
**路由**: `/login`

**功能描述**:
- 用户登录入口
- 记住密码功能

**API对接**:
- POST `/dynamic-mock/auth/login`
  - 请求参数: `{userAccount, password, remember}`
  - 响应: 用户信息和token

**页面元素**:
- 用户账号输入框
- 密码输入框
- 记住密码复选框
- 登录按钮
- 简洁的品牌标识

#### 2.1.2 退出登录
**功能描述**:
- 在顶部导航栏提供退出登录功能

**API对接**:
- POST `/dynamic-mock/auth/logout`

---

### 2.2 布局框架

#### 2.2.1 主框架布局
**结构**:
```
┌─────────────────────────────────────┐
│          顶部导航栏                  │
├────────┬────────────────────────────┤
│        │                            │
│  侧边  │        主内容区             │
│  菜单  │                            │
│        │                            │
└────────┴────────────────────────────┘
```

**顶部导航栏包含**:
- 系统Logo和名称
- 当前项目选择器（下拉选择）
- 用户信息
- 退出登录按钮

**侧边菜单包含**:
- Dashboard（可选）
- Mock处理器管理
- Mock任务管理
- 项目管理
- 用户管理（仅管理员可见）
- 函数文档
- 插件管理

---

### 2.3 项目管理模块

#### 2.3.1 项目列表页面
**路由**: `/project/list`

**功能描述**:
- 分页查询项目列表
- 新增项目
- 编辑项目
- 删除项目
- 搜索项目（按项目名称）

**API对接**:
- POST `/dynamic-mock/project/query` - 分页查询项目
- POST `/dynamic-mock/project/save` - 保存项目
- POST `/dynamic-mock/project/del` - 删除项目
- POST `/dynamic-mock/project/queryOwnAll` - 查询当前用户的全部项目

**页面元素**:
- 搜索框（项目名称）
- 新增项目按钮
- 项目列表表格
  - 列: 项目ID、项目名称、Base URI、操作（编辑、删除）
- 分页控件

#### 2.3.2 项目编辑对话框
**表单字段**:
- 项目名称（必填）
- Base URI（必填，例如: `/api/v1`）

---

### 2.4 Mock处理器管理模块

#### 2.4.1 Mock处理器列表页面
**路由**: `/mock-handler/list`

**功能描述**:
- 分页查询Mock处理器
- 新增Mock处理器
- 编辑Mock处理器
- 删除Mock处理器
- 启用/停用Mock处理器
- 高级搜索
- 查看处理器详情

**API对接**:
- POST `/dynamic-mock/mock/handler/query` - 分页查询
- POST `/dynamic-mock/mock/handler/save` - 保存处理器
- POST `/dynamic-mock/mock/handler/find` - 获取处理器详情
- POST `/dynamic-mock/mock/handler/del` - 删除处理器
- POST `/dynamic-mock/mock/handler/enableStatus` - 启用/停用
- POST `/dynamic-mock/mock/handler/queryOwn` - 查询当前用户的全部处理器

**页面元素**:
- 搜索区域
  - 项目选择器
  - 处理器名称输入框
  - HTTP方法选择器（GET/POST/PUT/DELETE）
  - 请求URI输入框
  - 标签输入框
  - 启用状态选择器（全部/已启用/已停用）
  - 搜索按钮、重置按钮
- 新增处理器按钮
- 处理器列表表格
  - 列: 处理器名称、项目名称、HTTP方法、请求URI、标签、启用状态、操作
  - 操作: 查看详情、编辑、启用/停用、删除
- 分页控件

#### 2.4.2 Mock处理器编辑页面
**路由**: `/mock-handler/edit/:id?`

**功能描述**:
- 提供表单模式和JSON模式两种编辑方式
- 支持在两种模式间切换
- 实时校验

**编辑模式切换**:
- 表单模式: 可视化表单编辑
- JSON模式: Monaco Editor代码编辑器

**表单模式结构**:

##### 基础信息区
- 所属项目（必填，下拉选择）
- 处理器名称（必填）
- HTTP方法（必填，多选: GET/POST/PUT/DELETE）
- 请求URI（必填）
- 标签（可选）
- 延迟时间（毫秒，可选）

##### 自定义参数空间区
- Key-Value列表编辑
- 支持添加、删除
- Value支持多种类型（字符串、数字、JSON对象、数组）

##### 响应集配置区
- 响应列表（可添加多个响应）
- 每个响应包含:
  - 响应名称
  - 适用条件集（支持多个条件表达式）
  - 延迟响应时间（毫秒）
  - HTTP响应码
  - HTTP响应头（Key-Value列表）
  - HTTP响应体（JSON编辑器）
- 响应启用/停用开关
- 拖拽排序（响应优先级）

##### 任务集配置区
- 任务列表（可添加多个任务）
- 每个任务包含:
  - 任务名称
  - 适用条件集
  - 是否异步（开关）
  - Cron表达式（异步任务必填）
  - 执行次数（异步任务必填）
  - 请求URL
  - HTTP方法
  - 请求头（Key-Value列表）
  - 请求体（JSON编辑器）
  - URI参数（Key-Value列表）
- 任务启用/停用开关

**JSON模式结构**:
- Monaco Editor编辑器
- 语法高亮
- 实时校验
- 格式化按钮

**页面底部操作**:
- 保存按钮
- 保存并启用按钮
- 取消按钮

#### 2.4.3 Mock处理器详情页面
**路由**: `/mock-handler/detail/:id`

**功能描述**:
- 只读展示处理器配置
- 快速编辑入口
- 启用/停用操作

**API对接**:
- POST `/dynamic-mock/mock/handler/find` - 获取处理器详情
- POST `/dynamic-mock/mock/handler/response/update/:handlerId` - 修改响应
- POST `/dynamic-mock/mock/handler/response/status` - 修改响应开关
- POST `/dynamic-mock/mock/handler/task/status` - 修改任务开关

#### 2.4.4 快照管理
**路由**: `/mock-handler/snapshot/:handlerId`

**功能描述**:
- 查看处理器的历史快照
- 恢复指定快照

**API对接**:
- POST `/dynamic-mock/snapshot/handler/query` - 查询快照列表
- POST `/dynamic-mock/snapshot/handler/find` - 获取快照详情
- POST `/dynamic-mock/snapshot/handler/recover` - 恢复快照

**页面元素**:
- 快照列表表格
  - 列: 快照版本、创建时间、操作（查看、恢复）
- 快照详情对话框（只读展示）

---

### 2.5 Mock任务管理模块

#### 2.5.1 运行中任务页面
**路由**: `/mock-task/running`

**功能描述**:
- 查询当前运行中的任务
- 取消指定任务
- 批量取消任务

**API对接**:
- POST `/dynamic-mock/mock/task/running/query` - 查询运行中任务
- POST `/dynamic-mock/mock/task/running/unregister` - 取消任务
- POST `/dynamic-mock/mock/task/running/unregister/batch` - 批量取消

**页面元素**:
- 筛选区域
  - 项目选择器
  - 处理器选择器（根据项目动态加载）
- 批量取消按钮
- 任务列表表格
  - 列: 任务代码、处理器名称、项目名称、任务名称、下次执行时间、操作
  - 操作: 取消任务（带确认）
- 自动刷新开关（可选）

#### 2.5.2 任务日志页面
**路由**: `/mock-task/log`

**功能描述**:
- 分页查询任务执行日志
- 查看日志详情
- 删除日志

**API对接**:
- POST `/dynamic-mock/mock/task/log/query` - 查询任务日志
- POST `/dynamic-mock/mock/task/log/del` - 删除任务日志

**页面元素**:
- 筛选区域
  - 项目选择器
  - 处理器选择器
  - 任务代码输入框
  - 任务名称输入框
- 删除日志按钮
- 日志列表表格
  - 列: 日志ID、任务代码、任务名称、处理器名称、项目名称、执行时间、执行状态、操作
  - 操作: 查看详情、删除
- 分页控件
- 日志详情对话框
  - 显示请求详情、响应详情

---

### 2.6 用户管理模块（管理员）

#### 2.6.1 用户列表页面
**路由**: `/user/list`

**权限**: 仅管理员可访问

**功能描述**:
- 分页查询用户列表
- 新增用户
- 编辑用户
- 删除用户
- 搜索用户

**API对接**:
- POST `/dynamic-mock/user/query` - 分页查询用户
- POST `/dynamic-mock/user/save` - 保存用户
- POST `/dynamic-mock/user/del` - 删除用户

**页面元素**:
- 搜索区域
  - 用户名输入框
  - 用户账号输入框
  - 角色选择器
- 新增用户按钮
- 用户列表表格
  - 列: 用户ID、用户名、用户账号、角色、权限列表、操作
  - 操作: 编辑、删除
- 分页控件

#### 2.6.2 用户编辑对话框
**表单字段**:
- 用户名（必填）
- 用户账号（必填）
- 密码（新增时必填，编辑时可选）
- 角色（必填，下拉选择）
- 权限列表（多选，项目权限）

#### 2.6.3 修改密码页面
**路由**: `/user/change-password`

**功能描述**:
- 当前用户修改自己的密码

**API对接**:
- POST `/dynamic-mock/user/changePassword`

**表单字段**:
- 旧密码（必填）
- 新密码（必填）
- 确认新密码（必填）

---

### 2.7 函数文档模块

#### 2.7.1 函数列表页面
**路由**: `/function/list`

**功能描述**:
- 查询系统支持的全部函数
- 查看函数详情和使用示例
- 支持按返回类型筛选

**API对接**:
- POST `/dynamic-mock/mock/handler/function/query`

**页面元素**:
- 筛选区域
  - 返回类型选择器
- 函数列表（卡片或表格形式）
  - 显示: 函数名称、最小参数数量、最大参数数量、返回类型
  - 点击展开显示函数详细说明和使用示例

---

### 2.8 插件管理模块

#### 2.8.1 插件列表页面
**路由**: `/plugin/list`

**功能描述**:
- 查看系统已安装的插件列表
- 查看插件详情

**API对接**:
- POST `/dynamic-mock/plugin/list`

**页面元素**:
- 插件列表（卡片形式）
  - 显示: 插件名称、插件版本、插件描述、插件作者

---

### 2.9 文件管理模块

#### 2.9.1 文件上传/下载
**功能描述**:
- 在Mock处理器编辑页面，支持上传JSON/TXT文件作为配置
- 支持下载当前配置为文件

**API对接**:
- POST `/dynamic-mock/file/text/upload` - 上传文本文件
- POST `/dynamic-mock/file/text/download` - 下载文件

---

## 3. 技术实现细节

### 3.1 项目结构
```
dynamic-mock-web/
├── public/
│   └── favicon.ico
├── src/
│   ├── api/                    # API接口定义
│   │   ├── auth.js
│   │   ├── project.js
│   │   ├── mockHandler.js
│   │   ├── mockTask.js
│   │   ├── user.js
│   │   ├── function.js
│   │   ├── plugin.js
│   │   └── file.js
│   ├── assets/                 # 静态资源
│   ├── components/             # 公共组件
│   │   ├── Layout/
│   │   │   ├── Header.vue
│   │   │   ├── Sidebar.vue
│   │   │   └── MainLayout.vue
│   │   ├── JsonEditor.vue      # JSON编辑器组件
│   │   ├── KeyValueEditor.vue  # Key-Value编辑器组件
│   │   └── ...
│   ├── router/                 # 路由配置
│   │   └── index.js
│   ├── stores/                 # Pinia状态管理
│   │   ├── auth.js
│   │   ├── project.js
│   │   └── ...
│   ├── utils/                  # 工具函数
│   │   ├── request.js          # Axios封装
│   │   ├── auth.js             # 认证工具
│   │   └── ...
│   ├── views/                  # 页面组件
│   │   ├── Login.vue
│   │   ├── Project/
│   │   ├── MockHandler/
│   │   ├── MockTask/
│   │   ├── User/
│   │   ├── Function/
│   │   └── Plugin/
│   ├── App.vue
│   └── main.js
├── .env.development            # 开发环境配置
├── .env.production             # 生产环境配置
├── vite.config.js
├── package.json
└── README.md
```

### 3.2 Axios封装

**请求拦截器**:
- 添加认证token到请求头
- 设置统一的请求格式

**响应拦截器**:
- 统一处理响应数据格式
- 统一处理错误（401跳转登录、403权限不足提示、500服务器错误提示）
- 统一处理成功/失败消息提示

### 3.3 路由配置

**路由守卫**:
- 登录验证
- 权限验证（管理员页面）
- 页面标题设置

**路由结构**:
```javascript
[
  { path: '/login', component: Login },
  {
    path: '/',
    component: MainLayout,
    children: [
      { path: 'project/list', component: ProjectList },
      { path: 'mock-handler/list', component: MockHandlerList },
      { path: 'mock-handler/edit/:id?', component: MockHandlerEdit },
      { path: 'mock-handler/detail/:id', component: MockHandlerDetail },
      { path: 'mock-handler/snapshot/:handlerId', component: SnapshotList },
      { path: 'mock-task/running', component: RunningTask },
      { path: 'mock-task/log', component: TaskLog },
      { path: 'user/list', component: UserList, meta: { requireAdmin: true } },
      { path: 'user/change-password', component: ChangePassword },
      { path: 'function/list', component: FunctionList },
      { path: 'plugin/list', component: PluginList },
    ]
  }
]
```

### 3.4 状态管理

**Auth Store**:
- 用户信息
- Token
- 登录状态
- 权限列表

**Project Store**:
- 当前选中项目
- 项目列表
- 切换项目方法

### 3.5 组件封装

#### 3.5.1 JSON编辑器组件
**功能**:
- 基于Monaco Editor
- 语法高亮
- 自动格式化
- 实时校验
- 全屏编辑

#### 3.5.2 Key-Value编辑器组件
**功能**:
- 动态添加/删除行
- 支持多种值类型
- 值为数组时支持展开编辑

#### 3.5.3 条件表达式编辑器组件
**功能**:
- 支持添加多个条件
- 提供表达式语法提示
- 实时校验表达式合法性

---

## 4. 构建和部署

### 4.1 开发环境配置

**`.env.development`**:
```
VITE_API_BASE_URL=http://localhost:8088
VITE_APP_TITLE=Dynamic-Mock 管理平台
```

**开发启动命令**:
```bash
npm run dev
```

### 4.2 生产环境构建

#### 4.2.1 嵌入式部署构建

**`.env.production`**:
```
VITE_API_BASE_URL=/
VITE_APP_TITLE=Dynamic-Mock 管理平台
```

**构建命令**:
```bash
npm run build
```

**构建输出**:
- 输出目录: `dist/`
- 需要将构建产物复制到: `dynamic-mock-admin/src/main/resources/static/`

**Maven集成**:
建议在`dynamic-mock-admin/pom.xml`中添加`frontend-maven-plugin`插件，实现前端自动构建和复制。

**配置示例**:
```xml
<plugin>
    <groupId>com.github.eirslett</groupId>
    <artifactId>frontend-maven-plugin</artifactId>
    <version>1.12.1</version>
    <configuration>
        <workingDirectory>../dynamic-mock-web</workingDirectory>
    </configuration>
    <executions>
        <execution>
            <id>install node and npm</id>
            <goals>
                <goal>install-node-and-npm</goal>
            </goals>
        </execution>
        <execution>
            <id>npm install</id>
            <goals>
                <goal>npm</goal>
            </goals>
        </execution>
        <execution>
            <id>npm build</id>
            <goals>
                <goal>npm</goal>
            </goals>
            <configuration>
                <arguments>run build</arguments>
            </configuration>
        </execution>
    </executions>
</plugin>
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-resources-plugin</artifactId>
    <executions>
        <execution>
            <id>copy-frontend</id>
            <phase>process-resources</phase>
            <goals>
                <goal>copy-resources</goal>
            </goals>
            <configuration>
                <outputDirectory>${basedir}/src/main/resources/static</outputDirectory>
                <resources>
                    <resource>
                        <directory>../dynamic-mock-web/dist</directory>
                    </resource>
                </resources>
            </configuration>
        </execution>
    </executions>
</plugin>
```

#### 4.2.2 独立部署构建

**`.env.production`**:
```
VITE_API_BASE_URL=http://your-backend-server:8088
VITE_APP_TITLE=Dynamic-Mock 管理平台
```

**构建命令**:
```bash
npm run build
```

**部署方式**:
- 使用Nginx部署静态文件
- 配置反向代理到后端服务

**Nginx配置示例**:
```nginx
server {
    listen 80;
    server_name your-domain.com;

    location / {
        root /path/to/dynamic-mock-web/dist;
        index index.html;
        try_files $uri $uri/ /index.html;
    }

    location /dynamic-mock/ {
        proxy_pass http://backend-server:8088;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

---

## 5. 交互细节和用户体验

### 5.1 加载状态
- 所有API请求期间显示Loading状态
- 表格加载时显示骨架屏
- 页面切换时显示进度条

### 5.2 错误处理
- 表单验证错误实时提示
- API错误统一Toast提示
- 网络错误提供重试按钮

### 5.3 操作反馈
- 保存成功/失败Toast提示
- 删除操作前确认对话框
- 危险操作（删除、停用）使用警告色

### 5.4 数据刷新
- 列表页面提供手动刷新按钮
- 关键操作后自动刷新列表
- 支持下拉刷新（移动端）

### 5.5 快捷操作
- 表格行支持双击快速编辑
- 支持键盘快捷键（Ctrl+S保存、Esc取消等）
- 常用筛选条件记住用户选择

---

## 6. 性能优化

### 6.1 代码分割
- 路由懒加载
- 组件按需加载
- 第三方库按需引入

### 6.2 资源优化
- 图片压缩和懒加载
- 使用CDN加载大型库（Monaco Editor）
- Gzip压缩

### 6.3 缓存策略
- API响应缓存（项目列表、函数列表等）
- 静态资源浏览器缓存
- Service Worker离线缓存（可选）

---

## 7. 测试要求

### 7.1 单元测试
- 核心工具函数测试
- 关键组件测试
- Store状态测试

### 7.2 端到端测试
- 登录流程测试
- Mock处理器创建流程测试
- 权限验证测试

---

## 8. 浏览器兼容性
- Chrome（最新版本）
- Firefox（最新版本）
- Safari（最新版本）
- Edge（最新版本）

---

## 9. 国际化（可选）
- 支持中英文切换
- 使用vue-i18n
- 语言配置持久化

---

## 10. 开发里程碑

### Phase 1: 基础架构（1-2周）
- 项目初始化和配置
- 布局框架搭建
- 路由和状态管理配置
- 登录认证功能
- API封装和拦截器

### Phase 2: 核心功能（3-4周）
- 项目管理模块
- Mock处理器管理（列表、详情、编辑）
- 公共组件封装（JSON编辑器、Key-Value编辑器等）

### Phase 3: 扩展功能（2-3周）
- Mock任务管理
- 用户管理
- 函数文档和插件管理
- 快照管理

### Phase 4: 优化和测试（1-2周）
- 性能优化
- 测试覆盖
- Bug修复
- 文档完善

---

## 11. 附录

### 11.1 API接口清单

所有接口都使用POST方法，Base URL为 `/dynamic-mock`

#### 认证相关
- `/auth/login` - 登录
- `/auth/logout` - 登出

#### 用户管理
- `/user/save` - 保存用户
- `/user/del` - 删除用户
- `/user/changePassword` - 修改密码
- `/user/query` - 查询用户列表

#### 项目管理
- `/project/save` - 保存项目
- `/project/query` - 查询项目列表
- `/project/queryOwnAll` - 查询当前用户全部项目
- `/project/del` - 删除项目

#### Mock处理器管理
- `/mock/handler/save` - 保存处理器
- `/mock/handler/find` - 查询处理器详情
- `/mock/handler/query` - 查询处理器列表
- `/mock/handler/del` - 删除处理器
- `/mock/handler/enableStatus` - 启用/停用处理器
- `/mock/handler/queryOwn` - 查询当前用户全部处理器
- `/mock/handler/response/update/:handlerId` - 修改响应
- `/mock/handler/response/status` - 修改响应状态
- `/mock/handler/task/status` - 修改任务状态

#### 快照管理
- `/snapshot/handler/find` - 查询快照详情
- `/snapshot/handler/query` - 查询快照列表
- `/snapshot/handler/recover` - 恢复快照

#### Mock任务管理
- `/mock/task/running/query` - 查询运行中任务
- `/mock/task/running/unregister` - 取消任务
- `/mock/task/running/unregister/batch` - 批量取消任务
- `/mock/task/log/query` - 查询任务日志
- `/mock/task/log/del` - 删除任务日志

#### 函数和插件
- `/mock/handler/function/query` - 查询函数列表
- `/plugin/list` - 查询插件列表

#### 文件管理
- `/file/text/upload` - 上传文本文件
- `/file/text/download` - 下载文本文件

### 11.2 数据格式说明

#### 统一响应格式
```json
{
  "code": 200,
  "msg": "success",
  "data": {}
}
```

#### 分页响应格式
```json
{
  "code": 200,
  "msg": "success",
  "data": {
    "list": [],
    "total": 100
  }
}
```

#### Mock Handler数据结构
参考API文档: `docs/docs/Api.md`

---

## 12. 交付标准

### 12.1 代码质量
- 代码符合ESLint规范
- 关键功能有注释
- 组件和函数命名清晰
- 无Console.log等调试代码

### 12.2 文档
- README.md包含项目启动说明
- 组件使用文档
- API对接说明
- 部署文档

### 12.3 功能完整性
- 所有API都有对应的前端功能
- 所有页面响应式适配
- 所有表单有完整验证
- 所有操作有合理的加载和错误提示

---

## 结束语

本PRD旨在为Dynamic-Mock前端开发提供全面的指导。开发过程中如遇到后端API不匹配、功能需求不明确等问题，请及时沟通调整。

前端设计应始终遵循简约、高效的原则，为用户提供流畅的操作体验。
