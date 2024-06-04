package dependencyLocator.entities;

public class ServiceDescriptor {
    private final Class<?> serviceClass;
    private final EServiceLifetime serviceLifetime;
    
    public ServiceDescriptor(Class<?> serviceClass, EServiceLifetime serviceLifetime) {
        this.serviceClass = serviceClass;
        this.serviceLifetime = serviceLifetime;
    }
    
    public Class<?> getServiceClass() {
        return serviceClass;
    }
    
    public EServiceLifetime getServiceLifetime() {
        return serviceLifetime;
    }
}
