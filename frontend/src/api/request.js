import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'

const request = axios.create({
    baseURL: '/api',
    timeout: 5000
})

// Request Interceptor
request.interceptors.request.use(config => {
    const token = localStorage.getItem('token')
    if (token) {
        config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
}, error => {
    return Promise.reject(error)
})

// Response Interceptor
request.interceptors.response.use(response => {
    const res = response.data
    if (res.code === 200) {
        return res.data
    } else {
        ElMessage.error(res.message || 'Error')
        return Promise.reject(new Error(res.message || 'Error'))
    }
}, error => {
    if (error.response && error.response.status === 401) {
        ElMessage.error('Please login first')
        localStorage.removeItem('token')
        router.push('/login')
    } else if (error.response && error.response.status === 429) {
        ElMessage.error(error.response.data.message || '请求过于频繁，请稍后再试')
    } else {
        ElMessage.error(error.message || 'Network Error')
    }
    return Promise.reject(error)
})

export default request
