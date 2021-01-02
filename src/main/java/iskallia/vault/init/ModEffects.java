package iskallia.vault.init;

import iskallia.vault.Vault;
import iskallia.vault.effect.GhostWalkEffect;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.event.RegistryEvent;

import java.awt.*;

public class ModEffects {

    public static final Effect GHOST_WALK = new GhostWalkEffect(EffectType.BENEFICIAL, Color.GRAY.getRGB(), Vault.id("ghost_walk"));

    public static void register(RegistryEvent.Register<Effect> event) {
        register(GHOST_WALK, event);
    }

    /* --------------------------------------------- */

    private static <T extends Effect> void register(T effect, RegistryEvent.Register<Effect> event) {
        event.getRegistry().register(effect);
    }

}
