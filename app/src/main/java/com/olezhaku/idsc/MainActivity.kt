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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.unit.dp
import com.olezhaku.idsc.ui.components.Input
import com.olezhaku.idsc.ui.components.InputType
import com.olezhaku.idsc.ui.components.List
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
    var searchText by remember { mutableStateOf("") }

    val list = listOf(
        "Паспорт",
        "Водительское удостоверение",
        "Студенческий билет",
        "Загранпаспорт",
        "СНИЛС",
        "Паспорт",
        "Водительское удостоверение",
        "Студенческий билет",
        "Загранпаспорт",
        "СНИЛС",
        "Паспорт",
        "Водительское удостоверение",
        "Студенческий билет",
        "Загранпаспорт",
        "СНИЛС",
        "Паспорт",
        "Водительское удостоверение",
        "Студенческий билет",
        "Загранпаспорт",
        "СНИЛС",
        "Паспорт",
        "Водительское удостоверение",
        "Студенческий билет",
        "Загранпаспорт",
        "СНИЛС",
        "Паспорт",
        "Водительское удостоверение",
        "Студенческий билет",
        "Загранпаспорт",
        "СНИЛС",
    )

    Layout {
        Input(
            value = searchText,
            onValueChange = { searchText = it },
            type = InputType.Search
        )

        List(list)
    }
}

@Composable
fun Layout(content: @Composable () -> Unit) {
    Scaffold {
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .padding(start = 24.dp, top = 24.dp, end = 24.dp)
        ) {
            content()
        }
    }
}
