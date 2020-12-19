package iskallia.vault.init;

import iskallia.vault.container.*;
import iskallia.vault.research.ResearchTree;
import iskallia.vault.skill.ability.AbilityTree;
import iskallia.vault.skill.talent.TalentTree;
import iskallia.vault.util.RenameType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.network.IContainerFactory;

import java.util.Optional;
import java.util.UUID;

public class ModContainers {

    public static ContainerType<SkillTreeContainer> SKILL_TREE_CONTAINER;
    public static ContainerType<VaultCrateContainer> VAULT_CRATE_CONTAINER;
    public static ContainerType<VendingMachineContainer> VENDING_MACHINE_CONTAINER;
    public static ContainerType<AdvancedVendingContainer> ADVANCED_VENDING_MACHINE_CONTAINER;
    public static ContainerType<RenamingContainer> RENAMING_CONTAINER;

    public static void register(RegistryEvent.Register<ContainerType<?>> event) {
        SKILL_TREE_CONTAINER = createContainerType((windowId, inventory, buffer) -> {
            UUID uniqueID = inventory.player.getUniqueID();
            AbilityTree abilityTree = new AbilityTree(uniqueID);
            abilityTree.deserializeNBT(Optional.ofNullable(buffer.readCompoundTag()).orElse(new CompoundNBT()));
            TalentTree talentTree = new TalentTree(uniqueID);
            talentTree.deserializeNBT(Optional.ofNullable(buffer.readCompoundTag()).orElse(new CompoundNBT()));
            ResearchTree researchTree = new ResearchTree(uniqueID);
            researchTree.deserializeNBT(Optional.ofNullable(buffer.readCompoundTag()).orElse(new CompoundNBT()));
            return new SkillTreeContainer(windowId, abilityTree, talentTree, researchTree);
        });

        VAULT_CRATE_CONTAINER = createContainerType((windowId, inventory, buffer) -> {
            World world = inventory.player.getEntityWorld();
            BlockPos pos = buffer.readBlockPos();
            return new VaultCrateContainer(windowId, world, pos, inventory, inventory.player);
        });

        VENDING_MACHINE_CONTAINER = createContainerType((windowId, inventory, buffer) -> {
            World world = inventory.player.getEntityWorld();
            BlockPos pos = buffer.readBlockPos();
            return new VendingMachineContainer(windowId, world, pos, inventory, inventory.player);
        });

        ADVANCED_VENDING_MACHINE_CONTAINER = createContainerType((windowId, inventory, buffer) -> {
            World world = inventory.player.getEntityWorld();
            BlockPos pos = buffer.readBlockPos();
            return new AdvancedVendingContainer(windowId, world, pos, inventory, inventory.player);
        });

        RENAMING_CONTAINER = createContainerType((windowId, inventory, buffer) -> {
            RenameType type = RenameType.values()[buffer.readInt()];
            String name = buffer.readString();
            switch (type) {
                case PLAYER_STATUE:
                    BlockPos pos = buffer.readBlockPos();
                    return new RenamingContainer(windowId, type, name, pos);
                case TRADER_CORE:
                    return new RenamingContainer(windowId, type, name, null);
            }
            return null;
        });

        event.getRegistry().registerAll(
                SKILL_TREE_CONTAINER.setRegistryName("ability_tree"),
                VAULT_CRATE_CONTAINER.setRegistryName("vault_crate"),
                VENDING_MACHINE_CONTAINER.setRegistryName("vending_machine"),
                ADVANCED_VENDING_MACHINE_CONTAINER.setRegistryName("advanced_vending_machine"),
                RENAMING_CONTAINER.setRegistryName("renaming_container")
        );
    }

    private static <T extends Container> ContainerType<T> createContainerType(IContainerFactory<T> factory) {
        return new ContainerType<T>(factory);
    }

}