package cam;

import cam.modele.IJoueur;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;

/**
 * Cette classe permet de charger dynamiquement une classe de joueur, qui doit obligatoirement
 * implanter l'interface IJoueur. Vous lui donnez aussi en argument le nom de la machine distante
 * (ou "localhost") sur laquelle le serveur de jeu est lancé, ainsi que le port sur lequel la
 * machine écoute.
 * 
 * Exemple: >java -cp . cam.ClientJeu cam.joueurProf localhost 1234
 * 
 * Le client s'occupe alors de tout en lançant les méthodes implantées de l'interface IJoueur. Toute
 * la gestion réseau est donc cachée.
 * 
 * @author L. Simon (Univ. Paris-Sud)- 2006-2008
 * @see IJoueur
 */
public class ClientJeu {

    // Mais pas lors de la conversation avec l'arbitre
    // Vous pouvez changer cela en interne si vous le souhaitez
    static final int BLANC = IJoueur.BLANC;
    static final int NOIR = IJoueur.NOIR;
    static final int VIDE = -1;

    /**
     * @param args
     *            Dans l'ordre : NomClasseJoueur MachineServeur PortEcoute
     */
    public static void main(String[] args) {

	if (args.length < 3) {
	    System.err.println("ClientJeu Usage: NomClasseJoueur MachineServeur PortEcoute");
	    System.exit(1);
	}

	// Le nom de la classe joueur à charger dynamiquement
	String classeJoueur = args[0];
	// Le nom de la machine serveur a été donné en ligne de commande
	String serverMachine = args[1];
	// Le numéro du port sur lequel on se connecte a aussi été donné
	int portNum = Integer.parseInt(args[2]);

	System.out.println("Le client se connectera sur " + serverMachine + ":" + portNum);

	Socket clientSocket = null;
	IJoueur joueur;
	String msg, firstToken;
	// permet d'analyser les chaines de caractres lues
	StringTokenizer msgTokenizer;
	// C'est la couleur qui doit jouer le prochain coup
	int couleurAJouer;
	// C'est ma couleur (quand je joue)
	int maCouleur;

	boolean jeuTermine = false;

	try {
	    // initialise la socket
	    clientSocket = new Socket(serverMachine, portNum);
	    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
	    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

	    // *****************************************************
	    System.out.print("Chargement de la classe joueur " + classeJoueur + "... ");
	    Class<?> cjoueur = Class.forName(classeJoueur);
	    joueur = (IJoueur) cjoueur.newInstance();
	    System.out.println("Ok");
	    // ****************************************************

	    // Envoie de l'identifiant de votre quadrinome.
	    out.println(joueur.binoName());
	    System.out.println("Mon nom de quadrinome envoyé est " + joueur.binoName());

	    // Récupre le message sous forme de chaine de caractres
	    msg = in.readLine();
	    System.out.println(msg);

	    // Lit le contenu du message, toutes les infos du message
	    msgTokenizer = new StringTokenizer(msg, " \n\0");
	    if ((msgTokenizer.nextToken()).equals("Blanc")) {
		System.out.println("Je suis Blanc, c'est à moi de jouer.");
		maCouleur = BLANC;
	    } else { // doit etre égal à "Noir"
		System.out.println("Je suis Noir, j'attends le mouvement de Blanc.");
		maCouleur = NOIR;
	    }

	    // permet d'initialiser votre joueur avec sa couleur
	    joueur.initJoueur(maCouleur);

	    // boucle générale de jeu
	    do {

		// Lire le msg à partir du serveur
		msg = in.readLine();

		msgTokenizer = new StringTokenizer(msg, " \n\0");
		firstToken = msgTokenizer.nextToken();

		if (firstToken.equals("FIN!")) {

		    jeuTermine = true;
		    String theWinnerIs = msgTokenizer.nextToken();
		    if (theWinnerIs.equals("Blanc")) {
			couleurAJouer = BLANC;
		    } else {
			if (theWinnerIs.equals("Noir"))
			    couleurAJouer = NOIR;
			else
			    couleurAJouer = VIDE;
		    }

		    if (couleurAJouer == maCouleur)
			System.out.println("J'ai gagné!");

		    joueur.declareLeVainqueur(couleurAJouer);

		} else if (firstToken.equals("JOUEUR")) {

		    // On demande au joueur de jouer
		    if ((msgTokenizer.nextToken()).equals("Blanc")) {
			couleurAJouer = BLANC;
		    } else {
			couleurAJouer = NOIR;
		    }

		    if (couleurAJouer == maCouleur) {
			// On appelle la classe du joueur pour choisir un mouvement
			msg = joueur.choixMouvement();
			out.println(msg);
		    }
		} else if (firstToken.equals("MOUVEMENT")) {

		    // On lit ce que joue le joueur et on l'envoie à l'autre
		    joueur.mouvementEnnemi(msgTokenizer.nextToken());
		}
	    } while (!jeuTermine);

	} catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
	    System.out.println(e);
	}
    }
}