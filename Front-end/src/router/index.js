import { createRouter, createWebHistory } from "vue-router";
import { getCookie, getSession, setSession } from '@/utils/storage.js'
import { usePost } from '@/api/DataAsk.js'
import { api } from '@/utils/request.js'
import { useAppData } from '@/store/AppData.js'

const routes = [
    {
        path: '/',
        name: 'Welcome',
        component: () => import('@/view/introduce.vue')
    },
    {
        path: '/home',
        name: 'home',
        component: () => import('@/view/layout.vue'),
        // 导航守卫
        beforeEnter: (to, from, next) => {
            if (getSession('__user__', false)) {
                const useAppDataStore = useAppData()
                if (JSON.stringify(useAppDataStore.testData) == '{}') {
                    usePost(api.GetServerMySQL, { Account: getSession('__user__', false) }).then(res => {
                        const resp = res.data
                        useAppDataStore.saveData({
                            UserName: resp.UserName,
                            NewUser: resp.NewUser,
                            Avatar: resp.DefaultAvatar,
                            Account: resp.Account,
                            DefaultAvatar: resp.DefaultAvatar,
                            TryCount: resp.TryCount,
                            Disabled: resp.Disabled,
                            PaperPublish: JSON.parse(resp.Profile).PaperPublish,
                            PaperCollect: JSON.parse(resp.Profile).PaperCollect,
                            PaperDesign: JSON.parse(resp.Profile).PaperDesign
                        })
                        next()
                    }, rej => {
                        ElMessage.error('拉取用户信息失败')
                        next('/login')
                    })
                }
                next()
            } else {
                next('/login')
            }
        },
        redirect: '/home/publish',
        children: [
            {
                path: 'information',
                name: 'information',
                component: () => import('@/components/information.vue')
            },
            {
                path: 'publish',
                name: 'publish',
                component: () => import('@/components/publish.vue')
            },
            {
                path: 'collect',
                name: 'collect',
                component: () => import('@/components/collect.vue')
            },
            {
                path: 'design',
                name: 'design',
                component: () => import('@/components/design.vue')
            },
        ]
    },
    {
        path: '/login',
        name: 'login',
        component: () => import('@/view/loginHome.vue'),
        // 导航守卫
        beforeEnter: (to, from, next) => {
            if (getCookie('user')) {
                const useAppDataStore = useAppData()
                usePost(api.GetServerMySQL, { Account: getCookie('user') }).then(res => {
                    const resp = res.data
                    useAppDataStore.saveData({
                        UserName: resp.UserName,
                        NewUser: resp.NewUser,
                        Avatar: resp.DefaultAvatar,
                        Account: resp.Account,
                        DefaultAvatar: resp.DefaultAvatar,
                        TryCount: resp.TryCount,
                        Disabled: resp.Disabled,
                        PaperPublish: JSON.parse(resp.Profile).PaperPublish,
                        PaperCollect: JSON.parse(resp.Profile).PaperCollect,
                        PaperDesign: JSON.parse(resp.Profile).PaperDesign
                    })
                    setSession('__user__', resp.Account)
                    next('/home')
                }, rej => {
                    ElMessage.error('拉取用户信息失败')
                    next('/login')
                })
            } else {
                next()
            }
        },
    },
    {
        path: '/answer',
        name: 'answer',
        component: () => import('@/view/answer.vue')
    },
    {
        path: '/:catchALL(.*)',
        name: 'not-found',
        redirect: '/404'
    },
    {
        path: '/404',
        name: '404',
        // component: () => import('@/views/404.vue')
    },
    {
        path: '/test',
        name: 'test',
        component: () => import('@/ui/loading.vue')
    },

]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router;