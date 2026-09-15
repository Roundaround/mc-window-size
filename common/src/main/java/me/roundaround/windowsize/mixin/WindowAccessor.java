package me.roundaround.windowsize.mixin;

import com.mojang.blaze3d.platform.Window;
import me.roundaround.allay.api.MixinEnv;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Window.class)
@MixinEnv(MixinEnv.Env.CLIENT)
public interface WindowAccessor {
  // 26.3 dropped the public isFullscreen(); the field still tracks the applied state.
  @Accessor("fullscreen")
  boolean getFullscreen();

  @Accessor
  void setWindowedWidth(int windowedWidth);

  @Accessor
  void setWindowedHeight(int windowedHeight);
}
