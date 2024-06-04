package dependencyManager;

import dependencies.*;
import dependencyLocator.abstractions.IDependencyInjectionContainer;

public class DependencyInjectionManager implements IDependencyInjectionManager {
    IDependencyInjectionContainer _dependencyInjectionContainer;
    
    public DependencyInjectionManager(IDependencyInjectionContainer dependencyInjectionContainer){
        _dependencyInjectionContainer = dependencyInjectionContainer;
    } 
    
    public void AddDependences()
    {
        _dependencyInjectionContainer.addTransient(Dependency4.class);
        
        _dependencyInjectionContainer.addTransient(IDependency1.class, Dependency1.class);
        _dependencyInjectionContainer.addScoped(Dependency2.class);
        _dependencyInjectionContainer.addSingleton(Dependency3.class);
    }
}
