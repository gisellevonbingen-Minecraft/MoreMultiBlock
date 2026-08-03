package moremultiblock.common.data;

import moremultiblock.MoreMultiblock;
import moremultiblock.common.registries.MMBBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class MMBBlockTagProvider extends BlockTagsProvider {
    public MMBBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, MoreMultiblock.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(MMBBlocks.RADIOACTIVE_WASTE_VAULT.getBlock())
                .add(MMBBlocks.RADIOACTIVE_WASTE_VALVE.getBlock())
        ;
        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(MMBBlocks.RADIOACTIVE_WASTE_VAULT.getBlock())
                .add(MMBBlocks.RADIOACTIVE_WASTE_VALVE.getBlock())
        ;
    }
}
