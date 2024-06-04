package dependencies;

public class Dependency4 {
    private final IDependency1 _dependency1;
    private final IDependency1 _dependency1_2;
    private final Dependency2 _dependency2;
    private final Dependency2 _dependency2_2;
    private final Dependency3 _dependency3;
    private final Dependency3 _dependency3_2;
    
    public Dependency4(
            IDependency1 dependency1,
            IDependency1 dependency1_2,
            
            Dependency2 dependency2,
            Dependency2 dependency2_2,
            
            Dependency3 dependency3,
            Dependency3 dependency3_2
    ) {
        _dependency1 = dependency1;
        _dependency1_2 = dependency1_2;
        
        _dependency2 = dependency2;
        _dependency2_2 = dependency2_2;
        
        _dependency3 = dependency3;
        _dependency3_2 = dependency3_2;
    }
    
    public void printRandomNumbers(){
        System.out.println();
        System.out.println("Dependencia1: " + _dependency1.getRandomNumber());
        System.out.println("Dependencia1_2: " + _dependency1_2.getRandomNumber());
        System.out.println("Dependencia2: " + _dependency2.getRandomNumber());
        System.out.println("Dependencia2_2: " + _dependency2_2.getRandomNumber());
        System.out.println("Dependencia3: " + _dependency3.getRandomNumber());
        System.out.println("Dependencia3_2: " + _dependency3_2.getRandomNumber());
    }
}
