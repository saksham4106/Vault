package iskallia.vault.config;

import iskallia.vault.util.WeightedList;
import iskallia.vault.vending.Product;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;

public class CryoChamberConfig extends Config {


	public int GENERATOR_FE_PER_TICK;
	public WeightedList<Product> MINER_DROPS;
	public int MINER_TICKS_DELAY;
	public WeightedList<Product> LOOTER_DROPS;
	public int LOOTER_TICKS_DELAY;

	@Override
	public String getName() {
		return "cryo_chamber";
	}

	@Override
	protected void reset() {
		this.GENERATOR_FE_PER_TICK = 100;

		this.MINER_DROPS = new WeightedList<Product>()
				.add(new Product(Items.IRON_ORE, 2, new CompoundNBT()), 1)
				.add(new Product(Items.GOLD_ORE, 2, new CompoundNBT()), 1)
				.add(new Product(Items.DIAMOND_ORE, 1, new CompoundNBT()), 1).strip();

		this.MINER_TICKS_DELAY = 100;

		this.LOOTER_DROPS = new WeightedList<Product>()
				.add(new Product(Items.EMERALD, 1, new CompoundNBT()), 1)
				.add(new Product(Items.PAPER, 10, new CompoundNBT()), 1)
				.add(new Product(Items.WHITE_WOOL, 3, new CompoundNBT()), 1).strip();

		this.LOOTER_TICKS_DELAY = 100;
	}

}
