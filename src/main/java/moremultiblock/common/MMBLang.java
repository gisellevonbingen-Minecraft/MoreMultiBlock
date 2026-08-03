package moremultiblock.common;

import mekanism.api.text.ILangEntry;
import moremultiblock.MoreMultiblock;
import net.minecraft.Util;

public enum MMBLang implements ILangEntry {
    RADIOACTIVE_WASTE_VAULT("gui", "radioactive_waste_vault"),
    DESCRIPTION_RADIOACTIVE_WASTE_VAULT("description", "radioactive_waste_vault"),

    RADIOACTIVE_WASTE_VALVE("gui", "radioactive_waste_valve"),
    DESCRIPTION_RADIOACTIVE_WASTE_VALVE("description", "radioactive_waste_valve");

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
