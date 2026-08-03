package moremultiblock.common.data;

import moremultiblock.MoreMultiblock;
import moremultiblock.common.registries.MMBBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MMBLootTableProvider {

    public static LootTableProvider create(PackOutput output) {
        return new LootTableProvider(
                output,
                Set.of(),
                List.of(new LootTableProvider.SubProviderEntry(MMBBlockLootTables::new, LootContextParamSets.BLOCK))
        );
    }

    public static class MMBBlockLootTables extends BlockLootSubProvider {

        public MMBBlockLootTables() {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags());
        }

        @Override
        protected void generate() {
            dropSelf(MMBBlocks.RADIOACTIVE_WASTE_VAULT.getBlock());
            dropSelf(MMBBlocks.RADIOACTIVE_WASTE_VALVE.getBlock());
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return ForgeRegistries.BLOCKS.getValues().stream()
                    .filter(block -> {
                        ResourceLocation key = ForgeRegistries.BLOCKS.getKey(block);
                        return key != null && key.getNamespace().equals(MoreMultiblock.MODID);
                    })
                    .collect(Collectors.toList());
        }
    }


}
