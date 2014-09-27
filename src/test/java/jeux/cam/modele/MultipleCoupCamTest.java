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
public class MultipleCoupCamTest {

    @Rule
    public TestRule watcher = new TestWatcher() {
        @Override
        protected void starting(Description description) {
            System.out.println("Starting test: " + description.getMethodName());
        }
    };

    @Test
    public void toStringCheck() {
        MultipleCoupCam coupCam = new MultipleCoupCam(0, 0);
        coupCam.add(new Position (12,3));
        coupCam.add(new Position (10,1));
        coupCam.add(new Position (8,3));
        coupCam.add(new Position (6,5));
        
        assertEquals("A1-D13-B11-D9-F7", coupCam.toString());
        assertNotEquals("A1-D13-B11-D9-F8", coupCam.toString());
    }

    @Test
    public void fromStringCheck() {
        
        MultipleCoupCam coupCam = new MultipleCoupCam(0, 0);
        coupCam.add(new Position (12,3));
        coupCam.add(new Position (10,1));
        coupCam.add(new Position (8,3));
        coupCam.add(new Position (6,5));
        
        assertEquals(coupCam, new MultipleCoupCam("A1-D13-B11-D9-F7"));
    }
}
