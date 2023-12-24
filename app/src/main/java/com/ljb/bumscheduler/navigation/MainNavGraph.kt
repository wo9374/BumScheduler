package com.ljb.bumscheduler.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ljb.bumscheduler.BottomNavItem
import com.ljb.bumscheduler.ui.screen.CalendarScreen
import com.ljb.bumscheduler.ui.screen.MapScreen
import com.ljb.bumscheduler.ui.screen.NotificationScreen
import com.ljb.bumscheduler.ui.screen.SettingScreen

@Composable
fun MainNavGraph(navController: NavHostController, paddingValues: PaddingValues){
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Calendar.screenRoute,
        Modifier.padding(paddingValues)
    ){
        composable(BottomNavItem.Calendar.screenRoute){ CalendarScreen() }
        composable(BottomNavItem.Map.screenRoute){ MapScreen() }
        composable(BottomNavItem.Notification.screenRoute){ NotificationScreen() }
        composable(BottomNavItem.Setting.screenRoute){ SettingScreen() }
    }
}