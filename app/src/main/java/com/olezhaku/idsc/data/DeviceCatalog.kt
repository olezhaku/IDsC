package com.olezhaku.idsc.data

import android.content.Context


private const val DEVICES_ASSET_NAME = "devices.json"


fun loadDevices(context: Context): List<Device> {
    val json = context.assets
        .open(DEVICES_ASSET_NAME)
        .bufferedReader()
        .use { it.readText() }

    return parseDevices(json)
}
