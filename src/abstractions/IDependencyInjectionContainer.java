package dependencyLocator.abstractions;

public interface IDependencyInjectionContainer {
    void addTransient(Class<?> serviceClass);
    void addTransient(Class<?> serviceInterface, Class<?> serviceClass);

    void addScoped(Class<?> serviceClass);
    void addScoped(Class<?> serviceInterface, Class<?> serviceClass);

    void addSingleton(Class<?> serviceClass);
    void addSingleton(Class<?> serviceInterface, Class<?> serviceClass);
}
