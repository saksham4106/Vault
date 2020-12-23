package iskallia.vault.init;

import iskallia.vault.Vault;
import iskallia.vault.recipe.RelicSetRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraftforge.event.RegistryEvent;

public class ModRecipes {

	public static class Serializer {
		public static SpecialRecipeSerializer<RelicSetRecipe> CRAFTING_SPECIAL_RELIC_SET;

		public static void register(RegistryEvent.Register<IRecipeSerializer<?>> event) {
			CRAFTING_SPECIAL_RELIC_SET = register(event, "crafting_special_relic_set", new SpecialRecipeSerializer<>(RelicSetRecipe::new));
		}

		private static <T extends IRecipe<?>> SpecialRecipeSerializer<T>
        register(RegistryEvent.Register<IRecipeSerializer<?>> event, String name, SpecialRecipeSerializer<T> serializer) {
			serializer.setRegistryName(Vault.id(name));
			event.getRegistry().register(serializer);
			return serializer;
		}
	}

}
