package me.roundaround.windowsize.forge;

import me.roundaround.trove.forge.TroveForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("windowsize")
public final class WindowSizeForgeMod {
  public WindowSizeForgeMod(FMLJavaModLoadingContext context) {
    TroveForge.bootstrap(context);
    // Mixin-only client mod: nothing else to register.
  }
}
