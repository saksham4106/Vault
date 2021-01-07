package iskallia.vault.block.entity;

import iskallia.vault.init.ModBlocks;
import iskallia.vault.item.ItemTraderCore;
import iskallia.vault.util.SkinProfile;
import iskallia.vault.util.nbt.NBTSerializer;
import iskallia.vault.vending.TraderCore;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AdvancedVendingTileEntity extends TileEntity {

    protected SkinProfile skin;
    private List<TraderCore> cores = new ArrayList<>();

    public AdvancedVendingTileEntity() {
        super(ModBlocks.ADVANCED_VENDING_MACHINE_TILE_ENTITY);
        skin = new SkinProfile();
    }

    public SkinProfile getSkin() {
        return skin;
    }

    public List<TraderCore> getCores() {
        return cores;
    }

    public TraderCore getLastCore() {
        if (cores == null || cores.size() == 0) return null;
        return cores.get(cores.size() - 1);
    }

    public TraderCore getRenderCore() {
        if (cores == null || cores.size() == 0) return null;
        TraderCore renderCore = null;
        for (TraderCore core : cores) {
            if (renderCore == null || renderCore.getValue() < core.getValue())
                renderCore = core;
        }
        return renderCore;
    }

    public void sendUpdates() {
        this.world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), 0b11);
        this.world.notifyNeighborsOfStateChange(pos, this.getBlockState().getBlock());
        markDirty();
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        ListNBT list = new ListNBT();
        for (TraderCore core : cores) {
            try {
                list.add(NBTSerializer.serialize(core));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        compound.put("coresList", list);
        return super.write(compound);
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        ListNBT list = nbt.getList("coresList", Constants.NBT.TAG_COMPOUND);
        this.cores = new LinkedList<>();
        for (INBT tag : list) {
            TraderCore core = null;
            try {
                core = NBTSerializer.deserialize(TraderCore.class, (CompoundNBT) tag);
            } catch (Exception e) {
                e.printStackTrace();
            }
            cores.add(core);
        }
        updateSkin();
        super.read(state, nbt);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT nbt = super.getUpdateTag();

        ListNBT list = new ListNBT();
        for (TraderCore core : cores) {
            try {
                list.add(NBTSerializer.serialize(core));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        nbt.put("coresList", list);

        return nbt;
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        read(state, tag);
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, 1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        CompoundNBT nbt = pkt.getNbtCompound();
        handleUpdateTag(getBlockState(), nbt);
    }

    public void addCore(TraderCore core) {
        this.cores.add(core);
        updateSkin();
        sendUpdates();
    }

    public void updateSkin() {
        TraderCore lastCore = getLastCore();
        if (lastCore == null) return;
        skin.updateSkin(lastCore.getName());
    }
    public void updateSkin(String name) {
        skin.updateSkin(name);
    }

    public void printCores() {
        for (TraderCore core : cores) {
            System.out.println("------ " + core.getName() + "'s trades ------");
            if (core.getTrade().getBuy() != null) {
                System.out.println("Buy: " + core.getTrade().getBuy().toString());
                ItemStack buy = core.getTrade().getBuy().toStack();
                ItemEntity buyItem = new ItemEntity(this.world, this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), buy);
                this.world.addEntity(buyItem);
            }
            if (core.getTrade().getExtra() != null) {
                System.out.println("Extra: " + core.getTrade().getExtra().toString());
                ItemStack extra = core.getTrade().getExtra().toStack();
                ItemEntity extraItem = new ItemEntity(this.world, this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), extra);
                this.world.addEntity(extraItem);
            }
            if (core.getTrade().getSell() != null) {
                System.out.println("Sell: " + core.getTrade().getSell().toString());
                ItemStack sell = core.getTrade().getSell().toStack();
                ItemEntity sellItem = new ItemEntity(this.world, this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), sell);
                this.world.addEntity(sellItem);
            }
        }
    }

    public void ejectCores() {
        for (TraderCore core : cores) {
            ItemEntity entity = new ItemEntity(this.world, this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), ItemTraderCore.getStack(core));
            this.world.addEntity(entity);
        }
    }

    public void retrieveLastCore(PlayerEntity player) {
        TraderCore lastCore = this.getLastCore();
        if (lastCore == null) return;
        ItemStack stack = ItemTraderCore.getStack(lastCore);
        if (!player.addItemStackToInventory(ItemTraderCore.getStack(lastCore))) {
            ItemEntity entity = new ItemEntity(this.world, this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), stack);
            this.world.addEntity(entity);
        }
        cores.remove(lastCore);
    }
}
