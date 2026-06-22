package com.olezhaku.idsc.ui.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.olezhaku.idsc.data.Device
import kotlinx.coroutines.delay

@Composable
fun DeviceList(devices: List<Device>, onDeviceClick: (deviceId: String) -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow { listState.isScrollInProgress }
            .collect { isScrolling ->
                if (isScrolling) {
                    keyboardController?.hide()
                } else {
                    delay(300)
                    keyboardController?.show()
                }
            }
    }

    LazyColumn(state = listState) {
        itemsIndexed(devices) { index, device ->
            Box(
                Modifier
                    .fillMaxWidth()
                    .clickable { onDeviceClick(device.id.toString()) }
            ) {
                Text(
                    "${device.id}. ${device.manufacturer} | ${device.marketing_name} | ${device.chipset}",
                    Modifier.padding(12.dp)
                )
            }

            if (index < devices.lastIndex) HorizontalDivider()
        }
    }
}
