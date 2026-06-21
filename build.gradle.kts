plugins {
  id("me.roundaround.allay")
}

allay {
  displayName.set("Window Size")
  description.set("Permanently modify the Minecraft game's (non-fullscreen) window size without having to edit options.txt!")
  authors.set(listOf("Roundaround"))
  license.set("MIT")
  homepage.set("https://modrinth.com/mod/window-size")
  repository.set("https://github.com/Roundaround/mc-window-size")
  issues.set("https://github.com/Roundaround/mc-window-size/issues")
  logoFile.set("assets/windowsize/banner.png")

  gametest {
    // Acknowledge the Minecraft EULA for the throwaway worlds the headless
    // server game test spins up.
    eula.set(true)
  }

  modrinth {
    projectId.set("window-size")
  }

  curseforge {
    projectId.set(1506384)
  }

  release {
    versionType.set("release")
    minecraftVersions("26.2")
    changelogDir.set(file("changelogs"))
  }
}
