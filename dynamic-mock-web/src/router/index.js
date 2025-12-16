import { createRouter, createWebHistory } from 'vue-router'
import { getUserInfo } from '@/utils/auth'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: () => import('@/views/Login.vue'),
      meta: { title: '登录' }
    },
    {
      path: '/',
      component: () => import('@/components/Layout/MainLayout.vue'),
      redirect: '/mock-handler/list',
      children: [
        {
          path: 'project/list',
          name: 'ProjectList',
          component: () => import('@/views/Project/ProjectList.vue'),
          meta: { title: '项目管理', requireAdmin: true }
        },
        {
          path: 'mock-handler/list',
          name: 'MockHandlerList',
          component: () => import('@/views/MockHandler/MockHandlerList.vue'),
          meta: { title: 'Mock处理器列表' }
        },
        {
          path: 'mock-handler/edit/:id?',
          name: 'MockHandlerEdit',
          component: () => import('@/views/MockHandler/MockHandlerEdit.vue'),
          meta: { title: '编辑Mock处理器' }
        },
        {
          path: 'mock-handler/detail/:id',
          name: 'MockHandlerDetail',
          component: () => import('@/views/MockHandler/MockHandlerDetail.vue'),
          meta: { title: 'Mock处理器详情' }
        },
        {
          path: 'mock-handler/snapshot/:handlerId',
          name: 'SnapshotList',
          component: () => import('@/views/MockHandler/SnapshotList.vue'),
          meta: { title: '快照管理' }
        },
        {
          path: 'mock-task/running',
          name: 'RunningTask',
          component: () => import('@/views/MockTask/RunningTask.vue'),
          meta: { title: '运行中任务' }
        },
        {
          path: 'mock-task/log',
          name: 'TaskLog',
          component: () => import('@/views/MockTask/TaskLog.vue'),
          meta: { title: '任务日志' }
        },
        {
          path: 'user/list',
          name: 'UserList',
          component: () => import('@/views/User/UserList.vue'),
          meta: { title: '用户管理', requireAdmin: true }
        },
        {
          path: 'user/change-password',
          name: 'ChangePassword',
          component: () => import('@/views/User/ChangePassword.vue'),
          meta: { title: '修改密码' }
        },
        {
          path: 'function/list',
          name: 'FunctionList',
          component: () => import('@/views/Function/FunctionList.vue'),
          meta: { title: '函数文档' }
        },
        {
          path: 'plugin/list',
          name: 'PluginList',
          component: () => import('@/views/Plugin/PluginList.vue'),
          meta: { title: '插件管理' }
        }
      ]
    }
  ]
})

// 路由守卫
router.beforeEach((to, from, next) => {
  // 设置页面标题
  document.title = to.meta.title
    ? `${to.meta.title} - ${import.meta.env.VITE_APP_TITLE}`
    : import.meta.env.VITE_APP_TITLE

  // 登录验证（基于cookie的认证，检查本地是否有用户信息）
  const userInfo = getUserInfo()
  if (to.path !== '/login' && !userInfo) {
    next('/login')
  } else if (to.path === '/login' && userInfo) {
    next('/')
  } else {
    next()
  }
})

export default router
