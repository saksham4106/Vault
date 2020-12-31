package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.client.gui.helper.SkillFrame;
import iskallia.vault.config.entry.SkillStyle;
import iskallia.vault.init.ModConfigs;

import java.util.HashMap;

public class TalentsGUIConfig extends Config {

    @Expose private HashMap<String, SkillStyle> styles;

    @Override
    public String getName() {
        return "talents_gui_styles";
    }

    public HashMap<String, SkillStyle> getStyles() {
        return styles;
    }

    @Override
    protected void reset() {
        SkillStyle style;
        this.styles = new HashMap<>();

        style = new SkillStyle(0, 0, 16 * 6, 0);
        style.frameType = SkillFrame.RECTANGULAR;
        styles.put(ModConfigs.TALENTS.HASTE.getParentName(), style);

        style = new SkillStyle(70, 0, 16 * 3, 0);
        style.frameType = SkillFrame.RECTANGULAR;
        styles.put(ModConfigs.TALENTS.REGENERATION.getParentName(), style);

        style = new SkillStyle(70 * 2, 0, 16 * 1, 0);
        style.frameType = SkillFrame.RECTANGULAR;
        styles.put(ModConfigs.TALENTS.VAMPIRISM.getParentName(), style);

        style = new SkillStyle(70 * 3, 0, 16 * 7, 0);
        style.frameType = SkillFrame.RECTANGULAR;
        styles.put(ModConfigs.TALENTS.RESISTANCE.getParentName(), style);

        style = new SkillStyle(70 * 4, 0, 16 * 8, 0);
        style.frameType = SkillFrame.RECTANGULAR;
        styles.put(ModConfigs.TALENTS.STRENGTH.getParentName(), style);

        style = new SkillStyle(70 * 5, 0, 16 * 4, 0);
        style.frameType = SkillFrame.RECTANGULAR;
        styles.put(ModConfigs.TALENTS.FIRE_RESISTANCE.getParentName(), style);

        style = new SkillStyle(0, 70, 16 * 9, 0);
        style.frameType = SkillFrame.RECTANGULAR;
        styles.put(ModConfigs.TALENTS.SPEED.getParentName(), style);

        style = new SkillStyle(70, 70, 0, 0);
        style.frameType = SkillFrame.RECTANGULAR;
        styles.put(ModConfigs.TALENTS.WATER_BREATHING.getParentName(), style);

        style = new SkillStyle(70 * 2, 70, 16 * 2, 0);
        style.frameType = SkillFrame.STAR;
        styles.put(ModConfigs.TALENTS.WELL_FIT.getParentName(), style);

        style = new SkillStyle(70 * 3, 70, 16 * 13, 0);
        style.frameType = SkillFrame.RECTANGULAR;
        styles.put(ModConfigs.TALENTS.TWERKER.getParentName(), style);

        style = new SkillStyle(70 * 4, 70, 16 * 11, 0);
        style.frameType = SkillFrame.RECTANGULAR;
        styles.put(ModConfigs.TALENTS.ELVISH.getParentName(), style);

        style = new SkillStyle(70 * 5, 70, 16 * 14, 0);
        style.frameType = SkillFrame.RECTANGULAR;
        styles.put(ModConfigs.TALENTS.ANGEL.getParentName(), style);

        style = new SkillStyle(0, 70 * 2, 16 * 10, 0);
        style.frameType = SkillFrame.STAR;
        styles.put(ModConfigs.TALENTS.REACH.getParentName(), style);

        style = new SkillStyle(70, 70 * 2, 16 * 10, 0);
        style.frameType = SkillFrame.STAR;
        styles.put(ModConfigs.TALENTS.EXPERIENCED.getParentName(), style);

        style = new SkillStyle(70 * 2, 70 * 2, 16 * 10, 0);
        style.frameType = SkillFrame.STAR;
        styles.put(ModConfigs.TALENTS.PARRY.getParentName(), style);
    }

}
