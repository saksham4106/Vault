package iskallia.vault.skill.ability.type;

import com.google.gson.annotations.Expose;
import iskallia.vault.init.ModSounds;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.SoundCategory;

public class TankAbility extends EffectAbility {

	@Expose private int durationTicks;

	public TankAbility(int cost, Effect effect, int level, int durationTicks, Type type, Behavior behavior) {
		super(cost, effect, level, type, behavior);
		this.durationTicks = durationTicks;
	}

	public int getDurationTicks() {
		return this.durationTicks;
	}

	@Override
	public void onAction(PlayerEntity player, boolean active) {
		EffectInstance activeEffect = player.getActivePotionEffect(this.getEffect());
		EffectInstance newEffect = new EffectInstance(this.getEffect(),
				getDurationTicks(), this.getAmplifier(), false,
				this.getType().showParticles, this.getType().showIcon);

		if(activeEffect == null) {
			player.addPotionEffect(newEffect);
		}

		player.world.playSound(player, player.getPosX(), player.getPosY(), player.getPosZ(),
				ModSounds.GHOST_WALK_SFX, SoundCategory.MASTER, 0.7f, 1f);
		player.playSound(ModSounds.GHOST_WALK_SFX, SoundCategory.MASTER, 0.7f, 1f);
	}

	@Override
	public void onBlur(PlayerEntity player) {

	}

}
