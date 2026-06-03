package me.roundaround.windowsize;

import me.roundaround.allay.api.InjectedInterface;

@InjectedInterface
public interface VideoOptionsScreenExtensions {
  default void windowsize$onResolutionChange(int width, int height) {
  }
}
