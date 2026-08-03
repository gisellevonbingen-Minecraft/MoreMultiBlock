package moremultiblock.common.data;

import moremultiblock.MoreMultiblock;
import moremultiblock.client.data.MMBBlockStateProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = MoreMultiblock.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MMBDataGenerator {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        PackOutput output = gen.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        if (event.includeClient()) {
            gen.addProvider(true, new MMBBlockStateProvider(output, existingFileHelper));
        }

        if (event.includeServer()) {
            gen.addProvider(true, new MMBRecipeProvider(output));
            gen.addProvider(true, new MMBBlockTagProvider(output, lookupProvider, existingFileHelper));
            gen.addProvider(true, MMBLootTableProvider.create(output));
        }
    }
}
