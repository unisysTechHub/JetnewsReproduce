package com.example.jetnewsreproduce.dynamodbmapper

import android.content.Context
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.example.jetnewsreproduce.awsintegration.AppCognito
import kotlin.reflect.KClass

@DynamoDBTable(tableName = "Posts")
class PostsTableItem
{
    @DynamoDBHashKey(attributeName = "postid")
     var postid : String? =  null
        get() = field
        set(value) {field =value}


}

@Suppress("UNCHECKED_CAST")
class JetNewsDynamoDB {

    companion object {
        private var jetnewsDynamoDB :JetNewsDynamoDB? = null
        private var client: AmazonDynamoDB? = null
        fun getInstnace() : JetNewsDynamoDB =
                jetnewsDynamoDB ?: JetNewsDynamoDB()
            }

    fun configure(context: Context)
    { client = AmazonDynamoDBClient(AppCognito.provider(context = context))

    }

    fun getClient() = client?.apply { setRegion(Region.getRegion(Regions.US_EAST_1))}

    fun mapper() = DynamoDBMapper.builder().dynamoDBClient(getClient()).build()

    fun getTableItem( tableItem: KClass<PostsTableItem>,  value : String) : List<PostsTableItem>
    {
        val tableItemIstance = tableItem.java.newInstance() .apply { postid = value }
        val queryExpression = DynamoDBQueryExpression<PostsTableItem>()
                .withHashKeyValues(tableItemIstance)
        return mapper().query(PostsTableItem::class.java,queryExpression)

    }

}