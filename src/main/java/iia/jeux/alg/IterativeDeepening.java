package iia.jeux.alg;

import iia.jeux.modele.CoupJeu;
import iia.jeux.modele.PlateauJeu;
import iia.jeux.modele.joueur.JoueurInterface;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author godeau & pannirselvame
 */
public class IterativeDeepening implements AlgoJeu {

    /**
     * L'heuristique utilisée par l'algorithme.
     */
    private final Heuristique heuristique;

    /**
     * Le joueur ennemi (l'adversaire).
     */
    private final JoueurInterface joueurEnnemi;

    /**
     * Le joueur ami (celui dont l'algorithme de recherche adopte le point de
     * vue).
     */
    private final JoueurInterface joueurAmi;

    /**
     * Limite de temps maximal pour trouver le meilleur coup.
     */
    private final long limit;

    // dynamic
    /**
     * 
     */
    private int profMax;
    
    /**
     * Profondeur que l'on a réussi a atteindre.
     */
    private int profReach;
    
    /**
     * Copie du plateau de jeu sur laquel on cherche le meilleur couo.
     */
    private PlateauJeu plateauJeu;
    
    /**
     * Liste des coups susceptible de d'élaguer l'arbre.
     */
    private ArrayList<CoupJeu> promesse = new ArrayList<>(15);
    
    /**
     * Temps en millisecond ou l'on a démarrer la recherche de la meilleur solution.
     */
    private long start;

    /**
     *
     * @param heuristique
     * @param joueurAmi
     * @param joueurEnnemi
     * @param limit
     */
    public IterativeDeepening(Heuristique heuristique, JoueurInterface joueurAmi, JoueurInterface joueurEnnemi, long limit) {
        this.heuristique = heuristique;
        this.joueurEnnemi = joueurEnnemi;
        this.joueurAmi = joueurAmi;
        this.limit = limit;
    }

    @Override
    public CoupJeu meilleurCoup(PlateauJeu pj) {
        LinkedList<CoupJeu> coupsPossibles = pj.coupsPossibles(joueurAmi);
        CoupJeu coupJeu = coupsPossibles.getFirst();
        
        // S'il n'y a que un seul coup, on n'a pas le choix on le joue
        if(coupsPossibles.size() == 1){
            return coupJeu;
        }
        
        start = System.currentTimeMillis();
        promesse.clear();
        this.plateauJeu = pj.clone();
        profMax = 0;

        try {
            do { // prof reatch et prof max + do while prof max++ début 
                profReach = 0;
                profMax++;

                int alpha = Integer.MIN_VALUE, beta = Integer.MAX_VALUE;

                // on joue notre supposé meilleur coup
                if (!promesse.isEmpty()) {
                    CoupJeu promesseMeilleur = promesse.get(0);
                    setBeginning(promesseMeilleur, coupsPossibles);
                }

                for (CoupJeu cj : coupsPossibles) {
                    this.plateauJeu.play(joueurAmi, cj);
                    int max = Math.max(alpha, minMax(1, alpha, beta));
                    if (max > alpha) {
                        alpha = max;
                        setPromesse(cj, 0);
                        coupJeu = cj;
                    }
                    this.plateauJeu.unPlay(joueurAmi, cj);
                    if (alpha >= beta) {
                        coupJeu = cj;
                        // On garde dans promesse le coup qui a le plus de chance de nous faire couper par la suite
                        return coupJeu;
                    }
                }
                // on réinitialise les coups
                coupsPossibles = this.plateauJeu.coupsPossibles(joueurAmi);
            } while (profReach >= profMax);
        } catch (InterruptedException ex) {
        }

//        System.err.println("[END] Profondeur " + (profMax - 1) + "\ttemps " + (System.currentTimeMillis() - start));
//        System.err.println("Promesse " + promesse + "\t" + "coup jeu " + coupJeu);
//        System.err.println(pj);
//        System.err.flush();

        return coupJeu;
    }

    private int maxMin(int profondeur, int alpha, int beta) throws InterruptedException {
        if (System.currentTimeMillis() - start >= limit) {
            throw new InterruptedException();
        }

        if (profondeur == profMax || plateauJeu.finDePartie(joueurAmi)) {
            profReach = Math.max(profReach, profondeur);
            return heuristique.eval(plateauJeu, joueurAmi);
        } else {
            LinkedList<CoupJeu> coupsPossibles = plateauJeu.coupsPossibles(joueurAmi);
            if (promesse.size() > profondeur) {
                CoupJeu promesseMeilleur = promesse.get(profondeur);
                setBeginning(promesseMeilleur, coupsPossibles);
            }

            for (CoupJeu cj : coupsPossibles) {
                plateauJeu.play(joueurAmi, cj);
                int max = Math.max(alpha, minMax(profondeur + 1, alpha, beta));
                if (max > alpha) {
                    alpha = max;
                    setPromesse(cj, profondeur);
                }
                plateauJeu.unPlay(joueurAmi, cj);
                if (alpha >= beta) {
                    // setPromesse(cj, profondeur);
                    return beta;
                }
            }
            return alpha;
        }
    }

    private int minMax(int profondeur, int alpha, int beta) throws InterruptedException {
        if (System.currentTimeMillis() - start >= limit) {
            throw new InterruptedException();
        }

        if (profondeur == profMax || plateauJeu.finDePartie(joueurEnnemi)) {
            profReach = Math.max(profReach, profondeur);
            return heuristique.eval(plateauJeu, joueurAmi);
        } else {
            LinkedList<CoupJeu> coupsPossibles = plateauJeu.coupsPossibles(joueurEnnemi);
            if (promesse.size() > profondeur) {
                CoupJeu promesseMeilleur = promesse.get(profondeur);
                setBeginning(promesseMeilleur, coupsPossibles);
            }

            for (CoupJeu cj : coupsPossibles) {
                plateauJeu.play(joueurEnnemi, cj);
                int min = Math.min(beta, maxMin(profondeur + 1, alpha, beta));
                if (min < beta) {
                    beta = min;
                    setPromesse(cj, profondeur);

                }
                plateauJeu.unPlay(joueurEnnemi, cj);
                if (alpha >= beta) {
                    //  setPromesse(cj, profondeur);
                    return alpha;
                }
            }
            return beta;
        }
    }

    private void setPromesse(CoupJeu cj, int profondeur) {
        int size = promesse.size();
        if (size <= profondeur) {
            promesse.add(cj);
        } else {
            if (!cj.equals(promesse.get(profondeur))) {
                for (int i = size - 1; i >= profondeur; i--) {
                    promesse.remove(i);
                }
                promesse.add(cj);
            }
        }
    }

    private void setBeginning(CoupJeu coupJeu, LinkedList<CoupJeu> coupsPossibles) {
        for (Iterator<CoupJeu> it = coupsPossibles.iterator(); it.hasNext();) {
            CoupJeu cj = it.next();
            if (cj.equals(coupJeu)) {
                it.remove();
                coupsPossibles.addFirst(cj);
                return;
            }
        }
    }

    @Override
    public String toString() {
        return "IterativeDeepening(TempsLimite=" + limit + "ms)";
    }

}
