package moremultiblock.common.data;

import mekanism.common.resource.PrimaryResource;
import mekanism.common.resource.ResourceType;
import mekanism.common.tags.MekanismTags;
import moremultiblock.common.registries.MMBBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;

import java.util.function.Consumer;

;

public class MMBRecipeProvider extends RecipeProvider {
    public MMBRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, MMBBlocks.RADIOACTIVE_WASTE_VAULT.getBlock(), 4)
                .pattern(" L ")
                .pattern("LSL")
                .pattern(" L ")
                .define('S', MekanismTags.Items.STORAGE_BLOCKS_STEEL)
                .define('L', MekanismTags.Items.PROCESSED_RESOURCE_BLOCKS.get(PrimaryResource.LEAD))
                .unlockedBy("has_lead", has(MekanismTags.Items.PROCESSED_RESOURCES.get(ResourceType.INGOT, PrimaryResource.LEAD)))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, MMBBlocks.RADIOACTIVE_WASTE_VALVE.getBlock(), 1)
                .pattern(" V ")
                .pattern("VAV")
                .pattern(" V ")
                .define('V', MMBBlocks.RADIOACTIVE_WASTE_VAULT.getBlock())
                .define('A', MekanismTags.Items.CIRCUITS_ADVANCED)
                .unlockedBy("has_lead", has(MekanismTags.Items.PROCESSED_RESOURCES.get(ResourceType.INGOT, PrimaryResource.LEAD)))
                .save(consumer);
    }
}
