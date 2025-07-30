package me.samsuik.sakura.player.visibility;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface VisibilitySettings {
    default boolean isEnabled(VisibilityType type) {
        return this.get(type) == VisibilityState.ON;
    }

    default boolean isDisabled(VisibilityType type) {
        return this.get(type) == VisibilityState.OFF;
    }

    default boolean isToggled(VisibilityType type) {
        return !type.isDefault(this.get(type));
    }

    default VisibilityState toggle(VisibilityType type) {
        VisibilityState state = this.get(type);
        return this.set(type, toggleState(state));
    }

    default VisibilityState cycle(VisibilityType type) {
        VisibilityState state = this.get(type);
        return this.set(type, type.cycle(state));
    }

    default void toggleAll() {
        VisibilityState state = this.currentState();
        VisibilityState newState = toggleState(state);
        for (VisibilityType type : VisibilityTypes.types()) {
            this.set(type, newState);
        }
    }

    VisibilityState get(VisibilityType type);

    VisibilityState set(VisibilityType type, VisibilityState state);

    VisibilityState currentState();

    boolean playerModified();

    static VisibilityState toggleState(VisibilityState state) {
        return state != VisibilityState.OFF ? VisibilityState.OFF : VisibilityState.ON;
    }
}
