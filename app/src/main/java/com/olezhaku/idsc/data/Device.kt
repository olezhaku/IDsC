package com.olezhaku.idsc.data


data class Device(
    val id: Int,
    val brand: String,
    val marketingName: String,
    val device: String,
    val model: String,
    val fingerprint: String,
    val manufacturer: String,
    val serial: String,
    val buildId: String,
    val chipset: String
)
