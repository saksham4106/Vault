package iskallia.vault.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.ResourceLocation;

public class TankEffect extends Effect {

	public AttributeModifier[] attributeModifiers;

	public TankEffect(EffectType typeIn, int liquidColorIn, ResourceLocation id) {
		super(typeIn, liquidColorIn);
		this.attributeModifiers = new AttributeModifier[6];
		setRegistryName(id);
		//TODO
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;
	}

	@Override
	public void applyAttributesModifiersToEntity(LivingEntity entityLivingBaseIn, AttributeModifierManager attributeMapIn, int amplifier) {
		//TODO
	}

	@Override
	public void removeAttributesModifiersFromEntity(LivingEntity entityLivingBaseIn, AttributeModifierManager attributeMapIn, int amplifier) {
		//TODO
	}

}
