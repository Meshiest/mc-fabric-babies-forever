package me.roundaround.babiesforever;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TadpoleEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class InhibitorCallback implements UseEntityCallback {
  protected final Random random = Random.create();

  @Override
  public ActionResult interact(PlayerEntity player, World world, Hand hand, Entity entity,
      @Nullable EntityHitResult hitResult) {

    var itemStack = player.getStackInHand(hand);

    // poisonous potato only
    if (Items.POISONOUS_POTATO != itemStack.getItem())
      return ActionResult.PASS;

    // living entities and tadpoles only
    if (!entity.isAlive() || !(entity instanceof LivingEntity || entity instanceof TadpoleEntity))
      return ActionResult.PASS;

    // can't impact non-baby animals
    if (entity instanceof LivingEntity && !((LivingEntity) entity).isBaby())
      return ActionResult.PASS;

    // already inhibited
    if (((AgeInhibitorExt) entity).getInhibited()) {
      return ActionResult.PASS;
    }

    // inhibit growth
    ((AgeInhibitorExt) entity).setInhibited();

    // consume the item outside of creative
    if (!player.isCreative())
      itemStack.decrement(1);

    // play a nice sound and particle
    world.playSoundFromEntity(player, entity, SoundEvents.BLOCK_HONEY_BLOCK_BREAK, SoundCategory.PLAYERS, 1.0f,
        .2f);

    for (int i = 0; i < 7; ++i) {
      double d = random.nextGaussian() * 0.01;
      double e = random.nextGaussian() * 0.01;
      double f = random.nextGaussian() * 0.01;
      world.addParticle(ParticleTypes.POOF, entity.getParticleX(1.0), entity.getRandomBodyY() + 0.2,
          entity.getParticleZ(1.0), d, e, f);
      world.addParticle(ParticleTypes.FALLING_HONEY, entity.getParticleX(1.0), entity.getRandomBodyY() + 0.2,
          entity.getParticleZ(1.0), d, e, f);
    }

    return ActionResult.CONSUME;
  }
}
