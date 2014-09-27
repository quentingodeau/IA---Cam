//package cam;
//
//import iia.jeux.alg.AlgoJeu;
//import iia.jeux.alg.AlphaBeta;
//import iia.jeux.alg.Heuristique;
//import iia.jeux.modele.CoupJeu;
//import iia.jeux.modele.joueur.Humain;
//import iia.jeux.modele.joueur.Joueur;
//import java.util.LinkedList;
//import java.util.Scanner;
//import jeux.cam.HeuristiqueCam;
//import jeux.cam.PlateauCam;
//
///**
// * main
// *
// * @author godeau & pannirselvame
// */
//public class Main {
//
//    /**
//     * @param args the command line arguments
//     */
//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        Joueur noir, blanc;
//        String rep;
//
//        // Création du joueur noir
//        System.out.print("Le joueur noir est [Humain/IA] : ");
//        System.out.flush();
//        rep = scanner.nextLine();
//
//        System.out.print("Veuillez saisir le nom du joueur noir : ");
//        System.out.flush();
//        if (rep.equalsIgnoreCase("ia")) {
//            noir = new Joueur(scanner.nextLine());
//        } else {
//            noir = new Humain(scanner.nextLine());
//            System.out.print("Voulez voir les coups possibles avant de jouer (Y/n) ? ");
//            System.out.flush();
//            ((Humain) noir).setVoirCoups(!scanner.nextLine().equalsIgnoreCase("n"));
//        }
//
//        // Création du joueur blanc 
//        System.out.print("Le joueur blanc est [Humain/IA] : ");
//        System.out.flush();
//        rep = scanner.nextLine();
//
//        System.out.print("Veuillez saisir le nom du joueur blanc : ");
//        System.out.flush();
//        if (rep.equalsIgnoreCase("ia")) {
//            blanc = new Joueur(scanner.nextLine());
//        } else {
//            blanc = new Humain(scanner.nextLine());
//            System.out.print("Voulez voir les coups possibles avant de jouer (Y/n) ? ");
//            System.out.flush();
//            ((Humain) blanc).setVoirCoups(!scanner.nextLine().equalsIgnoreCase("n"));
//        }
//
//        // Lancement de la partie
//        PlateauCam plateauCam = new PlateauCam(noir, blanc);
//        Heuristique heuristique = new HeuristiqueCam();
//        Joueur joueurs[] = {blanc, noir};
//        AlgoJeu algoJeu[] = {
//            new AlphaBeta(heuristique, blanc, noir, 5),
//            new AlphaBeta(heuristique, noir, blanc, 6)
//        };
//        int tour = 1;
//
//        while (!plateauCam.finDePartie(joueurs[tour])) {
//            System.out.println("C'est au tour de " + joueurs[tour]);
//            System.out.println(plateauCam);
//
//            if (joueurs[tour] instanceof Humain) {
//                if (((Humain) joueurs[tour]).getVoirCoups()) {
//                    LinkedList<CoupJeu> coupsPossibles = (LinkedList<CoupJeu>) plateauCam.coupsPossibles(joueurs[tour]);
//                    System.out.println("Les coups possibles sont :");
//                    int i = 1;
//                    for (CoupJeu coupJeu : coupsPossibles) {
//                        System.out.println(i + " - " + coupJeu);
//                        i++;
//
//                    }
//
//                    System.out.print("Veuillez choisir votre coup : ");
//                    System.out.flush();
//                    i = -1;
//                    while (i < 0 || i >= coupsPossibles.size()) {
//                        i = scanner.nextInt() - 1;
//                    }
//
//                    plateauCam.play(joueurs[tour], coupsPossibles.get(i));
//                } else {
//                    System.out.print("Veuillez saisir votre coup : ");
//                    System.out.flush();
//                    rep = scanner.nextLine();
//                    if (!plateauCam.estValide(joueurs[tour], rep)) {
//                        System.out.print("Coup invalide veuillez recommencer : ");
//                        System.out.flush();
//                        rep = scanner.nextLine();
//                    }
//
//                    plateauCam.play(joueurs[tour], rep);
//                }
//            } else {
//                // Le joueur IA joue
//                plateauCam.play(joueurs[tour], algoJeu[tour].meilleurCoup(plateauCam));
//            }
//
//            tour = (tour == 0 ? 1 : 0);
//        }
//        System.out.println(plateauCam);
//
//        IJoueur gagant = plateauCam.getGagant();
//        if (gagant == null) {
//            System.out.println("Match nul");
//        } else {
//            System.out.println("Victoire de " + gagant);
//        }
//    }
//}
