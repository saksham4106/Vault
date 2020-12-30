package iskallia.vault.block.entity;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import iskallia.vault.Vault;
import iskallia.vault.block.CryoChamberBlock;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.util.MathUtilities;
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
import net.minecraft.state.properties.DoubleBlockHalf;
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
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class CryoChamberTileEntity extends TileEntity implements ITickableTileEntity {

    public static final Supplier<Generator> GENERATOR = Behaviour.register(Vault.id("generator"), Generator::new);
    public static final Supplier<Miner> MINER = Behaviour.register(Vault.id("miner"), Miner::new);
    public static final Supplier<Looter> LOOTER = Behaviour.register(Vault.id("looter"), Looter::new);

    private List<Behaviour> behaviours = new ArrayList<>();
    protected SkinProfile skin;

    public CryoChamberTileEntity() {
        super(ModBlocks.CRYO_CHAMBER_TILE_ENTITY);
        this.skin = new SkinProfile();

    }

    public SkinProfile getSkin() {
        return skin;
    }


    public void sendUpdates() {
        BlockState newState = this.getBlockState();
        if (!this.behaviours.isEmpty()) {
            if (this.behaviours.get(0) instanceof Generator) {
                newState = this.getBlockState().with(CryoChamberBlock.CHAMBER_STATE, CryoChamberBlock.ChamberState.GENERATOR);
            }
        }
        this.world.notifyBlockUpdate(pos, getBlockState(), newState, 3);
        this.world.notifyNeighborsOfStateChange(pos, this.getBlockState().getBlock());
        markDirty();
    }

    public List<Behaviour> getBehaviours() {
        return behaviours;
    }

    public void addBehaviour(Behaviour behaviour) {
        behaviours.add(behaviour);
        System.out.println(behaviours.get(0).toString());
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
            //tag.putString("RegistryId", Behaviour.REGISTRY.inverse().get(behaviour).toString()); // this no work
            tag.putString("RegistryId", behaviour.resourceLocation.toString());
            behavioursList.add(tag);
        });

        nbt.put("Behaviours", behavioursList);
        return super.write(nbt);
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {

        this.behaviours.clear();
        ListNBT behavioursList = nbt.getList("Behaviours", Constants.NBT.TAG_COMPOUND);

        for (int i = 0; i < behavioursList.size(); i++) {
            CompoundNBT tag = behavioursList.getCompound(i);
            Supplier<? extends Behaviour> supplier = Behaviour.REGISTRY.get(new ResourceLocation(tag.getString("RegistryId")));

            if (supplier != null) {
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

    public static class Energy extends EnergyStorage implements INBTSerializable<CompoundNBT> {
        public Energy(int capacity, int maxTransfer) {
            super(capacity, maxTransfer);
        }

        public int getTransferSpeed() {
            return this.maxExtract;
        }

        public void setTransferSpeed(int transferSpeed) {
            this.maxExtract = transferSpeed;
            this.maxReceive = transferSpeed;
        }

        public void setEnergy(int energy) {
            this.energy = energy;
            this.onEnergyChanged();
        }

        protected void onEnergyChanged() {
        }

        public void addEnergy(int energy) {
            this.energy += energy;

            if (this.energy > getMaxEnergyStored()) {
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

        @Override
        public CompoundNBT serializeNBT() {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putInt("energy", this.energy);
            nbt.putInt("transferSpeed", getTransferSpeed());
            return nbt;
        }

        @Override
        public void deserializeNBT(CompoundNBT nbt) {
            setEnergy(nbt.getInt("energy"));
            setTransferSpeed(nbt.getInt("transferSpeed"));
        }
    }

    public static abstract class Behaviour implements INBTSerializable<CompoundNBT> {
        public static final BiMap<ResourceLocation, Supplier<? extends Behaviour>> REGISTRY = HashBiMap.create();

        public ResourceLocation resourceLocation;

        public Behaviour(ResourceLocation resourceLocation) {
            this.resourceLocation = resourceLocation;
        }

        public abstract void tick(World world, BlockPos pos, CryoChamberTileEntity te);

        public static <T extends Behaviour> Supplier<T> register(ResourceLocation id, Supplier<T> behaviour) {
            REGISTRY.put(id, behaviour);
            return behaviour;
        }
    }

    public static class Generator extends Behaviour {


        private Energy energyStorage = createEnergyStorage();
        private LazyOptional<IEnergyStorage> energy = LazyOptional.of(() -> energyStorage);

        public Generator() {
            super(Vault.id("generator"));
        }

        private Energy createEnergyStorage() {
            int transferSpeed = MathUtilities.getRandomInt(ModConfigs.CRYO_CHAMBER.GENERATOR_FE_PER_TICK_MIN, ModConfigs.CRYO_CHAMBER.GENERATOR_FE_PER_TICK_MAX);
            return new Energy(ModConfigs.CRYO_CHAMBER.GENERATOR_FE_CAPACITY, transferSpeed);
        }

        @Override
        public void tick(World world, BlockPos pos, CryoChamberTileEntity te) {
            if (world.isRemote) return;
            energyStorage.addEnergy(energyStorage.getTransferSpeed());
            for (DoubleBlockHalf half : DoubleBlockHalf.values()) {
                if (half == DoubleBlockHalf.LOWER) pos = pos.offset(Direction.UP);
                sendOutPower(world, pos);
            }
            te.markDirty();
        }

        private void sendOutPower(World world, BlockPos pos) {
            AtomicInteger capacity = new AtomicInteger(energyStorage.getEnergyStored());
            if (capacity.get() > 0) {
                for (Direction direction : Direction.values()) {
                    TileEntity te = world.getTileEntity(pos.offset(direction));
                    if (te != null) {
                        if (te instanceof CryoChamberTileEntity) continue; // skip self
                        boolean doContinue = te.getCapability(CapabilityEnergy.ENERGY, direction).map(handler -> {
                                    if (handler.canReceive()) {
                                        int received = handler.receiveEnergy(Math.min(capacity.get(), energyStorage.getTransferSpeed()), false);
                                        capacity.addAndGet(-received);
                                        energyStorage.consumeEnergy(received);
                                        return capacity.get() > 0;
                                    } else {
                                        return true;
                                    }
                                }
                        ).orElse(true);
                        if (!doContinue) {
                            return;
                        }
                    }
                }
            }
        }

        @Override
        public CompoundNBT serializeNBT() {
            CompoundNBT nbt = new CompoundNBT();
            nbt.put("Generator", energyStorage.serializeNBT());
            return nbt;
        }

        @Override
        public void deserializeNBT(CompoundNBT nbt) {
            energyStorage.deserializeNBT(nbt.getCompound("Generator"));
        }

        @Override
        public String toString() {
            return "Generator{" +
                    "energy=" + energyStorage.getEnergyStored() +
                    ", speed=" + energyStorage.getTransferSpeed() +
                    '}';
        }
    }

    public static class Miner extends Poop {
        public Miner() {
            super(ModConfigs.CRYO_CHAMBER.MINER_DROPS, ModConfigs.CRYO_CHAMBER.MINER_TICKS_DELAY, Vault.id("miner"));
        }
    }

    public static class Looter extends Poop {
        public Looter() {
            super(ModConfigs.CRYO_CHAMBER.LOOTER_DROPS, ModConfigs.CRYO_CHAMBER.LOOTER_TICKS_DELAY, Vault.id("looter"));
        }
    }

    public static class Poop extends Behaviour {
        private Product product;
        private int delay;

        public Poop(WeightedList<Product> pool, int delay, ResourceLocation resourceLocation) {
            super(resourceLocation);
            this.product = pool.getRandom(new Random());
            this.delay = delay;
        }

        @Override
        public void tick(World world, BlockPos pos, CryoChamberTileEntity te) {
            if (world.getGameTime() % this.delay == 0) {
                this.poop(te, this.product.toStack(), false);
            }
        }

        public ItemStack poop(CryoChamberTileEntity te, ItemStack stack, boolean simulate) {
            TileEntity tileEntity = te.getWorld().getTileEntity(te.getPos().down());
            if (tileEntity == null) return stack;

            LazyOptional<IItemHandler> handler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.UP);

            if (handler.isPresent()) {
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
            } catch (CommandSyntaxException e) {
                e.printStackTrace();
            }
        }
    }

}
