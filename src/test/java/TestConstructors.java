import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;

public class TestConstructors extends Assert {
    private final ThreadLocalRandom rand = ThreadLocalRandom.current();

    @Test
    public void testCreatingBank() {
        String name = ((Integer)rand.nextInt()).toString();
        Bank bank = new Bank(name);
        assertEquals(0, bank.getName().compareTo(name));
        assertEquals(0, bank.getNumOfUsers());
        assertEquals(0, bank.getNumOfAccounts());

    }

}
