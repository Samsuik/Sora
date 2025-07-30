package me.samsuik.sakura.redstone;

import org.jspecify.annotations.NullMarked;

@NullMarked
public enum RedstoneImplementation {
    VANILLA("vanilla"),
    EIGENCRAFT("eigencraft"),
    ALTERNATE_CURRENT("alternate-current");

    private final String friendlyName;

    RedstoneImplementation(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public String getFriendlyName() {
        return this.friendlyName;
    }
}
