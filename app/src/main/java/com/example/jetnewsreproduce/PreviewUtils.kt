package com.example.jetnewsreproduce

import androidx.compose.Composable
import androidx.ui.material.Surface
import com.example.jetnewsreproduce.ui.JetnewsReproduceTheme

@Composable
internal fun ThemedPreview(
    darkTheme: Boolean = false,
    children: @Composable() () -> Unit
) {
    JetnewsReproduceTheme(darkTheme = darkTheme) {
        Surface {
            children()
        }
    }
}
