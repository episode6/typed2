package com.episode6.typed2.navigation.compose

import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.*
import androidx.navigation.compose.composable
import com.episode6.typed2.KeyDescriptor
import com.episode6.typed2.OutputDefault
import com.episode6.typed2.isRequired
import kotlin.reflect.KClass
import androidx.navigation.compose.NavHost as RawNavHost

// wrapper for NavHost that takes a startScreen instead of a string
@Composable
fun NavHost(
  navController: NavHostController,
  startScreen: NavScreen,
  modifier: Modifier = Modifier,
  builder: NavGraphBuilder.() -> Unit,
) {
  val hasRequiredArgs = remember { startScreen.requiredArgs.isNotEmpty() }
  if (hasRequiredArgs) throw IllegalStartScreenException(startScreen)

  RawNavHost(
    navController = navController,
    startDestination = remember { startScreen.makeRouteDefinition() },
    modifier = modifier,
    builder = builder
  )
}

// wrapper for composable that takes a string instead of a route and arg list
fun NavGraphBuilder.composableScreen(
  screen: NavScreen,
  deepLinks: List<NavDeepLink> = emptyList(),
  content: @Composable (NavBackStackEntry) -> Unit,
) {
  composable(
    route = screen.makeRouteDefinition(),
    arguments = screen.makeArgDefinitions(),
    deepLinks = deepLinks,
    content = content,
  )
}

@VisibleForTesting
internal fun NavScreen.makeRouteDefinition(): String {
  if (args.isEmpty()) return name
  val requiredArgStrings = requiredArgs.map { "{${it.name}}" }
  val optionalArgStrings = optionalArgs.map { "${it.name}={${it.name}}" }

  val route = (listOf(name) + requiredArgStrings).joinToString(separator = "/")
  if (optionalArgStrings.isEmpty()) return route
  return "$route?${optionalArgStrings.joinToString(separator = "&")}"
}

@VisibleForTesting
internal fun NavScreen.makeArgDefinitions(): List<NamedNavArgument> = args.map { it.toNavArgument() }

internal val NavScreen.requiredArgs: List<KeyDescriptor<*, *>> get() = args.filter { it.isRequired }
internal val NavScreen.optionalArgs: List<KeyDescriptor<*, *>> get() = args.filter { !it.isRequired }

private fun <T, BACKED_BY> KeyDescriptor<T, BACKED_BY>.toNavArgument(): NamedNavArgument = navArgument(name = name) {
  type = backingTypeInfo.kclass.asNavType()
  nullable = backingTypeInfo.nullable && !isRequired
  if (!nullable && backingTypeInfo.default != null) {
    defaultValue = backingTypeInfo.default
  }
}

private fun KClass<*>.asNavType(): NavType<*> = when (this) {
  Int::class     -> NavType.IntType
  String::class  -> NavType.StringType
  Long::class    -> NavType.LongType
  Float::class   -> NavType.FloatType
  Boolean::class -> NavType.BoolType
  else           -> throw UnexpectedKeyTypeException(this)
}

class IllegalStartScreenException(screen: NavScreen) :
  IllegalArgumentException("Illegal start screen used in NavHost definition: ${screen.name} has required arguments.")

class UnexpectedKeyTypeException(type: KClass<*>) : RuntimeException("Unexpected key type: $type")
