package iia.jeux.modele;

import iia.jeux.modele.joueur.JoueurInterface;

/**
 *
 * @author godeau & pannirselvame
 */
public interface Partie1 {

    /**
     * Initialise un plateau a partir d'un fichier texte
     *
     * @param fileName le nom du fichier a lire
     */
    public void setFromFile(String fileName);

    /**
     * Sauve la configuration d'un plateau dans d'un fichier
     *
     * @param fileName le nom du fichier a sauvegarder
     */
    public void saveToFile(String fileName);

    /**
     * Indique si le coup <move> est valide pour le joueur <player> sur le
     * plateau courant
     *
     * @param move le coup a jouer
     * @param player le joueur qui joue (représenté par "noir" ou "blanc")
     * @return
     */
    public boolean estValide(String move, String player);

    /**
     * Modifie le plateau en jouant le coup move
     *
     * @param move le coup a jouer, représenté sous la forme "A1-B2-C3"
     * @param player le joueur qui joue représenté par "noir" ou "blanc"
     */
    public void play(String move, String player);

    /**
     * Indique si la partie est terminée
     *
     * @param joueur
     * @return vrai si la partie est terminée
     */
    public boolean finDePartie(JoueurInterface joueur);
}
