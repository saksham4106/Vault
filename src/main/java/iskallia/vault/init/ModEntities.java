package iskallia.vault.init;

import iskallia.vault.Vault;
import iskallia.vault.entity.*;
import iskallia.vault.entity.renderer.*;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.function.Supplier;

public class ModEntities {

    public static EntityType<FighterEntity> FIGHTER;

    public static EntityType<ArenaFighterEntity> ARENA_FIGHTER;
    public static EntityType<ArenaBossEntity> ARENA_BOSS;
    public static EntityType<MonsterEyeEntity> MONSTER_EYE;
    public static EntityType<RobotEntity> ROBOT;
    public static EntityType<BlueBlazeEntity> BLUE_BLAZE;
    public static EntityType<BoogiemanEntity> BOOGIEMAN;
    public static EntityType<ArenaTrackerEntity> ARENA_TRACKER;
    public static EntityType<VaultGuardianEntity> VAULT_GUARDIAN;

    public static void register(RegistryEvent.Register<EntityType<?>> event) {
        FIGHTER = register("fighter", EntityType.Builder.create(FighterEntity::new, EntityClassification.MONSTER)
                .size(0.6F, 1.95F), ZombieEntity::func_234342_eQ_, event);
        ARENA_FIGHTER = register("arena_fighter", EntityType.Builder.create(ArenaFighterEntity::new, EntityClassification.MONSTER)
                .size(0.6F, 1.95F), ZombieEntity::func_234342_eQ_, event);
        ARENA_BOSS = register("arena_boss", EntityType.Builder.create(ArenaBossEntity::new, EntityClassification.MONSTER)
                .size(0.6F, 1.95F), ArenaBossEntity::getAttributes, event);
        MONSTER_EYE = register("monster_eye", EntityType.Builder.create(MonsterEyeEntity::new, EntityClassification.MONSTER)
                .size(2.04F * 2, 2.04F * 2), ZombieEntity::func_234342_eQ_, event);
        ROBOT = register("robot", EntityType.Builder.create(RobotEntity::new, EntityClassification.MONSTER)
                .size(1.4F * 2, 2.7F * 2), ZombieEntity::func_234342_eQ_, event);
        BLUE_BLAZE = register("blue_blaze", EntityType.Builder.create(BlueBlazeEntity::new, EntityClassification.MONSTER)
                .size(0.6F * 2, 1.8F * 2), ZombieEntity::func_234342_eQ_, event);
        BOOGIEMAN = register("boogieman", EntityType.Builder.create(BoogiemanEntity::new, EntityClassification.MONSTER)
                .size(0.6F * 2, 1.95F * 2), ZombieEntity::func_234342_eQ_, event);
        ARENA_TRACKER = register("arena_tracker", EntityType.Builder.create(ArenaTrackerEntity::new, EntityClassification.MISC)
                .size(0.0F, 0.0F), ZombieEntity::func_234342_eQ_, event);
        VAULT_GUARDIAN = register("vault_guardian", EntityType.Builder.create(VaultGuardianEntity::new, EntityClassification.MONSTER)
                .size(1.3F, 2.95F), ZombieEntity::func_234342_eQ_, event);
    }

    public static <T extends LivingEntity> EntityType<T> register(String name, EntityType.Builder<T> builder, Supplier<AttributeModifierMap.MutableAttribute> attributes, RegistryEvent.Register<EntityType<?>> event) {
        EntityType<T> entityType = builder.build(Vault.sId(name));
        event.getRegistry().register(entityType.setRegistryName(Vault.id(name)));
        if (attributes != null) GlobalEntityTypeAttributes.put(entityType, attributes.get().create());
        return entityType;
    }

    public static class Renderers {
        public static void register(FMLClientSetupEvent event) {
            RenderingRegistry.registerEntityRenderingHandler(FIGHTER, FighterRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler(ARENA_FIGHTER, FighterRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler(ARENA_BOSS, FighterRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler(MONSTER_EYE, MonsterEyeRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler(ROBOT, RobotRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler(BLUE_BLAZE, BlueBlazeRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler(BOOGIEMAN, BoogiemanRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler(ARENA_TRACKER, ArenaTrackerRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler(VAULT_GUARDIAN, VaultGuardianRenderer::new);
        }
    }

}
