package com.olezhaku.idsc.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.olezhaku.idsc.ui.screens.DeviceDetailsScreen
import com.olezhaku.idsc.ui.screens.DevicesScreen

@Composable
fun AppNavHost(navHostController: NavHostController) {
    NavHost(
        navHostController,
        Destination.Devices.route
    ) {
        composable(Destination.Devices.route) {
            DevicesScreen(
                onDeviceClick = { deviceId ->
                    navHostController.navigate(
                        Destination.DeviceDetails.createRoute(deviceId)
                    )
                }
            )
        }

        composable(
            Destination.DeviceDetails.route,
            listOf(
                navArgument(Destination.DEVICE_ID) {
                    NavType.StringType
                }
            )
        ) { backStackEntry ->
            val deviceId = requireNotNull(
                backStackEntry.arguments?.getString(Destination.DEVICE_ID)
            )

            DeviceDetailsScreen(
                deviceId = deviceId,
                onBackClick = {
                    navHostController.popBackStack()
                }
            )
        }
    }
}
