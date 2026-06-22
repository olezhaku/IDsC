package com.olezhaku.idsc.ui.layouts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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
