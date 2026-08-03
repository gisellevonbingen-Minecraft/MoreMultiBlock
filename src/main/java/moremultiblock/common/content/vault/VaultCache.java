package moremultiblock.common.content.vault;

import mekanism.api.NBTConstants;
import mekanism.common.lib.multiblock.MultiblockCache;
import mekanism.common.tile.interfaces.IFluidContainerManager.ContainerEditMode;
import mekanism.common.util.NBTUtils;
import net.minecraft.nbt.CompoundTag;

public class VaultCache extends MultiblockCache<VaultMultiblockData> {

    private ContainerEditMode editMode = ContainerEditMode.BOTH;

    @Override
    public void merge(MultiblockCache<VaultMultiblockData> mergeCache, RejectContents rejectContents) {
        super.merge(mergeCache, rejectContents);
        editMode = ((VaultCache) mergeCache).editMode;
    }

    @Override
    public void apply(VaultMultiblockData data) {
        super.apply(data);
        data.editMode = editMode;
    }

    @Override
    public void sync(VaultMultiblockData data) {
        super.sync(data);
        editMode = data.editMode;
    }

    @Override
    public void load(CompoundTag nbtTags) {
        super.load(nbtTags);
        NBTUtils.setEnumIfPresent(nbtTags, NBTConstants.EDIT_MODE, ContainerEditMode::byIndexStatic, mode -> editMode = mode);
    }

    @Override
    public void save(CompoundTag nbtTags) {
        super.save(nbtTags);
        NBTUtils.writeEnum(nbtTags, NBTConstants.EDIT_MODE, editMode);
    }
}