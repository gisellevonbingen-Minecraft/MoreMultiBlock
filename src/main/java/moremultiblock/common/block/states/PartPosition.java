package moremultiblock.common.block.states;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;

public enum PartPosition implements StringRepresentable {
    NONE("none"),
    TOP("top"),
    BOTTOM("bottom"),
    CENTER("center");

    public static final EnumProperty<PartPosition> PART_POSITION = EnumProperty.create("part_position", PartPosition.class);

    private final String name;

    PartPosition(String name) {
        this.name = name;
    }

    @Override
    public @NotNull String getSerializedName() {
        return name;
    }
}
