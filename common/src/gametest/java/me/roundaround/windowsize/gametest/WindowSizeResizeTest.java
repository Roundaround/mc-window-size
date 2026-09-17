package me.roundaround.windowsize.gametest;

import com.mojang.blaze3d.platform.Window;
import me.roundaround.allay.api.gametest.ClientGameTest;
import me.roundaround.trove.gametest.ClientTest;
import me.roundaround.trove.gametest.ClientTestContext;
import me.roundaround.trove.gametest.GameTestAssertionException;
import org.lwjgl.sdl.SDLVideo;

/** Resizes the window and checks the new size lands in options.overrideWidth/Height. */
@ClientGameTest
public class WindowSizeResizeTest implements ClientTest {
  private static final int WIDTH = 1000;
  private static final int HEIGHT = 700;

  @Override
  public void runTest(ClientTestContext context) {
    context.waitTicks(20);

    // bare size request, not Window.setWindowed — COSMIC over XWayland reverts the latter
    context.runOnClient((mc) -> {
      long handle = mc.getWindow().handle();
      SDLVideo.SDL_SetWindowSize(handle, WIDTH, HEIGHT);
      SDLVideo.SDL_SyncWindow(handle);
    });
    context.waitFor((mc) -> mc.getWindow().getScreenWidth() == WIDTH && mc.getWindow().getScreenHeight() == HEIGHT);
    context.waitTicks(5);

    String mismatch = context.computeOnClient((mc) -> {
      Window window = mc.getWindow();
      if (mc.options.overrideWidth == window.getScreenWidth() && mc.options.overrideHeight == window.getScreenHeight()) {
        return null;
      }
      return "window=" + window.getScreenWidth() + "x" + window.getScreenHeight() + " override="
          + mc.options.overrideWidth + "x" + mc.options.overrideHeight;
    });
    if (mismatch != null) {
      throw new GameTestAssertionException("override size did not track the resized window: " + mismatch);
    }
  }
}
