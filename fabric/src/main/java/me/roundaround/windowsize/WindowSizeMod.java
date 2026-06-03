package me.roundaround.windowsize;

import me.roundaround.allay.api.Entrypoint;
import net.fabricmc.api.ClientModInitializer;

@Entrypoint(Entrypoint.CLIENT)
public final class WindowSizeMod implements ClientModInitializer {
  @Override
  public void onInitializeClient() {
    // Mixin-only client mod: the resolution option is wired entirely via mixins.
    // Trove's Fabric core self-bootstraps, so there is nothing to register here.
  }
}
