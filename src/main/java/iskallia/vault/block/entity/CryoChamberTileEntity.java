package iskallia.vault.block.entity;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import iskallia.vault.Vault;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.util.SkinProfile;
import iskallia.vault.util.WeightedList;
import iskallia.vault.vending.Product;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class CryoChamberTileEntity extends TileEntity implements ITickableTileEntity {

	public static final Supplier<Generator> GENERATOR = Behaviour.register(Vault.id("generator"), Generator::new);
	public static final Supplier<Miner> MINER = Behaviour.register(Vault.id("miner"), Miner::new);
	public static final Supplier<Looter> LOOTER = Behaviour.register(Vault.id("looter"), Looter::new);

	private Energy energyStorage = createEnergy();
	private LazyOptional<IEnergyStorage> energy = LazyOptional.of(() -> energyStorage);
	private List<Behaviour> behaviours = new ArrayList<>();
	protected SkinProfile skin;

	public CryoChamberTileEntity() {
		super(ModBlocks.CRYO_CHAMBER_TILE_ENTITY);
		this.skin = new SkinProfile();
	}

	public SkinProfile getSkin() {
		return skin;
	}

	private Energy createEnergy() {
		return new Energy(0, 0) {
			@Override
			protected void onEnergyChanged() {
				CryoChamberTileEntity.this.markDirty();
			}
		};
	}

	public void sendUpdates() {
		this.world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), 0b11);
		this.world.notifyNeighborsOfStateChange(pos, this.getBlockState().getBlock());
		markDirty();
	}

	@Override
	public void tick() {
		this.behaviours.forEach(behaviour -> behaviour.tick(this.getWorld(), this.getPos(), this));
	}

	@Override
	public CompoundNBT write(CompoundNBT nbt) {
		ListNBT behavioursList = new ListNBT();

		this.behaviours.forEach(behaviour -> {
			CompoundNBT tag = behaviour.serializeNBT();
			tag.putString("RegistryId", Behaviour.REGISTRY.inverse().get(behaviour).toString());
			behavioursList.add(tag);
		});

		nbt.put("Behaviours", behavioursList);
		return super.write(nbt);
	}

	@Override
	public void read(BlockState state, CompoundNBT nbt) {
		this.behaviours.clear();
		ListNBT behavioursList = nbt.getList("Behaviours", Constants.NBT.TAG_COMPOUND);

		for(int i = 0; i < behavioursList.size(); i++) {
			CompoundNBT tag = behavioursList.getCompound(i);
			Supplier<? extends Behaviour> supplier = Behaviour.REGISTRY.get(new ResourceLocation(tag.getString("RegistryId")));

			if(supplier != null) {
				Behaviour behaviour = supplier.get();
				behaviour.deserializeNBT(tag);
				this.behaviours.add(behaviour);
			}
		}

		super.read(state, nbt);
	}


	@Override
	public CompoundNBT getUpdateTag() {
		CompoundNBT nbt = super.getUpdateTag();
		return nbt;
	}

	@Override
	public void handleUpdateTag(BlockState state, CompoundNBT tag) {
		read(state, tag);
	}

	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(pos, 1, getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		CompoundNBT nbt = pkt.getNbtCompound();
		handleUpdateTag(getBlockState(), nbt);
	}

	public static class Energy extends EnergyStorage {
		public Energy(int capacity, int maxTransfer) {
			super(capacity, maxTransfer);
		}

		protected void onEnergyChanged() {

		}

		public void setEnergy(int energy) {
			this.energy = energy;
			this.onEnergyChanged();
		}

		public void addEnergy(int energy) {
			this.energy += energy;

			if(this.energy > getMaxEnergyStored()) {
				this.energy = getEnergyStored();
			}

			this.onEnergyChanged();
		}

		public void consumeEnergy(int energy) {
			this.energy -= energy;
			if (this.energy < 0) {
				this.energy = 0;
			}

			this.onEnergyChanged();
		}
	}

	public static abstract class Behaviour implements INBTSerializable<CompoundNBT> {
		public static final BiMap<ResourceLocation, Supplier<? extends Behaviour>> REGISTRY = HashBiMap.create();

		public abstract void tick(World world, BlockPos pos, CryoChamberTileEntity te);

		public static <T extends Behaviour> Supplier<T> register(ResourceLocation id, Supplier<T> behaviour) {
			REGISTRY.put(id, behaviour);
			return behaviour;
		}
	}

	public static class Generator extends Behaviour {
		@Override
		public void tick(World world, BlockPos pos, CryoChamberTileEntity te) {

		}

		@Override
		public CompoundNBT serializeNBT() {
			CompoundNBT nbt = new CompoundNBT();
			return nbt;
		}

		@Override
		public void deserializeNBT(CompoundNBT nbt) {

		}
	}

	public static class Miner extends Poop {
		public Miner() {
			super(ModConfigs.CRYO_CHAMBER.MINER_DROPS, ModConfigs.CRYO_CHAMBER.MINER_TICKS_DELAY);
		}
	}

	public static class Looter extends Poop {
		public Looter() {
			super(ModConfigs.CRYO_CHAMBER.LOOTER_DROPS, ModConfigs.CRYO_CHAMBER.LOOTER_TICKS_DELAY);
		}
	}

	public static class Poop extends Behaviour {
		private Product product;
		private int delay;

		public Poop(WeightedList<Product> pool, int delay) {
			this.product = pool.getRandom(new Random());
			this.delay = delay;
		}

		@Override
		public void tick(World world, BlockPos pos, CryoChamberTileEntity te) {
			if(world.getGameTime() % this.delay == 0) {
				this.poop(te, this.product.toStack(), false);
			}
		}

		public ItemStack poop(CryoChamberTileEntity te, ItemStack stack, boolean simulate) {
			TileEntity tileEntity = te.getWorld().getTileEntity(te.getPos().down());
			if(tileEntity == null)return stack;

			LazyOptional<IItemHandler> handler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.UP);

			if(handler.isPresent()) {
				IItemHandler targetHandler = handler.orElse(null);
				return ItemHandlerHelper.insertItemStacked(targetHandler, stack, simulate);
			}

			return stack;
		}


		@Override
		public CompoundNBT serializeNBT() {
			CompoundNBT nbt = new CompoundNBT();

			CompoundNBT productNBT = new CompoundNBT();
			productNBT.putString("Id", this.product.getId());
			productNBT.putInt("Amount", this.product.getAmount());
			productNBT.putString("Nbt", this.product.getNBT().toString());
			nbt.put("Product", productNBT);

			return nbt;
		}

		@Override
		public void deserializeNBT(CompoundNBT nbt) {
			CompoundNBT productNBT = nbt.getCompound("Product");
			String id = productNBT.getString("Id");
			int amount = productNBT.getInt("Amount");
			String itemNbt = productNBT.getString("Nbt");

			try {
				this.product = new Product(
						Registry.ITEM.getOptional(new ResourceLocation(id)).orElse(Items.AIR),
						amount, JsonToNBT.getTagFromJson(itemNbt)
				);
			} catch(CommandSyntaxException e) {
				e.printStackTrace();
			}
		}
	}

}
