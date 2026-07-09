package com.olezhaku.idsc.ui.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.olezhaku.idsc.data.loadDevices
import com.olezhaku.idsc.ui.components.FAB
import com.olezhaku.idsc.ui.components.FABType
import com.olezhaku.idsc.ui.layouts.Layout
import com.olezhaku.idsc.ui.widgets.runWithToast
import com.olezhaku.idsc.utils.ProductPropsInstaller
import kotlinx.coroutines.launch


@Composable
fun DeviceDetailsScreen(deviceId: String, onBackClick: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val devices = remember {
        loadDevices(context)
    }

    val device = remember(deviceId, devices) {
        devices.first { it.id.toString() == deviceId }
    }

    val fields = remember(device) {
        listOf(
            "Brand" to device.brand,
            "Marketing name" to device.marketingName,
            "Manufacturer" to device.manufacturer,
            "Model" to device.model,
            "Device" to device.device,
            "Chipset" to device.chipset,
            "Build ID" to device.buildId,
            "Fingerprint" to device.fingerprint,
            "Serial" to device.serial
        )
    }

    fun installProductProps() {
        scope.launch {
            context.runWithToast(
                loadingMessage = "Saving configuration...",
                successMessage = "Configuration saved. Reboot to apply changes.",
                errorMessage = "Failed to save configuration."
            ) {
                ProductPropsInstaller.install(
                    context = context,
                    device = device
                )
            }
        }
    }

    Layout(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = { Text("Device: $deviceId/${devices.size}") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = ""
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FAB(
                type = FABType.Run,
                onClick = { installProductProps() }
            )
        }
    ) {
        fields.forEachIndexed { index, (key, value) ->
            Row(Modifier.padding(vertical = 3.dp)) {
                Text("$key:", Modifier.weight(0.4f))
                Text(value.toString(), Modifier.weight(0.6f))
            }

            if (index != fields.lastIndex) HorizontalDivider()
        }
    }
}
