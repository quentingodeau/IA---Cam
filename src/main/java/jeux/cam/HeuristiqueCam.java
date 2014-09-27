package jeux.cam;

import iia.jeux.alg.Heuristique;
import iia.jeux.modele.PlateauJeu;
import iia.jeux.modele.joueur.JoueurInterface;
import static jeux.cam.PlateauCam.*;
import jeux.cam.modele.Position;

/**
 *
 * @author godeau & pannirselvame
 */
public class HeuristiqueCam implements Heuristique {

    private static final int[][] SCORES_GRILLE_BlANC = {
        {0, 0, 0, 4, 0, 0, 0},
        {0, 0, 4, 0, 4, 0, 0},
        {0, 3, 0, 0, 0, 3, 0},
        {2, 0, 0, 0, 0, 0, 2},
        {1, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 0, 0, 0, 1},
        {0, 1, 1, 1, 1, 1, 0},
        {0, 0, 1, 1, 1, 0, 0},
        {0, 0, 0, 1, 0, 0, 0}
    };

    private static final int[][] SCORES_GRILLE_NOIR = {
        {0, 0, 0, 1, 0, 0, 0},
        {0, 0, 1, 1, 1, 0, 0},
        {0, 1, 1, 1, 1, 1, 0},
        {1, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 0, 0, 0, 1},
        {2, 0, 0, 0, 0, 0, 2},
        {0, 3, 0, 0, 0, 3, 0},
        {0, 0, 4, 0, 4, 0, 0},
        {0, 0, 0, 4, 0, 0, 0}
    };

    private final static float PRIX_CAVALIER = 2.25f;
//    private final static int ZONE_CRITIQUE = 3;

    @Override
    public int eval(PlateauJeu plateauJeu, JoueurInterface joueur) {
        PlateauCam plateauCam = (PlateauCam) plateauJeu;

        /**
         * Distance entre nos pions et le château adverse
         */
        int distanceChateau = Integer.MAX_VALUE;

        /**
         * Distance entre les cavaliers averse et notre château
         */
        int distanceChateauAdv = Integer.MAX_VALUE;

        int bonus = 0;

        /**
         *
         */
        float score = 0;
        float scoreAdv = 0;

        // On est le joueur blanc
        if (plateauCam.joueurBlanc == joueur) {

            if (plateauCam.getNbPieceNoir() == 0 || (plateauCam.plateau[CHATEAU_NOIR.getLigne()][CHATEAU_NOIR.getColonne()]) != CHATEAU) {
                return Integer.MAX_VALUE;
            }

            if (plateauCam.getNbPieceBlanc() == 0 || plateauCam.plateau[CHATEAU_BLANC.getLigne()][CHATEAU_BLANC.getColonne()] != CHATEAU) {
                return Integer.MIN_VALUE;
            }

            // Évaluation du plateau
            // Ami
            for (Position position : plateauCam.pieceJoueurBlanc) {
                if (plateauCam.plateau[position.getLigne()][position.getColonne()] == PION_BLANC) {
                    // Évaluation de la distance entre le chateau adverse et nos pions
                    distanceChateau = Math.min(position.getLigne(), distanceChateau);
                    score += PRIX_CAVALIER;
                } else {
                    // Évaluation de la distance entre le chateau adverse et nos pions
                    distanceChateau = Math.min(position.getLigne(), distanceChateau);
                    score++;
                }

                bonus += SCORES_GRILLE_BlANC[position.getLigne()][position.getColonne()];
            }

            // ennemi
            for (Position position : plateauCam.pieceJoueurNoir) {
                if (plateauCam.plateau[position.getLigne()][position.getColonne()] == PION_NOIR) {
                    // Évaluation de la distance entre notre chateau et l'adversaire
                    distanceChateauAdv = Math.min(NBLIGNE - 1 - position.getLigne(), distanceChateauAdv);
                    scoreAdv++;
                } else {
                    // Évaluation de la distance entre notre chateau et l'adversaire
                    distanceChateauAdv = Math.min(NBLIGNE - 1 - position.getLigne(), distanceChateauAdv);
                    scoreAdv += PRIX_CAVALIER;
                }
            }
        } // c'est le joueur noir
        else {
            // Victoire
            if (plateauCam.getNbPieceBlanc() == 0 || plateauCam.plateau[CHATEAU_BLANC.getLigne()][CHATEAU_BLANC.getColonne()] != CHATEAU) {
                return Integer.MAX_VALUE;
            }

            // Défaite
            if (plateauCam.getNbPieceNoir() == 0 || (plateauCam.plateau[CHATEAU_NOIR.getLigne()][CHATEAU_NOIR.getColonne()]) != CHATEAU) {
                return Integer.MIN_VALUE;
            }

            // Évaluation du plateau
            // Ennemi
            for (Position position : plateauCam.pieceJoueurBlanc) {
                if (plateauCam.plateau[position.getLigne()][position.getColonne()] == PION_BLANC) {
                    // Évaluation de la distance entre notre chateau et l'adversaire
                    distanceChateauAdv = Math.min(position.getLigne(), distanceChateauAdv);
                    scoreAdv += PRIX_CAVALIER;
                } else {
                    // Évaluation de la distance entre notre chateau et l'adversaire
                    distanceChateauAdv = Math.min(position.getLigne(), distanceChateauAdv);
                    scoreAdv++;
                }
            }

            // Ami
            for (Position position : plateauCam.pieceJoueurNoir) {
                if (plateauCam.plateau[position.getLigne()][position.getColonne()] == PION_NOIR) {
                    // Évaluation de la distance entre le chateau adverse et nos pions
                    distanceChateau = Math.min(NBLIGNE - 1 - position.getLigne(), distanceChateau);
                    score++;
                } else {
                    // Évaluation de la distance entre le chateau adverse et nos pions
                    distanceChateau = Math.min(NBLIGNE - 1 - position.getLigne(), distanceChateau);
                    score += PRIX_CAVALIER;
                }

                bonus += SCORES_GRILLE_NOIR[position.getLigne()][position.getColonne()];
            }
        }

        if (score - scoreAdv < 0) {
            return Math.round((score - scoreAdv) * 2) + (distanceChateauAdv - distanceChateau) + bonus;
        } else {
            return Math.round(score - scoreAdv) + (distanceChateauAdv - distanceChateau) + bonus;
        }
    }
}
