package iia.jeux.alg;

import iia.jeux.modele.CoupJeu;
import iia.jeux.modele.PlateauJeu;
import iia.jeux.modele.joueur.JoueurInterface;
import java.util.List;

public class Minimax implements AlgoJeu {

    /**
     * La profondeur de recherche par défaut
     */
    private final static int PROFMAXDEFAUT = 4;

    /**
     * La profondeur de recherche utilisée pour l'algorithme
     */
    private int profMax;

    /**
     * L'heuristique utilisée par l'algorithme
     */
    private Heuristique heuristique;

    /**
     * Le joueur Min (l'adversaire)
     */
    private JoueurInterface joueurEnnemi;

    /**
     * Le joueur Max (celui dont l'algorithme de recherche adopte le point de
     * vue)
     */
    private JoueurInterface joueurAmi;

    // -------------------------------------------
    // Constructeurs
    // -------------------------------------------
    public Minimax(Heuristique heuristique, JoueurInterface joueurMax, JoueurInterface joueurMin) {
        this(heuristique, joueurMax, joueurMin, PROFMAXDEFAUT);
    }

    public Minimax(Heuristique heuristique, JoueurInterface joueurAmi, JoueurInterface joueurEnnemi, int profMax) {
        this.heuristique = heuristique;
        this.joueurEnnemi = joueurEnnemi;
        this.joueurAmi = joueurAmi;
        this.profMax = profMax;
    }

    // -------------------------------------------
    // Méthodes de l'interface AlgoJeu
    // -------------------------------------------
    @Override
    public CoupJeu meilleurCoup(PlateauJeu plateauJeu) {
        int max = Integer.MIN_VALUE;
        List<CoupJeu> coupsPossibles = plateauJeu.coupsPossibles(joueurAmi);
        CoupJeu cj = coupsPossibles.get(0);

        for (CoupJeu coupJeu : coupsPossibles) {
            plateauJeu.play(joueurAmi, coupJeu);
            int newMax = minMax(profMax - 1, plateauJeu);
            if (max < newMax) {
                cj = coupJeu;
                max = newMax;
            }
            plateauJeu.unPlay(joueurAmi, coupJeu);
        }

        return cj;

    }

    // -------------------------------------------
    // Méthodes publiques
    // -------------------------------------------
    @Override
    public String toString() {
        return "MiniMax(ProfMax=" + profMax + ")";
    }

    // -------------------------------------------
    // Méthodes internes
    // -------------------------------------------
    //A vous de jouer pour implanter Minimax
    private int maxMin(int profondeur, PlateauJeu plateauJeu) {
        if (profondeur == 0 || plateauJeu.finDePartie(joueurAmi)) {
            return heuristique.eval(plateauJeu, joueurAmi);
        } else {
            int max = Integer.MIN_VALUE;

            for (CoupJeu coupJeu : plateauJeu.coupsPossibles(joueurAmi)) {
                plateauJeu.play(joueurAmi, coupJeu);
                max = Math.max(max, minMax(profondeur - 1, plateauJeu));
                plateauJeu.unPlay(joueurAmi, coupJeu);
            }
            return max;
        }
    }

    private int minMax(int profondeur, PlateauJeu plateauJeu) {
        if (profondeur == 0 || plateauJeu.finDePartie(joueurEnnemi)) {
            return heuristique.eval(plateauJeu, joueurAmi);
        } else {
            int min = Integer.MAX_VALUE;

            for (CoupJeu coupJeu : plateauJeu.coupsPossibles(joueurEnnemi)) {
                plateauJeu.play(joueurEnnemi, coupJeu);
                min = Math.min(min, maxMin(profondeur - 1, plateauJeu));
                plateauJeu.unPlay(joueurEnnemi, coupJeu);
            }
            return min;
        }
    }
}
