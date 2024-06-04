import dependencyLocator.abstractions.IDependencyInjectionContainer;
import dependencyLocator.implementations.DependencyInjectionContainer;

import java.awt.*;

public class Main {
    IDependencyInjectionContainer aa = new DependencyInjectionContainer();
    
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }
}