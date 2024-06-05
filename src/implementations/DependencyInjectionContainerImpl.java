package dependencyLocator.implementations;

import dependencyLocator.abstractions.IDependencyInjectionContainer;
import dependencyLocator.abstractions.IServiceLocator;
import dependencyLocator.entities.EServiceLifetime;
import dependencyLocator.entities.ServiceDescriptor;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class DependencyInjectionContainerImpl implements IDependencyInjectionContainer {
    private static DependencyInjectionContainerImpl instance;
    private final IServiceLocator _serviceLocator;
    private final Map<String, Object> singletonInstancesMap = new HashMap<>();
    
    public DependencyInjectionContainerImpl() {
        _serviceLocator = ServiceLocatorImpl.getInstance();
    }

    public static IDependencyInjectionContainer getInstance() {
        if (instance == null) {
            instance = new DependencyInjectionContainerImpl();
        }

        return instance;
    }

    public void addTransient(Class<?> serviceClass) {
        _serviceLocator.registerService(serviceClass, EServiceLifetime.Transient);
    }

    public void addTransient(Class<?> serviceInterface, Class<?> serviceClass) {
        _serviceLocator.registerService(serviceInterface, serviceClass, EServiceLifetime.Transient);
    }

    public void addScoped(Class<?> serviceClass) {
        _serviceLocator.registerService(serviceClass, EServiceLifetime.Scoped);
    }
    
    public void addScoped(Class<?> serviceInterface, Class<?> serviceClass) {
        _serviceLocator.registerService(serviceInterface, serviceClass, EServiceLifetime.Scoped);
    }

    public void addSingleton(Class<?> serviceClass) {
        _serviceLocator.registerService(serviceClass, EServiceLifetime.Singleton);
    }

    public void addSingleton(Class<?> serviceInterface, Class<?> serviceClass) {
        _serviceLocator.registerService(serviceInterface, serviceClass, EServiceLifetime.Singleton);
    }

    public Object getManagedService(Class<?> serviceClass, Object... optionalParameters) {
        try {
            var key = serviceClass.getSimpleName();
            var serviceDescriptor = _serviceLocator.getServiceDescriptorFromMap(key);
            
            return constructManagedService(serviceDescriptor, optionalParameters);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    private Object constructManagedService(ServiceDescriptor serviceDescriptor, Object... optionalObjects) {
        var scopedInstancesMap = new HashMap<String, Object>();
        
        if (serviceDescriptor == null) {
            var serviceClass = serviceDescriptor.getServiceClass();
            return tryGetServiceFromOptinalObjects(serviceClass.getSimpleName(), optionalObjects);
        }

        return constructServiceWithDependences(serviceDescriptor, scopedInstancesMap, optionalObjects);
    }
    
    private Object constructServiceWithDependences(ServiceDescriptor serviceDescriptor, HashMap<String, Object> scopedInstancesMap, Object... optionalObjects) {
        Constructor<?>[] constructors = serviceDescriptor.getServiceClass().getConstructors();
        for (Constructor<?> constructor : constructors) {
            var dependenciesFromParameters = getDependenciesFromParameters(constructor, scopedInstancesMap, optionalObjects);
            return getInstanceFromConstructorWithLifetime(constructor, serviceDescriptor.getServiceLifetime(), scopedInstancesMap, dependenciesFromParameters);
        }
        
        return null;
    }

    private Object[] getDependenciesFromParameters(Constructor<?> constructor, HashMap<String, Object> scopedInstancesMap, Object... optionalObjects) {
        var parameters = new Object[constructor.getParameterCount()];

        Class<?>[] parameterTypes = constructor.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            
            var parameterKey = parameterTypes[i].getSimpleName();
            var serviceDescriptor = _serviceLocator.getServiceDescriptorFromMap(parameterKey);
            var dependency = constructServiceWithDependences(serviceDescriptor, scopedInstancesMap, optionalObjects);
                    
            parameters[i] = dependency;
        }

        return parameters;
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
