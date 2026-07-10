package com.olezhaku.idsc.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.olezhaku.idsc.data.loadDevices
import com.olezhaku.idsc.ui.components.FAB
import com.olezhaku.idsc.ui.components.FABType
import com.olezhaku.idsc.ui.components.Input
import com.olezhaku.idsc.ui.components.InputType
import com.olezhaku.idsc.ui.layouts.Layout
import com.olezhaku.idsc.ui.widgets.DeviceList


@Composable
fun DevicesScreen(onDeviceClick: (deviceId: String) -> Unit) {
    var searchText by remember { mutableStateOf("") }

    val context = LocalContext.current
    val devices = remember {
        loadDevices(context)
    }

    val filteredDevices = remember(searchText, devices) {
        devices.filter {
            it.manufacturer.contains(searchText, ignoreCase = true) ||
                    it.marketingName.contains(searchText, ignoreCase = true) ||
                    it.chipset.contains(searchText, ignoreCase = true)
        }
    }

    Layout(
        floatingActionButton = {
            FAB(
                type = FABType.Random,
                onClick = {
                    val randomDevice = devices.random()
                    onDeviceClick(randomDevice.id.toString())
                }
            )
        }
    ) {
        Input(
            value = searchText,
            onValueChange = { searchText = it },
            type = InputType.Search
        )

        DeviceList(filteredDevices, onDeviceClick)
    }
}
