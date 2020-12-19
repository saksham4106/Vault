package iskallia.vault.init;

import iskallia.vault.client.gui.overlay.AbilitiesOverlay;
import iskallia.vault.client.gui.overlay.ArenaScoreboardOverlay;
import iskallia.vault.client.gui.overlay.HyperBarOverlay;
import iskallia.vault.client.gui.screen.SkillTreeScreen;
import iskallia.vault.client.gui.screen.VaultCrateScreen;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ModScreens {

    public static void register(final FMLClientSetupEvent event) {
        ScreenManager.registerFactory(ModContainers.SKILL_TREE_CONTAINER, SkillTreeScreen::new);
        ScreenManager.registerFactory(ModContainers.VAULT_CRATE_CONTAINER, VaultCrateScreen::new);
        ScreenManager.registerFactory(ModContainers.VENDING_MACHINE_CONTAINER, VendingMachineScreen::new);
        ScreenManager.registerFactory(ModContainers.ADVANCED_VENDING_MACHINE_CONTAINER, AdvancedVendingMachineScreen::new);
        ScreenManager.registerFactory(ModContainers.RENAMING_CONTAINER, RenameScreen::new);
    }

    public static void registerOverlays() {
        MinecraftForge.EVENT_BUS.register(VaultBarOverlay.class);
        MinecraftForge.EVENT_BUS.register(AbilitiesOverlay.class);
        MinecraftForge.EVENT_BUS.register(VaultRaidOverlay.class);
        MinecraftForge.EVENT_BUS.register(HyperBarOverlay.class);
        MinecraftForge.EVENT_BUS.register(ArenaScoreboardOverlay.class);
        MinecraftForge.EVENT_BUS.register(GiftBombOverlay.class);
    }

}
