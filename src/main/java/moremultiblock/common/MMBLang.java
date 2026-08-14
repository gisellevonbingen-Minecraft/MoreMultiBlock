package moremultiblock.common;

import mekanism.api.text.ILangEntry;
import moremultiblock.MoreMultiblock;
import net.minecraft.Util;

public enum MMBLang implements ILangEntry {
    RADIOACTIVE_WASTE_VAULT("gui", "radioactive_waste_vault"),
    DESCRIPTION_RADIOACTIVE_WASTE_VAULT("description", "radioactive_waste_vault"),
    DESCRIPTION_RADIOACTIVE_WASTE_VALVE("description", "radioactive_waste_valve"),

    LASER_CORE("laser_core","laser_core"),
    DESCRIPTION_LASER_CORE_CASING("description","laser_core_casing"),
    DESCRIPTION_LASER_CORE_PORT("description","laser_core_port"),
    DESCRIPTION_LASER_CORE("description","laser_core")
    ;


    private final String key;

    MMBLang(String type, String path) {
        this(Util.makeDescriptionId(type, MoreMultiblock.rl(path)));
    }

    MMBLang(String key) {
        this.key = key;
    }

    @Override
    public String getTranslationKey() {
        return key;
    }
}
