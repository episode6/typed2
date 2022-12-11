package com.episode6.typed2.sampleapp.screen.navargs

import com.episode6.typed2.async
import com.episode6.typed2.gson.gson
import com.episode6.typed2.kotlinx.serialization.json.json
import com.episode6.typed2.navigation.compose.NavScreen
import com.episode6.typed2.navigation.compose.launchNavigateTo
import com.episode6.typed2.navigation.compose.set
import com.episode6.typed2.sampleapp.data.KtxSerializable
import com.episode6.typed2.sampleapp.data.RegularAssDataClass
import com.episode6.typed2.sampleapp.nav.AppNavigators
import com.episode6.typed2.string

object NavArgScreen : NavScreen("navArgScreen") {
  val STRING = key("string").string(default = "")
  val KTX_SERIALIZABLE = key("ktxSerializable").json(KtxSerializable::serializer).required().async()
  val REGULAR_DATA_CLASS = key("dataClass").gson<RegularAssDataClass>().required().async()
}

fun interface NavArgScreenNavigator {
  fun go(string: String, ktxSerializable: KtxSerializable, regularDataClass: RegularAssDataClass)
}
fun AppNavigators.navArgScreen(): NavArgScreenNavigator = NavArgScreenNavigator { string, ktxSerializable, regularDataClass ->
  navController.launchNavigateTo(NavArgScreen, scope) {
    set(NavArgScreen.STRING, string)
    set(NavArgScreen.KTX_SERIALIZABLE, ktxSerializable)
    set(NavArgScreen.REGULAR_DATA_CLASS, regularDataClass)
  }
}
