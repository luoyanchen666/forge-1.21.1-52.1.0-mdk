package com.tacz.guns.compat.oculus.legacy.pbr;

import com.tacz.guns.client.resource_legacy.texture.FilePackTexture;
import com.tacz.guns.client.resource_legacy.texture.ZipPackTexture;
import net.coderbot.iris.texture.pbr.loader.PBRTextureLoaderRegistry;

public class PBRRegister {
    public static void registerPBRLoader() {
        PBRTextureLoaderRegistry.INSTANCE.register(FilePackTexture.class, new FilePackTexturePBRLoader());
        PBRTextureLoaderRegistry.INSTANCE.register(ZipPackTexture.class, new ZipPackTexturePBRLoader());
    }
}
