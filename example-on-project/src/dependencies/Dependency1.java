package dependencies;

import java.util.Random;

public class Dependency1 implements IDependency1{
    private Long _randomNumber;

    public Dependency1() {
        _randomNumber = new Random().nextLong();
    }

    public long getRandomNumber() {
        return _randomNumber;
    }
}
