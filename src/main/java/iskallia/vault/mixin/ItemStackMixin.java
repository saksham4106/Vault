package iskallia.vault.mixin;

import iskallia.vault.skill.talent.TalentNode;
import iskallia.vault.skill.talent.TalentTree;
import iskallia.vault.skill.talent.type.UnbreakableTalent;
import iskallia.vault.world.data.PlayerTalentsData;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.UnbreakingEnchantment;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;
import java.util.Random;

@Mixin(value = ItemStack.class, priority = 1001)
public abstract class ItemStackMixin {

	@Shadow public abstract boolean isDamageable();
	@Shadow public abstract int getDamage();
	@Shadow public abstract void setDamage(int damage);
	@Shadow public abstract int getMaxDamage();

	/**
	 * @author Vault (Iskallia)
	 */
	@Overwrite
	public boolean attemptDamageItem(int amount, Random rand, @Nullable ServerPlayerEntity damager) {
		if(!this.isDamageable()) return false;

		if(amount > 0) {
			int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, (ItemStack)(Object)this);

			if(damager != null) {
				TalentTree abilities = PlayerTalentsData.get(damager.getServerWorld()).getTalents(damager);

				for(TalentNode<?> node: abilities.getNodes()) {
					if(!(node.getTalent() instanceof UnbreakableTalent))continue;
					UnbreakableTalent talent = (UnbreakableTalent)node.getTalent();
					i += talent.getExtraUnbreaking();
				}
			}

			int j = 0;

			for(int k = 0; i > 0 && k < amount; ++k) {
				if(UnbreakingEnchantment.negateDamage((ItemStack)(Object)this, i, rand)) {
					++j;
				}
			}

			amount -= j;

			if(amount <= 0) {
				return false;
			}
		}

		if(damager != null && amount != 0) {
			CriteriaTriggers.ITEM_DURABILITY_CHANGED.trigger(damager, (ItemStack)(Object)this, this.getDamage() + amount);
		}

		int l = this.getDamage() + amount;
		this.setDamage(l);

		return l >= this.getMaxDamage();
	}

}
