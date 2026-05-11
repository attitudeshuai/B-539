import request from './request'

export const login = (data) => request.post('/user/login', data)
export const register = (data) => request.post('/user/register', data)
export const getUserInfo = () => request.get('/user/info')

export const getArticlePage = (params) => request.get('/article/page', { params })
export const getArticleDetail = (id) => request.get(`/article/${id}`)
export const saveArticle = (data) => request.post('/article/save', data)
export const updateArticle = (data) => request.put('/article/update', data)
export const deleteArticle = (id) => request.delete(`/article/delete/${id}`)

export const getCategoryList = () => request.get('/category/list')
export const saveCategory = (data) => request.post('/category/save', data)
export const updateCategory = (data) => request.put('/category/update', data)
export const deleteCategory = (id) => request.delete(`/category/delete/${id}`)

export const getTagList = () => request.get('/tag/list')
export const saveTag = (data) => request.post('/tag/save', data)
export const updateTag = (data) => request.put('/tag/update', data)
export const deleteTag = (id) => request.delete(`/tag/delete/${id}`)

export const getUserPage = (params) => request.get('/user/page', { params })
export const deleteUser = (id) => request.delete(`/user/delete/${id}`)

export const addComment = (data) => request.post('/comment/add', data)
export const getArticleComments = (articleId) => request.get(`/comment/article/${articleId}`)
export const getCommentPage = (params) => request.get('/comment/page', { params })
export const approveComment = (id) => request.put(`/comment/approve/${id}`)
export const rejectComment = (id) => request.put(`/comment/reject/${id}`)
