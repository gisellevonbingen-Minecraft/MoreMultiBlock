package moremultiblock.common.tile.multiblock;

import mekanism.api.IContentsListener;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.lib.multiblock.MultiblockManager;
import mekanism.common.tile.base.SubstanceType;
import moremultiblock.MoreMultiblock;
import moremultiblock.common.content.lasercore.LaserCoreMultiblockData;
import moremultiblock.common.registries.MMBBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class TileEntityLaserCorePort extends TileEntityLaserCoreCasing {
    public TileEntityLaserCorePort(BlockPos pos, BlockState state) {
        super(MMBBlocks.LASER_CORE_PORT, pos, state);
        delaySupplier = NO_DELAY;
    }

    @NotNull
    @Override
    public IChemicalTankHolder<Gas, GasStack, IGasTank> getInitialGasTanks(IContentsListener listener) {
        return side -> getMultiblock().getGasTanks(side);
    }

    @NotNull
    @Override
    protected IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener) {
        return side -> getMultiblock().getEnergyContainers(side);
    }

    @Override
    public boolean persists(SubstanceType type) {
        if (type == SubstanceType.GAS || type == SubstanceType.ENERGY) {
            return false;
        }
        return super.persists(type);
    }
}
