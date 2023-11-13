package kor.toxicity.inventory.api.registry;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;

public interface Registry<T> {
    @Nullable T getByName(@NotNull String name);
    @NotNull Collection<@NotNull T> getAllValues();
    @NotNull Set<@NotNull String> getAllKeys();
    void register(@NotNull String name, @NotNull T t);
}
