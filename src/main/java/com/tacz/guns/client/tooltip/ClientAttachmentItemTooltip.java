package com.tacz.guns.client.tooltip;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.tacz.guns.api.TimelessAPI;
import com.tacz.guns.api.item.IGun;
import com.tacz.guns.api.item.attachment.AttachmentType;
import com.tacz.guns.api.item.builder.AttachmentItemBuilder;
import com.tacz.guns.api.item.builder.GunItemBuilder;
import com.tacz.guns.client.resource.ClientAssetsManager;
import com.tacz.guns.client.resource.pojo.PackInfo;
import com.tacz.guns.inventory.tooltip.AttachmentItemTooltip;
import com.tacz.guns.resource.pojo.data.attachment.AttachmentData;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class ClientAttachmentItemTooltip implements ClientTooltipComponent {
    private static final Cache<ResourceLocation, List<ItemStack>> CACHE = CacheBuilder.newBuilder().expireAfterAccess(5, TimeUnit.SECONDS).build();
    private final ResourceLocation attachmentId;
    private final List<Component> components = Lists.newArrayList();
    private final MutableComponent tips = Component.translatable("tooltip.tacz.attachment.yaw.shift");
    private final MutableComponent support = Component.translatable("tooltip.tacz.attachment.yaw.support");
    private @Nullable MutableComponent packInfo;
    private List<ItemStack> showGuns = Lists.newArrayList();

    public ClientAttachmentItemTooltip(AttachmentItemTooltip tooltip) {
        this.attachmentId = tooltip.getAttachmentId();
        this.addText(tooltip.getType());
        this.getShowGuns();
        this.addPackInfo();
    }

    private void addPackInfo() {
        PackInfo packInfoObject = ClientAssetsManager.INSTANCE.getPackInfo(attachmentId);
        if (packInfoObject != null) {
            packInfo = Component.translatable(packInfoObject.getName()).withStyle(ChatFormatting.BLUE).withStyle(ChatFormatting.ITALIC);
        }
    }

    private static List<ItemStack> getAllAllowGuns(List<ItemStack> output, ResourceLocation attachmentId) {
        ItemStack attachment = AttachmentItemBuilder.create().setId(attachmentId).build();
        TimelessAPI.getAllCommonGunIndex().forEach(entry -> {
            ResourceLocation gunId = entry.getKey();
            ItemStack gun = GunItemBuilder.create().setId(gunId).build();
            if (!(gun.getItem() instanceof IGun iGun)) {
                return;
            }
            if (iGun.allowAttachment(gun, attachment)) {
                output.add(gun);
            }
        });
        return output;
    }

    @Override
    public int getHeight() {
        if (!Screen.hasShiftDown()) {
            return components.size() * 10 + 28;
        }
        return (showGuns.size() - 1) / 16 * 18 + 50 + components.size() * 10;
    }

    @Override
    public int getWidth(Font font) {
        int[] width = new int[]{0};
        if (packInfo != null) {
            width[0] = Math.max(width[0], font.width(packInfo) + 4);
        }
        components.forEach(c -> width[0] = Math.max(width[0], font.width(c)));
        if (!Screen.hasShiftDown()) {
            return Math.max(width[0], font.width(tips) + 4);
        } else {
            width[0] = Math.max(width[0], font.width(support) + 4);
        }
        if (showGuns.size() > 15) {
            return Math.max(width[0], 260);
        }
        return Math.max(width[0], showGuns.size() * 16 + 4);
    }

    @Override
    public void renderText(Font font, int pX, int pY, Matrix4f matrix4f, MultiBufferSource.BufferSource bufferSource) {
        int yOffset = pY;
        for (Component component : this.components) {
            font.drawInBatch(component, pX, yOffset, 0xffaa00, false, matrix4f, bufferSource, Font.DisplayMode.NORMAL, 0, 0xF000F0);
            yOffset += 10;
        }
        if (!Screen.hasShiftDown()) {
            font.drawInBatch(tips, pX, pY + 5 + this.components.size() * 10, 0x9e9e9e, false, matrix4f, bufferSource, Font.DisplayMode.NORMAL, 0, 0xF000F0);
            yOffset += 10;
        } else {
            yOffset += (showGuns.size() - 1) / 16 * 18 + 32;
        }
        // 枪包名
        if (packInfo != null) {
            font.drawInBatch(this.packInfo, pX, yOffset + 8, 0xffffff, false, matrix4f, bufferSource, Font.DisplayMode.NORMAL, 0, 0xF000F0);
        }
    }

    @Override
    public void renderImage(Font font, int mouseX, int mouseY, GuiGraphics gui) {
        if (!Screen.hasShiftDown()) {
            return;
        }
        int minY = components.size() * 10 + 3;
        int maxX = getWidth(font);
        gui.fill(mouseX, mouseY + minY, mouseX + maxX, mouseY + minY + 11, 0x8F00b0ff);
        gui.drawString(font, support, mouseX + 2, mouseY + minY + 2, 0xe3f2fd);

        for (int i = 0; i < showGuns.size(); i++) {
            ItemStack stack = showGuns.get(i);
            int x = i % 16 * 16 + 2;
            int y = i / 16 * 18 + minY + 15;
            gui.renderItem(stack, mouseX + x, mouseY + y);
        }
    }

    private void getShowGuns() {
        try {
            this.showGuns = CACHE.get(attachmentId, () -> getAllAllowGuns(Lists.newArrayList(), attachmentId));
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void addText(AttachmentType type) {
        TimelessAPI.getClientAttachmentIndex(attachmentId).ifPresent(index -> {
            AttachmentData data = index.getData();

            @Nullable String tooltipKey = index.getTooltipKey();
            if (tooltipKey != null) {
                String text = I18n.get(tooltipKey);
                String[] split = text.split("\n");
                Arrays.stream(split).forEach(s -> components.add(Component.literal(s).withStyle(ChatFormatting.GRAY)));
            }

            if (type == AttachmentType.SCOPE) {
                float[] zoom = index.getZoom();
                if (zoom != null) {
                    String[] zoomText = new String[zoom.length];
                    for (int i = 0; i < zoom.length; i++) {
                        zoomText[i] = "x" + zoom[i];
                    }
                    String zoomJoinText = StringUtils.join(zoomText, ", ");
                    components.add(Component.translatable("tooltip.tacz.attachment.zoom", zoomJoinText).withStyle(ChatFormatting.GOLD));
                }
            }

            if (type == AttachmentType.EXTENDED_MAG) {
                int magLevel = data.getExtendedMagLevel();
                if (magLevel == 1) {
                    components.add(Component.translatable("tooltip.tacz.attachment.extended_mag_level_1").withStyle(ChatFormatting.GRAY));
                } else if (magLevel == 2) {
                    components.add(Component.translatable("tooltip.tacz.attachment.extended_mag_level_2").withStyle(ChatFormatting.BLUE));
                } else if (magLevel == 3) {
                    components.add(Component.translatable("tooltip.tacz.attachment.extended_mag_level_3").withStyle(ChatFormatting.LIGHT_PURPLE));
                }
            }

            data.getModifier().forEach((key, value) -> {
                List<Component> result = value.getComponents();
                components.addAll(result);
            });
        });
    }
}
