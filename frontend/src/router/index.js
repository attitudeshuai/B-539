import { createRouter, createWebHistory } from 'vue-router'
import LayoutPublic from '../views/public/Layout.vue'
import LayoutAdmin from '../views/admin/Layout.vue'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            component: LayoutPublic,
            children: [
                {
                    path: '',
                    name: 'Home',
                    component: () => import('../views/public/Home.vue')
                },
                {
                    path: 'article/:id',
                    name: 'ArticleDetail',
                    component: () => import('../views/public/ArticleDetail.vue')
                }
            ]
        },
        {
            path: '/login',
            name: 'Login',
            component: () => import('../views/admin/Login.vue')
        },
        {
            path: '/register',
            name: 'Register',
            component: () => import('../views/admin/Register.vue')
        },
        {
            path: '/admin',
            component: LayoutAdmin,
            meta: { requiresAuth: true },
            children: [
                {
                    path: 'article',
                    name: 'AdminArticle',
                    component: () => import('../views/admin/ArticleManage.vue')
                },
                {
                    path: 'article/edit/:id?', // Optional ID for edit
                    name: 'AdminArticleEdit',
                    component: () => import('../views/admin/ArticleEdit.vue')
                },
                {
                    path: 'category',
                    name: 'AdminCategory',
                    component: () => import('../views/admin/CategoryManage.vue')
                },
                {
                    path: 'tag',
                    name: 'AdminTag',
                    component: () => import('../views/admin/TagManage.vue')
                },
                {
                    path: 'user',
                    name: 'AdminUser',
                    component: () => import('../views/admin/UserManage.vue')
                },
                {
                    path: 'comment',
                    name: 'AdminComment',
                    component: () => import('../views/admin/CommentManage.vue')
                }
            ]
        }
    ]
})

router.beforeEach((to, from, next) => {
    const token = localStorage.getItem('token')
    if (to.meta.requiresAuth && !token) {
        next('/login')
    } else {
        next()
    }
})

export default router
