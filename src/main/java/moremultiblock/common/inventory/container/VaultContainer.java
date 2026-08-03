package moremultiblock.common.inventory.container;

import mekanism.common.inventory.container.tile.MekanismTileContainer;
import moremultiblock.common.registries.MMBContainerTypes;
import moremultiblock.common.tile.multiblock.TileEntityRadioactiveWasteVault;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class VaultContainer extends MekanismTileContainer<TileEntityRadioactiveWasteVault> {
    public VaultContainer(int id, Inventory inv, @NotNull TileEntityRadioactiveWasteVault tile) {
        super(MMBContainerTypes.RADIOACTIVE_WASTE_VAULT, id, inv, tile);
    }

    @Override
    protected int getInventoryYOffset() {
        return 88; // Default : 84
    }
}
