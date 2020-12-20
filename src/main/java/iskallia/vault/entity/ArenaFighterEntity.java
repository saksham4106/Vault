package iskallia.vault.entity;

import iskallia.vault.Vault;
import iskallia.vault.world.data.ArenaRaidData;
import iskallia.vault.world.raid.ArenaRaid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.UUID;

public class ArenaFighterEntity extends FighterEntity {

    private boolean immuneToFall = true;

    public ArenaFighterEntity(EntityType<? extends ZombieEntity> type, World world) {
        super(type, world);
        this.setCustomName(new StringTextComponent("Subscriber" + (int)(Math.random() * 100)));
    }

    @Override
    protected void applyEntityAI() {
        super.applyEntityAI();
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, ArenaBossEntity.class, false));
        this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(100.0D);
    }

    @Override
    public void tick() {
        super.tick();

        if(!this.world.isRemote && this.getAttackTarget() == null && this.world.getDimensionKey() == Vault.ARENA_KEY) {
            ArenaRaid raid = ArenaRaidData.get((ServerWorld)this.world).getAt(this.getPosition());
            if(raid == null || raid.spawner.bosses.isEmpty())return;

            UUID target = raid.spawner.bosses.get(this.rand.nextInt(raid.spawner.bosses.size()));
            Entity targetEntity = ((ServerWorld)this.world).getEntityByUuid(target);

            if(targetEntity instanceof ArenaBossEntity) {
                this.setAttackTarget((ArenaBossEntity)targetEntity);
            }
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if(source == DamageSource.FALL && this.immuneToFall) {
            this.immuneToFall = false;
            return false;
        }

        return super.attackEntityFrom(source, amount);
    }

}
