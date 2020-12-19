package iskallia.vault.block;

import iskallia.vault.block.entity.CryoChamberTileEntity;
import iskallia.vault.init.ModBlocks;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class CryoChamberBlock extends Block {

	public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
	public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

	public CryoChamberBlock() {
		super(Properties.create(Material.IRON, MaterialColor.IRON)
				.hardnessAndResistance(5.0F, 3600000.0F)
				.sound(SoundType.METAL)
				.notSolid());

		this.setDefaultState(this.stateContainer.getBaseState()
				.with(FACING, Direction.NORTH)
				.with(HALF, DoubleBlockHalf.LOWER));
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		if (state.get(HALF) == DoubleBlockHalf.LOWER)
			return true;

		return false;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		if (state.get(HALF) == DoubleBlockHalf.LOWER)
			return ModBlocks.CRYO_CHAMBER_TILE_ENTITY.create();

		return null;
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		BlockPos pos = context.getPos();
		World world = context.getWorld();
		if (pos.getY() < 255 && world.getBlockState(pos.up()).isReplaceable(context)) {
			return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing()).with(HALF, DoubleBlockHalf.LOWER);
		} else {
			return null;
		}
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(HALF);
		builder.add(FACING);
	}

	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
		if (!worldIn.isRemote && player.isCreative()) {
			DoubleBlockHalf half = state.get(HALF);
			if (half == DoubleBlockHalf.UPPER) {
				BlockPos blockpos = pos.down();
				BlockState blockstate = worldIn.getBlockState(blockpos);
				if (blockstate.getBlock() == state.getBlock() && blockstate.get(HALF) == DoubleBlockHalf.LOWER) {
					worldIn.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 35);
					worldIn.playEvent(player, 2001, blockpos, Block.getStateId(blockstate));
				}
			}
		}

		super.onBlockHarvested(worldIn, pos, state, player);
	}

	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		DoubleBlockHalf half = stateIn.get(HALF);
		if (facing.getAxis() == Direction.Axis.Y && half == DoubleBlockHalf.LOWER == (facing == Direction.UP)) {
			return facingState.isIn(this) && facingState.get(HALF) != half ? stateIn.with(FACING, facingState.get(FACING)) : Blocks.AIR.getDefaultState();
		} else {
			return half == DoubleBlockHalf.LOWER && facing == Direction.DOWN && !stateIn.isValidPosition(worldIn, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
		}
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		worldIn.setBlockState(pos.up(), state.with(HALF, DoubleBlockHalf.UPPER), 3);
	}

	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (worldIn.isRemote) return;
		if (!newState.isAir()) return;

		CryoChamberTileEntity machine = getCryoChamberTileEntity(worldIn, pos, state);
		if (machine == null) return;

		if (state.get(HALF) == DoubleBlockHalf.LOWER) {
			dropCryoChamber(worldIn, pos);
		}

		super.onReplaced(state, worldIn, pos, newState, isMoving);
	}

	private void dropCryoChamber(World world, BlockPos pos) {
		ItemEntity entity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ModBlocks.CRYO_CHAMBER));
		world.addEntity(entity);
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {

		return super.onBlockActivated(state, world, pos, player, hand, hit);
	}

	public static BlockPos getCryoChamberPos(BlockState state, BlockPos pos) {
		return state.get(HALF) == DoubleBlockHalf.UPPER
				? pos.down() : pos;
	}

	public static CryoChamberTileEntity getCryoChamberTileEntity(World world, BlockPos pos, BlockState state) {
		BlockPos cryoChamberPos = getCryoChamberPos(state, pos);

		TileEntity tileEntity = world.getTileEntity(cryoChamberPos);

		if ((!(tileEntity instanceof CryoChamberTileEntity)))
			return null;

		return (CryoChamberTileEntity) tileEntity;
	}

}
