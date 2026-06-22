package com.olezhaku.idsc.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.olezhaku.idsc.data.parseDevices
import com.olezhaku.idsc.ui.components.Input
import com.olezhaku.idsc.ui.components.InputType
import com.olezhaku.idsc.ui.layouts.Layout
import com.olezhaku.idsc.ui.widgets.DeviceList

@Composable
fun DevicesScreen(onDeviceClick: (deviceId: String) -> Unit) {
    var searchText by remember { mutableStateOf("") }

    val context = LocalContext.current
    val devices = remember {
        val json = context.assets.open("devices.json").bufferedReader().use { it.readText() }
        parseDevices(json)
    }

    val filteredDevices = remember(searchText, devices) {
        devices.filter {
            it.manufacturer.contains(searchText, ignoreCase = true) ||
                    it.marketing_name.contains(searchText, ignoreCase = true) ||
                    it.chipset.contains(searchText, ignoreCase = true)
        }
    }

    Layout {
        Input(searchText, { searchText = it }, InputType.Search)

        DeviceList(filteredDevices, onDeviceClick)
    }
}
