package com.squareup.dashboard

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize.Min
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.squareup.dashboard.SortOrder.A_Z
import com.squareup.dashboard.data.AttributeWeight
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable fun AllModulesView(
  screen: ScreenModel
) {
  val columns = ModuleColumn.values()

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

      items(screen.modules) { moduleModel ->
        val module = moduleModel.module

        Box {
          Row(modifier = Modifier.horizontalScroll(rowScrollState).height(Min)) {
            // Draw an empty cell to offset the scrollable cells. Then draw the first cell
            // on top of this empty cell. This will make the scrollable cells disappear behind
            // the first cell.
            Box(modifier = Modifier.width(columnWidth * 2))

            CellText(
              text = module.team.name,
              modifier = Modifier.width(columnWidth)
                .clickable { moduleModel.teamTab.select() }
            )
            CellWeight(module.legacyModuleWeight, columnWidth)
            CellWeight(module.adoptAnvilWeight, columnWidth)
            CellWeight(
              module.tooGranularWeight,
              columnWidth,
              text = "${module.fileCount} files"
            )
            CellWeight(module.workflow0Weight, columnWidth)
            CellWeight(module.reactorWeight, columnWidth)
            CellWeight(module.rxJava1Weight, columnWidth)

            CellWeight(
              module.kotlinJavaWeight, columnWidth,
              text = when {
                module.fileCount <= 0 -> "No source code"
                module.javaFiles <= 0 -> "Kotlin only"
                module.kotlinFiles <= 0 -> "Java only"
                else -> "Mixed"
              }
            )

            CellWeight(module.kaptWeight, columnWidth)
            CellWeight(
              module.disabledModuleChecksWeight, columnWidth,
              text = if (module.disabledModuleChecks == 0) "" else "${module.disabledModuleChecks} checks"
            )
            CellWeight(
              module.disabledUnitTestsWeight, columnWidth,
              text = if (module.disabledUnitTests == 0) "" else "${module.disabledUnitTests} unit tests"
            )
            CellWeight(
              module.disabledUiTestsWeight, columnWidth,
              text = if (module.disabledUiTests == 0) "" else "${module.disabledUiTests} UI tests"
            )

            CellWeight(
              module.locWeight, columnWidth,
              text = module.loc.toStringCommaSeparated()
            )


            CellWeight(
              module.clocWeight, columnWidth,
              text = module.cloc.toStringCommaSeparated()
            )

            CellWeight(
              module.devAppWeight, columnWidth,
              text = if (module.isDemoModule()) module.cloc.toStringCommaSeparated() else ""
            )
          }

          Row(modifier = Modifier.height(Min)) {
            Text(
              text = module.path,
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
      rememberScrollbarAdapter(columnScrollState, screen.modules.size, rowHeight),
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

@Composable fun HorizontalFadingEdge() {
  Spacer(
    modifier = Modifier
      .width(8.dp)
      .fillMaxHeight()
      .background(
        brush = Brush.horizontalGradient(
          colors = listOf(
            AppTheme.colors.material.surface,
            Color.Transparent
          )
        )
      )
  )
}

@Composable fun VerticalFadingEdge(
  width: Dp
) {
  Spacer(
    Modifier
      .width(width)
      .height(8.dp)
      .background(
        brush = Brush.verticalGradient(
          colors = listOf(
            AppTheme.colors.material.surface,
            Color.Transparent
          )
        )
      )
  )
}

@Composable private fun HeaderCell(
  text: String,
  width: Dp,
  column: ModuleColumn,
  screen: ScreenModel
) {
  Column(
    modifier = Modifier.background(AppTheme.colors.material.surface)
      .width(width)
      .clickable {
        if (screen.moduleSortSelection.column == column) {
          screen.changeModuleSort(order = screen.moduleSortSelection.order.next())
        } else {
          screen.changeModuleSort(column = column)
        }
      },
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(text, fontWeight = FontWeight.Bold)

      val showSort = screen.moduleSortSelection.column == column

      val alpha by animateFloatAsState(if (showSort) 1f else 0f)
      val rotation by animateFloatAsState(if (screen.moduleSortSelection.order == A_Z) 0f else 180f)

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
  modifier: Modifier
) {
  Text(
    text = text,
    modifier = modifier
      .border(1.dp, Color.White)
      .padding(8.dp)
      .horizontalScroll(rememberScrollState()),
    maxLines = 1,
    softWrap = false
  )
}

@Composable private fun CellWeight(
  weight: AttributeWeight,
  width: Dp,
  text: String? = null
) {
  Column(
    modifier = Modifier.width(width)
      .fillMaxHeight()
      .background(weight.color)
      .border(1.dp, Color.White)
      .padding(8.dp),
    horizontalAlignment = Alignment.End,
    verticalArrangement = Arrangement.Center,
  ) {
    Text(
      text = text.orEmpty(),
      color = Color.Black,
      fontFamily = FontFamily.Monospace,
      fontSize = 12.sp
    )
  }
}

private fun Int.toStringCommaSeparated(): String = String.format(Locale.US, "%,d", this)
