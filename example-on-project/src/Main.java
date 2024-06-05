import dependencies.Dependency4;
import dependencyLocator.abstractions.IDependencyInjectionContainer;
import dependencyLocator.abstractions.IServiceLocator;
import dependencyLocator.implementations.DependencyInjectionContainerImpl;
import dependencyLocator.implementations.ServiceLocatorImpl;
import dependencyManager.DependencyInjectionManager;
import dependencyManager.IDependencyInjectionManager;

public class Main {

    public static void main(String[] args) {
        IDependencyInjectionManager dependencyInjectionManager = new DependencyInjectionManager(new DependencyInjectionContainerImpl());
        dependencyInjectionManager.AddDependences();

        printRandomNumbersWithANewInstance();
        printRandomNumbersWithANewInstance();
        printRandomNumbersWithANewInstance();
    }
    
    private static void printRandomNumbersWithANewInstance() {
        IDependencyInjectionContainer container = DependencyInjectionContainerImpl.getInstance();
        
        var dependency4 = (Dependency4)container.getManagedService(Dependency4.class);
        dependency4.printRandomNumbers();
    }
}