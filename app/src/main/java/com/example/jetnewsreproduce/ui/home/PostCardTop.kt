package com.example.jetnewsreproduce.ui.home

import androidx.compose.Composable
import androidx.ui.core.ContentScale
import androidx.ui.core.Modifier
import androidx.ui.core.clip
import androidx.ui.foundation.Image
import androidx.ui.foundation.Text
import androidx.ui.layout.*
import androidx.ui.material.EmphasisAmbient
import androidx.ui.material.MaterialTheme
import androidx.ui.material.ProvideEmphasis
import androidx.ui.unit.dp
import com.example.jetnewsreproduce.model.Post

@Composable
fun PostCardTop(post : Post , modifier: Modifier)
{
    val typography = MaterialTheme.typography
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        post.image?.let {image ->
                        val imageModfier = Modifier.preferredHeightIn(180.dp).
                            fillMaxWidth().
                            clip(shape = MaterialTheme.shapes.medium)
                        Image(image,imageModfier, contentScale = ContentScale.Crop)
            }

    }
    Spacer(modifier = Modifier.preferredHeight(16.dp))
    val emphasisLevels = EmphasisAmbient.current
    ProvideEmphasis(emphasis = emphasisLevels.high) {
        Text(text = post.title,
             style = typography.h6
             )
        Text(text = post.metadata.author.name,
            style =typography.body2
             )
     ProvideEmphasis(emphasis = emphasisLevels.medium) {
         Text(text = "${post.metadata.date} - ${post.metadata.readTimeMinutes} min read",
              style = typography.body2)
     }   
    }
}