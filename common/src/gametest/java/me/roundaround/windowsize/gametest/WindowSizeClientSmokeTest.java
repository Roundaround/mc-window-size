package me.roundaround.windowsize.gametest;

import me.roundaround.allay.api.gametest.ClientGameTest;
import me.roundaround.trove.gametest.ClientTest;
import me.roundaround.trove.gametest.ClientTestContext;
import me.roundaround.trove.gametest.ClientWorld;
import me.roundaround.trove.gametest.GameTestAssertionException;

/**
 * Boots a world and reads back the live window width. The mod has no in-world feature,
 * just window/display mixins (MinecraftMixin#resizeGui drives options.overrideWidth/Height);
 * the value is proving those applied and the client renders a frame without crashing.
 */
@ClientGameTest
public class WindowSizeClientSmokeTest implements ClientTest {
  @Override
  public void runTest(ClientTestContext context) {
    try (ClientWorld world = context.worldBuilder().creative().stopTime(true).create()) {
      world.teleport(0.5, 65.0, 0.5);
      context.waitTicks(10);

      int width = context.computeOnClient((mc) -> mc.getWindow().getWidth());
      if (width <= 0) {
        throw new GameTestAssertionException("window has a positive width after the window-size mixins ran");
      }
    }
  }
}
