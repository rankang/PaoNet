package com.ditclear.paonet.model.repository

import androidx.room.EmptyResultSetException
import com.ditclear.paonet.model.data.*
import com.ditclear.paonet.model.local.dao.ArticleDao
import com.ditclear.paonet.model.remote.api.PaoService
import io.reactivex.Single


/**
 * 页面描述：PaoRepository
 *
 * Created by ditclear on 2017/10/30.
 */
class PaoRepository(private val remote: PaoService, private val local: ArticleDao) {


    /**
     * 轮播图数据
     */
    fun getSlider() = remote.getSlider()

    // 文章置顶
    fun getTopList() = remote.getTopList()

    /**
     * 文章列表
     */
    fun getArticleList(page: Int, tid: Int)= remote.getArticleList( tid,page)


    /**
     * 文章详情
     */
    fun getArticle(articleId: Int): Single<Article> = local.getArticleById(articleId).onErrorResumeNext {
        if (it is EmptyResultSetException) {
            remote.getArticleDetail(articleId).doOnSuccess { t -> t?.let { local.insertArticle(it) } }
        } else throw it
    }

    /**
     * 代码列表
     */
    fun getCodeList(category: Int? = null, page: Int) = remote.getCodeList(page,category)

    /**
     * 代码详情
     */
    fun getCodeDetail(id: Int): Single<Article> = local.getArticleById(id).onErrorResumeNext {
        if (it is EmptyResultSetException) {
            remote.getCodeDetail(id)
                    .doOnSuccess { t: Article? -> t?.let { local.insertArticle(it) } }
        } else throw it
    }

    /**
     * 文章搜索
     * @param key 关键词
     * @param p 分页数
     */
    fun getSearchArticles(p: Int, key: String) = remote.getSearchArticles(p, key)

    /**
     * 代码搜索
     * @param p 分页数
     * @param cate 代码分类
     * @param key 关键词
     */
    fun getSearchCode(p: Int, cate: Int? = null, key: String): Single<ArticleList> = remote.getSearchCode(p, cate, key)

    /**
     * 获取热门搜索
     */
    fun getHotSearch()= remote.getHotSearch()

    fun getCodeCategory() = remote.getCodeCategory()
}