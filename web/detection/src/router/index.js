import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from "@/views/Home.vue";

// 解决导航栏或者底部导航tabBar中的vue-router在3.0版本以上频繁点击菜单报错的问题。
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push (location) {
    return originalPush.call(this, location).catch(err => err)
}

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'Manager',
        component: () => import('../views/login.vue'),
        redirect: '/login',  // 重定向到主页
    },
    { path: '/login', name: 'Login', meta: { name: '登录' }, component: () => import('../views/login.vue') },
    {
        path: '/home',
        name: 'Home',
        component: Home,
        children: [
            { path: '/picture_detection', name: 'Picture_detection', meta: { name: '图片检测' }, component: () => import('../components/picture_detection.vue') },
            { path: '/dashboard', name: 'Dashboard', meta: { name: '概要信息' }, component: () => import('../components/dashboard.vue') },
            { path: '/info', name: 'Info', meta: { name: '历史检测' }, component: () => import('../components/info.vue') },
            { path: '/charts', name: 'Charts', meta: { name: '检测报表' }, component: () => import('../components/charts.vue') },
            { path: '/warning', name: 'Warning', meta: { name: '异常管理' }, component: () => import('../components/warning.vue') },
            { path: '/log', name: 'Log', meta: { name: '日志管理' }, component: () => import('../components/log.vue') },
            { path: '/apimanager', name: 'apiManager', meta: { name: 'api管理' }, component: () => import('../components/apimanager.vue') },
            { path: '/pwdmanager', name: 'pwdManager', meta: { name: '操作秘钥管理' }, component: () => import('../components/pwdmanager.vue') },
            { path: '/camera', name: 'Camera', meta: { name: '摄像头监控' }, component: () => import('../components/camera.vue') },
            { path: '/person', name: 'Person', meta: { name: '个人中心' }, component: () => import('../components/person.vue') },
            { path: '/password', name: 'Password', meta: { name: '修改密码' }, component: () => import('../components/password.vue') },
        ]
    }
]

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
})

router.beforeEach((to, from, next) => {
    // to 是到达的路由信息
    // from 是开源的路由信息
    // next 是帮助我们跳转路由的函数
    //let adminPaths = ['/111']
    let user = JSON.parse(localStorage.getItem('useradmin') || '{}')
    next()
    if (user === null) {
        // 如果当前登录的用户不是管理员，然后当前的到达的路径是管理员才有权限访问的路径，那这个时候我就让用户去到一个没有权限的页面，不让他访问实际的页面
        next('/403')
    } else {
        next()
    }
})

export default router
