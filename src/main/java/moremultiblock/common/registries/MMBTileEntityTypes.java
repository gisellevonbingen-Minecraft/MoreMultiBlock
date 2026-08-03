package moremultiblock.common.registries;

import mekanism.common.registration.impl.TileEntityTypeDeferredRegister;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.common.tile.base.TileEntityMekanism;
import moremultiblock.MoreMultiblock;
import moremultiblock.common.tile.multiblock.TileEntityRadioactiveWasteValve;
import moremultiblock.common.tile.multiblock.TileEntityRadioactiveWasteVault;

public class MMBTileEntityTypes {
    private MMBTileEntityTypes() {
    }

    public static final TileEntityTypeDeferredRegister TILE_ENTITY_TYPES = new TileEntityTypeDeferredRegister(MoreMultiblock.MODID);

    public static final TileEntityTypeRegistryObject<TileEntityRadioactiveWasteVault> RADIOACTIVE_WASTE_VAULT = TILE_ENTITY_TYPES.register(MMBBlocks.RADIOACTIVE_WASTE_VAULT, TileEntityRadioactiveWasteVault::new, TileEntityMekanism::tickServer, TileEntityMekanism::tickClient);
    public static final TileEntityTypeRegistryObject<TileEntityRadioactiveWasteValve> RADIOACTIVE_WASTE_VALVE = TILE_ENTITY_TYPES.register(MMBBlocks.RADIOACTIVE_WASTE_VALVE, TileEntityRadioactiveWasteValve::new, TileEntityMekanism::tickServer, TileEntityMekanism::tickClient);
}
