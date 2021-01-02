package iskallia.vault.effect;

import iskallia.vault.skill.ability.AbilityNode;
import iskallia.vault.skill.ability.AbilityTree;
import iskallia.vault.skill.ability.type.GhostWalkAbility;
import iskallia.vault.skill.ability.type.PlayerAbility;
import iskallia.vault.world.data.PlayerAbilitiesData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Effects;
import net.minecraft.world.server.ServerWorld;

public class GhostWalkEffect extends Effect {

    public GhostWalkEffect(EffectType typeIn, int liquidColorIn) {
        super(typeIn, liquidColorIn);

    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;
    }

    @Override
    public void performEffect(LivingEntity living, int amplifier) {
        if (living.world.isRemote) return;
        PlayerAbilitiesData abilities = PlayerAbilitiesData.get((ServerWorld) living.world);
        if (living instanceof PlayerEntity) {

            AbilityTree tree = abilities.getAbilities((PlayerEntity) living);
            AbilityNode<?> focusedAbility = tree.getFocusedAbility();

            if (focusedAbility == null) return;

            PlayerAbility ability = focusedAbility.getAbility();
            if (ability instanceof GhostWalkAbility) {
                GhostWalkAbility ghostWalk = (GhostWalkAbility) ability;

                EffectInstance speedEffect = living.getActivePotionEffect(Effects.SPEED);
                EffectInstance newSpeed = new EffectInstance(Effects.SPEED,
                        ghostWalk.getDurationTicks(), ghostWalk.getSpeedAmplifier(), false,
                        ghostWalk.getType().showParticles, ghostWalk.getType().showIcon);

                if (speedEffect == null) {
                    living.addPotionEffect(newSpeed);
                }
            }
        }
    }

    @Override
    public void applyAttributesModifiersToEntity(LivingEntity entityLivingBaseIn, AttributeModifierManager attributeMapIn, int amplifier) {
        entityLivingBaseIn.setInvulnerable(true);
        super.applyAttributesModifiersToEntity(entityLivingBaseIn, attributeMapIn, amplifier);
    }

    @Override
    public void removeAttributesModifiersFromEntity(LivingEntity entityLivingBaseIn, AttributeModifierManager attributeMapIn, int amplifier) {
        entityLivingBaseIn.setInvulnerable(false);
        super.removeAttributesModifiersFromEntity(entityLivingBaseIn, attributeMapIn, amplifier);
    }

}
