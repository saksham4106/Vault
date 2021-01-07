package iskallia.vault.skill.ability.type;

import com.google.gson.annotations.Expose;
import iskallia.vault.init.ModEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TankAbility extends EffectAbility {

    @Expose
    private int durationTicks;

    public TankAbility(int cost, Effect effect, int level, int durationTicks, Type type, Behavior behavior) {
        super(cost, effect, level, type, behavior);
        this.durationTicks = durationTicks;
    }

    @Override
    public void onTick(PlayerEntity player, boolean active) {

    }

    public int getDurationTicks() {
        return this.durationTicks;
    }

    @Override
    public void onAction(PlayerEntity player, boolean active) {
        EffectInstance activeEffect = player.getActivePotionEffect(this.getEffect());
        EffectInstance newEffect = new EffectInstance(ModEffects.TANK,
                this.getDurationTicks(), this.getAmplifier(), false,
                this.getType().showParticles, this.getType().showIcon);

        if (activeEffect == null) {
            player.addPotionEffect(newEffect);
        }

//        player.world.playSound(player, player.getPosX(), player.getPosY(), player.getPosZ(),
//                ModSounds.GHOST_WALK_SFX, SoundCategory.MASTER, 0.7f, 1f);
//        player.playSound(ModSounds.GHOST_WALK_SFX, SoundCategory.MASTER, 0.7f, 1f);
    }

    @Override
    public void onBlur(PlayerEntity player) {

    }


    @SubscribeEvent
    public static void onDamage(LivingDamageEvent event) {
        LivingEntity entity = event.getEntityLiving();
        EffectInstance tank = entity.getActivePotionEffect(ModEffects.TANK);
        if (tank == null) return;

        float reduction = (float) (tank.getAmplifier() + 1) * 0.025f; //TODO Extract reduction percentage to config

        event.setAmount(event.getAmount() - reduction);

    }

}
