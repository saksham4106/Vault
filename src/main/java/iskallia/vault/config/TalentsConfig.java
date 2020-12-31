package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.talent.TalentGroup;
import iskallia.vault.skill.talent.type.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.Effects;
import net.minecraftforge.common.ForgeMod;

import java.util.Arrays;
import java.util.List;

public class TalentsConfig extends Config {

    @Expose public TalentGroup<EffectTalent> HASTE;
    @Expose public TalentGroup<EffectTalent> REGENERATION;
    @Expose public TalentGroup<VampirismTalent> VAMPIRISM;
    @Expose public TalentGroup<EffectTalent> RESISTANCE;
    @Expose public TalentGroup<EffectTalent> STRENGTH;
    @Expose public TalentGroup<EffectTalent> FIRE_RESISTANCE;
    @Expose public TalentGroup<EffectTalent> SPEED;
    @Expose public TalentGroup<EffectTalent> WATER_BREATHING;
    @Expose public TalentGroup<AttributeTalent> WELL_FIT;
    @Expose public TalentGroup<AttributeTalent> REACH;
    @Expose public TalentGroup<TwerkerTalent> TWERKER;
    @Expose public TalentGroup<ElvishTalent> ELVISH;
    @Expose public TalentGroup<AngelTalent> ANGEL;
    @Expose public TalentGroup<ExperiencedTalent> EXPERIENCED;
    @Expose public TalentGroup<ParryTalent> PARRY;

    @Override
    public String getName() {
        return "talents";
    }

    public List<TalentGroup<?>> getAll() {
        return Arrays.asList(HASTE, REGENERATION, VAMPIRISM, RESISTANCE, STRENGTH, FIRE_RESISTANCE, SPEED,
                WATER_BREATHING, WELL_FIT, TWERKER, ELVISH, ANGEL, REACH, EXPERIENCED, PARRY);
    }

    public TalentGroup<?> getByName(String name) {
        return this.getAll().stream().filter(group -> group.getParentName().equals(name)).findFirst()
                .orElseThrow(() -> new IllegalStateException("Unknown talent with name " + name));
    }

    @Override
    protected void reset() {
        this.HASTE = TalentGroup.ofEffect("Haste", Effects.HASTE, EffectTalent.Type.ICON_ONLY, 3, i -> 2);
        this.REGENERATION = TalentGroup.ofEffect("Regeneration", Effects.REGENERATION, EffectTalent.Type.ICON_ONLY, 1, i -> 3);
        this.VAMPIRISM = new TalentGroup<>("Vampirism", new VampirismTalent(2, 0.2F), new VampirismTalent(2, 0.4F), new VampirismTalent(2, 0.6F));
        this.RESISTANCE = TalentGroup.ofEffect("Resistance", Effects.RESISTANCE, EffectTalent.Type.ICON_ONLY, 2, i -> 3);
        this.STRENGTH = TalentGroup.ofEffect("Strength", Effects.STRENGTH, EffectTalent.Type.ICON_ONLY, 3, i -> 3);
        this.FIRE_RESISTANCE = TalentGroup.ofEffect("Fire Resistance", Effects.FIRE_RESISTANCE, EffectTalent.Type.ICON_ONLY, 1, i -> 5);
        this.SPEED = TalentGroup.ofEffect("Speed", Effects.SPEED, EffectTalent.Type.ICON_ONLY, 2, i -> 3);
        this.WATER_BREATHING = TalentGroup.ofEffect("Water Breathing", Effects.WATER_BREATHING, EffectTalent.Type.ICON_ONLY, 1, i -> 5);
        this.WELL_FIT = TalentGroup.ofAttribute("Well Fit", Attributes.MAX_HEALTH, "Extra Health", 10, i -> 1, i -> i * 2.0D, i -> AttributeModifier.Operation.ADDITION);
        this.REACH = TalentGroup.ofAttribute("Reach", ForgeMod.REACH_DISTANCE.get(), "Maximum Reach", 10, i -> 1, i -> i * 1.0D, i -> AttributeModifier.Operation.ADDITION);
        this.TWERKER = new TalentGroup<>("Twerker", new TwerkerTalent(4));
        this.ELVISH = new TalentGroup<>("Elvish", new ElvishTalent(5));
        this.ANGEL = new TalentGroup<>("Angel", new AngelTalent(15));
        this.EXPERIENCED = new TalentGroup<>("Experienced", new ExperiencedTalent(2, 0.20f), new ExperiencedTalent(2, 0.40f), new ExperiencedTalent(2, 0.60f), new ExperiencedTalent(2, 0.80f), new ExperiencedTalent(2, 1.00f), new ExperiencedTalent(2, 1.20f), new ExperiencedTalent(2, 1.40f), new ExperiencedTalent(2, 1.60f), new ExperiencedTalent(2, 1.80f), new ExperiencedTalent(2, 2.00f));
        this.PARRY = new TalentGroup<>("Parry", new ParryTalent(2, 0.02f), new ParryTalent(2, 0.04f), new ParryTalent(2, 0.06f), new ParryTalent(2, 0.08f), new ParryTalent(2, 0.10f), new ParryTalent(2, 0.12f), new ParryTalent(2, 0.14f), new ParryTalent(2, 0.16f), new ParryTalent(2, 0.18f), new ParryTalent(2, 0.20f));
    }

}
