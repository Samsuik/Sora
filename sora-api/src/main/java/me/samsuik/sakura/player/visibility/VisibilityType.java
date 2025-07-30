package me.samsuik.sakura.player.visibility;

import com.google.common.collect.ImmutableList;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record VisibilityType(String key, ImmutableList<VisibilityState> states) {
    public VisibilityState getDefault() {
        return this.states.getFirst();
    }

    public boolean isDefault(VisibilityState state) {
        return state == this.getDefault();
    }

    public VisibilityState cycle(VisibilityState state) {
        int index = this.states.indexOf(state);
        int next = (index + 1) % this.states.size();
        return this.states.get(next);
    }

    public static VisibilityType from(String key, boolean minimal) {
        return new VisibilityType(key, states(minimal));
    }

    private static ImmutableList<VisibilityState> states(boolean minimal) {
        ImmutableList.Builder<VisibilityState> listBuilder = ImmutableList.builder();
        listBuilder.add(VisibilityState.ON);
        if (minimal) {
            listBuilder.add(VisibilityState.MINIMAL);
        }
        listBuilder.add(VisibilityState.OFF);
        return listBuilder.build();
    }
}
