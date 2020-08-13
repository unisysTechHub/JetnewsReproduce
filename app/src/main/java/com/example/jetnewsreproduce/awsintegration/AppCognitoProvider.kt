package com.example.jetnewsreproduce.awsintegration

import android.content.Context
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.regions.Regions

object AppCognito
{
    private val  aws_account_id = "124883821192"
    private val aws_identity_pool_id = "us-east-1:b8ccdf94-781c-4c95-8617-373af459ff77"
    private val aws_unauthrole_arn = "arn:aws:iam::124883821192:role/amplify-jetnewsreproduce-dev-173257-authRole"
    private val aws_authrole_arn = "arn:aws:iam::124883821192:role/amplify-jetnewsreproduce-dev-173257-unauthRole"

    fun provider(context : Context) : CognitoCachingCredentialsProvider
    {
        return      CognitoCachingCredentialsProvider(context, aws_account_id, aws_identity_pool_id, aws_unauthrole_arn , aws_authrole_arn, Regions.US_EAST_1)

    }



}