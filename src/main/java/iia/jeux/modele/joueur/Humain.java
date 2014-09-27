package iia.jeux.modele.joueur;

/**
 *
 * @author godeau & pannirselvame
 */
public class Humain extends Joueur {

    private boolean voirCoups;

    public Humain(String name) {
        this(name, true);
    }

    public Humain(String nom, boolean voirCoups) {
        super(nom);
        this.voirCoups = voirCoups;
    }

    public boolean getVoirCoups() {
        return voirCoups;
    }

    public void setVoirCoups(boolean voirCoups) {
        this.voirCoups = voirCoups;
    }
}
