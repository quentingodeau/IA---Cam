package jeux.cam;

import iia.jeux.modele.CoupJeu;
import iia.jeux.modele.PlateauJeu;
import iia.jeux.modele.Partie1;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jeux.cam.modele.BitArray;
import jeux.cam.modele.MultipleCoupCam;
import jeux.cam.modele.Position;
import jeux.cam.modele.SimpleCoupCam;
import jeux.cam.modele.MultipleCoupCam.Pair;
import jeux.cam.modele.PositionPiece;
import iia.jeux.modele.joueur.JoueurInterface;

/**
 *
 * @author godeau & pannirselvame
 */
public class PlateauCam implements PlateauJeu, Partie1 {

    public final static int NBLIGNE = 13;
    public final static int NBCOLONNE = 7;

    final static char CHATEAU = '*';
    final static char VIDE = '-';
    final static char CAVALIER_BLANC = 'B';
    final static char CAVALIER_NOIR = 'N';
    final static char PION_BLANC = 'b';
    final static char PION_NOIR = 'n';

    final static Position CHATEAU_NOIR = new Position(0, (NBCOLONNE - 1) / 2);
    final static Position CHATEAU_BLANC = new Position(NBLIGNE - 1, (NBCOLONNE - 1) / 2);

    PositionPiece pieceJoueurNoir = new PositionPiece();
    final JoueurInterface joueurNoir;

    PositionPiece pieceJoueurBlanc = new PositionPiece();
    final JoueurInterface joueurBlanc;

    char plateau[][] = {
        {' ', ' ', ' ', CHATEAU, ' ', ' ', ' '},
        {' ', ' ', VIDE, VIDE, VIDE, ' ', ' '},
        {' ', VIDE, VIDE, VIDE, VIDE, VIDE, ' '},
        {VIDE, VIDE, CAVALIER_NOIR, VIDE, CAVALIER_NOIR, VIDE, VIDE},
        {VIDE, PION_NOIR, PION_NOIR, PION_NOIR, PION_NOIR, PION_NOIR, VIDE},
        {VIDE, VIDE, VIDE, VIDE, VIDE, VIDE, VIDE},
        {VIDE, VIDE, VIDE, VIDE, VIDE, VIDE, VIDE},
        {VIDE, VIDE, VIDE, VIDE, VIDE, VIDE, VIDE},
        {VIDE, PION_BLANC, PION_BLANC, PION_BLANC, PION_BLANC, PION_BLANC, VIDE},
        {VIDE, VIDE, CAVALIER_BLANC, VIDE, CAVALIER_BLANC, VIDE, VIDE},
        {' ', VIDE, VIDE, VIDE, VIDE, VIDE, ' '},
        {' ', ' ', VIDE, VIDE, VIDE, ' ', ' '},
        {' ', ' ', ' ', CHATEAU, ' ', ' ', ' '}
    };

    private PlateauCam(PlateauCam cam) {
        this.joueurBlanc = cam.joueurBlanc;
        this.joueurNoir = cam.joueurNoir;

        this.pieceJoueurBlanc = new PositionPiece(cam.pieceJoueurBlanc);
        this.pieceJoueurNoir = new PositionPiece(cam.pieceJoueurNoir);

        for (int i = 0; i < cam.plateau.length; i++) {
            System.arraycopy(cam.plateau[i], 0, this.plateau[i], 0, cam.plateau[i].length);
        }
    }

    /**
     *
     * @param joueurNoir Le joueur noir
     * @param joueurBlanc Le joueur blanc
     */
    public PlateauCam(JoueurInterface joueurNoir, JoueurInterface joueurBlanc) {
        this.joueurNoir = joueurNoir;
        this.joueurBlanc = joueurBlanc;

        // Blanc
        // pions
        for (int i = 1; i < NBCOLONNE - 1; i++) {
            pieceJoueurBlanc.add(new Position(8, i));
        }

        // Cavalier
        pieceJoueurBlanc.add(new Position(9, 2));
        pieceJoueurBlanc.add(new Position(9, 4));

        // Noir
        // pions
        for (int i = 1; i < NBCOLONNE - 1; i++) {
            pieceJoueurNoir.add(new Position(4, i));
        }
        
        // Cavaliers
        pieceJoueurNoir.add(new Position(3, 2));
        pieceJoueurNoir.add(new Position(3, 4));
    }

    @Override
    public PlateauJeu clone() {
        return new PlateauCam(this);
    }

    @Override
    public LinkedList<CoupJeu> coupsPossibles(JoueurInterface joueur) {
        LinkedList<CoupJeu> coupsPossibles = new LinkedList<>();
        LinkedList<CoupJeu> coupsPossiblesMange = new LinkedList<>();

        // On suppose le joueur noir
        char cavalier = CAVALIER_NOIR,
                cavalierAdv = CAVALIER_BLANC,
                pion = PION_NOIR,
                pionAdv = PION_BLANC;

        boolean descend = true;

        List<Position> pieces = pieceJoueurNoir;

        Position positionChateauJoueur = CHATEAU_NOIR;

        if (joueur == joueurBlanc) {
            cavalier = CAVALIER_BLANC;
            cavalierAdv = CAVALIER_NOIR;
            pion = PION_BLANC;
            pionAdv = PION_NOIR;
            positionChateauJoueur = CHATEAU_BLANC;
            descend = false;
            pieces = pieceJoueurBlanc;
        }

        // TODO Check fail in AlphaBeta
        for (Position position : pieces) {
            // Si cavalier
            if (plateau[position.getLigne()][position.getColonne()] == cavalier) {
                coupsPossiblesCavalier(position.getLigne(), position.getColonne(), coupsPossibles, coupsPossiblesMange, cavalier, pion, positionChateauJoueur, cavalierAdv, pionAdv);
            }//sinon si pion 
            else/* if (plateau[position.getLigne()][position.getColonne()] == pion)*/ {/* FIXME c'est condition ne devrais pas être */
                coupsPossiblesPion(position.getLigne(), position.getColonne(), coupsPossibles, coupsPossiblesMange, cavalier, pion, positionChateauJoueur, cavalierAdv, pionAdv, descend);
            } 
//            else {
//                 System.err.println("NO GOOD !!!" + position); // FIXME Ce message ne devrais jamais être afficher
//            }
        }

        return (coupsPossiblesMange.isEmpty() ? coupsPossibles : coupsPossiblesMange);
    }

    /**
     *
     * @param i
     * @param j
     * @param coupsPossibles
     * @param coupsPossiblesMange
     * @param cavalier
     * @param pion
     * @param pChateauJoueur
     * @param cavalierAdv
     * @param pionAdv
     */
    protected void coupsPossiblesCavalier(int i, int j, List<CoupJeu> coupsPossibles, List<CoupJeu> coupsPossiblesMange, char cavalier, char pion, Position pChateauJoueur, char cavalierAdv, char pionAdv) {
        // recherche des coups ou on mange au moins un pion
        coupsPossiblesMange(i, j, coupsPossiblesMange, new MultipleCoupCam(new Position(i, j)), new BitArray(NBLIGNE * NBCOLONNE), pChateauJoueur, cavalierAdv, pionAdv, true);

        // Si il n'existe pas de coup ou l'on mange une piece, on cherche les coups simples
        if (coupsPossiblesMange.isEmpty()) {
            Position depart = new Position(i, j);

            // haut
            if (i - 1 >= 0) {
                // Si vide
                if (plateau[i - 1][j] == VIDE || (plateau[i - 1][j] == CHATEAU && !pChateauJoueur.equals(i - 1, j))) {
                    coupsPossibles.add(new SimpleCoupCam(depart, new Position(i - 1, j)));
                    // Si non vide et "canter"
                } else if (i - 2 >= 0 && (plateau[i - 2][j] == VIDE || (plateau[i - 2][j] == CHATEAU && !pChateauJoueur.equals(i - 2, j)))) {
                    coupsPossibles.add(new SimpleCoupCam(depart, new Position(i - 2, j)));
                }

                // droit
                if (j + 1 < NBCOLONNE) {
                    if (plateau[i - 1][j + 1] == VIDE || (plateau[i - 1][j + 1] == CHATEAU && !pChateauJoueur.equals(i - 1, j + 1))) {
                        coupsPossibles.add(new SimpleCoupCam(depart, new Position(i - 1, j + 1)));
                    } else if (i - 2 >= 0 && j + 2 < NBCOLONNE && (plateau[i - 2][j + 2] == VIDE || (plateau[i - 2][j + 2] == CHATEAU && !pChateauJoueur.equals(i - 2, j + 2)))) {
                        coupsPossibles.add(new SimpleCoupCam(depart, new Position(i - 2, j + 2)));
                    }
                }

                // gauche
                if (j - 1 >= 0) {
                    if (plateau[i - 1][j - 1] == VIDE || (plateau[i - 1][j - 1] == CHATEAU && !pChateauJoueur.equals(i - 1, j - 1))) {
                        coupsPossibles.add(new SimpleCoupCam(depart, new Position(i - 1, j - 1)));
                    } else if (i - 2 >= 0 && j - 2 >= 0 && (plateau[i - 2][j - 2] == VIDE || (plateau[i - 2][j - 2] == CHATEAU && !pChateauJoueur.equals(i - 2, j - 2)))) {
                        coupsPossibles.add(new SimpleCoupCam(depart, new Position(i - 2, j - 2)));
                    }
                }
            }

            // bas
            if (i + 1 < NBLIGNE) {
                if (plateau[i + 1][j] == VIDE || (plateau[i + 1][j] == CHATEAU && !pChateauJoueur.equals(i + 1, j))) {
                    coupsPossibles.add(new SimpleCoupCam(depart, new Position(i + 1, j)));
                    // Si non vide et "canter"
                } else if (i + 2 < NBLIGNE && (plateau[i + 2][j] == VIDE || (plateau[i + 2][j] == CHATEAU && !pChateauJoueur.equals(i + 2, j)))) {
                    coupsPossibles.add(new SimpleCoupCam(depart, new Position(i + 2, j)));
                }

                // droit
                if (j + 1 < NBCOLONNE) {
                    if (plateau[i + 1][j + 1] == VIDE || (plateau[i + 1][j + 1] == CHATEAU && !pChateauJoueur.equals(i + 1, j + 1))) {
                        coupsPossibles.add(new SimpleCoupCam(depart, new Position(i + 1, j + 1)));
                    } else if (i + 2 < NBLIGNE && j + 2 < NBCOLONNE && (plateau[i + 2][j + 2] == VIDE || (plateau[i + 2][j + 2] == CHATEAU && !pChateauJoueur.equals(i + 2, j + 2)))) {
                        coupsPossibles.add(new SimpleCoupCam(depart, new Position(i + 2, j + 2)));
                    }
                }

                // gauche
                if (j - 1 >= 0) {
                    if (plateau[i + 1][j - 1] == VIDE || (plateau[i + 1][j - 1] == CHATEAU && !pChateauJoueur.equals(i + 1, j - 1))) {
                        coupsPossibles.add(new SimpleCoupCam(depart, new Position(i + 1, j - 1)));
                    } else if (i + 2 < NBLIGNE && j - 2 >= 0 && (plateau[i + 2][j - 2] == VIDE || (plateau[i + 2][j - 2] == CHATEAU && !pChateauJoueur.equals(i + 2, j - 2)))) {
                        coupsPossibles.add(new SimpleCoupCam(depart, new Position(i + 2, j - 2)));
                    }
                }
            }

            // droit
            if (j + 1 < NBCOLONNE) {
                if (plateau[i][j + 1] == VIDE || (plateau[i][j + 1] == CHATEAU && !pChateauJoueur.equals(i, j + 1))) {
                    coupsPossibles.add(new SimpleCoupCam(depart, new Position(i, j + 1)));
                } else if (j + 2 < NBCOLONNE && (plateau[i][j + 2] == VIDE || (plateau[i][j + 2] == CHATEAU && !pChateauJoueur.equals(i, j + 2)))) {
                    coupsPossibles.add(new SimpleCoupCam(depart, new Position(i, j + 2)));
                }
            }

            // gauche
            if (j - 1 >= 0) {
                if (plateau[i][j - 1] == VIDE || (plateau[i][j - 1] == CHATEAU && !pChateauJoueur.equals(i, j - 1))) {
                    coupsPossibles.add(new SimpleCoupCam(depart, new Position(i, j - 1)));
                } else if (j - 2 >= 0 && (plateau[i][j - 2] == VIDE || (plateau[i][j - 2] == CHATEAU && !pChateauJoueur.equals(i, j - 2)))) {
                    coupsPossibles.add(new SimpleCoupCam(depart, new Position(i, j - 2)));
                }
            }
        }
    }

    /**
     *
     * @param i [in] ligne où est le cavalier
     * @param j [in] colonne où est le cavalier
     * @param coupsPossibles [in|out]
     * @param coupsPossiblesMange [in|out]
     * @param cavalier
     * @param pion
     * @param pChateauJoueur
     * @param cavalierAdv
     * @param pionAdv
     * @param descend
     */
    protected void coupsPossiblesPion(int i, int j, List<CoupJeu> coupsPossibles, List<CoupJeu> coupsPossiblesMange, char cavalier, char pion, Position pChateauJoueur, char cavalierAdv, char pionAdv, boolean descend) {
        coupsPossiblesMange(i, j, coupsPossiblesMange, new MultipleCoupCam(new Position(i, j)), new BitArray(NBLIGNE * NBCOLONNE), pChateauJoueur, cavalierAdv, pionAdv, true);

        if (coupsPossiblesMange.isEmpty()) {
            Position depart = new Position(i, j);

            i = i + (descend ? 1 : -1);
            if (i >= 0 && i < NBLIGNE) {
                if (plateau[i][j] == VIDE || plateau[i][j] == CHATEAU) {
                    coupsPossibles.add(new SimpleCoupCam(depart, new Position(i, j)));
                }

                // Droite
                if (j + 1 < NBCOLONNE && (plateau[i][j + 1] == VIDE || plateau[i][j + 1] == CHATEAU)) {
                    coupsPossibles.add(new SimpleCoupCam(depart, new Position(i, j + 1)));
                }

                // Gauche
                if (j - 1 >= 0 && (plateau[i][j - 1] == VIDE || plateau[i][j - 1] == CHATEAU)) {
                    coupsPossibles.add(new SimpleCoupCam(depart, new Position(i, j - 1)));
                }
            }
        }
    }

    /**
     *
     * @param i [in] ligne où est le cavalier
     * @param j [in] colonne où est le cavalier
     * @param coupsPossibles [out] listes des coup possible
     * @param coupCam [out] le coup que l'on recherche
     * @param prises [in] tableau des pièces prises (leurs emplacement)
     * @param pChateauJoueur [in] position du château associé au joueur en cour
     * @param cavalierAdv [in] caractère associé au cavalier pour le joueur
     * adverse
     * @param pionAdv [in] caractère associé au pion pour le joueur adverse
     * @param toAdd [in] faut il ajouter le coupCam a la liste des coups
     * possibles
     */
    protected void coupsPossiblesMange(int i, int j, List<CoupJeu> coupsPossibles, MultipleCoupCam coupCam, BitArray prises, Position pChateauJoueur, char cavalierAdv, char pionAdv, boolean toAdd) {
        int hash;
        // droit
        if (j + 2 < NBCOLONNE) {
            // +---+---+---+
            // |   |   |   |
            // +---+---+---+
            // |   | X | O |
            // +---+---+---+
            // |   |   |   |
            // +---+---+---+
            hash = hash(i, j + 1);
            if (!prises.getBit(hash) // Si la pièce que l'on veux tester n'est pas encore prises
                    && (plateau[i][j + 1] == pionAdv || plateau[i][j + 1] == cavalierAdv) // Si la pièce est une pièce adverse
                    && (plateau[i][j + 2] == VIDE || coupCam.getDepart().equals(i, j + 2)) // Si la place après la pièce est disponible (note on ne peut jamais tomber dans un chateau en allant à doite)
                    ) {
                // on ajoute la piece que l'on vient de prendre (on ne doit pas pouvoir la reprendre)
                prises.setBit(hash, true);

                MultipleCoupCam tmp = coupCam;
                // On clone mon objet de façons à distinger 2 directions possibles 
                // car ce sont deux coups différents
                coupCam = (MultipleCoupCam) coupCam.clone();
                tmp.add(new Position(i, j + 2));
                // Calcul de la suite
                coupsPossiblesMange(i, j + 2, coupsPossibles, tmp, prises, pChateauJoueur, cavalierAdv, pionAdv, false);

                // on ajoute si n'est pas déjà présent
                if (toAdd) {
                    coupsPossibles.add(tmp);
                } else {
                    // la suite doit être ajouter car coup différent
                    toAdd = true;
                }

                // on enlève la piece de celle prise pour la suite
                prises.setBit(hash, false);
            }

            // haut
            if (i - 2 >= 0) {
                // +---+---+---+
                // |   |   | O |
                // +---+---+---+
                // |   | X |   |
                // +---+---+---+
                // |   |   |   |
                // +---+---+---+
                hash = hash(i - 1, j + 1);
                if (!prises.getBit(hash) // Si la pièce que l'on veux tester n'est pas encore prises
                        && (plateau[i - 1][j + 1] == pionAdv || plateau[i - 1][j + 1] == cavalierAdv) // Si la pièce est une pièce adverse
                        && (plateau[i - 2][j + 2] == VIDE || coupCam.getDepart().equals(i - 2, j + 2) || (plateau[i - 2][j + 2] == CHATEAU && !pChateauJoueur.equals(i - 2, j + 2))) // Si la place après la pièce est disponible
                        ) {
                    // on ajoute la piece que l'on vient de prendre (on ne doit pas pouvoir la reprendre)
                    prises.setBit(hash, true);
                    MultipleCoupCam tmp = coupCam;
                    // On clone mon objet de façons a distinger 2 dirrection possible 
                    // car sont deux coup différent
                    coupCam = (MultipleCoupCam) coupCam.clone();
                    tmp.add(new Position(i - 2, j + 2));

                    // Calcule de la suite
                    coupsPossiblesMange(i - 2, j + 2, coupsPossibles, tmp, prises, pChateauJoueur, cavalierAdv, pionAdv, false);

                    // on ajoute si n'est pas déjà présent
                    if (toAdd) {
                        coupsPossibles.add(tmp);
                    } else {
                        // la suite doit être ajouter car coup différent
                        toAdd = true;
                    }

                    // on enlève la piece de celle prise pour la suite
                    prises.setBit(hash, false);
                }
            }

            // bas
            if (i + 2 < NBLIGNE) {
                // +---+---+---+
                // |   |   |   |
                // +---+---+---+
                // |   | X |   |
                // +---+---+---+
                // |   |   | O |
                // +---+---+---+
                hash = hash(i + 1, j + 1);
                if (!prises.getBit(hash) // Si la pièce que l'on veux tester n'est pas encore prises
                        && (plateau[i + 1][j + 1] == pionAdv || plateau[i + 1][j + 1] == cavalierAdv) // Si la pièce est une pièce adverse
                        && (plateau[i + 2][j + 2] == VIDE || coupCam.getDepart().equals(i + 2, j + 2) || (plateau[i + 2][j + 2] == CHATEAU && !pChateauJoueur.equals(i + 2, j + 2))) // Si la place après la pièce est disponible
                        ) {
                    // on ajoute la piece que l'on vient de prendre (on ne doit pas pouvoir la reprendre)
                    prises.setBit(hash, true);
                    MultipleCoupCam tmp = coupCam;
                    // On clone mon objet de façons a distinger 2 dirrection possible 
                    // car sont deux coup différent
                    coupCam = (MultipleCoupCam) coupCam.clone();
                    tmp.add(new Position(i + 2, j + 2));

                    // Calcule de la suite
                    coupsPossiblesMange(i + 2, j + 2, coupsPossibles, tmp, prises, pChateauJoueur, cavalierAdv, pionAdv, false);

                    // on ajoute si n'est pas déjà présent
                    if (toAdd) {
                        coupsPossibles.add(tmp);
                    } else {
                        // la suite doit être ajouter car coup différent
                        toAdd = true;
                    }

                    // on enlève la piece de celle prise pour la suite
                    prises.setBit(hash, false);
                }
            }
        }

        // gauche
        if (j - 2 >= 0) {
            // +---+---+---+
            // |   |   |   |
            // +---+---+---+
            // | O | X |   |
            // +---+---+---+
            // |   |   |   |
            // +---+---+---+
            hash = hash(i, j - 1);
            if (!prises.getBit(hash) // Si la pièce que l'on veux tester n'est pas encore prises
                    && (plateau[i][j - 1] == pionAdv || plateau[i][j - 1] == cavalierAdv) // Si la pièce est une pièce adverse
                    && (plateau[i][j - 2] == VIDE || coupCam.getDepart().equals(i, j - 2)) // Si la place après la pièce est disponible (note on ne peut jamais tomber dans un chateau en allant à gauche)
                    ) {
                // on ajoute la piece que l'on vient de prendre (on ne doit pas pouvoir la reprendre)
                prises.setBit(hash, true);

                MultipleCoupCam tmp = coupCam;
                // On clone mon objet de façons a distinger 2 dirrection possible 
                // car sont deux coup différent
                coupCam = (MultipleCoupCam) coupCam.clone();
                tmp.add(new Position(i, j - 2));
                // Calcule de la suite
                coupsPossiblesMange(i, j - 2, coupsPossibles, tmp, prises, pChateauJoueur, cavalierAdv, pionAdv, false);

                // on ajoute si n'est pas déjà présent
                if (toAdd) {
                    coupsPossibles.add(tmp);
                } else {
                    // la suite doit être ajouter car coup différent
                    toAdd = true;
                }

                // on enlève la piece de celle prise pour la suite
                prises.setBit(hash, false);
            }

            // haut
            if (i - 2 >= 0) {
                // +---+---+---+
                // | O |   |   |
                // +---+---+---+
                // |   | X |   |
                // +---+---+---+
                // |   |   |   |
                // +---+---+---+
                hash = hash(i - 1, j - 1);
                if (!prises.getBit(hash) // Si la pièce que l'on veux tester n'est pas encore prises
                        && (plateau[i - 1][j - 1] == pionAdv || plateau[i - 1][j - 1] == cavalierAdv) // Si la pièce est une pièce adverse
                        && (plateau[i - 2][j - 2] == VIDE || coupCam.getDepart().equals(i - 2, j - 2) || (plateau[i - 2][j - 2] == CHATEAU && !pChateauJoueur.equals(i - 2, j - 2))) // Si la place après la pièce est disponible
                        ) {
                    // on ajoute la piece que l'on vient de prendre (on ne doit pas pouvoir la reprendre)
                    prises.setBit(hash, true);

                    MultipleCoupCam tmp = coupCam;
                    // On clone mon objet de façons a distinger 2 dirrection possible 
                    // car sont deux coup différent
                    coupCam = (MultipleCoupCam) coupCam.clone();
                    tmp.add(new Position(i - 2, j - 2));
                    // Calcule de la suite
                    coupsPossiblesMange(i - 2, j - 2, coupsPossibles, tmp, prises, pChateauJoueur, cavalierAdv, pionAdv, false);

                    // on ajoute si n'est pas déjà présent
                    if (toAdd) {
                        coupsPossibles.add(tmp);
                    } else {
                        // la suite doit être ajouter car coup différent
                        toAdd = true;
                    }

                    // on enlève la piece de celle prise pour la suite
                    prises.setBit(hash, false);
                }
            }

            // bas
            if (i + 2 < NBLIGNE) {
                // +---+---+---+
                // |   |   |   |
                // +---+---+---+
                // |   | X |   |
                // +---+---+---+
                // | O |   |   |
                // +---+---+---+
                hash = hash(i + 1, j - 1);
                if (!prises.getBit(hash) // Si la pièce que l'on veux tester n'est pas encore prises
                        && (plateau[i + 1][j - 1] == pionAdv || plateau[i + 1][j - 1] == cavalierAdv) // Si la pièce est une pièce adverse
                        && (plateau[i + 2][j - 2] == VIDE || coupCam.getDepart().equals(i + 2, j - 2) || (plateau[i + 2][j - 2] == CHATEAU && !pChateauJoueur.equals(i + 2, j - 2))) // Si la place après la pièce est disponible
                        ) {
                    // on ajoute la piece que l'on vient de prendre (on ne doit pas pouvoir la reprendre)
                    prises.setBit(hash, true);

                    MultipleCoupCam tmp = coupCam;
                    // On clone mon objet de façons a distinger 2 dirrection possible 
                    // car sont deux coup différent
                    coupCam = (MultipleCoupCam) coupCam.clone();
                    tmp.add(new Position(i + 2, j - 2));
                    // Calcule de la suite
                    coupsPossiblesMange(i + 2, j - 2, coupsPossibles, tmp, prises, pChateauJoueur, cavalierAdv, pionAdv, false);

                    // on ajoute si n'est pas déjà présent
                    if (toAdd) {
                        coupsPossibles.add(tmp);
                    } else {
                        // la suite doit être ajouter car coup différent
                        toAdd = true;
                    }

                    // on enlève la piece de celle prise pour la suite
                    prises.setBit(hash, false);
                }
            }
        }

        // haut
        if (i - 2 >= 0) {
            // +---+---+---+
            // |   | O |   |
            // +---+---+---+
            // |   | X |   |
            // +---+---+---+
            // |   |   |   |
            // +---+---+---+
            hash = hash(i - 1, j);
            if (!prises.getBit(hash) // Si la pièce que l'on veux tester n'est pas encore prises
                    && (plateau[i - 1][j] == pionAdv || plateau[i - 1][j] == cavalierAdv) // Si la pièce est une pièce adverse
                    && (plateau[i - 2][j] == VIDE || coupCam.getDepart().equals(i - 2, j) || (plateau[i - 2][j] == CHATEAU && !pChateauJoueur.equals(i - 2, j))) // Si la place après la pièce est disponible
                    ) {
                // on ajoute la piece que l'on vient de prendre (on ne doit pas pouvoir la reprendre)
                prises.setBit(hash, true);

                MultipleCoupCam tmp = coupCam;
                // On clone mon objet de façons a distinger 2 dirrection possible 
                // car sont deux coup différent
                coupCam = (MultipleCoupCam) coupCam.clone();
                tmp.add(new Position(i - 2, j));
                // Calcule de la suite
                coupsPossiblesMange(i - 2, j, coupsPossibles, tmp, prises, pChateauJoueur, cavalierAdv, pionAdv, false);

                // on ajoute si n'est pas déjà présent
                if (toAdd) {
                    coupsPossibles.add(tmp);
                } else {
                    // la suite doit être ajouter car coup différent
                    toAdd = true;
                }

                // on enlève la piece de celle prise pour la suite
                prises.setBit(hash, false);
            }
        }

        // bas
        if (i + 2 < NBLIGNE) {
            // +---+---+---+
            // |   |   |   |
            // +---+---+---+
            // |   | X |   |
            // +---+---+---+
            // |   | O |   |
            // +---+---+---+
            hash = hash(i + 1, j);
            if (!prises.getBit(hash) // Si la pièce que l'on veux tester n'est pas encore prises
                    && (plateau[i + 1][j] == pionAdv || plateau[i + 1][j] == cavalierAdv) // Si la pièce est une pièce adverse
                    && (plateau[i + 2][j] == VIDE || coupCam.getDepart().equals(i + 2, j) || (plateau[i + 2][j] == CHATEAU && !pChateauJoueur.equals(i + 2, j))) // Si la place après la pièce est disponible
                    ) {
                // on ajoute la piece que l'on vient de prendre (on ne doit pas pouvoir la reprendre)
                prises.setBit(hash, true);

                coupCam.add(new Position(i + 2, j));
                // Calcule de la suite
                coupsPossiblesMange(i + 2, j, coupsPossibles, coupCam, prises, pChateauJoueur, cavalierAdv, pionAdv, false);

                // on ajoute si n'est pas déjà présent
                if (toAdd) {
                    coupsPossibles.add(coupCam);
                }

                // on enlève la piece de celle prise pour la suite
                prises.setBit(hash, false);
            }
        }
    }

    @Override
    public void play(JoueurInterface joueur, CoupJeu c) {
        if (c instanceof SimpleCoupCam) {
            play(joueur, (SimpleCoupCam) c);
        } else if (c instanceof MultipleCoupCam) {
            play(joueur, (MultipleCoupCam) c);
        }
    }

    /**
     *
     * @param joueur
     * @param coupCam
     */
    protected void play(JoueurInterface joueur, SimpleCoupCam coupCam) {
        Position depart = coupCam.getDepart();
        Position arriver = coupCam.getArriver();

        char cavalierAdv = CAVALIER_BLANC, pionAdv = PION_BLANC;

        if (joueur == joueurBlanc) {
            cavalierAdv = CAVALIER_NOIR;
            pionAdv = PION_NOIR;
        }

        int ecartLigne = (depart.getLigne() - arriver.getLigne()) / 2,
                ecartColonne = (depart.getColonne() - arriver.getColonne()) / 2;

        if (ecartLigne != 0 || ecartColonne != 0) {
            char tmp = this.plateau[depart.getLigne() - ecartLigne][depart.getColonne() - ecartColonne];
            // Ma pièce intermédiaire est adverse
            if (tmp == pionAdv || tmp == cavalierAdv) {
                // stock dans coupCam
                // Suppression de la pièce
                coupCam.setPrise(tmp);
                plateau[depart.getLigne() - ecartLigne][depart.getColonne() - ecartColonne] = VIDE;
                // maj du nombre de pièce du joueur
                if (joueur == joueurBlanc) {
                    pieceJoueurNoir.remove(depart.getLigne() - ecartLigne, depart.getColonne() - ecartColonne);
                } else {
                    pieceJoueurBlanc.remove(depart.getLigne() - ecartLigne, depart.getColonne() - ecartColonne);
                }
            }
        }

        if (joueur == joueurBlanc) {
            pieceJoueurBlanc.remove(depart);
            pieceJoueurBlanc.add(arriver);
        } else {
            pieceJoueurNoir.remove(depart);
            pieceJoueurNoir.add(arriver);
        }

        plateau[arriver.getLigne()][arriver.getColonne()] = plateau[depart.getLigne()][depart.getColonne()];
        plateau[depart.getLigne()][depart.getColonne()] = VIDE;
    }

    /**
     *
     * @param joueur
     * @param coupCam
     */
    protected void play(JoueurInterface joueur, MultipleCoupCam coupCam) {
        Position depart = coupCam.getDepart(), arriver = null;

        List<Pair> coups = coupCam.getCoups();
        Position tmpDepart = depart;

        PositionPiece pieces;
        if (joueur == joueurBlanc) {
            pieces = pieceJoueurNoir;
        } else {
            pieces = pieceJoueurBlanc;
        }

        for (Pair pair : coups) {
            arriver = pair.getPosition();
            int ecartLigne = (tmpDepart.getLigne() - arriver.getLigne()) / 2,
                    ecartColonne = (tmpDepart.getColonne() - arriver.getColonne()) / 2;

            pair.setPrise(plateau[tmpDepart.getLigne() - ecartLigne][tmpDepart.getColonne() - ecartColonne]);

            // maj du nombre de pièce du joueur
            pieces.remove(tmpDepart.getLigne() - ecartLigne, tmpDepart.getColonne() - ecartColonne);
            plateau[tmpDepart.getLigne() - ecartLigne][tmpDepart.getColonne() - ecartColonne] = VIDE;
            tmpDepart = arriver;
        }

        if (!depart.equals(arriver)) {
            if (joueur == joueurBlanc) {
                pieceJoueurBlanc.remove(depart);
                pieceJoueurBlanc.add(arriver);
            } else {
                pieceJoueurNoir.remove(depart);
                pieceJoueurNoir.add(arriver);
            }

            plateau[arriver.getLigne()][arriver.getColonne()] = plateau[depart.getLigne()][depart.getColonne()];
            plateau[depart.getLigne()][depart.getColonne()] = VIDE;
        }
    }

    @Override
    public void play(String move, String player) {
        play(parsePlayer(player), parseCoup(move));
    }

    /**
     *
     * @param joueur
     * @param move
     */
    public void play(JoueurInterface joueur, String move) {
        play(joueur, parseCoup(move));
    }

    @Override
    public void unPlay(JoueurInterface joueur, CoupJeu c) {
        if (c instanceof SimpleCoupCam) {
            unPlay(joueur, (SimpleCoupCam) c);
        } else {
            unPlay(joueur, (MultipleCoupCam) c);
        }
    }

    /**
     *
     * @param joueur
     * @param coupCam
     */
    public void unPlay(JoueurInterface joueur, MultipleCoupCam coupCam) {
        Position depart = coupCam.getDepart(), arriver = null;

        List<Pair> coups = coupCam.getCoups();
        Position tmpDepart = depart;

        List<Position> pieces;
        if (joueur == joueurBlanc) {
            pieces = pieceJoueurNoir;
        } else {
            pieces = pieceJoueurBlanc;
        }

        for (Pair pair : coups) {
            arriver = pair.getPosition();
            int ecartLigne = (tmpDepart.getLigne() - arriver.getLigne()) / 2,
                    ecartColonne = (tmpDepart.getColonne() - arriver.getColonne()) / 2;

            // maj du nombre de pièce du joueur
            pieces.add(new Position(tmpDepart.getLigne() - ecartLigne, tmpDepart.getColonne() - ecartColonne));
            plateau[tmpDepart.getLigne() - ecartLigne][tmpDepart.getColonne() - ecartColonne] = pair.getPrise();
            tmpDepart = arriver;
        }

        if (!depart.equals(arriver)) {
            if (joueur == joueurBlanc) {
                pieceJoueurBlanc.remove(arriver);
                pieceJoueurBlanc.add(depart);
            } else {
                pieceJoueurNoir.remove(arriver);
                pieceJoueurNoir.add(depart);
            }

            plateau[depart.getLigne()][depart.getColonne()] = plateau[arriver.getLigne()][arriver.getColonne()];
            if (arriver.equals(CHATEAU_BLANC) || arriver.equals(CHATEAU_NOIR)) {
                plateau[arriver.getLigne()][arriver.getColonne()] = CHATEAU;
            } else {
                plateau[arriver.getLigne()][arriver.getColonne()] = VIDE;
            }
        }
    }

    /**
     *
     * @param joueur
     * @param coupCam
     */
    public void unPlay(JoueurInterface joueur, SimpleCoupCam coupCam) {
        Position depart = coupCam.getDepart();
        Position arriver = coupCam.getArriver();

        int ecartLigne = (depart.getLigne() - arriver.getLigne()) / 2,
                ecartColonne = (depart.getColonne() - arriver.getColonne()) / 2;

        if (ecartLigne != 0 || ecartColonne != 0) {
            Character tmp = coupCam.getPrise();
            // Ma pièce intermédiaire est adverse
            if (tmp != null) {
                plateau[depart.getLigne() - ecartLigne][depart.getColonne() - ecartColonne] = tmp;
                // maj du nombre de pièce du joueur
                if (joueur == joueurBlanc) {
                    pieceJoueurNoir.add(new Position(depart.getLigne() - ecartLigne, depart.getColonne() - ecartColonne));
                } else {
                    pieceJoueurBlanc.add(new Position(depart.getLigne() - ecartLigne, depart.getColonne() - ecartColonne));
                }
            }
        }

        if (joueur == joueurBlanc) {
            pieceJoueurBlanc.remove(arriver);
            pieceJoueurBlanc.add(depart);
        } else {
            pieceJoueurNoir.remove(arriver);
            pieceJoueurNoir.add(depart);
        }

        plateau[depart.getLigne()][depart.getColonne()] = plateau[arriver.getLigne()][arriver.getColonne()];
        if (arriver.equals(CHATEAU_BLANC) || arriver.equals(CHATEAU_NOIR)) {
            plateau[arriver.getLigne()][arriver.getColonne()] = CHATEAU;
        } else {
            plateau[arriver.getLigne()][arriver.getColonne()] = VIDE;
        }
    }

    @Override
    public boolean finDePartie(JoueurInterface joueur) {
        // nb de pions = 0 | une piece adverse est sur un chateau
        boolean fin = getNbPieceBlanc() == 0 || getNbPieceNoir() == 0
                || plateau[CHATEAU_BLANC.getLigne()][CHATEAU_BLANC.getColonne()] != CHATEAU
                || plateau[CHATEAU_NOIR.getLigne()][CHATEAU_NOIR.getColonne()] != CHATEAU;

        // joueur dont c'est le tour ne peut pas faire de mouvement
        if (!fin) {
            fin = coupsPossibles(joueur).isEmpty(); // TODO optimisation
        }

        return fin;
    }

    /**
     *
     * @return
     */
    public JoueurInterface getGagant() {
        JoueurInterface winner = null;

        if (0 == getNbPieceBlanc() || plateau[CHATEAU_BLANC.getLigne()][CHATEAU_BLANC.getColonne()] != CHATEAU) {
            winner = joueurNoir;
        } else if (0 == getNbPieceNoir() || plateau[CHATEAU_NOIR.getLigne()][CHATEAU_NOIR.getColonne()] != CHATEAU) {
            winner = joueurBlanc;
        }

        return winner;
    }

    /**
     *
     * @param joueur
     * @param c
     * @return
     */
    @Override
    public boolean estValide(JoueurInterface joueur, CoupJeu c) {
        return coupsPossibles(joueur).contains(c);
    }

    public boolean estValide(JoueurInterface joueur, String move) {
        return estValide(joueur, parseCoup(move));
    }

    /**
     *
     * @param move
     * @param player
     * @return
     */
    @Override
    public boolean estValide(String move, String player) {
        return estValide(parsePlayer(player), parseCoup(move));
    }

    /**
     *
     * @param move
     * @return
     */
    protected CoupJeu parseCoup(String move) {
        String tab[] = move.split("-");

        return (tab.length < 2 ? null : (tab.length > 2 ? new MultipleCoupCam(tab) : new SimpleCoupCam(tab)));
    }

    /**
     *
     * @param player
     * @return
     */
    protected JoueurInterface parsePlayer(String player) {
        return (player.equalsIgnoreCase("noir") ? joueurNoir : joueurBlanc);
    }

    @Override
    public String toString() {
        String str = "%  A B C D E F G\n";
        for (int i = 0; i < NBLIGNE; i++) {
            String numLigne = ((i + 1) < 10 ? "0" + (i + 1) : String.valueOf(i + 1));
            str += numLigne + " ";
            for (int j = 0; j < NBCOLONNE; j++) {
                str += plateau[i][j] + " ";
            }
            str += numLigne + "\n";
        }
        str += "%  A B C D E F G";
        return str;
    }

    @Override
    public void setFromFile(String fileName) {
        pieceJoueurBlanc = new PositionPiece();
        pieceJoueurNoir = new PositionPiece();

        try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)))) {
            String ligne;
            int i = 0;
            while ((ligne = in.readLine()) != null) {
                if (!ligne.equals("") && !ligne.startsWith("%")) {
                    ligne = ligne.replaceAll("[0-9]|\t| ", "");
                    int k = 0;
                    while (this.plateau[i][k] == ' ') {
                        k++;
                    }
                    for (int j = 0; j < ligne.length(); j++) {
                        char c = ligne.charAt(j);
                        char clc = Character.toLowerCase(c);
                        if (clc == PION_NOIR) {
                            pieceJoueurNoir.add(new Position(i, k + j));
                        } else if (clc == PION_BLANC) {
                            pieceJoueurBlanc.add(new Position(i, k + j));
                        }
                        this.plateau[i][k + j] = c;
                    }
                    i++;
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PlateauCam.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PlateauCam.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void saveToFile(String fileName) {
        File outFile = new File(fileName);
        if (outFile.exists()) {
            Logger.getLogger(PlateauCam.class.getName()).log(Level.WARNING, "File already exists, and will be erased.");
        }

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(outFile)))) {
            out.println(this);
        } catch (IOException ex) {
            Logger.getLogger(PlateauCam.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void saveToFile(FileWriter writer) {
        (new PrintWriter(new BufferedWriter(writer))).println(this);
    }

    /**
     * Hachage d'une position donnée
     *
     * @param i ligne de la position
     * @param j colonne de la position
     * @return hash
     */
    protected int hash(int i, int j) {
        return NBLIGNE * j + i;
    }

    /**
     * Hachage d'une position donnée
     *
     * @param p position a hacher
     * @return le hach
     */
    protected int hash(Position p) {
        return hash(p.getLigne(), p.getColonne());
    }

    /**
     * Obtenir le nombre de pions que possède le joueur blanc.
     *
     * @return
     */
    public int getNbPieceBlanc() {
        return pieceJoueurBlanc.size();
    }

    /**
     * Obtenir le nombre de pions que possède le joueur noir.
     *
     * @return
     */
    public int getNbPieceNoir() {
        return pieceJoueurNoir.size();
    }
}
