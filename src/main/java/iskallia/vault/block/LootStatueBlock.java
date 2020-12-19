package iskallia.vault.block;

import iskallia.vault.block.entity.LootStatueTileEntity;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.util.StatueType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class LootStatueBlock extends Block {

    public static final VoxelShape SHAPE_GIFT_NORMAL = Block.makeCuboidShape(1, 0, 1, 15, 5, 15);
    public static final VoxelShape SHAPE_GIFT_MEGA = Block.makeCuboidShape(1, 0, 1, 15, 13, 15);
    public static final VoxelShape SHAPE_PLAYER_STATUE = Block.makeCuboidShape(1, 0, 1, 15, 5, 15);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public StatueType type;

    public LootStatueBlock(StatueType type) {
        super(Properties.create(Material.ROCK, MaterialColor.STONE)
                .hardnessAndResistance(1.0F, 3600000.0F)
                .notSolid()
                .doesNotBlockMovement());
        this.setDefaultState(this.getStateContainer().getBaseState().with(FACING, Direction.SOUTH));

        this.type = type;

    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity instanceof LootStatueTileEntity) {
            LootStatueTileEntity lootStatue = (LootStatueTileEntity) tileEntity;
            if (stack.hasTag()) {
                CompoundNBT nbt = stack.getTag();
                CompoundNBT blockEntityTag = nbt.getCompound("BlockEntityTag");
                String playerNickname = blockEntityTag.getString("PlayerNickname");
                lootStatue.setInterval(blockEntityTag.getInt("Interval"));
                lootStatue.setLootItem(ItemStack.read(blockEntityTag.getCompound("LootItem")));
                lootStatue.setStatueType(StatueType.values()[blockEntityTag.getInt("StatueType")]);
                lootStatue.setCurrentTick(blockEntityTag.getInt("CurrentTick"));
                lootStatue.setHasCrown(blockEntityTag.getBoolean("HasCrown"));
                lootStatue.getSkin().updateSkin(playerNickname);
            }
        }
        super.onBlockPlacedBy(world, pos, state, placer, stack);
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isRemote) {
            TileEntity tileEntity = world.getTileEntity(pos);
            ItemStack itemStack = new ItemStack(getBlock());

            if (tileEntity instanceof LootStatueTileEntity) {
                LootStatueTileEntity statueTileEntity = (LootStatueTileEntity) tileEntity;

                CompoundNBT statueNBT = statueTileEntity.serializeNBT();
                CompoundNBT stackNBT = new CompoundNBT();
                stackNBT.put("BlockEntityTag", statueNBT);

                itemStack.setTag(stackNBT);
            }

            ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, itemStack);
            itemEntity.setDefaultPickupDelay();
            world.addEntity(itemEntity);
        }

        super.onBlockHarvested(world, pos, state, player);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModBlocks.LOOT_STATUE_TILE_ENTITY.create();
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockPos pos = context.getPos();
        World world = context.getWorld();
        if (pos.getY() < 255 && world.getBlockState(pos.up()).isReplaceable(context)) {
            return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing());
        } else {
            return null;
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        switch (this.getType()) {
            case GIFT_NORMAL:
                return SHAPE_GIFT_NORMAL;
            case GIFT_MEGA:
                return SHAPE_GIFT_MEGA;
            case VAULT_BOSS:
                return SHAPE_PLAYER_STATUE;
        }
        return Block.makeCuboidShape(0, 0, 0, 16, 16, 16);
    }

    public StatueType getType() {
        return type;
    }
}
