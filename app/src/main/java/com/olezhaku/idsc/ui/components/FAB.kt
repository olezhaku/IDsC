package com.olezhaku.idsc.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


enum class FABType {
    Random,
    Run
}

@Composable
fun FAB(type: FABType, onClick: () -> Unit) {
    FloatingActionButton(onClick = onClick, Modifier.padding(end = 24.dp)) {
        when (type) {
            FABType.Random -> {
                Icon(
                    imageVector = Icons.Filled.Casino,
                    contentDescription = ""
                )
            }

            FABType.Run -> {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = ""
                )
            }
        }
    }
}
