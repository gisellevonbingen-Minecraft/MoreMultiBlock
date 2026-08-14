package moremultiblock.common.registries;

import mekanism.common.registration.impl.TileEntityTypeDeferredRegister;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.common.tile.base.TileEntityMekanism;
import moremultiblock.MoreMultiblock;
import moremultiblock.common.tile.multiblock.*;

public class MMBTileEntityTypes {
    private MMBTileEntityTypes() {
    }

    public static final TileEntityTypeDeferredRegister TILE_ENTITY_TYPES = new TileEntityTypeDeferredRegister(MoreMultiblock.MODID);

    public static final TileEntityTypeRegistryObject<TileEntityRadioactiveWasteVault> RADIOACTIVE_WASTE_VAULT
            = TILE_ENTITY_TYPES.register(MMBBlocks.RADIOACTIVE_WASTE_VAULT, TileEntityRadioactiveWasteVault::new, TileEntityMekanism::tickServer, TileEntityMekanism::tickClient);
    public static final TileEntityTypeRegistryObject<TileEntityRadioactiveWasteValve> RADIOACTIVE_WASTE_VALVE
            = TILE_ENTITY_TYPES.register(MMBBlocks.RADIOACTIVE_WASTE_VALVE, TileEntityRadioactiveWasteValve::new, TileEntityMekanism::tickServer, TileEntityMekanism::tickClient);

    public static final TileEntityTypeRegistryObject<TileEntityLaserCoreCasing> LASER_CORE_CASING
            = TILE_ENTITY_TYPES.register(MMBBlocks.LASER_CORE_CASING, TileEntityLaserCoreCasing::new, TileEntityMekanism::tickServer, TileEntityMekanism::tickClient);

    public static final TileEntityTypeRegistryObject<TileEntityLaserCorePort> LASER_CORE_PORT
            = TILE_ENTITY_TYPES.register(MMBBlocks.LASER_CORE_PORT, TileEntityLaserCorePort::new, TileEntityMekanism::tickServer, TileEntityMekanism::tickClient);

    public static final TileEntityTypeRegistryObject<TileEntityLaserCore> LASER_CORR
            = TILE_ENTITY_TYPES.register(MMBBlocks.LASER_CORE, TileEntityLaserCore::new, TileEntityMekanism::tickServer, TileEntityMekanism::tickClient);
}
