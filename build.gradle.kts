plugins {
  id("me.roundaround.allay")
}

allay {
  displayName.set("Window Size")
  description.set("Permanently modify the Minecraft game's (non-fullscreen) window size without having to edit options.txt!")
  authors.set(listOf("Roundaround"))
  license.set("MIT")
  homepage.set("https://modrinth.com/mod/window-size")
  repository.set("https://github.com/Roundaround/mc-fabric-window-size")
  issues.set("https://github.com/Roundaround/mc-fabric-window-size/issues")
  logoFile.set("assets/windowsize/icon.png")

  modrinth {
    projectId.set("window-size")
  }

  release {
    versionType.set("release")
    sourcesJar.set(true)
  }
}
