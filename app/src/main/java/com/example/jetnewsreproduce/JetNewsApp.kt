package com.example.jetnewsreproduce

import androidx.compose.Composable
import com.example.jetnewsreproduce.data.AppContainer
import com.example.jetnewsreproduce.data.PostsRepository
import com.example.jetnewsreproduce.ui.JetnewsReproduceTheme
import com.example.jetnewsreproduce.ui.home.HomeScreen

@Composable
fun jetNewsApp(
    appContainer: AppContainer,
)
{
    JetnewsReproduceTheme() {
            AppContent(postsRepository = appContainer.postsRepository)
    }
}

@Composable
fun AppContent(postsRepository: PostsRepository)
{
    HomeScreen(postsRepository = postsRepository)
}


