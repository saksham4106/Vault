package iskallia.vault.skill.ability.type;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;

public class CleanseAbility extends PlayerAbility {

    public CleanseAbility(int cost, int cooldown) {
        super(cost, Behavior.RELEASE_TO_PERFORM);
        this.cooldown = cooldown;
    }

    @Override
    public void onAction(PlayerEntity player, boolean active) {
        player.removePotionEffect(Effects.WITHER);
        player.removePotionEffect(Effects.POISON);
    }

}
