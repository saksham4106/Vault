package iskallia.vault.item;

import iskallia.vault.Vault;
import iskallia.vault.container.RenamingContainer;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModItems;
import iskallia.vault.util.RenameType;
import iskallia.vault.util.nbt.NBTSerializer;
import iskallia.vault.vending.Product;
import iskallia.vault.vending.Trade;
import iskallia.vault.vending.TraderCore;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ItemTraderCore extends Item {


    public ItemTraderCore(ItemGroup group, ResourceLocation id) {
        super(new Properties()
                .group(group)
                .maxStackSize(1));

        this.setRegistryName(id);
    }

    public static ItemStack generate(String nickname, int value, boolean megahead) {
        List<Trade> trades = ModConfigs.VENDING_CONFIG.TRADES.stream().filter(trade -> trade.isValid())
                .collect(Collectors.toList());

        Collections.shuffle(trades);

        Optional<Trade> trade = trades.stream().findFirst();
        if (trade.isPresent())
            return getStack(new TraderCore(nickname, trade.get(), value, megahead));

        Vault.LOGGER.error("Attempted to generate a Trader Circuit.. No Trades in config.");
        return ItemStack.EMPTY;
    }

    public static ItemStack getStack(TraderCore core) {
        ItemStack stack = new ItemStack(ModItems.TRADER_CORE, 1);
        CompoundNBT nbt = new CompoundNBT();
        try {
            nbt.put("core", NBTSerializer.serialize(core));
            stack.setTag(nbt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stack;
    }

    public static TraderCore toTraderCore(ItemStack itemStack) {
        CompoundNBT nbt = itemStack.getTag();
        if (nbt == null) return null;
        try {
            return NBTSerializer.deserialize(TraderCore.class, nbt.getCompound("core"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        CompoundNBT nbt = stack.getOrCreateTag();
        if (nbt.contains("core")) {
            TraderCore core = null;
            try {
                core = NBTSerializer.deserialize(TraderCore.class, (CompoundNBT) nbt.get("core"));
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

            Trade trade = core.getTrade();
            if (!trade.isValid()) return;

            Product buy = trade.getBuy();
            Product extra = trade.getExtra();
            Product sell = trade.getSell();
            tooltip.add(new StringTextComponent(""));
            tooltip.add(new StringTextComponent("Trader: "));
            StringTextComponent traderName = new StringTextComponent(" " + core.getName());
            traderName.setStyle(Style.EMPTY.setColor(Color.fromInt(0x00_FFAA00)));
            tooltip.add(traderName);
            tooltip.add(new StringTextComponent(""));
            tooltip.add(new StringTextComponent("Trades: "));
            if (buy != null && buy.isValid()) {
                StringTextComponent comp = new StringTextComponent(" - Buy: ");
                TranslationTextComponent name = new TranslationTextComponent(buy.getItem().getTranslationKey());
                name.setStyle(Style.EMPTY.setColor(Color.fromInt(0x00_FFAA00)));
                comp.append(name).append(new StringTextComponent(" x" + buy.getAmount()));
                tooltip.add(comp);
            }
            if (extra != null && extra.isValid()) {
                StringTextComponent comp = new StringTextComponent(" - Extra: ");
                TranslationTextComponent name = new TranslationTextComponent(extra.getItem().getTranslationKey());
                name.setStyle(Style.EMPTY.setColor(Color.fromInt(0x00_FFAA00)));
                comp.append(name).append(new StringTextComponent(" x" + extra.getAmount()));
                tooltip.add(comp);
            }
            if (sell != null && sell.isValid()) {
                StringTextComponent comp = new StringTextComponent(" - Sell: ");
                TranslationTextComponent name = new TranslationTextComponent(sell.getItem().getTranslationKey());
                name.setStyle(Style.EMPTY.setColor(Color.fromInt(0x00_FFAA00)));
                comp.append(name).append(new StringTextComponent(" x" + sell.getAmount()));
                tooltip.add(comp);
            }

            tooltip.add(new StringTextComponent(""));
            if (trade.getTradesLeft() == 0) {
                StringTextComponent comp = new StringTextComponent("[0] Sold out, sorry!");
                comp.setStyle(Style.EMPTY.setColor(Color.fromInt(0x00_FF0000)));
                tooltip.add(comp);
            } else if (trade.getTradesLeft() == -1) {
                StringTextComponent comp = new StringTextComponent("[\u221e] Has unlimited trades.");
                comp.setStyle(Style.EMPTY.setColor(Color.fromInt(0x00_00AAFF)));
                tooltip.add(comp);
            } else {
                StringTextComponent comp = new StringTextComponent("[" + trade.getTradesLeft() + "] Has a limited stock.");
                comp.setStyle(Style.EMPTY.setColor(Color.fromInt(0x00_FFAA00)));
                tooltip.add(comp);
            }
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        StringTextComponent text = new StringTextComponent("Trader Circuit");
        text.setStyle(Style.EMPTY.setColor(Color.fromInt(0x00_ad96c5)));
        return text;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity player, Hand handIn) {
        if (worldIn.isRemote) return super.onItemRightClick(worldIn, player, handIn);

        ItemStack stack = player.getHeldItemMainhand();
        String name = ItemTraderCore.getTraderName(stack);
        NetworkHooks.openGui(
                (ServerPlayerEntity) player,
                new INamedContainerProvider() {
                    @Override
                    public ITextComponent getDisplayName() {
                        return new StringTextComponent("Player Statue");
                    }

                    @Nullable
                    @Override
                    public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                        return new RenamingContainer(windowId, RenameType.TRADER_CORE, ItemTraderCore.getTraderName(stack), null);
                    }
                },
                (buffer) -> {
                    buffer.writeInt(RenameType.TRADER_CORE.ordinal());
                    buffer.writeString(name);
                }
        );
        return super.onItemRightClick(worldIn, player, handIn);
    }

    public static String getTraderName(ItemStack stack) {
        CompoundNBT nbt = stack.getOrCreateTag();
        TraderCore core = null;
        try {
            core = NBTSerializer.deserialize(TraderCore.class, (CompoundNBT) nbt.get("core"));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return core.getName();
    }

    public static void updateTraderName(ItemStack stack, String newName) {
        CompoundNBT nbt = stack.getOrCreateTag();
        TraderCore core = null;
        try {
            core = NBTSerializer.deserialize(TraderCore.class, (CompoundNBT) nbt.get("core"));
            core.setName(newName);
            CompoundNBT coreNBT = new CompoundNBT();
            nbt.put("core", NBTSerializer.serialize(core));
            stack.setTag(coreNBT);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
