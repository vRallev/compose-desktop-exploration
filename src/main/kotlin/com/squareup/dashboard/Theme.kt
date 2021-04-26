package com.squareup.dashboard

import androidx.compose.foundation.ScrollbarStyleAmbient
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object AppTheme {
  val colors: Colors = Colors()

  class Colors(
    val backgroundDark: Color = Color(0xFF2B2B2B),
    val backgroundMedium: Color = Color(0xFF3C3F41),
    val backgroundLight: Color = Color(0xFFBBBBBB),

    val red: Color = Color(0xFFF26B6B),
    val orange: Color = Color(0xFFF4AC57),
    val yellow: Color = Color(0xFFF2D452),
    val green: Color = Color(0xFF7BD962),

    val material: androidx.compose.material.Colors = darkColors(
      background = backgroundDark,
      surface = backgroundMedium,
      primary = Color.White
    ),
  )

  @Composable
  fun scrollbarStyle() = ScrollbarStyleAmbient.current.copy(
    hoverColor = Color(0xAADDDDDD),
    unhoverColor = Color(0xAABBBBBB)
  )
}