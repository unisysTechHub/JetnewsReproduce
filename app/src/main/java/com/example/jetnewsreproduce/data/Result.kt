package com.example.jetnewsreproduce.data

 sealed class Result<out R>
{
    data class Success<out T>(val data : T) : Result<T>()
    class Error(val exception: Exception) : Result<Nothing>()
}

    val Result<*>.succeeded : Boolean
        get() = this is Result.Success && data != null

fun<T> Result<T>.successor(fallback : T) : T
{
    return (this as? Result.Success<T>)?.data ?: fallback
}