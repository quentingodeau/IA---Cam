package jeux.cam;

import iia.jeux.modele.CoupJeu;
import iia.jeux.modele.joueur.Joueur;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import jeux.cam.modele.MultipleCoupCam;
import jeux.cam.modele.SimpleCoupCam;
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
public class PlateauCamTest {

    Comparator<CoupJeu> comparatorCoup = new Comparator<CoupJeu>() {
        @Override
        public int compare(CoupJeu o1, CoupJeu o2) {
            return o1.toString().compareTo(o2.toString());
        }
    };

    @Rule
    public TestRule watcher = new TestWatcher() {
        @Override
        protected void starting(Description description) {
            System.out.println("Starting test: " + description.getMethodName());
        }
    };

    @Test
    public void coupsPossiblesCheck() {
        Joueur noir = new Joueur("1");
        Joueur blanc = new Joueur("2");

        /* Test des coups possibles lorsqu'il y a la possibilité de manger */
        PlateauCam plateauCam = new PlateauCam(noir, blanc);
        plateauCam.setFromFile("data/return.txt");

        List<CoupJeu> coupCamAtt = new LinkedList<>();
        List<CoupJeu> coupCamObt = plateauCam.coupsPossibles(noir);

        coupCamAtt.add(new MultipleCoupCam("D7-F5"));
        coupCamAtt.add(new MultipleCoupCam("D7-B5"));
        coupCamAtt.add(new MultipleCoupCam("D7-F7-D5-B7-D7"));
        coupCamAtt.add(new MultipleCoupCam("D7-B7-D5-F7-D7"));

        Collections.sort(coupCamAtt, comparatorCoup);
        Collections.sort(coupCamObt, comparatorCoup);

        assertEquals(coupCamAtt, coupCamObt);

        /* Test  des coups possibles lorsque le joueur doit entré dans son chateau */
        plateauCam.setFromFile("data/chateau1.txt");

        coupCamAtt = new LinkedList<>();
        coupCamObt = plateauCam.coupsPossibles(noir);

        coupCamAtt.add(new SimpleCoupCam("B3-B4"));
        coupCamAtt.add(new SimpleCoupCam("B3-C4"));
        coupCamAtt.add(new SimpleCoupCam("B3-A4"));

        Collections.sort(coupCamAtt, comparatorCoup);
        Collections.sort(coupCamObt, comparatorCoup);

        assertEquals(coupCamAtt, coupCamObt);

        /* Test  des coups possibles lorsque le joueur doit entré dans le chateau adverse */
        plateauCam.setFromFile("data/chateau2.txt");

        coupCamAtt = new LinkedList<>();
        coupCamObt = plateauCam.coupsPossibles(noir);

        coupCamAtt.add(new MultipleCoupCam("B11-D13-F11"));

        assertEquals(coupCamAtt, coupCamObt);

        /* Test de canter */
        plateauCam.setFromFile("data/canter.txt");

        coupCamAtt = new LinkedList<>();
        coupCamObt = plateauCam.coupsPossibles(blanc);

        coupCamAtt.add(new SimpleCoupCam("D8-D9"));
        coupCamAtt.add(new SimpleCoupCam("D8-E8"));
        coupCamAtt.add(new SimpleCoupCam("D8-D6"));
        coupCamAtt.add(new SimpleCoupCam("D8-F6"));
        coupCamAtt.add(new SimpleCoupCam("D8-C7"));
        coupCamAtt.add(new SimpleCoupCam("D8-C9"));
        coupCamAtt.add(new SimpleCoupCam("D8-C8"));
        coupCamAtt.add(new SimpleCoupCam("D8-E9"));

        coupCamAtt.add(new SimpleCoupCam("E7-E6"));
        coupCamAtt.add(new SimpleCoupCam("E7-D6"));
        coupCamAtt.add(new SimpleCoupCam("E7-F6"));

        coupCamAtt.add(new SimpleCoupCam("D7-C6"));
        coupCamAtt.add(new SimpleCoupCam("D7-D6"));
        coupCamAtt.add(new SimpleCoupCam("D7-E6"));

        coupCamAtt.add(new SimpleCoupCam("D5-C4"));
        coupCamAtt.add(new SimpleCoupCam("D5-E4"));
        coupCamAtt.add(new SimpleCoupCam("D5-D4"));

        Collections.sort(coupCamAtt, comparatorCoup);
        Collections.sort(coupCamObt, comparatorCoup);

        assertEquals(coupCamAtt, coupCamObt);

        /* Test lorsque l'on va gagner */
        plateauCam.setFromFile("data/win.txt");
        coupCamAtt = new LinkedList<>();
        coupCamObt = plateauCam.coupsPossibles(noir);
        
        coupCamAtt.add(new SimpleCoupCam("C12-D13"));
        
        assertEquals(coupCamAtt, coupCamObt);
    }

    @Test
    public void playCheck() {
        Joueur blanc = new Joueur("1");
        Joueur noir = new Joueur("2");
        String expectedPlateau
                = "%  A B C D E F G\n"
                + "01       *       01\n"
                + "02     - - -     02\n"
                + "03   - - - - -   03\n"
                + "04 - - - - - - - 04\n"
                + "05 - - - - - - - 05\n"
                + "06 - - - - - - - 06\n"
                + "07 - - - n - - - 07\n"
                + "08 - - - - - - - 08\n"
                + "09 - - - - - - - 09\n"
                + "10 - - - - - - - 10\n"
                + "11   - - - - -   11\n"
                + "12     - - -     12\n"
                + "13       *       13\n"
                + "%  A B C D E F G";

        PlateauCam plateauCam = new PlateauCam(noir, blanc);
        plateauCam.setFromFile("data/return.txt");
        plateauCam.play("D7-B7-D5-F7-D7", "noir");

        assertEquals(1, plateauCam.getNbPieceNoir());
        assertEquals(0, plateauCam.getNbPieceBlanc());
        assertEquals(expectedPlateau, plateauCam.toString());

        // test avec chateau
        expectedPlateau
                = "%  A B C D E F G\n"
                + "01       *       01\n"
                + "02     - - -     02\n"
                + "03   - - - - -   03\n"
                + "04 - - - - - - - 04\n"
                + "05 - - - - - - - 05\n"
                + "06 - - - - - - - 06\n"
                + "07 - - - - - - - 07\n"
                + "08 - - - - - - - 08\n"
                + "09 - - - - - - - 09\n"
                + "10 - - - - - - - 10\n"
                + "11   - - - - n   11\n"
                + "12     - - -     12\n"
                + "13       *       13\n"
                + "%  A B C D E F G";

        plateauCam.setFromFile("data/chateau2.txt");
        plateauCam.play("B11-D13-F11", "noir");

        assertEquals(1, plateauCam.getNbPieceNoir());
        assertEquals(0, plateauCam.getNbPieceBlanc());
        assertEquals(expectedPlateau, plateauCam.toString());

        // test avec simple coup
        expectedPlateau
                = "%  A B C D E F G\n"
                + "01       *       01\n"
                + "02     - - -     02\n"
                + "03   - - - - -   03\n"
                + "04 - - - - - - - 04\n"
                + "05 - - - - - n - 05\n"
                + "06 - - b - - - - 06\n"
                + "07 - - b - b - - 07\n"
                + "08 - - - - - - - 08\n"
                + "09 - - - - - - - 09\n"
                + "10 - - - - - - - 10\n"
                + "11   - - - - -   11\n"
                + "12     - - -     12\n"
                + "13       *       13\n"
                + "%  A B C D E F G";

        plateauCam.setFromFile("data/return.txt");
        plateauCam.play("D7-F5", "noir");

        assertEquals(3, plateauCam.getNbPieceBlanc());
        assertEquals(1, plateauCam.getNbPieceNoir());
        assertEquals(expectedPlateau, plateauCam.toString());

        // test avec coup simple canter
        expectedPlateau
                = "%  A B C D E F G\n"
                + "01       *       01\n"
                + "02     - - -     02\n"
                + "03   - - - - -   03\n"
                + "04 - - - - - - - 04\n"
                + "05 - - - b - - - 05\n"
                + "06 - - - B - - - 06\n"
                + "07 - - - b b - - 07\n"
                + "08 - - - - - - - 08\n"
                + "09 - - - - - - - 09\n"
                + "10 - - - - - - - 10\n"
                + "11   - - - - -   11\n"
                + "12     - - -     12\n"
                + "13       *       13\n"
                + "%  A B C D E F G";

        plateauCam.setFromFile("data/canter.txt");
        plateauCam.play("D8-D6", "noit");

        assertEquals(4, plateauCam.getNbPieceBlanc());
        assertEquals(0, plateauCam.getNbPieceNoir());
        assertEquals(expectedPlateau, plateauCam.toString());
    }

    @Test
    public void unPlayCheck() {
        Joueur blanc = new Joueur("1");
        Joueur noir = new Joueur("2");
        String expectedPlateau
                = "%  A B C D E F G\n"
                + "01       *       01\n"
                + "02     - - -     02\n"
                + "03   - - - - -   03\n"
                + "04 - - - - - - - 04\n"
                + "05 - - - - - - - 05\n"
                + "06 - - b - b - - 06\n"
                + "07 - - b n b - - 07\n"
                + "08 - - - - - - - 08\n"
                + "09 - - - - - - - 09\n"
                + "10 - - - - - - - 10\n"
                + "11   - - - - -   11\n"
                + "12     - - -     12\n"
                + "13       *       13\n"
                + "%  A B C D E F G";

        CoupJeu coupCam = new MultipleCoupCam("D7-B7-D5-F7-D7");

        PlateauCam plateauCam = new PlateauCam(noir, blanc);
        plateauCam.setFromFile("data/return.txt");
        plateauCam.play(noir, coupCam);
        plateauCam.unPlay(noir, coupCam);

        assertEquals(1, plateauCam.getNbPieceNoir());
        assertEquals(4, plateauCam.getNbPieceBlanc());
        assertEquals(expectedPlateau, plateauCam.toString());

        // test avec chateau
        expectedPlateau
                = "%  A B C D E F G\n"
                + "01       *       01\n"
                + "02     - - -     02\n"
                + "03   - - - - -   03\n"
                + "04 - - - - - - - 04\n"
                + "05 - - - - - - - 05\n"
                + "06 - - - - - - - 06\n"
                + "07 - - - - - - - 07\n"
                + "08 - - - - - - - 08\n"
                + "09 - - - - - - - 09\n"
                + "10 - - - - - - - 10\n"
                + "11   n - - - -   11\n"
                + "12     b - b     12\n"
                + "13       *       13\n"
                + "%  A B C D E F G";

        coupCam = new MultipleCoupCam("B11-D13-F11");

        plateauCam.setFromFile("data/chateau2.txt");
        plateauCam.play(noir, coupCam);
        plateauCam.unPlay(noir, coupCam);

        assertEquals(1, plateauCam.getNbPieceNoir());
        assertEquals(2, plateauCam.getNbPieceBlanc());
        assertEquals(expectedPlateau, plateauCam.toString());

        // test avec simple coup
        expectedPlateau
                = "%  A B C D E F G\n"
                + "01       *       01\n"
                + "02     - - -     02\n"
                + "03   - - - - -   03\n"
                + "04 - - - - - - - 04\n"
                + "05 - - - - - - - 05\n"
                + "06 - - b - b - - 06\n"
                + "07 - - b n b - - 07\n"
                + "08 - - - - - - - 08\n"
                + "09 - - - - - - - 09\n"
                + "10 - - - - - - - 10\n"
                + "11   - - - - -   11\n"
                + "12     - - -     12\n"
                + "13       *       13\n"
                + "%  A B C D E F G";

        coupCam = new SimpleCoupCam("D7-F5");

        plateauCam.setFromFile("data/return.txt");
        plateauCam.play(noir, coupCam);
        plateauCam.unPlay(noir, coupCam);

        assertEquals(4, plateauCam.getNbPieceBlanc());
        assertEquals(1, plateauCam.getNbPieceNoir());
        assertEquals(expectedPlateau, plateauCam.toString());

        // test avec coup simple canter
        expectedPlateau
                = "%  A B C D E F G\n"
                + "01       *       01\n"
                + "02     - - -     02\n"
                + "03   - - - - -   03\n"
                + "04 - - - - - - - 04\n"
                + "05 - - - b - - - 05\n"
                + "06 - - - - - - - 06\n"
                + "07 - - - b b - - 07\n"
                + "08 - - - B - - - 08\n"
                + "09 - - - - - - - 09\n"
                + "10 - - - - - - - 10\n"
                + "11   - - - - -   11\n"
                + "12     - - -     12\n"
                + "13       *       13\n"
                + "%  A B C D E F G";

        coupCam = new SimpleCoupCam("D8-D6");

        plateauCam.setFromFile("data/canter.txt");
        plateauCam.play(blanc, coupCam);
        plateauCam.unPlay(blanc, coupCam);

        assertEquals(4, plateauCam.getNbPieceBlanc());
        assertEquals(0, plateauCam.getNbPieceNoir());
        assertEquals(expectedPlateau, plateauCam.toString());

        // Test avec mise sur chateau
        expectedPlateau
                = "%  A B C D E F G\n"
                + "01       *       01\n"
                + "02     - - -     02\n"
                + "03   - - - - -   03\n"
                + "04 - - - - - - - 04\n"
                + "05 - - - - - - - 05\n"
                + "06 - - - - - - - 06\n"
                + "07 - - - - - - - 07\n"
                + "08 - - - - - - - 08\n"
                + "09 - - - - - - - 09\n"
                + "10 - - - - - - - 10\n"
                + "11   n - - - -   11\n"
                + "12     b - -     12\n"
                + "13       *       13\n"
                + "%  A B C D E F G";
        coupCam = new SimpleCoupCam("B11-D13");

        plateauCam.setFromFile("data/chateau3.txt");

        plateauCam.play(noir, coupCam);
        plateauCam.unPlay(noir, coupCam);

        assertEquals(1, plateauCam.getNbPieceBlanc());
        assertEquals(1, plateauCam.getNbPieceNoir());
        assertEquals(expectedPlateau, plateauCam.toString());
    }

    @Test
    public void finDePartieCheck() {
        Joueur noir = new Joueur("1");
        Joueur blanc = new Joueur("2");
        PlateauCam plateauCam = new PlateauCam(noir, blanc);

        plateauCam.setFromFile("data/finDePartie.txt");
        assertTrue(plateauCam.finDePartie(noir));

        plateauCam.setFromFile("data/finDePartie1.txt");
        assertTrue(plateauCam.finDePartie(noir));
        assertTrue(plateauCam.finDePartie(blanc));

        plateauCam.setFromFile("data/finDePartie2.txt");
        assertTrue(plateauCam.finDePartie(noir));

        plateauCam.setFromFile("data/return.txt");
        assertFalse(plateauCam.finDePartie(noir));
    }

    @Test
    public void estValideCheck() {
        Joueur noir = new Joueur("1");
        Joueur blanc = new Joueur("2");
        PlateauCam plateauCam = new PlateauCam(noir, blanc);

        plateauCam.setFromFile("data/return.txt");
        CoupJeu jeu = new MultipleCoupCam("D7-F5");
        CoupJeu jeu2 = new MultipleCoupCam("D7-D3");

        assertTrue(plateauCam.estValide(noir, jeu));
        assertFalse(plateauCam.estValide(noir, jeu2));

        CoupJeu jeu3 = new SimpleCoupCam("D7-F7");
        CoupJeu jeu4 = new SimpleCoupCam("D7-F5");
        assertFalse(plateauCam.estValide(noir, jeu3));
        assertTrue(plateauCam.estValide(noir, jeu4));
    }

    @Test
    public void toStringCheck() {
        String expectedPlateau
                = "%  A B C D E F G\n"
                + "01       *       01\n"
                + "02     - - -     02\n"
                + "03   - - - - -   03\n"
                + "04 - - N - N - - 04\n"
                + "05 - n n n n n - 05\n"
                + "06 - - - - - - - 06\n"
                + "07 - - - - - - - 07\n"
                + "08 - - - - - - - 08\n"
                + "09 - b b b b b - 09\n"
                + "10 - - B - B - - 10\n"
                + "11   - - - - -   11\n"
                + "12     - - -     12\n"
                + "13       *       13\n"
                + "%  A B C D E F G";

        assertEquals(expectedPlateau, (new PlateauCam(null, null)).toString());
    }

    @Test
    public void setFromFileCheck() {
        String expectedPlateau
                = "%  A B C D E F G\n"
                + "01       *       01\n"
                + "02     - - -     02\n"
                + "03   - - - - -   03\n"
                + "04 - - - - - - - 04\n"
                + "05 - - - - - - - 05\n"
                + "06 - - - - - - - 06\n"
                + "07 - - - - - - - 07\n"
                + "08 - - - - - - - 08\n"
                + "09 - - - - - - - 09\n"
                + "10 - - - - - - - 10\n"
                + "11   - - - - -   11\n"
                + "12     - - -     12\n"
                + "13       *       13\n"
                + "%  A B C D E F G";
        PlateauCam plateauCam = new PlateauCam(new Joueur("noir"), new Joueur("blanc"));

        plateauCam.setFromFile("data/empty.txt");
        assertEquals(expectedPlateau, plateauCam.toString());
        assertEquals(0, plateauCam.getNbPieceBlanc());
        assertEquals(0, plateauCam.getNbPieceNoir());

        expectedPlateau
                = "%  A B C D E F G\n"
                + "01       *       01\n"
                + "02     - - -     02\n"
                + "03   - - - - -   03\n"
                + "04 - - - - - - - 04\n"
                + "05 - - - - - - - 05\n"
                + "06 - - b - b - - 06\n"
                + "07 - - b n b - - 07\n"
                + "08 - - - - - - - 08\n"
                + "09 - - - - - - - 09\n"
                + "10 - - - - - - - 10\n"
                + "11   - - - - -   11\n"
                + "12     - - -     12\n"
                + "13       *       13\n"
                + "%  A B C D E F G";
        plateauCam.setFromFile("data/return.txt");

        assertEquals(expectedPlateau, plateauCam.toString());
        assertEquals(4, plateauCam.getNbPieceBlanc());
        assertEquals(1, plateauCam.getNbPieceNoir());
    }

    @Test
    public void saveToFileCheck() {
        String outFileName = "data/test.txt";

        PlateauCam plateauCamActual = new PlateauCam(new Joueur("noir"), new Joueur("blanc"));
        plateauCamActual.saveToFile(outFileName);

        PlateauCam plateauCamExpected = new PlateauCam(new Joueur("noir"), new Joueur("blanc"));
        plateauCamExpected.setFromFile(outFileName);

        assertEquals(plateauCamExpected.toString(), plateauCamActual.toString());

    }
}
