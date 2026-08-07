package moremultiblock.client.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import moremultiblock.MoreMultiblock;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class MMBJEI implements IModPlugin {
    public MMBJEI() {
    }

    @Override
    public ResourceLocation getPluginUid() {
        return MoreMultiblock.rl("jei_plugin");
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        if (MoreMultiblock.JustEnoughMekanismMultiblocksLoaded) {
            MMBJEIHelper.registerRecipeCatalysts(registry);
        }
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        if (MoreMultiblock.JustEnoughMekanismMultiblocksLoaded) {
            MMBJEIHelper.registerCategories(registry);
        }
    }

    @Override
    public void registerRecipes(IRecipeRegistration registry) {
        if (MoreMultiblock.JustEnoughMekanismMultiblocksLoaded) {
            MMBJEIHelper.registerRecipes(registry);
        }
    }

}
