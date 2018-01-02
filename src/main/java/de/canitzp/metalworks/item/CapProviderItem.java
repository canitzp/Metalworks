package de.canitzp.metalworks.item;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * @author canitzp
 */
public class CapProviderItem implements ICapabilityProvider{

    private Map<Capability<?>, Object> providedCapabilities = new HashMap<>();

    @SafeVarargs
    public CapProviderItem(Pair<Capability<?>, ?>... caps){
        Arrays.stream(caps).forEach(cap -> providedCapabilities.put(cap.getKey(), cap.getValue()));
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return providedCapabilities.containsKey(capability);
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return providedCapabilities.containsKey(capability) ? (T) providedCapabilities.get(capability) : null;
    }

}
