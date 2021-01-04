package iskallia.vault.skill.ability.type;

import iskallia.vault.init.ModSounds;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundCategory;

public class CleanseAbility extends PlayerAbility {

    public CleanseAbility(int cost, int cooldown) {
        super(cost, Behavior.RELEASE_TO_PERFORM);
        this.cooldown = cooldown;
    }

    @Override
    public void onAction(PlayerEntity player, boolean active) {
        player.removePotionEffect(Effects.WITHER);
        player.removePotionEffect(Effects.POISON);

        player.world.playSound(player, player.getPosX(), player.getPosY(), player.getPosZ(),
                ModSounds.CLEANSE_SFX, SoundCategory.MASTER, 0.7f, 1f);
        player.playSound(ModSounds.CLEANSE_SFX, SoundCategory.MASTER, 0.7f, 1f);
    }

}
