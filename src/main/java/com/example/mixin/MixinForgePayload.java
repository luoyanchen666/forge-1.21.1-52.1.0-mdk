package com.example.mixin;

import com.tac.total_armor_core.TAC;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.ForgePayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ForgePayload.class)
public class MixinForgePayload {

    @Overwrite
    public ResourceLocation id() {
        return ((ForgePayload)(Object)this).id();
    }

    @Overwrite
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(((ForgePayload)(Object)this).id());
    }
}
