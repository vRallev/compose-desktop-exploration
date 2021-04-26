package com.squareup.dashboard

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle.Italic
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TeamView(screen: ScreenModel) {
  Box(Modifier.fillMaxSize()) {
    val team = (screen.selectedTab as TeamTab).team
    val modules = screen.modules.map { it.module }.filter { it.team == team }

    Column(
      modifier = Modifier
        .fillMaxSize()
    ) {
      Text(
        text = team.name,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        fontStyle = Italic,
        modifier = Modifier.padding(bottom = 16.dp, start = 16.dp)
      )

      LazyVerticalGrid(
        cells = GridCells.Fixed(2),
        modifier = Modifier.width(300.dp).padding(start = 24.dp)
      ) {
        items(10) { index ->
          val text = when (index) {
            0 -> "Owned modules:"
            1 -> modules.size
            2 -> "Owned LOC:"
            3 -> modules.sumOf { it.loc }
            4 -> "Dev apps:"
            5 -> modules.count { it.isDemoModule() }
            6 -> "Legacy modules:"
            7 -> modules.filter { it.legacyModule }.size
            8 -> "Violations:"
            9 -> modules.sumOf { it.redCells }
            else -> throw NotImplementedError()
          }.toString()

          if (index >= 8) {
            Text(
              text,
              modifier = Modifier.padding(top = 16.dp)
            )
          } else {
            Text(text)
          }
        }
      }

      Surface(
        elevation = 12.dp,
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 24.dp)
      ) {
        Spacer(
          modifier = Modifier
            .height(1.dp)
            .fillMaxWidth()
            .background(AppTheme.colors.backgroundLight),
        )
      }

      AllModulesView(ScreenModel(modules))
    }
  }
}