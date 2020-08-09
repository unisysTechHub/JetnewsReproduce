package com.example.jetnewsreproduce.data

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.os.Handler
import android.os.Looper
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

interface AppContainer
{
    val postsRepository : PostsRepository
}

class AppContainerImpl(applicationContext : Context) : AppContainer
{
    private val executorService : ExecutorService by lazy {
        Executors.newFixedThreadPool(4)
    }

    private val mainThreadHandler : Handler by lazy {
        Handler(Looper.getMainLooper())
    }
    override val postsRepository: PostsRepository by lazy {
                                    FakePostsRepository(executorService = executorService,
                                    resultThreadHandler = mainThreadHandler,
                                    resources = applicationContext.resources)}

}
