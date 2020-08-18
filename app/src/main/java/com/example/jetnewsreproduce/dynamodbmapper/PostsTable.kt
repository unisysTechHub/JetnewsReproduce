package com.example.jetnewsreproduce.dynamodbmapper

import android.content.Context
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.*
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.example.jetnewsreproduce.awsintegration.AppCognito
import com.example.jetnewsreproduce.model.Post
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.util.concurrent.atomic.AtomicInteger
import kotlin.reflect.KClass

@DynamoDBTable(tableName = "Posts")
 class PostsTableItem
{
    @DynamoDBHashKey(attributeName = "postid")
     var postid : String? =  null

    @DynamoDBAttribute(attributeName = "id")
     var id : String? = null

    @DynamoDBAttribute(attributeName = "title")
    var title : String? = null

    @DynamoDBAttribute(attributeName = "subtitle")
    var subtitle : String? = null

    @DynamoDBAttribute(attributeName = "url")
    var url : String? = null

    @DynamoDBAttribute(attributeName = "publication")
    var  publication : Publication? = null

    @DynamoDBAttribute(attributeName = "metadata")
    var  metadata : Metadata? = null


    @DynamoDBAttribute(attributeName = "paragraphs")
    var paragraphs : List<Paragraph>? = null



}
@DynamoDBDocument
class Metadata {
    @DynamoDBAttribute(attributeName = "author")
     var author: PostAuthor? = null

    @DynamoDBAttribute(attributeName = "date")
    var date: String? = null

    @DynamoDBAttribute(attributeName = "readTimeMinutes")
    var readTimeMinutes: Int? = null

}

@DynamoDBDocument
data class PostAuthor(
        @DynamoDBAttribute(attributeName = "name")
        val name: String,
        @DynamoDBAttribute(attributeName = "url")
        val url: String? = null
)

@DynamoDBDocument
class Publication(
        @DynamoDBAttribute(attributeName = "name")
        val name: String,
        @DynamoDBAttribute(attributeName = "logoUrl")
        val logoUrl: String
)

@DynamoDBDocument
class Paragraph {
    @DynamoDBAttribute(attributeName = "type")
    var type: com.example.jetnewsreproduce.dynamodbmapper.ParagraphType? = null

    @DynamoDBAttribute(attributeName = "text")
    var text: String? = null

    @DynamoDBAttribute(attributeName = "markups")
    var markups: List<Markup> = emptyList()
}
@DynamoDBDocument
enum class ParagraphType {
    Title,
    Caption,
    Header,
    Subhead,
    Text,
    CodeBlock,
    Quote,
    Bullet,
}
@DynamoDBDocument
 class Markup(
        @DynamoDBAttribute(attributeName = "type")
        var type: MarkupType,
        @DynamoDBAttribute(attributeName = "start")
        var start: Int,
        @DynamoDBAttribute(attributeName = "end")
        var end: Int,
        @DynamoDBAttribute(attributeName = "href")
        var href: String? = null
)
@DynamoDBDocument
enum class MarkupType {
    Link,
    Code,
    Italic,
    Bold,
}
@Suppress("UNCHECKED_CAST")
object JetNewsDynamoDB {
    private var client: AmazonDynamoDB? = null

    fun configure(context: Context)
    {
        client = AmazonDynamoDBClient(AppCognito.provider(context = context))

    }

    private fun getClient() = client?.apply { setRegion(Region.getRegion(Regions.US_EAST_1))}

    fun mapper() = DynamoDBMapper.builder().dynamoDBClient(getClient()).build()

    fun getTableItem( tableItem: KClass<PostsTableItem>,  value : String) : List<PostsTableItem>
    {
        val tableItemIstance = tableItem.java.newInstance() .apply { postid = value }
        val queryExpression = DynamoDBQueryExpression<PostsTableItem>()
                .withHashKeyValues(tableItemIstance)
        return mapper().query(PostsTableItem::class.java,queryExpression)

    }

    suspend fun saveTableItem(tableItem: PostsTableItem)
    {
            mapper().save(tableItem)
    }

}
fun markupsDB(markups : List<com.example.jetnewsreproduce.model.Markup>)

        =    List(markups.size){Markup(MarkupType.valueOf(markups[it].type.toString()),markups[it].start, markups[it].end,markups[it].href)}

fun loadJetNewsPostsTable(posts : List<Post>)
{   val count : AtomicInteger = AtomicInteger(0)
    posts.forEach { post ->

          val postsTableItem = PostsTableItem()
            postsTableItem.postid = "post" +  count.incrementAndGet()
            postsTableItem.id = post.id
            val postAuthor = PostAuthor(post.metadata.author.name,url = post.metadata.author.url)
            val metadata = Metadata().apply { this.author =postAuthor
                                              this.date = post.metadata.date
                                              this.readTimeMinutes = post.metadata.readTimeMinutes
                                            }
            postsTableItem.metadata = metadata
            postsTableItem.paragraphs = List<Paragraph>(post.paragraphs.size){
                val markups = markupsDB(post.paragraphs[it].markups)
                Paragraph().apply {  type = ParagraphType.valueOf(post.paragraphs[it].type.toString())
                    text = post.paragraphs[it].text
                    this.markups = markups}

            }

            postsTableItem.publication = Publication(post.publication!!.name,post.publication.logoUrl)
            postsTableItem.title = post.title
            postsTableItem.subtitle = post.subtitle
            postsTableItem.url = post.url
            GlobalScope.async { JetNewsDynamoDB.saveTableItem(postsTableItem) }
      }
}