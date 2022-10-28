package com.episode6.typed2.navigation.compose

import androidx.navigation.NavController
import com.episode6.typed2.KeyDescriptor
import com.episode6.typed2.PrimitiveKeyValueSetter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
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
inline fun NavController.navigateTo(screen: NavScreen, args: PrimitiveKeyValueSetter.() -> Unit) {
  val builder = ComposeNavArgBuilder()
  builder.args()
  navigate(screen.buildRoute(builder))
}

inline fun NavController.launchNavigateTo(
  screen: NavScreen,
  scope: CoroutineScope,
  crossinline args: suspend PrimitiveKeyValueSetter.() -> Unit,
): Job = scope.launch { navigateTo(screen) { args() } }


fun NavScreen.buildRoute(argBuilder: ComposeNavArgBuilder): String {
  val argValues = argBuilder.argMap
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

class ComposeNavArgBuilder : PrimitiveKeyValueSetter {
  internal val argMap: MutableMap<String, Any?> = mutableMapOf()
  override fun setBoolean(name: String, value: Boolean) { argMap[name] = value }
  override fun setFloat(name: String, value: Float) { argMap[name] = value }
  override fun setInt(name: String, value: Int) { argMap[name] = value }
  override fun setLong(name: String, value: Long) { argMap[name] = value }
  override fun setString(name: String, value: String?) { argMap[name] = value }
  override fun remove(name: String) { argMap.remove(name) }
}

class MissingRequiredArgumentException(arg: KeyDescriptor<*, *>, screen: NavScreen) : IllegalArgumentException(
  "Missing required argument \"${arg.name}\" when navigating to screen \"${screen.name}\""
)
