package iskallia.vault.mixin;

import net.minecraft.entity.player.PlayerInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(PlayerInventory.class)
public class MixinPlayerInventory {

    @ModifyArg(method = "func_234563_a_", index = 0, at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;damageItem(ILnet/minecraft/entity/LivingEntity;Ljava/util/function/Consumer;)V"))
    public int limitMaxArmorDamage(int damageAmount) {
        return Math.min(damageAmount, 5); // Allow maximum of 5 armor damage
    }

}
