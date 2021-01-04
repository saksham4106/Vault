package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.Vault;
import iskallia.vault.init.ModConfigs;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class VaultGeneralConfig extends Config {

	@Expose private int TICK_COUNTER;
	@Expose private int NO_EXIT_CHANCE;
	@Expose private int OBELISK_DROP_CHANCE;
	@Expose private List<String> ITEM_BLACKLIST;
	@Expose private List<String> BLOCK_BLACKLIST;

	@Override
	public String getName() {
		return "vault_general";
	}

	public int getTickCounter() {
		return this.TICK_COUNTER;
	}

	public int getNoExitChance() {
		return this.NO_EXIT_CHANCE;
	}

	public int getObeliskDropChance() {
		return OBELISK_DROP_CHANCE;
	}

	@Override
	protected void reset() {
		this.TICK_COUNTER = 20 * 60 * 25;
		this.NO_EXIT_CHANCE = 10;

		this.ITEM_BLACKLIST = new ArrayList<>();
		this.ITEM_BLACKLIST.add(Items.ENDER_CHEST.getRegistryName().toString());

		this.BLOCK_BLACKLIST = new ArrayList<>();
		this.BLOCK_BLACKLIST.add(Blocks.ENDER_CHEST.getRegistryName().toString());
		this.OBELISK_DROP_CHANCE = 2;
	}

	@SubscribeEvent
	public static void cancelItemInteraction(PlayerInteractEvent event) {
		if(event.getPlayer().world.getDimensionKey() != Vault.VAULT_KEY)return;

		if(ModConfigs.VAULT_GENERAL.ITEM_BLACKLIST.contains(event.getItemStack().getItem().getRegistryName().toString())) {
			if(event.isCancelable())
				event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void cancelBlockInteraction(PlayerInteractEvent event) {
		if(event.getPlayer().world.getDimensionKey() != Vault.VAULT_KEY)return;
		BlockState state = event.getWorld().getBlockState(event.getPos());

		if(ModConfigs.VAULT_GENERAL.BLOCK_BLACKLIST.contains(state.getBlock().getRegistryName().toString())) {
			if(event.isCancelable())
				event.setCanceled(true);
		}
	}

}
