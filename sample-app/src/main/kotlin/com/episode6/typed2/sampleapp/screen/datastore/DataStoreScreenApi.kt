package com.episode6.typed2.sampleapp.screen.datastore

import com.episode6.typed2.navigation.compose.NavScreen
import com.episode6.typed2.navigation.compose.navigateTo
import com.episode6.typed2.sampleapp.nav.AppNavigators

object DataStoreScreen : NavScreen("datastore")

fun interface DataStoreScreenNavigator { fun go() }
fun AppNavigators.dataStoreScreen(): DataStoreScreenNavigator = DataStoreScreenNavigator { navController.navigateTo(DataStoreScreen) }
