package jeux.cam.modele;

/**
 *
 * @author godeau & pannirselvame
 */
public class Position {

    private final static int REF = (int) 'A';

    private final int ligne;
    private final int colonne;

    public Position(int ligne, int colonne) {
        this.ligne = ligne;
        this.colonne = colonne;
    }

    public Position(String position) {
        /* recupération de la ligne (?..) */            /* recupértion de la colonne (.??) */

        this(Integer.valueOf(position.substring(1)) - 1, ((int) Character.toUpperCase(position.charAt(0))) - REF);
    }

    public int getLigne() {
        return ligne;
    }

    public int getColonne() {
        return colonne;
    }

    @Override
    public String toString() {
        return Character.toString((char) (REF + colonne)) + (ligne + 1);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Position) {
            return this.colonne == ((Position) obj).colonne && this.ligne == ((Position) obj).ligne;
        } else if (obj instanceof String) {
            return this.equals(new Position(((String) obj)));
        } else {
            return super.equals(obj);
        }
    }

    public boolean equals(int ligne, int colonne) {
        return this.ligne == ligne && this.colonne == colonne;
    }
}
