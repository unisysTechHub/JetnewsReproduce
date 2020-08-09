package com.example.jetnewsreproduce.ui.home

import androidx.compose.Composable
import androidx.compose.launchInComposition
import androidx.compose.remember
import androidx.compose.stateFor
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.foundation.Box
import androidx.ui.foundation.Text
import androidx.ui.foundation.clickable
import androidx.ui.foundation.contentColor
import androidx.ui.layout.*
import androidx.ui.material.*
import androidx.ui.unit.dp
import com.example.jetnewsreproduce.data.PostsRepository
import com.example.jetnewsreproduce.model.Post
import com.example.jetnewsreproduce.ui.snackbarAction
import com.example.jetnewsreproduce.ui.state.*
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(postsRepository: PostsRepository, scaffoldState: ScaffoldState = remember  { ScaffoldState()})
{
    Scaffold(scaffoldState = scaffoldState,
            topAppBar = {
                TopAppBar(title = { Text(text = "JetNews") })

            },
            bodyContent = { modfier ->
                HomeScreenContent(postsRepository = postsRepository, modifier = modfier)
            }

    )
}

@Composable
fun HomeScreenContent(postsRepository: PostsRepository, modifier: Modifier)
{
    var (postsState, refreshPosts) = RefreshableUIStateFrom(repositoryCall = postsRepository :: getPosts)
    if(postsState.loading && !postsState.refreshing)
        LoadingHomeScreen()
    else
    {
           HomeScreenBodyWrapper(
                    modifier = modifier,
                    state = postsState,
                    onErrorAction = {
                        refreshPosts()
                    }
            )
    }
}

@Composable
fun HomeScreenBodyWrapper(modifier: Modifier, state : RefreshableUIState<List<Post>>, onErrorAction: () -> Unit )
{
                    val (showSnackbarError, updateShowSnackbarError)= stateFor(state){ state is RefreshableUIState.Error}
    Stack(modifier = Modifier.fillMaxSize()) {
        state.currentData?.let {posts ->
            HomeScreenBody(posts = posts , modifier = modifier)
        }
        ErrorSnackbar(
                showError = showSnackbarError,
                onErrorAction = onErrorAction,
                onDismiss = { updateShowSnackbarError(false) },
                modifier = Modifier.gravity(Alignment.BottomCenter)
        )

    }
}
@Composable
private fun HomeScreenBody(
        posts: List<Post>,
        modifier : Modifier

 ) {
    val postTop = posts[3]

        HomeScreenTopSection(postTop)

}

@Composable
fun HomeScreenTopSection(post : Post)
{
    Column() {
        ProvideEmphasis(emphasis = EmphasisAmbient.current.high) {
            Text(modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp),
                text = "Top stories for you ",
                style = MaterialTheme.typography.subtitle1

            )
           PostCardTop(
            post = post,
            modifier = Modifier.clickable(onClick = {  })
    )


        }

    }

    HomeScreenDivider()
}

@Composable
private fun HomeScreenDivider() {
    Divider(
            modifier = Modifier.padding(horizontal = 14.dp),
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.08f)
    )
}
@Composable
fun ErrorSnackbar(
        showError: Boolean,
        modifier: Modifier = Modifier,
        onErrorAction: () -> Unit = { },
        onDismiss: () -> Unit = { }
) {
    if (showError) {
        // Make Snackbar disappear after 5 seconds if the user hasn't interacted with it
        launchInComposition {
            delay(timeMillis = 5000L)
            onDismiss()
        }

        Snackbar(
                modifier = modifier.padding(16.dp),
                text = { Text("Can't update latest news") },
                action = {
                    TextButton(
                            onClick = {
                                onErrorAction()
                                onDismiss()
                            },
                            contentColor = contentColor()
                    ) {
                        Text(
                                text = "RETRY",
                                color = MaterialTheme.colors.snackbarAction
                        )
                    }
                }
        )
    }
}
@Composable
fun LoadingHomeScreen()
{
    Box(modifier = Modifier.fillMaxSize().wrapContentSize(align = Alignment.Center))
    {
        CircularProgressIndicator()
    }

}

