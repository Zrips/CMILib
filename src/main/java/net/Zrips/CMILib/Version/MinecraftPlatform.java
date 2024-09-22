package net.Zrips.CMILib.Version;

public enum MinecraftPlatform {

    paper(true),
    purpur(true),
    leaves(true),
    leaf(true),
    tuinity(true),
    yatopia(true),
    airplane(true),
    pufferfish(true),
    folia(true), 
    fabric(true),
    magma(true),
    
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
