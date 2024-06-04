package dependencyLocator.abstractions;

import dependencyLocator.entities.EServiceLifetime;

public interface IServiceLocator {
    void registerService(Class<?> serviceClass, EServiceLifetime lifetime);
    void registerService(Class<?> serviceInterface, Class<?> serviceClass, EServiceLifetime lifetime);
    Object getService(Class<?> serviceClass, Object... optionalObjects);
}
