package com.example.jetnewsreproduce

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.ui.core.setContent
import androidx.ui.foundation.Text
import androidx.ui.tooling.preview.Preview
import com.amplifyframework.auth.result.AuthSignInResult
import com.amplifyframework.core.Amplify
import com.example.jetnewsreproduce.dynamodbmapper.JetNewsDynamoDB
import com.example.jetnewsreproduce.dynamodbmapper.PostsTableItem
import com.example.jetnewsreproduce.ui.JetnewsReproduceTheme


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appContainer = (application as JetNewsReproduceApplication).container
        setContent {
            jetNewsApp(appContainer = appContainer)
        }
//        Amplify.Auth.signUp(
//            "clouduser1",
//            "Password123",
//            AuthSignUpOptions.builder().userAttribute(AuthUserAttributeKey.email(), "ramesh5penta@gmail.com").build(),
//            { result -> Log.i("AuthQuickStart", "Result: $result") },
//            { error -> Log.e("AuthQuickStart", "Sign up failed", error) }
//        )
//        Amplify.Auth.confirmSignUp(
//            "clouduser1",
//            "987176",
//            { result -> Log.i("AuthQuickstart", if (result.isSignUpComplete) "Confirm signUp succeeded" else "Confirm sign up not complete") },
//            { error -> Log.e("AuthQuickstart", error.toString()) }
//        )

        Amplify.Auth.signIn(
                "clouduser1",
                "Password123",
                { result ->

                    dbaccess(result = result)
                    Log.i(
                            "AuthQuickstart",
                            if (result.isSignInComplete) "Sign in succeeded" else "Sign in not complete"
                    )
                },
                { error -> Log.e("AuthQuickstart", error.toString()) }
        )
    }

    fun dbaccess(result: AuthSignInResult)
    {

//       var credentialsProvider = AppCognito.provider(this)
//
//      var dbClient  = AmazonDynamoDBClient(credentialsProvider)
//
//        dbClient.setRegion(Region.getRegion(Regions.US_EAST_1))
//
//        var dbTable = Table.loadTable(dbClient, "Posts")
//
//        Log.d("@Ramesh", dbTable.attributes[0].attributeName)


        val itemList = JetNewsDynamoDB.getTableItem(PostsTableItem::class, "post1")
        Log.d("@Ramesh", itemList.get(0).subtitle!!)


    }



}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JetnewsReproduceTheme {
        Greeting("Android")
    }
}

