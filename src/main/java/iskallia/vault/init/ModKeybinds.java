package iskallia.vault.init;

import iskallia.vault.Vault;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.awt.event.KeyEvent;

@OnlyIn(Dist.CLIENT)
public class ModKeybinds {

    public static KeyBinding openAbilityTree;
    public static KeyBinding openRaffleScreen;
    public static KeyBinding abilityKey;
    public static KeyBinding globalTimerKey;
    public static KeyBinding abilityWheelKey;

    public static void register(final FMLClientSetupEvent event) {
        openAbilityTree = createKeyBinding("open_ability_tree", KeyEvent.VK_H);
        openRaffleScreen = createKeyBinding("open_raffle_screen", 295); // --> Supposed to be F6
        abilityKey = createKeyBinding("ability_key", KeyEvent.VK_G);
        globalTimerKey = createKeyBinding("global_timer_key", KeyEvent.VK_P);
        abilityWheelKey = createKeyBinding("ability_wheel_key", 342); // --> Supposed to be R_ALT

        ClientRegistry.registerKeyBinding(openAbilityTree);
        ClientRegistry.registerKeyBinding(openRaffleScreen);
        ClientRegistry.registerKeyBinding(abilityKey);
        ClientRegistry.registerKeyBinding(globalTimerKey);
        ClientRegistry.registerKeyBinding(abilityWheelKey);
    }

    private static KeyBinding createKeyBinding(String name, int key) {
        return new KeyBinding(
                "key." + Vault.MOD_ID + "." + name,
                key,
                "key.category." + Vault.MOD_ID
        );
    }
}
