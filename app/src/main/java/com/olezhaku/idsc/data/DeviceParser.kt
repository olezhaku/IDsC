package com.olezhaku.idsc.data

import com.olezhaku.idsc.data.Device
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
                marketing_name = item.getString("marketing_name"),
                device = item.getString("device"),
                model = item.getString("model"),
                fingerprint = item.getString("fingerprint"),
                board = item.getString("board"),
                manufacturer = item.getString("manufacturer"),
                serial = item.getString("serial"),
                build_id = item.getString("build_id"),
                chipset = item.getString("chipset")
            )
        )
    }

    return devices
}
