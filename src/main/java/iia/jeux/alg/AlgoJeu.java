package iia.jeux.alg;

import iia.jeux.modele.CoupJeu;
import iia.jeux.modele.PlateauJeu;

public interface AlgoJeu {

    /**
     * Renvoie le meilleur
     *
     * @param plateauJeu
     * @return
     */
    public CoupJeu meilleurCoup(PlateauJeu plateauJeu);

}
