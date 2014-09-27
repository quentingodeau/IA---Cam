//package cam;
//
//import iia.jeux.modele.joueur.Joueur;
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.StringTokenizer;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import jeux.cam.PlateauCam;
//
///**
// *
// * @author godeau
// */
//public class Server extends Thread {
//
//    final static int BLANC = 0;
//    final static int NOIR = 1;
//
//    Socket socketBlanc;
//    Socket socketNoir;
//    File logFile;
//
//    public Server(Socket socketBlanc, Socket socketNoir) {
//        this.socketBlanc = socketBlanc;
//        this.socketNoir = socketNoir;
//
//        logFile = new File("/tmp/" + System.currentTimeMillis() + ".log");
//        while (logFile.exists()) {
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException ex) {
//                Logger.getLogger(Server.class.getName()).log(Level.WARNING, null, ex);
//            }
//            logFile = new File("/tmp/" + System.currentTimeMillis() + ".log");
//        }
//
//        Logger.getLogger(Server.class.getName()).log(Level.INFO, "{0}: Log file is located at {1}", new Object[]{getId(), logFile.getAbsolutePath()});
//    }
//
//    @Override
//    public void run() {
//        Logger logger = Logger.getLogger(Server.class.getName());
//        try (PrintWriter outFile = new PrintWriter(new BufferedWriter(new FileWriter(logFile, true)))) {
//            PrintWriter outBlanc = new PrintWriter(socketBlanc.getOutputStream(), true);
//            PrintWriter outNoir = new PrintWriter(socketNoir.getOutputStream(), true);
//
//            BufferedReader inBlanc = new BufferedReader(new InputStreamReader(socketBlanc.getInputStream()));
//            BufferedReader inNoir = new BufferedReader(new InputStreamReader(socketNoir.getInputStream()));
//
//            Joueur joueurs[] = new Joueur[2];
//            // Réception du nom des joueurs
//            joueurs[NOIR] = new Joueur(new StringTokenizer(inNoir.readLine(), "\n").nextToken());
//            joueurs[BLANC] = new Joueur(new StringTokenizer(inBlanc.readLine(), "\n").nextToken());
//
//            // envois de la couleur au joueurs
//            outBlanc.println("Blanc");
//            outNoir.println("Noir");
//
//            // Enregistrement dans le fichier
//            outFile.println("% NOIR: " + joueurs[NOIR]);
//            outFile.println("% BLANC: " + joueurs[BLANC]);
//            outFile.println();
//
//            // Création du plateau
//            PlateauCam plateauCam = new PlateauCam(joueurs[NOIR], joueurs[BLANC]);
//            boolean finDePartie = false;
//            int tour = NOIR;
//            String mouvement = null;
//            Integer winner = null;
//
//            while (!finDePartie) {
//                outFile.println(plateauCam);
//
//                if (tour == NOIR) {
//                    logger.log(Level.INFO, "{0}: C''est au tour de {1} [Noir]", new Object[]{getId(), joueurs[tour]});
//                    // On informe que c'est au joueur noir de jouer
//                    outNoir.println("JOUEUR Noir");
//                    long start = System.currentTimeMillis();
//                    // On récupère sont coup
//                    mouvement = new StringTokenizer(inNoir.readLine(), " \n").nextToken();
//                    long end = System.currentTimeMillis();
//                    
//                    String temps = new SimpleDateFormat("mm:ss.SSS").format(new Date(end - start));
//                    
//                    logger.log(Level.INFO, "{0}: {1} [Noir] joue {2} trouver en {3}", new Object[]{getId(), joueurs[tour], mouvement, temps});
//                    // Si le coup est valide
//                    if (plateauCam.estValide(joueurs[tour], mouvement)) {
//                        logger.log(Level.INFO, "{0}: Le coup de {1} [Noir] est valide", new Object[]{getId(), joueurs[tour]});
//                        // On joue sont coup
//                        plateauCam.play(joueurs[tour], mouvement);
//                        outFile.println("% [Noir] " + joueurs[tour] + " " + mouvement + " en " + temps);
//                        finDePartie = plateauCam.finDePartie(joueurs[BLANC]);
//                        // Si la partie n'est pas terminé
//                        if (!finDePartie) {
//                            // On envois le coup a l'autre joueur
//                            outBlanc.println("MOUVEMENT " + mouvement);
//                        }
//                        tour = BLANC;
//                    } else {
//                        logger.log(Level.WARNING, "{0}: Le coup de {1} [Noir] n''est pas valide", new Object[]{getId(), joueurs[tour]});
//                        // Le coup du joueur n'est pas valide, victoire par défaut
//                        finDePartie = true;
//                        winner = BLANC;
//                    }
//                } else {
//                    logger.log(Level.INFO, "{0}: C''est au tour de {1} [Blanc]", new Object[]{getId(), joueurs[tour]});
//                    // On informe que c'est au joueur noir de jouer
//                    outBlanc.println("JOUEUR Blanc");
//                    long start = System.currentTimeMillis();
//                    // On récupère sont coup
//                    mouvement = new StringTokenizer(inBlanc.readLine(), " \n").nextToken();
//                    long end = System.currentTimeMillis();
//                    
//                    String temps = new SimpleDateFormat("mm:ss.SSS").format(new Date(end - start));
//                    
//                    logger.log(Level.INFO, "{0}: {1} [Blanc] joue {2} trouver en {3}", new Object[]{getId(), joueurs[tour], mouvement, temps});
//                    // Si le coup est valide
//                    if (plateauCam.estValide(joueurs[tour], mouvement)) {
//                        logger.log(Level.INFO, "{0}: Le coup de {1} [Blanc] est valide", new Object[]{getId(), joueurs[tour]});
//                        // On joue sont coup
//                        plateauCam.play(joueurs[tour], mouvement);
//                        outFile.println("% [Blanc] " + joueurs[tour] + " " + mouvement + " en " + temps);
//                        finDePartie = plateauCam.finDePartie(joueurs[NOIR]);
//                        // Si la partie n'est pas terminé
//                        if (!finDePartie) {
//                            // On envois le coup a l'autre joueur
//                            outNoir.println("MOUVEMENT " + mouvement);
//                        }
//                        tour = NOIR;
//                    } else {
//                        logger.log(Level.WARNING, "{0}: Le coup de {1} [Blanc] n''est pas valide", new Object[]{getId(), joueurs[tour]});
//                        // Le coup du joueur n'est pas valide, victoire par défaut
//                        finDePartie = true;
//                        winner = BLANC;
//                    }
//                }
//                
//                outFile.println();
//            }
//
//            if (winner != null) {
//                outFile.println("% Victoire par défaut de " + joueurs[winner] + " a cause du mouvement " + mouvement);
//                if (winner == BLANC) {
//                    outBlanc.println("FIN! Blanc");
//                    outNoir.println("FIN! Blanc");
//                } else {
//                    outBlanc.println("FIN! Noir");
//                    outNoir.println("FIN! Noir");
//                }
//            } else {
//                IJoueur gagant = plateauCam.getGagant();
//                if (gagant != null) {
//                    if (gagant == joueurs[BLANC]) {
//                        outBlanc.println("FIN! Blanc");
//                        outNoir.println("FIN! Blanc");
//                        winner = BLANC;
//                    } else {
//                        outBlanc.println("FIN! Noir");
//                        outNoir.println("FIN! Noir");
//                        winner = NOIR;
//                    }
//                    outFile.println("% Victoire de " + joueurs[winner]);
//                } else {
//                    outBlanc.println("FIN!");
//                    outNoir.println("FIN!");
//                    outFile.println("% Match nul");
//                }
//            }
//
//        } catch (IOException ex) {
//            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
//        } finally {
//            try {
//                socketBlanc.close();
//                socketNoir.close();
//            } catch (IOException ex) {
//                //Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//    }
//
//    public static void main(String[] args) {
//        if (args.length < 1) {
//            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, "You must indicate the port");
//            System.exit(1);
//        }
//        
//        Logger.getLogger(Server.class.getName()).log(Level.INFO, "Server listen on {0}.", args[0]);
//        Integer port = Integer.parseInt(args[0]);
//        Socket players[] = new Socket[2];
//        int nbPlayer = 0;
//        try (ServerSocket socket = new ServerSocket(port)) {
//            while (true) {
//                players[nbPlayer] = socket.accept();
//                Logger.getLogger(Server.class.getName()).log(Level.INFO, "A new client is connected.");
//                nbPlayer++;
//                if (nbPlayer == 2) {
//                    Logger.getLogger(Server.class.getName()).log(Level.INFO, "Two clients are connected the game can start.");
//                    new Server(players[0], players[1]).start();
//                    nbPlayer = 0;
//                }
//            }
//        } catch (IOException ex) {
//            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//}
