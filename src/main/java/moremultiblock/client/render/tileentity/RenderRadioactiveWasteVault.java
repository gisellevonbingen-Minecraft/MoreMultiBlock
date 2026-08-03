package moremultiblock.client.render.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.client.render.data.RenderData;
import mekanism.client.render.tileentity.MultiblockTileEntityRenderer;
import moremultiblock.common.content.vault.VaultMultiblockData;
import moremultiblock.common.tile.multiblock.TileEntityRadioactiveWasteVault;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

@NothingNullByDefault
public class RenderRadioactiveWasteVault extends MultiblockTileEntityRenderer<VaultMultiblockData, TileEntityRadioactiveWasteVault> {

    public RenderRadioactiveWasteVault(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void render(TileEntityRadioactiveWasteVault tile, VaultMultiblockData multiblock, float partialTick, PoseStack matrix, MultiBufferSource renderer, int light,
                          int overlayLight, ProfilerFiller profiler) {
        RenderData data = getRenderData(multiblock);
        if (data != null) {
            VertexConsumer buffer = renderer.getBuffer(Sheets.translucentCullBlockSheet());
            renderObject(data, multiblock.valves, tile.getBlockPos(), matrix, buffer, overlayLight, multiblock.prevScale);
        }
    }

    @Nullable
    private RenderData getRenderData(VaultMultiblockData multiblock) {
        if (multiblock.isEmpty()) {
            return null;
        }

        return RenderData.Builder.create(multiblock.getWasteTank().getStack()).of(multiblock).build();
    }

    @Override
    protected String getProfilerSection() {
        return "radioactiveWasteVault";
    }

    @Override
    protected boolean shouldRender(TileEntityRadioactiveWasteVault tile, VaultMultiblockData multiblock, Vec3 camera) {
        return super.shouldRender(tile, multiblock, camera) && !multiblock.isEmpty();
    }
}