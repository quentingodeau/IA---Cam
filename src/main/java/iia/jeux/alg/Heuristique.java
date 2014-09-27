package iia.jeux.alg;

import iia.jeux.modele.PlateauJeu;
import iia.jeux.modele.joueur.JoueurInterface;

public interface Heuristique {

    /**
     *
     * @param plateauJeu
     * @param joueur
     * @return
     */
    public int eval(PlateauJeu plateauJeu, JoueurInterface joueur);

}
