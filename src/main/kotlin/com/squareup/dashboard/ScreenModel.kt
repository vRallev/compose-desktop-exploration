package com.squareup.dashboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import com.squareup.dashboard.ModuleColumn.ADOPT_ANVIL
import com.squareup.dashboard.ModuleColumn.CLOC
import com.squareup.dashboard.ModuleColumn.DISABLED_CI_CHECKS
import com.squareup.dashboard.ModuleColumn.DISABLED_UI_TESTS
import com.squareup.dashboard.ModuleColumn.DISABLED_UNIT_TESTS
import com.squareup.dashboard.ModuleColumn.KAPT
import com.squareup.dashboard.ModuleColumn.KOTLIN_JAVA
import com.squareup.dashboard.ModuleColumn.LARGE_DEV_APP
import com.squareup.dashboard.ModuleColumn.LEGACY_MODULE
import com.squareup.dashboard.ModuleColumn.LOC
import com.squareup.dashboard.ModuleColumn.MODULE
import com.squareup.dashboard.ModuleColumn.REACTOR_MODULE
import com.squareup.dashboard.ModuleColumn.RXJAVA1_MODULE
import com.squareup.dashboard.ModuleColumn.TEAM
import com.squareup.dashboard.ModuleColumn.TOO_GRANULAR
import com.squareup.dashboard.ModuleColumn.WORKFLOW0_MODULE
import com.squareup.dashboard.SortOrder.A_Z
import com.squareup.dashboard.SortOrder.Z_A
import com.squareup.dashboard.data.Module
import com.squareup.dashboard.data.Team

class ScreenModel(
  modules: List<Module>
) {
  private val _selectedTab = TabSelection<Tab>()
  val selectedTab: Tab
    get() = _selectedTab.selected!!

  val moduleSortSelection = ModuleSortSelection()
  val teamSortSelection = TeamSortSelection()

  val allModulesTab = AllModulesTab(_selectedTab)
  val allTeamsTab = AllTeamsTab(_selectedTab, TeamSummary.fromModules(modules))

  val teamTabs = modules
    .distinctBy { it.team }
    .sortedBy { it.team.name }
    .map { TeamTab(it.team, _selectedTab) }

  val modules = modules
    .map { module ->
      ModuleModel(module, teamTabs.single { teamTab ->
        teamTab.team == module.team
      })
    }
    .toMutableStateList()

  init {
    _selectedTab.selected = allModulesTab
    // _selectedTab.selected = allTeamsTab
    // _selectedTab.selected = teamTabs.first()
  }

  fun isAllModulesTabSelected(): Boolean = _selectedTab.selected === allModulesTab
  fun isAllTeamsTabSelected(): Boolean = _selectedTab.selected === allTeamsTab

  fun changeModuleSort(
    column: ModuleColumn = moduleSortSelection.column,
    order: SortOrder = moduleSortSelection.order
  ) {
    moduleSortSelection.column = column
    moduleSortSelection.order = order

    // val selector: (ModuleModel) -> Comparable<*> = { it.module.path }

    val selector: (ModuleModel) -> Comparable<*> = when (column) {
      MODULE -> {
        { it.module.path }
      }
      TEAM -> {
        { it.module.team.name }
      }
      LEGACY_MODULE -> {
        { it.module.legacyModule }
      }
      ADOPT_ANVIL -> {
        { it.module.adoptAnvil }
      }
      TOO_GRANULAR -> {
        { it.module.tooGranular }
      }
      WORKFLOW0_MODULE -> {
        { it.module.workflow0 }
      }
      REACTOR_MODULE -> {
        { it.module.reactor }
      }
      RXJAVA1_MODULE -> {
        { it.module.rxJava1 }
      }
      KOTLIN_JAVA -> {
        { it.module.kotlinJavaWeight }
      }
      KAPT -> {
        { it.module.kaptWeight }
      }
      DISABLED_CI_CHECKS -> {
        { it.module.disabledModuleChecks }
      }
      DISABLED_UNIT_TESTS -> {
        { it.module.disabledUnitTests }
      }
      DISABLED_UI_TESTS -> {
        { it.module.disabledUiTests }
      }
      LOC -> {
        { it.module.loc }
      }
      CLOC -> {
        { it.module.cloc }
      }
      LARGE_DEV_APP -> {
        { it.module.devAppWeight }
      }
    }

    when (order) {
      A_Z -> modules.sortWith(compareBy(selector, { it.module.path }))
      Z_A -> modules.sortWith(compareByDescending(selector).thenBy { it.module.path })
    }
  }

  fun changeTeamSort(
    column: TeamColumn = teamSortSelection.column,
    order: SortOrder = teamSortSelection.order
  ) {
    teamSortSelection.column = column
    teamSortSelection.order = order

    val selector: (TeamSummary) -> Comparable<*> = when (column) {
      TeamColumn.TEAM -> {
        { it.team.name }
      }
      TeamColumn.OWNED_MODULES -> {
        { it.ownedModules }
      }
      TeamColumn.OWNED_LOC -> {
        { it.ownedLoc }
      }
      TeamColumn.DEV_APPS -> {
        { it.devApps }
      }
      TeamColumn.LEGACY_MODULES -> {
        { it.legacyModules }
      }
      TeamColumn.RED_CELLS -> {
        { it.redCells }
      }
      TeamColumn.RED_CELLS_PER_MODULE -> {
        { it.redCellsPerModule }
      }
    }

    when (order) {
      A_Z -> allTeamsTab.teamSummaries.sortWith(compareBy(selector, { it.team.name }))
      Z_A -> allTeamsTab.teamSummaries.sortWith(compareByDescending(selector).thenBy { it.team.name })
    }
  }
}

class ModuleModel(
  val module: Module,
  val teamTab: TeamTab
)

sealed class Tab(
  private val selectedTab: TabSelection<Tab>
) {
  fun isSelected(): Boolean = selectedTab.selected === this
  fun select() {
    selectedTab.selected = this
  }
}

class AllModulesTab(
  selectedTab: TabSelection<Tab>
) : Tab(selectedTab)

class AllTeamsTab(
  selectedTab: TabSelection<Tab>,
  teamSummaries: List<TeamSummary>
) : Tab(selectedTab) {
  val teamSummaries = teamSummaries.toMutableStateList()
}

class TeamTab(
  val team: Team,
  selectedTab: TabSelection<Tab>
) : Tab(selectedTab) {
  val name = team.name
}

class TabSelection<T : Tab> {
  var selected: T? by mutableStateOf(null)
}

class ModuleSortSelection {
  var column: ModuleColumn by mutableStateOf(MODULE)
  var order: SortOrder by mutableStateOf(A_Z)
}

class TeamSortSelection {
  var column: TeamColumn by mutableStateOf(TeamColumn.TEAM)
  var order: SortOrder by mutableStateOf(A_Z)
}

enum class ModuleColumn(
  val header: String
) {
  MODULE("Module"),
  TEAM("Team"),
  LEGACY_MODULE("Legacy Module"),
  ADOPT_ANVIL("Adopt Anvil"),
  TOO_GRANULAR("Too Granular"),
  WORKFLOW0_MODULE("Workflow0 Module"),
  REACTOR_MODULE("Reactor Module"),
  RXJAVA1_MODULE("RxJava 1"),
  KOTLIN_JAVA("Kotlin/Java"),
  KAPT("KAPT"),
  DISABLED_CI_CHECKS("Disabled CI Checks"),
  DISABLED_UNIT_TESTS("Disabled Unit Tests"),
  DISABLED_UI_TESTS("Disabled UI Tests"),
  LOC("LOC"),
  CLOC("Cumulative LOC"),
  LARGE_DEV_APP("Large Dev App"),
}

enum class TeamColumn(
  val header: String
) {
  TEAM("Team"),
  OWNED_MODULES("Owned Modules"),
  OWNED_LOC("Owned LOC"),
  DEV_APPS("Dev Apps"),
  LEGACY_MODULES("Legacy Modules"),
  RED_CELLS("Violations"),
  RED_CELLS_PER_MODULE("Violation Average"),
}

enum class SortOrder {
  A_Z,
  Z_A;

  fun next(): SortOrder {
    return values()[(ordinal + 1) % values().size]
  }
}

data class TeamSummary(
  val team: Team,
  val ownedModules: Int,
  val ownedLoc: Int,
  val devApps: Int,
  val legacyModules: Int,
  val redCells: Int,
) {
  val redCellsPerModule: Double = redCells / ownedModules.toDouble()

  companion object {
    fun fromModules(modules: List<Module>): List<TeamSummary> {
      val teams = modules.map { it.team }.distinct()
      val modulesPerTeam = teams.associateWith { team -> modules.filter { it.team == team } }

      return teams
        .map { team ->
          val teamModules = modulesPerTeam.getValue(team)
          TeamSummary(
            team = team,
            ownedModules = teamModules.size,
            ownedLoc = teamModules.sumOf { it.loc },
            devApps = teamModules.count { it.isDemoModule() },
            legacyModules = teamModules.count { it.legacyModule },
            redCells = teamModules.sumOf { it.redCells },
          )
        }
        .sortedBy { it.team.name }
    }
  }
}