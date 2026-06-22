package com.olezhaku.idsc.ui.screens

import androidx.compose.material3.Text
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
fun DeviceDetailsScreen(deviceId: String, onBackClick: () -> Unit) {
    val context = LocalContext.current
    val devices = remember {
        val json = context.assets.open("devices.json").bufferedReader().use { it.readText() }
        parseDevices(json)
    }

    val device = remember(deviceId, devices) {
        devices.firstOrNull { it.id.toString() == deviceId }
    }

    Layout {
        if (device == null) {
            Text("Device not found")
        } else {
            Text("ID: ${device.id}")
            Text("Board: ${device.board}")
            Text("Brand: ${device.brand}")
            Text("Build ID: ${device.build_id}")
            Text("Chipset: ${device.chipset}")
            Text("Device: ${device.device}")
            Text("Fingerprint: ${device.fingerprint}")
            Text("Manufacturer: ${device.manufacturer}")
            Text("Marketing name: ${device.marketing_name}")
            Text("Model: ${device.model}")
            Text("Serial: ${device.serial}")
        }
    }
}
