package com.olezhaku.idsc.data

import org.json.JSONArray


fun parseDevices(json: String): List<Device> {
    val array = JSONArray(json)
    val devices = mutableListOf<Device>()

    for (i in 0 until array.length()) {
        val item = array.getJSONObject(i)

        devices.add(
            Device(
                id = item.getInt("id"),
                brand = item.getString("brand"),
                marketingName = item.getString("marketing_name"),
                device = item.getString("device"),
                model = item.getString("model"),
                fingerprint = item.getString("fingerprint"),
                manufacturer = item.getString("manufacturer"),
                serial = item.getString("serial"),
                buildId = item.getString("build_id"),
                chipset = item.getString("chipset")
            )
        )
    }

    return devices
}
