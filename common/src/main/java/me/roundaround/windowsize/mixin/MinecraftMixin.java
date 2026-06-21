package me.roundaround.windowsize.mixin;

import com.mojang.blaze3d.platform.Window;
import me.roundaround.allay.api.MixinEnv;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.options.VideoSettingsScreen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
@MixinEnv(MixinEnv.Env.CLIENT)
public abstract class MinecraftMixin {
  @Shadow
  @Final
  public Options options;

  @Shadow
  @Final
  private Window window;

  @Shadow
  @Final
  public Gui gui;

  @Inject(method = "resizeGui", at = @At("RETURN"))
  private void afterResolutionChanged(CallbackInfo ci) {
    if (this.window.isFullscreen()) {
      return;
    }

    this.options.overrideWidth = this.window.getScreenWidth();
    this.options.overrideHeight = this.window.getScreenHeight();

    if (this.gui.screen() instanceof VideoSettingsScreen currScreen) {
      currScreen.windowsize$onResolutionChange(this.options.overrideWidth, this.options.overrideHeight);
    }
  }
}
