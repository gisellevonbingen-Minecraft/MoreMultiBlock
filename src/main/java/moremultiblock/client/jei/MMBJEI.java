package moremultiblock.client.jei;

import giselle.jei_mekanism_multiblocks.client.jei.JEI_MekanismMultiblocks_JeiPlugin;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import moremultiblock.MoreMultiblock;
import moremultiblock.client.jei.category.RadioactiveWasteVaultCategory;
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
    public void registerCategories(IRecipeCategoryRegistration registry) {
        if (MoreMultiblock.JustEnoughMekanismMultiblocksLoaded) {
            IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
            JEI_MekanismMultiblocks_JeiPlugin.instance().addCategory(registry, new RadioactiveWasteVaultCategory(guiHelper));
        }
    }

}
