package iskallia.vault.init;

import iskallia.vault.config.*;

public class ModConfigs {

    public static StreamerMultipliersConfig STREAMER_MULTIPLIERS;
    public static AbilitiesConfig ABILITIES;
    public static AbilitiesGUIConfig ABILITIES_GUI;
    public static TalentsConfig TALENTS;
    public static TalentsGUIConfig TALENTS_GUI;
    public static ResearchConfig RESEARCHES;
    public static ResearchesGUIConfig RESEARCHES_GUI;
    public static SkillDescriptionsConfig SKILL_DESCRIPTIONS;
    public static SkillGatesConfig SKILL_GATES;
    public static VaultLevelsConfig LEVELS_META;
    public static StreamerExpConfig STREAMER_EXP;
    public static VaultRelicsConfig VAULT_RELICS;
    public static VaultOreConfig VAULT_ORES;
    public static VaultMobsConfig VAULT_MOBS;
    public static VaultItemsConfig VAULT_ITEMS;
    public static VaultAltarConfig VAULT_ALTAR;
    public static VaultGeneralConfig VAULT_GENERAL;
    public static VaultCrystalConfig VAULT_CRYSTAL;
    public static VaultPortalConfig VAULT_PORTAL;
    public static VaultVendingConfig VENDING_CONFIG;
    public static ArenaGeneralConfig ARENA_GENERAL;
    public static ArenaMobsConfig ARENA_MOBS;
    public static LegendaryTreasureNormalConfig LEGENDARY_TREASURE_NORMAL;
    public static LegendaryTreasureRareConfig LEGENDARY_TREASURE_RARE;
    public static LegendaryTreasureEpicConfig LEGENDARY_TREASURE_EPIC;
    public static LegendaryTreasureOmegaConfig LEGENDARY_TREASURE_OMEGA;
    public static GiftBombConfig GIFT_BOMB;
    public static StatueLootConfig STATUE_LOOT;
    public static CryoChamberConfig CRYO_CHAMBER;
    public static KeyPressRecipesConfig KEY_PRESS;
    public static OverLevelEnchantConfig OVERLEVEL_ENCHANT;
    public static VaultStewConfig VAULT_STEW;

    public static void register() {
        STREAMER_MULTIPLIERS = (StreamerMultipliersConfig) new StreamerMultipliersConfig().readConfig();
        ABILITIES = (AbilitiesConfig) new AbilitiesConfig().readConfig();
        ABILITIES_GUI = (AbilitiesGUIConfig) new AbilitiesGUIConfig().readConfig();
        TALENTS = (TalentsConfig) new TalentsConfig().readConfig();
        TALENTS_GUI = (TalentsGUIConfig) new TalentsGUIConfig().readConfig();
        RESEARCHES = (ResearchConfig) new ResearchConfig().readConfig();
        RESEARCHES_GUI = (ResearchesGUIConfig) new ResearchesGUIConfig().readConfig();
        SKILL_DESCRIPTIONS = (SkillDescriptionsConfig) new SkillDescriptionsConfig().readConfig();
        SKILL_GATES = (SkillGatesConfig) new SkillGatesConfig().readConfig();
        LEVELS_META = (VaultLevelsConfig) new VaultLevelsConfig().readConfig();
        STREAMER_EXP = (StreamerExpConfig) new StreamerExpConfig().readConfig();
        VAULT_RELICS = (VaultRelicsConfig) new VaultRelicsConfig().readConfig();
        VAULT_ORES = (VaultOreConfig) new VaultOreConfig().readConfig();
        VAULT_MOBS = (VaultMobsConfig) new VaultMobsConfig().readConfig();
        VAULT_ITEMS = (VaultItemsConfig) new VaultItemsConfig().readConfig();
        VAULT_ALTAR = (VaultAltarConfig) new VaultAltarConfig().readConfig();
        VAULT_GENERAL = (VaultGeneralConfig) new VaultGeneralConfig().readConfig();
        VAULT_CRYSTAL = (VaultCrystalConfig) new VaultCrystalConfig().readConfig();
        VAULT_PORTAL = (VaultPortalConfig) new VaultPortalConfig().readConfig();
        VENDING_CONFIG = (VaultVendingConfig) new VaultVendingConfig().readConfig();
        ARENA_GENERAL = (ArenaGeneralConfig) new ArenaGeneralConfig().readConfig();
        ARENA_MOBS = (ArenaMobsConfig) new ArenaMobsConfig().readConfig();
        LEGENDARY_TREASURE_NORMAL = (LegendaryTreasureNormalConfig) new LegendaryTreasureNormalConfig().readConfig();
        LEGENDARY_TREASURE_RARE = (LegendaryTreasureRareConfig) new LegendaryTreasureRareConfig().readConfig();
        LEGENDARY_TREASURE_EPIC = (LegendaryTreasureEpicConfig) new LegendaryTreasureEpicConfig().readConfig();
        LEGENDARY_TREASURE_OMEGA = (LegendaryTreasureOmegaConfig) new LegendaryTreasureOmegaConfig().readConfig();
        GIFT_BOMB = (GiftBombConfig) new GiftBombConfig().readConfig();
        STATUE_LOOT = (StatueLootConfig) new StatueLootConfig().readConfig();
        CRYO_CHAMBER = (CryoChamberConfig) new CryoChamberConfig().readConfig();
        KEY_PRESS = (KeyPressRecipesConfig) new KeyPressRecipesConfig().readConfig();
        OVERLEVEL_ENCHANT = (OverLevelEnchantConfig) new OverLevelEnchantConfig().readConfig();
        VAULT_STEW = (VaultStewConfig) new VaultStewConfig().readConfig();
    }

}
