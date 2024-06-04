package dependencyLocator.implementations;

import dependencyLocator.abstractions.IDependencyInjectionContainer;
import dependencyLocator.abstractions.IServiceLocator;
import dependencyLocator.entities.EServiceLifetime;

public class DependencyInjectionContainerImpl implements IDependencyInjectionContainer {
    private final IServiceLocator _serviceLocator;
    
    public DependencyInjectionContainerImpl() {
        _serviceLocator = ServiceLocatorImpl.getInstance();
    }

    @Override
    public void addTransient(Class<?> serviceClass) {
        _serviceLocator.registerService(serviceClass, EServiceLifetime.Transient);
    }

    @Override
    public void addTransient(Class<?> serviceInterface, Class<?> serviceClass) {
        _serviceLocator.registerService(serviceInterface, serviceClass, EServiceLifetime.Transient);
    }

    @Override
    public void addScoped(Class<?> serviceClass) {
        _serviceLocator.registerService(serviceClass, EServiceLifetime.Scoped);
    }

    @Override
    public void addScoped(Class<?> serviceInterface, Class<?> serviceClass) {
        _serviceLocator.registerService(serviceInterface, serviceClass, EServiceLifetime.Scoped);
    }

    @Override
    public void addSingleton(Class<?> serviceClass) {
        _serviceLocator.registerService(serviceClass, EServiceLifetime.Singleton);
    }

    @Override
    public void addSingleton(Class<?> serviceInterface, Class<?> serviceClass) {
        _serviceLocator.registerService(serviceInterface, serviceClass, EServiceLifetime.Singleton);
    }
}
