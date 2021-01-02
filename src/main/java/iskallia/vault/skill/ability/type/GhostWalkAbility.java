package iskallia.vault.skill.ability.type;

import com.google.gson.annotations.Expose;
import iskallia.vault.init.ModEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GhostWalkAbility extends EffectAbility {

    @Expose
    private int durationTicks;
    @Expose
    private int speedAmplifier;

    public int getDurationTicks() {
        return durationTicks;
    }

    public int getSpeedAmplifier() {
        return speedAmplifier;
    }

    public GhostWalkAbility(int cost, Effect effect, int level, int speedAmplifier, int durationTicks, Type type, Behavior behavior) {
        super(cost, effect, level, type, behavior);
        this.speedAmplifier = speedAmplifier;
        this.durationTicks = durationTicks;
    }

    @Override
    public void onTick(PlayerEntity player, boolean active) {

    }

    @Override
    public void onAction(PlayerEntity player, boolean active) {
        EffectInstance activeEffect = player.getActivePotionEffect(this.getEffect());
        EffectInstance newEffect = new EffectInstance(this.getEffect(),
                getDurationTicks(), this.getAmplifier(), false,
                this.getType().showParticles, this.getType().showIcon);

        if (activeEffect == null) {
            player.addPotionEffect(newEffect);
        }

    }

    @SubscribeEvent
    public static void onDamage(LivingDamageEvent event) {
        Entity e = event.getSource().getTrueSource();
        if (e instanceof LivingEntity) {
            LivingEntity living = (LivingEntity) e;
            EffectInstance ghostWalk = living.getActivePotionEffect(ModEffects.GHOST_WALK);
            EffectInstance speed = living.getActivePotionEffect(Effects.SPEED);
            if (ghostWalk != null) {
                living.removePotionEffect(ModEffects.GHOST_WALK);
                if (speed != null) {
                    living.removePotionEffect(Effects.SPEED);
                }
            }
        }
    }
}
