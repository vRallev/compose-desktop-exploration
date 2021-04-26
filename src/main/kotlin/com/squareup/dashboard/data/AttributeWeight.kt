package com.squareup.dashboard.data

import androidx.compose.ui.graphics.Color
import com.squareup.dashboard.AppTheme

enum class AttributeWeight(
  val color: Color
) {
  NONE(AppTheme.colors.material.surface),
  GREEN(AppTheme.colors.green),
  YELLOW(AppTheme.colors.yellow),
  ORANGE(AppTheme.colors.orange),
  RED(AppTheme.colors.red),
  ;

  companion object {
    fun Boolean.toWeight(): AttributeWeight = if (this) GREEN else RED
  }
}
