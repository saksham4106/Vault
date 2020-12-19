package iskallia.vault.block.entity;

import iskallia.vault.init.ModBlocks;
import iskallia.vault.util.SkinProfile;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;

public class CryoChamberTileEntity extends TileEntity {

    protected SkinProfile skin;

    public CryoChamberTileEntity() {
        super(ModBlocks.CRYO_CHAMBER_TILE_ENTITY);
        skin = new SkinProfile();
    }

    public SkinProfile getSkin() {
        return skin;
    }

    public void sendUpdates() {
        this.world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), 0b11);
        this.world.notifyNeighborsOfStateChange(pos, this.getBlockState().getBlock());
        markDirty();
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {

        return super.write(compound);
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {

        updateSkin();
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

    public void updateSkin() {
//        TraderCore lastCore = getLastCore();
//        if (lastCore == null) return;
//        skin.updateSkin(lastCore.getName());
    }
}
