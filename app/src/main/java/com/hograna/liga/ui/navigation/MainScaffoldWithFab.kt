package com.hograna.liga.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.SportsScore
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hograna.liga.R
import com.hograna.liga.ui.matches.MatchesTabsScreen
import com.hograna.liga.ui.standings.StandingsScreen
import com.hograna.liga.ui.stats.HogRanaStatsScreen
import com.hograna.liga.ui.teams.AddTeamModalSheet
import com.hograna.liga.ui.theme.Background
import com.hograna.liga.ui.theme.GreenNeon
import com.hograna.liga.ui.theme.Surface
import com.hograna.liga.ui.theme.TextDisabled
import com.hograna.liga.ui.theme.TextPrimary
import com.hograna.liga.ui.videos.VideoHistoryScreen

private enum class AppDestination(val route: String) {
    STANDINGS("standings"),
    MATCHES("matches"),
    STATS("stats"),
    VIDEOS("videos")
}

@Composable
fun MainScaffoldWithFab() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    var showAddTeamSheet by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        containerColor = Background,
        bottomBar = {
            Box {
                NavigationBar(
                    containerColor = Surface,
                    tonalElevation = androidx.compose.ui.unit.Dp(0f)
                ) {
                    NavigationBarItem(
                        selected = currentRoute == AppDestination.STANDINGS.route,
                        onClick = {
                            navController.navigate(AppDestination.STANDINGS.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Default.Home, contentDescription = null) },
                        label = { Text(stringResource(R.string.nav_standings)) },
                        colors = navBarItemColors()
                    )

                    NavigationBarItem(
                        selected = currentRoute == AppDestination.MATCHES.route,
                        onClick = {
                            navController.navigate(AppDestination.MATCHES.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Default.SportsScore, contentDescription = null) },
                        label = { Text(stringResource(R.string.nav_matches)) },
                        colors = navBarItemColors()
                    )

                    // Empty center slot — FAB floats above
                    NavigationBarItem(
                        selected = false,
                        onClick = {},
                        icon = {},
                        enabled = false,
                        colors = navBarItemColors()
                    )

                    NavigationBarItem(
                        selected = currentRoute == AppDestination.STATS.route,
                        onClick = {
                            navController.navigate(AppDestination.STATS.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Default.BarChart, contentDescription = null) },
                        label = { Text(stringResource(R.string.nav_stats)) },
                        colors = navBarItemColors()
                    )

                    NavigationBarItem(
                        selected = currentRoute == AppDestination.VIDEOS.route,
                        onClick = {
                            navController.navigate(AppDestination.VIDEOS.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Default.VideoLibrary, contentDescription = null) },
                        label = { Text(stringResource(R.string.nav_videos)) },
                        colors = navBarItemColors()
                    )
                }

                // Central FAB elevated above the NavigationBar
                FloatingActionButton(
                    onClick = { showAddTeamSheet = true },
                    modifier = Modifier.align(Alignment.TopCenter),
                    containerColor = GreenNeon,
                    contentColor = Background
                ) {
                    Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_team))
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppDestination.STANDINGS.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(AppDestination.STANDINGS.route) { StandingsScreen() }
            composable(AppDestination.MATCHES.route) { MatchesTabsScreen() }
            composable(AppDestination.STATS.route) { HogRanaStatsScreen() }
            composable(AppDestination.VIDEOS.route) { VideoHistoryScreen() }
        }
    }

    if (showAddTeamSheet) {
        AddTeamModalSheet(onDismiss = { showAddTeamSheet = false })
    }
}

@Composable
private fun navBarItemColors() = NavigationBarItemDefaults.colors(
    selectedIconColor = GreenNeon,
    selectedTextColor = GreenNeon,
    indicatorColor = GreenNeon.copy(alpha = 0.12f),
    unselectedIconColor = TextDisabled,
    unselectedTextColor = TextDisabled
)
