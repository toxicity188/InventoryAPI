package kor.toxicity.inventory.api.registry;

import org.jetbrains.annotations.NotNull;

public interface RegistrySupplier<T>  {
    @NotNull Registry<T> getRegistry();
}
