package iskallia.vault.event;

import iskallia.vault.config.entry.EnchantedBookEntry;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.util.OverlevelEnchantHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AnvilEvents {

    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack equipment = event.getLeft();
        ItemStack enchantedBook = event.getRight();

        if (equipment.getItem() == Items.ENCHANTED_BOOK) return;
        if (enchantedBook.getItem() != Items.ENCHANTED_BOOK) return;

        ItemStack upgradedEquipment = equipment.copy();

        Map<Enchantment, Integer> equipmentEnchantments = OverlevelEnchantHelper.getEnchantments(equipment);
        Map<Enchantment, Integer> bookEnchantments = OverlevelEnchantHelper.getEnchantments(enchantedBook);
        int overlevels = OverlevelEnchantHelper.getOverlevels(enchantedBook);

        if (overlevels == -1) return; // No over-levels, let vanilla do its thing

        Map<Enchantment, Integer> enchantmentsToApply = new HashMap<>(equipmentEnchantments);

        for (Enchantment bookEnchantment : bookEnchantments.keySet()) {
            if (!equipmentEnchantments.containsKey(bookEnchantment)) continue;
            int currentLevel = equipmentEnchantments.getOrDefault(bookEnchantment, 0);
            int bookLevel = bookEnchantments.get(bookEnchantment);
            int nextLevel = currentLevel == bookLevel ? currentLevel + 1 : Math.max(currentLevel, bookLevel);
            enchantmentsToApply.put(bookEnchantment, nextLevel);
        }

        EnchantmentHelper.setEnchantments(enchantmentsToApply, upgradedEquipment);

        if (upgradedEquipment.equals(equipment, true)) {
            event.setCanceled(true);
        } else {
            EnchantedBookEntry bookTier = ModConfigs.OVERLEVEL_ENCHANT.getTier(overlevels);
            event.setOutput(upgradedEquipment);
            event.setCost(bookTier == null ? 1 : bookTier.getLevelNeeded());
        }
    }

}
