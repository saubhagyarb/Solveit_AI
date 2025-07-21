package com.saubh.solveitai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.saubh.solveitai.ui.SolveItApp
import com.saubh.solveitai.ui.theme.SolveItAITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SolveItAITheme {
                SolveItApp()
            }
        }
    }
}

@Preview
@Composable
fun SolveItAppPreview(){
    SolveItAITheme {
        SolveItApp()
    }
}