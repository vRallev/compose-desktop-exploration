package com.squareup.dashboard

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize.Min
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign.End
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.squareup.dashboard.SortOrder.A_Z

@OptIn(ExperimentalFoundationApi::class)
@Composable fun AllTeamsView(
  screen: ScreenModel
) {
  val columns = TeamColumn.values()

  val columnWidth = 180.dp
  val rowHeight = 32.dp
  val rowScrollState = rememberScrollState()
  val columnScrollState = rememberLazyListState()

  Box {
    LazyColumn(state = columnScrollState) {
      // Use a single sticky header with multiple cells.
      stickyHeader {
        Column {
          Box(modifier = Modifier.background(AppTheme.colors.material.surface)) {
            Row(modifier = Modifier.horizontalScroll(rowScrollState)) {
              // Draw an empty cell to offset the scrollable header cells. Then draw the first cell
              // on top of this empty cell. This will make the scrollable cells disappear behind
              // the first cell.
              Box(modifier = Modifier.width(columnWidth * 2))

              columns.drop(1).forEach { column ->
                HeaderCell(column.header, columnWidth, column, screen)
              }
            }

            Row(modifier = Modifier.height(Min)) {
              HeaderCell(
                text = columns[0].header,
                width = columnWidth * 2 - 8.dp,
                column = columns[0],
                screen = screen
              )
              HorizontalFadingEdge()
            }
          }

          // Draw the fading edge at the bottom of the header.
          VerticalFadingEdge(columnWidth * columns.size + columnWidth)
        }
      }

      items(screen.allTeamsTab.teamSummaries) { summary ->
        // val summary = moduleModel.module

        Box {
          Row(modifier = Modifier.horizontalScroll(rowScrollState).height(Min)) {
            // Draw an empty cell to offset the scrollable cells. Then draw the first cell
            // on top of this empty cell. This will make the scrollable cells disappear behind
            // the first cell.
            Box(modifier = Modifier.width(columnWidth * 2))

            CellText(
              text = summary.ownedModules.toString(),
              width = columnWidth
            )
            CellText(
              text = summary.ownedLoc.toString(),
              width = columnWidth
            )
            CellText(
              text = summary.devApps.toString(),
              width = columnWidth
            )
            CellText(
              text = summary.legacyModules.toString(),
              width = columnWidth
            )
            CellText(
              text = summary.redCells.toString(),
              width = columnWidth
            )
            CellText(
              text = "%.2f".format(summary.redCellsPerModule),
              width = columnWidth
            )
          }

          Row(modifier = Modifier.height(Min).clickable { screen.teamTabs.single { it.team == summary.team }.select() }) {
            Text(
              text = summary.team.name,
              modifier = Modifier
                .width(columnWidth * 2 - 8.dp)
                .background(AppTheme.colors.material.surface)
                .horizontalScroll(rememberScrollState())
                .padding(8.dp),
              fontFamily = FontFamily.Monospace,
              maxLines = 1,
              softWrap = false
            )

            HorizontalFadingEdge()
          }
        }
      }
    }

    VerticalScrollbar(
      rememberScrollbarAdapter(columnScrollState, screen.allTeamsTab.teamSummaries.size, rowHeight),
      Modifier.align(Alignment.CenterEnd),
      style = AppTheme.scrollbarStyle()
    )

    HorizontalScrollbar(
      rememberScrollbarAdapter(rowScrollState),
      Modifier.align(Alignment.BottomCenter),
      style = AppTheme.scrollbarStyle()
    )
  }
}

@Composable private fun HeaderCell(
  text: String,
  width: Dp,
  column: TeamColumn,
  screen: ScreenModel
) {
  Column(
    modifier = Modifier.background(AppTheme.colors.material.surface)
      .width(width)
      .clickable {
        if (screen.teamSortSelection.column == column) {
          screen.changeTeamSort(order = screen.teamSortSelection.order.next())
        } else {
          screen.changeTeamSort(column = column)
        }
      },
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(text, fontWeight = FontWeight.Bold)

      val showSort = screen.teamSortSelection.column == column

      val alpha by animateFloatAsState(if (showSort) 1f else 0f)
      val rotation by animateFloatAsState(if (screen.teamSortSelection.order == A_Z) 0f else 180f)

      Icon(
        imageVector = Icons.Default.ExpandMore,
        contentDescription = null,
        modifier = Modifier
          .defaultMinSize(28.dp, 28.dp)
          .padding(4.dp)
          .alpha(alpha)
          .rotate(rotation)
      )
    }
  }
}

@Composable private fun CellText(
  text: String,
  modifier: Modifier = Modifier,
  width: Dp
) {
  Text(
    text = text,
    modifier = modifier
      .width(width)
      .border(1.dp, Color.White)
      .padding(8.dp),
    maxLines = 1,
    softWrap = false,
    fontFamily = FontFamily.Monospace,
    textAlign = End
  )
}
