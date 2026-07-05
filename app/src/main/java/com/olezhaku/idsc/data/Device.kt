package com.olezhaku.idsc.data


data class Device(
    val id: Int,
    val brand: String,
    val marketing_name: String,
    val device: String,
    val model: String,
    val fingerprint: String,
    val manufacturer: String,
    val serial: String,
    val build_id: String,
    val chipset: String
) {
    val board: String
        get() = device
}
