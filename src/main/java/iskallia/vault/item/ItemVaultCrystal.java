package iskallia.vault.item;

import iskallia.vault.block.VaultPortalSize;
import iskallia.vault.init.ModItems;
import iskallia.vault.init.ModSounds;
import iskallia.vault.util.VaultRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import java.util.Optional;

public class ItemVaultCrystal extends Item {

    private VaultRarity vaultRarity;

    public ItemVaultCrystal(ItemGroup group, ResourceLocation id, VaultRarity vaultRarity) {
        super(new Properties()
                .group(group)
                .maxStackSize(1));

        this.setRegistryName(id);
        this.vaultRarity = vaultRarity;
    }

    public static ItemStack getRandomCrystal() {
        VaultRarity vaultRarity = VaultRarity.getWeightedRandom();
        switch (vaultRarity) {
            case NORMAL:
                return new ItemStack(ModItems.VAULT_CRYSTAL_NORMAL);
            case RARE:
                return new ItemStack(ModItems.VAULT_CRYSTAL_RARE);
            case EPIC:
                return new ItemStack(ModItems.VAULT_CRYSTAL_EPIC);
            case OMEGA:
                return new ItemStack(ModItems.VAULT_CRYSTAL_OMEGA);
        }
        return new ItemStack(ModItems.VAULT_CRYSTAL_NORMAL);
    }

    public static ItemStack getCrystalWithBoss(String playerBossName) {
        ItemStack stack = ItemVaultCrystal.getRandomCrystal();
        stack.getOrCreateTag().putString("playerBossName", playerBossName);
        return stack;
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        if (context.getWorld().isRemote) return super.onItemUse(context);

        ItemStack stack = context.getPlayer().getHeldItemMainhand();
        Item item = stack.getItem();

        if (item instanceof ItemVaultCrystal) {
            ItemVaultCrystal crystal = (ItemVaultCrystal) item;

            String playerBossName = "";
            CompoundNBT tag = stack.getOrCreateTag();
            if (tag.keySet().contains("playerBossName")) {
                playerBossName = tag.getString("playerBossName");
            }

            BlockPos pos = context.getPos();
            if (tryCreatePortal(crystal, context.getWorld(), pos, context.getFace(), playerBossName)) {
                context.getWorld().playSound(null,
                        pos.getX(),
                        pos.getY(),
                        pos.getZ(),
                        ModSounds.VAULT_PORTAL_OPEN,
                        SoundCategory.BLOCKS,
                        1f, 1f
                );
                context.getItem().shrink(1);
                return ActionResultType.SUCCESS;
            }

        }
        return super.onItemUse(context);
    }

    private boolean tryCreatePortal(ItemVaultCrystal crystal, World world, BlockPos pos, Direction facing, String playerBossName) {
        Optional<VaultPortalSize> optional = VaultPortalSize.getPortalSize(world, pos.offset(facing), Direction.Axis.X);
        if (optional.isPresent()) {
            optional.get().placePortalBlocks(crystal, playerBossName);
            return true;
        }
        return false;
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        if (stack.getItem() instanceof ItemVaultCrystal) {
            ItemVaultCrystal item = (ItemVaultCrystal) stack.getItem();

            CompoundNBT tag = stack.getOrCreateTag();
            if (tag.keySet().contains("playerBossName")) {
                return new StringTextComponent(item.getRarity().color + "Vault Crystal (" + tag.getString("playerBossName") + ")");
            }

            switch (item.getRarity()) {
                case NORMAL:
                    return new StringTextComponent(item.getRarity().color + "Vault Crystal (normal)");
                case RARE:
                    return new StringTextComponent(item.getRarity().color + "Vault Crystal (rare)");
                case EPIC:
                    return new StringTextComponent(item.getRarity().color + "Vault Crystal (epic)");
                case OMEGA:
                    return new StringTextComponent(item.getRarity().color + "Vault Crystal (omega)");
            }
        }

        return super.getDisplayName(stack);
    }

    public VaultRarity getRarity() {
        return vaultRarity;
    }


}
