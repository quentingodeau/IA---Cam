package iia.jeux.alg;

import iia.jeux.modele.CoupJeu;
import iia.jeux.modele.PlateauJeu;
import iia.jeux.modele.joueur.JoueurInterface;
import java.util.List;

/**
 *
 * @author godeau & pannirselvame
 */
public class AlphaBeta implements AlgoJeu {

    /**
     * La profondeur de recherche par défaut
     */
    private final static int PROFMAXDEFAUT = 6;

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

    /**
     * 
     * @param heuristique
     * @param joueurAmi
     * @param joueurEnnemi 
     */
    public AlphaBeta(Heuristique heuristique, JoueurInterface joueurAmi, JoueurInterface joueurEnnemi) {
        this(heuristique, joueurAmi, joueurEnnemi, PROFMAXDEFAUT);
    }

    /**
     * 
     * @param heuristique
     * @param joueurAmi
     * @param joueurEnnemi
     * @param profMax 
     */
    public AlphaBeta(Heuristique heuristique, JoueurInterface joueurAmi, JoueurInterface joueurEnnemi, int profMax) {
        this.profMax = profMax;
        this.heuristique = heuristique;
        this.joueurEnnemi = joueurEnnemi;
        this.joueurAmi = joueurAmi;
    }

    @Override
    public CoupJeu meilleurCoup(PlateauJeu plateauJeu) {
        List<CoupJeu> coupsPossibles = plateauJeu.coupsPossibles(joueurAmi);
        CoupJeu cj = coupsPossibles.get(0);

        int alpha = Integer.MIN_VALUE, beta = Integer.MAX_VALUE;
        for (CoupJeu coupJeu : coupsPossibles) {
            plateauJeu.play(joueurAmi, coupJeu);
            int max = Math.max(alpha, minMax(profMax - 1, alpha, beta, plateauJeu));
            if (max > alpha) {
                alpha = max;
                cj = coupJeu;
            }
            plateauJeu.unPlay(joueurAmi, coupJeu);
            if (alpha >= beta) {
                return coupJeu;
            }
        }
        return cj;
    }

    @Override
    public String toString() {
        return "AlphaBeta(ProfMax=" + profMax + ")";
    }

    private int maxMin(int profondeur, int alpha, int beta, PlateauJeu plateauJeu) {
        if (profondeur == 0 || plateauJeu.finDePartie(joueurAmi)) {
            return heuristique.eval(plateauJeu, joueurAmi);
        } else {
            for (CoupJeu coupJeu : plateauJeu.coupsPossibles(joueurAmi)) {
                plateauJeu.play(joueurAmi, coupJeu);
                alpha = Math.max(alpha, minMax(profondeur - 1, alpha, beta, plateauJeu));
                plateauJeu.unPlay(joueurAmi, coupJeu);
                if (alpha >= beta) {
                    return beta;
                }
            }
            return alpha;
        }
    }

    private int minMax(int profondeur, int alpha, int beta, PlateauJeu plateauJeu) {
        if (profondeur == 0 || plateauJeu.finDePartie(joueurEnnemi)) {
            return heuristique.eval(plateauJeu, joueurAmi);
        } else {
            for (CoupJeu coupJeu : plateauJeu.coupsPossibles(joueurEnnemi)) {
                plateauJeu.play(joueurEnnemi, coupJeu);
                beta = Math.min(beta, maxMin(profondeur - 1, alpha, beta, plateauJeu));
                plateauJeu.unPlay(joueurEnnemi, coupJeu);
                if (alpha >= beta) {
                    return alpha;
                }
            }
            return beta;
        }
    }

}
