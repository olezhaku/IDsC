package com.olezhaku.idsc.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp

enum class InputType {
    Text,
    Search
}

@Composable
fun Input(
    value: String,
    onValueChange: (String) -> Unit,
    clearFocus: () -> Unit = {},
    type: InputType = InputType.Text
) {
    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .onFocusChanged { focusState -> isFocused = focusState.isFocused },
        value = value,
        onValueChange = onValueChange,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
        singleLine = true,
        keyboardActions = KeyboardActions(onAny = { clearFocus() }),
        trailingIcon = {
            when (type) {
                InputType.Text -> {}

                InputType.Search -> {
                    IconButton(
                        onClick = {
                            focusRequester.requestFocus()
                            if (value.isNotEmpty()) onValueChange("")
                        }) {
                        Icon(
                            imageVector =
                                if (value.isNotEmpty())
                                    Icons.Default.Close
                                else
                                    Icons.Default.Search,
                            contentDescription = "",
                            tint =
                                if (isFocused)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    )
}
