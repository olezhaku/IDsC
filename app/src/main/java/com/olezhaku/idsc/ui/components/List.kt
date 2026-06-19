package com.olezhaku.idsc.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp


@Composable
fun List(list: List<String>) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow { listState.isScrollInProgress }
            .collect { isScrolling ->
                if (isScrolling) {
                    keyboardController?.hide()
                } else {
                    keyboardController?.show()
                }
            }
    }

    LazyColumn(state = listState) {
        items(list.size) { index ->
            Text(
                text = "${index + 1}. ${list[index]}",
                modifier = Modifier.padding(12.dp)
            )

            if (index < list.lastIndex) HorizontalDivider()
        }
    }
}
