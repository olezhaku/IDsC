package com.olezhaku.idsc.navigation

sealed class Destination(val route: String) {
    data object Devices : Destination(ROUTE_DEVICES)
    data object DeviceDetails : Destination("$ROUTE_DEVICE/{$DEVICE_ID}") {
        fun createRoute(deviceId: String) = "$ROUTE_DEVICE/$deviceId"
    }

    companion object {
        private const val ROUTE_DEVICES = "route_devices"
        private const val ROUTE_DEVICE = "route_device"

        const val DEVICE_ID = "deviceId"
    }
}