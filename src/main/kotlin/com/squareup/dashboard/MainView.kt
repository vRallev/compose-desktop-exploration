package com.squareup.dashboard

import androidx.compose.desktop.DesktopTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MainView(
  screen: ScreenModel
) {
  MaterialTheme(
    colors = AppTheme.colors.material
  ) {
    DesktopTheme {
      Surface {
        Column(Modifier.fillMaxSize()) {
          TabsView(screen)

          when {
            screen.isAllModulesTabSelected() -> AllModulesView(screen)
            screen.isAllTeamsTabSelected() -> AllTeamsView(screen)
            else -> TeamView(screen)
          }
        }
      }
    }
  }
}
