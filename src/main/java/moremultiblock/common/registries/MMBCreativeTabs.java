package moremultiblock.common.registries;

import moremultiblock.MoreMultiblock;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class MMBCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MoreMultiblock.MODID);

    public static final RegistryObject<CreativeModeTab> TAB_MORE_MULTI_BLOCK = CREATIVE_TABS.register("tab_tab_more_multi_block", () ->
            CreativeModeTab.builder()
                    .title(Component.translatable("More MultiBlock"))
                    .icon(() -> new ItemStack((MMBBlocks.RADIOACTIVE_WASTE_VAULT.asItem())))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(MMBBlocks.RADIOACTIVE_WASTE_VAULT);
                        output.accept(MMBBlocks.RADIOACTIVE_WASTE_VALVE);
                    })
                    .build()
    );

    public static void register(IEventBus eventBus) {
        CREATIVE_TABS.register(eventBus);
    }
}
