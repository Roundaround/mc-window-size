package me.roundaround.windowsize.mixin;

import com.mojang.blaze3d.platform.Window;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Window.class)
public interface WindowAccessor {
  @Accessor
  void setWindowedWidth(int windowedWidth);

  @Accessor
  void setWindowedHeight(int windowedHeight);
}
