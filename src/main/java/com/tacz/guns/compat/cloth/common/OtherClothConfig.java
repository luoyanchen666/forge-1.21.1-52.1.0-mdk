package com.tacz.guns.compat.cloth.common;

import com.tacz.guns.config.PreLoadConfig;
import com.tacz.guns.config.common.OtherConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.network.chat.Component;

public class OtherClothConfig {
    public static void init(ConfigBuilder root, ConfigEntryBuilder entryBuilder) {
        ConfigCategory other = root.getOrCreateCategory(Component.translatable("config.tacz.common.other"));

        other.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.tacz.common.other.default_pack_debug"), PreLoadConfig.override.get())
                .setDefaultValue(false).setTooltip(Component.translatable("config.tacz.common.other.default_pack_debug.desc"))
                .setSaveConsumer(PreLoadConfig.override::set).build());

        other.addEntry(entryBuilder.startIntField(Component.translatable("config.tacz.common.other.target_sound_distance"), OtherConfig.TARGET_SOUND_DISTANCE.get())
                .setMin(0).setMax(Integer.MAX_VALUE).setDefaultValue(128).setTooltip(Component.translatable("config.tacz.common.other.target_sound_distance.desc"))
                .setSaveConsumer(OtherConfig.TARGET_SOUND_DISTANCE::set).build());
    }
}
