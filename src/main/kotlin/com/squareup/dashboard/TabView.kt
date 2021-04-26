package com.squareup.dashboard

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TabsView(screen: ScreenModel) {
  Surface(
    elevation = 12.dp,
    modifier = Modifier.padding(bottom = 20.dp)
  ) {
    Row(Modifier.horizontalScroll(rememberScrollState())) {
      TabView(screen.allModulesTab)
      TabView(screen.allTeamsTab)

      screen.teamTabs.forEach { team ->
        TabView(team)
      }
    }
  }
}

@Composable
fun TabView(tab: Tab) {
  Surface(
    color = animateColorAsState(
      targetValue = if (tab.isSelected()) AppTheme.colors.backgroundDark else Color.Transparent,
      animationSpec = TweenSpec()
    ).value
  ) {
    Row(
      Modifier
        .clickable(remember(::MutableInteractionSource), indication = null) {
          tab.select()
        }
        .padding(4.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = when (tab) {
          is TeamTab -> tab.name
          is AllTeamsTab -> "All Teams"
          is AllModulesTab -> "All Modules"
        },
        color = LocalContentColor.current,
        fontSize = 14.sp,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
      )
    }
  }
}
