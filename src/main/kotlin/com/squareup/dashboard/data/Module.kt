package com.squareup.dashboard.data

import com.squareup.dashboard.data.AttributeWeight.Companion.toWeight
import com.squareup.dashboard.data.AttributeWeight.GREEN
import com.squareup.dashboard.data.AttributeWeight.NONE
import com.squareup.dashboard.data.AttributeWeight.ORANGE
import com.squareup.dashboard.data.AttributeWeight.RED
import com.squareup.dashboard.data.AttributeWeight.YELLOW

data class Module(
  val path: String,
  val team: Team,
  val legacyModule: Boolean,
  val adoptAnvil: Boolean,
  val tooGranular: Boolean,
  val workflow0: Boolean,
  val reactor: Boolean,
  val rxJava1: Boolean,
  val kapt: Boolean,
  val disabledModuleChecks: Int,
  val disabledUnitTests: Int,
  val disabledUiTests: Int,
  val kotlinFiles: Int,
  val javaFiles: Int,
  val loc: Int,
  val cloc: Int,
) {
  val fileCount = kotlinFiles + javaFiles

  val legacyModuleWeight = legacyModule.not().toWeight()
  val adoptAnvilWeight = adoptAnvil.not().toWeight()
  val tooGranularWeight = tooGranular.not().toWeight()
  val workflow0Weight = workflow0.not().toWeight()
  val reactorWeight = reactor.not().toWeight()
  val rxJava1Weight = rxJava1.not().toWeight()

  val kotlinJavaWeight = when {
    javaFiles <= 0 -> GREEN
    kotlinFiles <= 0 -> YELLOW
    else -> RED
  }

  val kaptWeight = if (isDemoModule()) GREEN else kapt.not().toWeight()

  val disabledModuleChecksWeight = (disabledModuleChecks == 0).toWeight()
  val disabledUnitTestsWeight = (disabledUnitTests == 0).toWeight()
  val disabledUiTestsWeight = (disabledUiTests == 0).toWeight()

  val locWeight = when {
    loc < 3_000 -> GREEN
    loc < 5_000 -> YELLOW
    loc < 10_000 -> ORANGE
    else -> RED
  }

  val clocWeight = when {
    cloc < 300_000 -> GREEN
    cloc < 500_000 -> YELLOW
    cloc < 700_000 -> ORANGE
    else -> RED
  }

  val devAppWeight = when {
    !isDemoModule() -> NONE
    cloc < 400_000 -> GREEN
    cloc < 500_000 -> YELLOW
    cloc < 600_000 -> ORANGE
    else -> RED
  }

  val redCells: Int = listOf(
    legacyModuleWeight, adoptAnvilWeight, tooGranularWeight, workflow0Weight, reactorWeight,
    rxJava1Weight, kotlinJavaWeight, kaptWeight, disabledModuleChecksWeight,
    disabledUnitTestsWeight, disabledUiTestsWeight, locWeight, clocWeight, devAppWeight
  ).filter { it == RED }.size

  fun isDemoModule(): Boolean = path.substringAfterLast(':').startsWith("demo")
}
