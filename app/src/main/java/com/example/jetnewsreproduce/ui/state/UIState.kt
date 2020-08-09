package com.example.jetnewsreproduce.ui.state

import androidx.compose.Composable
import androidx.compose.onActive
import androidx.compose.state
import com.example.jetnewsreproduce.data.Result

typealias RepositoryCall<T> = ( (Result<T>) -> Unit ) -> Unit
sealed class UIState<out T>
{
    object loading : UIState<Nothing>()
    data class Success<T>(val data : T?, val loading : Boolean) : UIState<T>()
    class Error(exception: Exception) : UIState<Nothing>()
}
@Composable
    fun<T> UIStateFrom( repositoryCall: RepositoryCall<T> ) : UIState<T> {
    var state : UIState<T>   = state{ UIState.loading } as UIState<T>

    onActive(){
        repositoryCall{
            result -> state = when(result){
                              is Result.Success<*> -> UIState.Success<T>(result.data as T,loading = false)
                               is Result.Error  ->    UIState.Error(result.exception)

        }

        }
    }


    return state

}


