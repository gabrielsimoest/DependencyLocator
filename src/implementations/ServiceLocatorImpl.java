package dependencyLocator.implementations;

import dependencyLocator.abstractions.IServiceLocator;
import dependencyLocator.entities.EServiceLifetime;
import dependencyLocator.entities.ServiceDescriptor;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class ServiceLocatorImpl implements IServiceLocator {
    private static ServiceLocatorImpl instance;
    private final Map<String, ServiceDescriptor> services = new HashMap<>();
    private final Map<String, Object> singletonInstances = new HashMap<>();

    public static IServiceLocator getInstance() {
        if (instance == null) {
            instance = new ServiceLocatorImpl();
        }

        return instance;
    }

    public void registerService(Class<?> serviceClass, EServiceLifetime lifetime) {
        var serviceDescriptor = new ServiceDescriptor(serviceClass, lifetime);
        services.put(serviceClass.getSimpleName(), serviceDescriptor);
    }

    public void registerService(Class<?> serviceInterface, Class<?> serviceClass, EServiceLifetime lifetime) {
        if (!serviceInterface.isInterface()) {
            throw new RuntimeException("Service interface expected.");
        }

        if (!serviceInterface.isAssignableFrom(serviceClass)) {
            throw new RuntimeException("Service class must implement the specified interface.");
        }

        var serviceDescriptor = new ServiceDescriptor(serviceClass, lifetime);
        services.put(serviceInterface.getSimpleName(), serviceDescriptor);
    }

    public Object getService(Class<?> serviceClass, Object... optionalParameters) {
        return getService(serviceClass.getSimpleName(), optionalParameters);
    }

    private Object getService(String key, Object... optionalObjects) {
        var serviceDescriptor = services.get(key);

        if (serviceDescriptor == null) {
            for (Object obj : optionalObjects) {
                if (obj == null)
                    return null;

                if (obj.getClass().getSimpleName().equals(key)) {
                    return obj;
                }
            }

            throw new RuntimeException("No registered service for " + key);
        }

        try {
            Constructor<?>[] constructors = serviceDescriptor.getServiceClass().getConstructors();
            for (Constructor<?> constructor : constructors) {

                if (constructor.getParameterCount() == 0) {
                    return getInstanceFromConstructorWithLifetime(constructor, serviceDescriptor.getServiceLifetime());
                } else {

                    Object[] parameters = new Object[constructor.getParameterCount()];
                    Class<?>[] parameterTypes = constructor.getParameterTypes();

                    for (int i = 0; i < parameterTypes.length; i++) {

                        String parameterKey = parameterTypes[i].getSimpleName();
                        Object dependency = getService(parameterKey, optionalObjects);


                        parameters[i] = dependency;
                    }

                    return getInstanceFromConstructorWithLifetime(constructor, serviceDescriptor.getServiceLifetime(), parameters);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    private Object getInstanceFromConstructorWithLifetime(Constructor<?> constructor, EServiceLifetime lifetime, Object... parameters) {
        try {
            Object serviceInstance = null;

            switch (lifetime) {
                case Transient:
                    return constructor.newInstance(parameters);
                case Scoped: //Não implementado
                    return constructor.newInstance(parameters);
                case Singleton:
                    String singletonKey = constructor.getDeclaringClass().getSimpleName();
                    if (!singletonInstances.containsKey(singletonKey)) {
                        singletonInstances.put(singletonKey, constructor.newInstance(parameters));
                    }

                    return singletonInstances.get(singletonKey);
            }

            return serviceInstance;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
