package iskallia.vault.skill.talent.type;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.talent.TalentNode;
import iskallia.vault.skill.talent.TalentTree;
import iskallia.vault.util.MathUtilities;
import iskallia.vault.world.data.PlayerTalentsData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ParryTalent extends PlayerTalent {

    @Expose private final float parryChance;

    public ParryTalent(int cost, float parryChance) {
        super(cost);
        this.parryChance = parryChance;
    }

    public float getParryChance() {
        return parryChance;
    }

    @SubscribeEvent
    public static void onPlayerDamage(LivingAttackEvent event) {
        if (event.getEntityLiving().world.isRemote) return;
        if (!(event.getEntityLiving() instanceof ServerPlayerEntity)) return;

        ServerPlayerEntity player = (ServerPlayerEntity) event.getEntityLiving();
        TalentTree abilities = PlayerTalentsData.get(player.getServerWorld()).getTalents(player);

        for (TalentNode<?> node : abilities.getNodes()) {
            if (!(node.getTalent() instanceof ParryTalent)) continue;
            ParryTalent talent = (ParryTalent) node.getTalent();

            if (MathUtilities.randomFloat(0, 1) <= talent.getParryChance()) {
                event.setCanceled(true);

                player.world.playSound(
                        null,
                        player.getPosX(),
                        player.getPosY(),
                        player.getPosZ(),
                        SoundEvents.ITEM_SHIELD_BLOCK,
                        SoundCategory.MASTER,
                        1F, 1F
                );
            }
        }
    }

}
