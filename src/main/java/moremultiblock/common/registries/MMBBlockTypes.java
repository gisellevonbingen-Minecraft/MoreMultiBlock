package moremultiblock.common.registries;

import mekanism.common.block.attribute.Attributes;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.content.blocktype.BlockTypeTile.BlockTileBuilder;
import moremultiblock.common.MMBLang;
import moremultiblock.common.tile.multiblock.TileEntityRadioactiveWasteValve;
import moremultiblock.common.tile.multiblock.TileEntityRadioactiveWasteVault;

public class MMBBlockTypes {

    private MMBBlockTypes() {
    }

    public static final BlockTypeTile<TileEntityRadioactiveWasteVault> RADIOACTIVE_WASTE_VAULT = BlockTileBuilder
            .createBlock(() -> MMBTileEntityTypes.RADIOACTIVE_WASTE_VAULT, MMBLang.DESCRIPTION_RADIOACTIVE_WASTE_VAULT)
            .withGui(() -> MMBContainerTypes.RADIOACTIVE_WASTE_VAULT, MMBLang.RADIOACTIVE_WASTE_VAULT)
            .with(Attributes.INVENTORY,Attributes.ACTIVE)
            .externalMultiblock()
            .build();

    public static final BlockTypeTile<TileEntityRadioactiveWasteValve> RADIOACTIVE_WASTE_VALVE = BlockTileBuilder
            .createBlock(() -> MMBTileEntityTypes.RADIOACTIVE_WASTE_VALVE, MMBLang.DESCRIPTION_RADIOACTIVE_WASTE_VALVE)
            .withGui(() -> MMBContainerTypes.RADIOACTIVE_WASTE_VAULT, MMBLang.RADIOACTIVE_WASTE_VAULT)
            .with(Attributes.INVENTORY, Attributes.COMPARATOR)
            .externalMultiblock()
            .withComputerSupport("radioactiveWasteValve")
            .build();
}
