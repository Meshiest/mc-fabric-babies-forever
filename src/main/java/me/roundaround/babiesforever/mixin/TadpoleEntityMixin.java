package me.roundaround.babiesforever.mixin;

import me.roundaround.babiesforever.AgeInhibitorExt;
import me.roundaround.babiesforever.BabiesForeverMod;
import net.minecraft.entity.passive.TadpoleEntity;
import net.minecraft.nbt.NbtCompound;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TadpoleEntity.class)
public abstract class TadpoleEntityMixin implements AgeInhibitorExt {

  private boolean inhibited;

  @Shadow
  private int tadpoleAge;

  @Shadow
  private int getTadpoleAge() {
    return 0;
  }

  @Inject(method = "setTadpoleAge", at = @At(value = "HEAD"), cancellable = true)
  public void setBreedingAge(int age, CallbackInfo info) {
    TadpoleEntity self = ((TadpoleEntity) (Object) this);
    if ((self.hasCustomName() || inhibited) && !BabiesForeverMod.isNameBypass(self) &&
        age >= TadpoleEntity.MAX_TADPOLE_AGE) {
      this.tadpoleAge = TadpoleEntity.MAX_TADPOLE_AGE - 1;
      info.cancel();
    }
  }

  public void setInhibited() {
    inhibited = true;
  }

  public boolean getInhibited() {
    return inhibited;
  }

  @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
  public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo info) {
    if (inhibited)
      nbt.putBoolean("inhibited", true);
  }

  @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
  public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo info) {
    inhibited = nbt.contains("inhibited") && nbt.getBoolean("inhibited");
  }
}
