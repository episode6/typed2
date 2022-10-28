package com.episode6.typed2.navigation.compose

import com.episode6.typed2.int
import com.episode6.typed2.string

object EmptyScreen : NavScreen("empty")

object ScreenWithRequiredArgs : NavScreen("required_args") {
  val IntArg = key("intArg").int().required()
  val StringArg = key("stringArg").string().required()
}

object ScreenWithOptionalArgs : NavScreen("opt_args") {
  val NullArg = key("nullArg").string()
  val DefaultArg = key("defaultArg").int(default = 42)
}

object ScreenWithAllArgTypes : NavScreen("all_args") {
  val NullArg = key("nullArg").string()
  val DefaultArg = key("defaultArg").int(default = 42)
  val RequiredArg = key("stringArg").string().required()
}

object ScreenWithAsyncArgTypes : NavScreen("all_args") {
  val NullArg = key("nullArg").string().async()
  val DefaultArg = key("defaultArg").int(default = 42).async()
  val RequiredArg = key("stringArg").string().required().async()
}
