package com.example.jetnewsreproduce.ui.state

import androidx.compose.*
import com.example.jetnewsreproduce.data.Result

sealed class RefreshableUIState<out T>
{
    data class Success<T>(val data :T?,val loading : Boolean) : RefreshableUIState<T>()
    data class Error<T>(val exception: Exception, val prevData : T?) : RefreshableUIState<T>()
}

data class RefreshableUIStateHandler<out T>(val state : RefreshableUIState<T>, val refreshAction : () -> Unit)
@Composable
fun<T> RefreshableUIStateFrom( repositoryCall: RepositoryCall<T>) : RefreshableUIStateHandler<T>
{
    var state by state<RefreshableUIState<T>> {
        RefreshableUIState.Success(data = null, loading = true)
    }

    var refresh = {
                state = RefreshableUIState.Success(data = state.currentData,loading = true)
            repositoryCall{
                result -> state = when(result){
                                  is Result.Success -> RefreshableUIState.Success(data = result.data,loading = false)
                                  is Result.Error -> RefreshableUIState.Error(exception = result.exception, prevData = state.currentData)
            }


            }
          }
    onActive {
        refresh()
    }

    return RefreshableUIStateHandler(state,refresh)
}

 val <T> RefreshableUIState<T>.loading : Boolean
    get() = this is RefreshableUIState.Success && this.data == null && this.loading

val <T> RefreshableUIState<T>.refreshing : Boolean
    get() = this is RefreshableUIState.Success && this.data != null && this.loading

val <T> RefreshableUIState<T>.currentData : T?
        get() = when(this)
                {
                    is RefreshableUIState.Success -> this.data
                    is RefreshableUIState.Error -> this.prevData
                }
