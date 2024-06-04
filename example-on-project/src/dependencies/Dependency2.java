package dependencies;

import java.util.Random;

public class Dependency2 {
    private Long _randomNumber;

    public Dependency2() {
        _randomNumber = new Random().nextLong();
    }

    public long getRandomNumber() {
        return _randomNumber;
    }
}
