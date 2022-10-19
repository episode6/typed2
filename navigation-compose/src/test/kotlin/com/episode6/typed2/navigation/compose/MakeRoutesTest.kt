package com.episode6.typed2.navigation.compose

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavArgument
import androidx.navigation.NavType
import assertk.Assert
import assertk.all
import assertk.assertThat
import assertk.assertions.*
import org.junit.Test

class MakeRoutesTest {

  @Test fun emptyScreen() {
    val route = EmptyScreen.makeRouteDefinition()
    val args = EmptyScreen.makeArgDefinitions()

    assertThat(route).isEqualTo("empty")
    assertThat(args).isEmpty()
  }

  @Test fun requiredArgs() {
    val route = ScreenWithRequiredArgs.makeRouteDefinition()
    val args = ScreenWithRequiredArgs.makeArgDefinitions()

    assertThat(route).isEqualTo("required_args/{intArg}/{stringArg}")
    assertThat(args).all {
      hasSize(2)
      index(0).all {
        hasName("intArg")
        hasType(NavType.StringType) // required int args are treated as strings
        isNotNullable()
        hasNoDefaultValuePresent()
      }
      index(1).all {
        hasName("stringArg")
        hasType(NavType.StringType)
        isNotNullable()
        hasNoDefaultValuePresent()
      }
    }
  }

  @Test fun optionalArgs() {
    val route = ScreenWithOptionalArgs.makeRouteDefinition()
    val args = ScreenWithOptionalArgs.makeArgDefinitions()

    assertThat(route).isEqualTo("opt_args?nullArg={nullArg}&defaultArg={defaultArg}")
    assertThat(args).all {
      hasSize(2)
      index(0).all {
        hasName("nullArg")
        hasType(NavType.StringType)
        isNullable()
        hasNoDefaultValuePresent()
      }
      index(1).all {
        hasName("defaultArg")
        hasType(NavType.IntType)
        isNotNullable()
        hasDefaultValuePresent()
        hasDefault(42)
      }
    }
  }

  @Test fun testAllArgTypes() {
    val route = ScreenWithAllArgTypes.makeRouteDefinition()
    val args = ScreenWithAllArgTypes.makeArgDefinitions()

    assertThat(route).isEqualTo("all_args/{stringArg}?nullArg={nullArg}&defaultArg={defaultArg}")
    assertThat(args).all {
      hasSize(3)
      index(0).all {
        hasName("nullArg")
        hasType(NavType.StringType)
        isNullable()
        hasNoDefaultValuePresent()
      }
      index(1).all {
        hasName("defaultArg")
        hasType(NavType.IntType)
        isNotNullable()
        hasDefaultValuePresent()
        hasDefault(42)
      }
      index(2).all {
        hasName("stringArg")
        hasType(NavType.StringType)
        isNotNullable()
        hasNoDefaultValuePresent()
      }
    }
  }
}

fun Assert<NamedNavArgument>.hasName(name: String) = prop(NamedNavArgument::name).isEqualTo(name)
fun Assert<NamedNavArgument>.argProp() = prop(NamedNavArgument::argument)
fun Assert<NamedNavArgument>.hasType(navType: NavType<*>) = argProp().prop(NavArgument::type).isEqualTo(navType)
fun Assert<NamedNavArgument>.isNullable() = argProp().prop(NavArgument::isNullable).isTrue()
fun Assert<NamedNavArgument>.isNotNullable() = argProp().prop(NavArgument::isNullable).isFalse()
fun Assert<NamedNavArgument>.hasDefaultValuePresent() = argProp().prop(NavArgument::isDefaultValuePresent).isTrue()
fun Assert<NamedNavArgument>.hasNoDefaultValuePresent() = argProp().prop(NavArgument::isDefaultValuePresent).isFalse()
fun Assert<NamedNavArgument>.hasDefault(value: Any?) = argProp().prop(NavArgument::defaultValue).isEqualTo(value)
