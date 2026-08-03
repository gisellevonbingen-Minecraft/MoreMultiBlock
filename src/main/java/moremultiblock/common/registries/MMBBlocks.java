package moremultiblock.common.registries;

import mekanism.common.block.interfaces.IHasDescription;
import mekanism.common.block.prefab.BlockBasicMultiblock;
import mekanism.common.item.block.ItemBlockTooltip;
import mekanism.common.registration.impl.BlockDeferredRegister;
import mekanism.common.registration.impl.BlockRegistryObject;
import moremultiblock.MoreMultiblock;
import moremultiblock.common.block.BlockRadioactiveWasteVault;
import moremultiblock.common.tile.multiblock.TileEntityRadioactiveWasteValve;
import moremultiblock.common.tile.multiblock.TileEntityRadioactiveWasteVault;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.MapColor;

import java.util.function.Supplier;

public class MMBBlocks {

    private MMBBlocks() {
    }

    public static final BlockDeferredRegister BLOCKS = new BlockDeferredRegister(MoreMultiblock.MODID);

    public static final BlockRegistryObject<BlockBasicMultiblock<TileEntityRadioactiveWasteVault>, ItemBlockTooltip<BlockBasicMultiblock<TileEntityRadioactiveWasteVault>>> RADIOACTIVE_WASTE_VAULT = registerBlock("radioactive_waste_vault", BlockRadioactiveWasteVault::new);
    public static final BlockRegistryObject<BlockBasicMultiblock<TileEntityRadioactiveWasteValve>, ItemBlockTooltip<BlockBasicMultiblock<TileEntityRadioactiveWasteValve>>> RADIOACTIVE_WASTE_VALVE = registerBlock("radioactive_waste_valve", () -> new BlockBasicMultiblock<>(MMBBlockTypes.RADIOACTIVE_WASTE_VALVE, properties -> properties.mapColor(MapColor.COLOR_YELLOW)));

    private static <BLOCK extends Block & IHasDescription> BlockRegistryObject<BLOCK, ItemBlockTooltip<BLOCK>> registerBlock(String name, Supplier<? extends BLOCK> blockSupplier) {
        return BLOCKS.registerDefaultProperties(name, blockSupplier, ItemBlockTooltip::new);
    }
}
