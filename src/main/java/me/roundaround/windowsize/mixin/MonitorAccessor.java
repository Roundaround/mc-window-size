package me.roundaround.windowsize.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import com.mojang.blaze3d.platform.Monitor;
import com.mojang.blaze3d.platform.VideoMode;
import java.util.List;

@Mixin(Monitor.class)
public interface MonitorAccessor {
  @Accessor
  List<VideoMode> getVideoModes();
}
