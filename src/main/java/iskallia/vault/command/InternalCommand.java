package iskallia.vault.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import iskallia.vault.config.StreamerMultipliersConfig;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModSounds;
import iskallia.vault.item.ItemGiftBomb;
import iskallia.vault.item.ItemTraderCore;
import iskallia.vault.util.EntityHelper;
import iskallia.vault.world.data.PlayerVaultStatsData;
import iskallia.vault.world.data.StreamData;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;

import static net.minecraft.command.Commands.argument;
import static net.minecraft.command.Commands.literal;

public class InternalCommand extends Command {

    @Override
    public String getName() {
        return "internal";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("received_sub")
                .then(argument("subscriber", StringArgumentType.word())
                        .then(argument("months", IntegerArgumentType.integer(0))
                                .executes(context -> this.receivedSub(context, StringArgumentType.getString(context, "subscriber"), IntegerArgumentType.getInteger(context, "months"))))));

        builder.then(literal("received_sub_gift")
                .then(argument("gifter", StringArgumentType.word())
                        .then(argument("amount", IntegerArgumentType.integer())
                                .then(argument("tier", IntegerArgumentType.integer())
                                        .executes(context -> this.receivedSubGift(context, StringArgumentType.getString(context, "gifter"), IntegerArgumentType.getInteger(context, "amount"), IntegerArgumentType.getInteger(context, "tier")))))));

        builder.then(literal("received_donation")
                .then(argument("donator", StringArgumentType.word())
                        .then(argument("amount", IntegerArgumentType.integer())
                                .executes(context -> this.receivedDonation(context, StringArgumentType.getString(context, "donator"), IntegerArgumentType.getInteger(context, "amount"))))));

        builder.then(literal("received_bit_donation")
                .then(argument("donator", StringArgumentType.word())
                        .then(argument("amount", IntegerArgumentType.integer())
                                .executes(context -> this.receivedBitDonation(context, StringArgumentType.getString(context, "donator"), IntegerArgumentType.getInteger(context, "amount"))))));
    }

    private int receivedSub(CommandContext<CommandSource> context, String name, int months) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        StreamData.get(player.getServerWorld()).onSub(player.getServer(), player.getUniqueID(), name, months);
        PlayerVaultStatsData.get(player.getServerWorld()).addVaultExp(player, ModConfigs.STREAMER_EXP.getExpPerSub(player.getName().getString()));
        return 0;
    }

    private int receivedSubGift(CommandContext<CommandSource> context, String gifter, int amount, int tier) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        String mcNick = player.getDisplayName().getString();
        StreamerMultipliersConfig.StreamerMultipliers multipliers = ModConfigs.STREAMER_MULTIPLIERS.ofStreamer(mcNick);
        float multiplier = (tier == 0 || tier == 1) ? multipliers.weightPerGiftedSubT1
                : tier == 2 ? multipliers.weightPerGiftedSubT2
                : multipliers.weightPerGiftedSubT3;
        StreamData.get(player.getServerWorld()).onDono(player.getServer(), player.getUniqueID(), gifter, (int) (amount * multiplier));
        handleGiftBombs(player, gifter, amount);
        return 0;
    }

    private void handleGiftBombs(ServerPlayerEntity player, String gifter, int amount) {
        if (amount < 5) return;

        ItemGiftBomb.Variant variant = amount <= 9 ? ItemGiftBomb.Variant.NORMAL
                : amount <= 19 ? ItemGiftBomb.Variant.SUPER
                : amount <= 49 ? ItemGiftBomb.Variant.MEGA
                : ItemGiftBomb.Variant.OMEGA;

        Vector3d position = player.getPositionVec();
        player.getServerWorld().playSound(
                null,
                position.x,
                position.y,
                position.z,
                variant == ItemGiftBomb.Variant.NORMAL || variant == ItemGiftBomb.Variant.SUPER
                        ? ModSounds.GIFT_BOMB_GAIN_SFX : ModSounds.MEGA_GIFT_BOMB_GAIN_SFX,
                SoundCategory.PLAYERS,
                0.75f, 1f
        );

        ItemStack giftBomb = ItemGiftBomb.forGift(variant, gifter, amount);
        EntityHelper.giveItem(player, giftBomb);
    }

    private int receivedDonation(CommandContext<CommandSource> context, String donator, int amount) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        String mcNick = player.getDisplayName().getString();
        int multiplier = ModConfigs.STREAMER_MULTIPLIERS.ofStreamer(mcNick).weightPerDonationUnit;
        StreamData.get(player.getServerWorld()).onDono(player.getServer(), player.getUniqueID(), donator, amount * multiplier);
        if (amount >= 25) {
            ItemStack core = ItemTraderCore.generate(donator, 100 * amount, amount >= 100);
            EntityHelper.giveItem(player, core);
            traderCoreParticles(player);
        }
        GiveBitsCommand.dropBits(player, amount * 100);
        return 0;
    }

    private int receivedBitDonation(CommandContext<CommandSource> context, String donator, int amount) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        String mcNick = player.getDisplayName().getString();
        int multiplier = ModConfigs.STREAMER_MULTIPLIERS.ofStreamer(mcNick).weightPerHundredBits;
        StreamData.get(player.getServerWorld()).onDono(player.getServer(), player.getUniqueID(), donator, (amount / 100) * multiplier);
        if (amount >= 2500) {
            ItemStack core = ItemTraderCore.generate(donator, amount, amount >= 10000);
            EntityHelper.giveItem(player, core);
            traderCoreParticles(player);
        }
        GiveBitsCommand.dropBits(player, amount);
        return 0;
    }

    private void traderCoreParticles(ServerPlayerEntity player) {
        Vector3d position = player.getPositionVec();

        player.getServerWorld().playSound(
                null,
                position.x,
                position.y,
                position.z,
                SoundEvents.ENTITY_ITEM_PICKUP,
                SoundCategory.PLAYERS,
                0.75f, 1f
        );

        player.getServerWorld().spawnParticle(ParticleTypes.REVERSE_PORTAL,
                position.x,
                position.y,
                position.z,
                500,
                1, 1, 1,
                1f
        );
    }

    @Override
    public boolean isDedicatedServerOnly() {
        return false;
    }

}
