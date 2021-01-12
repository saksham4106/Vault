package iskallia.vault.init;

import iskallia.vault.Vault;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class ModAttributes {

	public static Attribute CRIT_CHANCE;
	public static Attribute CRIT_MULTIPLIER;

	public static void register(RegistryEvent.Register<Attribute> event) {
		CRIT_CHANCE = register(event.getRegistry(), "generic.crit_chance", new RangedAttribute("attribute.name.generic.crit_chance", 0.0D, 0.0D, 1.0D)).setShouldWatch(true);
		CRIT_MULTIPLIER = register(event.getRegistry(), "generic.crit_multiplier", new RangedAttribute("attribute.name.generic.crit_multiplier", 0.0D, 0.0D, 1024.0D)).setShouldWatch(true);
	}

	private static Attribute register(IForgeRegistry<Attribute> registry, String name, Attribute attribute) {
		registry.register(attribute.setRegistryName(Vault.id(name)));
		return attribute;
	}

}
