package iskallia.vault.container;

import iskallia.vault.init.ModContainers;
import iskallia.vault.util.RenameType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.math.BlockPos;


public class RenamingContainer extends Container {

    private RenameType type;
    private String name;
    private BlockPos pos;

    public RenamingContainer(int windowId, RenameType type, String name, BlockPos pos) {
        super(ModContainers.RENAMING_CONTAINER, windowId);
        this.type = type;
        this.name = name;
        this.pos = pos;
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    public RenameType getRenameType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public BlockPos getPos() {
        return pos;
    }
}
