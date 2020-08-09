package com.example.jetnewsreproduce

import android.app.Application
import com.example.jetnewsreproduce.data.AppContainer
import com.example.jetnewsreproduce.data.AppContainerImpl

class JetNewsReproduceApplication : Application()
{
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppContainerImpl(this)
    }
}