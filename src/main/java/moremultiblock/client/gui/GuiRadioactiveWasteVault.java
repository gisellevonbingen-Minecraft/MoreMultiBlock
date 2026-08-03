package moremultiblock.client.gui;

import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.IChemicalTank;
import mekanism.api.text.EnumColor;
import mekanism.client.gui.GuiMekanismTile;
import mekanism.client.gui.element.GuiDownArrow;
import mekanism.client.gui.element.GuiElementHolder;
import mekanism.client.gui.element.GuiInnerScreen;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiGasGauge;
import mekanism.client.gui.element.slot.GuiSlot;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.client.gui.element.tab.GuiContainerEditModeTab;
import mekanism.common.MekanismLang;
import mekanism.common.config.MekanismConfig;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.util.UnitDisplayUtils;
import mekanism.common.util.text.TextUtils;
import moremultiblock.common.content.vault.VaultMultiblockData;
import moremultiblock.common.tile.multiblock.TileEntityRadioactiveWasteVault;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ToLongFunction;

public class GuiRadioactiveWasteVault extends GuiMekanismTile<TileEntityRadioactiveWasteVault, MekanismTileContainer<TileEntityRadioactiveWasteVault>> {

    public GuiRadioactiveWasteVault(MekanismTileContainer<TileEntityRadioactiveWasteVault> container, Inventory inv, Component title) {
        super(container, inv, title);
        imageHeight = 170; // Default : 166
        inventoryLabelY = imageHeight - 94 + 2; // Default : = imageHeight - 94;
        dynamicSlots = true;
    }

    @Override
    protected void addGuiElements() {
//        addRenderableWidget(GuiSideHolder.armorHolder(this));
        addRenderableWidget(new GuiElementHolder(this, 141, 20, 26, 56));
        super.addGuiElements();
        addRenderableWidget(new GuiSlot(SlotType.INNER_HOLDER_SLOT, this, 145, 24));
        addRenderableWidget(new GuiSlot(SlotType.INNER_HOLDER_SLOT, this, 145, 54));
        addRenderableWidget(new GuiInnerScreen(this, 49, 15, 84, 61, () -> {
            List<Component> ret = new ArrayList<>();
            VaultMultiblockData multiblock = tile.getMultiblock();
            long capacity = multiblock.getWasteTankCapacity();
            addStored(ret, multiblock.getWasteTank());
            ret.add(MekanismLang.CAPACITY.translate(""));
            ret.add(MekanismLang.GENERIC_MB.translate(TextUtils.format(capacity)));
            addDecayAmount(ret);
            return ret;
        }).spacing(2));
        addRenderableWidget(new GuiDownArrow(this, 150, 43));
        addRenderableWidget(new GuiContainerEditModeTab<>(this, tile));
        addRenderableWidget(new GuiGasGauge(() -> tile.getMultiblock().getWasteTank(), () -> tile.getMultiblock().getGasTanks(null), GaugeType.WIDE, this, 7, 20, 34, 56));
    }

    private void addStored(List<Component> ret, IChemicalTank<?, ?> tank) {
        addStored(ret, tank.getStack(), ChemicalStack::getAmount);
    }

    private <STACK> void addStored(List<Component> ret, STACK stack, ToLongFunction<STACK> amountGetter) {
        ret.add(MekanismLang.GENERIC_PRE_COLON.translate(stack));
        ret.add(MekanismLang.GENERIC_MB.translate(TextUtils.format(amountGetter.applyAsLong(stack))));
    }

    // TODO Scaleによって減少量を増やす
    private void addDecayAmount(List<Component> ret) {
        int ticks = MekanismConfig.general.radioactiveWasteBarrelProcessTicks.get();
        long decayAmount = MekanismConfig.general.radioactiveWasteBarrelDecayAmount.get();

        int volume = tile.getMultiblock().getVolume();

        if (decayAmount == 0 || ticks == 1) {
            ret.add(MekanismLang.WASTE_BARREL_DECAY_RATE.translateColored(EnumColor.INDIGO, EnumColor.YELLOW, TextUtils.format(decayAmount)));
        } else {
            ret.add(MekanismLang.WASTE_BARREL_DECAY_RATE.translateColored(EnumColor.INDIGO, EnumColor.GRAY,
                    TextUtils.format(UnitDisplayUtils.roundDecimals((decayAmount * volume) / (double) ticks, 4))));
            ret.add(MekanismLang.WASTE_BARREL_DECAY_RATE_ACTUAL.translateColored(
                    EnumColor.INDIGO, EnumColor.GRAY, TextUtils.format(decayAmount * volume),
                    EnumColor.GRAY, TextUtils.format(ticks)
            ));
        }
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        renderTitleText(guiGraphics);
        drawString(guiGraphics, playerInventoryTitle, inventoryLabelX, inventoryLabelY, titleTextColor());
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }
}