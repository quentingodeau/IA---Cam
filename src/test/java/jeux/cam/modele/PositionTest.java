package jeux.cam.modele;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import static org.junit.Assert.*;

/**
 *
 * @author godeau & pannirselvame
 */
public class PositionTest {
    @Rule
    public TestRule watcher = new TestWatcher() {
        @Override
        protected void starting(Description description) {
            System.out.println("Starting test: " + description.getMethodName());
        }
    };

    @Test
    public void toStringCheck() {
        assertEquals("A1", (new Position(0, 0)).toString());
        assertEquals("A2", (new Position(1, 0)).toString());
        assertEquals("A11", (new Position(10, 0)).toString());
        assertEquals("C1", (new Position(0, 2)).toString());
        assertEquals("F1", (new Position(0, 5)).toString());
    }

    @Test
    public void fromStringCheck() {
        assertEquals(new Position(0, 0), new Position("A1"));
        assertEquals(new Position(0, 0), new Position("a1"));
        assertEquals(new Position(10, 0), new Position("A11"));
        assertEquals(new Position(0, 2), new Position("c1"));
        assertEquals(new Position(0, 5), new Position("F1"));
    }
}
