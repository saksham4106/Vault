package iskallia.vault.skill.ability.type;

import com.google.gson.annotations.Expose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;

public class RampageAbility extends EffectAbility {

    @Expose private int durationTicks;
    @Expose private int damageIncrease;

    public int getDurationTicks() {
        return durationTicks;
    }

    public int getDamageIncrease() {
        return damageIncrease;
    }

    public RampageAbility(int cost, Effect effect, int level, int damageIncrease, int durationTicks, int cooldown, Type type, Behavior behavior) {
        super(cost, effect, level, type, behavior);
        this.damageIncrease = damageIncrease;
        this.durationTicks = durationTicks;
        this.cooldown = cooldown;
    }

    @Override
    public void onTick(PlayerEntity player, boolean active) {}

    @Override
    public void onAction(PlayerEntity player, boolean active) {
        EffectInstance activeEffect = player.getActivePotionEffect(this.getEffect());
        EffectInstance newEffect = new EffectInstance(this.getEffect(),
                getDurationTicks(), this.getAmplifier(), false,
                this.getType().showParticles, this.getType().showIcon);

        if (activeEffect == null) {
            player.addPotionEffect(newEffect);
        }

//        player.world.playSound(player, // TODO: Play that sound on client for player
//                player.getPosX(),
//                player.getPosY(),
//                player.getPosZ(),
//                SoundEvents.PARTICLE_SOUL_ESCAPE,
//                SoundCategory.PLAYERS,
//                1F, 1F
//        );
    }

    @Override
    public void onBlur(PlayerEntity player) {} // Do not remove effect on blur

}
