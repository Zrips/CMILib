package net.Zrips.CMILib.Version;

public enum MinecraftPlatform {

    paper(true),
    purpur(true),
    tuinity(true),
    yatopia(true),
    airplane(true),
    pufferfish(true),
    folia(true),
    
    craftbukkit(false),
    spigot(false),
    tacospigot(false),
    glowstone(false),
    mohist(false),
    arclight(false);

    private boolean async;

    MinecraftPlatform(boolean async) {
        this.async = async;
    }

    public boolean isAsync() {
        return async;
    }
}
