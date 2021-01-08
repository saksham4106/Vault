package iskallia.vault.skill.talent.type;

import com.google.gson.annotations.Expose;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModSounds;
import iskallia.vault.skill.talent.TalentNode;
import iskallia.vault.skill.talent.TalentTree;
import iskallia.vault.util.MathUtilities;
import iskallia.vault.world.data.PlayerTalentsData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class VampirismTalent extends PlayerTalent {

    @Expose
    private final float leechRatio;

    public VampirismTalent(int cost, float leechRatio) {
        super(cost);
        this.leechRatio = leechRatio;
    }

    public float getLeechRatio() {
        return this.leechRatio;
    }

    public void onDamagedEntity(PlayerEntity player, LivingHurtEvent event, int level) {
        float ratio = ModConfigs.TALENTS.VAMPIRISM.getTalent(level).getLeechRatio();
        player.heal(event.getAmount() * ratio);

        if (player.getRNG().nextFloat() <= 0.2) {
            float pitch = MathUtilities.randomFloat(1f, 1.5f);
            player.world.playSound(player, player.getPosX(), player.getPosY(), player.getPosZ(),
                    ModSounds.VAMPIRE_HISSING_SFX, SoundCategory.MASTER, 0.2f * 0.1f, pitch);
            player.playSound(ModSounds.VAMPIRE_HISSING_SFX, SoundCategory.MASTER, 0.2f * 0.1f, pitch);
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (!(event.getSource().getTrueSource() instanceof ServerPlayerEntity)) return;
        ServerPlayerEntity player = (ServerPlayerEntity) event.getSource().getTrueSource();
        TalentTree abilities = PlayerTalentsData.get(player.getServerWorld()).getTalents(player);

        for (TalentNode<?> node : abilities.getNodes()) {
            if (!(node.getTalent() instanceof VampirismTalent)) continue;
            VampirismTalent vampirism = (VampirismTalent) node.getTalent();
            int level = node.getLevel();
            vampirism.onDamagedEntity(player, event, level);
        }
    }

}
