package net.Zrips.CMILib.Advancements;

public enum AdvancementFrameType {
    TASK("task"),
    GOAL("goal"),
    CHALLENGE("challenge");

    private String name;

    AdvancementFrameType(String name) {
        this.name = name;
    }

    public static AdvancementFrameType getFromString(String frameType) {
        try {
            for (AdvancementFrameType one : AdvancementFrameType.values()) {
                if (one.name.equalsIgnoreCase(frameType))
                    return one;
            }
        } catch (EnumConstantNotPresentException e) {
        }
        return AdvancementFrameType.TASK;
    }

    @Override
    public String toString() {
        return name;
    }
}
