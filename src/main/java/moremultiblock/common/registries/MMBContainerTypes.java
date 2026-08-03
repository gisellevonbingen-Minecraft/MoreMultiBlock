package moremultiblock.common.registries;

import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.registration.impl.ContainerTypeDeferredRegister;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;
import moremultiblock.MoreMultiblock;
import moremultiblock.common.inventory.container.VaultContainer;
import moremultiblock.common.tile.multiblock.TileEntityRadioactiveWasteVault;

public class MMBContainerTypes {
    private MMBContainerTypes() {
    }

    public static final ContainerTypeDeferredRegister CONTAINER_TYPES = new ContainerTypeDeferredRegister(MoreMultiblock.MODID);

    // TODO armorSideBarを設定できるカスタムレジスターの作成
    public static final ContainerTypeRegistryObject<MekanismTileContainer<TileEntityRadioactiveWasteVault>> RADIOACTIVE_WASTE_VAULT = CONTAINER_TYPES.register(MMBBlocks.RADIOACTIVE_WASTE_VAULT, TileEntityRadioactiveWasteVault.class, VaultContainer::new);
//            .armorSideBar().build();
}
