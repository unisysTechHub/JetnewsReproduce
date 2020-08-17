package com.example.jetnewsreproduce.awsintegration

import android.content.Context
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.regions.Regions
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

object AppCognito
{
    private const val  aws_account_id = "124883821192"
    private const val aws_identity_pool_id = "us-east-1:b8ccdf94-781c-4c95-8617-373af459ff77"
    private const val aws_unauthrole_arn = "arn:aws:iam::124883821192:role/amplify-jetnewsreproduce-dev-173257-authRole"
    private const val aws_authrole_arn = "arn:aws:iam::124883821192:role/amplify-jetnewsreproduce-dev-173257-unauthRole"

    fun provider(context : Context) : CognitoCachingCredentialsProvider
    {
        val deferred =  GlobalScope.async {CognitoCachingCredentialsProvider(context, aws_account_id, aws_identity_pool_id, aws_unauthrole_arn , aws_authrole_arn, Regions.US_EAST_1)  }

            return runBlocking { deferred.await() }
    }



}