package com.djx.kmpappsubstrate

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.djx.kmpappsubstrate.router.Router


@Composable
fun App() {
    MaterialTheme {
        Surface {
            Router()
        }
    }
}