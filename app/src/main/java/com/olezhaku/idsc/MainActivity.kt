package com.olezhaku.idsc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.olezhaku.idsc.ui.components.Input
import com.olezhaku.idsc.ui.components.InputType
import com.olezhaku.idsc.ui.theme.IDsCTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IDsCTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val focusManager = LocalFocusManager.current
    var searchText by remember { mutableStateOf("") }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Input(
                value = searchText,
                onValueChange = { searchText = it },
                clearFocus = { focusManager.clearFocus() },
                type = InputType.Search
            )
        }
    }
}
