package cam.modele;

import iia.jeux.modele.joueur.JoueurInterface;

/**
 * Voici l'interface abstraite qu'il suffit d'implanter pour jouer. Ensuite,
 * vous devez utiliser ClientJeu en lui donnant le nom de votre classe pour
 * qu'il la charge et se connecte au serveur.
 *
 * @author L. Simon (Univ. Paris-Sud)- 2006-2013
 *
 */
public interface IJoueur extends JoueurInterface {

    public static final int BLANC = 0;  // Mais pas lors de la conversation avec l'arbitre (méthodes initJoueur et getNumJoueur)
    public static final int NOIR = 1;   // Vous pouvez changer cela en interne si vous le souhaitez

    /**
     * L'arbitre vient de lancer votre joueur. Il lui informe par cette méthode
     * que vous devez jouer dans cette couleur. Vous pouvez utiliser cette
     * méthode abstraite, ou la méthode constructeur de votre classe, pour
     * initialiser vos structures.
     *
     * @param mycolour La couleur dans laquelle vous allez jouer (-1=BLANC,
     * 1=NOIR)
     */
    public void initJoueur(int mycolour);

    /**
     *
     * @return retourne l'argument passé par la fonction ci-dessus (constantes
     * BLANC ou NOIR)
     */
    public int getNumJoueur();

    /**
     * C'est ici que vous devez faire appel à votre IA pour trouver le meilleur
     * coup à jouer sur le plateau courant.
     *
     * @return une chaîne décrivant le mouvement. Cette chaîne doit être décrite
     * exactement comme sur l'exemple : String msg = "" + positionInitiale + " "
     * + positionIntermédiaire1 + " " + positionIntermédiaire2 + " " + [...] + "
     * " + positionIntermédiaireN + " " + positionFinale + '\0'; Chaque position
     * contient une lettre et un numéro, par exemple: A1,B2,C13
     */
    public String choixMouvement();

    /**
     * Méthode appelée par l'arbitre pour désigner le vainqueur. Vous pouvez en
     * profiter pour imprimer une bannière de joie... Si vous gagnez...
     *
     * @param colour La couleur du gagnant (BLANC=-1, NOIR=1).
     */
    public void declareLeVainqueur(int colour);

    /**
     * On suppose que l'arbitre a vérifié que le mouvement ennemi était bien
     * légal. Il vous informe du mouvement ennemi. A vous de répercuter ce
     * mouvement dans vos structures. Comme par exemple éliminer les pions que
     * ennemi vient de vous prendre par ce mouvement. Il n'est pas nécessaire de
     * réfléchir déjà à votre prochain coup à jouer : pour cela l'arbitre
     * appelera ensuite choixMouvement().
     *
     * @param coup une chaîne décrivant le mouvement: positionInitiale + " " +
     * positionIntermédiaire1 + " " + positionIntermédiaire2 + " " + [...] + " "
     * + positionIntermédiaireN + " " + positionFinale + '\0'; Chaque position
     * contient une lettre et un numéro, par exemple: A1,B2,C13
     */
    public void mouvementEnnemi(String coup);

    public String binoName();

}
