package com.episode6.typed2.navigation.compose

import androidx.navigation.NavController
import com.episode6.typed2.KeyTypeInfo
import com.episode6.typed2.PrimitiveKeyValueSetter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun NavController.navigateTo(screen: NavScreen) {
  navigate(screen.name)
}

/**
 * Replacement for [NavController.navigate] that allows type-safe setting of arguments
 * Usage:
 * navController.navigateTo(Screen) {
 *   set(Screen.Arg1, "someValue")
 *   set(Screen.Arg2, 42)
 * }
 */
fun NavController.navigateTo(screen: NavScreen, args: PrimitiveKeyValueSetter.() -> Unit) {
  val builder = ComposeNavArgBuilder()
  builder.args()
  navigate(screen.buildRoute(builder.argMap))
}

fun NavController.navigateTo(
  screen: NavScreen,
  scope: CoroutineScope,
  args: suspend PrimitiveKeyValueSetter.() -> Unit,
) {
  scope.launch {
    val builder = ComposeNavArgBuilder()
    builder.args()
    navigate(screen.buildRoute(builder.argMap))
  }
}

private fun NavScreen.buildRoute(argValues: Map<String, Any?>): String {
  if (argValues.isEmpty()) return name

  val requiredValues = requiredArgs
    .map { argValues[it.name] ?: throw MissingRequiredArgumentException(it, this) }

  val optValues = optionalArgs
    .filter { argValues[it.name] != null }
    .map { "${it.name}=${argValues[it.name]}" }

  val route = (listOf(name) + requiredValues).joinToString(separator = "/")
  return if (optValues.isNotEmpty()) {
    "$route?${optValues.joinToString(separator = "&")}"
  } else {
    route
  }
}

private class ComposeNavArgBuilder constructor() : PrimitiveKeyValueSetter {
  val argMap: MutableMap<String, Any?> = mutableMapOf()
  override fun setInt(name: String, value: Int) { argMap[name] = value }
  override fun setString(name: String, value: String?) { argMap[name] = value }
}

class MissingRequiredArgumentException(arg: KeyTypeInfo<*, *>, screen: NavScreen) : IllegalArgumentException(
  "Missing required argument \"${arg.name}\" when navigating to screen \"${screen.name}\""
)
