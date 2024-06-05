package dependencyLocator.implementations;

import dependencyLocator.abstractions.IServiceLocator;
import dependencyLocator.entities.EServiceLifetime;
import dependencyLocator.entities.ServiceDescriptor;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class ServiceLocatorImpl implements IServiceLocator {
    private static ServiceLocatorImpl instance;
    private final Map<String, ServiceDescriptor> servicesMap = new HashMap<>();

    public static IServiceLocator getInstance() {
        if (instance == null) {
            instance = new ServiceLocatorImpl();
        }

        return instance;
    }

    public void registerService(Class<?> serviceClass, EServiceLifetime lifetime) {
        var serviceDescriptor = new ServiceDescriptor(serviceClass, lifetime);
        servicesMap.put(serviceClass.getSimpleName(), serviceDescriptor);
    }

    public void registerService(Class<?> serviceInterface, Class<?> serviceClass, EServiceLifetime lifetime) {
        if (!serviceInterface.isInterface()) {
            throw new RuntimeException("Service interface expected.");
        }

        if (!serviceInterface.isAssignableFrom(serviceClass)) {
            throw new RuntimeException("Service class must implement the specified interface.");
        }

        var serviceDescriptor = new ServiceDescriptor(serviceClass, lifetime);
        servicesMap.put(serviceInterface.getSimpleName(), serviceDescriptor);
    }

    public Object getService(Class<?> serviceClass) {
        try {
            var key = serviceClass.getSimpleName();
            var serviceDescriptor = getServiceDescriptorFromMap(key);

            var constructor = serviceDescriptor.getServiceClass().getConstructor();

            return constructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ServiceDescriptor getServiceDescriptorFromMap(String key) {
        return servicesMap.get(key);
    }
}
