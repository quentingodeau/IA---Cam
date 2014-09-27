package iia.jeux.modele;

import iia.jeux.modele.joueur.JoueurInterface;
import java.util.LinkedList;

public interface PlateauJeu {
    
    public PlateauJeu clone();
    
    /**
     * renvoie la liste des coups possibles
     *
     * @param joueur	le joueur dont c'est au tour de jouer
     * @return liste des coups possibles pour le joueur j
     */
    public LinkedList<CoupJeu> coupsPossibles(JoueurInterface joueur);

    /**
     * joue un coup sur le plateau
     *
     * @param joueur	Le joueur qui joue
     * @param coupJeu Le coup joué par le joueur
     */
    public void play(JoueurInterface joueur, CoupJeu coupJeu);

    /**
     * Indique si la partie est terminée
     *
     * @param joueur
     * @return vrai si la partie est terminée
     */
    public boolean finDePartie(JoueurInterface joueur);

    /**
     *
     * @param joueur
     * @param coupJeu
     */
    public void unPlay(JoueurInterface joueur, CoupJeu coupJeu);

    /**
     * indique si un coup est possible pour un joueur sur le plateau courant
     *
     * @param joueur
     * @param coupJeu Le coup envisagé par le joueur
     * @return
     */
    public boolean estValide(JoueurInterface joueur, CoupJeu coupJeu);
}
