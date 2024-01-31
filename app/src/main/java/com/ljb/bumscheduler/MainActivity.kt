package com.ljb.bumscheduler

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ljb.bumscheduler.navigation.MainNavGraph
import com.ljb.bumscheduler.ui.theme.BumSchedulerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BumSchedulerTheme {
                BumSchedulerApp()
            }
        }
    }
}

const val CALENDAR = "CALENDAR"
const val MAP = "MAP"
const val NOTIFICATION = "NOTIFICATION"
const val SETTING = "SETTING"

sealed class BottomNavItem(
    val title: Int, val icon: ImageVector, val screenRoute: String
) {
    data object Calendar : BottomNavItem(R.string.text_calendar, Icons.Rounded.DateRange, CALENDAR)
    data object Map : BottomNavItem(R.string.text_map, Icons.Rounded.LocationOn, MAP)
    data object Notification : BottomNavItem(R.string.text_notification, Icons.Rounded.Notifications, NOTIFICATION)
    data object Setting : BottomNavItem(R.string.text_setting, Icons.Rounded.Settings, SETTING)
}


@Composable
fun BumSchedulerApp() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { paddingValues ->
        MainNavGraph(navController, paddingValues)
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.Calendar,
        BottomNavItem.Map,
        BottomNavItem.Notification,
        BottomNavItem.Setting
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        modifier = Modifier
            .height(64.dp)
            .clip(RoundedCornerShape(20.dp, 20.dp, 0.dp, 0.dp))

    ) {
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = "${item.title} Menu"
                        )

                        Text(
                            text = stringResource(id = item.title),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 10.sp,
                        )
                    }
                },
                label = {
                    /*Text(
                        text = stringResource(id = item.title),
                        //fontWeight = FontWeight.SemiBold
                    )*/
                },
                selected = currentRoute == item.screenRoute,
                //alwaysShowLabel = false,
                onClick = {
                    navController.navigate(item.screenRoute) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        //동일한 항목을 다시 선택할 때 동일한 대상의 여러 복사본 방지
                        launchSingleTop = true
                        // 이전에 선택한 항목을 다시 선택할 때 상태 복원
                        restoreState = true
                    }
                },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BumSchedulerAppPreview(){
    BumSchedulerApp()
}

@Preview(showBackground = true)
@Composable
fun BottomNavigationBarPreview(){
    val navController = rememberNavController()
    BottomNavigationBar(navController)
}