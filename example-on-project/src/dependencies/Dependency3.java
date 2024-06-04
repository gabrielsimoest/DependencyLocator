package dependencies;

import java.util.Random;

public class Dependency3 {
    private Long _randomNumber;
    
    public Dependency3() {
        _randomNumber = new Random().nextLong();
    }
    
    public long getRandomNumber() {
        return _randomNumber;
    }
}
