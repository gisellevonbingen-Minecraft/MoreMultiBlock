package moremultiblock.client;

import mekanism.client.ClientRegistrationUtil;
import moremultiblock.MoreMultiblock;
import moremultiblock.client.gui.GuiRadioactiveWasteVault;
import moremultiblock.client.render.tileentity.RenderRadioactiveWasteVault;
import moremultiblock.common.registries.MMBContainerTypes;
import moremultiblock.common.registries.MMBTileEntityTypes;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(modid = MoreMultiblock.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientRegistration {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        ClientRegistrationUtil.bindTileEntityRenderer(event, RenderRadioactiveWasteVault::new, MMBTileEntityTypes.RADIOACTIVE_WASTE_VAULT, MMBTileEntityTypes.RADIOACTIVE_WASTE_VALVE);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void registerContainers(RegisterEvent event) {
        event.register(Registries.MENU, menuTypeRegisterHelper ->
                ClientRegistrationUtil.registerScreen(MMBContainerTypes.RADIOACTIVE_WASTE_VAULT, GuiRadioactiveWasteVault::new)
        );
    }
}
