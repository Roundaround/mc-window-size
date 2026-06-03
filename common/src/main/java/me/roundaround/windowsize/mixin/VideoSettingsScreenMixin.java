package me.roundaround.windowsize.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.platform.Monitor;
import com.mojang.blaze3d.platform.Window;
import me.roundaround.allay.api.MixinEnv;
import me.roundaround.windowsize.Resolution;
import me.roundaround.windowsize.VideoOptionsScreenExtensions;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.client.gui.screens.options.VideoSettingsScreen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(VideoSettingsScreen.class)
@MixinEnv(MixinEnv.Env.CLIENT)
public abstract class VideoSettingsScreenMixin extends OptionsSubScreen implements VideoOptionsScreenExtensions {
  @Unique
  @Nullable
  private List<Resolution> resolutions;

  @Unique
  @Nullable
  private OptionInstance<Integer> resolutionOption;

  @Unique
  private boolean dirty;

  @Inject(
      method = "addOptions", at = @At(
      value = "INVOKE",
      target = "Lnet/minecraft/client/gui/components/OptionsList;addBig(Lnet/minecraft/client/OptionInstance;)V",
      ordinal = 0,
      shift = At.Shift.AFTER
  )
  )
  private void afterFullscreenResolutionOptionAdded(CallbackInfo ci, @Local Monitor monitor, @Local Window window) {
    this.resolutions = this.getAvailableResolutions(monitor);

    this.resolutionOption = new OptionInstance<>(
        "windowsize.options.resolution", OptionInstance.noTooltip(), (optionText, value) -> {
      if (this.getResolutions().isEmpty()) {
        return Component.translatable("windowsize.options.resolution.unavailable");
      }

      Resolution resolution = this.getSelectedResolution(value);
      if (resolution == null) {
        return Options.genericValueLabel(optionText, Component.translatable("windowsize.options.resolution.current"));
      }

      return Options.genericValueLabel(
          optionText,
          Component.translatable("windowsize.options.resolution.entry", resolution.width(), resolution.height())
      );
    }, new OptionInstance.IntRange(-1, this.getResolutions().size() - 1), this.getCurrentIndex(), (value) -> {

      Resolution resolution = this.getSelectedResolution(value);
      if (resolution == null) {
        this.dirty = false;
        return;
      }

      this.dirty = true;
      this.options.overrideWidth = resolution.width();
      this.options.overrideHeight = resolution.height();
    }
    );

    assert this.list != null;
    this.list.addBig(this.resolutionOption);
  }

  @Inject(method = "onClose", at = @At("HEAD"))
  private void onClose(CallbackInfo ci) {
    if (!this.dirty || this.minecraft == null || this.resolutionOption == null) {
      return;
    }

    Window window = this.minecraft.getWindow();
    if (window == null) {
      return;
    }

    Resolution resolution = this.getSelectedResolution(this.resolutionOption.get());
    if (resolution == null) {
      return;
    }

    if (!window.isFullscreen()) {
      window.setWindowed(resolution.width(), resolution.height());
    } else {
      ((WindowAccessor) (Object) window).setWindowedWidth(resolution.width());
      ((WindowAccessor) (Object) window).setWindowedHeight(resolution.height());
    }
  }

  @Override
  public void windowsize$onResolutionChange(int width, int height) {
    if (this.resolutionOption == null) {
      return;
    }
    this.resolutionOption.set(this.getCurrentIndex());
  }

  @Unique
  private List<Resolution> getAvailableResolutions(@Nullable Monitor monitor) {
    if (monitor == null) {
      return List.of();
    }

    return ((MonitorAccessor) (Object) monitor).getVideoModes()
        .stream()
        .map((videoMode) -> new Resolution(videoMode.getWidth(), videoMode.getHeight()))
        .distinct()
        .toList();
  }

  @Unique
  private List<Resolution> getResolutions() {
    if (this.resolutions == null) {
      return List.of();
    }
    return this.resolutions;
  }

  @Unique
  private int getCurrentIndex() {
    if (this.resolutions == null || this.resolutions.isEmpty()) {
      return -1;
    }
    return this.resolutions.indexOf(new Resolution(this.options.overrideWidth, this.options.overrideHeight));
  }

  @Unique
  @Nullable
  private Resolution getSelectedResolution(int index) {
    List<Resolution> resolutions = this.getResolutions();
    if (index < 0 || index >= resolutions.size()) {
      return null;
    }
    return resolutions.get(index);
  }

  private VideoSettingsScreenMixin(Screen parent, Options gameOptions, Component title) {
    super(parent, gameOptions, title);
  }
}
