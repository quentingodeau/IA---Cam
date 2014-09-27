package iia.jeux.modele.joueur;


public class Joueur  implements JoueurInterface {

    private String nom;

    /**
     *
     * @param nom
     */
    public Joueur(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public String toString() {
        return nom;
    }
}
