package me.roundaround.babiesforever;

import net.minecraft.entity.Entity;
import net.minecraft.util.Formatting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import java.util.Locale;

public final class BabiesForeverMod implements ModInitializer {
  public static final String MOD_ID = "babiesforever";
  public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

  @Override
  public void onInitialize() {
    UseEntityCallback.EVENT.register(new InhibitorCallback());
  }

  public static boolean isNameBypass(Entity entity) {
    if (!entity.hasCustomName()) {
      return false;
    }
    String name = Formatting.strip(entity.getName().getString()).toLowerCase(Locale.ROOT);
    return "growup".equals(name) || "grow up".equals(name) || "grow_up".equals(name);
  }
}
