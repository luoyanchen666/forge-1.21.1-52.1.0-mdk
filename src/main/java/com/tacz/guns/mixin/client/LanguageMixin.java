package com.tacz.guns.mixin.client;

import net.minecraft.client.resources.language.ClientLanguage;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ClientLanguage.class)
public class LanguageMixin {
//    @Inject(method = "getOrDefault(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;", at = @At(value = "HEAD"), cancellable = true)
//    public void getCustomLanguage(String key, String defaultValue, CallbackInfoReturnable<String> call) {
//        String code = Minecraft.getInstance().getLanguageManager().getSelected();
//        Map<String, String> languages = ClientAssetManager.INSTANCE.getLanguages(code);
//        Map<String, String> alternative = ClientAssetManager.INSTANCE.getLanguages("en_us");
//        if (languages != null && languages.containsKey(key)) {
//            call.setReturnValue(languages.get(key));
//        } else if (alternative != null && alternative.containsKey(key)) {
//            call.setReturnValue(alternative.get(key));
//        }
//    }
//
//    @Inject(method = "has(Ljava/lang/String;)Z", at = @At(value = "HEAD"), cancellable = true)
//    public void hasCustomLanguage(String key, CallbackInfoReturnable<Boolean> call) {
//        String code = Minecraft.getInstance().getLanguageManager().getSelected();
//        Map<String, String> languages = ClientAssetManager.INSTANCE.getLanguages(code);
//        Map<String, String> alternative = ClientAssetManager.INSTANCE.getLanguages("en_us");
//        if (languages != null && languages.containsKey(key)) {
//            call.setReturnValue(true);
//        } else if (alternative != null && alternative.containsKey(key)) {
//            call.setReturnValue(true);
//        }
//    }
}
