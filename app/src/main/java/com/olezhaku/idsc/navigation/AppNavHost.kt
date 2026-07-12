package com.olezhaku.idsc.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.olezhaku.idsc.ui.screens.DeviceDetailsScreen
import com.olezhaku.idsc.ui.screens.DevicesScreen


@Composable
fun AppNavHost(navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        startDestination = Destination.Devices.route,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                tween(300)
            )
        },
        exitTransition = { ExitTransition.None },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                tween(300)
            )
        },
        popEnterTransition = { EnterTransition.None }
    ) {
        composable(route = Destination.Devices.route) {
            DevicesScreen(
                onDeviceClick = { deviceId ->
                    navHostController.navigate(
                        Destination.DeviceDetails.createRoute(deviceId)
                    )
                }
            )
        }

        composable(
            route = Destination.DeviceDetails.route,
            arguments = listOf(
                navArgument(Destination.DEVICE_ID) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val deviceId = requireNotNull(
                backStackEntry.arguments?.getString(Destination.DEVICE_ID)
            )

            DeviceDetailsScreen(
                deviceId = deviceId,
                onBackClick = { navHostController.popBackStack() }
            )
        }
    }
}
