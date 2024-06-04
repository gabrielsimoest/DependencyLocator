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
    private final Map<String, Object> singletonInstancesMap = new HashMap<>();

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

    public Object getService(Class<?> serviceClass, Object... optionalParameters) {
        try {
            var scopedInstancesMap = new HashMap<String, Object>();
            return getService(serviceClass.getSimpleName(), scopedInstancesMap, optionalParameters);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object getService(String key, HashMap<String, Object> scopedInstancesMap, Object... optionalObjects) {
        var serviceDescriptor = servicesMap.get(key);
        if (serviceDescriptor == null)
            return tryGetServiceFromOptinalObjects(key, optionalObjects);

        Constructor<?>[] constructors = serviceDescriptor.getServiceClass().getConstructors();
        for (Constructor<?> constructor : constructors) {
            var dependenciesFromParameters = getDependenciesFromParameters(constructor, scopedInstancesMap, optionalObjects);
            return getInstanceFromConstructorWithLifetime(constructor, serviceDescriptor.getServiceLifetime(), scopedInstancesMap, dependenciesFromParameters);
        }

        return null;
    }

    private Object tryGetServiceFromOptinalObjects(String key, Object... optionalObjects) {
        for (Object obj : optionalObjects) {
            if (obj == null)
                return null;

            if (obj.getClass().getSimpleName().equals(key)) {
                return obj;
            }
        }

        throw new RuntimeException("No registered service for " + key);
    }

    private Object[] getDependenciesFromParameters(Constructor<?> constructor, HashMap<String, Object> scopedInstancesMap, Object... optionalObjects) {
        var parameters = new Object[constructor.getParameterCount()];

        Class<?>[] parameterTypes = constructor.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            var parameterKey = parameterTypes[i].getSimpleName();
            var dependency = getService(parameterKey, scopedInstancesMap, optionalObjects);

            parameters[i] = dependency;
        }

        return parameters;
    }

    private Object getInstanceFromConstructorWithLifetime(Constructor<?> constructor, EServiceLifetime lifetime, HashMap<String, Object> scopedInstancesMap, Object... parameters) {
        try {
            switch (lifetime) {
                case Transient:
                    return constructor.newInstance(parameters);

                case Scoped:
                    String scopedKey = constructor.getDeclaringClass().getSimpleName();
                    if (!scopedInstancesMap.containsKey(scopedKey))
                        scopedInstancesMap.put(scopedKey, constructor.newInstance(parameters));

                    return scopedInstancesMap.get(scopedKey);

                case Singleton:
                    String singletonKey = constructor.getDeclaringClass().getSimpleName();
                    if (!singletonInstancesMap.containsKey(singletonKey))
                        singletonInstancesMap.put(singletonKey, constructor.newInstance(parameters));

                    return singletonInstancesMap.get(singletonKey);
            }

            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
