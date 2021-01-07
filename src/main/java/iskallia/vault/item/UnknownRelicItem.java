package iskallia.vault.item;

import iskallia.vault.init.ModConfigs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class UnknownRelicItem extends BasicItem {

	public UnknownRelicItem(ResourceLocation id, Properties properties) {
		super(id, properties);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
		if (!world.isRemote) {
			ItemStack heldStack = player.getHeldItem(hand);
			RelicPartItem randomPart = ModConfigs.VAULT_RELICS.getRandomPart();
			ItemStack stackToDrop = new ItemStack(randomPart);
			ItemRelicBoosterPack.successEffects(world, player.getPositionVec());

			player.dropItem(stackToDrop, false, false);
			heldStack.shrink(1);
		}

		return super.onItemRightClick(world, player, hand);
	}

}
