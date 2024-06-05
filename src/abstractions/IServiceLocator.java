package dependencyLocator.abstractions;

import dependencyLocator.entities.EServiceLifetime;
import dependencyLocator.entities.ServiceDescriptor;

public interface IServiceLocator {
    void registerService(Class<?> serviceClass, EServiceLifetime lifetime);
    void registerService(Class<?> serviceInterface, Class<?> serviceClass, EServiceLifetime lifetime);
    Object getService(Class<?> serviceClass);
    ServiceDescriptor getServiceDescriptorFromMap(String key);
}
