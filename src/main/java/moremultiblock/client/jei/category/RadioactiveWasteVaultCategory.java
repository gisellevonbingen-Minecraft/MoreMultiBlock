package moremultiblock.client.jei.category;

import giselle.jei_mekanism_multiblocks.client.gui.IntSliderWidget;
import giselle.jei_mekanism_multiblocks.client.gui.IntSliderWithButtons;
import giselle.jei_mekanism_multiblocks.client.jei.MultiblockCategory;
import giselle.jei_mekanism_multiblocks.client.jei.MultiblockWidget;
import giselle.jei_mekanism_multiblocks.client.jei.ResultWidget;
import giselle.jei_mekanism_multiblocks.client.jei.category.ICostConsumer;
import mekanism.common.MekanismLang;
import mekanism.common.config.MekanismConfig;
import mekanism.common.util.UnitDisplayUtils;
import mekanism.common.util.text.TextUtils;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import moremultiblock.MoreMultiblock;
import moremultiblock.common.MMBLang;
import moremultiblock.common.registries.MMBBlocks;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;

public class RadioactiveWasteVaultCategory extends MultiblockCategory<RadioactiveWasteVaultCategory.RadioactiveWasteVaultWidget> {
    public static final RecipeType<RadioactiveWasteVaultCategory.RadioactiveWasteVaultWidget> RECIPE_TYPE = MultiblockCategory.createRecipeType(MoreMultiblock.rl("radioactive_waste_vault"), RadioactiveWasteVaultWidget.class);
    public RadioactiveWasteVaultCategory(IGuiHelper helper) {
        super(helper, RECIPE_TYPE, MMBLang.RADIOACTIVE_WASTE_VAULT.translate(), MMBBlocks.RADIOACTIVE_WASTE_VAULT.getItemStack());
    }

    @Override
    protected void getRecipeCatalystItemStacks(Consumer<ItemStack> consumer) {
        super.getRecipeCatalystItemStacks(consumer);
        consumer.accept(MMBBlocks.RADIOACTIVE_WASTE_VAULT.getItemStack());
        consumer.accept(MMBBlocks.RADIOACTIVE_WASTE_VALVE.getItemStack());
    }

    public static class RadioactiveWasteVaultWidget extends MultiblockWidget {
        protected IntSliderWithButtons valvesWidget;

        public RadioactiveWasteVaultWidget() {

        }

        @Override
        protected void collectOtherConfigs(Consumer<AbstractWidget> consumer) {
            consumer.accept(this.valvesWidget = new IntSliderWithButtons(0, 0, 0, 0, "text.jei_mekanism_multiblocks.specs.valves", 0, 2, 0));
            this.valvesWidget.getSlider().addValueChangeHanlder(this::onValvesChanged);

            this.updateValvesSliderLimit();
        }

        @Override
        public void load(CompoundTag tag) {
            super.load(tag);

            this.setValveCount(tag.getInt("ValveCount"));
        }

        @Override
        public void save(CompoundTag tag) {
            super.save(tag);

            tag.putInt("ValveCount", this.getValveCount());
        }

        @Override
        protected void onDimensionChanged() {
            super.onDimensionChanged();

            this.updateValvesSliderLimit();
        }

        public void updateValvesSliderLimit() {
            IntSliderWidget valvesSlider = this.valvesWidget.getSlider();
            int valves = valvesSlider.getValue();
            valvesSlider.setMaxValue(this.getSideBlocks());
            valvesSlider.setValue(valves);
        }

        protected void onValvesChanged(int valves) {
            this.markNeedUpdate();
        }

        @Override
        protected void collectCost(ICostConsumer consumer) {
            super.collectCost(consumer);

            int edges = this.getEdgeBlocks();
            int sides = this.getSideBlocks();
            int valves = this.getValveCount();
            sides -= valves;

            int tanks = 0;

            tanks = edges + sides;


            consumer.accept(new ItemStack(MMBBlocks.RADIOACTIVE_WASTE_VAULT, tanks));
            consumer.accept(new ItemStack(MMBBlocks.RADIOACTIVE_WASTE_VALVE, valves));
        }

        @Override
        protected void collectResult(Consumer<AbstractWidget> consumer) {
            super.collectResult(consumer);

            int volume = this.getDimensionVolume();
            int ticks = MekanismConfig.general.radioactiveWasteBarrelProcessTicks.get();
            long decayAmount = MekanismConfig.general.radioactiveWasteBarrelDecayAmount.get();

            double rate = UnitDisplayUtils.roundDecimals((decayAmount * volume) / (double) ticks, 4);
            Component decayRateValue = Component.literal(TextUtils.format(rate) + " mB/t");
            Component decayRateActual = Component.literal(TextUtils.format(decayAmount * volume) + "  mB / " + TextUtils.format(ticks) + " ticks");

            long wasteTankCapacity = volume * MekanismConfig.general.radioactiveWasteBarrelMaxGas.get();
            consumer.accept(new ResultWidget(Component.translatable("text.jei_mekanism_multiblocks.result.chemical_tank"), MekanismLang.GENERIC_MB.translate(TextUtils.format(wasteTankCapacity))));

            ResultWidget wasteBarrelDecayRate = new ResultWidget(Component.translatable("text.moremultiblock.result.radioactive_waste_vault.decay_rate"), decayRateValue);
            consumer.accept(wasteBarrelDecayRate);

            ResultWidget wasteBarrelDecayRateActual = new ResultWidget(Component.translatable("text.moremultiblock.result.radioactive_waste_vault.decay_rate.actual"), decayRateActual);
            consumer.accept(wasteBarrelDecayRateActual);
        }

        public int getValveCount() {
            return this.valvesWidget.getSlider().getValue();
        }

        public void setValveCount(int valveCount) {
            this.valvesWidget.getSlider().setValue(valveCount);
        }


        @Override
        public int getDimensionWidthMin() {
            return 3;
        }

        @Override
        public int getDimensionWidthMax() {
            return 18;
        }

        @Override
        public int getDimensionLengthMin() {
            return 3;
        }

        @Override
        public int getDimensionLengthMax() {
            return 18;
        }

        @Override
        public int getDimensionHeightMin() {
            return 3;
        }

        @Override
        public int getDimensionHeightMax() {
            return 18;
        }

        @Override
        public Block getGlassBlock() {
            return null;
        }

        @Override
        public boolean isUseGlass() {
            return false;
        }
    }
}
