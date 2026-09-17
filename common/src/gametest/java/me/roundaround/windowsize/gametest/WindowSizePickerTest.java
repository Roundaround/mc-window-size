package me.roundaround.windowsize.gametest;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import me.roundaround.allay.api.gametest.ClientGameTest;
import me.roundaround.trove.gametest.ClientTest;
import me.roundaround.trove.gametest.ClientTestContext;
import me.roundaround.trove.gametest.GameTestAssertionException;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.screens.options.VideoSettingsScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.input.MouseButtonInfo;
import net.minecraft.network.chat.Component;

/** Picks a size on the Video Settings slider and checks the window takes it once the screen closes. */
@ClientGameTest
public class WindowSizePickerTest implements ClientTest {
  @Override
  public void runTest(ClientTestContext context) {
    context.waitTicks(20);
    context.setScreen(() -> {
      var mc = context.minecraft();
      return new VideoSettingsScreen(null, mc, mc.options);
    });
    context.waitForScreen(VideoSettingsScreen.class);
    context.waitTicks(5);

    String label = Component.translatable("windowsize.options.resolution").getString();
    OptionInstance.OptionInstanceSliderButton<?> slider = context.widgets(OptionInstance.OptionInstanceSliderButton.class)
        .stream()
        .filter((widget) -> widget.getMessage().getString().startsWith(label))
        .findFirst()
        .orElseThrow(() -> new GameTestAssertionException("no window size slider on the Video Settings screen"));

    int[] before = context.computeOnClient(WindowSizePickerTest::screenSize);

    // small modes that fit any desktop; the second covers a window already at the first
    int[] picked = before;
    for (double fraction : new double[]{0.2, 0.4}) {
      context.runOnClient((mc) -> slider.onClick(
          new MouseButtonEvent(
              slider.getX() + slider.getWidth() * fraction,
              slider.getY() + slider.getHeight() / 2.0,
              new MouseButtonInfo(InputConstants.MOUSE_BUTTON_LEFT, 0)
          ), false
      ));
      // slider values apply on a 600ms debounce
      context.waitTicks(30);

      picked = context.computeOnClient((mc) -> new int[]{mc.options.overrideWidth, mc.options.overrideHeight});
      if (picked[0] != before[0] || picked[1] != before[1]) {
        break;
      }
    }
    if (picked[0] == before[0] && picked[1] == before[1]) {
      throw new GameTestAssertionException("slider clicks did not pick a new size: " + picked[0] + "x" + picked[1]);
    }

    context.runOnClient((mc) -> mc.gui.screen().onClose());
    context.waitTicks(40);

    int[] after = context.computeOnClient(WindowSizePickerTest::screenSize);
    if (after[0] != picked[0] || after[1] != picked[1]) {
      throw new GameTestAssertionException(
          "picked " + picked[0] + "x" + picked[1] + " but the window is " + after[0] + "x" + after[1]);
    }
  }

  private static int[] screenSize(net.minecraft.client.Minecraft mc) {
    Window window = mc.getWindow();
    return new int[]{window.getScreenWidth(), window.getScreenHeight()};
  }
}
