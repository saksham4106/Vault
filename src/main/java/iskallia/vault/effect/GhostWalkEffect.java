package iskallia.vault.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class GhostWalkEffect extends Effect {

    public GhostWalkEffect(EffectType typeIn, int liquidColorIn) {
        super(typeIn, liquidColorIn);

        this.addAttributesModifier(Attributes.MOVEMENT_SPEED, "91AEAA56-376B-4498-935B-2F7F68070635", (double) 0.2F, AttributeModifier.Operation.MULTIPLY_TOTAL);

    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;
    }

    @Override
    public void performEffect(LivingEntity living, int amplifier) {
        living.setInvulnerable(true);
    }


}
