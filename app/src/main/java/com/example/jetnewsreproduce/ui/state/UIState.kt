package com.example.jetnewsreproduce.ui.state

import androidx.compose.*
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
    var state  by state<UIState<T>>{ UIState.loading }

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


