package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.init.ModItems;
import iskallia.vault.item.RelicPartItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class VaultRelicsConfig extends Config {

    @Expose private int extraTickPerSet;
    @Expose private List<Relic> relicDrops;

    @Override
    public String getName() {
        return "vault_relics";
    }

    public int getExtraTickPerSet() {
        return extraTickPerSet;
    }

    public RelicPartItem getRandomPart() {
        relicDrops.sort(Comparator.comparingInt(a -> a.WEIGHT));

        int totalWeight = relicDrops.stream().mapToInt(relic -> relic.WEIGHT).sum();
        int random = new Random().nextInt(totalWeight);

        for (Relic relicDrop : relicDrops) {
            if (random < relicDrop.WEIGHT)
                return (RelicPartItem) Registry.ITEM.getOrDefault(new ResourceLocation(relicDrop.NAME));
            random -= relicDrop.WEIGHT;
        }

        return (RelicPartItem) Registry.ITEM.getOrDefault(new ResourceLocation(relicDrops.get(relicDrops.size() - 1).NAME));
    }

    @Override
    protected void reset() {
        this.extraTickPerSet = 60 * 20;

        this.relicDrops = new LinkedList<>();
        this.relicDrops.add(new Relic(ModItems.DRAGON_HEAD_RELIC.getRegistryName().toString(), 1));
        this.relicDrops.add(new Relic(ModItems.DRAGON_TAIL_RELIC.getRegistryName().toString(), 1));
        this.relicDrops.add(new Relic(ModItems.DRAGON_FOOT_RELIC.getRegistryName().toString(), 1));
        this.relicDrops.add(new Relic(ModItems.DRAGON_CHEST_RELIC.getRegistryName().toString(), 1));
        this.relicDrops.add(new Relic(ModItems.DRAGON_BREATH_RELIC.getRegistryName().toString(), 1));
        this.relicDrops.add(new Relic(ModItems.MINERS_DELIGHT_RELIC.getRegistryName().toString(), 1));
        this.relicDrops.add(new Relic(ModItems.MINERS_LIGHT_RELIC.getRegistryName().toString(), 1));
        this.relicDrops.add(new Relic(ModItems.PICKAXE_HANDLE_RELIC.getRegistryName().toString(), 1));
        this.relicDrops.add(new Relic(ModItems.PICKAXE_HEAD_RELIC.getRegistryName().toString(), 1));
        this.relicDrops.add(new Relic(ModItems.PICKAXE_TOOL_RELIC.getRegistryName().toString(), 1));
        this.relicDrops.add(new Relic(ModItems.SWORD_BLADE_RELIC.getRegistryName().toString(), 1));
        this.relicDrops.add(new Relic(ModItems.SWORD_HANDLE_RELIC.getRegistryName().toString(), 1));
        this.relicDrops.add(new Relic(ModItems.SWORD_STICK_RELIC.getRegistryName().toString(), 1));
        this.relicDrops.add(new Relic(ModItems.WARRIORS_ARMOUR_RELIC.getRegistryName().toString(), 1));
        this.relicDrops.add(new Relic(ModItems.WARRIORS_CHARM_RELIC.getRegistryName().toString(), 1));
        this.relicDrops.add(new Relic(ModItems.DIAMOND_ESSENCE_RELIC.getRegistryName().toString(), 1));
        this.relicDrops.add(new Relic(ModItems.GOLD_ESSENCE_RELIC.getRegistryName().toString(), 1));
        this.relicDrops.add(new Relic(ModItems.MYSTIC_GEM_ESSENCE_RELIC.getRegistryName().toString(), 1));
        this.relicDrops.add(new Relic(ModItems.NETHERITE_ESSENCE_RELIC.getRegistryName().toString(), 1));
        this.relicDrops.add(new Relic(ModItems.PLATINUM_ESSENCE_RELIC.getRegistryName().toString(), 1));
        this.relicDrops.add(new Relic(ModItems.TWITCH_EMOTE1_RELIC.getRegistryName().toString(), 1));
        this.relicDrops.add(new Relic(ModItems.TWITCH_EMOTE2_RELIC.getRegistryName().toString(), 1));
        this.relicDrops.add(new Relic(ModItems.TWITCH_EMOTE3_RELIC.getRegistryName().toString(), 1));
        this.relicDrops.add(new Relic(ModItems.TWITCH_EMOTE4_RELIC.getRegistryName().toString(), 1));
        this.relicDrops.add(new Relic(ModItems.TWITCH_EMOTE5_RELIC.getRegistryName().toString(), 1));
    }

    public static class Relic {
        @Expose public String NAME;
        @Expose public int WEIGHT;

        public Relic(String name, int weight) {
            this.NAME = name;
            this.WEIGHT = weight;
        }
    }

}
