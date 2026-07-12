package com.olezhaku.idsc.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


enum class FABType {
    Random,
    Run,
    Success,
    Error
}

@Composable
fun FAB(
    type: FABType,
    onClick: () -> Unit,
    isRunning: Boolean = false
) {
    val rotation by rememberInfiniteTransition()
        .animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                tween(1000, easing = LinearEasing)
            )
        )

    val icon = when (type) {
        FABType.Random -> Icons.Filled.Casino
        FABType.Run -> Icons.Filled.Refresh
        FABType.Success -> Icons.Filled.Check
        FABType.Error -> Icons.Filled.Close
    }

    val containerColor = when (type) {
        FABType.Success -> Color.Green
        FABType.Error -> MaterialTheme.colorScheme.errorContainer
        else -> FloatingActionButtonDefaults.containerColor
    }

    val iconColor = MaterialTheme.colorScheme.contentColorFor(containerColor)

    FloatingActionButton(
        onClick = {
            if (
                !isRunning &&
                type != FABType.Error &&
                type != FABType.Success
            )
                onClick()
        },
        Modifier.padding(end = 24.dp, bottom = 24.dp),
        containerColor = containerColor
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "",
            tint = iconColor,
            modifier =
                if (type == FABType.Run && isRunning)
                    Modifier.rotate(rotation)
                else
                    Modifier
        )
    }
}
