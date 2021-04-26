package com.squareup.dashboard

import androidx.compose.desktop.Window
import androidx.compose.ui.unit.IntSize
import com.squareup.dashboard.data.Module
import com.squareup.dashboard.data.Team
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import kotlin.random.Random

fun main() = Window(
  title = "Module Dashboard",
  size = IntSize(1280, 768),
  icon = loadImageResource("ic_launcher.png"),
) {
  val csvFileUrl = Thread.currentThread().contextClassLoader.getResource("all.csv")
  val screenModel = if (csvFileUrl == null) {
    fakeData()
  } else {
    parseCsv(csvFileUrl.openStream().bufferedReader().use { it.readText() })
  }

  MainView(screenModel)
}

private fun fakeData(): ScreenModel {
  val teams = List(20) {
    Team("Team ${it.toString().padStart(2, '0')}")
  }

  return ScreenModel(
    modules = List(80) {
      Module(
        path = if (Random.nextBoolean()) ":$it:public" else ":$it:demo",
        team = teams[it % teams.size],
        legacyModule = Random.nextBoolean(),
        adoptAnvil = Random.nextBoolean(),
        tooGranular = Random.nextBoolean(),
        workflow0 = Random.nextBoolean(),
        reactor = Random.nextBoolean(),
        rxJava1 = Random.nextBoolean(),
        kapt = Random.nextBoolean(),
        disabledModuleChecks = Random.nextInt(3),
        disabledUnitTests = Random.nextInt(3),
        disabledUiTests = Random.nextInt(3),
        kotlinFiles = Random.nextInt(20),
        javaFiles = Random.nextInt(5),
        loc = Random.nextInt(20_000) + 100,
        cloc = Random.nextInt(1_000_000),
      )
    }
  )
}

private fun parseCsv(csv: String): ScreenModel {
  val modules = csv
    .lineSequence()
    .drop(1)
    .map { line ->
      val elements = line.split(",")

      Module(
        path = elements[0],
        team = Team(elements[1]),
        legacyModule = elements[2].toBoolean(),
        adoptAnvil = elements[3].toBoolean(),
        tooGranular = elements[4].toBoolean(),
        workflow0 = elements[5].toBoolean(),
        reactor = elements[6].toBoolean(),
        rxJava1 = elements[7].toBoolean(),
        kapt = elements[8].toBoolean(),
        disabledModuleChecks = elements[9].toInt(),
        disabledUnitTests = elements[10].toInt(),
        disabledUiTests = elements[11].toInt(),
        kotlinFiles = elements[12].toInt(),
        javaFiles = elements[13].toInt(),
        loc = elements[14].toInt(),
        cloc = elements[15].toInt(),
      )
    }
    .sortedBy { it.path }

  return ScreenModel(modules.toList())
}

@Suppress("SameParameterValue")
private fun loadImageResource(path: String): BufferedImage {
  val resource = Thread.currentThread().contextClassLoader.getResource(path)
  requireNotNull(resource) { "Resource $path not found" }
  return resource.openStream().use(ImageIO::read)
}
