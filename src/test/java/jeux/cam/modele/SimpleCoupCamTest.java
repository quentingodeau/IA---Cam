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
public class SimpleCoupCamTest {

    @Rule
    public TestRule watcher = new TestWatcher() {
        @Override
        protected void starting(Description description) {
            System.out.println("Starting test: " + description.getMethodName());
        }
    };

    @Test
    public void toStringCheck() {
        assertEquals("A1-D13", (new SimpleCoupCam(0, 0, 12, 3)).toString());
    }

    @Test
    public void fromStringCheck() {
        assertEquals(new SimpleCoupCam(0, 0, 12, 3), "A1-D13");
        assertEquals(new SimpleCoupCam(0, 0, 12, 3), "a1-d13");
    }
}
