package cam.modele;

import iia.jeux.alg.AlgoJeu;
import iia.jeux.alg.AlphaBeta;
import iia.jeux.alg.IterativeDeepening;
import iia.jeux.modele.CoupJeu;
import iia.jeux.modele.joueur.JoueurInterface;
import jeux.cam.HeuristiqueCam;
import jeux.cam.PlateauCam;

/**
 *
 * @author godeau
 */
public class JoueurCam implements IJoueur {

    AlgoJeu algoJeu;
    PlateauCam plateauCam;
    int myColor;

    public JoueurCam() {
        algoJeu = new IterativeDeepening(new HeuristiqueCam(), this, null, /*2*/5000);
    }
    
    @Override
    public void initJoueur(int mycolour) {
        if (mycolour == IJoueur.BLANC) {
            plateauCam = new PlateauCam(null, this);
        } else {
            plateauCam = new PlateauCam(this, null);
        }
        this.myColor = mycolour;
    }

    @Override
    public int getNumJoueur() {
        return myColor;
    }

    @Override
    public String choixMouvement() {
        CoupJeu meilleurCoup = algoJeu.meilleurCoup(plateauCam);
        plateauCam.play(this, meilleurCoup);
        return meilleurCoup.toString();
    }

    @Override
    public void declareLeVainqueur(int colour) {
    }

    @Override
    public void mouvementEnnemi(String coup) {
        plateauCam.play((JoueurInterface) null, coup);
    }

    @Override
    public String binoName() {
        return "Godeau & Panirselvame";
    }

}
