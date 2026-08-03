package moremultiblock;

import mekanism.common.lib.multiblock.MultiblockManager;
import moremultiblock.common.content.vault.VaultCache;
import moremultiblock.common.content.vault.VaultMultiblockData;
import moremultiblock.common.content.vault.VaultValidator;
import moremultiblock.common.registries.MMBBlocks;
import moremultiblock.common.registries.MMBContainerTypes;
import moremultiblock.common.registries.MMBCreativeTabs;
import moremultiblock.common.registries.MMBTileEntityTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MoreMultiblock.MODID)
public class MoreMultiblock {

    public static final String MODID = "moremultiblock";

    public static final MultiblockManager<VaultMultiblockData> vaultManager = new MultiblockManager<>("radioactiveWasteVault", VaultCache::new, VaultValidator::new);


    public static ResourceLocation rl(String path) {
        return new ResourceLocation("moremultiblock", path);
    }

    public MoreMultiblock() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MMBBlocks.BLOCKS.register(modEventBus);
        MMBTileEntityTypes.TILE_ENTITY_TYPES.register(modEventBus);
        MMBContainerTypes.CONTAINER_TYPES.register(modEventBus);
        MMBCreativeTabs.register(modEventBus);
    }
}
